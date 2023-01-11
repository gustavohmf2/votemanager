package com.youvotematter.votemanager.services.vos;

import com.youvotematter.votemanager.models.Topic;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class TopicVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8690344991308838543L;
    private Long id;
    private String theme;


    private TopicVO(final Long id, final String theme) {
        this.id = id;
        this.theme = theme;
    }

    public static TopicVO of(final Topic topic) {
        return new TopicVO(topic.getId(), topic.getTheme());
    }
}
