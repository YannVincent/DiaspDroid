package fr.android.scaron.diaspdroid.activity;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.DeveloperKey;
import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;

/**
 * Created by Sébastien on 30/01/2015.
 */
public class YoutubeActivity extends  YouTubeFailureRecoveryActivity {

    private static Logger LOGGEUR = LoggerFactory.getLogger(YoutubeActivity.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    String idYoutubeVideo = "";
    YouTubePlayerFragment youTubePlayerFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idYoutubeVideo = (String)getIntent().getSerializableExtra("idYoutubeVideo");
        LOG.d("Getted idYoutube Video : "+idYoutubeVideo);
        setContentView(R.layout.fragment_youtube);

        youTubePlayerFragment =
                (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
        youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        player.cueVideo(idYoutubeVideo);
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
