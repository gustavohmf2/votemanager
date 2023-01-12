package com.youvotematter.votemanager.service;

import com.youvotematter.votemanager.exception.InvalidVoteException;
import com.youvotematter.votemanager.exception.SessionVoteException;
import com.youvotematter.votemanager.model.Topic;
import com.youvotematter.votemanager.model.Vote;
import com.youvotematter.votemanager.model.VoteSession;
import com.youvotematter.votemanager.model.enums.SessionState;
import com.youvotematter.votemanager.repository.TopicRepository;
import com.youvotematter.votemanager.repository.VoteRepository;
import com.youvotematter.votemanager.repository.VoteSessionRepository;
import com.youvotematter.votemanager.service.vos.VoteSessionFinishVO;
import com.youvotematter.votemanager.service.vos.VoteSessionVO;
import com.youvotematter.votemanager.service.vos.VoteVO;
import com.youvotematter.votemanager.service.vos.VotesSessionVO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class VoteSessionServiceTest {

    private static final SessionState DEFAULT_SESSION_STATE = SessionState.OPEN;

    private static final Float DEFAULT_SESSION_DURATION = 1F;
    public static final String DEFAULT_TOPIC_NAME = "Teste";
    @Mock
    private VoteSessionRepository voteSessionRepository;
    @Mock
    private TopicRepository topicRepository;
    @InjectMocks
    private VoteSessionService voteSessionService;
    @Autowired
    private VoteRepository voteRepository;


    @Test
    public void listAll() {
        when(voteSessionRepository.findAll()).thenReturn(Arrays.asList());
        final Optional<List<VoteSessionVO>> listResult = voteSessionService.listAll();
        assertNotNull("Object null", listResult);
    }

    @Test
    public void allVotesOfSessionVote() {
        final var voteSessionId = 123L;
        final VoteSession voteSession = buildSessionVote(new Topic());
        voteSession.setSessionState(SessionState.CLOSED);
        voteSession.setDuration(1F);
        when(voteSessionRepository.findById(voteSessionId)).thenReturn(Optional.of(voteSession));
        final Optional<VotesSessionVO> result = voteSessionService.votesOfSession(voteSessionId);
        assertNotNull("Object null", result.get());
        assertEquals("title not equals", result.get().getTitle(), voteSession.getTitle());

    }

    @Test
    public void initSessionVoteDefault() {

        final var topic = new Topic(1L, DEFAULT_TOPIC_NAME);
        final var voteSession = new VoteSession("Teste", topic, null, null);

        when(topicRepository.getReferenceById(1L)).thenReturn(topic);
        when(voteSessionRepository.save(voteSession)).thenReturn(voteSession);

        var voteSessionVO = VoteSessionVO.of(voteSession);
        final Optional<VoteSessionVO> result = voteSessionService.init(voteSessionVO);

        assertNotNull("",result.get());
        assertEquals("SessionState diferent", result.get().getSessionState(), DEFAULT_SESSION_STATE);
        assertEquals("SessionDuration diferent", result.get().getDuration(), DEFAULT_SESSION_DURATION);

    }

    @Test
    public void initSessionVote() {

        final var topic = new Topic(1L,DEFAULT_TOPIC_NAME);
        final var voteSession = buildSessionVote(topic);
        voteSession.setId(1L);
        voteSession.setSessionState(SessionState.CLOSED);
        when(topicRepository.getReferenceById(1L)).thenReturn(topic);
        when(voteSessionRepository.save(any(VoteSession.class))).thenReturn(voteSession);

        final var voteSessionVO = VoteSessionVO.of(voteSession);
        final Optional<VoteSessionVO> result = voteSessionService.init(voteSessionVO);

        assertNotNull("",result.get());
        assertEquals("SessionState diferent", result.get().getSessionState(), SessionState.CLOSED);
        assertEquals("SessionDuration diferent", result.get().getDuration(), 15F);

    }

    @Test
    public void addVoteOfSessionVoteSuccess() throws InvalidVoteException {
        final var topic = new Topic(1L,DEFAULT_TOPIC_NAME);
        final VoteSession voteSession = buildSessionVote(topic);

        Vote vote = buildVote();

        when(voteSessionRepository.findById(1L)).thenReturn(Optional.of(voteSession));
        when(voteSessionRepository.findByVotes_AssociatedId(1234L)).thenReturn(null);
        when(voteSessionRepository.save(voteSession)).thenReturn(voteSession);

        voteSessionService.collectVote(1L, VoteVO.of(vote));
    }



    @Test
    public void addVoteOfSessionVoteTimeout() throws InvalidVoteException {
        final var topic = new Topic(1L,DEFAULT_TOPIC_NAME);
        VoteSession voteSession = buildSessionVote(topic);
        voteSession.setId(1L);
        voteSession.setCreatedAt(LocalDateTime.now().minusMinutes(20));
        voteSession.setVotes(new ArrayList<>());

        Vote vote = buildVote();

        when(voteSessionRepository.findById(1L)).thenReturn(Optional.of(voteSession));
        when(voteSessionRepository.findByVotes_AssociatedId(1234L)).thenReturn(null);

        assertThrows(InvalidVoteException.class, () -> voteSessionService.collectVote(1L, VoteVO.of(vote)));
    }

    @Test
    public void addVoteOfSessionVoteClosed() throws InvalidVoteException {
        final var topic = new Topic(1L,DEFAULT_TOPIC_NAME);
        VoteSession voteSession = buildSessionVote(topic);
        voteSession.setSessionState(SessionState.CLOSED);
        voteSession.setId(1L);
        voteSession.setCreatedAt(LocalDateTime.now().minusMinutes(20));
        voteSession.setVotes(new ArrayList<>());

        Vote vote = buildVote();

        when(voteSessionRepository.findById(1L)).thenReturn(Optional.of(voteSession));
        when(voteSessionRepository.findByVotes_AssociatedId(1234L)).thenReturn(null);

        assertThrows(InvalidVoteException.class, () -> voteSessionService.collectVote(1L, VoteVO.of(vote)));
    }

    @Test
    public void addVoteOfSessionVoteAssociateAlreadyVote() throws InvalidVoteException {
        final var topic = new Topic(1L,DEFAULT_TOPIC_NAME);
        final Vote vote = buildVote();

        final VoteSession voteSession = new VoteSession("Teste", topic, SessionState.OPEN
                , 15F);
        voteSession.setId(1L);
        voteSession.setCreatedAt(LocalDateTime.now().minusMinutes(20));
        voteSession.setVotes(Arrays.asList(vote));



        when(voteSessionRepository.findById(1L)).thenReturn(Optional.of(voteSession));
        when(voteSessionRepository.findByVotes_AssociatedId(1234L)).thenReturn(null);

        assertThrows(InvalidVoteException.class, () -> voteSessionService.collectVote(1L, VoteVO.of(vote)));
    }

    @Test
    public void changeStatusSessionVote() throws SessionVoteException {
        final var topic = new Topic(1L,DEFAULT_TOPIC_NAME);
        final VoteSession voteSession = new VoteSession("Teste", topic, SessionState.OPEN
                , 15F);
        final VoteSessionFinishVO finishSessionVO = new VoteSessionFinishVO(SessionState.CLOSED);
        when(voteSessionRepository.findById(1L)).thenReturn(Optional.of(voteSession));
        voteSessionService.changeStatus(1L, finishSessionVO);
    }

    private static Vote buildVote() {
        Vote vote = new Vote();
        vote.setId(1L);
        vote.setChoice(true);
        vote.setAssociatedId(1234L);
        return vote;
    }

    private static VoteSession buildSessionVote(Topic topic) {
        VoteSession voteSession = new VoteSession("Teste", topic, SessionState.OPEN
                , 15F);
        voteSession.setId(1L);
        voteSession.setCreatedAt(LocalDateTime.now());
        voteSession.setVotes(new ArrayList<>());
        return voteSession;
    }
}
