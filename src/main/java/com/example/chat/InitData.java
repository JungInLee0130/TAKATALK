package com.example.chat;

import com.example.chat.channel.domain.ChannelType;
import com.example.chat.channel.entity.Channels;
import com.example.chat.channel.entity.ChatMessages;
import com.example.chat.channel.repository.ChannelRepository;
import com.example.chat.channel.repository.ChatMessageRepository;
import com.example.chat.group.GroupRepository;
import com.example.chat.group.Groups;
import com.example.chat.user.entity.SiteUser;
import com.example.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final GroupRepository groupRepository;

    private final ChannelRepository channelRepository;
    private final ChatMessageRepository chatMessageRepository;


    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("test@naver.com")) {
            SiteUser siteUser1 = SiteUser.builder()
                    .username("테스트1")
                    .nickname("테스트1")
                    .email("test1@naver.com")
                    .password(passwordEncoder.encode("qwer1234"))
                    .birthday(LocalDate.parse("2013-03-03"))
                    .build();

            SiteUser siteUser2 = SiteUser.builder()
                    .username("테스트2")
                    .nickname("테스트2")
                    .email("test2@naver.com")
                    .password(passwordEncoder.encode("qwer1234"))
                    .birthday(LocalDate.parse("2013-03-04"))
                    .build();

            siteUser1.setProfile("/images/meeng.png");
            siteUser2.setProfile("/images/meeng.png");

            userRepository.save(siteUser1);
            userRepository.save(siteUser2);
            System.out.println("======테스트용 초기 데이터 생성 완료 (ID : test1@naver.com / PW : qwer1234) ========");
            System.out.println("======테스트용 초기 데이터 생성 완료 (ID : test2@naver.com / PW : qwer1234) ========");


            Groups group1 = new Groups("그룹1", siteUser1);

            group1.setProfile("/images/meeng.png");

            groupRepository.save(group1);

            System.out.println("======그룹 초기 데이터 생성 완료 (name : group1 / siteUser : 테스트1) ========");

            Channels channel1 = Channels.builder()
                    .type(ChannelType.TEXT)
                    .name("채널1")
                    .isSecret(false)
                    .group(group1)
                    .build();

            Channels channel2 = Channels.builder()
                    .type(ChannelType.TEXT)
                    .name("채널2")
                    .isSecret(false)
                    .group(group1)
                    .build();

            channelRepository.save(channel1);
            channelRepository.save(channel2);

            System.out.println("======채널 초기 데이터 생성 완료 (name : channel1 / siteUser : 테스트1) ========");
            System.out.println("======채널 초기 데이터 생성 완료 (name : channel2 / siteUser : 테스트1) ========");


            for (int i = 1; i <= 100; i++) {
                ChatMessages chatMessages = ChatMessages.builder()
                        .content("dd" + i)
                        .channel(channel1)   // 중간에 누군가가 채널을 삭제할수있기때문에 직접 불러와야함.
                        .siteUser(siteUser1)
                        .build();

                chatMessageRepository.save(chatMessages);
            }

            System.out.println("======채팅메시지 초기 데이터 생성 완료 (개수 : 100개 /channel : channel1 / siteUser : 테스트1) ========");
        }
    }
}
