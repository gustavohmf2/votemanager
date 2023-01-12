package com.youvotematter.votemanager.repository;

import com.youvotematter.votemanager.model.VoteSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteSessionRepository extends JpaRepository<VoteSession, Long> {

    VoteSession findByVotes_AssociatedId(Long id);
}
