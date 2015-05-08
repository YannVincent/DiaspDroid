package fr.android.scaron.diaspdroid.vues.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.DiasporaControler;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.Post;

/**
 * Created by Sébastien on 03/05/2015.
 */
@EViewGroup(R.layout.actionbar_main)
public class HeaderView extends LinearLayout {

    private static Logger LOGGEUR = LoggerFactory.getLogger(HeaderView.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = HeaderView.class.getSimpleName();

    @Bean
    DiasporaBean diasporaService;

    @ViewById(R.id.actbar_name)
    TextView mTitleTextView;
    @ViewById(R.id.actbar_adress)
    TextView mSecondTitleTextView;
    @ViewById(R.id.actbar_avatar_icon)
    ImageView mTitleAvatarView;


    byte[] imageAvatarDatas = null;
    Context context;

    public HeaderView(final Context context) {
        super(context);
        this.context = context;
    }

    public void bind(){
        getInfosUserForBar();
    }

    @Background
    void getInfosUserForBar(){
        String TAG_METHOD = TAG + ".getInfosUserForBar : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "call diasporaBean.getInfo");
        diasporaService.setRootUrl(DiasporaControler.POD_URL);
        List<Post> postsUser = diasporaService.getInfo("tilucifer");
        if (postsUser!=null && postsUser.size()>0) {
            Post first = postsUser.get(0);
            String userName = first.getAuthor().getName();
            String userAdress = first.getAuthor().getDiaspora_id();
            String userAvatar = first.getAuthor().getAvatar().getLarge();
            updateActionBar(userName, userAdress, userAvatar);
        }
        LOG.d(TAG_METHOD+ "Sortie");
    }
    @UiThread
    void updateActionBar(String userName, String userAdress, String userAvatar){
        String TAG_METHOD = TAG + ".updateActionBar : ";
        LOG.d(TAG_METHOD+ "Entrée");
        mTitleTextView.setText(userName);
        mSecondTitleTextView.setText(userAdress);
        setImageAvatarInView(userAvatar);
        LOG.d(TAG_METHOD+ "Sortie");
    }


    @Background
    public void setImageAvatarInView(String imagePath){
        String TAG_METHOD = TAG + ".setImageAvatarInView : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LOG.d(TAG_METHOD + "call diasporaBean.getImageFile with : "+imagePath);
        if(imagePath!=null) {
            imageAvatarDatas = diasporaService.getImageFile(imagePath);
            DiasporaConfig.setAvatarDatas(imageAvatarDatas);
            setImageAvatarInViewUIT();
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void setImageAvatarInViewUIT(){
        String TAG_METHOD = TAG + ".setImageAvatarInViewUIT : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (imageAvatarDatas!=null) {
            LOG.d(TAG_METHOD + "converting datas to bitmap");
            Bitmap imageAvatar = BitmapFactory.decodeByteArray(imageAvatarDatas, 0, imageAvatarDatas.length);
            mTitleAvatarView.setImageBitmap(imageAvatar);
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

//    @Background
//    public void setImageAvatarInView(String imagePath){
//        String TAG_METHOD = TAG + ".setImageAvatarInView : ";
//        LOG.d(TAG_METHOD + "Entrée");
//        try {
//            LOG.d(TAG_METHOD + "call diasporaBean.getImageFile with : " + imagePath);
//            diasporaService.setRootUrl(DiasporaControler.POD_URL);
//            imageAvatarDatas = diasporaService.getImageFile(imagePath);
//            if (imageAvatarDatas != null) {
//                LOG.d(TAG_METHOD + "converting datas to bitmap");
//                Bitmap imageAvatar = BitmapFactory.decodeByteArray(imageAvatarDatas, 0, imageAvatarDatas.length);
//                mTitleAvatarView.setImageBitmap(imageAvatar);
//            }
//        }catch (Throwable thr){LOG.e(TAG_METHOD + "Interception de l'exception " + thr.getMessage(), thr);}
//        LOG.d(TAG_METHOD+ "Sortie");
//    }
}
