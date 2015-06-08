package fr.android.scaron.diaspdroid.vues.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.acra.ACRA;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.Comment;

/**
 * Created by Sébastien on 28/03/2015.
 */
@EViewGroup(R.layout.fragment_contact)
public class CommentView extends LinearLayout{


    private static Logger LOGGEUR = LoggerFactory.getLogger(CommentView.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = CommentView.class.getSimpleName();

    final String mimeType = "text/html";
    final String encoding = "utf-8";

    @Bean
    DiasporaBean diasporaService;

    @ViewById(R.id.detailContact)
    RelativeLayout detailContact;
    @ViewById(R.id.detailContactImg)
    ImageView detailContactImg;
    @ViewById(R.id.detailContactTitle)
    TextView detailContactTitle;
    @ViewById(R.id.detailContactTxt)
    TextView detailContactTxt;
    @ViewById(R.id.detailContactWebSite)
    TextView detailContactWebSite;

    public Integer commentID;

    Comment comment;
    Context context;
    byte[] imageLinkDatas = null;

    public CommentView(Context context) {
        super(context);
        this.context = context;
    }

    public void bind(final Comment comment) {
        this.comment = comment;
        setComment(comment);

    }


    @Background
    public void setImageLinkInView(String imagePath){
        String TAG_METHOD = TAG + ".setImageLinkInView : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LOG.d(TAG_METHOD + "call diasporaBean.getImageFile with : "+imagePath);
        imageLinkDatas = diasporaService.getImageFile(imagePath);
        setImageLinkInViewUIT();
        LOG.d(TAG_METHOD+ "Sortie");
    }

    @UiThread
    public void setImageLinkInViewUIT(){
        String TAG_METHOD = TAG + ".setImageLinkInViewUIT : ";
        LOG.d(TAG_METHOD+ "Entrée");
        if (imageLinkDatas!=null) {
            LOG.d(TAG_METHOD + "converting datas to bitmap");
            Bitmap imageAvatar = BitmapFactory.decodeByteArray(imageLinkDatas, 0, imageLinkDatas.length);
            detailContactImg.setImageBitmap(imageAvatar);
        }
        LOG.d(TAG_METHOD + "Sortie");
    }


    public void setComment(Comment comment) {
        LOG.d(".setComment entree with post : "+ comment);
        try {
            LOG.d(".setComment getSettings set params");
            detailContactTitle.setText(comment.getAuthor().getName());
            detailContactTxt.setText(comment.getText());
            detailContactWebSite.setText(comment.getCreated_at().toString());
            setImageLinkInView(comment.getAuthor().getAvatar().getLarge());
            LOG.d(".setComment sortie en succès");
        }catch(Throwable thr) {
            LOG.e(".setComment sortie en Erreur ("+thr.toString()+")", thr);
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

}
