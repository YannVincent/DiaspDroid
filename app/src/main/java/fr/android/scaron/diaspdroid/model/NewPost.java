package fr.android.scaron.diaspdroid.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sébastien on 01/05/2015.
 */
public class NewPost {

    //{"status_message":{"text":"héhé ^^"},"aspect_ids":"all_aspects","photos":"133682","location_coords":"","poll_question":"","poll_answers":""}

    StatusMessage status_message;

    //"aspects":[{"id":9795,"name":"Connaissances","selected":true},{"id":9794,"name":"Travail","selected":true},
    // {"id":9793,"name":"Amis","selected":true},{"id":9792,"name":"Famille","selected":true}]
    String aspect_ids;

    String photos;

    String location_coords;

    String poll_question;

    String poll_answers;


    public StatusMessage getStatus_message() {
        return status_message;
    }

    public void setStatus_message(final StatusMessage status_message) {
        this.status_message = status_message;
    }

    public String getAspect_ids() {
        aspect_ids = "all_aspects";
        return aspect_ids;
    }

    public void setAspect_ids(final String aspect_ids) {
        this.aspect_ids = aspect_ids;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(final String photos) {
        this.photos = photos;
    }

    public String getLocation_coords() {
        return location_coords;
    }

    public void setLocation_coords(final String location_coords) {
        this.location_coords = location_coords;
    }

    public String getPoll_question() {
        return poll_question;
    }

    public void setPoll_question(final String poll_question) {
        this.poll_question = poll_question;
    }

    public String getPoll_answers() {
        return poll_answers;
    }

    public void setPoll_answers(final String poll_answers) {
        this.poll_answers = poll_answers;
    }
}
