package fr.android.scaron.diaspdroid.vues.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.vues.adapter.PostsAdapter;
import fr.android.scaron.diaspdroid.model.Post;

@EFragment(R.layout.fragment_flux_list)
public class FluxFragment extends Fragment {

    private static Logger LOGGEUR = LoggerFactory.getLogger(FluxFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "FluxFragment";
    @Bean
    DiasporaBean diasporaBean;

    @Bean
    PostsAdapter adapter;

    @ViewById(R.id.fragment_flux_list)
    AbsListView mListView;
    ActionBarActivity activity;

    List<Post> posts;
    byte[] imageAvatarDatas = null;

    public void setActivityParent(ActionBarActivity activity){
        this.activity = activity;
    }

    @Background
    void getInfosUserForBar(){
        String TAG_METHOD = TAG + ".getInfosUserForBar : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "call diasporaBean.getInfo");
        List<Post> postsUser = diasporaBean.getInfo("tilucifer");
//        LOG.d(TAG_METHOD+ "call diasporaBean.getStream");
        posts = diasporaBean.getStream();
        Post first = postsUser.get(0);
        String userName = first.getAuthor().getName();
        String userAdress = first.getAuthor().getDiaspora_id();
        String userAvatar = first.getAuthor().getAvatar().getLarge();
        updateActionBar(userName, userAdress, userAvatar);
        LOG.d(TAG_METHOD+ "Sortie");
    }
    @UiThread
    void updateActionBar(String userName, String userAdress, String userAvatar){
        String TAG_METHOD = TAG + ".updateActionBar : ";
        LOG.d(TAG_METHOD+ "Entrée");
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
//        ProfilControler.putImage(mTitleAvatarView, userAvatar);
        setImageAvatarInView(userAvatar);
        if (imageAvatarDatas !=null) {
            LOG.d(TAG_METHOD + "converting datas to bitmap");
            Bitmap imageAvatar = BitmapFactory.decodeByteArray(imageAvatarDatas, 0, imageAvatarDatas.length);
            mTitleAvatarView.setImageBitmap(imageAvatar);
        }
        actionBar.setCustomView(mCustomView);
//        actionBar.setDisplayShowCustomEnabled(true);

        if (posts != null) {
            LOG.d(TAG_METHOD + "adapter.setPosts(posts) with " + posts);
            if (adapter!=null) {
                adapter.setPosts(posts);
            }
        }


        LOG.d(TAG_METHOD+ "Sortie");
    }

    @Background
    public void setImageAvatarInView(String imagePath){
        String TAG_METHOD = TAG + ".setImageAvatarInView : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LOG.d(TAG_METHOD + "call diasporaBean.getImageFile with : "+imagePath);
        imageAvatarDatas = diasporaBean.getImageFile(imagePath);
        LOG.d(TAG_METHOD+ "Sortie");
    }

    @AfterViews
    void bindAdapter() {
        String TAG_METHOD = TAG + ".bindAdapter : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LOG.d(TAG_METHOD+ "mListView.setAdapter");
        mListView.setAdapter(adapter);
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String TAG_METHOD = TAG + ".onCreate : ";
        LOG.d(TAG_METHOD+ "Entrée");
        try{
            super.onCreate(savedInstanceState);
            LOG.d(TAG_METHOD+ "call getInfosUserForBar");
            getInfosUserForBar();

            LOG.d(TAG_METHOD+ "Sortie");
        } catch (Throwable thr) {
            LOG.e(TAG_METHOD+ "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(TAG_METHOD+ "Sortie en erreur");
            throw thr;
        }
    }


}
