package com.example.chat;

import com.example.chat.category.entity.Category;
import com.example.chat.category.repository.CategoryRepository;
import com.example.chat.channel.domain.ChannelType;
import com.example.chat.chatmessage.domain.ChatMessageType;
import com.example.chat.channel.entity.Channel;
import com.example.chat.chatmessage.entity.ChatMessage;
import com.example.chat.channel.repository.ChannelRepository;
import com.example.chat.chatmessage.repository.ChatMessageRepository;
import com.example.chat.group.repository.GroupRepository;
import com.example.chat.group.entity.Group;
import com.example.chat.groupmember.domain.GroupRole;
import com.example.chat.groupmember.entity.GroupMember;
import com.example.chat.groupmember.repository.GroupMemberRepository;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Profile("init")
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

            List<Group> groupList = createGroups();

            log.info("siteUserMap : {}", siteUserMap.get(1L));

            setGroupMembers(siteUserMap, groupList);

            List<Channel> channelList = setupCategoriesAndChannels(groupList);

            createChatMessages(siteUserMap.get(1L), channelList);

            System.out.println("======모든 더미데이터 생성완료! ========");
        }
    }

    private void createChatMessages(SiteUser siteUser, List<Channel> channelList) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        for (Channel channel : channelList) {
            for (int i = 1; i <= 30; i++) {
                ChatMessage chatMessage = ChatMessage.create(
                        "dd" + i,
                        channel,
                        siteUser,
                        ChatMessageType.TALK
                );

                chatMessageList.add(chatMessage);
            }
        }

        chatMessageRepository.saveAll(chatMessageList);

        System.out.println("======채팅메시지 초기 데이터 생성 완료 (개수 : 30개) ========");
    }

    private List<Channel> setupCategoriesAndChannels(List<Group> groupList) {
        Map<Group, List<Category>> categoryMap = new HashMap<>();

        for (Group group : groupList) {
            List<Category> categoryList = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                Category category = Category.create(
                        "카테고리 " + i,
                        false,
                        group
                );
                categoryList.add(category);
            }
            categoryMap.put(group, categoryList);

        }

        List<Category> allCategories = categoryMap.values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        categoryRepository.saveAll(allCategories);

        System.out.println("======카테고리 초기 데이터 생성 완료 (name : 카테고리) ========");


        List<Channel> channelList = new ArrayList<>();
        for(Map.Entry<Group, List<Category>> entry : categoryMap.entrySet()) {
            Group group = entry.getKey();
            List<Category> categories = entry.getValue();

            // categorizedChannel
            for (Category category : categories) {
                for (int i = 1; i <= 3; i++) {
                    Channel channel = Channel.create(
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
                Channel channel = Channel.create(
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

    private void setGroupMembers(Map<Long, SiteUser> siteUserMap, List<Group> groupList) {
        List<GroupMember> groupMemberList = new ArrayList<>();
        for (Group group : groupList) {
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

    private List<Group> createGroups() {
        List<Group> groupList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Group group = Group.create(
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
