package com.youvotematter.votemanager.service;

import com.youvotematter.votemanager.model.Topic;
import com.youvotematter.votemanager.repository.TopicRepository;
import com.youvotematter.votemanager.service.vos.TopicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public Optional<TopicVO> create(final TopicVO topicVO) {
        final Topic createdTopic = topicRepository.save(new Topic(topicVO.getId(), topicVO.getTheme()));
        final TopicVO topicVOCreated = TopicVO.of(createdTopic);
        return Optional.of(topicVOCreated);
    }

    public Optional<List<TopicVO>> listAll() {
        List<TopicVO> topicsVO = topicRepository.findAll().stream()
                .map(topic -> TopicVO.of(topic))
                .toList();
        return Optional.of(topicsVO);
    }
}
