package fr.android.scaron.diaspdroid.vues.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.DeveloperKey;
import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.Data;
import fr.android.scaron.diaspdroid.model.OEmbedCache;
import fr.android.scaron.diaspdroid.model.Post;

/**
 * Created by CARON-08651 on 03/02/2015.
 */
public class DetailPostFragment extends Fragment {
    private static Logger LOGGEUR = LoggerFactory.getLogger(DetailPostFragment.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);
    private WebView webview;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    final String mimeType = "text/html";
    final String encoding = "utf-8";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayer.OnInitializedListener youtubeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LOG.d(".onCreateView entree with savedInstanceState : "+savedInstanceState);
        LOG.d(".onCreateView create view with layout  R.layout.fragment_detailpostyoutube");
        View view = inflater.inflate(R.layout.fragment_detailpostyoutube, container);
        LOG.d(".onCreateView find web view with id  R.id.detailPostTextYoutube");
        webview = (WebView)view.findViewById(R.id.detailPostTextYoutube);

        LOG.d(".onCreateView webview found ? "+(webview!=null));

        LOG.d(".onCreateView sortie with view : "+view);
        return view;
//        webview = new WebView(getActivity());
//        LOG.d(".onCreateView sortie with webview : "+webview);
//        return webview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LOG.d(".onActivityCreated entree with savedInstanceState : "+savedInstanceState);


        LOG.d(".onCreateView find youtube fragment with id  R.id.detailPostVideoYoutube");
        youTubePlayerFragment =
                (YouTubePlayerSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.detailPostVideoYoutube);
        LOG.d(".onCreateView youTubePlayerFragment found ? "+(youTubePlayerFragment!=null));
//        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        LOG.d(".onCreateView create youtubeListener");
        youtubeListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {

                if (!wasRestored) {
                    player.cueVideo("nCgQDjiotG0");
                }
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult errorReason) {
                if (errorReason.isUserRecoverableError()) {
                    errorReason.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
                } else {
                    String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        };
        if (youTubePlayerFragment!=null) {
            LOG.d(".onCreateView initialize youTubePlayerFragment");
            youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, youtubeListener);
        }else{
            LOG.d(".onCreateView initialize youTubePlayerFragment failed");
        }


        Bundle bundle = getArguments();
        LOG.d(".onActivityCreated bundle is null ? : "+(bundle==null));
        if (bundle != null) {
            String jsonMyObject;
            jsonMyObject = bundle.getString("post");
            Post post = new Gson().fromJson(jsonMyObject, Post.class);
            LOG.d(".onActivityCreated setText with text : "+bundle.getString("link"));
            setPost(post);
        }
        LOG.d(".onActivityCreated entree with savedInstanceState : "+savedInstanceState);
    }

    public void setPost(Post post) {
        LOG.d(".setText entree with text : "+post);
        if (webview!=null) {
//            LOG.d(".setText setInitialScale");
//            webview.setInitialScale(50);
//            LOG.d(".setText loadUrl with : "+item);
//            webview.loadUrl(item);
            LOG.d(".setText getSettings set params");
            webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webview.getSettings().setUseWideViewPort(true);
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setJavaScriptEnabled(true);
            // Remplissage de l'objet web
            StringBuilder sbHtml = new StringBuilder();
            // Ajout du titre
            sbHtml.append("<h1>"+post.getTitle()+"</h1>");
            // Ajout du text
            sbHtml.append("<div>"+post.getText()+"</div>");
                    // Remplissage de l'objet web avec l'objet
            OEmbedCache object = post.getO_embed_cache();
            if (object != null) {
                Data objectData = object.getData();
                if (objectData != null) {
                    String objectHtml = objectData.getHtml();
                    if (objectHtml != null && !objectHtml.isEmpty()) {

                        LOG.d("Object HTML récolté '" + objectHtml + "'");
                        sbHtml.append(objectHtml);
                    }
                }
            }
            webview.loadDataWithBaseURL(null, sbHtml.toString(), mimeType, encoding, "");
            LOG.d(".setText sortie en succès");
            return;
        }
        LOG.d(".setText not possible webview not found");
        LOG.d(".setText sortie en erreur");
    }
}
