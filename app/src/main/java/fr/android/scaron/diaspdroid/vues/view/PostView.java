package fr.android.scaron.diaspdroid.vues.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.android.scaron.diaspdroid.DeveloperKey;
import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.activity.YoutubeActivity;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.DiasporaControler;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.controler.ProfilControler;
import fr.android.scaron.diaspdroid.model.Image;
import fr.android.scaron.diaspdroid.model.OpenGraphCache;
import fr.android.scaron.diaspdroid.model.People;
import fr.android.scaron.diaspdroid.model.Post;
//import fr.android.scaron.diaspdroid.vues.fragment.YoutubePlayerFragment;

/**
 * Created by Sébastien on 28/03/2015.
 */
@EViewGroup(R.layout.fragment_detailpostvids)
public class PostView extends LinearLayout{


    private static Logger LOGGEUR = LoggerFactory.getLogger(PostView.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);

    private final ThumbnailListener thumbnailListener = new ThumbnailListener();
    private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();


    final String mimeType = "text/html";
    final String encoding = "utf-8";

    @Bean
    DiasporaBean diasporaService;

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

    Post post;
    Context context;


    public PostView(Context context) {
        super(context);
        this.context = context;
    }

    public void bind(final Post post) {
        this.post = post;
        LOG.d(".getView videos from post");
        Map<String, String> videos = getVideo(post);
        LOG.d(".getView videos found for post "+post+" : "+videos);
        View.OnClickListener youtubeclickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (post.asYoutubeVideo && post.idYoutubeVideo != null && !post.idYoutubeVideo.isEmpty()) {
                    // Launching YoutubeActivity Screen
                    Intent i = new Intent(context, YoutubeActivity.class);
                    i.putExtra("idYoutubeVideo", post.idYoutubeVideo);
                    context.startActivity(i);
                }
            }
        };
        thumbnail.setOnClickListener(youtubeclickListener);
        imgplay.setOnClickListener(youtubeclickListener);

        View.OnClickListener urlclickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (post.asWebSiteUrl && post.webSiteUrl!=null && !post.webSiteUrl.isEmpty()){
                    // Launching Browser Screen
                    Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                    myWebLink.setData(Uri.parse(post.webSiteUrl));
                    context.startActivity(myWebLink);
                }
            }
        };
        detailOpenGraph.setOnClickListener(urlclickListener);

        // On crée la fonction pour le bouton like
        View.OnClickListener likeclickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (post!=null && post.getGuid()!=null){
                    FutureCallback<Response<String>> likeCallBack = new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
                            String methodName = ".getView likeCallBack onCompleted: ";
                            boolean resultOK = DiasporaControler.onCompleteRepartager(e, result);
                            if (!resultOK){
                                if (context==null){
                                    LOG.e(methodName + "Le contexte est vide et empêche un traitement");
                                    return;
                                }
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                final AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.setIcon(R.drawable.ic_launcher);
                                alertDialog.setTitle("PB Accès");
                                alertDialog.setMessage("Le like est impossible");
                                alertDialog.show();
                                return;
                            }
                        }
                    };
                    DiasporaControler.aimer(post.getId(), likeCallBack, false);
                }
            }
        };
        // On attache la fonction au bouton like
        detailLike.setOnClickListener(likeclickListener);

        // On crée la fonction pour le bouton reshare
        View.OnClickListener reshareclickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (post!=null && post.getGuid()!=null){
                    repartager(post.getGuid());
//                    diasporaService.reshare(post.getGuid());
                }
            }
        };
        // On attache la fonction au bouton reshare
        detailRepublish.setOnClickListener(reshareclickListener);
        setPost(post);

        if (videos.containsKey("youtube")){
            // Initialize youtubeVideo with youtube thumbnail
            // 1) The view has not yet been created - we need to initialize the YouTubeThumbnailView.
            post.asYoutubeVideo = true;
            post.idYoutubeVideo = videos.get("youtube");
            LOG.d(".getView video youtube found ? "+post.asYoutubeVideo+" with ID ? "+post.idYoutubeVideo + " (url is '"+videos.get("youtubeurl")+"')");
            thumbnail.setTag(videos.get("youtube"));
            thumbnail.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);


            LOG.i(".getView video youtube found ? " + post.asYoutubeVideo + " with ID ? " + post.idYoutubeVideo + " (url is '" + videos.get("youtubeurl") + "')");
        }

    }

    @Background
    public void repartager(String rootGuid){
        diasporaService.reshare(post.getGuid());
    }

    public void setPost(Post post) {
        LOG.d(".setPost entree with post : "+post);
        try {
            LOG.d(".setPost getSettings set params");
            // *** Entete
            // Remplissage de la partie auteur
            People author = post.getAuthor();
            if (author != null){
                Image iavatar = author.getAvatar();
                // Remplissage de l'avatar
                if (iavatar!=null){
                    ProfilControler.putImage(avatar, iavatar.getLarge());
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
                ProfilControler.putImage(image, imageURL);
            }
            // Remplissage des contenus objets web
            Map<String, String> videoData = getVideo(post);
            if (!videoData.isEmpty()){
                if (videoData.containsKey("web")) {
                    LOG.d(".setPost set web video");
                    setWebVideo(videoData.get("web"));
                    thumbnail.setVisibility(View.GONE);
                    imgplay.setVisibility(View.GONE);
//                    sbHtml.append(videoData.get("web"));
                }else if (videoData.containsKey("youtube")){
                    LOG.d(".setPost set youtube video");
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
                    ProfilControler.putImage(detailOpenGraphImg, opengraphimg);
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
            LOG.d(".setPost sortie en succès");
        }catch(Throwable thr) {
            LOG.e(".setPost sortie en Erreur ("+thr.toString()+")", thr);
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
