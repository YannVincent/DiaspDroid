package fr.android.scaron.diaspdroid.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.AuthenticationInterceptor;
import fr.android.scaron.diaspdroid.controler.DiasporaControler;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.AndroidMultiPartEntity;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;

/**
 * Created by Sébastien on 25/03/2015.
 */
@EActivity(R.layout.activity_upload)
public class ShareActivity  extends ActionBarActivity {

    private static Logger LOGGEUR = LoggerFactory.getLogger(ShareActivity.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static final String TAG = "ShareActivity";


    @ViewById(R.id.progressBar)
    ProgressBar progressBar;
    String filePath = null;
    @ViewById(R.id.txtPercentage)
    TextView txtPercentage;
    @ViewById(R.id.imgPreview)
    ImageView imgPreview;
    @ViewById(R.id.videoPreview)
    VideoView vidPreview;
    @ViewById(R.id.btnUpload)
    Button btnUpload;
    @ViewById(R.id.upload_message)
    EditText shareMessage;
    long totalSize = 0;


//    @Extra
    Uri imageUri;
//
//    @Bean
//    DiasporaBean diasporaBean;
//
//
//    @ViewById(R.id.share_image)
//    ImageView shareImage;
//
//    @ViewById(R.id.share_text_button)
//    Button shareTextButton;
//
//    @ViewById(R.id.progress_horizontal)
//    ProgressBar progressBar;

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
        LOG.d(TAG_METHOD + "getPath for imageUri : "+imageUri);
        final String imageLocalPath = getPath(imageUri);
        LOG.d(TAG_METHOD + "getPath for imageLocalPath : "+imageLocalPath);
        if (imageLocalPath == null){
            shareMessage.setText("Le partage de la photo a échouéé\n" +
                    "(Erreur obtenue : pas de chemin local de l'image obtenu)");
            return;
        }
        filePath = imageLocalPath;
        boolean isImage = true;
        previewMedia(isImage);
        LOG.d(TAG_METHOD + "getImageName for imageLocalPath : "+imageLocalPath);
        final String imageName = imageLocalPath.substring(imageLocalPath.lastIndexOf('/')+1);
        shareMessage.setText("Partage de la photo à faire : " + imageName + " ("+imageLocalPath+")");
        LOG.d(TAG_METHOD + "Sortie");
    }


    @Click(R.id.btnUpload)
    public void launchSharing(){
        String TAG_METHOD = TAG + ".updateScreen : ";
        LOG.d(TAG_METHOD + "Entrée");

        if (imageUri == null){
            shareMessage.setText("Le partage de la photo a échouéé\n" +
                    "(Erreur obtenue : pas d'Uri de l'image obtenue)");
            return;
        }
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
        new UploadFileToServer().execute();
//        UploadResult uploadFileResult = diasporaBean.uploadFile(fileName, localPath);
//        checkSharePic(uploadFileResult);
    }

    /**
     * Displaying captured image/video on the screen
     * */
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            vidPreview.setVisibility(View.GONE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            imgPreview.setImageBitmap(bitmap);
        } else {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            vidPreview.setVideoPath(filePath);
            // start playing
            vidPreview.start();
        }
    }

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String TAG_METHOD = TAG + ".UploadFileToServer.uploadFile : ";
            LOG.d(TAG_METHOD + "Entrée");
            String responseString = null;
            LOG.d(TAG_METHOD + "Get fileName in filePath "+filePath);
            final String fileName = filePath.substring(filePath.lastIndexOf('/')+1);
            String fileNameUrlEncoded = fileName;
            try {
                fileNameUrlEncoded = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            LOG.d(TAG_METHOD + "FileName getted  "+fileName);
            HttpClient httpclient = new DefaultHttpClient();
            LOG.d(TAG_METHOD + "Create httpclient");
            HttpPost httppost = new HttpPost(DiasporaConfig.POD_URL+"/photos?photo[pending]=true&photo[aspect_id]=all&qqfile="+fileNameUrlEncoded);
            LOG.d(TAG_METHOD + "Create httppost for url "+DiasporaConfig.POD_URL+"/photos?photo[pending]=true&qqfile="+fileNameUrlEncoded);
//            @RequiresCookie({"_diaspora_session", "remember_user_token"})
//            @SetsCookie({"_diaspora_session", "remember_user_token"})
//            @RequiresHeader({"x-csrf-token", "x-requested-with", "x-file-name", "origin", "referer", "authenticity_token"})


            httppost.setHeader("content-type", "application/octet-stream");
            httppost.setHeader("accept", "application/json");
            httppost.setHeader("accept-encoding", "gzip, deflate");
            httppost.setHeader("origin", "https://framasphere.org");
            httppost.setHeader("referer", "https://framasphere.org/stream");

            LOG.d(TAG_METHOD + "Add header Cookie to httppost with value : " + AuthenticationInterceptor.COOKIE_AUTH);
//            httppost.setHeader("Cookie", "remember_user_token=BAhbB1sGaQISCUkiIiQyYSQxMCRhRkt5Zm1zNzQ5Mjc1UkpqL2NnMVYuBjoGRVQ%3D--f4d3bf8cffd10524e0bae308c1afeed9de766c26; _diaspora_session=BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJTA5MDc5MTBkOWIwNDFlMmViZDk0OTgzNzc0YmMyMjlhBjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJGFGS3lmbXM3NDkyNzVSSmovY2cxVi4GOwBUSSIQX2NzcmZfdG9rZW4GOwBGSSIxdnhhZWR6VzdoZjhPK3A2YnlkVUhuaHFhdUNpd3A4cVc3aE1PVVpKeWtpdz0GOwBG--ca1ab19823a5bb151719f96946728bf4124cc53d; _pk_id.26.270d=0045c88642cbe4c2.1427292262.2.1429879130.1429879105.; _pk_ses.26.270d=*");//AuthenticationInterceptor.COOKIE_AUTH);
            httppost.setHeader("Cookie", AuthenticationInterceptor.COOKIE_AUTH);//"remember_user_token="+DiasporaControler.COOKIE_REMEMBER + "; _diaspora_session="+ DiasporaControler.COOKIE_SESSION_STREAM);

            LOG.d(TAG_METHOD + "Add header x-csrf-token to httppost with value : " + DiasporaControler.TOKEN);
//            httppost.setHeader("x-csrf-token", "vxaedzW7hf8O+p6bydUHnhqauCiwp8qW7hMOUZJykiw=");
            httppost.setHeader("x-csrf-token", DiasporaControler.TOKEN);

            LOG.d(TAG_METHOD + "Add header x-requested-with to httppost with value : XMLHttpRequest");
            httppost.setHeader("x-requested-with", "XMLHttpRequest");

            LOG.d(TAG_METHOD + "Add header x-file-name to httppost with value : " + fileNameUrlEncoded);
            httppost.setHeader("x-file-name", fileNameUrlEncoded);

//            LOG.d(TAG_METHOD + "Add header authenticity_token to httppost with value : " + DiasporaControler.TOKEN);
//            httppost.setHeader("authenticity_token", DiasporaControler.TOKEN);
            try {
                LOG.d(TAG_METHOD + "Create AndroidMultiPartEntity");
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                LOG.d(TAG_METHOD + "Create sourceFile");
                File sourceFile = new File(filePath);

                // Adding file data to http body
                LOG.d(TAG_METHOD + "addPart image file");
                entity.addPart("file", new FileBody(sourceFile));
//                entity.addPart("qqFile", new StringBody(fileNameUrlEncoded));
//                entity.addPart("json", new StringBody("{ \"photo\": { \"pending\": true, \"aspect_id\": \"all\" }}"));
//                entity.addPart("image", new FileBody(sourceFile));

//                // Extra parameters if you want to pass to server
//                entity.addPart("website",
//                        new StringBody("www.androidhive.info"));
//                entity.addPart("email", new StringBody("abc@gmail.com"));

                LOG.d(TAG_METHOD + "get totalSize multipart");
                totalSize = entity.getContentLength();
                LOG.d(TAG_METHOD + "totalSize multipart getted "+totalSize);
                httppost.setEntity(entity);

                // Making server call
                LOG.d(TAG_METHOD + "execute request");
                HttpResponse response = httpclient.execute(httppost);

                LOG.d(TAG_METHOD + "getted response : "+response.toString());
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
                LOG.e(TAG_METHOD + "ClientProtocolException obtenue : "+e.toString());
            } catch (IOException e) {
                responseString = e.toString();
                LOG.e(TAG_METHOD + "IOException obtenue : " + e.toString());
            } catch (Throwable e) {
                responseString = e.toString();
                LOG.e(TAG_METHOD + "Throwable obtenue : "+e.toString());
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            final String TAG_METHOD = TAG+".UploadFileToServer.onPostExecute";
            LOG.d(TAG_METHOD + "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
