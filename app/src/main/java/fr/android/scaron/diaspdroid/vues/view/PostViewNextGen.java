package fr.android.scaron.diaspdroid.vues.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.activity.MainActivity;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.OpenGraphCache;
import fr.android.scaron.diaspdroid.model.Photo;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.fragment.CommentsFragment_;

/**
 * Created by Sébastien on 20/05/2015.
 */
@EViewGroup(R.layout.postnextgen)
public class PostViewNextGen extends LinearLayout {
    private static Logger LOGGEUR = LoggerFactory.getLogger(PostView.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = PostViewNextGen.class.getSimpleName();
    final String mimeType = "text/html";
    final String encoding = "utf-8";

    long TIME_TEMPO = 3000;

    @Bean
    DiasporaBean diasporaService;

//    @ViewById(R.id.scrollpostnextgen)
//    ScrollView scrollpostnextgen;
    @ViewById(R.id.postnextgen_avatar)
    ImageView postnextgen_avatar;
    @ViewById(R.id.postnextgen_user)
    TextView postnextgen_user;
    @ViewById(R.id.postnextgen_datetime)
    TextView postnextgen_datetime;
    @ViewById(R.id.scrollpostnextgen_images)
    HorizontalScrollView scrollpostnextgen_images;
    @ViewById(R.id.postnextgen_images)
    LinearLayout postnextgen_images;
    @ViewById(R.id.scrollpostnextgen_text)
    TextView scrollpostnextgen_text;

    @ViewById(R.id.scrollpostnextgen_webvideo)
    WebView scrollpostnextgen_webvideo;

    @ViewById(R.id.scrollpostnextgen_detailOpenGraph)
    RelativeLayout detailOpenGraph;
    @ViewById(R.id.scrollpostnextgen_detailOpenGraphImg)
    ImageView detailOpenGraphImg;
    @ViewById(R.id.scrollpostnextgen_detailOpenGraphTitle)
    TextView detailOpenGraphTitle;
    @ViewById(R.id.scrollpostnextgen_detailOpenGraphTxt)
    TextView detailOpenGraphTxt;
    @ViewById(R.id.scrollpostnextgen_detailOpenGraphWebSite)
    TextView detailOpenGraphWebSite;


    @ViewById(R.id.scrollpostnextgen_detailIndicsReshare)
    LinearLayout detailIndicsReshare;
    @ViewById(R.id.scrollpostnextgen_detailIndicsLike)
    LinearLayout detailIndicsLike;
    @ViewById(R.id.scrollpostnextgen_detailIndicsCommentaire)
    LinearLayout detailIndicsCommentaire;
    @ViewById(R.id.scrollpostnextgen_detailIndicsReshareText)
    TextView detailIndicsReshareText;
    @ViewById(R.id.scrollpostnextgen_detailIndicsLikeText)
    TextView detailIndicsLikeText;
    @ViewById(R.id.scrollpostnextgen_detailIndicsCommentaireText)
    TextView detailIndicsCommentaireText;


    @ViewById(R.id.scrollpostnextgen_detailLike)
    LinearLayout detailLike;
    @ViewById(R.id.scrollpostnextgen_detailRepublish)
    LinearLayout detailRepublish;
    @ViewById(R.id.scrollpostnextgen_detailComment)
    LinearLayout detailComment;


    Post post;
    Context context;

    public PostViewNextGen(final Context context) {
        super(context);
        this.context = context;
    }

    public void bind(final Post post){
        String TAG_METHOD = TAG + ".getBitmap >";
        LOG.d(TAG_METHOD + "Entrée");
        this.post = post;
        LOG.d(TAG_METHOD + "On appel la focntion de traitement des photos");
        addAvatar();
        addPhotos();
        //Si on a plusieurs photos on affiche la scrollbar pour indiquer qu'il y a d'autres photos à consulter
        if (post.getPhotos()!=null&&post.getPhotos().size()>1){
            scrollpostnextgen_images.setScrollbarFadingEnabled(false);
        }
        postnextgen_user.setText(post.getAuthor().getName());
        postnextgen_datetime.setText(post.getCreated_at_str());
        scrollpostnextgen_text.setText(Html.fromHtml(post.getText()));
        setVideo();
        setOpenGraphView();
        // Remplissage des indicateurs reshare/like/comments
        detailIndicsReshareText.setText(""+post.getInteractions().getReshares_count());
        detailIndicsLikeText.setText(""+post.getInteractions().getLikes_count());
        detailIndicsCommentaireText.setText(""+post.getInteractions().getComments_count());

        //Add buttons fonctions
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
                    commenter(post);
                }
            }
        };
        // On attache la fonction au bouton commenter
        detailComment.setOnClickListener(commentclickListener);

        // On crée la fonction pour le bouton affichage des commentaires
        View.OnClickListener commentsclickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                getShowComments();
                showComments();
            }
        };
        // On attache la fonction au bouton commenter
        detailIndicsCommentaire.setOnClickListener(commentsclickListener);

        LOG.d(TAG_METHOD + "Sortie");
    }

    public void aimer(Integer postID){
        String TAG_METHOD = TAG + ".aimer : ";
        LOG.d(TAG_METHOD + "Entrée");
        aimerTask(postID);
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Background
    public void aimerTask(Integer postID){
        String TAG_METHOD = TAG + ".aimerTask : ";
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

    public void repartager(String rootGuid){
        String TAG_METHOD = TAG + ".repartager : ";
        LOG.d(TAG_METHOD + "Entrée");
        repartagerTask(rootGuid);
        LOG.d(TAG_METHOD + "Sortie");
    }
    @Background
    public void repartagerTask(String rootGuid){
        String TAG_METHOD = TAG + ".repartagerTask : ";
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
        LOG.d(TAG_METHOD + "Sortie");
    }

    public void commenter(Post postParent){
        String TAG_METHOD = TAG + ".commenter : ";
        LOG.d(TAG_METHOD + "Entrée");
        ((MainActivity) DiasporaConfig.APPLICATION_CONTEXT).listItemClicked("Partagez", postParent);
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Background
    public void addAvatar(){
        String TAG_METHOD = TAG + ".addAvatar > ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "On récupère les données de l'image pour l'url : " + post.getAuthor().getAvatar().getLarge());
        byte[] imageDatas = diasporaService.getImageFile(post.getAuthor().getAvatar().getLarge());
        LOG.d(TAG_METHOD + "imageData is null ? " + (imageDatas == null));
        if (imageDatas != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
            if (bitmap==null){
                LOG.e(TAG_METHOD + "Erreur de traitement innatendu pour l'image");
            }else {
                addImageViewAvatar(bitmap);
            }
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void addImageViewAvatar(Bitmap bitmap){
        String TAG_METHOD = TAG + ".addImageView > ";
        LOG.d(TAG_METHOD + "Entrée");
        postnextgen_avatar.setImageBitmap(bitmap);
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Background
    public void addPhotos(){
        String TAG_METHOD = TAG + ".addPhotos > ";
        LOG.d(TAG_METHOD + "Entrée");
        ArrayList<Photo> photos = post.getPhotos();
        if (photos!=null && photos.size()>0){
            LOG.d(TAG_METHOD + post.getPhotos().size()+" photos à ajouter");
        }else{
            LOG.d(TAG_METHOD +" aucune photo à ajouter");
            LOG.d(TAG_METHOD + "Sortie");
            return;
        }

        int photoSize = photos.size();
        int indexPhoto = 0;
        for (Photo photo:photos) {
            LOG.d(TAG_METHOD + "On récupère les données de l'image pour l'url : " + photo.getSizes().getMedium());
            indexPhoto++;
            byte[] imageDatas = diasporaService.getImageFile(photo.getSizes().getLarge());
            LOG.d(TAG_METHOD + "imageData is null ? "+(imageDatas==null));
            if (imageDatas != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
                if (bitmap==null){
                    LOG.e(TAG_METHOD + "Erreur de traitement innatendu pour l'image");
                }else {
                    final ImageView imageView = new ImageView(context);
                    imageView.setTag(post.getId());
                    imageView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String TAG_METHOD = "ImageView.onClick";
                            LOG.d(TAG_METHOD + "Entrée");
                            // TODO Auto-generated method stub
                            LOG.d(TAG_METHOD + imageView.getTag());
                        }
                    });

                    //Test autre méthode pour positionner l'image
                    DisplayMetrics metrics = new DisplayMetrics();
                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    display.getMetrics(metrics);
                    int width = metrics.widthPixels-(int)dipToPixels(context, 30);
////                    Display display = this.getDisplay();
//                    Point size = new Point();
//                    display.getSize(size);
//                    int width = size.x;
//                    //            int height = size.y;
//                    Matrix mat = new Matrix();
                    int imgHeight = bitmap.getHeight();
                    int imgWidth = bitmap.getWidth();
                    LOG.d(TAG_METHOD + " calcul du ratio : (long)" + imgWidth + "/(long)" + imgHeight + ";");
                    double ratio = (double) ((double) imgWidth / (double) imgHeight);
                    LOG.d(TAG_METHOD + " calcul de la nouvelle largeur : = " + width);
                    int newWidth = width;
                    LOG.d(TAG_METHOD + " calcul de la nouvelle hauteur : (float)(" + newWidth + " / " + ratio + ") = " + (float) (newWidth / ratio) + ";");
                    int newHeight = Integer.parseInt("" + (int) (newWidth / ratio));

                    imageView.setImageBitmap(bitmap);
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(newWidth, newHeight));
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    addImageViewInHScroll(imageView, (indexPhoto==photoSize));
                }
            }
        }
        LOG.d(TAG_METHOD + "Sortie");
    }
    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
    @UiThread
    public void addImageViewInHScroll(ImageView imageView, boolean isLast){
        String TAG_METHOD = TAG + ".addPhotos";
        LOG.d(TAG_METHOD + "Entrée");
        postnextgen_images.addView(imageView);
        if (!isLast) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setMinimumWidth((int)dipToPixels(context, 30));
            postnextgen_images.addView(linearLayout);
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    public void setOpenGraphView(){
        // Remplissage du open_graph_cache
        OpenGraphCache opengraph = post.getOpen_graph_cache();
        if (opengraph!=null){
            // Affichage de l'image
            String opengraphimg = opengraph.getImage();
            if (opengraphimg!=null && !opengraphimg.isEmpty()){
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
    }


    @Background
    public void setImageLinkInView(String imagePath){
        String TAG_METHOD = TAG + ".setImageLinkInView : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LOG.d(TAG_METHOD + "call diasporaBean.getImageFile with : "+imagePath);
        byte[] imageLinkDatas = diasporaService.getImageFile(imagePath);
        setImageLinkInViewUIT(imageLinkDatas);
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void setImageLinkInViewUIT(byte[] imageLinkDatas){
        String TAG_METHOD = TAG + ".setImageLinkInViewUIT : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (imageLinkDatas!=null) {
            LOG.d(TAG_METHOD + "converting datas to bitmap");
            Bitmap imageAvatar = BitmapFactory.decodeByteArray(imageLinkDatas, 0, imageLinkDatas.length);
            detailOpenGraphImg.setImageBitmap(imageAvatar);
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    public void setVideo(){
        // Remplissage des contenus objets web
        Map<String, String> videoData = getVideo(post);
        if (!videoData.isEmpty()){
            if (videoData.containsKey("web")) {
                LOG.d(".setComment set web video");
                setWebVideo(videoData.get("web"));
            }else if (videoData.containsKey("youtube")){
                LOG.d(".setComment set youtube video");
                scrollpostnextgen_webvideo.setVisibility(View.GONE);
            }else{
                scrollpostnextgen_webvideo.setVisibility(View.GONE);
            }
        }else{
            scrollpostnextgen_webvideo.setVisibility(View.GONE);
        }
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

            //TODO a supprimer si on remet du youtube
            mapVideo.put("web",objectHtml);
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

    public void setWebVideo(final String htmlSrc){
        LOG.d(".setWebVideo entree avec la valeur : " + htmlSrc);
        try {
            LOG.d(".setWebVideo find youtube fragment with id  R.id.scrollpostnextgen_webvideo");
            if (scrollpostnextgen_webvideo==null){
                LOG.d(".setWebVideo gloops webVideoFragment.webView is null");
            }else {
                LOG.d(".setWebVideo yeeppee webVideoFragment.webView is not null");
                scrollpostnextgen_webvideo.getSettings().setJavaScriptEnabled(true);
                scrollpostnextgen_webvideo.loadDataWithBaseURL(null, "<html><head><meta name=\"viewport\" content=\"width=device-width; user-scalable=no; initial-scale=1.0;"+
                        " minimum-scale=1.0; maximum-scale=1.0; target-densityDpi=device-dpi;\"/></head><body>" + htmlSrc + "</body></html>", mimeType, encoding, "");
                scrollpostnextgen_webvideo.setVisibility(View.VISIBLE);
                scrollpostnextgen_webvideo.setInitialScale(1);
                scrollpostnextgen_webvideo.getSettings().setJavaScriptEnabled(true);
                scrollpostnextgen_webvideo.getSettings().setLoadWithOverviewMode(true);
                scrollpostnextgen_webvideo.getSettings().setUseWideViewPort(true);
                scrollpostnextgen_webvideo.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                scrollpostnextgen_webvideo.setScrollbarFadingEnabled(false);
            }
        }catch(Throwable thr) {
            LOG.e(".setWebVideo sortie en Erreur ("+thr.toString()+")", thr);
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
        LOG.d(".setWebVideo sortie");
    }

////    @Click({R.id.scrollpostnextgen_detailIndicsCommentaire, R.id.scrollpostnextgen_detailIndicsCommentaireText})
//    public void getShowComments(){
//        if (!"0".equals(detailIndicsCommentaireText.getText().toString())){
//            //Nous avons des commentaires à afficher
//            getComments();
//        }
//    }
//
//    @Background
//    public void getComments(){
//        List<Comment> comments = diasporaService.getComments(post.getId());
//        showComments(comments);
//    }
//
//    @UiThread
//    public void showComments(List<Comment> comments){
//        if (comments!=null&&!comments.isEmpty()){
//            CommentsFragment_ commentFragment = new CommentsFragment_();
//            MainActivity mainActivity = (MainActivity)DiasporaConfig.APPLICATION_CONTEXT;
//            Bundle bundle = new Bundle();
//            bundle.putInt(Intent.EXTRA_UID, post.getId());
//            commentFragment.setArguments(bundle);
//            Fragment fragmentParent = mainActivity.getFragementActif();
//            commentFragment.setFragmentParent(fragmentParent);
//            commentFragment.show(fragmentParent.getFragmentManager(), "podSelection");
//        }
//    }

    @UiThread
    public void showComments(){
        if (!"0".equals(detailIndicsCommentaireText.getText().toString())){
            //Nous avons des commentaires à afficher
            CommentsFragment_ commentFragment = new CommentsFragment_();
            MainActivity mainActivity = (MainActivity)DiasporaConfig.APPLICATION_CONTEXT;
            Bundle bundle = new Bundle();
            bundle.putInt(Intent.EXTRA_UID, post.getId());
            commentFragment.setArguments(bundle);
            Fragment fragmentParent = mainActivity.getFragementActif();
            commentFragment.setFragmentParent(fragmentParent);
            commentFragment.show(fragmentParent.getFragmentManager(), "podSelection");
        }
    }
}
