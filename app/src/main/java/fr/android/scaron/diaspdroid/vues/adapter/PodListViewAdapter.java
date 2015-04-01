package fr.android.scaron.diaspdroid.vues.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.GroupList;
import fr.android.scaron.diaspdroid.model.Pod;
import fr.android.scaron.diaspdroid.model.Pods;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.view.PodView;

/**
 * Created by Sébastien on 20/02/2015.
 */
public class PodListViewAdapter extends ArrayAdapter<Pod> {
    private static Logger LOGGEUR = LoggerFactory.getLogger(PodListViewAdapter.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private Context context;
    private List<Pod> items;

    public PodListViewAdapter(Context context, int textViewResourceId,
                               List<Pod> items) {
        super(context, textViewResourceId, items);
        LOG.d(".constructeur entree with items : "+items);
        this.context = context;
        this.items = items;
        LOG.d(".constructeur sortie with items : "+items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LOG.d(".getView entree with position : "+position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.podview_detail, null);
        final Pod pod = items.get(position);
        TextView podDetail = (TextView) convertView.findViewById(R.id.poddetail);
        LOG.d(".getView setText with text : statut : "+pod.getStatus()+ " | securisé : "+pod.getSecure());
        podDetail.setText("statut : "+pod.getStatus()+ " | securisé : "+pod.getSecure());
        final CheckBox podName = (CheckBox) convertView.findViewById(R.id.podname);
        LOG.d(".getView setText with text : "+pod.getDomain()+ " set selected "+pod.isSelected());
        podName.setText(pod.getDomain());
        podName.setSelected(pod.isSelected());
        convertView.setTag(view);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String methodName = ".getView onClick : ";
                LOG.d(methodName + "Entrée");
                if (pod.isSelected()){
                    pod.setSelected(false);
                    podName.setSelected(false);
                }else{
                    pod.setSelected(true);
                    podName.setSelected(true);
                }
            }
        });
        return view;

    }
}
