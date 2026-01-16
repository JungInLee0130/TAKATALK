package com.example.chat.groupmember.domain.aop;

import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.groupmember.domain.annotation.RequireGroupRole;
import com.example.chat.groupmember.entity.GroupMember;
import com.example.chat.groupmember.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GroupRoleAspect {
    private final GroupMemberRepository groupMemberRepository;

    @Before("@annotation(requireGroupRole)")
    public void checkGroupRole(JoinPoint joinPoint, RequireGroupRole requireGroupRole) {
        // 1. 현재 로그인한 유저의 PK(ID)가져오기
        String principalName = SecurityContextHolder.getContext().getAuthentication().getName();
        Long currentUserId = Long.parseLong(principalName);

        // 2. groupId를 method parameter로 부터 가져옴
        Long groupId = findGroupIdInArgs(joinPoint);

        // 3. DB 권한 체크
        validateGroupMemberManagerRole(groupId, currentUserId);
    }

    /* groupId를 method parameter로 부터 가져옴 */
    private Long findGroupIdInArgs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            if ("groupId".equals(parameterNames[i])) {
                return (Long) args[i];
            }
        }
        throw new IllegalArgumentException("groupId 파라미터가 없음.");
    }


    // 권한 체크 공통로직 : DB GroupMember 존재 여부 체크
    // 그룹아이디와 유저ID로 그룹 멤버 찾기
    public void validateGroupMemberManagerRole(Long groupId, Long siteUserId) {
        GroupMember member = groupMemberRepository.findByGroupIdAndSiteUserId(groupId, siteUserId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_MEMBER_NOT_FOUND));
        member.validateManagerRole();
    }
}
