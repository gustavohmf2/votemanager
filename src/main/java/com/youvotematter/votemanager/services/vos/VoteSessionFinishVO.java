package com.youvotematter.votemanager.services.vos;

import com.youvotematter.votemanager.models.enums.SessionState;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class VoteSessionFinishVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1032147718304777501L;
    private SessionState sessionState;
}
