package fr.android.scaron.diaspdroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Sébastien on 18/01/2015.
 */
public class UploadPhoto {
    //    "author_id": 7738,
    Integer author_id;
    //    "comments_count": null,
    Integer comments_count;
    //    "created_at": "2015-01-16T16:01:18Z",
    Date created_at;
//    "diaspora_handle": "tilucifer@framasphere.org",
    String diaspora_handle;
//    "guid": "d4ba17f07fc60132ce422a0000053625",
    String guid;
//    "height": 512,
    Integer height;
//    "id": 77686,
    Integer id;
//    "pending": true,
    Boolean pending;
//    "processed_image": {
//        "url": null,
//                "thumb_small": {
//            "url": null
//        },
//        "thumb_medium": {
//            "url": null
//        },
//        "thumb_large": {
//            "url": null
//        },
//        "scaled_full": {
//            "url": null
//        }
//    },
    UploadImage processed_image;
//    "public": false,
    @SerializedName("public")
    Boolean isPublic;
//    "random_string": "ed80cafa10172c4230d1",
    String random_string;
//    "remote_photo_name": "ed80cafa10172c4230d1.png",
    String remote_photo_name;
//    "remote_photo_path": "https://framasphere.org/uploads/images/",
    String remote_photo_path;
//    "status_message_guid": null,
    String status_message_guid;
//    "text": null,
    String text;
//    "tmp_old_id": null,
    Integer tmp_old_id;
//    "unprocessed_image": {
//        "url": "/uploads/images/ed80cafa10172c4230d1.png",
//        "thumb_small": {
//            "url": "/uploads/images/thumb_small_ed80cafa10172c4230d1.png"
//        },
//        "thumb_medium": {
//            "url": "/uploads/images/thumb_medium_ed80cafa10172c4230d1.png"
//        },
//        "thumb_large": {
//            "url": "/uploads/images/thumb_large_ed80cafa10172c4230d1.png"
//        },
//        "scaled_full": {
//            "url": "/uploads/images/scaled_full_ed80cafa10172c4230d1.png"
//        }
//    },
    UploadImage unprocessed_image;
//    "updated_at": "2015-01-16T16:01:18Z",
    Date updated_at;
//    "width": 512
    Integer width;

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public Integer getComments_count() {
        return comments_count;
    }

    public void setComments_count(Integer comments_count) {
        this.comments_count = comments_count;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getDiaspora_handle() {
        return diaspora_handle;
    }

    public void setDiaspora_handle(String diaspora_handle) {
        this.diaspora_handle = diaspora_handle;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public UploadImage getProcessed_image() {
        return processed_image;
    }

    public void setProcessed_image(UploadImage processed_image) {
        this.processed_image = processed_image;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getRandom_string() {
        return random_string;
    }

    public void setRandom_string(String random_string) {
        this.random_string = random_string;
    }

    public String getRemote_photo_name() {
        return remote_photo_name;
    }

    public void setRemote_photo_name(String remote_photo_name) {
        this.remote_photo_name = remote_photo_name;
    }

    public String getRemote_photo_path() {
        return remote_photo_path;
    }

    public void setRemote_photo_path(String remote_photo_path) {
        this.remote_photo_path = remote_photo_path;
    }

    public String getStatus_message_guid() {
        return status_message_guid;
    }

    public void setStatus_message_guid(String status_message_guid) {
        this.status_message_guid = status_message_guid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTmp_old_id() {
        return tmp_old_id;
    }

    public void setTmp_old_id(Integer tmp_old_id) {
        this.tmp_old_id = tmp_old_id;
    }

    public UploadImage getUnprocessed_image() {
        return unprocessed_image;
    }

    public void setUnprocessed_image(UploadImage unprocessed_image) {
        this.unprocessed_image = unprocessed_image;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
