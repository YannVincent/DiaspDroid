package fr.android.scaron.diaspdroid.vues.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import java.util.ArrayList;

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
    @ViewById(R.id.postnextgen_avatar)
    ImageView postnextgen_avatar;
    @ViewById(R.id.postnextgen_user)
    TextView postnextgen_user;
    @ViewById(R.id.postnextgen_datetime)
    TextView postnextgen_datetime;
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
        String TAG_METHOD = TAG + ".bind >";
        LOG.d(TAG_METHOD + "Entrée");
        this.post = post;
        LOG.d(TAG_METHOD + "On appel la focntion de traitement des photos");
        addAvatar();
        addPhotos();
        postnextgen_user.setText(post.getAuthor().getName());
        postnextgen_datetime.setText(post.getCreated_at_str());
        scrollpostnextgen_text.setText(post.getText());
        LOG.d(TAG_METHOD + "Sortie");
    }
    @Background
    public void addAvatar(){
        String TAG_METHOD = TAG + ".addAvatar > ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "On récupère les données de l'image pour l'url : " + post.getAuthor().getAvatar().getLarge());
        byte[] imageDatas = diasporaService.getImageFile(post.getAuthor().getAvatar().getLarge());
        LOG.d(TAG_METHOD + "imageData is null ? " + (imageDatas == null));
        if (imageDatas != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
            if (bitmap==null){
                LOG.e(TAG_METHOD + "Erreur de traitement innatendu pour l'image");
            }else {
                addImageViewAvatar(bitmap);
            }
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void addImageViewAvatar(Bitmap bitmap){
        String TAG_METHOD = TAG + ".addImageViewAvatar > ";
        LOG.d(TAG_METHOD + "Entrée");
        postnextgen_avatar.setImageBitmap(bitmap);
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Background
    public void addPhotos(){
        String TAG_METHOD = TAG + ".addPhotos > ";
        LOG.d(TAG_METHOD + "Entrée");
        ArrayList<Photo> photos = post.getPhotos();
        if (photos!=null && photos.size()>0){
            LOG.d(TAG_METHOD + post.getPhotos().size()+" photos à ajouter");
        }else{
            LOG.d(TAG_METHOD +" aucune photo à ajouter");
            LOG.d(TAG_METHOD + "Sortie");
            return;
        }

        int photoSize = photos.size();
        int indexPhoto = 0;
        for (Photo photo:photos) {
            LOG.d(TAG_METHOD + "On récupère les données de l'image pour l'url : " + photo.getSizes().getMedium());
            indexPhoto++;
            byte[] imageDatas = diasporaService.getImageFile(photo.getSizes().getLarge());
            LOG.d(TAG_METHOD + "imageData is null ? "+(imageDatas==null));
            if (imageDatas != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
                if (bitmap==null){
                    LOG.e(TAG_METHOD + "Erreur de traitement innatendu pour l'image");
                }else {
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

                    //Test autre méthode pour positionner l'image
                    DisplayMetrics metrics = new DisplayMetrics();
                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    display.getMetrics(metrics);
                    int width = metrics.widthPixels-(int)dipToPixels(context, 30);
////                    Display display = this.getDisplay();
//                    Point size = new Point();
//                    display.getSize(size);
//                    int width = size.x;
//                    //            int height = size.y;
//                    Matrix mat = new Matrix();
                    int imgHeight = bitmap.getHeight();
                    int imgWidth = bitmap.getWidth();
                    LOG.d(TAG_METHOD + " calcul du ratio : (long)" + imgWidth + "/(long)" + imgHeight + ";");
                    double ratio = (double) ((double) imgWidth / (double) imgHeight);
                    LOG.d(TAG_METHOD + " calcul de la nouvelle largeur : = " + width);
                    int newWidth = width;
                    LOG.d(TAG_METHOD + " calcul de la nouvelle hauteur : (float)(" + newWidth + " / " + ratio + ") = " + (float) (newWidth / ratio) + ";");
                    int newHeight = Integer.parseInt("" + (int) (newWidth / ratio));

                    imageView.setImageBitmap(bitmap);
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(newWidth, newHeight));
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    addImageViewInHScroll(imageView, (indexPhoto==photoSize));
                }
            }
        }
        LOG.d(TAG_METHOD + "Sortie");
    }
    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
    @UiThread
    public void addImageViewInHScroll(ImageView imageView, boolean isLast){
        String TAG_METHOD = TAG + ".addPhotos";
        LOG.d(TAG_METHOD + "Entrée");
        postnextgen_images.addView(imageView);
        if (!isLast) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setMinimumWidth((int)dipToPixels(context, 30));
            postnextgen_images.addView(linearLayout);
        }
        LOG.d(TAG_METHOD + "Sortie");
    }
}
