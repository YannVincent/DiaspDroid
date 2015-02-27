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
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import fr.android.scaron.diaspdroid.R;

@EFragment(R.layout.list_fragment)
public class MyListFragment extends ListFragment {

	boolean	listItemClicked = false;
    ArrayAdapter<String> adapter;
    @ViewById(value=android.R.id.list)
	ListView list;

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        ArrayAdapter<CharSequence> adapter;

        adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                android.R.id.text1,
                getResources().getStringArray(R.array.drawer_array));
//		adapter = ArrayAdapter.createFromResource(getActivity(), R.array.drawer_array, R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		list.setAdapter(adapter);
	}

	@ItemClick
	void listItemClicked(String string) {
		listItemClicked  = true;
	}

    @AfterViews
    void notifyDataChanged(){
        if (adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }
}