package com.youvotematter.votemanager.model;

import com.youvotematter.votemanager.model.enums.SessionState;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Entity
/*@NamedQueries({
        @NamedQuery(name = "VoteSession.findByVotes_AssociatedIdIn", query = "select v from VoteSession v inner join v.votes votes where votes.associatedId in :associatedId")
})*/
@NoArgsConstructor
public class VoteSession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Nonnull
    private String title;
    @OneToOne
    private Topic topic;
    @Nonnull
    private SessionState sessionState = SessionState.OPEN;
    @Nonnull
    private Float duration = 1F;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "voteSessionId")
    private List<Vote> votes;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public VoteSession(final String title, final Topic topic, final SessionState sessionState, final Float duration) {
        this.title = title;
        this.topic = topic;
        if(Objects.nonNull(sessionState)) {
            this.sessionState = sessionState;
        }
        if(Objects.nonNull(duration)) {
            this.duration = duration;
        }
    }
}
