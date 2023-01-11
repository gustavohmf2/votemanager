package com.youvotematter.votemanager.models;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Nonnull
    private Long associatedId;
    @Nonnull
    private boolean choice;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Vote(Long associatedId, boolean choice) {
        this.associatedId = associatedId;
        this.choice = choice;
    }
}
