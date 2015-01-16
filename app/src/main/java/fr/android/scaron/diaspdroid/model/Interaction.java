package fr.android.scaron.diaspdroid.model;

import java.util.ArrayList;

/**
 * Created by Maison on 13/01/2015.
 */
public class Interaction {

//        "likes": [
//
//        ],
    //TODO a determiner
        ArrayList<Like> likes;
//        "reshares": [
//
//        ],
//    //TODO a determiner
    ArrayList<Share> reshares;

//        "comments_count": 7,
    Integer comments_count;
//                "likes_count": 1,
    Integer likes_count;
//                "reshares_count": 0,
    Integer reshares_count;

    ArrayList<Comment> comments;

    public ArrayList<Like> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Like> likes) {
        this.likes = likes;
    }

    public ArrayList<Share> getReshares() {
        return reshares;
    }

    public void setReshares(ArrayList<Share> reshares) {
        this.reshares = reshares;
    }

    public Integer getComments_count() {
        return comments_count;
    }

    public void setComments_count(Integer comments_count) {
        this.comments_count = comments_count;
    }

    public Integer getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(Integer likes_count) {
        this.likes_count = likes_count;
    }

    public Integer getReshares_count() {
        return reshares_count;
    }

    public void setReshares_count(Integer reshares_count) {
        this.reshares_count = reshares_count;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
