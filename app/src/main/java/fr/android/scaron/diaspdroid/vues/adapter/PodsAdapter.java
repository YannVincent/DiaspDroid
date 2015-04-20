package fr.android.scaron.diaspdroid.vues.adapter;

import android.content.Context;
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
import java.util.List;

import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.Pod;
import fr.android.scaron.diaspdroid.vues.view.PodView;
import fr.android.scaron.diaspdroid.vues.view.PodView_;

/**
 * Created by Sébastien on 01/04/2015.
 */
@EBean(scope = EBean.Scope.Singleton)
public class PodsAdapter extends BaseAdapter {
    private static Logger LOGGEUR = LoggerFactory.getLogger(PodsAdapter.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "PodsAdapter";

    List<Pod> pods;
    Pod podSelected = null;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        pods = new ArrayList<Pod>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PodView podView;
        if (convertView == null) {
            podView = PodView_.build(context);
        } else {
            podView = (PodView) convertView;
        }

        podView.bind(getItem(position), position);

        return podView;
    }

    @Override
    public int getCount() {
        return pods.size();
    }

    @Override
    public Pod getItem(int position) {
        return pods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void setPodSelected(Pod pod, int position, boolean selected){
        this.podSelected = pod;
        getItem(position).setSelected(selected);
        for (int index=0; index<pods.size(); index++){
            if (index!=position){
                getItem(index).setSelected(false);
            }
        }
        notifyDataSetChanged();
    }
    public Pod getPodSelected(){
        return podSelected;
    }

    public void setPods(List<Pod> pods) {
        String TAG_METHOD = TAG + ".setPods : ";
        try {
            LOG.d(TAG_METHOD + "Entrée");
            if (pods == null) {
                LOG.d(".setPods initialize this.pods");
                this.pods = new ArrayList<Pod>();
            } else {
                LOG.d(TAG_METHOD + "set this.pods with" + pods);
                this.pods = pods;
            }
            LOG.d(TAG_METHOD + "notifyDataSetChanged");
            super.notifyDataSetChanged();
            LOG.d(TAG_METHOD + "sortie");
        } catch (Throwable thr) {
            LOG.e(TAG_METHOD + "Erreur : " + thr.toString(), thr);
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }
}
