package com.example.chat.channel.entity;

import com.example.chat.category.entity.Categories;
import com.example.chat.channel.domain.ChannelType;
import com.example.chat.group.entity.Groups;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channels {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelType type;

    @Column(nullable = false)
    private Boolean isSecret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Groups group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = true)
    private Categories categories;

    @Builder
    public Channels(String name, ChannelType type, Boolean isSecret, Groups group) {
        this.name = name;
        this.type = type;
        this.isSecret = isSecret;
        this.group = group;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }
}
