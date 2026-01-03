package com.example.chat.category.entity;

import com.example.chat.channel.entity.Channel;
import com.example.chat.group.entity.Group;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    private Boolean isSecret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Channel> channels = new ArrayList<>();

    @Builder
    private Category(Long id, String name, Boolean isSecret, Group group, List<Channel> channels) {
        this.id = id; // 테스트용. 어짜피 save 시도하려해도 jpa가 무시.
        this.name = name;
        this.isSecret = isSecret;
        this.group = group;
        this.channels = (channels == null) ? new ArrayList<>() : channels;
    }


    public static Category create(String name, Boolean isSecret, Group group) {
        return Category.builder()
                .name(name)
                .isSecret(isSecret)
                .group(group)
                .build();
    }


    public void updateName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category that = (Category) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
