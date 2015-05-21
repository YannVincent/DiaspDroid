package fr.android.scaron.diaspdroid.vues.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import org.androidannotations.annotations.Background;
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

/**
 * Created by Sébastien on 22/05/2015.
 */
@EViewGroup(R.layout.listphotosview)
public class LignePhotosView extends TableRow {

    private static Logger LOGGEUR = LoggerFactory.getLogger(PostView.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "LignePhotosView";

    @Bean
    DiasporaBean diasporaService;

    @ViewById(R.id.imageview_share_g)
    RelativeLayout imageview_share_g;
    @ViewById(R.id.imageview_share_image_g)
    ImageView imageview_share_image_g;
    @ViewById(R.id.imageview_share_image_remove_g)
    ImageView imageview_share_image_remove_g;

    @ViewById(R.id.imageview_share_d)
    RelativeLayout imageview_share_d;
//    @ViewById(R.id.imageview_share_image_d)
//    ImageView imageview_share_image_d;
//    @ViewById(R.id.imageview_share_image_remove_d)
//    ImageView imageview_share_image_remove_d;


    LignePhotosView previousLigne;
    LignePhotosView nextLigne;

    String localPath_g;
    String localPath_d;
    boolean empty=false;
    boolean full=false;
    Context context;

    public LignePhotosView(final Context context) {
        super(context);
        this.context = context;
    }

    public boolean addPhoto(String localPath){
        if (full){
            nextLigne = LignePhotosView_.build(context);
            nextLigne.addPhoto(localPath);
            return false;
        }
        if (!empty){
            //add photo at right side
            localPath_d = localPath;
            //call background method ?
            bindRight();
            imageview_share_d.setVisibility(VISIBLE);
            full=true;
            return true;
        }
        //add photo at leftt side
        localPath_g = localPath;
        //call background method ?
        bindLeft();
        // no more empty yet
        empty = false;
        imageview_share_g.setVisibility(VISIBLE);
        return true;
    }

    @Background
    public void bindLeft(){
        String TAG_METHOD = TAG + ".addAvatar > ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "On récupère les données de l'image pour l'url : " + localPath_g);
        byte[] imageDatas = diasporaService.getImageFile(localPath_g);
        LOG.d(TAG_METHOD + "imageData is null ? " + (imageDatas == null));
        if (imageDatas != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
            if (bitmap==null){
                LOG.e(TAG_METHOD + "Erreur de traitement innatendu pour l'image");
            }else {
                addLeftImageView(bitmap);
            }
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void addLeftImageView(Bitmap bitmap){
        String TAG_METHOD = TAG + ".addLeftImageView > ";
        LOG.d(TAG_METHOD + "Entrée");
        imageview_share_image_g.setImageBitmap(bitmap);
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Background
    public void bindRight(){
        String TAG_METHOD = TAG + ".addAvatar > ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "On récupère les données de l'image pour l'url : " + localPath_g);
        byte[] imageDatas = diasporaService.getImageFile(localPath_g);
        LOG.d(TAG_METHOD + "imageData is null ? " + (imageDatas == null));
        if (imageDatas != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
            if (bitmap==null){
                LOG.e(TAG_METHOD + "Erreur de traitement innatendu pour l'image");
            }else {
                addLeftImageView(bitmap);
            }
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void addRightImageView(Bitmap bitmap){
        String TAG_METHOD = TAG + ".addLeftImageView > ";
        LOG.d(TAG_METHOD + "Entrée");
        imageview_share_image_g.setImageBitmap(bitmap);
        LOG.d(TAG_METHOD + "Sortie");
    }

    public boolean isFull(){
        return full;
    }


    public boolean isEmpty(){
        return empty;
    }

    @Click(R.id.imageview_share_image_remove_g)
    public void removeLeft(){
        if (full) {
            localPath_g = localPath_d;
            bindLeft();
            removeRight();
        }else{
            imageview_share_g.setVisibility(INVISIBLE);
            localPath_g = null;
        }
    }

    @Click(R.id.imageview_share_image_remove_d)
    public void removeRight(){
        imageview_share_d.setVisibility(INVISIBLE);
        localPath_d = null;
    }
}
