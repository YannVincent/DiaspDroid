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
}
