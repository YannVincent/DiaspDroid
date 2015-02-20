package fr.android.scaron.diaspdroid.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sébastien on 20/02/2015.
 */
public class GroupList {
    public String name;
    public final List<Object> children = new ArrayList<Object>();

    public GroupList(String name){
        this.name = name;
    }
}
