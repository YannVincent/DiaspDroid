package fr.android.scaron.diaspdroid.model;

/**
 * Created by Sébastien on 18/05/2015.
 */
public class PollAnswer {
    /**
     * "poll_answers": [
     {
     "id": 2251,
     "answer": "1) Oui, mon chat a miaulé de plaisir en voyant la nouvelle interface! ",
     "poll_id": 624,
     "guid": "068fdaa0df080132c41e2a0000053625",
     "vote_count": 0
     },
     */
    Integer id;
    String answer;
    Integer poll_id;
    String guid;
    Integer vote_count;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(final String answer) {
        this.answer = answer;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(final String guid) {
        this.guid = guid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getPoll_id() {
        return poll_id;
    }

    public void setPoll_id(final Integer poll_id) {
        this.poll_id = poll_id;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setVote_count(final Integer vote_count) {
        this.vote_count = vote_count;
    }
}
