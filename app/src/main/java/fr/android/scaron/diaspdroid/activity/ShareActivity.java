package fr.android.scaron.diaspdroid.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.UploadResult;

/**
 * Created by Sébastien on 25/03/2015.
 */
@EActivity(R.layout.activity_share)
public class ShareActivity  extends ActionBarActivity {

    private static Logger LOGGEUR = LoggerFactory.getLogger(ShareActivity.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static final String TAG = "ShareActivity";

    @Extra
    Uri imageUri;

    @Bean
    DiasporaBean diasporaBean;

    @ViewById(R.id.share_message)
    EditText shareMessage;

    @ViewById(R.id.share_image)
    ImageView shareImage;

    @ViewById(R.id.share_text_button)
    Button shareTextButton;

    @ViewById(R.id.progress_horizontal)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String TAG_METHOD = TAG + ".onCreate : ";
        LOG.d(TAG_METHOD + "entrée");
        try {
            super.onCreate(savedInstanceState);
            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if (type.startsWith("image/") && imageUri == null) {
                    imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                }
            }
        }catch(Throwable thr){
            LOG.e(TAG_METHOD + "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(TAG_METHOD + "sortie en erreur");
            throw thr;
        }
        LOG.d(TAG_METHOD + "sortie");
    }


    @AfterViews
    void init() {
        String TAG_METHOD = TAG + ".init -> ";
        LOG.d(TAG_METHOD + "entrée");
        try {
            LOG.d(TAG_METHOD + "Add Activity in Config : " + this);
            DiasporaConfig.addActivity(this);
            LOG.d(TAG_METHOD + "Init Config with application and context");
            DiasporaConfig.init(this.getApplication(), this);
        }catch(Throwable thr) {
            LOG.e(TAG_METHOD + "Erreur : " + thr.toString(), thr);
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(TAG_METHOD + "sortie");
            throw thr;
        }
    }

//    @AfterExtras
    @AfterViews
    public void updateScreen(){
        String TAG_METHOD = TAG + ".updateScreen : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (imageUri == null){
            shareMessage.setText("Le partage de la photo a échouéé\n" +
                    "(Erreur obtenue : pas d'Uri de l'image obtenue)");
            return;
        }
        LOG.d(TAG_METHOD + "setImageURI to shareImage : "+imageUri);
        shareImage.setImageURI(imageUri);
        LOG.d(TAG_METHOD + "getPath for imageUri : "+imageUri);
        final String imageLocalPath = getPath(imageUri);
        LOG.d(TAG_METHOD + "getPath for imageLocalPath : "+imageLocalPath);
        if (imageLocalPath == null){
            shareMessage.setText("Le partage de la photo a échouéé\n" +
                    "(Erreur obtenue : pas de chemin local de l'image obtenu)");
            return;
        }
        LOG.d(TAG_METHOD + "getImageName for imageLocalPath : "+imageLocalPath);
        final String imageName = imageLocalPath.substring(imageLocalPath.lastIndexOf('/')+1);
        shareMessage.setText("Partage de la photo à faire : " + imageName + " ("+imageLocalPath+")");
        LOG.d(TAG_METHOD + "Sortie");
    }


    @Click(R.id.share_text_button)
    public void launchSharing(){
        String TAG_METHOD = TAG + ".updateScreen : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (imageUri == null){
            shareMessage.setText("Le partage de la photo a échouéé\n" +
                    "(Erreur obtenue : pas d'Uri de l'image obtenue)");
            return;
        }
        LOG.d(TAG_METHOD + "setImageURI to shareImage : "+imageUri);
        shareImage.setImageURI(imageUri);
        LOG.d(TAG_METHOD + "getPath for imageUri : "+imageUri);
        final String imageLocalPath = getPath(imageUri);
        LOG.d(TAG_METHOD + "getPath for imageLocalPath : "+imageLocalPath);
        if (imageLocalPath == null){
            shareMessage.setText("Le partage de la photo a échouéé\n" +
                    "(Erreur obtenue : pas de chemin local de l'image obtenu)");
            return;
        }
        LOG.d(TAG_METHOD + "getImageName for imageLocalPath : "+imageLocalPath);
        final String imageName = imageLocalPath.substring(imageLocalPath.lastIndexOf('/')+1);
        shareMessage.setText("Partage de la photo en cours: " + imageName + " ("+imageLocalPath+")");
        LOG.d(TAG_METHOD + "appel de shareImage");
        shareImage(imageName, imageLocalPath);
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Background
    public void shareImage(String fileName, String localPath){
        String TAG_METHOD = TAG + ".getInfosUserForBar : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "call diasporaBean.uploadFile");
        UploadResult uploadFileResult = diasporaBean.uploadFile(fileName, localPath);
        checkSharePic(uploadFileResult);
    }

    @UiThread
    public void checkSharePic(UploadResult uploadResult){
        String TAG_METHOD = TAG + ".checkSharePic : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "response body ? : "+uploadResult);
        if (uploadResult==null){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DiasporaConfig.APPLICATION_CONTEXT);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setTitle("Echec de publication");
            alertDialog.setMessage("Votre publication a échouée\n" +
                    "(Erreur obtenue : pas de réponse du service)");
            alertDialog.show();
//                uploadProgressBar.setVisibility(View.GONE);
            shareMessage.setText("Le partage de la photo a échouéé\n" +
                    "(Erreur obtenue : pas de réponse du service)");
            return;
        }

        LOG.d(TAG_METHOD + "response body : "+uploadResult.toString());
        if (uploadResult.getError()!=null){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DiasporaConfig.APPLICATION_CONTEXT);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setTitle("Echec de publication");
            alertDialog.setMessage("Votre publication a échouée\n(Erreur obtenue : "+uploadResult.getError()+")");
            alertDialog.show();
//            uploadProgressBar.setVisibility(View.GONE);
            shareMessage.setText("Le partage de la photo a échouéé\n" +
                    "(Erreur obtenue : \"+uploadResult.getError()+\")\");");
            return;
        }
        if (uploadResult.getData()!=null && uploadResult.getData().getPhoto()!=null){
            String urlSharedImg = null;
            if (uploadResult.getData().getPhoto().getUnprocessed_image()!=null){
                urlSharedImg = uploadResult.getData().getPhoto().getUnprocessed_image().getUrl();
            }else if  (uploadResult.getData().getPhoto().getProcessed_image()!=null){
                urlSharedImg = uploadResult.getData().getPhoto().getProcessed_image().getUrl();
            }
            if (urlSharedImg==null){
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DiasporaConfig.APPLICATION_CONTEXT);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setIcon(R.drawable.ic_launcher);
                alertDialog.setTitle("Publication réussie");
                alertDialog.setMessage("Votre publication a bien été effectuée\n--------------\nimage:"+
                        urlSharedImg+"\n-------\nteste:"+shareMessage.getText().toString());
                alertDialog.show();
                LOG.d(TAG_METHOD + "publication réussie");
//                uploadProgressBar.setVisibility(View.GONE);
                shareMessage.setText("Votre publication a bien été effectuée\n--------------\nimage:"+
                        urlSharedImg+"\n-------\nteste:"+shareMessage.getText().toString());
                return;
            }else{
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DiasporaConfig.APPLICATION_CONTEXT);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setIcon(R.drawable.ic_launcher);
                alertDialog.setTitle("Echec de publication");
                alertDialog.setMessage("Votre publication a échouée");
                alertDialog.show();
//                uploadProgressBar.setVisibility(View.GONE);
                shareMessage.setText("Le partage de la photo a échouéé");
                return;
            }
        }
        shareMessage.setText("Le partage de la photo a échouéé");
//                            ACRA.getErrorReporter().handleException(e);
//    uploadProgressBar.setVisibility(View.GONE);
    }


    /**
     * helper to retrieve the path of an image URI
     */
    private String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
}
