package fr.android.scaron.diaspdroid.model;

/**
 * Created by Sébastien on 18/05/2015.
 */
public class User {
    /**
     * {
     "id": 7738,
     "guid": "1f9bd9b030fc013219722a0000053625",
     "name": "Sebastien Caron",
     "diaspora_id": "tilucifer@framasphere.org",
     "relationship": false,
     "block": false,
     "contact": false,
     "is_own_profile": false,
     "profile": {
         "id": 7265,
         "tags": [
         "grenoble",
         "fanfare",
         "trompette",
         "codeur",
         "fetard"
         ],
         "bio": "",
         "location": "Grenoble",
         "gender": "Mâle",
         "birthday": "December 16 1979",
         "searchable": true,
             "avatar": {
             "small": "https://framasphere.org/uploads/images/thumb_small_93779d6fd285331e91dd.jpg",
             "medium": "https://framasphere.org/uploads/images/thumb_medium_93779d6fd285331e91dd.jpg",
             "large": "https://framasphere.org/uploads/images/thumb_large_93779d6fd285331e91dd.jpg"
             }
         }
     }
     */

    Integer id;
    String guid;
    String name;
    String diaspora_id;
    Boolean relationship;
    Boolean block;
    Boolean contact;
    Boolean is_own_contact;
    Profile profile;

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(final Boolean block) {
        this.block = block;
    }

    public Boolean getContact() {
        return contact;
    }

    public void setContact(final Boolean contact) {
        this.contact = contact;
    }

    public String getDiaspora_id() {
        return diaspora_id;
    }

    public void setDiaspora_id(final String diaspora_id) {
        this.diaspora_id = diaspora_id;
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

    public Boolean getIs_own_contact() {
        return is_own_contact;
    }

    public void setIs_own_contact(final Boolean is_own_contact) {
        this.is_own_contact = is_own_contact;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(final Profile profile) {
        this.profile = profile;
    }

    public Boolean getRelationship() {
        return relationship;
    }

    public void setRelationship(final Boolean relationship) {
        this.relationship = relationship;
    }
}
