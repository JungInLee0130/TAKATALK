package com.example.chat.channel.entity;

import com.example.chat.category.entity.Categories;
import com.example.chat.channel.domain.ChannelType;
import com.example.chat.group.entity.Groups;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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
    private Channels(String name, ChannelType type, Boolean isSecret, Groups group, Categories categories) {
        this.name = name;
        this.type = type;
        this.isSecret = isSecret;
        this.group = group;
        this.categories = categories;
    }

    @Builder
    public static Channels create(String name, ChannelType type, Boolean isSecret, Groups group,
                                 Categories categories) {
        return Channels.builder()
                .name(name)
                .type(type)
                .isSecret(isSecret)
                .group(group)
                .categories(categories)
                .build();
    }

    @Builder
    public static Channels create(String name, ChannelType type, Boolean isSecret, Groups group) {
        return Channels.builder()
                .name(name)
                .type(type)
                .isSecret(isSecret)
                .group(group)
                .build();
    }

    public void updateCategories(Categories categories) {
        this.categories = categories;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channels channels = (Channels) o;
        return Objects.equals(id, channels.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
