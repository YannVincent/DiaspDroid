package fr.android.scaron.diaspdroid.model;

import java.util.Date;

/**
 * Created by Maison on 13/01/2015.
 */
public class Comment {

//    {
//        "id": 245281,
//            "guid": "677102c07bc00132a5a3221315d95ef2",
//            "text": "Faut pas crier trop fort qu'on se trouve plut\u00f4t pas b\u00eate, c'est toujours la qu'on tombe sur un mec qui va te faire passer pour un idiot fini ! :D",
//            "author": {
//            "id": 103,
//                "guid": "f4130fc32ed91f6a",
//                "name": "dada",
//                "diaspora_id": "dada@diaspora-fr.org",
//                "avatar": {
//                "small": "https://diaspora-fr.org/uploads/images/thumb_small_47348508d4b08a2b9dd1.png",
//                    "medium": "https://diaspora-fr.org/uploads/images/thumb_medium_47348508d4b08a2b9dd1.png",
//                    "large": "https://diaspora-fr.org/uploads/images/thumb_large_47348508d4b08a2b9dd1.png"
//                }
//            },
//        "created_at": "2015-01-11T13:03:55Z"
//    },

    Integer id;
    String guid;
    String text;
    People author;
    Date created_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public People getAuthor() {
        return author;
    }

    public void setAuthor(People author) {
        this.author = author;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
