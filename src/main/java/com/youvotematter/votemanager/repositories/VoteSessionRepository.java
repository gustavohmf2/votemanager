package com.youvotematter.votemanager.repositories;

import com.youvotematter.votemanager.models.VoteSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteSessionRepository extends JpaRepository<VoteSession, Long> {

    VoteSession findByVotes_AssociatedId(Long id);
}
