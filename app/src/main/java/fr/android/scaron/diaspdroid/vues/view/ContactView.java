package fr.android.scaron.diaspdroid.vues.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
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
import fr.android.scaron.diaspdroid.model.Contact;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
//import fr.android.scaron.diaspdroid.vues.fragment.YoutubePlayerFragment;

/**
 * Created by Sébastien on 28/03/2015.
 */
@EViewGroup(R.layout.fragment_contact)
public class ContactView extends LinearLayout{


    private static Logger LOGGEUR = LoggerFactory.getLogger(ContactView.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "ContactView";

    final String mimeType = "text/html";
    final String encoding = "utf-8";

    @Bean
    DiasporaBean diasporaService;

    @ViewById(R.id.detailErreurContact)
    LinearLayout detailErreurContact;
    @ViewById(R.id.detailErreurContactText)
    TextView detailErreurContactText;

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

    public Integer contactId;

    Contact contact;
    Context context;
    byte[] imageLinkDatas = null;

    public ContactView(Context context) {
        super(context);
        this.context = context;
    }

    public void bind(final Contact contact) {
        this.contact = contact;
        this.contactId = contact.getId();
        if (this.contactId == 0){
            // *** Detail de post en erreur
            // Remplissage de la partie texte
            detailErreurContactText.setText(contact.getName());
            detailContact.setVisibility(GONE);
            detailErreurContact.setVisibility(VISIBLE);
            return;
        }
        LOG.d(".getView videos from post " + contact.getId() + "( instance id : " + this + ")");


        OnClickListener urlclickListener = new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (contact.getUrl()!=null && !contact.getUrl().isEmpty()){
                    // Launching Browser Screen
                    Intent myWebLink = new Intent(Intent.ACTION_VIEW);
                    myWebLink.setData(Uri.parse(DiasporaConfig.POD_URL+contact.getUrl()));
                    myWebLink.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(myWebLink);
                }
            }
        };
        detailContact.setOnClickListener(urlclickListener);
        setContact(contact);

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


    public void setContact(Contact contact) {
        LOG.d(".setContact entree with post : "+contact);
        try {
            LOG.d(".setContact getSettings set params");
            detailContactTitle.setText(contact.getName());
            detailContactTxt.setText(contact.getHandle());
            detailContactWebSite.setText(contact.getUrl());
            setImageLinkInView(contact.getAvatar());
            LOG.d(".setContact sortie en succès");
        }catch(Throwable thr) {
            LOG.e(".setContact sortie en Erreur ("+thr.toString()+")", thr);
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

}
