package com.sgta.shared.model.audit;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Auditable {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    protected LocalDateTime created;

    @LastModifiedDate
    @Column(nullable = false)
    protected LocalDateTime modified;

    @CreatedBy
    @Column(updatable = false)
    protected Long createdId;

    @LastModifiedBy
    protected Long modifiedId;
}