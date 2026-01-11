package com.example.chat.global.websocket;

import com.example.chat.channel.domain.ChatMessageType;
import com.example.chat.channel.dto.ChatMessageResponse;
import com.example.chat.channel.entity.Channel;
import com.example.chat.channel.entity.ChatMessage;
import com.example.chat.channel.service.ChatMessageService;
import com.example.chat.global.exception.CustomException;
import com.example.chat.global.exception.ErrorCode;
import com.example.chat.group.entity.Group;
import com.example.chat.group.service.GroupService;
import com.example.chat.groupmember.entity.GroupMember;
import com.example.chat.groupmember.repository.GroupMemberRepository;
import com.example.chat.groupmember.service.GroupMemberService;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.service.CustomUserDetails;
import com.example.chat.user.service.UserService;
import com.example.chat.groupmember.dto.GroupMemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    // 어떤 방에, 어떤 유저가 있는지 저장
    // 멀티스레드 환경에서 안전하게 concurrentHashmap 사용
    private static final Map<Long, Set<GroupMemberResponse>> CHANNEL_USERS = new ConcurrentHashMap<>();

    // 어떤 세션ID가 어떤 방을 보고있는지 저장(퇴장 처리용)
    private static final Map<String, Long> SESSION_CHANNEL = new ConcurrentHashMap<>();

    private static final Pattern FIRST_SUBSCRIBE = Pattern.compile("/sub/channel/(\\d+)$");
    private static final Pattern VISITOR_REGEX = Pattern.compile("/sub/channel/(\\d+)/visitors$");
    private final GroupMemberService groupMemberService;

    @EventListener
    public void sessionSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String destination = headerAccessor.getDestination();

        // 세션 만료 검사
        Principal user = headerAccessor.getUser();
        if (user == null) {
            log.info("세션 만료");
            return;
        }
        
        // 채팅방 구독이 아니면 무시
        if (destination == null || !destination.startsWith("/sub/channel")) {
            log.info("채팅방 구독이 아님.");
            return;
        }

        // 이건 한번 들어왔을때 날리면됨. && groupmember가 아닐때
        Matcher firstSubscribe = FIRST_SUBSCRIBE.matcher(destination);
        if (firstSubscribe.matches()) {
            log.info("처음 구독");
            Long channelId = Long.parseLong(firstSubscribe.group(1));

            UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) user;
            CustomUserDetails userDetails = (CustomUserDetails) authenticationToken.getPrincipal();

            GroupMember groupMember = groupMemberService.getGroupMemberWithChannelId(userDetails.getId(), channelId);
            GroupMemberResponse groupMemberResponse = GroupMemberResponse.from(groupMember);    // siteUser 칼럼 참조

            String sessionId = headerAccessor.getSessionId();
            CHANNEL_USERS.computeIfAbsent(channelId, k -> ConcurrentHashMap.newKeySet()).add(groupMemberResponse);
            SESSION_CHANNEL.put(sessionId, channelId);
            log.info("User Entered : {} -> Channel {}", groupMemberResponse.toString(), channelId);

            /*String[] welcomeMessages = {
                    "님을 환영해요. 피자는 가져오셨겠죠?",
                    "님이 착륙했습니다.",
                    "님이 서버에 들어왔어요. 다들 박수!",
                    "님이 야생에서 나타났습니다.",
                    "님이 오셨어요. 환영해주세요!"
            };
            String randomMsg = welcomeMessages[new Random().nextInt(welcomeMessages.length)];
            ChatMessage saveSystemMessage = chatMessageService.saveSystemMessage(channelId,
                    groupMember.getSiteUser().getId(),
                    randomMsg,
                    ChatMessageType.ENTER);
            ChatMessageResponse welcomeMsg = ChatMessageResponse.from(saveSystemMessage);
            messagingTemplate.convertAndSend("/sub/channel/" + channelId, welcomeMsg);*/

            sendVisitorDtosToChannel(channelId);
        } else {
            Matcher visitorRegex = VISITOR_REGEX.matcher(destination);

            if (visitorRegex.matches()) {
                log.info("방문자 구독");
                Long channelId = Long.parseLong(visitorRegex.group(1));
                sendVisitorDtosToChannel(channelId);
            }
        }
    }

    // 1. 연결 감지 (입장)
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        // 누가 들어왔는지 로그 찍어보기
        log.info("유저가 서버와 소켓 연결을 맺음.");
    }

    // 2. 연결 해제 감지 (퇴장)
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // 누가 나갔는지 확인하고, 같은 방 사람들에게 "00님 나감" 알려주기
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        Long channelId = SESSION_CHANNEL.get(sessionId);
        if (channelId == null) {
            log.info("채널 없음.");
            return;
        }

        Principal user = headerAccessor.getUser();

        if (user == null) {
            log.info("세션 만료");
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) user;
        CustomUserDetails userDetails = (CustomUserDetails) authenticationToken.getPrincipal();

        GroupMemberResponse targetToRemove = GroupMemberResponse.builder()
                .siteUserId(userDetails.getId())
                .profile(null)
                .nickname(null)
                .build();

        Set<GroupMemberResponse> visitors = CHANNEL_USERS.get(channelId);

        if (visitors != null) {
            boolean removed = visitors.remove(targetToRemove);

            if (removed) {
                log.info("User Left : {} -> Channel {}", userDetails.getNickname(), channelId);

                sendVisitorDtosToChannel(channelId);

                if (visitors.isEmpty()) {
                    CHANNEL_USERS.remove(channelId);
                }
            }
        }

        // 세션채널에서 세션아이디삭제
        SESSION_CHANNEL.remove(sessionId);
    }

    /* 방문자 명단 구독자 전체에게 발송 */
    private void sendVisitorDtosToChannel(Long channelId) {
        Set<GroupMemberResponse> visitors = CHANNEL_USERS.get(channelId);

        log.info("channelId : {}", channelId);

        if (visitors == null) {
            log.info("방문자 없음.");
            return;
        }

        messagingTemplate.convertAndSend("/sub/channel/" + channelId + "/visitors", visitors);
    }
}
