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
import org.coding4coffee.diaspora.api.DiasporaClient;
import org.coding4coffee.diaspora.api.DiasporaClientFactory;
import org.coding4coffee.diaspora.api.exceptions.PodFailureException;
import org.coding4coffee.diaspora.api.upload.ProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;

//import com.google.api.client.util.IOUtils;

/**
 * Created by Sébastien on 25/03/2015.
 */
@EActivity(R.layout.activity_upload)
public class ShareActivityOldSchool extends ActionBarActivity {

    private static Logger LOGGEUR = LoggerFactory.getLogger(ShareActivityOldSchool.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static final String TAG = "ShareActivityOldSchool";


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
            return "" + uploadFile();
        }

        public String uploadFile(){
            String TAG_METHOD = TAG + ".UploadFileToServer.uploadFile : ";
            LOG.d(TAG_METHOD + "Entrée");
            String reponseUpload = "Une erreur inconnue est survenue";
            DiasporaClient diasporaClient = DiasporaClientFactory.createDiasporaClient(DiasporaConfig.POD_URL);
            try {
                boolean logged = diasporaClient.login(DiasporaConfig.POD_USER, DiasporaConfig.POD_PASSWORD);
                if (logged){
                    final ProgressListener listener = new ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    };
                    final byte[] photoBytes = getImageBytes(filePath);
                    String photoUrl = diasporaClient.uploadPhoto(photoBytes, listener);
                    LOG.d(TAG_METHOD + "url de la photo sur le POD : "+photoUrl);
                    reponseUpload = photoUrl;
                }else{
                    reponseUpload="Impossible de se connecter à votre POD Diaspora, veuillez vérifier votre connexion ou vos paramètres";
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOG.e(TAG_METHOD + "IOException rencontrée "+e.getMessage(),e);
                reponseUpload="Erreur IOException "+e.getMessage();
            } catch (PodFailureException e) {
                e.printStackTrace();
                LOG.e(TAG_METHOD + "PodFailureException rencontrée "+e.getMessage(),e);
                reponseUpload="Erreur PodFailureException "+e.getMessage();
            }
            return reponseUpload;
        }

//        public String uploadFile(){
//
//            String TAG_METHOD = TAG + ".UploadFileToServer.uploadFile : ";
//            LOG.d(TAG_METHOD + "Entrée");
//            final HttpParams httpParams = new BasicHttpParams();
//            HttpClientParams.setRedirecting(httpParams, false);
//            HttpClient session = new DefaultHttpClient(httpParams);
//            final byte[] photoBytes = getImageBytes(filePath);
//            final ProgressListener listener;
//
//            final HttpPost photoRequest = new HttpPost(DiasporaConfig.POD_URL + "/photos?photo%5Baspect_ids%5D=all&qqfile=uploaded.jpg");
//
//            try {
//                // add header
//                photoRequest.addHeader("content-type", "application/octet-stream");
//                photoRequest.addHeader("accept", "application/json");
//                photoRequest.addHeader("X-Requested-With", "XMLHttpRequest");
//                photoRequest.addHeader("X-CSRF-Token", DiasporaControler.TOKEN);
//                photoRequest.addHeader("Cookie", AuthenticationInterceptor.COOKIE_AUTH);
//                listener = new ProgressListener() {
//                    @Override
//                    public void transferred(long num) {
//                        publishProgress((int) ((num / (float) totalSize) * 100));
//                    }
//                };
//                final HttpEntity photoEntity = new ProgressByteArrayEntity(photoBytes, listener);
//                totalSize = photoEntity.getContentLength();
//                photoRequest.setEntity(photoEntity);
//
//                // send request
//                final HttpResponse response = session.execute(photoRequest);
//
//                StringBuilder sb = new StringBuilder("\n-----------------\n");
//                for (Header header:photoRequest.getAllHeaders()){
//                    sb.append(header.toString()+"\n");
//                }
//                sb.append("-----------------");
//                LOG.d(TAG_METHOD + "headers sended :"+sb.toString());
//
//
//                if (response.getStatusLine().getStatusCode() == 200) { // successful
//                    // get guid
//                    final JSONObject photoJson = new JSONObject(EntityUtils.toString(response.getEntity()));
//                    final JSONObject photoData = photoJson.getJSONObject("data").getJSONObject("photo");
//                    LOG.d(TAG_METHOD + photoData.getJSONObject("unprocessed_image").getString("url"));
//                    LOG.d(TAG_METHOD + photoData.getString("guid"));
//                    return photoData.getJSONObject("unprocessed_image").getString("url");
//                }
//                // ignore content if not successful
//                // response.getEntity().consumeContent();
//                LOG.d(EntityUtils.toString(response.getEntity()));
//                return "Error "+response.getStatusLine().getStatusCode()+" while creating the post! Probably the diaspora behavior has changed. (reason : "+response.getStatusLine().getReasonPhrase()+")";
//            } catch (final IOException e) {
//                // reset http connection
//                photoRequest.abort();
//                return "IOException : "+e.getMessage();
//            } catch (final ParseException e) {
//                // reset http connection
//                photoRequest.abort();
//                return "ParseException : "+e.getMessage();
//            } catch (final JSONException e) {
//                // reset http connection
//                photoRequest.abort();
//                return "JSONException : "+e.getMessage();
//            }
//        }



//    private String uploadFile(){
//            String TAG_METHOD = TAG + ".UploadFileToServer.uploadFile : ";
//            LOG.d(TAG_METHOD + "Entrée");
//        //File to download
//
////            final String uploadFileName =
////
////            final String uploadFilePath = filePath.substring(0, indexEndOfPath);
//            int indexEndOfPath = filePath.lastIndexOf('/');
//        String fileName = filePath.substring(indexEndOfPath+1);
//        String fileDir = filePath.substring(0, indexEndOfPath);
//        Uri mSelectedImageUri = imageUri;
//        String responseText;
//        try {
//            HttpClient httpClient=new DefaultHttpClient();
//            HttpPost httpPost=new HttpPost("https://framasphere.org/photos");
//
//
//
//
//            //--------------------------------------
//
//    //            httppost.setHeader("accept", "application/json");
//    //            httppost.setHeader("accept-encoding", "gzip, deflate");
//    //            httppost.setHeader("accept-language", "fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4");
//    //
//    ////            httppost.setHeader("content-type", "application/x-www-form-urlencoded");
//    ////            httppost.setHeader("content-type", "application/octet-stream");
//    ////            httppost.setHeader("content-type", "multipart/form-data");
//    //            httppost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36");
//    //
//
//            httpPost.setHeader("Connection", "Keep-Alive");
//            httpPost.setHeader("cookie", "remember_user_token=BAhbB1sGaQISCUkiIiQyYSQxMCRhRkt5Zm1zNzQ5Mjc1UkpqL2NnMVYuBjoGRVQ%3D--f4d3bf8cffd10524e0bae308c1afeed9de766c26; _diaspora_session=BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJWUzYTYyODMyOGNmYTQ2MTJiODZhNGQ1ZDUzMGYyMzUyBjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJGFGS3lmbXM3NDkyNzVSSmovY2cxVi4GOwBUSSIQX2NzcmZfdG9rZW4GOwBGSSIxV0NzSXBKYjlhSFhLcjdoN1N6VVkwN1pmZEFieWNyckFTUXJDblhuQTJlTT0GOwBG--8b4e7448a125c0933d98340ae4aa0d67ac662f94; _pk_ref.26.270d=%5B%22%22%2C%22%22%2C1429998251%2C%22https%3A%2F%2Fwww.google.fr%2F%22%5D; _pk_id.26.270d=da3742c7b86218bb.1425942309.13.1429998963.1429998251.; _pk_ses.26.270d=*");
////            httpPost.setHeader("content-type", "application/octet-stream");
//            httpPost.setHeader("accept", "application/json, text/plain, */*");
//            httpPost.setHeader("X-CSRF-Token", "WCsIpJb9aHXKr7h7SzUY07ZfdAbycrrASQrCnXnA2eM=");
//            httpPost.setHeader("X-File-Name", fileName);
//            httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
////            httpPost.setHeader("accept-encoding", "gzip, deflate");
//            //--------------------------------
//            AndroidMultiPartEntity mUploadEntity = new AndroidMultiPartEntity(
//                new AndroidMultiPartEntity.ProgressListener() {
//
//                    @Override
//                    public void transferred(long num) {
//                        publishProgress((int) ((num / (float) totalSize) * 100));
//                    }
//                });
//            if (mSelectedImageUri != null) {
//                InputStream imageStream=getContentResolver().openInputStream(mSelectedImageUri);
//                byte[] imageData= IOUtils.toByteArray(imageStream);
//                InputStreamBody imageStreamBody=new InputStreamBody(new ByteArrayInputStream(imageData),fileName);
//                mUploadEntity.addPart("qqFile",imageStreamBody);
//                mUploadEntity.addPart("json", new StringBody("{ \"photo\": { \"pending\": true, \"aspect_id\": \"all\" }}"));
//            }
//
//            LOG.d(TAG_METHOD + "get totalSize multipart");
//                totalSize = mUploadEntity.getContentLength();
//                LOG.d(TAG_METHOD + "totalSize multipart getted "+totalSize);
//            httpPost.setEntity(mUploadEntity);
//            HttpResponse response;
//            response=httpClient.execute(httpPost);
//            InputStream instream = response.getEntity().getContent();
//            Header contentEncoding = response.getFirstHeader("Content-Encoding");
//            if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
//                instream = new GZIPInputStream(instream);
//            }
//            responseText = convertStreamToString(instream);
//            LOG.d(TAG_METHOD + "reponse obtenue : "+responseText);
//            JSONObject responseObject=new JSONObject(responseText);
//            LOG.d(TAG_METHOD + "reponse json obtenue : "+responseObject.toString());
//        }
//        catch (  FileNotFoundException fileException) {
//            fileException.printStackTrace();
//            responseText = "Erreur FileNotFoundException obtenue : "+fileException.getMessage();
//        }
//        catch (  IOException ioException) {
//            ioException.printStackTrace();
//            responseText = "Erreur IOException obtenue : "+ioException.getMessage();
//        }
//        catch (  JSONException jsonException) {
//            jsonException.printStackTrace();
//            responseText = "Erreur JSONException obtenue : "+jsonException.getMessage();
//        }
//        return responseText;
//    }

        public byte[] decompress(byte[] compressed) throws IOException
        {
            GZIPInputStream gzipInputStream;
            if (compressed.length > 4)
            {
                gzipInputStream = new GZIPInputStream(
                        new ByteArrayInputStream(compressed, 4,
                                compressed.length - 4));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                for (int value = 0; value != -1;)
                {
                    value = gzipInputStream.read();
                    if (value != -1)
                    {
                        baos.write(value);
                    }
                }
                gzipInputStream.close();
                baos.close();

                return baos.toByteArray();
            }
            else
            {
                return null;
            }
        }


        private byte[] getImageBytes(String filePath){
            try {
                File sourceFile = new File(filePath);
                InputStream is = new FileInputStream(sourceFile);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
//                while ((int bytesRead = is.read(b))!=-1){
//                    bos.write(b, 0, bytesRead);
//                }
                while(is.available()>0){
                    bos.write(is.read());
                }
                byte[] bytes = bos.toByteArray();
                return bytes;
            }catch(FileNotFoundException fnfe){
                return ("FileNotFoundException : "+fnfe.getMessage()).getBytes(Charset.forName("UTF-8"));
            }catch(IOException ioe){
                return ("IOException : "+ioe.getMessage()).getBytes(Charset.forName("UTF-8"));
            }
        }

//        public String uploadFile() {
//            String TAG_METHOD = TAG + ".UploadFileToServer.uploadFile : ";
//            LOG.d(TAG_METHOD + "Entrée");
//            String responseString = null;
//            int serverResponseCode = 0;
//            LOG.d(TAG_METHOD + "Get fileName in filePath "+filePath);
//            int indexEndOfPath = filePath.lastIndexOf('/');
//            final String uploadFileName = filePath.substring(indexEndOfPath+1);
//
//            final String uploadFilePath = filePath.substring(0, indexEndOfPath);
//            final String sourceFileUri = filePath;
//            String fileNameUrlEncoded = uploadFileName;
//            String serverResponseMessage = "";
//            try {
//                fileNameUrlEncoded = URLEncoder.encode(uploadFileName, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            final String upLoadServerUri = "https://framasphere.org/photos?photo%5Bpending%5D=true&qqfile="+fileNameUrlEncoded;
//
//            LOG.d(TAG_METHOD + "FileName getted  "+fileNameUrlEncoded);
//
//            String fileName = sourceFileUri;
//
//            HttpURLConnection conn = null;
//            DataOutputStream dos = null;
//            String lineEnd = "\r\n";
//            String twoHyphens = "--";
//            String boundary = "*****";
//            int bytesRead, bytesAvailable, bufferSize;
//            byte[] buffer;
//            int maxBufferSize = 1 * 1024 * 1024;
//            File sourceFile = new File(sourceFileUri);
//
//            if (!sourceFile.isFile()) {
//                return "Le fichier n'existe pas ! ("+uploadFilePath + "" + uploadFileName;
//            }
//            try {
//
//                // open a URL connection to the Servlet
//                FileInputStream fileInputStream = new FileInputStream(sourceFile);
//                URL url = new URL(upLoadServerUri);
//
//                // Open a HTTP  connection to  the URL
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setDoInput(true); // Allow Inputs
//                conn.setDoOutput(true); // Allow Outputs
//                conn.setUseCaches(false); // Don't use a Cached Copy
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Connection", "Keep-Alive");
//                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                conn.setRequestProperty("accept", "application/json, text/plain, */*");
//                conn.setRequestProperty("Cookie", "remember_user_token=BAhbB1sGaQISCUkiIiQyYSQxMCRhRkt5Zm1zNzQ5Mjc1UkpqL2NnMVYuBjoGRVQ%3D--f4d3bf8cffd10524e0bae308c1afeed9de766c26; _diaspora_session=BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJWUzYTYyODMyOGNmYTQ2MTJiODZhNGQ1ZDUzMGYyMzUyBjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJGFGS3lmbXM3NDkyNzVSSmovY2cxVi4GOwBUSSIQX2NzcmZfdG9rZW4GOwBGSSIxV0NzSXBKYjlhSFhLcjdoN1N6VVkwN1pmZEFieWNyckFTUXJDblhuQTJlTT0GOwBG--8b4e7448a125c0933d98340ae4aa0d67ac662f94; _pk_ref.26.270d=%5B%22%22%2C%22%22%2C1429998251%2C%22https%3A%2F%2Fwww.google.fr%2F%22%5D; _pk_id.26.270d=da3742c7b86218bb.1425942309.13.1429998963.1429998251.; _pk_ses.26.270d=*");
//                conn.setRequestProperty("X-CSRF-Token", "WCsIpJb9aHXKr7h7SzUY07ZfdAbycrrASQrCnXnA2eM=");
//                conn.setRequestProperty("X-File-Name", fileNameUrlEncoded);
//                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
////                conn.setRequestProperty("qqFile", fileNameUrlEncoded);
//
//                dos = new DataOutputStream(conn.getOutputStream());
//
//                dos.writeBytes(twoHyphens + boundary + lineEnd);
//                dos.writeBytes("Content-Disposition: form-data; name=\"qqFile\";filename=\""
//                                + fileNameUrlEncoded + "\"" + lineEnd);
//                dos.writeBytes(lineEnd);
//
//                // create a buffer of  maximum size
//                bytesAvailable = fileInputStream.available();
//
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                buffer = new byte[bufferSize];
//
//                // read file and write it into form...
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                while (bytesRead > 0) {
//
//                    dos.write(buffer, 0, bufferSize);
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                }
//
//                // send multipart form data necesssary after file data...
//                dos.writeBytes(lineEnd);
//                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//                // Responses from the server (code and message)
//                serverResponseCode = conn.getResponseCode();
//                serverResponseMessage = conn.getResponseMessage();
//
//                LOG.i("uploadFile HTTP Response is : "
//                        + serverResponseMessage + ": " + serverResponseCode);
//
////                if(serverResponseCode == 200){
//                    LOG.d("File "+uploadFileName+" Upload Completed ? "+(serverResponseCode==200));
////                }
//                String responseMessage = "";
//                if (serverResponseCode == 200) {
//                    responseMessage = convertStreamToString(conn.getInputStream());
//                }else{
//                    responseMessage =  convertStreamToString(conn.getErrorStream());
//                }
//
//                //close the streams //
//                fileInputStream.close();
//                dos.flush();
//                dos.close();
//                return responseMessage;
//
//            } catch (MalformedURLException ex) {
//                ex.printStackTrace();
//                LOG.e("Upload file to server error: " + ex.getMessage(), ex);
//                serverResponseMessage = ex.getMessage();
//            } catch (Exception e) {
//                e.printStackTrace();
//                LOG.e("Upload file to server Exception : " + e.getMessage(), e);
//                serverResponseMessage = e.getMessage();
//            }
//            return "Error code "+serverResponseCode+" : "+serverResponseMessage;
//        }


        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }



//        @SuppressWarnings("deprecation")
//        private String uploadFile() {
//            String TAG_METHOD = TAG + ".UploadFileToServer.uploadFile : ";
//            LOG.d(TAG_METHOD + "Entrée");
//            String responseString = null;
//            LOG.d(TAG_METHOD + "Get fileName in filePath "+filePath);
//            final String fileName = filePath.substring(filePath.lastIndexOf('/')+1);
//            String fileNameUrlEncoded = fileName;
//            try {
//                fileNameUrlEncoded = URLEncoder.encode(fileName, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//            LOG.d(TAG_METHOD + "FileName getted  "+fileName);
//            HttpClient httpclient = new DefaultHttpClient();
//            LOG.d(TAG_METHOD + "Create httpclient");
//            HttpPost httppost = new HttpPost("https://framasphere.org/photos?photo%5Bpending%5D=true&qqfile="+fileNameUrlEncoded);//&qqfile=uploaded.jpg");
////            HttpPost httppost = new HttpPost(DiasporaConfig.POD_URL+"/photos?photo%5Bpending%5D=truephoto%5Baspect_ids%5D=all");//&qqfile=uploaded.jpg");
////            HttpPost httppost = new HttpPost(DiasporaConfig.POD_URL+"/photos?photo[pending]=true&photo[aspect_id]=all&qqfile="+fileNameUrlEncoded);
//            LOG.d(TAG_METHOD + "Create httppost for url "+httppost.getURI());//DiasporaConfig.POD_URL+"/photos?photo[pending]=true&qqfile="+fileNameUrlEncoded);
//
//
//            httppost.setHeader("Connection", "Keep-Alive");
//            httppost.setHeader("accept", "application/json");
//            httppost.setHeader("accept-encoding", "gzip, deflate");
//            httppost.setHeader("accept-language", "fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4");
//
////            httppost.setHeader("content-type", "application/x-www-form-urlencoded");
////            httppost.setHeader("content-type", "application/octet-stream");
////            httppost.setHeader("content-type", "multipart/form-data");
//            httppost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36");
//
//            httppost.setHeader("cookie", "remember_user_token=BAhbB1sGaQISCUkiIiQyYSQxMCRhRkt5Zm1zNzQ5Mjc1UkpqL2NnMVYuBjoGRVQ%3D--f4d3bf8cffd10524e0bae308c1afeed9de766c26; _diaspora_session=BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJWUzYTYyODMyOGNmYTQ2MTJiODZhNGQ1ZDUzMGYyMzUyBjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJGFGS3lmbXM3NDkyNzVSSmovY2cxVi4GOwBUSSIQX2NzcmZfdG9rZW4GOwBGSSIxV0NzSXBKYjlhSFhLcjdoN1N6VVkwN1pmZEFieWNyckFTUXJDblhuQTJlTT0GOwBG--8b4e7448a125c0933d98340ae4aa0d67ac662f94; _pk_ref.26.270d=%5B%22%22%2C%22%22%2C1429998251%2C%22https%3A%2F%2Fwww.google.fr%2F%22%5D; _pk_id.26.270d=da3742c7b86218bb.1425942309.13.1429998963.1429998251.; _pk_ses.26.270d=*");
//
//            httppost.setHeader("X-CSRF-Token", "WCsIpJb9aHXKr7h7SzUY07ZfdAbycrrASQrCnXnA2eM=");
//            httppost.setHeader("X-File-Name", fileNameUrlEncoded);
//            httppost.setHeader("X-Requested-With", "XMLHttpRequest");
//
//
//
//
//
//
//
//
//
////            httppost.setHeader("content-type", "application/octet-stream");
//////            httppost.setHeader("accept", "application/json");
//////            httppost.setHeader("accept-encoding", "gzip, deflate");
//////            httppost.setHeader("origin", "https://framasphere.org");
//////            httppost.setHeader("referer", "https://framasphere.org/stream");
////
////            LOG.d(TAG_METHOD + "Add header Cookie to httppost with value : " + AuthenticationInterceptor.COOKIE_AUTH);
////            httppost.setHeader("Cookie", "remember_user_token=BAhbB1sGaQISCUkiIiQyYSQxMCRhRkt5Zm1zNzQ5Mjc1UkpqL2NnMVYuBjoGRVQ%3D--f4d3bf8cffd10524e0bae308c1afeed9de766c26; path=/; expires=Sat, 09-May-2015 21:52:01 GMT; HttpOnly; secure, _diaspora_session=BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJTMzZWZiYTM2NTZhYzEyZTliZTFmNzY4MzJjY2ViOWIwBjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJGFGS3lmbXM3NDkyNzVSSmovY2cxVi4GOwBUSSIKZmxhc2gGOwBUbzolQWN0aW9uRGlzcGF0Y2g6OkZsYXNoOjpGbGFzaEhhc2gJOgpAdXNlZG86CFNldAY6CkBoYXNoewA6DEBjbG9zZWRGOg1AZmxhc2hlc3sGOgtub3RpY2VJIihWb3VzIMOqdGVzIMOgIHByw6lzZW50IGNvbm5lY3TDqS1lLgY7AFQ6CUBub3cw--8a4fb6a36138dc110abde04b5e29b3761dd116e7; path=/; secure");
//////            httppost.setHeader("Cookie", AuthenticationInterceptor.COOKIE_AUTH);//"remember_user_token="+DiasporaControler.COOKIE_REMEMBER + "; _diaspora_session="+ DiasporaControler.COOKIE_SESSION_STREAM);
////
////            LOG.d(TAG_METHOD + "Add header x-csrf-token to httppost with value : " + DiasporaControler.TOKEN);
//////            httppost.setHeader("x-csrf-token", "M6clGALpvaV/g3VnWTxwW27E+hQSsYAFyu6MhPANmbw=");
//////            httppost.setHeader("x-csrf-token", DiasporaControler.TOKEN);
////
//////            LOG.d(TAG_METHOD + "Add header x-requested-with to httppost with value : XMLHttpRequest");
//////            httppost.setHeader("x-requested-with", "XMLHttpRequest");
////
//////            LOG.d(TAG_METHOD + "Add header x-file-name to httppost with value : " + fileNameUrlEncoded);
//////            httppost.setHeader("x-file-name", fileNameUrlEncoded);
////
//////            LOG.d(TAG_METHOD + "Add header authenticity_token to httppost with value : " + DiasporaControler.TOKEN);
//////            httppost.setHeader("authenticity_token", DiasporaControler.TOKEN);
//            try {
//                LOG.d(TAG_METHOD + "Create AndroidMultiPartEntity");
//                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
//                        new AndroidMultiPartEntity.ProgressListener() {
//
//                            @Override
//                            public void transferred(long num) {
//                                publishProgress((int) ((num / (float) totalSize) * 100));
//                            }
//                        });
//
//                LOG.d(TAG_METHOD + "Create sourceFile");
//                File sourceFile = new File(filePath);
//
//                // Adding file data to http body
//                LOG.d(TAG_METHOD + "addPart image file");
//                entity.addPart("qqFile", new FileBody(sourceFile));
////                entity.addPart("qqFile", new StringBody(fileNameUrlEncoded));
////                entity.addPart("json", new StringBody("{ \"photo\": { \"pending\": true, \"aspect_id\": \"all\" }}"));
////                entity.addPart("image", new FileBody(sourceFile));
//
////                // Extra parameters if you want to pass to server
////                entity.addPart("website",
////                        new StringBody("www.androidhive.info"));
////                entity.addPart("email", new StringBody("abc@gmail.com"));
//
//                LOG.d(TAG_METHOD + "get totalSize multipart");
//                totalSize = entity.getContentLength();
//                LOG.d(TAG_METHOD + "totalSize multipart getted "+totalSize);
//                httppost.setEntity(entity);
//
//                // Making server call
//                LOG.d(TAG_METHOD + "execute request");
//                HttpResponse response = httpclient.execute(httppost);
//                StringBuilder sb = new StringBuilder("\n-----------------\n");
//                for (Header header:httppost.getAllHeaders()){
//                    sb.append(header.toString()+"\n");
//                }
//                sb.append("-----------------");
//                LOG.d(TAG_METHOD + "headers sended :"+sb.toString());
//
//                LOG.d(TAG_METHOD + "getted response : "+response.toString());
//                HttpEntity r_entity = response.getEntity();
//
//                int statusCode = response.getStatusLine().getStatusCode();
//                if (statusCode == 200) {
//                    // Server response
//                    responseString = EntityUtils.toString(r_entity);
//                } else {
//                    responseString = "Error occurred! Http Status Code: "
//                    EntityUtils.
//                            + statusCode + "\n"+ (response.toString()+"\n"+EntityUtils.toString(r_entity));
//                }
//
//            } catch (ClientProtocolException e) {
//                responseString = e.toString();
//                LOG.e(TAG_METHOD + "ClientProtocolException obtenue : "+e.toString());
//            } catch (IOException e) {
//                responseString = e.toString();
//                LOG.e(TAG_METHOD + "IOException obtenue : " + e.toString());
//            } catch (Throwable e) {
//                responseString = e.toString();
//                LOG.e(TAG_METHOD + "Throwable obtenue : "+e.toString());
//            }
//
//            return responseString;
//
//        }

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
