package com.example.chat.group;

import com.example.chat.user.entity.SiteUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    private String name;

    private String profileImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siteuser_id")
    private SiteUser siteUser;  // spring security user 위치

    @Builder
    public Groups(String name, SiteUser siteUser) {
        this.name = name;
        this.profileImageUrl = "meeng.png";
        this.siteUser = siteUser;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
    }
}
