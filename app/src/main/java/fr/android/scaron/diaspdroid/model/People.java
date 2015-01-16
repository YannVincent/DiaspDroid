package fr.android.scaron.diaspdroid.model;

/**
 * Created by Maison on 11/01/2015.
 */
public class People {
    //id": 7449,
    Integer id;
    //"guid": "50b8df903074013219742a0000053625",
    String guid;
    //"name": "Julien",
    String name;
    //"diaspora_id": "julien@framasphere.org",
    String diaspora_id;
    //"avatar"
    Image avatar;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiaspora_id() {
        return diaspora_id;
    }

    public void setDiaspora_id(String diaspora_id) {
        this.diaspora_id = diaspora_id;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }
}
