package com.example.chat.global.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass   // 상속받은 엔티티들이 이 클래스의 필드를 칼럼으로 인식하게한다.
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
    @CreatedDate    // 엔티티가 생성되어 저장될때 시간이 자동 저장됨
    @Column(updatable = false) // 생성시간은 수정되지않도록 설정
    private LocalDateTime createdAt;

    @LastModifiedDate   // 조회한 엔티티의 값을 변경할때 시간이 자동저장됨.
    private LocalDateTime modifiedAt;
}
