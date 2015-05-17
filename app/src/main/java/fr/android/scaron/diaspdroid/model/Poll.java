package fr.android.scaron.diaspdroid.model;

import java.util.List;

/**
 * Created by Sébastien on 18/05/2015.
 */
public class Poll {

    /**
     * {
     "poll_id": 624,
     "post_id": 703325,
     "question": "Framasphere est passé en mode 0.5, vous aimez?",
     "poll_answers": [
     {
     "id": 2251,
     "answer": "1) Oui, mon chat a miaulé de plaisir en voyant la nouvelle interface! ",
     "poll_id": 624,
     "guid": "068fdaa0df080132c41e2a0000053625",
     "vote_count": 0
     },
     {
     "id": 2252,
     "answer": "2) Non, ma mère fait des meilleurs sites web!",
     "poll_id": 624,
     "guid": "068fe470df080132c41e2a0000053625",
     "vote_count": 0
     },
     {
     "id": 2253,
     "answer": "3) Je ne me prononce pas car je déteste internet, je préfère le minitel et quand il pleut!",
     "poll_id": 624,
     "guid": "068fee00df080132c41e2a0000053625",
     "vote_count": 2
     }
     ],
     "participation_count": 2
     }
     */

    Integer poll_id;
    Integer post_id;
    String question;
    List<PollAnswer> poll_answers;
    Integer participation_count;

    public List<PollAnswer> getPoll_answers() {
        return poll_answers;
    }

    public void setPoll_answers(final List<PollAnswer> poll_answers) {
        this.poll_answers = poll_answers;
    }

    public Integer getPoll_id() {
        return poll_id;
    }

    public void setPoll_id(final Integer poll_id) {
        this.poll_id = poll_id;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(final Integer post_id) {
        this.post_id = post_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(final String question) {
        this.question = question;
    }

    public Integer getParticipation_count() {
        return participation_count;
    }

    public void setParticipation_count(final Integer participation_count) {
        this.participation_count = participation_count;
    }
}
