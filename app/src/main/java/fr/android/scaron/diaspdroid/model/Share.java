package fr.android.scaron.diaspdroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Maison on 13/01/2015.
 */
//
//    "reshare": {
//        "actor_url": null,
//        "author_id": 7738,
//        "comments_count": 0,
//        "created_at": "2015-02-10T12:51:14Z",
//        "diaspora_handle": "tilucifer@framasphere.org",
//        "facebook_id": null,
//        "favorite": false,
//        "frame_name": null,
//        "guid": "6be24ea0935101324d5b2a0000053625",
//        "id": 393477,
//        "image_height": null,
//        "image_url": null,
//        "image_width": null,
//        "interacted_at": "2015-02-10T12:51:14Z",
//        "likes_count": 0,
//        "o_embed_cache_id": null,
//        "objectId": null,
//        "object_url": null,
//        "open_graph_cache_id": null,
//        "pending": false,
//        "processed_image": null,
//        "provider_display_name": null,
//        "public": true,
//        "random_string": null,
//        "remote_photo_name": null,
//        "remote_photo_path": null,
//        "reshares_count": 0,
//        "root_guid": "e04819d092c701324d5b2a0000053625",
//        "status_message_guid": null,
//        "text": null,
//        "tumblr_ids": null,
//        "tweet_id": null,
//        "unprocessed_image": null,
//        "updated_at": "2015-02-10T12:51:14Z"
//        }
public class Share {
    String actor_url;
    int author_id;
    int comments_count;
    Date created_at;
    String diaspora_handle;
    String facebook_id;
    boolean favorite;
    String frame_name;
    String guid;
    int id;
    int image_height;
    String image_url;
    int image_width;
    Date interacted_at;
    int likes_count;
    int o_embed_cache_id;
    int objectId;
    String object_url;
    int open_graph_cache_id;
    boolean pending;
    UploadImage processed_image;
    String provider_display_name;
    @SerializedName("public")
    boolean isPublic;
    String random_string;
    String remote_photo_name;
    String remote_photo_path;
    int reshares_count;
    String root_guid;
    String status_message_guid;
    String text;
    String tumblr_ids;
    String tweet_id;
    UploadImage unprocessed_image;
    Date updated_at;

    public String getActor_url() {
        return actor_url;
    }

    public void setActor_url(final String actor_url) {
        this.actor_url = actor_url;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(final int author_id) {
        this.author_id = author_id;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(final int comments_count) {
        this.comments_count = comments_count;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(final Date created_at) {
        this.created_at = created_at;
    }

    public String getDiaspora_handle() {
        return diaspora_handle;
    }

    public void setDiaspora_handle(final String diaspora_handle) {
        this.diaspora_handle = diaspora_handle;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(final String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(final boolean favorite) {
        this.favorite = favorite;
    }

    public String getFrame_name() {
        return frame_name;
    }

    public void setFrame_name(final String frame_name) {
        this.frame_name = frame_name;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(final String guid) {
        this.guid = guid;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getImage_height() {
        return image_height;
    }

    public void setImage_height(final int image_height) {
        this.image_height = image_height;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(final String image_url) {
        this.image_url = image_url;
    }

    public int getImage_width() {
        return image_width;
    }

    public void setImage_width(final int image_width) {
        this.image_width = image_width;
    }

    public Date getInteracted_at() {
        return interacted_at;
    }

    public void setInteracted_at(final Date interacted_at) {
        this.interacted_at = interacted_at;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setIsPublic(final boolean isPublic) {
        this.isPublic = isPublic;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(final int likes_count) {
        this.likes_count = likes_count;
    }

    public int getO_embed_cache_id() {
        return o_embed_cache_id;
    }

    public void setO_embed_cache_id(final int o_embed_cache_id) {
        this.o_embed_cache_id = o_embed_cache_id;
    }

    public String getObject_url() {
        return object_url;
    }

    public void setObject_url(final String object_url) {
        this.object_url = object_url;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(final int objectId) {
        this.objectId = objectId;
    }

    public int getOpen_graph_cache_id() {
        return open_graph_cache_id;
    }

    public void setOpen_graph_cache_id(final int open_graph_cache_id) {
        this.open_graph_cache_id = open_graph_cache_id;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(final boolean pending) {
        this.pending = pending;
    }

    public UploadImage getProcessed_image() {
        return processed_image;
    }

    public void setProcessed_image(final UploadImage processed_image) {
        this.processed_image = processed_image;
    }

    public String getProvider_display_name() {
        return provider_display_name;
    }

    public void setProvider_display_name(final String provider_display_name) {
        this.provider_display_name = provider_display_name;
    }

    public String getRandom_string() {
        return random_string;
    }

    public void setRandom_string(final String random_string) {
        this.random_string = random_string;
    }

    public String getRemote_photo_name() {
        return remote_photo_name;
    }

    public void setRemote_photo_name(final String remote_photo_name) {
        this.remote_photo_name = remote_photo_name;
    }

    public String getRemote_photo_path() {
        return remote_photo_path;
    }

    public void setRemote_photo_path(final String remote_photo_path) {
        this.remote_photo_path = remote_photo_path;
    }

    public int getReshares_count() {
        return reshares_count;
    }

    public void setReshares_count(final int reshares_count) {
        this.reshares_count = reshares_count;
    }

    public String getRoot_guid() {
        return root_guid;
    }

    public void setRoot_guid(final String root_guid) {
        this.root_guid = root_guid;
    }

    public String getStatus_message_guid() {
        return status_message_guid;
    }

    public void setStatus_message_guid(final String status_message_guid) {
        this.status_message_guid = status_message_guid;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getTumblr_ids() {
        return tumblr_ids;
    }

    public void setTumblr_ids(final String tumblr_ids) {
        this.tumblr_ids = tumblr_ids;
    }

    public String getTweet_id() {
        return tweet_id;
    }

    public void setTweet_id(final String tweet_id) {
        this.tweet_id = tweet_id;
    }

    public UploadImage getUnprocessed_image() {
        return unprocessed_image;
    }

    public void setUnprocessed_image(final UploadImage unprocessed_image) {
        this.unprocessed_image = unprocessed_image;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(final Date updated_at) {
        this.updated_at = updated_at;
    }
}
