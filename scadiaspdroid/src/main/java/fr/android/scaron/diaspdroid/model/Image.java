package fr.android.scaron.diaspdroid.model;

/**
 * Created by Maison on 11/01/2015.
 */
public class Image {
    //"small": "https://framasphere.org/uploads/images/thumb_small_ab1d6560034de7d0402f.jpg",
    String small;
    //"medium": "https://framasphere.org/uploads/images/thumb_medium_ab1d6560034de7d0402f.jpg",
    String medium;
    //"large": "https://framasphere.org/uploads/images/thumb_large_ab1d6560034de7d0402f.jpg"
    String large;

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}
