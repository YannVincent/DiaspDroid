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
import fr.android.scaron.diaspdroid.model.Comment;
import fr.android.scaron.diaspdroid.vues.view.CommentView;
import fr.android.scaron.diaspdroid.vues.view.CommentView_;

/**
 * Created by Sébastien on 01/04/2015.
 */
@EBean(scope = EBean.Scope.Singleton)
public class CommentsAdapter extends BaseAdapter {
    private static Logger LOGGEUR = LoggerFactory.getLogger(CommentsAdapter.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = CommentsAdapter.class.getSimpleName();
    private SparseArray<View> viewHolder = new SparseArray<View>();

    List<Comment> comments;

    @RootContext
    Context context;

    Fragment fragment;

    public void setFragmentParent(Fragment fragment){
        this.fragment = fragment;
    }

    @AfterInject
    void initAdapter() {
        comments = new ArrayList<Comment>();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);
        if (comment!=null && comment.getId()!=null) {
            int id = Integer.valueOf(comment.getId());
            View childView = viewHolder.get(id);
            if (childView == null) {
                childView = CommentView_.build(context);
                ((CommentView) childView).bind(comment);
                viewHolder.put(id, childView);
            }
            return childView;
        }
        return null;

    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    public void setComments(List<Comment> comments) {
        String TAG_METHOD = TAG + ".setComments : ";
        try {
            LOG.d(TAG_METHOD + "Entrée");
            if (comments == null) {
                LOG.d(".setComments initialize this.comments");
                this.comments = new ArrayList<Comment>();
            } else {
                LOG.d(TAG_METHOD + "set this.comments with" + comments);
                this.comments = comments;
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
