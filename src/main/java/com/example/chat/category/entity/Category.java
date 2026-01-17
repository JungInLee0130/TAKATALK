package com.example.chat.category.entity;

import com.example.chat.channel.entity.Channel;
import com.example.chat.group.entity.Group;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE category SET is_deleted = true where category_id = ?")
@SQLRestriction("is_deleted = false")
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
    private List<Channel> channels = new ArrayList<>(); // 연관관계라서 DB열에는 포함안됨.

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted = false;

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

    public void updateCategory(String name, Boolean isSecret) {
        if (this.isSecret != isSecret) {
            this.isSecret = isSecret;
        }
        this.name = name;
    }
}
