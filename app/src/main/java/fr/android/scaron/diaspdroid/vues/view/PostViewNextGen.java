package fr.android.scaron.diaspdroid.vues.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
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

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.Photo;
import fr.android.scaron.diaspdroid.model.Post;

/**
 * Created by Sébastien on 20/05/2015.
 */
@EViewGroup(R.layout.postnextgen)
public class PostViewNextGen extends LinearLayout {
    private static Logger LOGGEUR = LoggerFactory.getLogger(PostView.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "PostViewNextGen";

    @Bean
    DiasporaBean diasporaService;

//    @ViewById(R.id.scrollpostnextgen)
//    ScrollView scrollpostnextgen;
    @ViewById(R.id.postnextgen_images)
    LinearLayout postnextgen_images;
    @ViewById(R.id.scrollpostnextgen_text)
    TextView scrollpostnextgen_text;


    Post post;
    Context context;

    public PostViewNextGen(final Context context) {
        super(context);
        this.context = context;
    }

    public void bind(Post post){
        String TAG_METHOD = TAG + ".bind";
        LOG.d(TAG_METHOD + "Entrée");
        this.post = post;
        LOG.d(TAG_METHOD + "On ajoute les photos à la vue");
        addPhotos();
        scrollpostnextgen_text.setText(post.getText());
        LOG.d(TAG_METHOD + "Sortie");
    }


    @Background
    public void addPhotos(){
        String TAG_METHOD = TAG + ".addPhotos";
        LOG.d(TAG_METHOD + "Entrée");
        for (Photo photo:post.getPhotos()){
            LOG.d(TAG_METHOD + "On récupère les données de l'image pour l'url : "+photo.getSizes().getMedium());
            byte[] imageDatas = diasporaService.getImageFile(photo.getSizes().getLarge());
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
            final ImageView imageView = new ImageView(context);
            imageView.setTag(post.getId());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String TAG_METHOD = "ImageView.onClick";
                    LOG.d(TAG_METHOD + "Entrée");
                    // TODO Auto-generated method stub
                    LOG.d(TAG_METHOD + imageView.getTag());
                }
            });

//            //Test autre méthode pour positionner l'image
//            Display display = this.getDisplay();
//            Point size = new Point();
//            display.getSize(size);
//            int width = size.x;
////            int height = size.y;
//            Matrix mat = new Matrix();
//            int imgHeight = bitmap.getHeight();
//            int imgWidth = bitmap.getWidth();
//            LOG.d(TAG_METHOD+ " calcul du ratio : (long)"+imgWidth+"/(long)"+imgHeight+";");
//            double ratio = (double)((double)imgWidth/(double)imgHeight);
//            LOG.d(TAG_METHOD+ " calcul de la nouvelle largeur : = "+width);
//            int newWidth = width;
//            LOG.d(TAG_METHOD + " calcul de la nouvelle hauteur : (float)(" + newWidth + " / " + ratio + ") = " + (float) (newWidth / ratio) + ";");
//            int newHeight = Integer.parseInt("" + (int) (newWidth / ratio)) ;
//            Bitmap bMapOptimal = Bitmap.createBitmap(bitmap, 0, 0, newWidth, newHeight, mat, true);
//            imageView.setImageBitmap(bMapOptimal);

            imageView.setImageBitmap(bitmap);
//        imageView.setImageResource(R.drawable.ic_launcher);
            addImageViewInHScroll(imageView);
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void addImageViewInHScroll(ImageView imageView){
        String TAG_METHOD = TAG + ".addPhotos";
        LOG.d(TAG_METHOD + "Entrée");
        postnextgen_images.addView(imageView);
        LOG.d(TAG_METHOD + "Sortie");
    }
}
