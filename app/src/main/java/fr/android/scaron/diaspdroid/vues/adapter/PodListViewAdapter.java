package fr.android.scaron.diaspdroid.vues.adapter;

import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.GroupList;
import fr.android.scaron.diaspdroid.model.Pod;

/**
 * Created by Sébastien on 20/02/2015.
 */
public class PodListViewAdapter extends BaseExpandableListAdapter {
    private static Logger LOGGEUR = LoggerFactory.getLogger(PodListViewAdapter.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);

    private SparseArray<GroupList> groups;
    public LayoutInflater inflater;
    public FragmentActivity activity;

    public PodListViewAdapter(FragmentActivity act, SparseArray<GroupList> groups){
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Pod children = (Pod) getChild(groupPosition, childPosition);
        TextView text = null;
        CheckBox check = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.podview_detail, null);
        }
        text = (TextView) convertView.findViewById(R.id.poddetail);
        text.setText("statut : "+children.getStatus()+ " | securisé : "+children.getSecure());

        check = (CheckBox) convertView.findViewById(R.id.podname);
        check.setText(children.getDomain());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, children.getDomain(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    public void addGroup(GroupList group){
        groups = new SparseArray<GroupList>();
        LOG.d(".addGroup "+group+" in index "+groups.size());
        groups.append(groups.size(), group);

        LOG.d(".addGroup notifyDataSetChanged");
        super.notifyDataSetChanged();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.podview_group, null);
        }
        GroupList group = (GroupList) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.name);
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
