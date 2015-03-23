package fr.android.scaron.diaspdroid.vues.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.vues.adapter.TestAdapter;

/**
 * Created by Sébastien on 13/03/2015.
 */
@EFragment(R.layout.test_layout)
public class TestFragment extends Fragment {

    @ViewById(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    ActionBarActivity activity;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public void setActivityParent(ActionBarActivity activity){
        this.activity = activity;
    }

    @AfterViews
    void init(){

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        String[] dataSet = {"Toto 1 de la montagne\n et du maki de l'oisan", "Toto 2 de la montagne\n et du maki de l'oisan", "Toto 1 de la montagne\n et du maki de l'oisan\"Toto 1 de la montagne\\n et du maki de l'oisan\"\"Toto 1 de la montagne\\n et du maki de l'oisan\"\"Toto 1 de la montagne\\n et du maki de l'oisan\""};
        mAdapter = new TestAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);
    }
}
