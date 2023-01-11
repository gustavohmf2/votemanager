package com.youvotematter.votemanager.services.vos;

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
}
