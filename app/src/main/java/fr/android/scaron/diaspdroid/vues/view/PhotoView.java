package fr.android.scaron.diaspdroid.vues.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;

/**
 * Created by Sébastien on 22/05/2015.
 */
@EViewGroup(R.layout.imageview_share)
public class PhotoView extends RelativeLayout {

    private static Logger LOGGEUR = LoggerFactory.getLogger(PhotoView.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = PhotoView.class.getSimpleName();

    final static int SELECT_PICTURE = 1;
    final static String FAKE_ADDPHOTO = "FAKE_ADDPHOTO";

    @Bean
    DiasporaBean diasporaService;

    @ViewById(R.id.imageview_share_add)
    LinearLayout imageview_share_add;

    @ViewById(R.id.imageview_share)
    RelativeLayout imageview_share;
    @ViewById(R.id.imageview_share_image)
    ImageView imageview_share_image;

    @ViewById(R.id.imageview_share_progress)
    ProgressBar imageview_share_progress;

    String localPath;
    Context context;

    public PhotoView(final Context context) {
        super(context);
        this.context = context;
    }

    public PhotoView bind(String localPath){
        String TAG_METHOD = TAG + ".addLeft > ";
        LOG.d(TAG_METHOD + "Entrée");
        //add photo at leftt side
        this.localPath = localPath;
        imageview_share_progress.setIndeterminate(true);
        LOG.d(TAG_METHOD + "localPath set : " + localPath);
        if (FAKE_ADDPHOTO.equals(localPath)) {
            imageview_share_add.setVisibility(VISIBLE);
            imageview_share.setVisibility(GONE);
//            showAddButton();
        }else {//call background method ?
            imageview_share_add.setVisibility(GONE);
            imageview_share.setVisibility(GONE);
            imageview_share_progress.setVisibility(VISIBLE);
            getBitmap();
        }
        LOG.d(TAG_METHOD + "Sortie");
        return this;
    }

    @UiThread
    public void getBitmap(){
        String TAG_METHOD = TAG + ".getBitmap > ";
        LOG.d(TAG_METHOD + "Entrée");
//        imageview_share_add.setVisibility(GONE);
        LOG.d(TAG_METHOD + "On récupère les données de l'image pour l'url : " + localPath);
        //Fix for memory consumption
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
//        options.inJustDecodeBounds = true;
        //End of Fix for memory consumption
        Bitmap bitmap = BitmapFactory.decodeFile(localPath,options);
        showBitmap(bitmap);
        LOG.d(TAG_METHOD + "Sortie");
    }


    @UiThread
    public void showBitmap(Bitmap bitmap){
        String TAG_METHOD = TAG + ".showBitmap > ";
        LOG.d(TAG_METHOD + "Entrée");
        if (bitmap==null){
            LOG.e(TAG_METHOD + "Erreur de traitement innatendu pour l'image");
        }else {
            imageview_share_image.setImageBitmap(bitmap);
            imageview_share_progress.setVisibility(GONE);
            imageview_share.setVisibility(VISIBLE);
//            addImageView(bitmap);
        }
        LOG.d(TAG_METHOD + "Sortie");
    }


    @Click(R.id.imageview_share_image_remove)
    public void remove(){
        localPath = null;
    }

    @Click(R.id.imageview_share_add_image)
    public void add(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity) DiasporaConfig.APPLICATION_CONTEXT).startActivityForResult(Intent.createChooser(intent, ""), SELECT_PICTURE);
    }
}
