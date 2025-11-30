package com.example.chat.user.entity;

import com.example.chat.user.domain.UserCreateForm;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private LocalDate birthday;

    @Builder
    public SiteUser(String nickname, String username, String password, String email
            , LocalDate birthday) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthday = birthday;
    }

    // 생성자
    public static SiteUser createUser(UserCreateForm userCreateForm) {
        String nickname = userCreateForm.nickname();
        /*닉네임이 없다면*/
        if (!StringUtils.hasText(userCreateForm.nickname())) {
            nickname = userCreateForm.username();
        }

        int year = Integer.parseInt(userCreateForm.birthYear());
        int month = Integer.parseInt(userCreateForm.birthMonth());
        int day = Integer.parseInt(userCreateForm.birthDay());

        LocalDate dateTime = LocalDate.of(year, month, day);

        return SiteUser.builder()
                .username(userCreateForm.username())
                .email(userCreateForm.email())
                .nickname(nickname)
                .password(userCreateForm.password())
                .birthday(dateTime)
                .build();
    }
}
