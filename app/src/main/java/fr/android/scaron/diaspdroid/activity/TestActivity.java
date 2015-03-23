package fr.android.scaron.diaspdroid.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.vues.adapter.TestAdapter;

/**
 * Created by Sébastien on 13/03/2015.
 */
public class TestActivity extends ActionBarActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager =  new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        String[] dataSet = {"Toto 1 de la montagne\n et du maki de l'oisan", "Toto 2 de la montagne\n et du maki de l'oisan", "Toto 1 de la montagne\n et du maki de l'oisan\"Toto 1 de la montagne\\n et du maki de l'oisan\"\"Toto 1 de la montagne\\n et du maki de l'oisan\"\"Toto 1 de la montagne\\n et du maki de l'oisan\""};
        mAdapter = new TestAdapter(dataSet);
        mRecyclerView.setAdapter(mAdapter);
    }
}

