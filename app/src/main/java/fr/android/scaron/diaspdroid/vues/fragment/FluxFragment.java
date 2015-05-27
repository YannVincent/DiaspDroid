package fr.android.scaron.diaspdroid.vues.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
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
import fr.android.scaron.diaspdroid.activity.MainActivity;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.adapter.PostsAdapter;

@EFragment(R.layout.fragment_flux_list)
public class FluxFragment extends Fragment implements AbsListView.OnScrollListener{

    private static Logger LOGGEUR = LoggerFactory.getLogger(FluxFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = FluxFragment.class.getSimpleName();
    final static int SELECT_PICTURE = 1;
    @Bean
    DiasporaBean diasporaBean;


    CommentsFragment dialogFragment;

    @Bean
    PostsAdapter adapter;

    @ViewById(R.id.fragment_flux_list)
    ListView mListView;

    int currentFirstVisibleItem = 0;
    int currentVisibleItemCount = 0;
    int totalItemCount = 0;
    int currentScrollState = 0;
    boolean loadingMore = false;
    Long startIndex = 0L;
    View headerView;
    View footerView;
    ImageView imageViewAvatar;
    TextView headerText;
    ImageView imageViewAddPhoto;

    ActionBarActivity activity;

    List<Post> posts;

    public void setActivityParent(ActionBarActivity activity){
        this.activity = activity;
    }
    @Background
    void getInfos(){
        String TAG_METHOD = TAG + ".getInfos : ";
        LOG.d(TAG_METHOD + "Entrée");
        addFooterView();
        LOG.d(TAG_METHOD+ "call diasporaBean.getStream");
        posts = diasporaBean.getStream();
        bindDatas();
        LOG.d(TAG_METHOD + "Sortie");
    }
    @UiThread
    void bindDatas(){
        String TAG_METHOD = TAG + ".bindDatas : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (footerView != null) {
            mListView.removeFooterView(footerView);
        }
        if (DiasporaConfig.avatarDatas!=null) {
            Bitmap imageAvatar = BitmapFactory.decodeByteArray(DiasporaConfig.avatarDatas, 0, DiasporaConfig.avatarDatas.length);
            imageViewAvatar.setImageBitmap(imageAvatar);
        }
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
        LOG.d(TAG_METHOD + "Entrée");
        imageViewAvatar = (ImageView)headerView.findViewById(R.id.header_comment_avatar);
        headerText = (TextView)headerView.findViewById(R.id.header_comment_text);
        headerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ((MainActivity)activity).listItemClicked("Partagez");
            }
        });
        imageViewAddPhoto = (ImageView)headerView.findViewById(R.id.header_comment_photo);
        imageViewAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                ((MainActivity)activity).listItemClicked("Partagez");
                //TODO voir pour retablir ce pb
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, ""), SELECT_PICTURE);
            }
        });
        mListView.addHeaderView(headerView);
        LOG.d(TAG_METHOD + "mListView.setAdapter");
        mListView.setAdapter(adapter);
        mListView.setOnScrollListener(this);
        LOG.d(TAG_METHOD + "Sortie");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null) {
            switch (requestCode) {
                case SELECT_PICTURE:
                    ((MainActivity)activity).listItemClicked("Partagez", data.getData());
//                    ((MainActivity)activity).listItemClicked("Partagez");
//                    Intent i = new Intent(getActivity(),
//                            ShareActivity_.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.setAction(Intent.ACTION_SEND);
//                    i.setType("image/");
//                    i.putExtra(Intent.EXTRA_STREAM, data.getData());
//                    i.putExtra(Intent.EXTRA_TEXT, "MainActivity");
//                    startActivity(i);
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        String TAG_METHOD = TAG + ".onCreate : ";
        LOG.d(TAG_METHOD+ "Entrée");
        try{
            super.onCreate(savedInstanceState);
            headerView = ((LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.base_list_item_loading_header, null, false);
            footerView = ((LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.base_list_item_loading_footer, null, false);
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


    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        this.currentScrollState = scrollState;
        this.isScrollCompleted();
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        this.currentFirstVisibleItem = firstVisibleItem;
        this.currentVisibleItemCount = visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    private void isScrollCompleted() {
        if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE && this.totalItemCount == (currentFirstVisibleItem + currentVisibleItemCount)) {
            if (!loadingMore) {
                loadingMore = true;
                getMoreInfos();
            }
        }
    }

    @Background
    void getMoreInfos(){
        String TAG_METHOD = TAG + ".getInfos : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (posts!=null && posts.size()>0) {
            addFooterView();
            //On récupère le dernier post pour déterminer le timestamp max à précisier pour le getMoreStream
            Post firstPost = posts.get(0);
            Post olderPost = posts.get(posts.size() - 1);
            long timestampNewest = Long.valueOf("" + (firstPost.getCreated_at().getTime() / 1000));
            long timestampOlder = Long.valueOf("" + (olderPost.getCreated_at().getTime() / 1000));
            LOG.d(TAG_METHOD + "call diasporaBean.getStream");
            posts = diasporaBean.getMoreStream(timestampOlder, timestampNewest);
            bindMoreDatas();
        }
        LOG.d(TAG_METHOD+ "Sortie");
    }
    @UiThread
    void addFooterView(){
        mListView.addFooterView(footerView);
    }
    @UiThread
    void bindMoreDatas(){
        String TAG_METHOD = TAG + ".bindDatas : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (footerView != null) {
            mListView.removeFooterView(footerView);
        }
        loadingMore = false;
        if (posts != null) {
            LOG.d(TAG_METHOD + "adapter.addPosts(posts) with " + posts);
            if (adapter!=null) {
                int newPostsAdded = adapter.addPosts(posts);
                LOG.d(TAG_METHOD + "added "+newPostsAdded + " posts from list of ori size "+posts.size());
                startIndex = startIndex + newPostsAdded;
            }
        }
        LOG.d(TAG_METHOD+ "Sortie");
    }
}
