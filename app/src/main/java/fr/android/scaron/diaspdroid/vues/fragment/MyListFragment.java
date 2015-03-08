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
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.Trace;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.ItemDrawer;
import fr.android.scaron.diaspdroid.vues.adapter.DrawerListAdapter;

@EFragment(R.layout.list_fragment)
public class MyListFragment extends Fragment {
    private static Logger LOGGEUR = LoggerFactory.getLogger(MyListFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static final String TAG = "MyListFragment";

    boolean	listItemClicked = false;
//    ArrayAdapter<String> adapter;

//    @ViewById(android.R.id.list)
    @ViewById(R.id.listItemDrawer)
    ListView listItemDrawer;

    @Bean
    DrawerListAdapter adapter;

    @AfterViews
    void bindAdapter() {
        listItemDrawer.setAdapter(adapter);
//        listItemDrawer.setSelection(0);
    }

//    @ItemClick
//    void personListItemClicked(Person person) {
//        makeText(this, person.firstName + " " + person.lastName, LENGTH_SHORT).show();
//    }


//    @ViewById(value=android.R.id.empty)
//    TextView empty;
//	@Override
//    @Trace(level = Log.DEBUG, tag = TAG)
//    public void onActivityCreated(Bundle savedInstanceState) {
//        String TAG_METHOD = ".onActivityCreated -> ";
//        LOG.d(TAG + TAG_METHOD + "call super.onActivityCreated");
//        super.onActivityCreated(savedInstanceState);
//        LOG.d(TAG + TAG_METHOD + "call notifyDataChanged");
//        notifyDataChanged();
////        empty.setVisibility(View.GONE);
//
//////        ArrayAdapter<CharSequence> adapter;
////
//////        adapter = new ArrayAdapter<String>(
//////                getActivity(),
//////                android.R.layout.simple_spinner_item,
//////                android.R.id.text1,
//////                getResources().getStringArray(R.array.drawer_array));
////////		adapter = ArrayAdapter.createFromResource(getActivity(), R.array.drawer_array, R.layout.simple_spinner_item);
//////		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////
////        adapter = new ArrayAdapter<String>(
////                getActivity(),
////                android.R.layout.simple_list_item_1,
////                android.R.id.text1,
////                new String[]{
////                        getString(R.string.title_section1),
////                        getString(R.string.title_section2),
////                        getString(R.string.title_section3),
////                        getString(R.string.title_section4),
////                });
////
////		list.setAdapter(adapter);
////        notifyDataChanged();
//	}

//	@ItemClick
//    @Trace(level = Log.DEBUG, tag = TAG)
//	void listItemClicked(String string) {
//        String TAG_METHOD = ".listItemClicked -> ";
//        LOG.d(TAG + TAG_METHOD + "list item clicked ? "+string);
//		listItemClicked  = true;
//	}


    @ItemClick
    @Trace(level = Log.DEBUG, tag = TAG)
    void listItemClicked(ItemDrawer itemDrawer) {
        String TAG_METHOD = ".listItemClicked -> ";
        LOG.d(TAG + TAG_METHOD + "list item clicked ? "+itemDrawer.itemName);
        listItemClicked  = true;
    }

//    @AfterInject
//    @Trace(level = Log.DEBUG, tag = TAG)
//    void afterInject(){
//        String TAG_METHOD = ".afterInject -> ";
//        LOG.d(TAG + TAG_METHOD + "list is null ? "+(list==null));
//        LOG.d(TAG + TAG_METHOD + "adapter is null ? "+(adapter==null));
//        LOG.d(TAG + TAG_METHOD + "setListAdapter");
//        setListAdapter();
////        notifyDataChanged();
//    }

//    @AfterViews
//    @Trace(level = Log.DEBUG, tag = TAG)
//    void afterViews(){
//        String TAG_METHOD = ".afterViews -> ";
//        LOG.d(TAG + TAG_METHOD + "list is null ? "+(list==null));
//        LOG.d(TAG + TAG_METHOD + "adapter is null ? "+(adapter==null));
//        LOG.d(TAG + TAG_METHOD + "setListAdapter");
//        setListAdapter();
//        LOG.d(TAG + TAG_METHOD + "notifyDataChanged");
//        notifyDataChanged();
//
//    }
//    @Trace(level = Log.DEBUG, tag = TAG)
//    void setListAdapter(){
//        String TAG_METHOD = ".setListAdapter -> ";
//        LOG.d(TAG + TAG_METHOD + "initAdapter");
//        initAdapter();
//        if (list!=null) {
//            LOG.d(TAG + TAG_METHOD + "setAdapter");
//            list.setAdapter(adapter);
//        }
//    }
//
//    @Trace(level = Log.DEBUG, tag = TAG)
//    void initAdapter(){
//        String TAG_METHOD = ".initAdapter -> ";
//        LOG.d(TAG + TAG_METHOD + "initAdapter");
//        if (adapter==null) {
//            LOG.d(TAG + TAG_METHOD + "create ArrayAdapter");
//            adapter = new ArrayAdapter<String>(
//                    getActivity(),
//                    android.R.layout.simple_list_item_1,
//                    android.R.id.text1,
//                    new String[]{
//                            getString(R.string.title_section1),
//                            getString(R.string.title_section2),
//                            getString(R.string.title_section3),
//                            getString(R.string.title_section4),
//                    });
//        }
//    }
//    @Trace(level = Log.DEBUG, tag = TAG)
//    void notifyDataChanged(){
//        String TAG_METHOD = ".notifyDataChanged -> ";
//        LOG.d(TAG + TAG_METHOD + "list is null ? "+(list==null));
//        LOG.d(TAG + TAG_METHOD + "adapter is null ? "+(adapter==null));
//        if (adapter!=null) {
//            LOG.d(TAG + TAG_METHOD + "adapter notifyDataSetChanged");
//            adapter.notifyDataSetChanged();
//        }
//    }
}
