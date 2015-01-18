package fr.android.scaron.diaspdroid.model;

/**
 * Created by Sébastien on 18/01/2015.
 */
public class UploadImage {
//        "url": "/uploads/images/ed80cafa10172c4230d1.png",
    String url;
//        "thumb_small": {
//            "url": "/uploads/images/thumb_small_ed80cafa10172c4230d1.png"
//        },
    UploadThumbImage thumb_small;
//        "thumb_medium": {
//            "url": "/uploads/images/thumb_medium_ed80cafa10172c4230d1.png"
//        },
    UploadThumbImage thumb_medium;
//        "thumb_large": {
//            "url": "/uploads/images/thumb_large_ed80cafa10172c4230d1.png"
//        },
    UploadThumbImage thumb_large;
//        "scaled_full": {
//            "url": "/uploads/images/scaled_full_ed80cafa10172c4230d1.png"
//        }
    UploadThumbImage scaled_Full;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UploadThumbImage getThumb_small() {
        return thumb_small;
    }

    public void setThumb_small(UploadThumbImage thumb_small) {
        this.thumb_small = thumb_small;
    }

    public UploadThumbImage getThumb_medium() {
        return thumb_medium;
    }

    public void setThumb_medium(UploadThumbImage thumb_medium) {
        this.thumb_medium = thumb_medium;
    }

    public UploadThumbImage getThumb_large() {
        return thumb_large;
    }

    public void setThumb_large(UploadThumbImage thumb_large) {
        this.thumb_large = thumb_large;
    }

    public UploadThumbImage getScaled_Full() {
        return scaled_Full;
    }

    public void setScaled_Full(UploadThumbImage scaled_Full) {
        this.scaled_Full = scaled_Full;
    }
}
