package fr.android.scaron.diaspdroid.vues.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.Pod;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.adapter.PodsAdapter;

/**
 * Created by Sébastien on 20/02/2015.
 */
@EViewGroup(R.layout.podview_detail)
public class PodView extends LinearLayout {

    private static Logger LOGGEUR = LoggerFactory.getLogger(PodView.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "PodView";

    @ViewById(R.id.podname)
    public CheckBox podName;

    @ViewById(R.id.poddetail)
    public TextView podDetail;

    @Bean
    PodsAdapter adapter;
    Pod pod;
    Context context;
    int position;


    public PodView(Context context) {
        super(context);
        this.context = context;
    }


    public void bind(final Pod pod, final int position) {
        String TAG_METHOD = TAG + ".getBitmap : ";
        LOG.d(TAG_METHOD+ "Entrée");
        this.pod = pod;
        LOG.d(TAG_METHOD + "setText with text : statut : " + pod.getStatus() + " | securisé : " + pod.getSecure());
        podDetail.setText("statut : " + pod.getStatus() + " | securisé : " + pod.getSecure());
        podName.setText(pod.getDomain());
        LOG.d(TAG_METHOD + "setText with text : " + pod.getDomain() + " set selected " + pod.isSelected());
        podName.setSelected(pod.isSelected());
        this.position = position;
        LOG.d(TAG_METHOD+ "Sortie");
    }

    @Click(R.id.podname)
    public void setPodSelected(){
        String TAG_METHOD = TAG + ".podClicked : ";
        LOG.d(TAG_METHOD + "Entrée");
        podName.setChecked(true);
        pod.setSelected(true);
        adapter.setPodSelected(pod);
        LOG.d(TAG_METHOD+ "Sortie");
    }
}
