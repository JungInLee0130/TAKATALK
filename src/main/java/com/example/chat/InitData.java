package com.example.chat;

import com.example.chat.category.entity.Categories;
import com.example.chat.category.repository.CategoryRepository;
import com.example.chat.channel.domain.ChannelType;
import com.example.chat.channel.domain.ChatMessageType;
import com.example.chat.channel.entity.Channels;
import com.example.chat.channel.entity.ChatMessages;
import com.example.chat.channel.repository.ChannelRepository;
import com.example.chat.channel.repository.ChatMessageRepository;
import com.example.chat.group.repository.GroupRepository;
import com.example.chat.group.entity.Groups;
import com.example.chat.groupmember.domain.GroupRole;
import com.example.chat.groupmember.entity.GroupMember;
import com.example.chat.groupmember.repository.GroupMemberRepository;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final GroupRepository groupRepository;
    private final CategoryRepository categoryRepository;

    private final ChannelRepository channelRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final GroupMemberRepository groupMemberRepository;


    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("test@naver.com")) {

            Map<Long, SiteUser> siteUserMap = createUsers();

            List<Groups> groupList = createGroups();

            log.info("siteUserMap : {}", siteUserMap.get(1L));

            setGroupMembers(siteUserMap, groupList);

            List<Channels> channelList = setupCategoriesAndChannels(groupList);

            createChatMessages(siteUserMap.get(1), channelList);

            System.out.println("======모든 더미데이터 생성완료! ========");
        }
    }

    private void createChatMessages(SiteUser siteUser, List<Channels> channelList) {
        List<ChatMessages> chatMessageList = new ArrayList<>();
        for (Channels channel : channelList) {
            for (int i = 1; i <= 30; i++) {
                ChatMessages chatMessages = ChatMessages.create(
                        "dd" + i,
                        channel,
                        siteUser,
                        ChatMessageType.TALK
                );

                chatMessageList.add(chatMessages);
            }
        }

        chatMessageRepository.saveAll(chatMessageList);

        System.out.println("======채팅메시지 초기 데이터 생성 완료 (개수 : 30개) ========");
    }

    private List<Channels> setupCategoriesAndChannels(List<Groups> groupList) {
        Map<Groups, List<Categories>> categoryMap = new HashMap<>();

        for (Groups group : groupList) {
            List<Categories> categoryList = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                Categories category = Categories.create(
                        "카테고리 " + i,
                        false,
                        group
                );
                categoryList.add(category);
            }
            categoryMap.put(group, categoryList);

        }

        List<Categories> allCategories = categoryMap.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        categoryRepository.saveAll(allCategories);

        System.out.println("======카테고리 초기 데이터 생성 완료 (name : 카테고리) ========");


        List<Channels> channelList = new ArrayList<>();
        for(Map.Entry<Groups, List<Categories>> entry : categoryMap.entrySet()) {
            Groups group = entry.getKey();
            List<Categories> categories = entry.getValue();

            // categorizedChannel
            for (Categories category : categories) {
                for (int i = 1; i <= 3; i++) {
                    Channels channel = Channels.create(
                            "채널" + i,
                            ChannelType.TEXT,
                            false,
                            group,
                            category
                    );
                    channelList.add(channel);
                }
            }

            // uncategorizedChannel
            for(int i = 1; i <= 5; i++) {
                Channels channel = Channels.create(
                        "채널" + i,
                        ChannelType.TEXT,
                        false,
                        group
                );
                channelList.add(channel);
            }
        }

        channelRepository.saveAll(channelList);

        System.out.println("======채널 초기 데이터 생성 완료 (name : 채널) ========");
        return channelList;
    }

    private void setGroupMembers(Map<Long, SiteUser> siteUserMap, List<Groups> groupList) {
        List<GroupMember> groupMemberList = new ArrayList<>();
        for (Groups group : groupList) {
            for (int i = 1; i <= 10; i++) {
                GroupRole groupRole = assignRole(i);

                GroupMember groupMember = GroupMember.create(
                        siteUserMap.get((long) i),
                        group,
                        groupRole
                );

                groupMemberList.add(groupMember);
            }
        }

        groupMemberRepository.saveAll(groupMemberList);
    }

    private static GroupRole assignRole(int i) {
        GroupRole groupRole;
        switch(i) {
            case 1:
                groupRole = GroupRole.ADMIN;
                break;
            case 2:
            case 3:
            case 4:
                groupRole = GroupRole.OWNER;
                break;
            default:
                groupRole = GroupRole.USER;
                break;
        }
        return groupRole;
    }

    private List<Groups> createGroups() {
        List<Groups> groupList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Groups group = Groups.create(
                    "그룹" + i
            );
            groupList.add(group);
        }
        groupRepository.saveAll(groupList);

        System.out.println("======그룹멤버 초기 데이터 생성 완료 (name : 그룹) ========");
        return groupList;
    }

    private Map<Long, SiteUser> createUsers() {
        Map<Long, SiteUser> siteUserMap = new HashMap<>();
        // 유저 100명
        List<SiteUser> siteUsers = new ArrayList<>();
        // 관리자
        SiteUser admin = SiteUser.create("관리자",
                "관리자",
                "admin@naver.com",
                "qwer1234",
                LocalDate.of(2013,03,03),
                null,
                passwordEncoder
        );
        siteUsers.add(admin);
        siteUserMap.put(1L, admin);

        for (int i = 2; i <= 10; i++) {
            SiteUser siteUser = SiteUser.create("테스트" + i,
                    "테스트" + i,
                    "test" + i + "@naver.com",
                    "qwer1234",
                    LocalDate.of(2013,03,03),
                    null,
                    passwordEncoder
            );
            siteUsers.add(siteUser);
            siteUserMap.put((long) i, siteUser);
        }

        userRepository.saveAll(siteUsers);

        System.out.println("======유저 데이터 생성 완료 관리자 1명, 유저 9명 (ID : test2~9@naver.com / PW : qwer1234) ========");

        return siteUserMap;
    }
}
