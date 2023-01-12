package com.youvotematter.votemanager.controller;

import com.youvotematter.votemanager.exception.InvalidVoteException;
import com.youvotematter.votemanager.exception.SessionVoteException;
import com.youvotematter.votemanager.service.VoteSessionService;
import com.youvotematter.votemanager.service.vos.VoteSessionFinishVO;
import com.youvotematter.votemanager.service.vos.VoteSessionVO;
import com.youvotematter.votemanager.service.vos.VoteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votesessions")
public class VoteSessionController {

    @Autowired
    private VoteSessionService voteSessionService;

    @GetMapping
    public ResponseEntity listAll() {
        return ResponseEntity.of(voteSessionService.listAll());
    }

    @GetMapping("{voteSessionId}")
    public ResponseEntity listAllVotes(@PathVariable("voteSessionId") Long voteSessionId) {
        return ResponseEntity.of(voteSessionService.votesOfSession(voteSessionId));
    }

    @PostMapping
    public ResponseEntity init(@RequestBody VoteSessionVO voteSessionVO) {
        return ResponseEntity.of(voteSessionService.init(voteSessionVO));
    }

    @PostMapping("{voteSessionId}/votes")
    public ResponseEntity collect(@PathVariable("voteSessionId") final Long voteSessionId,
                                  @RequestBody final VoteVO voteVO) {
        try {
            voteSessionService.collectVote(voteSessionId, voteVO);
            return ResponseEntity.noContent().build();
        }  catch (InvalidVoteException e) {
            return ResponseEntity.badRequest().body("Fail to acount vote");
        }
    }

    @PatchMapping("{voteSessionId}")
    public ResponseEntity changeStatus(@PathVariable("voteSessionId") final Long voteSessionId,
                                       @RequestBody final VoteSessionFinishVO voteSessionFinishVO) {
        try {
            voteSessionService.changeStatus(voteSessionId, voteSessionFinishVO);
            return ResponseEntity.noContent().build();
        }  catch (final SessionVoteException e) {
            return ResponseEntity.badRequest().body("Fail to finish session");
        }
    }
}
