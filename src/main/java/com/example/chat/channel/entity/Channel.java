package com.example.chat.channel.entity;

import com.example.chat.category.entity.Category;
import com.example.chat.channel.domain.ChannelType;
import com.example.chat.channel.dto.ChannelEditRequest;
import com.example.chat.group.entity.Group;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE channel set is_deleted = true where channel_id = ?")
@SQLRestriction("is_deleted = false")
public class Channel {
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
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted = false;

    @Builder
    private Channel(String name, ChannelType type, Boolean isSecret, Group group, Category category) {
        this.name = name;
        this.type = type;
        this.isSecret = isSecret;
        this.group = group;
        this.category = category;
    }

    @Builder
    public static Channel create(String name, ChannelType type, Boolean isSecret, Group group,
                                 Category category) {
        return Channel.builder()
                .name(name)
                .type(type)
                .isSecret(isSecret)
                .group(group)
                .category(category)
                .build();
    }

    @Builder
    public static Channel create(String name, ChannelType type, Boolean isSecret, Group group) {
        return Channel.builder()
                .name(name)
                .type(type)
                .isSecret(isSecret)
                .group(group)
                .build();
    }

    public void updateCategories(Category category) {
        this.category = category;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(id, channel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updateChannel(Group group, Category category, ChannelEditRequest request) {
        this.group = group != null ? group : this.group;
        this.category = category != null ? category : this.category;
        this.type = request.getType();
        this.isSecret = request.getIsSecret();
        this.name = request.getName();
    }
}
