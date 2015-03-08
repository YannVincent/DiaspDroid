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

import android.app.ActionBar;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaControler;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.GroupList;
import fr.android.scaron.diaspdroid.model.Pods;
import fr.android.scaron.diaspdroid.vues.adapter.PodListViewAdapter;

//import org.androidannotations.test15.R;
//import org.androidannotations.test15.ebean.SomeBean;
//import org.androidannotations.test15.roboguice.SampleRoboApplication;

@EFragment(R.layout.pod_list)
public class ParamsFragment extends Fragment {

    private static Logger LOGGEUR = LoggerFactory.getLogger(ParamsFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);

    SparseArray<GroupList> groups = new SparseArray<GroupList>();
    PodListViewAdapter podListViewAdapter;

    @ViewById(R.id.pod_listview)
    ExpandableListView pod_listview;
    @ViewById(R.id.poduser)
    TextView poduser;
    @ViewById(R.id.podpassword)
    TextView podpassword;
    @ViewById(R.id.podselected)
    TextView podselected;

    @Click(R.id.podConfigOK)
    void podConfigOKClicked() {
        String methodName = ".updateBookmarksClicked : ";
        LOG.d(methodName+ "Entrée");
        final String user=poduser.getText().toString();
        final String password=podpassword.getText().toString();
        final String url=podselected.getText().toString();
        DiasporaConfig.setPodAuthenticationValues(url, user, password);
        DiasporaControler.testerConnexion();
        LOG.d(methodName+ "Sortie");
    }


//    ListView pod_listview;

    @AfterViews
    void updateBarIcon() {
        DiasporaConfig.addActivity(getActivity());
        String methodName = ".updateBarIcon : ";
        LOG.d(methodName+ "Entrée");
        LOG.d(methodName+ "getActionBar");
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar!=null) {
            LOG.d(methodName+ "actionBar.setTitle");
            actionBar.setTitle("Paramètres");
        }
        LOG.d(methodName+ "Entrée");
    }

    @AfterViews
    void setupListPod(){

        DiasporaConfig.init(getActivity().getApplication(), getActivity());
        String methodName = ".setupListPod : ";

        LOG.d(methodName+ "create adapter for list with PodListViewAdapter");
        podListViewAdapter = new PodListViewAdapter(getActivity(),  groups, pod_listview);
        LOG.d(methodName+ "create setAdapter in post_listview");
        pod_listview.setAdapter(podListViewAdapter);
        if (DiasporaConfig.POD_URL!=null && !DiasporaConfig.POD_URL.isEmpty()){
            podselected.setText(DiasporaConfig.POD_URL);
        }
        if (DiasporaConfig.POD_USER!=null && !DiasporaConfig.POD_USER.isEmpty()){
            poduser.setText(DiasporaConfig.POD_USER);
        }
        if (DiasporaConfig.POD_PASSWORD!=null && !DiasporaConfig.POD_PASSWORD.isEmpty()){
            podpassword.setText(DiasporaConfig.POD_PASSWORD);
        }

        //Restore PODList from memory
        if (DiasporaConfig.POD_LIST_JSON!=null && !DiasporaConfig.POD_LIST_JSON.isEmpty()&&
                DiasporaConfig.POD_LIST!=null && DiasporaConfig.POD_LIST.getPods()!=null && DiasporaConfig.POD_LIST.getPodcount()>0){
            LOG.d(methodName + "Chargement des pods en mémoire");
            podListViewAdapter.addGroup(DiasporaConfig.POD_LIST);
            LOG.d(methodName + "Sortie");
            return;
        }

        //Callback de la récupération du flux
        final FutureCallback<Response<Pods>> podListCallback = new FutureCallback<Response<Pods>>() {
            @Override
            public void onCompleted(Exception e, Response<Pods> podsResponse) {

                String methodName = ".setupListPod podListCallback onCompleted : ";
                LOG.d(methodName+ "exception ? " + e);
                if (e!=null){
                    e.printStackTrace();
                }
                if (e!=null) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    alertDialog.setTitle("PB Données");
                    alertDialog.setMessage("La récupétion de la liste des pods a échouée");
                    alertDialog.show();
                    LOG.e(methodName + "Erreur : " + e.toString(), e);
                    if (e.getCause()!=null) {
                        LOG.e(methodName+ "cause exception ? " + e.getCause().getMessage());
                    }

                }
                LOG.d(methodName+ "podsResponse ? " + podsResponse);
                if (podsResponse!=null){
                    LOG.d(methodName+ "podsResponse value? " + podsResponse.toString());

                    Pods pods = podsResponse.getResult();

                    LOG.d(methodName + "Pods ? : " + pods);
                    pods.setName("Liste des Pods");
                    LOG.d(methodName + "Pods.pods ? : " + pods.getPods());
                    if (pods.getPods()!=null) {
                        pods.setPodcount(pods.getPods().size());
                        pods.children.clear();
                        pods.children.addAll(pods.getPods());
                        DiasporaConfig.setPods(pods);
                        LOG.d(methodName + "Nombre de pods trouvés : " + pods.getPodcount());
                    }
                    podListViewAdapter.addGroup(pods);
//                    groups.append(0, pods);
//                    GroupList group = new Pods("Liste des Pods");
//                    group.children.addAll(pods.getPods());
//                    groups.append(0, group);
//                    podListViewAdapter.notifyDataSetChanged();
                    return;
                }
                ACRA.getErrorReporter().handleException(e);
            }
        };

        DiasporaControler.getPodList(podListCallback);
        LOG.d(methodName + "Sortie");
    }


//    @ItemClick(R.id.pod_listview)
//    void listItemClicked(String food) {
//        String methodName = ".listItemClicked : ";
//        LOG.d(methodName+ "Entrée");
//        LOG.d(methodName+ "Create Toast message for selected '"+food+"'");
//        Toast.makeText(this, "click: " + food, Toast.LENGTH_SHORT).show();
//        LOG.d(methodName + "Sortie");
//    }

}
