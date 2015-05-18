package fr.android.scaron.diaspdroid.model;

import java.util.List;

/**
 * Created by Sébastien on 18/05/2015.
 */
public class Profile {
    /**
     * {
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
     */
    Integer id;
    List<String> tags;
    String bio;
    String location;
    String gender;
    String birthday;
    Boolean searchable;
    Image avatar;

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(final Image avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(final String bio) {
        this.bio = bio;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(final String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public Boolean getSearchable() {
        return searchable;
    }

    public void setSearchable(final Boolean searchable) {
        this.searchable = searchable;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(final List<String> tags) {
        this.tags = tags;
    }
}
