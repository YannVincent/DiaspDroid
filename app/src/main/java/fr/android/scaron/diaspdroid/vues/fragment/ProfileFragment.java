package fr.android.scaron.diaspdroid.vues.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
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
import org.w3c.dom.Text;

import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.User;

@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends Fragment {

    private static Logger LOGGEUR = LoggerFactory.getLogger(ProfileFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = ProfileFragment.class.getSimpleName();
    @Bean
    DiasporaBean diasporaBean;

    @ViewById(R.id.profile_avatar_icon)
    ImageView avatar;
    @ViewById(R.id.profile_name)
    TextView name;
    @ViewById(R.id.profile_adress)
    TextView adress;
    @ViewById(R.id.tags)
    TextView tags;
    @ViewById(R.id.location)
    TextView location;
    @ViewById(R.id.genre)
    TextView genre;
    ActionBarActivity activity;

    User user;

    public void setActivityParent(ActionBarActivity activity){
        this.activity = activity;
    }

    @Background
    void getInfos(){
        String TAG_METHOD = TAG + ".getInfos : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD+ "call diasporaBean.getContacts");
        user = diasporaBean.getInfo(DiasporaConfig.POD_USER);
        bindDatas();
        LOG.d(TAG_METHOD + "Sortie");
    }
    @UiThread
    void bindDatas(){
        String TAG_METHOD = TAG + ".bindDatas : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (user != null) {
            if (DiasporaConfig.avatarDatas!=null) {
                LOG.d(TAG_METHOD + "converting datas to bitmap");
                Bitmap imageAvatar = BitmapFactory.decodeByteArray(DiasporaConfig.avatarDatas, 0, DiasporaConfig.avatarDatas.length);
                avatar.setImageBitmap(imageAvatar);
            }
            name.setText(user.getName());
            adress.setText(user.getDiaspora_id());
            StringBuilder sb = new StringBuilder();
            for (String tag : user.getProfile().getTags()){
                sb.append(tag+ " ");
            }
            tags.setText(sb.toString());
            location.setText(user.getProfile().getLocation());
            genre.setText(user.getProfile().getGender());
        }
        LOG.d(TAG_METHOD+ "Sortie");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String TAG_METHOD = TAG + ".onCreate : ";
        LOG.d(TAG_METHOD+ "Entrée");
        try{
            super.onCreate(savedInstanceState);LOG.d(TAG_METHOD+ "call getInfos");
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
