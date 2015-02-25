package fr.android.scaron.diaspdroid.vues.adapter;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.android.scaron.diaspdroid.DeveloperKey;
import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.controler.ProfilControler;
import fr.android.scaron.diaspdroid.model.Image;
import fr.android.scaron.diaspdroid.model.People;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.fragment.YoutubePlayerFragment;
import fr.android.scaron.diaspdroid.vues.view.PostView;

/**
 * Created by Sébastien on 16/01/2015.
 */
public class PostAdapter extends ArrayAdapter<Post> { // implements MediaPlayer.OnErrorListener { for test textureview

    private static Logger LOGGEUR = LoggerFactory.getLogger(PostAdapter.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
//    private final FragmentManager supportFragmentManager;
    LayoutInflater inflater;
    FragmentActivity follower;
    private List<Post> posts = new ArrayList<Post>();


    public PostAdapter(FragmentActivity follower, int ressource, List<Post> posts){
        super(follower, ressource, posts);
        try{
            inflater = LayoutInflater.from(follower.getApplicationContext());
            this.follower = follower;
            this.posts = posts;
//            supportFragmentManager =  follower.getSupportFragmentManager();
            setPosts(posts);
        } catch (Throwable thr) {
            LOG.e("Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    public void setPosts(List<Post> posts){
        try{
            if (posts==null) {
                this.posts = new ArrayList<Post>();
            }else{
                this.posts = posts;
            }
            super.notifyDataSetChanged();
        } catch (Throwable thr) {
            LOG.e("Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @Override
    public int getCount() {
        try{
            return posts.size();
        } catch (Throwable thr) {
            LOG.e("Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @Override
    public Post getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        try{
            return posts.get(position).getId().longValue();
        } catch (Throwable thr) {
            LOG.e("Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

//    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try{
            final int RECOVERY_DIALOG_REQUEST = 1;

            YoutubePlayerFragment youTubePlayerFragment;
            YouTubePlayer.OnInitializedListener youtubeListener;
            int sdk=Build.VERSION.SDK_INT;

            final String mimeType = "text/html";
            final String encoding = "utf-8";
            PostView postView;
            if(convertView == null) {
                postView = new PostView();
                convertView = inflater.inflate(R.layout.flux_list_item, null);
                postView.flux_list_item_post_avatar = (ImageView)convertView.findViewById(R.id.flux_list_item_post_avatar);
                postView.flux_list_item_post_user = (TextView)convertView.findViewById(R.id.flux_list_item_post_user);
                postView.flux_list_item_post_datetime = (TextView)convertView.findViewById(R.id.flux_list_item_post_datetime);
                postView.flux_list_item_post_detail = (WebView)convertView.findViewById(R.id.flux_list_item_post_detail);
                postView.flux_list_item_post_detail_picture = (ImageView)convertView.findViewById(R.id.flux_list_item_post_detail_picture);



                postView.flux_list_item_post_detail_video_web = (WebView)convertView.findViewById(R.id.flux_list_item_post_detail_video_web);
                convertView.setTag(postView);
            } else {
                postView = (PostView) convertView.getTag();
            }

            final Post post = posts.get(position);
            postView.post = post;
            // *** Entete
            // Remplissage de la partie auteur
            People author = post.getAuthor();
            if (author != null){
                Image avatar = author.getAvatar();
                // Remplissage de l'avatar
                if (avatar!=null){
                    ProfilControler.putImage(postView.flux_list_item_post_avatar, avatar.getLarge());
                }
                // Remplissage du nom
                postView.flux_list_item_post_user.setText(author.getName());
            }
            // Remplissage de la date
            postView.flux_list_item_post_datetime.setText(post.getCreated_at_str());

            // *** Detail
            // Remplissage de la partie texte
//            postView.flux_list_item_post_detail.setText(post.getText());
            postView.flux_list_item_post_detail.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            postView.flux_list_item_post_detail.getSettings().setUseWideViewPort(true);
            postView.flux_list_item_post_detail.getSettings().setLoadWithOverviewMode(true);
            postView.flux_list_item_post_detail.getSettings().setJavaScriptEnabled(true);

            String postHTML = "<div style=\"display: block;font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;" +
                    "font-size: 34px;font-style: normal;font-weight: normal;" +
                    "vertical-align: baseline;zoom: 1;\">"+post.getText()+"</div>";
            postView.flux_list_item_post_detail.loadDataWithBaseURL(null, postHTML, mimeType, encoding, "");



            // Remplissage de l'image
            String imageURL = post.getImage_url();
            if (imageURL!=null && !imageURL.isEmpty()){
                ProfilControler.putImage(postView.flux_list_item_post_detail_picture, imageURL);
            }

            // Remplissage de l'objet web(vidéo ou autre) et de la vue vidéo
            final Map<String, String> videoData = getVideo(post);

            if (!videoData.isEmpty()){
                if (videoData.containsKey("youtube")){
                    LOG.d(".getView set youtube video");
                    FragmentManager supportFragmentManager=null;
                    if (postView!=null && postView.getActivity()!=null && postView.getActivity().getSupportFragmentManager()!=null) {
                        supportFragmentManager = postView.getActivity().getSupportFragmentManager();
                    }

                    if (supportFragmentManager==null && follower!=null && follower.getSupportFragmentManager()!=null) {
                        supportFragmentManager = follower.getSupportFragmentManager();
                    }
                    youTubePlayerFragment =
                            (YoutubePlayerFragment) supportFragmentManager.findFragmentById(R.id.detailPostObjectData);
                    if (supportFragmentManager==null || youTubePlayerFragment==null){
                        if (supportFragmentManager==null) {
                            LOG.e(".getView impossible to get supportFragmentManager to add Youtube video ID = " + videoData.get("youtube"));
                        }
                        if (youTubePlayerFragment==null) {
                            LOG.e(".getView impossible to get youTubePlayerFragment to add Youtube video ID = " + videoData.get("youtube"));
                        }

                        postView.flux_list_item_post_detail_video_web.getSettings().setJavaScriptEnabled(true);
                        postView.flux_list_item_post_detail_video_web.loadDataWithBaseURL(null, "<html><head><meta name=\"viewport\" content=\"width=device-width; user-scalable=no; initial-scale=1.0; minimum-scale=1.0; maximum-scale=1.0; target-densityDpi=device-dpi;\"/></head><body><iframe class=\"youtube-player\" type=\"text/html\" width=\"370\" height=\"220\" src=\"http://www.youtube.com/embed/"+videoData.get("youtube")+"\" frameborder=\"0\"></body></html>", mimeType, encoding, "");
                        postView.flux_list_item_post_detail_video_web.setVisibility(View.VISIBLE);
                        postView.flux_list_item_post_detail_video_web.getSettings().setLoadWithOverviewMode(true);
                        postView.flux_list_item_post_detail_video_web.getSettings().setUseWideViewPort(true);
//                        if (videoData.containsKey("web")) {
//                            LOG.d(".getView set web video");
//                            postView.flux_list_item_post_detail_video_web.getSettings().setUseWideViewPort(true);
//                            postView.flux_list_item_post_detail_video_web.getSettings().setLoadWithOverviewMode(true);
//                            postView.flux_list_item_post_detail_video_web.getSettings().setJavaScriptEnabled(true);
//                            postView.flux_list_item_post_detail_video_web.loadDataWithBaseURL(null, "<html><head><meta name=\"viewport\" content=\"width=device-width; user-scalable=no; initial-scale=1.0; minimum-scale=1.0; maximum-scale=1.0; target-densityDpi=device-dpi;\"/></head><body>"+videoData.get("web")+"</body></html>", mimeType, encoding, "");
//                            postView.flux_list_item_post_detail_video_web.setVisibility(View.VISIBLE);
//                            postView.flux_list_item_post_detail_video_web.zoomBy(Float.parseFloat("0.8"));
//                        }
                    }else {

                        LOG.d(".getView : youTubePlayerFragment found ? " + (youTubePlayerFragment != null));
                        FragmentTransaction sfmTx = supportFragmentManager.beginTransaction();
                        LOG.d(".getView fragment 'detailPostVideoYoutube' to create");
                        youTubePlayerFragment = new YoutubePlayerFragment();
                        LOG.d(".getView create FragmentTransaction");
                        LOG.d(".getView add add(R.id.detailPostVideoYoutube, youTubePlayerFragment) in FragmentTransaction");
                        sfmTx.replace(R.id.detailPostObjectData, youTubePlayerFragment);
                        sfmTx.commit();

                        LOG.d(".getView create youtubeListener");
                        youtubeListener = new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                                boolean wasRestored) {
                                LOG.d(".getView.onInitializationSuccess ID de la vidéo récoltée '" + videoData.get("youtube") + "'");
                                player.cueVideo(videoData.get("youtube"));
                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                YouTubeInitializationResult errorReason) {
                                if (errorReason.isUserRecoverableError()) {
                                    errorReason.getErrorDialog(follower, RECOVERY_DIALOG_REQUEST).show();
                                } else {
                                    String errorMessage = String.format(follower.getString(R.string.error_player), errorReason.toString());
                                    Toast.makeText(follower, errorMessage, Toast.LENGTH_LONG).show();
                                }
                            }
                        };
                        LOG.d(".getView initialize api youTubePlayerFragment");
                        youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, youtubeListener);
                    }
                }else if (videoData.containsKey("web")) {
                    LOG.d(".getView set web video");
                    postView.flux_list_item_post_detail_video_web.getSettings().setUseWideViewPort(true);
                    postView.flux_list_item_post_detail_video_web.getSettings().setLoadWithOverviewMode(true);
                    postView.flux_list_item_post_detail_video_web.getSettings().setJavaScriptEnabled(true);
                    postView.flux_list_item_post_detail_video_web.getSettings().setUseWideViewPort(true);//TEST
                    postView.flux_list_item_post_detail_video_web.loadDataWithBaseURL(null, videoData.get("web"), mimeType, encoding, "");
                    postView.flux_list_item_post_detail_video_web.setVisibility(View.VISIBLE);
                }
            }
            return convertView;
        } catch (Throwable thr) {
            LOG.e("getView Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
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

    public Map<String, String> getVideo(Post post) {
        Map mapVideo = new HashMap<String, String>();
        String videoType = getVideoType(post);
        if ("youtube".equals(videoType)) {
            String objectHtml = post.getO_embed_cache().getData().getHtml();
            int indexSrcBegin = objectHtml.indexOf("src=\"");
            indexSrcBegin = indexSrcBegin + "src=\"".length();
            int indexSrcEnd = objectHtml.indexOf("?feature=oembed", indexSrcBegin);
            String urlSrc = objectHtml.substring(indexSrcBegin, indexSrcEnd);
            indexSrcBegin = urlSrc.lastIndexOf("/") + 1;
            urlSrc = urlSrc.substring(indexSrcBegin);
            mapVideo.put("youtube",urlSrc);
            mapVideo.put("web",post.getO_embed_cache().getData().getHtml());
        }else if ("web".equals(videoType)) {
            mapVideo.put("web",post.getO_embed_cache().getData().getHtml());
        }
        return mapVideo;
    }
}
