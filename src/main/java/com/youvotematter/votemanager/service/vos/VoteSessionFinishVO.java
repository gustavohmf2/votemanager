package com.youvotematter.votemanager.service.vos;

import com.youvotematter.votemanager.model.enums.SessionState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class VoteSessionFinishVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1032147718304777501L;
    private SessionState sessionState;
}
