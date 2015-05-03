package fr.android.scaron.diaspdroid.vues.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.AbsListView;

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
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.adapter.PostsAdapter;

@EFragment(R.layout.fragment_flux_list)
public class TagSuivisFragment extends Fragment {

    private static Logger LOGGEUR = LoggerFactory.getLogger(TagSuivisFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = TagSuivisFragment.class.getSimpleName();
    @Bean
    DiasporaBean diasporaBean;

    @Bean
    PostsAdapter adapter;

    @ViewById(R.id.fragment_flux_list)
    AbsListView mListView;
    ActionBarActivity activity;

    List<Post> posts;

    public void setActivityParent(ActionBarActivity activity){
        this.activity = activity;
    }

    @Background
    void getInfos(){
        String TAG_METHOD = TAG + ".getInfos : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD+ "call diasporaBean.getTagSuivis");
        posts = diasporaBean.getTagSuivis();
        bindDatas();
        LOG.d(TAG_METHOD+ "Sortie");
    }
    @UiThread
    void bindDatas(){
        String TAG_METHOD = TAG + ".bindDatas : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (posts != null) {
            LOG.d(TAG_METHOD + "adapter.setPosts(posts) with " + posts);
            if (adapter!=null) {
                adapter.setPosts(posts);
            }
        }
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
            LOG.d(TAG_METHOD+ "call getInfos");
            getInfos();

            LOG.d(TAG_METHOD+ "Sortie");
        } catch (Throwable thr) {
            LOG.e(TAG_METHOD+ "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(TAG_METHOD+ "Sortie en erreur");
            throw thr;
        }
    }


}
