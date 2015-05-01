package fr.android.scaron.diaspdroid.model;

/**
 * Created by Sébastien on 01/05/2015.
 */
public class Aspect {
    //{"id":9795,"name":"Connaissances","selected":true}
    int id;
    String name;
    boolean selected;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
}
