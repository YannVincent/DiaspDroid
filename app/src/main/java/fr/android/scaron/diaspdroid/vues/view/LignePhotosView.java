package fr.android.scaron.diaspdroid.vues.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private static Logger LOGGEUR = LoggerFactory.getLogger(LignePhotosView.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = LignePhotosView.class.getSimpleName();

    final static int SELECT_PICTURE = 1;

    @Bean
    DiasporaBean diasporaService;

    @ViewById(R.id.imageview_share_add_g)
    LinearLayout imageview_share_add_g;
    @ViewById(R.id.imageview_share_g)
    RelativeLayout imageview_share_g;
    @ViewById(R.id.imageview_share_image_g)
    ImageView imageview_share_image_g;
//    @ViewById(R.id.imageview_share_image_remove_g)
//    ImageView imageview_share_image_remove_g;

    @ViewById(R.id.imageview_share_add_d)
    LinearLayout imageview_share_add_d;
    @ViewById(R.id.imageview_share_d)
    RelativeLayout imageview_share_d;
    @ViewById(R.id.imageview_share_image_d)
    ImageView imageview_share_image_d;
//    @ViewById(R.id.imageview_share_image_remove_d)
//    ImageView imageview_share_image_remove_d;


    LignePhotosView previousLigne;
    LignePhotosView nextLigne;

    String localPath_g;
    String localPath_d;
    boolean empty=true;
    boolean full=false;
    Context context;

    public LignePhotosView(final Context context) {
        super(context);
        this.context = context;
    }


    @Click({R.id.imageview_share_add_image_g, R.id.imageview_share_add_g})
    public void clickAddLeft(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity)context).startActivityForResult(Intent.createChooser(intent, ""), SELECT_PICTURE);
    }
    @Click({R.id.imageview_share_add_image_d, R.id.imageview_share_add_d})
    public void clickAddRight(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity)context).startActivityForResult(Intent.createChooser(intent, ""), SELECT_PICTURE);
    }

    public LignePhotosView addPhoto(String localPath){
        if (full){
            nextLigne = LignePhotosView_.build(context);
            nextLigne.addPhoto(localPath);
            nextLigne.setPreviousLigne(this);
            return nextLigne;
        }
        if (!empty){
            return addRight(localPath);
        }
        return addLeft(localPath);
    }

    public void setPreviousLigne(LignePhotosView prevLigne){
        this.previousLigne = prevLigne;
    }

    public LignePhotosView addLeft(String localPath){
        String TAG_METHOD = TAG + ".addLeft > ";
        LOG.d(TAG_METHOD + "Entrée");
        //add photo at leftt side
        localPath_g = localPath;

        LOG.d(TAG_METHOD + "localPath_g set : " + localPath_g);
        //call background method ?
        bindLeft();
        // no more empty yet
        empty = false;
        LOG.d(TAG_METHOD + "Sortie");
        return null;
    }

    public LignePhotosView addRight(String localPath){
        String TAG_METHOD = TAG + ".addRight > ";
        LOG.d(TAG_METHOD + "Entrée");
        //add photo at right side
        localPath_d = localPath;
        LOG.d(TAG_METHOD + "localPath_d set : " + localPath_d);
        //call background method ?
        bindRight();
        LOG.d(TAG_METHOD + "set full");
        full=true;
        LOG.d(TAG_METHOD + "Sortie");
        return null;
    }

    @Background
    public void bindLeft(){
        String TAG_METHOD = TAG + ".getBitmap > ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "On récupère les données de l'image pour l'url : " + localPath_g);
        Bitmap bitmap = BitmapFactory.decodeFile(localPath_g);
        if (bitmap==null){
            LOG.e(TAG_METHOD + "Erreur de traitement innatendu pour l'image");
        }else {
            addLeftImageView(bitmap);
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void addLeftImageView(Bitmap bitmap){
        String TAG_METHOD = TAG + ".addImageView > ";
        LOG.d(TAG_METHOD + "Entrée");
        imageview_share_image_g.setImageBitmap(bitmap);
        LOG.d(TAG_METHOD + "imageview_share_g VISIBLE");
        imageview_share_g.setVisibility(VISIBLE);
        LOG.d(TAG_METHOD + "imageview_share_add_g GONE");
        imageview_share_add_g.setVisibility(GONE);
        LOG.d(TAG_METHOD + "imageview_share_add_d VISIBLE");
        imageview_share_add_d.setVisibility(VISIBLE);
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Background
    public void bindRight(){
        String TAG_METHOD = TAG + ".bindRight > ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "On récupère les données de l'image pour l'url : " + localPath_g);
        Bitmap bitmap = BitmapFactory.decodeFile(localPath_g);
        if (bitmap==null){
            LOG.e(TAG_METHOD + "Erreur de traitement innatendu pour l'image");
        }else {
            addLeftImageView(bitmap);
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void addRightImageView(Bitmap bitmap){
        String TAG_METHOD = TAG + ".addRightImageView > ";
        LOG.d(TAG_METHOD + "Entrée");
        imageview_share_image_g.setImageBitmap(bitmap);
        LOG.d(TAG_METHOD + "imageview_share_d VISIBLE");
        imageview_share_d.setVisibility(VISIBLE);
        LOG.d(TAG_METHOD + "imageview_share_add_d GONE");
        imageview_share_add_d.setVisibility(GONE);
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
            imageview_share_add_d.setVisibility(VISIBLE);
            imageview_share_g.setVisibility(GONE);
            localPath_g = null;
            empty = true;
        }
    }

    @Click(R.id.imageview_share_image_remove_d)
    public void removeRight(){
        imageview_share_d.setVisibility(GONE);
        imageview_share_add_d.setVisibility(INVISIBLE);
        if (!empty){
            imageview_share_add_d.setVisibility(VISIBLE);
        }
        localPath_d = null;
        full = false;
        localPath_d = null;
    }
}
