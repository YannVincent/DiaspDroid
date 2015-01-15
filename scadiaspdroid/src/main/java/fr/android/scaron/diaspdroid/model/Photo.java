package fr.android.scaron.diaspdroid.model;

import java.util.Date;

/**
 * Created by Maison on 12/01/2015.
 */
//        "id": 76300,
//        "guid": "e618aae07d910132a5c1221315d95ef2",
//        "created_at": "2015-01-13T20:37:22Z",
//        "author": {
//                "id": 78,
//                "guid": "a9fa9480b39f013119525b11fd42445c",
//                "name": "Caps",
//                "diaspora_id": "caps@diaspora-fr.org",
//                "avatar": {
//                    "small": "https://diaspora-fr.org/uploads/images/thumb_small_b9fda0453f1b5de0a4a1.jpg",
//                    "medium": "https://diaspora-fr.org/uploads/images/thumb_medium_b9fda0453f1b5de0a4a1.jpg",
//                    "large": "https://diaspora-fr.org/uploads/images/thumb_large_b9fda0453f1b5de0a4a1.jpg"
//                }
//        },
//        "sizes": {
//                "small": "https://diaspora-fr.org/uploads/images/thumb_small_1d21378b0a344a0a9d06.jpg",
//                "medium": "https://diaspora-fr.org/uploads/images/thumb_medium_1d21378b0a344a0a9d06.jpg",
//                "large": "https://diaspora-fr.org/uploads/images/scaled_full_1d21378b0a344a0a9d06.jpg"
//        },
//        "dimensions": {
//                "height": 2401,
//                "width": 3608
//        }
public class Photo {
//        "id": 76300,
    Integer id;
//    "guid": "e618aae07d910132a5c1221315d95ef2",
    String guid;
//        "created_at": "2015-01-13T20:37:22Z",
    Date created_at;
//        "author": {
//                "id": 78,
//                "guid": "a9fa9480b39f013119525b11fd42445c",
//                "name": "Caps",
//                "diaspora_id": "caps@diaspora-fr.org",
//                "avatar": {
//                    "small": "https://diaspora-fr.org/uploads/images/thumb_small_b9fda0453f1b5de0a4a1.jpg",
//                    "medium": "https://diaspora-fr.org/uploads/images/thumb_medium_b9fda0453f1b5de0a4a1.jpg",
//                    "large": "https://diaspora-fr.org/uploads/images/thumb_large_b9fda0453f1b5de0a4a1.jpg"
//                }
//        },
    People author;
//        "sizes": {
//                "small": "https://diaspora-fr.org/uploads/images/thumb_small_1d21378b0a344a0a9d06.jpg",
//                "medium": "https://diaspora-fr.org/uploads/images/thumb_medium_1d21378b0a344a0a9d06.jpg",
//                "large": "https://diaspora-fr.org/uploads/images/scaled_full_1d21378b0a344a0a9d06.jpg"
//        },
    Image sizes;


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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public People getAuthor() {
        return author;
    }

    public void setAuthor(People author) {
        this.author = author;
    }

    public Image getSizes() {
        return sizes;
    }

    public void setSizes(Image sizes) {
        this.sizes = sizes;
    }
}
