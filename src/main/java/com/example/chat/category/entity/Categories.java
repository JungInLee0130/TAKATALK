package com.example.chat.category.entity;

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
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    private Boolean isSecret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Groups group;

    @Builder
    private Categories(String name, Boolean isSecret, Groups group) {
        this.name = name;
        this.isSecret = isSecret;
        this.group = group;
    }

    @Builder
    public static Categories create(String name, Boolean isSecret, Groups group) {
        return Categories.builder()
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
        Categories that = (Categories) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
