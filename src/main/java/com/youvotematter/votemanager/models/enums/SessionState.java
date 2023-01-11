package com.youvotematter.votemanager.models.enums;

import java.util.Arrays;
import java.util.Optional;

public enum SessionState {

    OPEN("OPEN"),
    CLOSED("CLOSED");
    private String value;

    SessionState(final String value) {
        this.value = value;
    }

    public SessionState getValue(final Integer stateValue) {
        return Arrays.stream(values()).filter(sessionState -> sessionState.ordinal() == stateValue)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Value not recognized to session status"));
    }
}
