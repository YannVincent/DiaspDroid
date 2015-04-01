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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.controler.PodsService;
import fr.android.scaron.diaspdroid.model.Pod;
import fr.android.scaron.diaspdroid.vues.adapter.PodsAdapter;


@EFragment(R.layout.fragment_pod_list)
public class PodsFragment extends Fragment {

    private static Logger LOGGEUR = LoggerFactory.getLogger(PodsFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "PodsFragment";


    @ViewById(R.id.fragment_pod_list)
    ListView podList;

    @RestService
    PodsService podsService;

    @Bean
    PodsAdapter adapter;

    List<Pod> pods;

    @AfterViews
    void bindAdapter() {
        podList.setAdapter(adapter);
    }

    @Background
    void getPodsService(){
        pods = podsService.getPods();
        updateAdapter();
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

        LOG.d(methodName+ "Sortie");
    }
}
