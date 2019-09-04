package it.smartcommunitylab.smartchainbackend.bean;

import java.util.Date;

import it.smartcommunitylab.model.ext.ChallengeConcept;

public class PlayerChallenge {
    private String challengeId;
    private Date start;
    private Date end;


    public PlayerChallenge() {

    }

    public PlayerChallenge(String challengeId, ChallengeConcept c) {
        this.challengeId = challengeId;
        start = c.getStart();
        end = c.getEnd();
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }


}
