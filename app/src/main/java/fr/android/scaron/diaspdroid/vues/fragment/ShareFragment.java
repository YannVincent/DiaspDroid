package fr.android.scaron.diaspdroid.vues.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.activity.MainActivity;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.NewPost;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.model.StatusMessage;
import fr.android.scaron.diaspdroid.model.UploadResult;
import fr.android.scaron.diaspdroid.vues.adapter.PhotosAdapter;

/**
 * Created by Sébastien on 23/05/2015.
 */
@EFragment(R.layout.fragment_sharenextgen)
@OptionsMenu(R.menu.share)
public class ShareFragment extends Fragment {

    private static Logger LOGGEUR = LoggerFactory.getLogger(ShareFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = ShareFragment.class.getSimpleName();
    final static int SELECT_PICTURE = 1;
    final static String FAKE_ADDPHOTO = "FAKE_ADDPHOTO";

    @Bean
    DiasporaBean diasporaService;

    @Bean
    PhotosAdapter adapter;
    List<String> photosUrl=new ArrayList<String>();

    // POST REFERENCE
    @ViewById(R.id.sharenextgen_post)
    LinearLayout sharenextgen_post;
    @ViewById(R.id.sharenextgen_post_avatar)
    ImageView sharenextgen_post_avatar;
    @ViewById(R.id.sharenextgen_post_user)
    TextView sharenextgen_post_user;
    @ViewById(R.id.sharenextgen_post_datetime)
    TextView sharenextgen_post_datetime;
    @ViewById(R.id.sharenextgen_post_text)
    TextView sharenextgen_post_text;

    // SHARE ENTRIES
    @ViewById(R.id.sharenextgen_avatar)
    ImageView sharenextgen_avatar;
    @ViewById(R.id.sharenextgen_text)
    TextView sharenextgen_text;
//    @ViewById(R.id.sharenextgen_tablephoto)
//    TableLayout sharenextgen_tablephoto;
    @ViewById(R.id.sharenextgen_gridphotos)
    GridView sharenextgen_gridphotos;

    ActionBarActivity activity;


    Post postRef=null;

    public void setActivityParent(ActionBarActivity activity){
        this.activity = activity;
    }

    @UiThread
    public void showToastResult(boolean resultOK, String message){
        if (resultOK){
            Toast.makeText(activity, message,Toast.LENGTH_LONG).show();
            ((MainActivity) DiasporaConfig.APPLICATION_CONTEXT).listItemClicked("Flux");
        }else{
            Toast.makeText(activity, "Erreur de "+message,Toast.LENGTH_LONG).show();
        }
    }


    @UiThread
    public void showToastInfo(boolean resultOK, String message){
        if (resultOK){
            Toast.makeText(activity, message,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(activity, "Erreur de "+message,Toast.LENGTH_LONG).show();
        }
    }

    @OptionsItem(R.id.action_share)
    public void launchSharing(){
        String TAG_METHOD = TAG + ".launchSharing : ";
        LOG.d(TAG_METHOD + "Entrée");
        CharSequence textToShare = sharenextgen_text.getText();
        if (textToShare==null||textToShare.length()==0){
            showToastResult(false,"partage ! Le texte est vide");
            return;
        }
        LOG.d(TAG_METHOD + "photosUrl:" + photosUrl + " / postRef:" + postRef);
        if (postRef!=null && postRef.getId()>0){
            //On écrit un commentaire.
            commentPost(textToShare.toString());
        }else if(photosUrl!=null && photosUrl.size()>1){
            sendImagesAndComment(textToShare.toString());
        }else{
            //On démarre une conversation sans photos, sans post de référence
            postConversation(textToShare.toString());
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Background
    public void commentPost(String text){
        Post newPosted = diasporaService.comment(postRef.getId(), text);
        postRef=null;
        showToastResult(newPosted!=null, "mise en ligne de votre commentaire");
    }

    @Background
    public void postConversation(String text){
        NewPost newPost = new NewPost();
        StatusMessage statusMessage=new StatusMessage();
        statusMessage.setText(text);
        newPost.setStatus_message(statusMessage);
        Post newPosted = diasporaService.sendPost(newPost);
        //TODO, ajoutez la gestion de la progression
        showToastResult(newPosted!=null, "mise en ligne de votre nouvelle conversation");
    }

    @Background
    public void sendImagesAndComment(String text) {
        String TAG_METHOD = TAG + ".getInfosUserForBar : ";
        LOG.d(TAG_METHOD + "Entrée");
        NewPost newPost = new NewPost();
        StatusMessage statusMessage = new StatusMessage();
        statusMessage.setText(text);
        newPost.setStatus_message(statusMessage);
        StringBuilder listPhotoID = new StringBuilder("{");
        boolean first = true;
        for (String photoUrl : photosUrl) {
            //On partage des photos, attention la dernière photo est le FAKE qui permet d'ajouter de nouvelles photos.
            if (!FAKE_ADDPHOTO.equals(photoUrl)) {
                boolean lastUploadSucceed = false;
                UploadResult uploadFileResult = diasporaService.uploadFile(photoUrl);



                if (uploadFileResult.getData()!=null && uploadFileResult.getData().getPhoto()!=null) {
                    String urlSharedImg = null;
                    if (uploadFileResult.getData().getPhoto().getUnprocessed_image() != null) {
                        urlSharedImg = uploadFileResult.getData().getPhoto().getUnprocessed_image().getUrl();
                    } else if (uploadFileResult.getData().getPhoto().getProcessed_image() != null) {
                        urlSharedImg = uploadFileResult.getData().getPhoto().getProcessed_image().getUrl();
                    }
                    if (urlSharedImg != null) {
                        String uploadedFilePath = urlSharedImg;
                        String uploadedFileGuid = uploadFileResult.getData().getPhoto().getGuid();
                        Integer uploadedFileId = uploadFileResult.getData().getPhoto().getId();

                        if (!first) {
                            listPhotoID.append("; ");
                        }
                        first = false;
                        lastUploadSucceed = true;
                        if (lastUploadSucceed) {
                            listPhotoID.append(uploadFileResult.getData().getPhoto().getId());
                        }
                    }
                }

                showToastInfo(lastUploadSucceed, "partage d'une photo.");
                
                
                
                
                
//                if (!first) {
//                    listPhotoID.append("; ");
//                }
//                first = false;
//                boolean lastUploadSucceed = (uploadFileResult!=null && uploadFileResult.getData()!=null && uploadFileResult.getData().getPhoto()!=null && uploadFileResult.getData().getPhoto().getId()!=null);
//                showToastInfo(lastUploadSucceed, "partage d'une photo.");
//                if (lastUploadSucceed) {
//                    listPhotoID.append(uploadFileResult.getData().getPhoto().getId());
//                }
            }
        }
        newPost.setPhotos(listPhotoID.toString());
        LOG.d(TAG_METHOD+ "Photos ID to share : "+listPhotoID.toString());
        Post newPosted = diasporaService.sendPost(newPost);
        photosUrl = new ArrayList<String>();
        LOG.d(TAG_METHOD + "newPosted is null ? " + (newPosted == null));
        showToastResult(newPosted != null, "mise en ligne de votre nouvelle conversation avec vos photos");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String TAG_METHOD = TAG + ".onActivityResult : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "data is null ? "+(data==null));
        if (data!=null) {
            LOG.d(TAG_METHOD + "requestCode ? "+requestCode);
            switch (requestCode) {
                case SELECT_PICTURE:
                    LOG.d(TAG_METHOD + "case SELECT_PICTURE");
                    Uri imageUri = data.getData();
                    String imageLocalPath = getPath(imageUri);
                    LOG.d(TAG_METHOD + "imageLocalPath : "+imageLocalPath);
                    LOG.d(TAG_METHOD + "call getBitmap(urlImage)");
                    bind(imageLocalPath);
                    break;
                default:
                    break;
            }
        }
        LOG.d(TAG_METHOD + "Sortie");
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        String TAG_METHOD = TAG + ".onCreate : ";
        LOG.d(TAG_METHOD + "Entrée");
        try{
            super.onCreate(savedInstanceState);
            postRef = null;
            photosUrl=new ArrayList<String>();
            LOG.d(TAG_METHOD + "this.getArguments() is null ? "+(this.getArguments()==null));
            if (this.getArguments()!=null){
                Uri imageUri = this.getArguments().getParcelable(Intent.EXTRA_STREAM);
                LOG.d(TAG_METHOD + "imageUri is null ? "+(imageUri==null));
                if (imageUri!=null) {
                    String imageLocalPath = getPath(imageUri);
                    LOG.d(TAG_METHOD + "call bind(imageLocalPath) for image "+imageLocalPath);
                    bind(imageLocalPath);
                }
                String postJson = this.getArguments().getString(Intent.EXTRA_TEXT);
                if (postJson!=null && !postJson.isEmpty()){
                    Gson gson = new Gson();
                    Post postID = gson.fromJson(postJson, Post.class);
                    if (postID!=null){
                        LOG.d(TAG_METHOD + "call bind(postID) for postIDparent "+postID.getId());
                        bind(postID);
                    }
                }
            }else {
                LOG.d(TAG_METHOD + "call bind()");
                bind();
            }
            LOG.d(TAG_METHOD+ "Sortie");
        } catch (Throwable thr) {
            LOG.e(TAG_METHOD+ "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(TAG_METHOD+ "Sortie en erreur");
            throw thr;
        }
    }

    @AfterViews
    public void bindAdapter(){
        sharenextgen_gridphotos.setAdapter(adapter);
    }

    @UiThread
    public void bind(Post postRef){
        String TAG_METHOD = TAG + ".getBitmap(postRef) >";
        LOG.d(TAG_METHOD + "Entrée");
        sharenextgen_post.setVisibility(View.VISIBLE);
        addPostAvatar();
        sharenextgen_post_user.setText(postRef.getAuthor().getName());
        sharenextgen_post_datetime.setText(postRef.getCreated_at_str());
        sharenextgen_post_text.setText(postRef.getText());
        adapter.setPhotosUrls(photosUrl);
        adapter.notifyDataSetChanged();
        sharenextgen_gridphotos.setVisibility(View.GONE);
        sharenextgen_text.setHint("Ecrivez votre commentaire ...");
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void bind(String urlLocalImage){
        sharenextgen_post.setVisibility(View.GONE);
        sharenextgen_gridphotos.setVisibility(View.VISIBLE);
        if (photosUrl.contains(urlLocalImage)){
            Toast.makeText(getActivity(), "La photo a déjà été ajouté",Toast.LENGTH_LONG).show();
            return;
        }
        //On retire le dernier element qui doit être la photo pour l'ajout
        if (!photosUrl.isEmpty()) {
            photosUrl.remove(photosUrl.size() - 1);
        }
        photosUrl.add(urlLocalImage);
        photosUrl.add(FAKE_ADDPHOTO);
        adapter.setPhotosUrls(photosUrl);
        adapter.notifyDataSetChanged();
    }

    @UiThread
    public void bind(){
        sharenextgen_post.setVisibility(View.GONE);
        sharenextgen_gridphotos.setVisibility(View.VISIBLE);
        photosUrl.add(FAKE_ADDPHOTO);
        adapter.setPhotosUrls(photosUrl);
        adapter.notifyDataSetChanged();
    }


    @Background
    public void addPostAvatar(){
        String TAG_METHOD = TAG + ".addPostAvatar > ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "On récupère les données de l'image pour l'url : " + postRef.getAuthor().getAvatar().getLarge());
        byte[] imageDatas = diasporaService.getImageFile(postRef.getAuthor().getAvatar().getLarge());
        LOG.d(TAG_METHOD + "imageData is null ? " + (imageDatas == null));
        if (imageDatas != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
            if (bitmap==null){
                LOG.e(TAG_METHOD + "Erreur de traitement innatendu pour l'image");
            }else {
                addImageViewPostAvatar(bitmap);
            }
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void addImageViewPostAvatar(Bitmap bitmap){
        String TAG_METHOD = TAG + ".addImageViewPostAvatar > ";
        LOG.d(TAG_METHOD + "Entrée");
        sharenextgen_post_avatar.setImageBitmap(bitmap);
        LOG.d(TAG_METHOD + "Sortie");
    }


    @Background
    public void addMyAvatar(){
        String TAG_METHOD = TAG + ".addMyAvatar > ";
        LOG.d(TAG_METHOD + "Entrée");
//        LOG.d(TAG_METHOD + "On récupère les données de l'image pour l'url : " + postRef.getAuthor().getAvatar().getLarge());
        byte[] imageDatas = DiasporaConfig.avatarDatas;//diasporaService.getImageFile(postRef.getAuthor().getAvatar().getLarge());
        LOG.d(TAG_METHOD + "imageData is null ? " + (imageDatas == null));
        if (imageDatas != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
            if (bitmap==null){
                LOG.e(TAG_METHOD + "Erreur de traitement innatendu pour l'image");
            }else {
                addImageViewMyAvatar(bitmap);
            }
        }
        LOG.d(TAG_METHOD + "Sortie");
    }

    @UiThread
    public void addImageViewMyAvatar(Bitmap bitmap){
        String TAG_METHOD = TAG + ".addImageViewMyAvatar > ";
        LOG.d(TAG_METHOD + "Entrée");
        sharenextgen_avatar.setImageBitmap(bitmap);
        LOG.d(TAG_METHOD + "Sortie");
    }


    public String getPath(Uri uri){
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();
        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }


}
