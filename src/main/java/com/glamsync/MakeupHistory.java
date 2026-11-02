package com.glamsync;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "makeup_history")
public class MakeupHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    private String title;

    @Column(length = 2000)
    private String details;

    private LocalDateTime createdAt;

    public MakeupHistory() {
    }

    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}