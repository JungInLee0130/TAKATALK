package com.example.chat.category.entity;

import com.example.chat.group.entity.Groups;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    public Categories(String name, Boolean isSecret, Groups group) {
        this.name = name;
        this.isSecret = isSecret;
        this.group = group;
    }


    public void setName(String name) {
        this.name = name;
    }
}
