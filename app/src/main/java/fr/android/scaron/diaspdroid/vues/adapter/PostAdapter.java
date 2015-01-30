package fr.android.scaron.diaspdroid.vues.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
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
import com.google.android.youtube.player.YouTubePlayerView;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.DeveloperKey;
import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.controler.ProfilControler;
import fr.android.scaron.diaspdroid.model.Data;
import fr.android.scaron.diaspdroid.model.Image;
import fr.android.scaron.diaspdroid.model.OEmbedCache;
import fr.android.scaron.diaspdroid.model.People;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.view.PostView;

/**
 * Created by CARON-08651 on 16/01/2015.
 */
public class PostAdapter extends ArrayAdapter<Post> { // implements MediaPlayer.OnErrorListener { for test textureview

    private static Logger LOGGEUR = LoggerFactory.getLogger(PostAdapter.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);
    LayoutInflater inflater;
    Activity follower;
    private List<Post> posts = new ArrayList<Post>();


    public PostAdapter(Activity follower, int ressource, List<Post> posts){
        super(follower, ressource);
        try{
            inflater = LayoutInflater.from(follower.getApplicationContext());
            this.follower = follower;
            this.posts = posts;
            setPosts(posts);
        } catch (Throwable thr) {
            LOG.e("Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    public void setPosts(List<Post> posts){
        try{
            //follower.updateTitle(String.valueOf(downloads.size()));
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

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try{
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
//                postView.flux_list_item_post_detail = (TextView)convertView.findViewById(R.id.flux_list_item_post_detail);
                postView.flux_list_item_post_detail = (WebView)convertView.findViewById(R.id.flux_list_item_post_detail);
                postView.flux_list_item_post_detail_picture = (ImageView)convertView.findViewById(R.id.flux_list_item_post_detail_picture);
                postView.flux_list_item_post_detail_video = (YouTubePlayerView) convertView.findViewById(R.id.flux_list_item_post_detail_video);
//                if (sdk>= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                    postView.flux_list_item_post_detail_video = (TextureView) convertView.findViewById(R.id.flux_list_item_post_detail_video);
//                }
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
//            String postHTML = "<div style=\"border-bottom-color: rgb(34, 34, 34);border-bottom-style: none;border-bottom-width: 0px;" +
//                    "border-image-outset: 0px;border-image-repeat: stretch;border-image-slice: 100%;border-image-source: none;border-image-width: 1;" +
//                    "border-left-color: rgb(34, 34, 34);border-left-style: none;border-left-width: 0px;border-right-color: rgb(34, 34, 34);" +
//                    "border-right-style: none;border-right-width: 0px;border-top-color: rgb(34, 34, 34);border-top-style: none;" +
//                    "border-top-width: 0px;color: rgb(34, 34, 34);display: block;font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;" +
//                    "font-size: 13px;font-style: normal;font-weight: normal;height: 266px;line-height: 19.5px;margin-bottom: 0px;margin-left: 0px;" +
//                    "margin-right: 0px;margin-top: 0px;padding-bottom: 0px;padding-left: 0px;padding-right: 0px;padding-top: 0px;" +
//                    "vertical-align: baseline;width: 429px;zoom: 1;\">"+post.getText()+"</div>";
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
            OEmbedCache object = post.getO_embed_cache();
            if (object!=null) {
                Data objectData = object.getData();
                if (objectData!=null) {
                    String objectHtml = objectData.getHtml();
                    if (objectHtml != null && !objectHtml.isEmpty()) {

                        LOG.d("Object HTML récolté '" + objectHtml + "'");
                        //Cas de la vidéo Youtube
                        if (objectHtml.contains("youtube") && (sdk>= Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
                            int indexSrcBegin = objectHtml.indexOf("src=\"");
                            indexSrcBegin = indexSrcBegin + "src=\"".length();
                            int indexSrcEnd = objectHtml.indexOf("?feature=oembed", indexSrcBegin);
                            String urlSrc = objectHtml.substring(indexSrcBegin, indexSrcEnd);
                            LOG.d("Url de la vidéo récoltée '" + urlSrc + "'");
                            //POUR TEST
//                            urlSrc = "rtsp://r5---sn-cg07luel.c.youtube.com/CiILENy73wIaGQkhqmoFDXKHthMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp";
//                            LOG.d("Url de la vidéo surchargée pour TEST '" + urlSrc + "'");

                            objectData.setVideoUrl(urlSrc);

                            //Test avec API Youtube et YoutubePlayeur
                            int indexOfVideoID = urlSrc.lastIndexOf('/')+1;
                            if (indexOfVideoID>0 && indexOfVideoID<urlSrc.length()){
                                urlSrc = urlSrc.substring(indexOfVideoID);
                            }
                            final YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
                                @Override
                                public void onBuffering(boolean arg0) { }

                                @Override
                                public void onPaused() { }

                                @Override
                                public void onPlaying() { }

                                @Override
                                public void onSeekTo(int arg0) { }

                                @Override
                                public void onStopped() { }
                            };

                            final YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

                                @Override
                                public void onAdStarted() { }

                                @Override
                                public void onError(YouTubePlayer.ErrorReason arg0) { }

                                @Override
                                public void onLoaded(String arg0) { }

                                @Override
                                public void onLoading() { }

                                @Override
                                public void onVideoEnded() { }

                                @Override
                                public void onVideoStarted() { }
                            };

                            final String VIDEO_ID = urlSrc;// "dKLftgvYsVU";
                            final YouTubePlayerView youtubeView = postView.flux_list_item_post_detail_video;
                            YouTubePlayer.OnInitializedListener onInitializedListener = new YouTubePlayer.OnInitializedListener (){
                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
                                    Toast.makeText(follower, "Failured to Initialize!", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {

                                    /** add listeners to YouTubePlayer instance **/
                                    player.setPlayerStateChangeListener(playerStateChangeListener);
                                    player.setPlaybackEventListener(playbackEventListener);

                                    /** Start buffering **/
                                    if (!wasRestored) {
                                        player.cueVideo(VIDEO_ID);
                                    }
                                }
                            };
                            youtubeView.initialize(DeveloperKey.DEVELOPER_KEY, onInitializedListener);







                            //Test texture view (ne fonctionne pas)
//                            final TextureView textureView = postView.flux_list_item_post_detail_video;
//                            textureView.setVisibility(View.VISIBLE);
//
//                            textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//
//                                @Override
//                                public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
//
//                                }
//
//                                @Override
//                                public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
//                                                                        int arg2) {
//                                }
//
//                                @Override
//                                public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
//                                    return false;
//                                }
//
//                                @Override
//                                public void onSurfaceTextureAvailable(SurfaceTexture surface, int arg1, int arg2) {
//                                    final MediaPlayer mediaPlayer = new MediaPlayer();
////                                    mediaPlayer.reset();
////                                    mediaPlayer.release();
////                                    holder.vid_play.setTag(mediaPlayer);
////                                    new SetVideotask().execute(surface, post, mediaPlayer, holder.vid_play);
//                                    new SetVideotask().execute(surface, post, mediaPlayer);
//                                }
//                            });


                            //Old version avec videoview ne fonctionne pas
////                        postView.flux_list_item_post_detail_video.setVideoPath(urlSrc);
////                        postView.flux_list_item_post_detail_video.setVisibility(View.VISIBLE);
//
////                        postView.flux_list_item_post_detail_video.setVideoURI(Uri.parse("rtsp://v4.cache1.c.youtube.com/CiILENy73wIaGQk4RDShYkdS1BMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp"));
////                        postView.flux_list_item_post_detail_video.setMediaController(new MediaController(AlertDetail.this));
////                        postView.flux_list_item_post_detail_video.requestFocus();
////                        postView.flux_list_item_post_detail_video.start();
//
//                            // Le lien rtsp est déjà en mémoire ?
//                            String rtspVideo = urlSrc;
//                            final VideoView videoView = postView.flux_list_item_post_detail_video;
//                            if (!YoutubeControler.rtspMapping.containsKey(urlSrc)){
//                                // Non donc on tente de le trouver
//                                rtspVideo = YoutubeControler.getUrlVideoRTSP(follower,urlSrc, videoView);
//                                LOG.d("Url de la vidéo rtsp en court de recherche ...");
//                            }else{
//                                // Oui donc on affiche directement
//                                rtspVideo = YoutubeControler.rtspMapping.get(urlSrc);
//                                LOG.d("Url de la vidéo rtsp déjà en mémoire : '" + rtspVideo + "'");
////                                postView.flux_list_item_post_detail_video.setVideoURI(Uri.parse(rtspVideo));
////                                MediaController mc = new MediaController(follower);
////                                postView.flux_list_item_post_detail_video.setMediaController(mc);
////                                postView.flux_list_item_post_detail_video.requestFocus();
////                                postView.flux_list_item_post_detail_video.start();
////                                mc.show();
////                                postView.flux_list_item_post_detail_video.setVisibility(View.VISIBLE);
//
//                                // Create a progressbar
//                                final ProgressDialog pDialog = new ProgressDialog(follower);
//                                // Set progressbar title
//                                pDialog.setTitle("Android Video Streaming Youtube");
//                                // Set progressbar message
//                                pDialog.setMessage("Buffering...");
//                                pDialog.setIndeterminate(false);
//                                pDialog.setCancelable(false);
//                                // Show progressbar
//                                pDialog.show();
//
//                                try {
//                                    // Start the MediaController
//                                    MediaController mediacontroller = new MediaController(follower);
//                                    mediacontroller.setAnchorView(videoView);
//                                    // Get the URL from String VideoURL
//                                    Uri video = Uri.parse(rtspVideo);
//                                    videoView.setMediaController(mediacontroller);
//                                    videoView.setVideoURI(video);
//
//                                } catch (Exception e) {
//                                    LOG.e(".getView Error : "+e.getMessage());
//                                    e.printStackTrace();
//                                }
//
//                                videoView.requestFocus();
//                                videoView.setVisibility(View.VISIBLE);
//                                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                                    // Close the progress bar and play the video
//                                    public void onPrepared(MediaPlayer mp) {
//                                        pDialog.dismiss();
//                                        videoView.start();
//                                    }
//                                });
//                            }
////                            postView.flux_list_item_post_detail_video.setVideoURI(Uri.parse(rtspVideo));
////                            MediaController mc = new MediaController(follower);
////                            postView.flux_list_item_post_detail_video.setMediaController(mc);
////                            postView.flux_list_item_post_detail_video.requestFocus();
////                            postView.flux_list_item_post_detail_video.start();
////                            mc.show();
////                            postView.flux_list_item_post_detail_video.setVisibility(View.VISIBLE);
                        }

//                        }else{
//                            //Cas de l'objet web
//                            int indexSrcBegin = objectHtml.indexOf("src=\"");
//                            indexSrcBegin = indexSrcBegin + "src=\"".length();
//                            int indexSrcEnd = objectHtml.indexOf("\"", indexSrcBegin);
//                            String urlSrc = objectHtml.substring(indexSrcBegin, indexSrcEnd);
//                            post.getO_embed_cache().getData().setVideoUrl(urlSrc);
//                            LOG.d("Url de l'objet récoltée '" + urlSrc + "'");
                            String objectHtmlDroid = objectHtml;//"<iframe scrolling=\"no\" frameborder=\"no\" src=\""+urlSrc+"\">";//objectHtml;//"<div style=\"width: 100%;\">" + objectHtml + "</div>";
                            postView.flux_list_item_post_detail_video_web.getSettings().setUseWideViewPort(true);
                            postView.flux_list_item_post_detail_video_web.getSettings().setLoadWithOverviewMode(true);
                            postView.flux_list_item_post_detail_video_web.getSettings().setJavaScriptEnabled(true);
                            postView.flux_list_item_post_detail_video_web.getSettings().setUseWideViewPort(true);//TEST
                            postView.flux_list_item_post_detail_video_web.loadDataWithBaseURL(null, objectHtmlDroid, mimeType, encoding, "");
                            //test on masque la video
                            postView.flux_list_item_post_detail_video_web.setVisibility(View.VISIBLE);
//                        }
                    }
                }
            }
            return convertView;
        } catch (Throwable thr) {
            LOG.e("Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    //Pour test textureview
//    class SetVideotask extends AsyncTask<Object, Integer, String> {
//
//        @SuppressLint("NewApi")
//        @Override
//        protected String doInBackground(Object... o) {
//            // TODO Auto-generated method stub
//            final SurfaceTexture s = (SurfaceTexture) o[0];
//            final Post post = (Post) o[1];
//            final MediaPlayer mediaPlayer = (MediaPlayer) o[2];
//
//
//            mediaPlayer.setSurface(new Surface(s));
//            try {
////                mediaPlayer.reset();
////                mediaPlayer.release();
//                mediaPlayer.setDataSource(post.getO_embed_cache().getData().getVideoUrl());
//                mediaPlayer.prepare();
//                mediaPlayer.start();
//                //I do this so there is a frame in the video to act as a preview
//                Thread.sleep(100);
//                mediaPlayer.pause();
//                mediaPlayer.setOnErrorListener(PostAdapter.this);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//    }
//
//    @Override
//    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
//        return true;
//    }
}
