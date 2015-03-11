package fr.android.scaron.diaspdroid.vues.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaControler;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.controler.ProfilControler;
import fr.android.scaron.diaspdroid.controler.RestServiceControler;
import fr.android.scaron.diaspdroid.controler.UserPublicPostsService;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.adapter.DetailPostViewAdapter;

@EFragment(R.layout.fragment_flux_list)
public class FluxFragment extends Fragment {

    private static Logger LOGGEUR = LoggerFactory.getLogger(FluxFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    @RestService
    UserPublicPostsService restClient;
    @Bean
    RestServiceControler serviceControler;

    @ViewById(R.id.fragment_flux_list)
    AbsListView mListView;
    ActionBarActivity activity;
    DetailPostViewAdapter adapter;

    public void setActivityParent(ActionBarActivity activity){
        this.activity = activity;
    }

    @Background
    void getInfosUserForBar(){
        restClient.setRootUrl(DiasporaControler.POD_URL);
        List<Post> postsUser = restClient.getInfo("tilucifer");
        Post first = postsUser.get(0);
        String userName = first.getAuthor().getName();
        String userAdress = first.getAuthor().getDiaspora_id();

        String userAvatar = first.getAuthor().getAvatar().getLarge();
        serviceControler.seLogguer();

        updateActionBar(userName, userAdress, userAvatar);
    }
    @UiThread
    void updateActionBar(String userName, String userAdress, String userAvatar){
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
////        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(activity);

        View mCustomView = mInflater.inflate(R.layout.actionbar_main, null);

        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.actbar_name);
        mTitleTextView.setText(userName);
        TextView mSecondTitleTextView = (TextView) mCustomView.findViewById(R.id.actbar_adress);
        mSecondTitleTextView.setText(userAdress);
        ImageView mTitleAvatarView = (ImageView) mCustomView.findViewById(R.id.actbar_avatar_icon);
        ProfilControler.putImage(mTitleAvatarView, userAvatar);
        actionBar.setCustomView(mCustomView);
//        actionBar.setDisplayShowCustomEnabled(true);
    }
    @AfterViews
    void bindAdapter() {
        mListView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            getInfosUserForBar();



            //Callback de la récupération du flux
            final FutureCallback<Response<List<Post>>> fluxCallback = new FutureCallback<Response<List<Post>>>() {
                @Override
                public void onCompleted(Exception e, Response<List<Post>> posts) {

                    LOG.d(FluxFragment.class, "Callback flux, exception ? " + e);
                    if (e!=null){
                        e.printStackTrace();
                    }
                    if (e!=null && e.getCause()!=null) {
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setIcon(R.drawable.ic_launcher);
                        alertDialog.setTitle("PB Données");
                        alertDialog.setMessage("La récupétion de votre flux a échouée");
                        alertDialog.show();
                        LOG.d(FluxFragment.class , "Callback flux, cause exception ? " + e.getCause().getMessage());
                    }
                    if (posts!=null){
                        adapter.setPosts(DiasporaControler.onCompleteStream(e, posts));
                        return;
                    }
                    LOG.e("Callback flux, Erreur : " + e.toString());
                    if (e.getCause()!=null) {
                        LOG.e("Callback flux, cause exception ? " + e.getCause().getMessage());
                    }
                    ACRA.getErrorReporter().handleException(e);
                }
            };

            DiasporaControler.getStreamFlow(fluxCallback, false);
            adapter = new DetailPostViewAdapter(getActivity(), R.layout.fragment_flux_list, new ArrayList<Post>());
        } catch (Throwable thr) {
            LOG.e("Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }


}
