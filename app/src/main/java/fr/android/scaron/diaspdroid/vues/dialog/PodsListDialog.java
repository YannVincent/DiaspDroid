package fr.android.scaron.diaspdroid.vues.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ListView;
import android.widget.Toast;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.Pod;
import fr.android.scaron.diaspdroid.vues.adapter.PodsAdapter;

/**
 * Created by Sébastien on 02/04/2015.
 */
//@EFragment(R.layout.dialog_pods_list)
@EFragment(R.layout.fragment_pod_list)
public class PodsListDialog extends DialogFragment {


    private static Logger LOGGEUR = LoggerFactory.getLogger(PodsListDialog.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "PodsListDialog";


    @ViewById(R.id.fragment_pod_list)
    ListView podList;

    @Bean
    DiasporaBean diasporaBean;

    @Bean
    PodsAdapter adapter;

    List<Pod> pods;

    @AfterViews
    void bindAdapter() {
        podList.setAdapter(adapter);
    }

    @Background
    void getPodsService(){
        //TODO trouver pourquoi l'inject n'est pas fait
        if (diasporaBean!=null) {
            pods = diasporaBean.getPods();
            updateAdapter();
        }
    }

    @UiThread
    void updateAdapter(){
        adapter.setPods(pods);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String TAG_METHOD = TAG + ".onCreate : ";
        LOG.d(TAG_METHOD+ "Entrée");
        try{
            super.onCreate(savedInstanceState);
            LOG.d(TAG_METHOD+ "set Title");
            Dialog dialog  = getDialog();
            dialog.setTitle("Liste des pods");
            LOG.d(TAG_METHOD+ "call getPodsService");
            getPodsService();
            LOG.d(TAG_METHOD+ "Sortie");
        } catch (Throwable thr) {
            LOG.e(TAG_METHOD+ "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(TAG_METHOD+ "Sortie en erreur");
            throw thr;
        }
    }

    @ItemClick(R.id.pod_listview)
    void podListClicked(Pod pod) {
        String methodName = ".podClicked : ";
        LOG.d(methodName+ "Entrée");
        Toast.makeText(getActivity().getBaseContext(),"Pod "+pod.getDomain()+" selectionné !",Toast.LENGTH_LONG);
        LOG.d(methodName+ "Sortie");
        dismiss();
    }


//    @ViewById(R.id.dialogTextHeader)
//    TextView dialogTextHeader;
//
//
////    @Override
////    public Dialog onCreateDialog(Bundle savedInstanceState) {
////        super.onCreateDialog(savedInstanceState);
//////        Dialog customDialog = new Dialog(getActivity());
//////        customDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//////        customDialog.setContentView(R.layout.dialog_pods_list);
//////        return customDialog;
////    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
////        super.setTargetFragment(new PodsFragment_(), getTargetRequestCode());
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.dialogFragmentHolder, new PodsFragment_())
//                .commit();
//
//
////        //getFragmentManager()
////        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
////        fragmentTransaction.add(R.id.dialogFragmentHolder, new PodsFragment_());
////        fragmentTransaction.commit();
//    }
//
//    public void setTitle(String title){
//        if (dialogTextHeader!=null) {
//            dialogTextHeader.setText(title);
//        }
//    }
}