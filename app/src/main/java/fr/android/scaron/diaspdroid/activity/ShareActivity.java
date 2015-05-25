package fr.android.scaron.diaspdroid.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.NewPost;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.model.StatusMessage;
import fr.android.scaron.diaspdroid.model.UploadResult;

/**
 * Created by Sébastien on 25/03/2015.
 */
@EActivity(R.layout.activity_share)
@OptionsMenu(R.menu.share)
public class ShareActivity extends ActionBarActivity {

    private static Logger LOGGEUR = LoggerFactory.getLogger(ShareActivity.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = ShareActivity.class.getSimpleName();
    final static int SELECT_PICTURE = 1;

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    @Extra
    Uri imageUri;
    int imageOrientation=-1;
    byte[] photoBytes;
    @Extra
    String activityParent;
    @Extra
    Integer postIDParent;
    String imageLocalPath;
    String uploadedFilePath;
    String uploadedFileGuid;
    Integer uploadedFileId;

    @Bean
    DiasporaBean diasporaBean;

    @ViewById(R.id.share_text_entry)
    EditText shareTextEntry;

    @ViewById(R.id.share_message)
    TextView shareMessage;

    @ViewById(R.id.share_image)
    ImageView shareImage;

    @ViewById(R.id.header_share_photo)
    ImageView headerSharePhoto;

    @ViewById(R.id.header_comment_avatar)
    ImageView headerAvatar;

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
            LOG.d(TAG_METHOD + "action ? "+action);
            String type = intent.getType();
            LOG.d(TAG_METHOD + "type ? "+type);
            activityParent = intent.getStringExtra(Intent.EXTRA_TEXT);
            LOG.d(TAG_METHOD + "activityParent ? "+activityParent);
            postIDParent = intent.getIntExtra(Intent.EXTRA_REFERRER, 0);
            LOG.d(TAG_METHOD + "postIDParent ? "+ postIDParent);

            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if (type.startsWith("image/") && imageUri == null) {
                    imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                }
            }
            if (imageUri!=null || postIDParent==null){
                if (headerSharePhoto!=null) {
                    headerSharePhoto.setVisibility(View.GONE);
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


    public void retourMenuPrincipal() {
        Intent i = new Intent(this,
                MainActivity_.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    @Override
    public void onBackPressed() {
        if ("MainActivity".equals(activityParent)) {
            Intent i = new Intent(this,
                    MainActivity_.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }else{
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
            {
                super.onBackPressed();
                finish();
                return;
            }
            else {
                Toast.makeText(getBaseContext(), "Cliquez deux fois sur 'retour' pour quitter l'application", Toast.LENGTH_LONG).show();
            }
            mBackPressed = System.currentTimeMillis();
        }
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
            if (headerSharePhoto!=null && (imageUri!=null|| postIDParent==null)) {
                headerSharePhoto.setVisibility(View.GONE);
            }

            if (DiasporaConfig.avatarDatas!=null) {
                LOG.d(TAG_METHOD + "converting datas to bitmap");
                Bitmap imageAvatar = BitmapFactory.decodeByteArray(DiasporaConfig.avatarDatas, 0, DiasporaConfig.avatarDatas.length);
                headerAvatar.setImageBitmap(imageAvatar);
            }
        }catch(Throwable thr) {
            LOG.e(TAG_METHOD + "Erreur : " + thr.toString(), thr);
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(TAG_METHOD + "sortie");
            throw thr;
        }
    }

    @AfterViews
    public void updateScreen(){
        String TAG_METHOD = TAG + ".updateScreen : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (imageUri == null){
            shareMessage.setText("Pas de partage de photo possible\n" +
                    "(Erreur obtenue : pas d'Uri de l'image obtenue)");
            shareImage.setVisibility(View.GONE);
            headerSharePhoto.setVisibility(View.VISIBLE);
            return;
        }
        shareImage.setVisibility(View.VISIBLE);
        headerSharePhoto.setVisibility(View.GONE);
        LOG.d(TAG_METHOD + "getPath for imageUri : "+imageUri);
        imageLocalPath = getPath(imageUri);//(this.getBaseContext(), imageUri)

        try {
            ExifInterface exif = new ExifInterface(imageLocalPath);
            imageOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            LOG.d(TAG_METHOD + "imageOrientation for imageLocalPath is : "+imageOrientation);
            int degree = 0;
            switch (imageOrientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }
            //Test autre méthode pour positionner l'image
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
//            int height = size.y;
            Matrix mat = new Matrix();
            Bitmap bMap = BitmapFactory.decodeFile(imageLocalPath);
            int imgHeight = bMap.getHeight();
            int imgWidth = bMap.getWidth();
            if (degree==90 || degree==270){
                imgHeight = bMap.getWidth();
                imgWidth = bMap.getHeight();
            }
            LOG.d(TAG_METHOD+ " calcul du ratio : (long)"+imgWidth+"/(long)"+imgHeight+";");
            double ratio = (double)((double)imgWidth/(double)imgHeight);
            LOG.d(TAG_METHOD+ " calcul de la nouvelle largeur : = "+width);
            int newWidth = width;
            LOG.d(TAG_METHOD+ " calcul de la nouvelle hauteur : (float)("+newWidth+" / "+ratio+") = "+(float)(newWidth / ratio)+";");
            int newHeight = Integer.parseInt("" + (int)(newWidth / ratio)) ;
            mat.postRotate(degree);
            Bitmap bMapOrientated = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(), bMap.getHeight(), mat, true);

//            //1er test de conversion direct en bytes
//            int bytes = bMapOrientated.getByteCount();
//            ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
//            bMapOrientated.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
//            photoBytes = buffer.array(); //Get the underlying array containing the data

            //2e test de conversion direct en bytes
            FileOutputStream out = null;
            try {
                File sdcard = Environment.getExternalStorageDirectory();
                File storagePath = new File(sdcard.getAbsolutePath() + "/Android/data/fr.scaron.diaspdroid/temp");
                if (!storagePath.exists()){
                    storagePath.mkdirs();
                }
                File userimage = new File(storagePath + "/uploaded.jpg");
                out = new FileOutputStream(userimage);
                bMapOrientated.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                out.close();
                imageLocalPath = storagePath + "/uploaded.jpg";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Bitmap bMapRotate =Bitmap.createScaledBitmap(bMapOrientated, newWidth, newHeight, true);// Bitmap.createBitmap(bMap, 0, 0, newWidth, newHeight, mat, true);//Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(), bMap.getHeight(), mat, true);

            shareImage.setImageBitmap(bMapRotate);
        } catch (IOException e) {
            LOG.d(TAG_METHOD + "imageOrientation not found for imageLocalPath ("+e.getMessage()+")");
        }
        LOG.d(TAG_METHOD + "getPath got imageLocalPath : "+imageLocalPath);
        if (imageLocalPath == null){
            shareMessage.setText("Le partage de la photo a échouéé\n" +
                    "(Erreur obtenue : pas de chemin local de l'image obtenu)");
            shareImage.setVisibility(View.GONE);
            return;
        }
        LOG.d(TAG_METHOD + "getImageName for imageLocalPath : " + imageLocalPath);
        final String imageName = imageLocalPath.substring(imageLocalPath.lastIndexOf('/')+1);
        shareMessage.setText("Partage de la photo à faire : " + imageName + " (" + imageLocalPath + ")");
        LOG.d(TAG_METHOD + "Sortie");
    }


    @OptionsItem(R.id.action_share)
    public void launchSharing(){
        progressBar.setVisibility(View.VISIBLE);
        String TAG_METHOD = TAG + ".launchSharing : ";
        LOG.d(TAG_METHOD + "Entrée");

        LOG.d(TAG_METHOD + "imageUri:"+imageUri+" / postIDParent:"+postIDParent+" / activityParent:"+activityParent);
        if (imageUri == null){
            LOG.d(TAG_METHOD + "Il semble qu'il n'y ait pas de photo à partager\n" +
                            "(Erreur obtenue : pas d'Uri de l'image obtenue)");
            shareMessage.setText("Il semble qu'il n'y ait pas de photo à partager\n" +
                    "(Erreur obtenue : pas d'Uri de l'image obtenue)");
//            if (postIDParent!=null && postIDParent>0) {
                LOG.d(TAG_METHOD + "On va commenter un post");
                if (shareTextEntry.getText() != null && !shareTextEntry.getText().toString().isEmpty()) {
                    shareComment();
                    return;
                }
//            }
            LOG.d(TAG_METHOD + "Il semble qu'il n'y ait ni photo et ni commentaire à partager\n" +
                    "(Erreur obtenue : pas d'Uri de l'image obtenue)");
            shareMessage.setText("Il semble qu'il n'y ait ni photo et ni commentaire à partager\n" +
                    "(Erreur obtenue : pas d'Uri de l'image obtenue)");
            return;
        }
        if (imageLocalPath == null){
            LOG.d(TAG_METHOD + "Le partage de la photo a échouéé\n" +
                    "(Erreur obtenue : pas de chemin local de l'image obtenu)");
            shareMessage.setText("Le partage de la photo a échouéé\n" +
                    "(Erreur obtenue : pas de chemin local de l'image obtenu)");
            return;
        }
        LOG.d(TAG_METHOD + "getImageName for imageLocalPath : " + imageLocalPath);
        LOG.d(TAG_METHOD + "appel de shareImage");
        shareImage(imageLocalPath);
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Click(R.id.header_share_photo)
    public void getImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, ""), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null) {
            switch (requestCode) {
                case SELECT_PICTURE:
                    imageUri = data.getData();
                    updateScreen();
                    break;
                default:
                    break;
            }
        }
    }

    @Background
    public void shareComment(){
        publishProgress(25);
        String TAG_METHOD = TAG + ".getInfosUserForBar : ";
        LOG.d(TAG_METHOD + "Entrée");

        NewPost newPost = new NewPost();
        StatusMessage statusMessage=new StatusMessage();
        statusMessage.setText(shareTextEntry.getText().toString());
        newPost.setStatus_message(statusMessage);
        publishProgress(50);
        Post newPosted;
        String message="";
        if (postIDParent !=null && postIDParent!=0){
            message="mise en ligne de votre commentaire";
            newPosted = diasporaBean.comment(postIDParent, statusMessage.getText());
        }else {
            message="mise en ligne de votre nouvel conversation";
            newPosted = diasporaBean.sendPost(newPost);
        }
        LOG.d(TAG_METHOD + "newPosted is null ? " + (newPosted==null));
        showToastResult(newPost!=null, message);
        publishProgress(100);
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void showToastResult(boolean resultOK, String message){
        if (resultOK){
            Toast.makeText(this, message,Toast.LENGTH_LONG).show();
            retourMenuPrincipal();
        }else{
            Toast.makeText(this, "Erreur de "+message,Toast.LENGTH_LONG).show();
        }
    }

    @Background
    public void shareImage(String localPath){
        publishProgress(25);
        String TAG_METHOD = TAG + ".getInfosUserForBar : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "call diasporaBean.uploadFile");
        UploadResult uploadFileResult = diasporaBean.uploadFile(localPath);
        publishProgress(50);
//        UploadResult uploadFileResult = diasporaBean.uploadFile(photoBytes);
        checkSharePic(uploadFileResult);
        publishProgress(62);
        LOG.d(TAG_METHOD + "uploadedFilePath = " + uploadedFilePath);
        LOG.d(TAG_METHOD + "uploadedFileGuid = " + uploadedFileGuid);
        LOG.d(TAG_METHOD + "uploadedFileId = "+uploadedFileId);
        if (uploadFileResult!=null){
        LOG.d(TAG_METHOD + "photoID = "+
                uploadFileResult.getData().getPhoto().getId());
            NewPost newPost = new NewPost();
            StatusMessage statusMessage=new StatusMessage();
            statusMessage.setText(shareTextEntry.getText().toString());
            newPost.setStatus_message(statusMessage);
            newPost.setPhotos(""+uploadFileResult.getData().getPhoto().getId());
            publishProgress(75);
            Post newPosted = diasporaBean.sendPost(newPost);
            LOG.d(TAG_METHOD + "newPosted is null ? "+(newPosted==null));
            showToastResult(newPosted != null, "partage de votre photo");
        }
        publishProgress(100);
        showToastResult(false, "partage de votre photo");
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    void publishProgress(int progress) { // will run on the UI thread
        // Update progress views
        progressBar.setProgress(progress);
        if (progress==100){
            progressBar.setVisibility(View.GONE);
        }
    }

    @UiThread
    public void checkSharePic(UploadResult uploadResult){
        String TAG_METHOD = TAG + ".checkSharePic : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "response body ? : " + uploadResult);
        if (uploadResult==null){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DiasporaConfig.APPLICATION_CONTEXT);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setTitle("Echec de publication");
            alertDialog.setMessage("Votre publication a échouée\n" +
                    "(Erreur obtenue : pas de réponse du service)");
            alertDialog.show();
            shareMessage.setText("Le partage de la photo a échouéé\n" +
                    "(Erreur obtenue : pas de réponse du service)");
            return;
        }

        LOG.d(TAG_METHOD + "response body : " + uploadResult.toString());
        if (uploadResult.getError()!=null){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DiasporaConfig.APPLICATION_CONTEXT);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setTitle("Echec de publication");
            alertDialog.setMessage("Votre publication a échouée\n(Erreur obtenue : "+uploadResult.getError()+")");
            alertDialog.show();
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
            if (urlSharedImg!=null){
                uploadedFilePath = urlSharedImg;
                uploadedFileGuid = uploadResult.getData().getPhoto().getGuid();
                uploadedFileId = uploadResult.getData().getPhoto().getId();
                return;
            }else{
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DiasporaConfig.APPLICATION_CONTEXT);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setIcon(R.drawable.ic_launcher);
                alertDialog.setTitle("Echec de publication");
                alertDialog.setMessage("Votre publication a échouée");
                alertDialog.show();
                shareMessage.setText("Le partage de la photo a échouéé");
                return;
            }
        }
        shareMessage.setText("Le partage de la photo a échouéé");
    }

    public String getPath(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();
        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }
}
