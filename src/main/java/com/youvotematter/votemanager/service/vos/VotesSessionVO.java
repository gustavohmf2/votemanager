package com.youvotematter.votemanager.service.vos;

import com.youvotematter.votemanager.model.Vote;
import com.youvotematter.votemanager.model.VoteSession;
import com.youvotematter.votemanager.model.enums.SessionState;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
public class VotesSessionVO implements Serializable {


    @Serial
    private static final long serialVersionUID = 2900962328178182765L;
    private Long id;
    private String title;
    private SessionState sessionState;
    private List<Vote> votes;

    private VotesSessionVO(final Long id, final String title, final SessionState sessionState,
                           final List<Vote> votes) {
        this.id = id;
        this.title = title;
        this.sessionState = sessionState;
        this.votes = votes;
    }
    public static VotesSessionVO of(final VoteSession voteSession) {
        return new VotesSessionVO(voteSession.getId(), voteSession.getTitle(), voteSession.getSessionState(),
                voteSession.getVotes());
    }
}

