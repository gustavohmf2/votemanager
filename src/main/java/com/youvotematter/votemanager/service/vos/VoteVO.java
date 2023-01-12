package com.youvotematter.votemanager.service.vos;

import com.youvotematter.votemanager.model.Vote;
import com.youvotematter.votemanager.model.VoteSession;
import com.youvotematter.votemanager.model.enums.SessionState;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class VoteVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6525532385184658388L;

    private Long sessionId;

    private Long associatedId;

    private boolean choice;

    private VoteVO(final Long sessionId, final Long associatedId, final boolean choice) {
        this.sessionId = sessionId;
        this.associatedId = associatedId;
        this.choice = choice;
    }
    public static VoteVO of(final Vote vote) {
        return new VoteVO(vote.getId(), vote.getAssociatedId(), vote.isChoice());
    }
}
