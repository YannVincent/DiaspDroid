package fr.android.scaron.diaspdroid.vues.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.Post;

/**
 * Created by Sébastien on 03/02/2015.
 */
public class PostOverviewAdapter  extends ArrayAdapter<Post> {
    private static Logger LOGGEUR = LoggerFactory.getLogger(PostOverviewAdapter.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);
    private Context context;
    private List<Post> items;

    public PostOverviewAdapter(Context context, int textViewResourceId,
                     List<Post> items) {
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
        View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        LOG.d(".getView setText with text : "+items.get(position).getTitle());
        textView.setText(items.get(position).getTitle());
        LOG.d(".getView sortie with view : "+view);
        return view;
    }
}
