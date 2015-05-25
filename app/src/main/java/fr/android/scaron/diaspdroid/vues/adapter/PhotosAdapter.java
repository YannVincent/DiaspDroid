package fr.android.scaron.diaspdroid.vues.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.vues.view.PhotoView;
import fr.android.scaron.diaspdroid.vues.view.PhotoView_;

/**
 * Created by Sébastien on 29/03/2015.
 */
@EBean(scope = EBean.Scope.Singleton)
public class PhotosAdapter extends BaseAdapter {
    private static Logger LOGGEUR = LoggerFactory.getLogger(PhotosAdapter.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = PhotosAdapter.class.getSimpleName();
    private SparseArray<View> viewHolder = new SparseArray<View>();
    List<String> photosUrls;
    final static String FAKE_ADDPHOTO = "FAKE_ADDPHOTO";

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        photosUrls = new ArrayList<String>();
        photosUrls.add(FAKE_ADDPHOTO);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String photoUrl = getItem(position);
        int id = photoUrl.hashCode();
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView  = PhotoView_.build(context);
            ((PhotoView)childView).bind(photoUrl);
            viewHolder.put(id, childView);
        }
        return childView;


    }

    @Override
    public int getCount() {
        return photosUrls.size();
    }

    @Override
    public String getItem(int position) {
        return photosUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    public void setPhotosUrls(List<String> photosUrls){
        String TAG_METHOD = TAG + ".setContacts : ";
        try{
            LOG.d(TAG_METHOD + "Entrée");
            if (photosUrls==null) {
                LOG.d(".setContacts initialize this.contacts");
                this.photosUrls = new ArrayList<String>();
            }else{
                LOG.d(TAG_METHOD + "set this.contacts with"+photosUrls);
                this.photosUrls = Collections.checkedList(photosUrls, String.class);
            }
            LOG.d(TAG_METHOD + "notifyDataSetChanged");
            super.notifyDataSetChanged();
            LOG.d(TAG_METHOD + "sortie");
        } catch (Throwable thr) {
            LOG.e(TAG_METHOD + "Erreur : "+thr.toString(), thr);
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }
}