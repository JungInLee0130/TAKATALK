package com.example.chat.group;

import com.example.chat.user.entity.SiteUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    private String profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siteuser_id")
    private SiteUser siteUser;  // spring security user 위치

    public Groups(String name, SiteUser siteUser) {
        this.name = name;
        this.profile = "default";
        this.siteUser = siteUser;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
