package com.example.chat.groupmember.repository;

import com.example.chat.group.entity.Groups;
import com.example.chat.groupmember.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    @Query("select m.group " +
            "from GroupMember m " +
            "where m.siteUser.id = :siteUserId ")
    List<Groups> findGroupBySiteUserId (Long siteUserId);
}
