/**
 * Copyright (C) 2010-2013 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package fr.android.scaron.diaspdroid.vues.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import org.androidannotations.annotations.rest.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.controler.PodsService;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.Pod;
import fr.android.scaron.diaspdroid.vues.adapter.PodsAdapter;


@EFragment(R.layout.fragment_pod_list)
public class PodsFragment extends DialogFragment {

    private static Logger LOGGEUR = LoggerFactory.getLogger(PodsFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = PodsFragment.class.getSimpleName();


    @ViewById(R.id.fragment_pod_list)
    ListView podList;

    @RestService
    PodsService podsService;

    @Bean
    PodsAdapter adapter;
    View footerView;

    List<Pod> pods;

    Fragment fragment;

    public void setFragmentParent(Fragment fragment){
        this.fragment = fragment;
    }

    public void validPod(Pod pod){
        String TAG_METHOD = TAG + ".validPod : ";
        LOG.d(TAG_METHOD + "Pod selectionné : " + pod.getDomain());
        DiasporaConfig.setPodDomainValue(pod.getDomain(), pod.getSecure());
        if (fragment instanceof ParamsFragment){
            ((ParamsFragment) fragment).validSelectedPod(pod);
        }else if (fragment instanceof ParamsFragment_){
            ((ParamsFragment_) fragment).validSelectedPod(pod);
        }
    }

    @AfterViews
    void bindAdapter() {
        podList.setAdapter(adapter);
        adapter.setFragmentParent(this);
        getDialog().setTitle("Liste de pods");
    }

    @Background
    void getPodsService(){
        addFooterView();
        pods = podsService.getPods().getPods();
        if (pods!=null){
            Collections.sort(pods);
        }
        updateAdapter();
    }

    @UiThread
    void addFooterView(){
        podList.addFooterView(footerView);
    }
    @UiThread
    void updateAdapter(){

        if (footerView != null) {
            podList.removeFooterView(footerView);
        }
        adapter.setPods(pods);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String TAG_METHOD = TAG + ".onCreate : ";
        LOG.d(TAG_METHOD+ "Entrée");
        try{
            super.onCreate(savedInstanceState);
            footerView = ((LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.base_list_item_loading_footer, null, false);
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
}
