package fr.android.scaron.diaspdroid.vues.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
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
import java.util.List;

import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.Pod;
import fr.android.scaron.diaspdroid.vues.fragment.PodsFragment;
import fr.android.scaron.diaspdroid.vues.fragment.PodsFragment_;
import fr.android.scaron.diaspdroid.vues.view.PodView;
import fr.android.scaron.diaspdroid.vues.view.PodView_;

/**
 * Created by Sébastien on 01/04/2015.
 */
@EBean(scope = EBean.Scope.Singleton)
public class PodsAdapter extends BaseAdapter {
    private static Logger LOGGEUR = LoggerFactory.getLogger(PodsAdapter.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = PodsAdapter.class.getSimpleName();
    private SparseArray<View> viewHolder = new SparseArray<View>();

    List<Pod> pods;
    Pod podSelected = null;

    @RootContext
    Context context;

    Fragment fragment;

    public void setFragmentParent(Fragment fragment){
        this.fragment = fragment;
    }

    @AfterInject
    void initAdapter() {
        pods = new ArrayList<Pod>();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Pod pod = getItem(position);
        if (pod!=null && pod.getId()!=null) {
            int id = Integer.valueOf(pod.getId());
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = PodView_.build(context);
                ((PodView) childView).bind(pod, position);
                viewHolder.put(id, childView);
            }
            return childView;
        }
        return null;

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

    public void setPodSelected(Pod pod){
        String TAG_METHOD = TAG + ".setPodSelected : ";
        LOG.d(TAG_METHOD + "Entrée");
        this.podSelected = pod;
        LOG.d(TAG_METHOD + "Pod selectionné : " + pod.getDomain());
        DiasporaConfig.setPodDomainValue(pod.getDomain(), pod.getSecure());
        if (fragment instanceof PodsFragment){
            ((PodsFragment) fragment).validPod(pod);
        }else if (fragment instanceof PodsFragment_){
            ((PodsFragment_) fragment).validPod(pod);
        }
        LOG.d(TAG_METHOD + "Sortie");
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
