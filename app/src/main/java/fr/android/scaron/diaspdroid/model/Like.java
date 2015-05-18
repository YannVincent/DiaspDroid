package fr.android.scaron.diaspdroid.model;

import java.util.Date;

/**
 * Created by Sébastien on 13/01/2015.
 */
public class Like {
    /**
     * id": 932474,
     "guid": "15e474f0dfd30132c4282a0000053625",
     "author": {
         "id": 7738,
         "guid": "1f9bd9b030fc013219722a0000053625",
         "name": "Sebastien Caron",
         "diaspora_id": "tilucifer@framasphere.org",
         "avatar": {
             "small": "https://framasphere.org/uploads/images/thumb_small_93779d6fd285331e91dd.jpg",
             "medium": "https://framasphere.org/uploads/images/thumb_medium_93779d6fd285331e91dd.jpg",
             "large": "https://framasphere.org/uploads/images/thumb_large_93779d6fd285331e91dd.jpg"
             }
         },
     "created_at": "2015-05-18T21:30:53.431Z"
     */
    Integer id;
    String guid;
    People author;
    Date created_at;

    public People getAuthor() {
        return author;
    }

    public void setAuthor(final People author) {
        this.author = author;
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(final Date created_at) {
        this.created_at = created_at;
    }
}
