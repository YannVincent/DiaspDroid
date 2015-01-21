package fr.android.scaron.diaspdroid.vues.view;

import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import fr.android.scaron.diaspdroid.model.Post;

/**
 * Created by CARON-08651 on 16/01/2015.
 */
public class PostView {

    public Post post;

    // Entete du post
    public ImageView flux_list_item_post_avatar;
    public TextView flux_list_item_post_user;
    public TextView flux_list_item_post_datetime;
    // Detail du post
    public TextView flux_list_item_post_detail;
    public ImageView flux_list_item_post_detail_picture;
    public VideoView flux_list_item_post_detail_video;
    public WebView flux_list_item_post_detail_video_web;
    //Boutons d'action
    public Button flux_list_item_like;
    public Button flux_list_item_publish;
    public  Button flux_list_item_comment;


}
