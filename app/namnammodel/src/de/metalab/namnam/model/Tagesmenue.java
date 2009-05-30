package de.metalab.namnam.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * model class for the meneus of 1 day
 * @author fake
 */


public class Tagesmenue implements Comparable<Tagesmenue> {

    private Date tag;
    private List<Mensaessen> menues = new ArrayList<Mensaessen>();;

    public Tagesmenue() {
    }

    public Tagesmenue(Date tag) {
        this.tag = tag;
    }

    public void addMenu(Mensaessen e) {
        this.menues.add(e);
    }
    public List<Mensaessen> getMenues() {
        return menues;
    }
    public void setMenues(List<Mensaessen> menues) {
        this.menues = menues;
    }

    public Date getTag() {
        return tag;
    }

    public void setTag(Date tag) {
        this.tag = tag;
    }

    public int compareTo(Tagesmenue rhs) {
        return this.tag.compareTo(rhs.tag);
    }

}
