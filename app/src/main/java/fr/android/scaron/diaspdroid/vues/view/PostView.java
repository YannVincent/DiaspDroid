package fr.android.scaron.diaspdroid.vues.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.acra.ACRA;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.android.scaron.diaspdroid.DeveloperKey;
import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.activity.ShareActivity_;
import fr.android.scaron.diaspdroid.activity.YoutubeActivity;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.Comment;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.Image;
import fr.android.scaron.diaspdroid.model.Like;
import fr.android.scaron.diaspdroid.model.OpenGraphCache;
import fr.android.scaron.diaspdroid.model.People;
import fr.android.scaron.diaspdroid.model.Photo;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.model.Share;

/**
 * Created by Sébastien on 28/03/2015.
 */
@EViewGroup(R.layout.fragment_detailpostvids)
public class PostView extends LinearLayout{


    private static Logger LOGGEUR = LoggerFactory.getLogger(PostView.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "PostView";

    private final ThumbnailListener thumbnailListener = new ThumbnailListener();
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();


    final String mimeType = "text/html";
    final String encoding = "utf-8";

    @Bean
    DiasporaBean diasporaService;

    @ViewById(R.id.detailErreurPost)
    LinearLayout detailErreurPost;
    @ViewById(R.id.detailErreurPostText)
    TextView detailErreurPostText;

    @ViewById(R.id.detailPost)
    LinearLayout detailPost;

    @ViewById(R.id.detailPostAvatar)
    ImageView avatar;
    @ViewById(R.id.detailPostUser)
    TextView user;
    @ViewById(R.id.detailPostDatetime)
    TextView datetime;
    @ViewById(R.id.detailPostText)
    TextView texte;
    @ViewById(R.id.detailPostImage)
    ImageView image;
    @ViewById(R.id.detailPostImageB)
    ImageView imageB;
    @ViewById(R.id.detailPostImageC)
    ImageView imageC;
    @ViewById(R.id.detailPostImageD)
    ImageView imageD;
    @ViewById(R.id.detailPostImageE)
    ImageView imageE;
    @ViewById(R.id.detailPostImageF)
    ImageView imageF;
    @ViewById(R.id.webvideo)
    WebView webvideo;
    @ViewById(R.id.ytb_thumbnail)
    YouTubeThumbnailView thumbnail;
    @ViewById(R.id.imgplay)
    ImageView imgplay;
//    YoutubePlayerFragment youtubePlayerFragment;
    @ViewById(R.id.detailOpenGraph)
    RelativeLayout detailOpenGraph;
    @ViewById(R.id.detailOpenGraphImg)
    ImageView detailOpenGraphImg;
    @ViewById(R.id.detailOpenGraphTitle)
    TextView detailOpenGraphTitle;
    @ViewById(R.id.detailOpenGraphTxt)
    TextView detailOpenGraphTxt;
    @ViewById(R.id.detailOpenGraphWebSite)
    TextView detailOpenGraphWebSite;
    @ViewById(R.id.detailIndicsReshareText)
    TextView detailIndicsReshareText;
    @ViewById(R.id.detailIndicsLikeText)
    TextView detailIndicsLikeText;
    @ViewById(R.id.detailIndicsCommentaireText)
    TextView detailIndicsCommentaireText;
    @ViewById(R.id.detailLike)
    LinearLayout detailLike;
    @ViewById(R.id.detailRepublish)
    LinearLayout detailRepublish;
    @ViewById(R.id.detailComment)
    LinearLayout detailComment;

    public Integer postId;

    long TIME_TEMPO = 3000;

    Post post;
    Context context;
    byte[] imageAvatarDatas = null;
    byte[] imageDatas = null;
    byte[] imageLinkDatas = null;

    public PostView(Context context) {
        super(context);
        this.context = context;
    }

    public void bind(final Post post) {
        this.post = post;
        this.postId = post.getId();
        if (this.postId == 0){
            // *** Detail de post en erreur
            // Remplissage de la partie texte
            detailErreurPostText.setText(post.getText());
            detailPost.setVisibility(GONE);
            detailErreurPost.setVisibility(VISIBLE);
            return;
        }

        detailErreurPost.setVisibility(GONE);

        LOG.d(".getView videos from post " + post.getId() + "( instance id : " + this + ")");
        Map<String, String> videos = getVideo(post);
        LOG.d(".getView videos found for post " + post + " : " + videos);
        View.OnClickListener youtubeclickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (post.asYoutubeVideo && post.idYoutubeVideo != null && !post.idYoutubeVideo.isEmpty()) {
                    // Launching YoutubeActivity Screen
                    Intent i = new Intent(context, YoutubeActivity.class);
                    i.putExtra("idYoutubeVideo", post.idYoutubeVideo);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        };
        thumbnail.setOnClickListener(youtubeclickListener);
        imgplay.setOnClickListener(youtubeclickListener);

        View.OnClickListener urlclickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (post.asWebSiteUrl && post.webSiteUrl != null && !post.webSiteUrl.isEmpty()) {
                    // Launching Browser Screen
                    Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                    myWebLink.setData(Uri.parse(post.webSiteUrl));
                    myWebLink.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(myWebLink);
                }
            }
        };
        detailOpenGraph.setOnClickListener(urlclickListener);

        // On crée la fonction pour le bouton like
        View.OnClickListener likeclickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (post != null && post.getGuid() != null) {
                    aimer(post.getId());
                }
            }
        };
        // On attache la fonction au bouton like
        detailLike.setOnClickListener(likeclickListener);

        // On crée la fonction pour le bouton reshare
        View.OnClickListener reshareclickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (post != null && post.getGuid() != null) {
                    repartager(post.getGuid());
                }
            }
        };
        // On attache la fonction au bouton reshare
        detailRepublish.setOnClickListener(reshareclickListener);



        // On crée la fonction pour le bouton commenter
        View.OnClickListener commentclickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (post != null && post.getGuid() != null) {
                    commenter(post.getId());
                }
            }
        };
        // On attache la fonction au bouton commenter
        detailComment.setOnClickListener(commentclickListener);



        setPost(post);

        if (videos.containsKey("youtube")){
            // Initialize youtubeVideo with youtube thumbnail
            // 1) The view has not yet been created - we need to initialize the YouTubeThumbnailView.
            post.asYoutubeVideo = true;
            post.idYoutubeVideo = videos.get("youtube");
            LOG.d(".getView video youtube found ? " + post.asYoutubeVideo + " with ID ? " + post.idYoutubeVideo + " (url is '" + videos.get("youtubeurl") + "')");
            thumbnail.setTag(videos.get("youtube"));
            if (thumbnailListener!=null) {
                thumbnail.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);
            }

            LOG.i(".getView video youtube found ? " + post.asYoutubeVideo + " with ID ? " + post.idYoutubeVideo + " (url is '" + videos.get("youtubeurl") + "')");
        }
        refresh();

    }



    @UiThread
    public void refresh() {
        String TAG_METHOD = TAG + ".refresh : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "DiasporaConfig.MY_DIASP_ID = "+DiasporaConfig.MY_DIASP_ID+" && post==null ? "+(post==null));
        // Remplissage des indicateurs reshare/like/comments
        if (post!=null) {
            LOG.d(TAG_METHOD + "post.getInteractions()==null ? "+(post.getInteractions()==null));
            if (post.getInteractions()!=null) {
                detailIndicsReshareText.setText("" + post.getInteractions().getReshares_count());
                detailIndicsLikeText.setText("" + post.getInteractions().getLikes_count());
                detailIndicsCommentaireText.setText("" + post.getInteractions().getComments_count());

                LOG.d(TAG_METHOD + "comments ? " + post.getInteractions().getComments_count());
                for(Comment comment:post.getInteractions().getComments()){
                    LOG.d(TAG_METHOD + "comment by "+comment.getAuthor().getId());
                    if (comment.getAuthor().getId().intValue()== DiasporaConfig.MY_DIASP_ID.intValue()){
                        LOG.d(TAG_METHOD + "Je sais que j'ai commenté ce post");
                    }
                }

                LOG.d(TAG_METHOD + "likes ? " + post.getInteractions().getLikes_count());
                for(Like like:post.getInteractions().getLikes()){
                    LOG.d(TAG_METHOD + "liked by "+like.getAuthor().getId());
                    if (like.getAuthor().getId().intValue()== DiasporaConfig.MY_DIASP_ID.intValue()){
                        LOG.d(TAG_METHOD + "Je sais que j'ai aimé ce post");
                    }
                }

                LOG.d(TAG_METHOD + "reshares ? " + post.getInteractions().getReshares_count());
                for(Share reshare:post.getInteractions().getReshares()){
                    LOG.d(TAG_METHOD + "reshared by "+reshare.getAuthor_id());
                    if (reshare.getAuthor_id()== DiasporaConfig.MY_DIASP_ID.intValue()){
                        LOG.d(TAG_METHOD + "Je sais que j'ai repartagé ce post");
                    }
                }
            }
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Background
    public void aimer(Integer postID){
        String TAG_METHOD = TAG + ".aimer : ";
        LOG.d(TAG_METHOD + "Entrée");
        Post result = diasporaService.like(postID);
        showToastResult(result != null, "prise en compte de votre j'aime");

        try {
            Thread.sleep(TIME_TEMPO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Post postUpdated = diasporaService.getPost(postID);
        post = postUpdated;
        refresh();

        LOG.d(TAG_METHOD + "Sortie");
    }


    @UiThread
    public void showToastResult(boolean resultOK, String message){
        if (resultOK){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "Erreur de "+message,Toast.LENGTH_LONG).show();
        }
    }

    @Background
    public void repartager(String rootGuid){
        String TAG_METHOD = TAG + ".repartager : ";
        LOG.d(TAG_METHOD + "Entrée");
        Post result = diasporaService.reshare(rootGuid);
        showToastResult(result != null, "prise en compte de votre repartage");

        try {
            Thread.sleep(TIME_TEMPO);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Post postUpdated = diasporaService.getPost(post.getId());
        post = postUpdated;
        refresh();
        LOG.d(TAG_METHOD + "Sortie");
    }

    public void commenter(Integer postID){
        String TAG_METHOD = TAG + ".commenter : ";
        LOG.d(TAG_METHOD+ "Entrée");
        Intent i = new Intent(context,
                ShareActivity_.class);
        LOG.d(TAG_METHOD + "add Intent.EXTRA_TEXT : MainActivity");
//        i.putExtra(Intent.EXTRA_TEXT, "MainActivity");
        LOG.d(TAG_METHOD + "add Intent.EXTRA_REFERRER : " + postID);
        i.putExtra(Intent.EXTRA_REFERRER, postID);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        LOG.d(TAG_METHOD + "Sortie");
    }



    @Background
    public void setImageAvatarInView(String imagePath){
        String TAG_METHOD = TAG + ".setImageAvatarInView : ";
        LOG.d(TAG_METHOD+ "Entrée");
        if (imagePath!=null) {
            LOG.d(TAG_METHOD + "call diasporaBean.getImageFile with : " + imagePath);
            imageAvatarDatas = diasporaService.getImageFile(imagePath);
            setImageAvatarInViewUIT();
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void setImageAvatarInViewUIT(){
        String TAG_METHOD = TAG + ".setImageAvatarInViewUIT : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (imageAvatarDatas!=null) {
            LOG.d(TAG_METHOD + "converting datas to bitmap");
            Bitmap imageAvatar = BitmapFactory.decodeByteArray(imageAvatarDatas, 0, imageAvatarDatas.length);
            avatar.setImageBitmap(imageAvatar);
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

@UiThread
public void hideLastImageInView(int sizePhotos){
    switch(sizePhotos) {
        case 0 : image.setVisibility(GONE);
            imageB.setVisibility(GONE);
            imageC.setVisibility(GONE);
            imageD.setVisibility(GONE);
            imageE.setVisibility(GONE);
            imageF.setVisibility(GONE);
            break;
        case 1 : imageB.setVisibility(GONE);
            imageC.setVisibility(GONE);
            imageD.setVisibility(GONE);
            imageE.setVisibility(GONE);
            imageF.setVisibility(GONE);
            break;
        case 2 : imageC.setVisibility(GONE);
            imageD.setVisibility(GONE);
            imageE.setVisibility(GONE);
            imageF.setVisibility(GONE);
            break;
        case 3 : imageD.setVisibility(GONE);
            imageE.setVisibility(GONE);
            imageF.setVisibility(GONE);
            break;
        case 4 : imageE.setVisibility(GONE);
            imageF.setVisibility(GONE);
            break;
        case 5 :imageF.setVisibility(GONE);
            break;
        default : break;
    }
}

    @Background
    public void setImageInView(String imagePath, int id){
        String TAG_METHOD = TAG + ".setImageInView : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LOG.d(TAG_METHOD + "call diasporaBean.getImageFile with : "+imagePath);
        imageDatas = diasporaService.getImageFile(imagePath);
        setImageInViewUIT(id);
        LOG.d(TAG_METHOD + "Sortie");
    }
    @UiThread
    public void setImageInViewUIT(int id){
        String TAG_METHOD = TAG + ".setImageInViewUIT : ";
        LOG.d(TAG_METHOD+ "Entrée");
        if (imageDatas!=null) {
            LOG.d(TAG_METHOD + "converting datas to bitmap");
            Bitmap imageAvatar = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
            switch(id) {
                case 0 : image.setImageBitmap(imageAvatar);
                    break;
                case 1 : imageB.setImageBitmap(imageAvatar);
                    break;
                case 2 : imageC.setImageBitmap(imageAvatar);
                    break;
                case 3 : imageD.setImageBitmap(imageAvatar);
                    break;
                case 4 : imageE.setImageBitmap(imageAvatar);
                    break;
                case 5 : imageF.setImageBitmap(imageAvatar);
                    break;
                default : LOG.d(TAG_METHOD + "Il manque une vue image detail pour l'affichage de la photo n°"+id);
                    break;
            }
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Background
    public void setImageLinkInView(String imagePath){
        String TAG_METHOD = TAG + ".setImageLinkInView : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LOG.d(TAG_METHOD + "call diasporaBean.getImageFile with : "+imagePath);
        imageLinkDatas = diasporaService.getImageFile(imagePath);
        setImageLinkInViewUIT();
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void setImageLinkInViewUIT(){
        String TAG_METHOD = TAG + ".setImageLinkInViewUIT : ";
        LOG.d(TAG_METHOD+ "Entrée");
        if (imageLinkDatas!=null) {
            LOG.d(TAG_METHOD + "converting datas to bitmap");
            Bitmap imageAvatar = BitmapFactory.decodeByteArray(imageLinkDatas, 0, imageLinkDatas.length);
            detailOpenGraphImg.setImageBitmap(imageAvatar);
        }
        LOG.d(TAG_METHOD + "Sortie");
    }


    public void setPost(Post post) {
        LOG.d(".setComment entree with post : "+post);
        try {
            LOG.d(".setComment getSettings set params");
            // *** Entete
            // Remplissage de la partie auteur
            People author = post.getAuthor();
            if (author != null){
                Image iavatar = author.getAvatar();
                // Remplissage de l'avatar
                if (iavatar!=null){

//                    ProfilControler.putImage(avatar, iavatar.getLarge());
                    setImageAvatarInView(iavatar.getLarge());
                }
                // Remplissage du nom
                user.setText(author.getName());
            }
            // Remplissage de la date
            datetime.setText(post.getCreated_at_str());


            // *** Detail
            // Remplissage de la partie texte
            texte.setText(post.getText());
            // Remplissage de l'image
            String imageURL = post.getImage_url();
            if (imageURL!=null && !imageURL.isEmpty()){
//                ProfilControler.putImage(image, imageURL);
                setImageInView(imageURL,0);
            }else{
                ArrayList<Photo> photos = post.getPhotos();
                if (photos!=null && photos.size()>0) {
                    for (int index=0; index < photos.size(); index++) {
                        imageURL = photos.get(index).getSizes().getLarge();
                        if (imageURL != null && !imageURL.isEmpty()) {
                            setImageInView(imageURL, index);
                        }
                    }
                }
                hideLastImageInView(photos.size());
            }
            // Remplissage des contenus objets web
            Map<String, String> videoData = getVideo(post);
            if (!videoData.isEmpty()){
                if (videoData.containsKey("web")) {
                    LOG.d(".setComment set web video");
                    setWebVideo(videoData.get("web"));
                    thumbnail.setVisibility(View.GONE);
                    imgplay.setVisibility(View.GONE);
//                    sbHtml.append(videoData.get("web"));
                }else if (videoData.containsKey("youtube")){
                    LOG.d(".setComment set youtube video");
                    webvideo.setVisibility(View.GONE);
                }else{
                    thumbnail.setVisibility(View.GONE);
                    imgplay.setVisibility(View.GONE);
                    webvideo.setVisibility(View.GONE);
                }
            }else{
                thumbnail.setVisibility(View.GONE);
                imgplay.setVisibility(View.GONE);
                webvideo.setVisibility(View.GONE);
            }

            // Remplissage du open_graph_cache
            OpenGraphCache opengraph = post.getOpen_graph_cache();
            if (opengraph!=null){
                // Affichage de l'image
                String opengraphimg = opengraph.getImage();
                if (opengraphimg!=null && !opengraphimg.isEmpty()){
//                    ProfilControler.putImage(detailOpenGraphImg, opengraphimg);
                    setImageLinkInView(opengraphimg);
                }
                detailOpenGraphTitle.setText(opengraph.getTitle());
                detailOpenGraphTxt.setText(opengraph.getDescription());
                detailOpenGraphWebSite.setText(opengraph.getUrl());
                post.asWebSiteUrl=true;
                post.webSiteUrl=opengraph.getUrl();
            }else{
                detailOpenGraph.setVisibility(View.GONE);
            }

            // Remplissage des indicateurs reshare/like/comments
            detailIndicsReshareText.setText(""+post.getInteractions().getReshares_count());
            detailIndicsLikeText.setText(""+post.getInteractions().getLikes_count());
            detailIndicsCommentaireText.setText(""+post.getInteractions().getComments_count());


//            texte.loadDataWithBaseURL("file:///android_asset/", sbHtml.toString(), mimeType, encoding, "");
            LOG.d(".setComment sortie en succès");
        }catch(Throwable thr) {
            LOG.e(".setComment sortie en Erreur ("+thr.toString()+")", thr);
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    public void setWebVideo(final String htmlSrc){
        LOG.d(".setWebVideo entree avec la valeur : "+htmlSrc);
        try {
            LOG.d(".setWebVideo find youtube fragment with id  R.id.webvideo");
            if (webvideo==null){
                LOG.d(".setWebVideo gloops webVideoFragment.webView is null");
            }else {
                LOG.d(".setWebVideo yeeppee webVideoFragment.webView is not null");
                webvideo.getSettings().setJavaScriptEnabled(true);
                webvideo.loadDataWithBaseURL(null, "<html><head><meta name=\"viewport\" content=\"width=device-width; user-scalable=no; initial-scale=1.0;"+
                        " minimum-scale=1.0; maximum-scale=1.0; target-densityDpi=device-dpi;\"/></head><body>" + htmlSrc + "</body></html>", mimeType, encoding, "");
                webvideo.setVisibility(View.VISIBLE);
                webvideo.setInitialScale(1);
                webvideo.getSettings().setJavaScriptEnabled(true);
                webvideo.getSettings().setLoadWithOverviewMode(true);
                webvideo.getSettings().setUseWideViewPort(true);
                webvideo.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                webvideo.setScrollbarFadingEnabled(false);
            }
        }catch(Throwable thr) {
            LOG.e(".setWebVideo sortie en Erreur ("+thr.toString()+")", thr);
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
        LOG.d(".setWebVideo sortie");
    }

    public Map<String, String> getVideo(Post post) {
        Map mapVideo = new HashMap<String, String>();
        String videoType = getVideoType(post);
        if ("youtube".equals(videoType)) {

            String objectHtml = post.getO_embed_cache().getData().getHtml();
            String vId = null;
            Pattern pattern = Pattern.compile(".*(?:youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=)([^#\\&\\?]*).*");
            Matcher matcher = pattern.matcher(objectHtml);
            if (matcher.matches()){
                vId = matcher.group(1);
            }
            mapVideo.put("youtube",vId);
            mapVideo.put("youtubeurl", objectHtml);
        }else if ("web".equals(videoType)) {
            mapVideo.put("web",post.getO_embed_cache().getData().getHtml());
        }
        return mapVideo;
    }


    public String getVideoType(Post post) {
        if (post.getO_embed_cache() != null && post.getO_embed_cache().getData() != null
                && post.getO_embed_cache().getData().getHtml() != null){
            if (post.getO_embed_cache().getData().getHtml().contains("youtube") ) {
                return "youtube";
            }
            return "web";
        }
        return "none";
    }



    private final class ThumbnailListener implements
            YouTubeThumbnailView.OnInitializedListener,
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onInitializationSuccess(
                YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
            loader.setOnThumbnailLoadedListener(this);
            thumbnailViewToLoaderMap.put(view, loader);
            view.setImageResource(R.drawable.loading_thumbnail);
            String videoId = (String) view.getTag();
            loader.setVideo(videoId);
        }

        @Override
        public void onInitializationFailure(
                YouTubeThumbnailView view, YouTubeInitializationResult loader) {
            view.setImageResource(R.drawable.no_thumbnail);
        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
            view.setImageResource(R.drawable.no_thumbnail);
        }
    }
}
