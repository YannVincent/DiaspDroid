package fr.android.scaron.diaspdroid.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.Headers;
import com.koushikdutta.ion.HeadersResponse;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaControler;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.UploadResult;

//@EActivity(R.layout.activity_share)
public class ShareActivity extends ActionBarActivity {

    private static Logger LOGGEUR = LoggerFactory.getLogger(ShareActivity.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            DiasporaConfig.init(this.getApplication(), this);
            // Get intent, action and MIME type
            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();
            restoreActionBar();
            if (Intent.ACTION_SEND.equals(action) && type != null) {
//                if ("text/plain".equals(type)) {
//                    handleSendText(intent); // Handle text being sent
//                } else
                if (type.startsWith("image/")) {
                    handleSendImage(intent); // Handle single image being sent

                    setContentView(R.layout.activity_share);
//                    LinearLayout shareLayout = (LinearLayout)findViewById(R.id.share_layout);
                    final EditText shareText = (EditText)findViewById(R.id.share_message);
                    final Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                    final String imageLocalPath = getPath(imageUri);
                    final ImageView imageShared = (ImageView)findViewById(R.id.share_image);
                    imageShared.setImageURI(imageUri);
                    final String imageName = imageLocalPath.substring(imageLocalPath.lastIndexOf('/')+1);
                    final ProgressBar uploadProgressBar = (ProgressBar)findViewById(R.id.progress_horizontal);
                    shareText.setText("Partage de la photo en cours: " + imageName + " ("+imageLocalPath+")");

                    //Callback de la récupération du flux

                    //Callback de la récupération du flux
                    final FutureCallback<Response<String>> shareCallbackString = new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> responseUploadResult) {
                            LOG.d(ShareActivity.class, "shareCallback, exception ? " + e);
                            String uploadResult = responseUploadResult.getResult();
                            if (e!=null && e.getCause()!=null) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShareActivity.this);
                                final AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.setIcon(R.drawable.ic_launcher);
                                alertDialog.setTitle("PB de partage");
                                alertDialog.setMessage("Votre publication a échouée");
                                alertDialog.show();
                                shareText.setText("Le partage de la photo a échouéé");
                                LOG.d(ShareActivity.class , "shareCallback, cause exception ? " + e.getCause().getMessage());
                                LOG.e("Callback flux, Erreur : " + e.getMessage());
                                if (e.getCause()!=null) {
                                    LOG.e("shareCallback, cause exception ? " + e.getCause().getMessage());
                                }
                            }
                            LOG.d(ShareActivity.class, "shareCallback, request body : " + responseUploadResult.getRequest().getBody());
                            if (uploadResult!=null){
                                LOG.d(ShareActivity.class, "shareCallback, response body : "+uploadResult.toString());
                            }
                            shareText.setText("Le partage de la photo a échouéé");
//                            ACRA.getErrorReporter().handleException(e);
                            uploadProgressBar.setVisibility(View.GONE);
                        }
                    };


                    final FutureCallback<Response<UploadResult>> shareCallback = new FutureCallback<Response<UploadResult>>() {
                        @Override
                        public void onCompleted(Exception e, Response<UploadResult> responseUploadResult) {
                            LOG.d(ShareActivity.class, "shareCallback, exception ? " + e);
                            UploadResult uploadResult = responseUploadResult.getResult();
                            if (e!=null) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShareActivity.this);
                                final AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.setIcon(R.drawable.ic_launcher);
                                alertDialog.setTitle("PB de partage");
                                alertDialog.setMessage("Votre publication a échouée\n("+ e.getMessage()+")");
                                alertDialog.show();
                                shareText.setText("Le partage de la photo a échouéé");
                                LOG.e("Callback flux, Erreur : " + e.getMessage());
                                if (e.getCause()!=null){
                                    LOG.e("shareCallback, cause exception ? " + e.getCause().getMessage());
                                }
                            }
                            LOG.d(ShareActivity.class, "shareCallback, request body : " + responseUploadResult.getRequest().getBody());

                            HeadersResponse responseHeaders = responseUploadResult.getHeaders();
                            LOG.d(ShareActivity.class, "shareCallback, response headers ? : " + responseHeaders);
                            if (responseHeaders!=null){
                                int headerCode = responseHeaders.code();
                                String headerMessage = responseHeaders.message();
                                LOG.d(ShareActivity.class, "shareCallback, response headers code : " + headerCode);
                                LOG.d(ShareActivity.class, "shareCallback, response headers message : " + headerMessage);

                                Headers headers = responseHeaders.getHeaders();
                                LOG.d(ShareActivity.class, "shareCallback, headers ? : " + headers);
                                if (headers!=null) {
                                    LOG.d(ShareActivity.class, "shareCallback, headers values : " +headers.toHeaderArray());
                                }
                            }

                            LOG.d(ShareActivity.class, "shareCallback, response body ? : "+uploadResult);
                            if (uploadResult!=null){
                                LOG.d(ShareActivity.class, "shareCallback, response body : "+uploadResult.toString());
                                if (uploadResult.getError()!=null){
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShareActivity.this);
                                    final AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.setIcon(R.drawable.ic_launcher);
                                    alertDialog.setTitle("Echec de publication");
                                    alertDialog.setMessage("Votre publication a échouée\n(Erreur obtenue : "+uploadResult.getError()+")");
                                    alertDialog.show();
                                    uploadProgressBar.setVisibility(View.GONE);
                                    shareText.setText("Le partage de la photo a échouéé");
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
                                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShareActivity.this);
                                        final AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.setIcon(R.drawable.ic_launcher);
                                        alertDialog.setTitle("Publication réussie");
                                        alertDialog.setMessage("Votre publication a bien été effectuée\n--------------\nimage:"+urlSharedImg+"\n-------\nteste:"+shareText.getText().toString());
                                        alertDialog.show();
                                        LOG.d(ShareActivity.class , "shareCallback, publication réussie");
                                        uploadProgressBar.setVisibility(View.GONE);
                                        shareText.setText("Le partage de la photo a réussie");
                                        return;
                                    }else{
                                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShareActivity.this);
                                        final AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.setIcon(R.drawable.ic_launcher);
                                        alertDialog.setTitle("Publication réussie");
                                        alertDialog.setMessage("Votre publication a échouée");
                                        alertDialog.show();
                                        uploadProgressBar.setVisibility(View.GONE);
                                        shareText.setText("Le partage de la photo a échouéé");
                                        return;
                                    }
                                }
                            }
                            shareText.setText("Le partage de la photo a échouéé");
//                            ACRA.getErrorReporter().handleException(e);
                            uploadProgressBar.setVisibility(View.GONE);
                        }
                    };

                    Button shareButton = (Button) findViewById(R.id.share_text_button);
                    View.OnClickListener myhandler = new View.OnClickListener() {
                        public void onClick(View v) {
                            uploadProgressBar.setVisibility(View.VISIBLE);

                            // Execute some code after 2 seconds have passed
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                public void run() {
                                    shareText.setText("Partage en cours de la photo : " + imageName + " (" + imageLocalPath + ")-----------\nAvec le message : " + shareText.getText());
                                    DiasporaControler.partagerImage(imageLocalPath, imageName, shareText.getText().toString(), uploadProgressBar, shareCallback);
//                                    DiasporaControler.partagerImage(ShareActivity.this, imageLocalPath, imageName, uploadProgressBar, shareCallbackString);
//                                }
//                            }, 2000);
                        }
                    };
                    shareButton.setOnClickListener(myhandler);
                }
//            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
//                if (type.startsWith("image/")) {
//                    handleSendMultipleImages(intent); // Handle multiple images being sent
//                }
            }
        }catch(Throwable thr){
            LOG.e(this.getClass(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
                throw thr;
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
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

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            Toast.makeText(this, "Image : "+imageUri.getPath(), Toast.LENGTH_LONG);
        }
    }

    public void restoreActionBar() {
        try{
            ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle("Partagez avec Diaspora");
        }catch(Throwable thr){
            LOG.e(this.getClass(),"Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }catch(Throwable thr){
            LOG.e(this.getClass(),"Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

}
