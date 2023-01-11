package com.youvotematter.votemanager.services.vos;

import com.youvotematter.votemanager.models.Topic;
import com.youvotematter.votemanager.models.VoteSession;
import com.youvotematter.votemanager.models.enums.SessionState;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class VoteSessionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7174925352225305016L;

    private Long id;
    private String title;
    private Long topicId;
    private SessionState sessionState;
    private Float duration;

    private VoteSessionVO(final Long id, final String title, final Long topic, final SessionState sessionState, final Float duration) {
        this.id = id;
        this.title = title;
        this.topicId = topic;
        this.sessionState = sessionState;
        this.duration = duration;
    }
    public static VoteSessionVO of(final VoteSession voteSession) {
        return new VoteSessionVO(voteSession.getId(), voteSession.getTitle(), voteSession.getTopic().getId(),
                voteSession.getSessionState(), voteSession.getDuration());
    }
}
