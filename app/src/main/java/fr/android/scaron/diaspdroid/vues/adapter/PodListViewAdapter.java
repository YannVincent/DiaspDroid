package fr.android.scaron.diaspdroid.vues.adapter;

import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Text;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.GroupList;
import fr.android.scaron.diaspdroid.model.Pod;
import fr.android.scaron.diaspdroid.model.Pods;
import fr.android.scaron.diaspdroid.vues.view.DetailPostView;
import fr.android.scaron.diaspdroid.vues.view.PodView;

/**
 * Created by Sébastien on 20/02/2015.
 */
public class PodListViewAdapter extends BaseExpandableListAdapter {
    private static Logger LOGGEUR = LoggerFactory.getLogger(PodListViewAdapter.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);

    private SparseArray<GroupList> groups;
    public LayoutInflater inflater;
    public FragmentActivity activity;
    ExpandableListView litview;

    public PodListViewAdapter(FragmentActivity act, SparseArray<GroupList> groups, ExpandableListView litview){
        super();
        activity = act;
        this.groups = groups;
        this.litview = litview;
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
        final int groupID = groupPosition;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.podview_detail, null);
        }

        final PodView podView;
        podView = new PodView();
        podView.text = (TextView) convertView.findViewById(R.id.poddetail);
        podView.text.setText("statut : "+children.getStatus()+ " | securisé : "+children.getSecure());

        podView.check = (CheckBox) convertView.findViewById(R.id.podname);
//        podView.check = (RadioButton) convertView.findViewById(R.id.podname);
        podView.check.setText(children.getDomain());
        podView.check.setSelected(children.isSelected());

        convertView.setTag(podView);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String methodName = ".getChildView onClick : ";
                LOG.d(methodName+ "Entrée");


                Pods pods = (Pods)getGroup(groupID);
                int positionTemp = 0;
                for (Pod pod:pods.getPods()){
                    if (positionTemp == childPosition){
                        LOG.d(methodName + "set children selected true at position " + positionTemp);
                        pod.setSelected(true);
                        podView.check.setSelected(true);
                    }else {
                        LOG.d(methodName + "set children selected false at position "+positionTemp);
                        pod.setSelected(false);
                    }
                    positionTemp++;
                }

//                pods.children.clear();
//                pods.children.addAll(pods.getPods());
                DiasporaConfig.setPods(pods);
                litview.collapseGroup(groupID);
                LOG.d(methodName + "notifyDataSetChanged");
                notifyDataSetChanged();


////                PodView podView = (PodView)v.getTag();
//                GroupList group = (GroupList)getGroup(groupID);
//                int positionTemp = 0;
////                podView.check.setSelected(true);
////                children.setSelected(true);
//                for (Object podObject:group.children){
//                    Pod pod=(Pod)podObject;
//                    if (positionTemp == childPosition){
//                        LOG.d(methodName + "set children selected true at position " + positionTemp);
//                        pod.setSelected(true);
//                    }else {
//                        LOG.d(methodName + "set children selected false at position "+positionTemp);
//                        pod.setSelected(false);
//                    }
//                    group.children.set(positionTemp, pod);
//                    positionTemp++;
//                }
//
//                DiasporaConfig.setPods(pods);
////                LOG.d(methodName + "cleargroups");
////                clearGroups();
////                LOG.d(methodName + "addGroup "+group);
////                addGroup(group);
//                litview.collapseGroup(groupID);
//                LOG.d(methodName + "notifyDataSetChanged");
//                notifyDataSetChanged();
////                LOG.d(methodName + "super.notifyDataSetChanged");
////                PodListViewAdapter.super.notifyDataSetChanged();


                TextView podSelected = (TextView) activity.findViewById(R.id.podselected);
                String podName = children.getDomain();
                String podSecured = children.getSecure();
                children.setSelected(true);
                boolean isPodSecured = ("true".equals(podSecured.toLowerCase()));
                String podUrl="http";
                if (isPodSecured){
                    podUrl=podUrl+"s";
                }
                podUrl=podUrl+"://"+podName;
                podSelected.setText(podUrl);
                LOG.d(methodName + "Show message " + children.getDomain());
                Toast.makeText(activity, children.getDomain(), Toast.LENGTH_SHORT).show();
//                GroupList group = (GroupList) getGroup(groupID);
//                ((CheckedTextView) convertView)
                LOG.d(methodName + "Sortie");
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
        notifyDataSetChanged();
//        super.notifyDataSetChanged();
    }

    public void clearGroups(){
        groups.clear();
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

//        return true;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
