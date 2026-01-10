package com.example.chat.groupmember.repository;

import com.example.chat.group.entity.Group;
import com.example.chat.groupmember.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    @Query("select distinct m.group " +
            "from GroupMember m " +
            "join m.group " +
            "where m.siteUser.id = :siteUserId ")   // fetch 적용, join 명시
    List<Group> findGroupBySiteUserId (@Param("siteUserId") Long siteUserId);

    Boolean existsByGroupIdAndSiteUserId(Long groupId, Long siteUserId);

    Optional<GroupMember> findByGroupIdAndSiteUserId(Long groupId, Long siteUserId);

    @Query("select distinct m " +
            "from GroupMember m " +
            "inner join fetch m.siteUser u " +
            "where u.id = :siteUserId ")
    Optional<GroupMember> findByIdWithUser(@Param("siteUserId") Long siteUserId);

    // @Param은 명시가 정석.
    @Query("select m " +
            "from GroupMember m " +
            "inner join fetch m.group g " +
            "inner join fetch m.siteUser u " +  // DTO.from에서 필요함
            "inner join Channel ch " +  // 채널과 그룹을 논리적 연결
            "on ch.group.id = g.id " +
            "where ch.id = :channelId and u.id = :siteUserId")
    Optional<GroupMember> getGroupMemberWithChannelId(@Param("siteUserId") Long siteUserId, @Param("channelId") Long channelId);
}
