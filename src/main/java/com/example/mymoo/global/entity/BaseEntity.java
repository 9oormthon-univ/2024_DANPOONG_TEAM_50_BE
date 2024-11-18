package com.example.mymoo.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
    @Column(columnDefinition = "TIMESTAMP(6)", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP(6)", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
