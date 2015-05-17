package fr.android.scaron.diaspdroid.model;

/**
 * Created by Sebastien on 03/05/2015.
 * [
 {
 "id": 1,
 "guid": "7f54cbe0bd850131f18f2a0000053625",
 "name": "podmin@framasphere.org",
 "avatar": "https://framasphere.org/uploads/images/thumb_medium_ccb2b215f36c8c084e47.png",
 "handle": "podmin@framasphere.org",
 "url": "/people/7f54cbe0bd850131f18f2a0000053625"
 },
 {
 "id": 7199,
 "guid": "a90ee7102fb90132196e2a0000053625",
 "name": "Globulle Cdw",
 "avatar": "https://framasphere.org/uploads/images/thumb_medium_8c9069c2049385d5af81.JPG",
 "handle": "globulle@framasphere.org",
 "url": "/people/a90ee7102fb90132196e2a0000053625"
 },
 {
 "id": 7891,
 "guid": "556a82e03130013219762a0000053625",
 "name": "Martin Le Blevennec",
 "avatar": "https://framasphere.org/uploads/images/thumb_medium_36ab8ccad0671cc65659.jpg",
 "handle": "martinlb@framasphere.org",
 "url": "/people/556a82e03130013219762a0000053625"
 },
 {
 "id": 16023,
 "guid": "317bada053a501326e692a0000053625",
 "name": "? Boob l'eponge ? J'accepte tt le monde en ami :)",
 "avatar": "https://framasphere.org/uploads/images/thumb_medium_655ca65497d1dc5f2dc8.png",
 "handle": "boobleponge@framasphere.org",
 "url": "/people/317bada053a501326e692a0000053625"
 },
 {
 "id": 20775,
 "guid": "0754e1f06864013278af2a0000053625",
 "name": "caravane des alternatives",
 "avatar": "https://framasphere.org/uploads/images/thumb_medium_458d1db8817cd49eed5e.jpg",
 "handle": "caravane_desalternatives@framasphere.org",
 "url": "/people/0754e1f06864013278af2a0000053625"
 },
 {
 "id": 33093,
 "guid": "e30466d0c9e10132eaf52a0000053625",
 "name": "lereve_dupeupledesarbres@framasphere.org",
 "avatar": "https://framasphere.org/uploads/images/thumb_medium_57a0126256e285711240.jpg",
 "handle": "lereve_dupeupledesarbres@framasphere.org",
 "url": "/people/e30466d0c9e10132eaf52a0000053625"
 }
 ]
 */

public class Contact {
    Integer id;
    String guid;
    String name;
    String avatar;
    String handle;
    String url;

    public static Contact createContactErreur(String pTextErreur){
        Contact contactError = new Contact();
        contactError.setId(0);
        contactError.setName(pTextErreur);
        return contactError;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(final String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(final String handle) {
        this.handle = handle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
