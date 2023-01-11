package com.youvotematter.votemanager.services;

import com.youvotematter.votemanager.exceptions.InvalidVoteException;
import com.youvotematter.votemanager.exceptions.SessionVoteException;
import com.youvotematter.votemanager.exceptions.VoteSessionExceptio;
import com.youvotematter.votemanager.models.Topic;
import com.youvotematter.votemanager.models.Vote;
import com.youvotematter.votemanager.models.VoteSession;
import com.youvotematter.votemanager.models.enums.SessionState;
import com.youvotematter.votemanager.repositories.TopicRepository;
import com.youvotematter.votemanager.repositories.VoteSessionRepository;
import com.youvotematter.votemanager.services.vos.VoteSessionFinishVO;
import com.youvotematter.votemanager.services.vos.VoteSessionVO;
import com.youvotematter.votemanager.services.vos.VoteVO;
import com.youvotematter.votemanager.services.vos.VotesSessionVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
public class VoteSessionService {

    @Autowired
    private VoteSessionRepository voteSessionRepository;

    @Autowired
    private TopicRepository topicRepository;

    public Optional<VoteSessionVO> init(final VoteSessionVO voteSessionVO) {
        log.info("Vote Session started, title={}", voteSessionVO.getTitle());
        final Topic topic = topicRepository.getReferenceById(voteSessionVO.getTopicId());
        final VoteSession voteSession = new VoteSession(voteSessionVO.getTitle(), topic,
                voteSessionVO.getSessionState(), voteSessionVO.getDuration());

        final VoteSessionVO voteSessionCreated = VoteSessionVO.of(voteSessionRepository.save(voteSession));
        return Optional.of(voteSessionCreated);
    }

    public Optional<List<VoteSessionVO>> listAll() {
        final List<VoteSessionVO> allVotes = voteSessionRepository.findAll().stream()
                .map(voteSession -> VoteSessionVO.of(voteSession))
                .toList();
        return Optional.of(allVotes);
    }

    public void collect(final Long voteSessionId, final VoteVO voteVO) throws InvalidVoteException {
        log.info("New vote to contabilize, voteSessionId={}, associatedId={}", voteSessionId, voteVO.getAssociatedId());
        final VoteSession voteSession = findById(voteSessionId);
        try {
            validateSession(voteSession, voteVO);
            addVote(voteSession, new Vote(voteVO.getAssociatedId(), voteVO.isChoice()));
        } catch (final Exception | VoteSessionExceptio exception) {
            throw new InvalidVoteException("Vote contain error");
        }
    }

    private void validateSession(VoteSession voteSession, final VoteVO voteVO) throws VoteSessionExceptio {
        log.info("Validate new vote, title={}", voteSession.getTitle());
        if(SessionState.CLOSED.equals(voteSession.getSessionState())){
            log.error("Vote session is closed, title={}", voteSession.getTitle());
            throw new VoteSessionExceptio("Vote session is Closed");
        }

        Duration durationSession = Duration.between(LocalDateTime.now(), voteSession.getCreatedAt());
        if(durationSession.toMinutes() >= voteSession.getDuration()){
            log.error("Vote session is closed, title={}, dateCreated={}", voteSession.getTitle(), voteSession.getCreatedAt());
            throw new VoteSessionExceptio("Vote session is timeout");
        }

        VoteSession voteAlreadyContabilized = voteSessionRepository.findByVotes_AssociatedId(voteVO.getAssociatedId());
        if(voteAlreadyContabilized != null) {
            log.error("Vote already contabilized, title={}, associatedId={}", voteSession.getTitle(), voteVO.getAssociatedId());
            throw new VoteSessionExceptio("Vote already contabilized");
        }
    }

    private void addVote(final VoteSession voteSession, final Vote vote) {
        voteSession.getVotes().add(vote);
        voteSessionRepository.save(voteSession);
    }

    public Optional<List<VotesSessionVO>> votesOfSession(Long voteSessionId) {
        final List<VotesSessionVO> allVotes = voteSessionRepository.findById(voteSessionId).stream()
                .map(voteSession -> VotesSessionVO.of(voteSession))
                .toList();
        return Optional.of(allVotes);
    }

    public void changeStatus(Long voteSessionId, VoteSessionFinishVO voteSessionFinishVO) throws SessionVoteException {
        final VoteSession voteSession = findById(voteSessionId);
        if(isNotValidChange(voteSessionFinishVO, voteSession)){
            log.error("Vote session already closed, title={}, status={}", voteSession.getTitle(), voteSession.getSessionState());
            throw new SessionVoteException("Session already closed");
        }
        voteSession.setSessionState(voteSessionFinishVO.getSessionState());
        voteSessionRepository.save(voteSession);
    }

    private static boolean isNotValidChange(VoteSessionFinishVO voteSessionFinishVO, VoteSession voteSession) {
        if(Objects.isNull(voteSession.getSessionState())&& Objects.isNull(voteSession.getSessionState())) {
            return true;
        }
        return !voteSession.getSessionState().equals(voteSessionFinishVO.getSessionState());
    }

    private VoteSession findById(Long voteSessionId) {
        return voteSessionRepository.findById(voteSessionId)
                .orElseThrow(() -> new IllegalArgumentException("The identify vote session wrong"));
    }
}
