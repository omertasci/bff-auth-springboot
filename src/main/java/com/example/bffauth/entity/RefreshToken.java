package com.example.bffauth.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "refresh_tokens")
public class RefreshToken {

  @Id private String id = UUID.randomUUID().toString();

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdDate;

  @CreatedBy
  @Column(updatable = false)
  private String createdBy;

  @LastModifiedDate
  @Column(insertable = false)
  private LocalDateTime lastModifiedDate;

  @LastModifiedBy
  @Column(insertable = false)
  private String lastModifiedBy;

  private String username;

  private String domain;

  private String deviceId;

  @Column(length = 512)
  private String tokenHash;

  private Instant expiresAt;

  private boolean revoked = false;

  private String familyId; // rotation family id
}
