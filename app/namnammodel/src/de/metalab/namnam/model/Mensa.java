package de.metalab.namnam.model;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * this is a holding entity for all tagesmenues
 * @author fake
 */
public class Mensa {

    private TreeSet<Tagesmenue> dayMenues = new TreeSet<Tagesmenue>();
    private String name;

    public Mensa() {
    }

    public Mensa(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Set<Tagesmenue> getDayMenues() {
        return dayMenues;
    }
    public void setDayMenues(Set<Tagesmenue> dayMenues) {
        this.dayMenues = new TreeSet<Tagesmenue>(dayMenues);
    }
    public void addDayMenue(Tagesmenue dayMenu) {
        this.dayMenues.add(dayMenu);
    }
    public boolean hasMenuForDate(Date theDate) {
        for(Tagesmenue t : dayMenues)
            if(t.getTag().equals(theDate)) return true;
        return false;
    }
    public Tagesmenue getMenuForDate(Date theDate) {
        for(Tagesmenue t : dayMenues)
            if(t.getTag().equals(theDate)) return t;
        return null;
    }

    public Date getFirstDate() {
        return dayMenues.first().getTag();
    }
    public Date getLastDate() {
        return dayMenues.last().getTag();
    }
}
