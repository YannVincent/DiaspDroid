package fr.android.scaron.diaspdroid.vues.view;

import android.view.TextureView;
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
//    public TextView flux_list_item_post_detail;
    public WebView flux_list_item_post_detail;
    public ImageView flux_list_item_post_detail_picture;
    public TextureView flux_list_item_post_detail_video;
//    public VideoView flux_list_item_post_detail_video;
    public WebView flux_list_item_post_detail_video_web;
    //Boutons d'action
    public Button flux_list_item_like;
    public Button flux_list_item_publish;
    public  Button flux_list_item_comment;




    /*
    Code html du post vue dans framasphere
    <div class="post-content"><div data-template="status-message">
  <div class="photo_attachments">
    <a href="#" class="stream-photo-link">

        <img src="https://pod.orkz.net/uploads/images/scaled_full_b1b7c758666dda8b4ce2.jpg" class="stream-photo big_stream_photo" data-small-photo="https://pod.orkz.net/uploads/images/thumb_small_b1b7c758666dda8b4ce2.jpg" data-full-photo="https://pod.orkz.net/uploads/images/scaled_full_b1b7c758666dda8b4ce2.jpg" rel="lightbox">

    </a>


  </div>


<div class="collapsible">
  <h3><a href="/tags/microsoft" class="tag">#Microsoft</a> to invest in <a href="/tags/cyanogen" class="tag">#Cyanogen</a>, which hopes to take <a href="/tags/android" class="tag">#Android</a> from <a href="/tags/google" class="tag">#Google</a></h3>

<p><a href="http://arstechnica.com/gadgets/2015/01/microsoft-to-invest-in-cyanogen-hopes-to-take-android-away-from-google/" target="_blank">...report says that Microsoft would be a "minority investor" in a $70 million round of financing that values Cyanogen ...</a></p>
  <div class="oembed"><div data-template="oembed">
</div></div>
  <div class="opengraph"><div data-template="opengraph">



</div></div>
  <div class="poll"><div data-template="poll">
</div></div>
</div>
</div></div>



     */

}
