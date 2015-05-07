package fr.android.scaron.diaspdroid.vues.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.Contact;
import fr.android.scaron.diaspdroid.vues.adapter.ContactsAdapter;

@EFragment(R.layout.fragment_flux_list)
public class ContactsFragment extends Fragment {

    private static Logger LOGGEUR = LoggerFactory.getLogger(ContactsFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = ContactsFragment.class.getSimpleName();
    @Bean
    DiasporaBean diasporaBean;

    @Bean
    ContactsAdapter adapter;

    @ViewById(R.id.fragment_flux_list)
    ListView mListView;
    ActionBarActivity activity;
    View footerView;

    List<Contact> contacts;

    public void setActivityParent(ActionBarActivity activity){
        this.activity = activity;
    }

    @Background
    void getInfos(){
        String TAG_METHOD = TAG + ".getInfos : ";
        LOG.d(TAG_METHOD + "Entrée");
        addFooterView();
        LOG.d(TAG_METHOD+ "call diasporaBean.getContacts");
        contacts = diasporaBean.getContacts();
        bindDatas();
        LOG.d(TAG_METHOD+ "Sortie");
    }
    @UiThread
    void bindDatas(){
        String TAG_METHOD = TAG + ".bindDatas : ";
        LOG.d(TAG_METHOD + "Entrée");
        if (footerView != null) {
            mListView.removeFooterView(footerView);
        }
        if (contacts != null) {
            LOG.d(TAG_METHOD + "adapter.setContacts(contacts) with " + contacts);
            if (adapter!=null) {
                adapter.setContacts(contacts);
            }
        }
        LOG.d(TAG_METHOD+ "Sortie");
    }

    @UiThread
    void addFooterView(){
        mListView.addFooterView(footerView);
    }
    @AfterViews
    void bindAdapter() {
        String TAG_METHOD = TAG + ".bindAdapter : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LOG.d(TAG_METHOD+ "mListView.setAdapter");
        mListView.setAdapter(adapter);
        LOG.d(TAG_METHOD + "Sortie");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String TAG_METHOD = TAG + ".onCreate : ";
        LOG.d(TAG_METHOD+ "Entrée");
        try{
            super.onCreate(savedInstanceState);
            footerView = ((LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.base_list_item_loading_footer, null, false);
            LOG.d(TAG_METHOD+ "call getInfos");
            getInfos();

            LOG.d(TAG_METHOD+ "Sortie");
        } catch (Throwable thr) {
            LOG.e(TAG_METHOD+ "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(TAG_METHOD+ "Sortie en erreur");
            throw thr;
        }
    }


}
