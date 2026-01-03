package com.example.chat.groupmember.repository;

import com.example.chat.group.entity.Group;
import com.example.chat.groupmember.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    @Query("select m.group " +
            "from GroupMember m " +
            "where m.siteUser.id = :siteUserId ")
    List<Group> findGroupBySiteUserId (Long siteUserId);

    Boolean existsByGroupIdAndSiteUserId(Long groupId, Long siteUserId);

    Optional<GroupMember> findByGroupIdAndSiteUserId(Long groupId, Long siteUserId);
}
