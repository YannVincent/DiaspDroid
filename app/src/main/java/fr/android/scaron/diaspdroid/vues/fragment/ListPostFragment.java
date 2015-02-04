package fr.android.scaron.diaspdroid.vues.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.adapter.PostOverviewAdapter;


public class ListPostFragment extends ListFragment {
    private static Logger LOGGEUR = LoggerFactory.getLogger(ListPostFragment.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);

    private OnItemSelectedListener listener;
    ParseTask parseTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.d(".onCreate entree with savedInstanceState : "+savedInstanceState);
        List<Post> list = new ArrayList<Post>();

        LOG.d(".onCreate create PostOverviewAdapter");
        PostOverviewAdapter adapter = new PostOverviewAdapter(getActivity(),
                android.R.layout.simple_list_item_1, list);
        LOG.d(".onCreate setListAdapter");
        setListAdapter(adapter);
        LOG.d(".onCreate setRetainInstance");
        setRetainInstance(true);
        LOG.d(".onCreate sortie");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        LOG.d(".onListItemClick entree");
        LOG.d(".onListItemClick getListAdapter().getItem("+position+")");
        Post item = (Post) getListAdapter().getItem(position);
        LOG.d(".onListItemClick updateDetail("+item+")");
        updateDetail(item);
        LOG.d(".onListItemClick sortie");
    }

    private static class ParseTask extends
            AsyncTask<String, Void, List<Post>> {
        private ListPostFragment fragment;

        public void setFragment(ListPostFragment fragment) {
            LOG.d("ParseTask.setFragment entree");
            this.fragment = fragment;
            LOG.d("ParseTask.setFragment sortie");
        }

        @Override
        protected List<Post> doInBackground(String... params) {
            LOG.d("ParseTask.doInBackground entree");
            LOG.d("ParseTask.doInBackground set list");
            List<Post> list = FluxContent.POSTS;
            LOG.d("ParseTask.doInBackground sortie avec list : "+list);
            return list;
        }

        @Override
        protected void onPostExecute(List<Post> result) {
            LOG.d("ParseTask.onPostExecute entree");
            LOG.d("ParseTask.onPostExecute setListContent("+result+")");
            fragment.setListContent(result);
            LOG.d("ParseTask.onPostExecute sortie");
        }
    }
    public void updateListContent() {
        LOG.d("updateListContent entree");
        LOG.d("updateListContent parseTask is null ? : "+(parseTask==null));
        if (parseTask == null) {
            LOG.d("updateListContent create ParseTask");
            parseTask = new ParseTask();
            LOG.d("updateListContent parseTask.setFragment(this);");
            parseTask.setFragment(this);
            LOG.d("updateListContent parseTask.execute(...);");
            parseTask.execute("http://www.vogella.com/article.rss");
        }
        LOG.d("updateListContent sortie");
    }
    public void setListContent(List<Post> result) {
        LOG.d("setListContent entree");
        LOG.d("setListContent get listAdapter");
        ArrayAdapter listAdapter = (ArrayAdapter) getListAdapter();
        LOG.d("setListContent clear listAdapter");
        listAdapter.clear();
        LOG.d("setListContent add result in listAdapter");
        listAdapter.addAll(result);
        LOG.d("setListContent parseTask.setFragment(null)");
        parseTask.setFragment(null);
        LOG.d("setListContent parseTask = null");
        parseTask = null;
        LOG.d("setListContent sortie");
    }
    public interface OnItemSelectedListener {
        public void onPostItemSelected(Post post);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LOG.d(".onAttach entree");

        LOG.d(".onAttach activity instanceof OnItemSelectedListener ? : "+(activity instanceof OnItemSelectedListener));
        if (activity instanceof OnItemSelectedListener) {
            LOG.d(".onAttach set listener");
            listener = (OnItemSelectedListener) activity;
        } else {
            LOG.d(".onAttach throw ClassCastException");
            LOG.d(".onAttach sortie");
            throw new ClassCastException(activity.toString()
                    + " doit implémenter ListPostFragment.OnItemSelectedListener");
        }
        LOG.d(".onAttach sortie");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LOG.d(".onDetach entree");
        LOG.d(".onDetach reset listener");
        listener = null;
        LOG.d(".onDetach sortie");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LOG.d(".onActivityCreated entree");
        LOG.d(".onActivityCreated getListAdapter().getCount() == 0 ? :"+(getListAdapter().getCount() == 0));
        if (getListAdapter().getCount() == 0) { // load list initially if empty
            LOG.d(".onActivityCreated updateListContent");
            updateListContent();
        }
        LOG.d(".onActivityCreated sortie");
    }

    // may also be triggered from the Activity
    public void updateDetail(Post item) {
        LOG.d(".updateDetail entree");
        LOG.d(".updateDetail listener.onPostItemSelected("+item.getTitle()+")");
        listener.onPostItemSelected(item);
        LOG.d(".updateDetail sortie");
    }
}
