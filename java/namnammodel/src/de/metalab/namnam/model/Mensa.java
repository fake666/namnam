package de.metalab.namnam.model;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * diese klasse repraesentiert eine Mensa, und enthaelt alle Tagesmenues fuer diese Mensa
 * @author fake
 * @author testicle
 * @author nati
 */
public class Mensa {

    private TreeSet<Tagesmenue> dayMenues = new TreeSet<Tagesmenue>();
    private String name;

    public Mensa() {
    }

    /**
     * erzeugt ein mensa-objekt mit deren namen bereits vorbelegt
     * @param name der name der mensa
     */
    public Mensa(String name) {
        this.name = name;
    }

    /**
     * gibt den namen der mensa zurueck, die die aktuelle instanz repraesentiert
     * @return den namen der mensa
     */
    public String getName() {
        return name;
    }
    /**
     * setzt den namen der mensa, die die aktuelle isntanz repraesentiert
     * @param name der name der mensa
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gibt alle Tagesmenues zurueck, die zu dieser Mensa bekannt sind
     * @return ein nach Datum sortiertes Set aller bekannter Tagesmenues dieser Mensa
     */
    public Set<Tagesmenue> getDayMenues() {
        return dayMenues;
    }
    /**
     * legt alle bekannten tagesmenues dieser mensa fest
     * @param dayMenues ein nach datum sortiertes set aller bekannter Tagesmenues dieser Mensa
     */
    public void setDayMenues(Set<Tagesmenue> dayMenues) {
        this.dayMenues = new TreeSet<Tagesmenue>(dayMenues);
    }
    /**
     * fuegt ein Tagesmenue zum Satz an Tagesmeneus dieser Mensa hinzu
     * @param dayMenu das Tagesmenue, das hinzugefuegt werden soll
     */
    public void addDayMenue(Tagesmenue dayMenu) {
        this.dayMenues.add(dayMenu);
    }
    /**
     * ueberpreuft, ob fuer das gegebene datum ein tagesmenue vorliegt
     * @param theDate das datum (stunde, minute, sekunde auf 0!) fuer das das vorhandensein eines tagesmenues geprueft werden soll
     * @return true, wenn ein tagesmenue fuer dies datum vorliegt, false andernfalls
     */
    public boolean hasMenuForDate(Date theDate) {
        for(Tagesmenue t : dayMenues)
            if(t.getTag().equals(theDate)) return true;
        return false;
    }
    /**
     * gibt das tagesmenue fuer das angegeben datum zurueck
     * @param theDate das datum (stunde, minute, sekunde auf 0!) fuer das das tagesmenues zurueckgegeben werden soll
     * @return das gesuchte tagesmenue, oder null, wenn keines bekannt ist
     */
    public Tagesmenue getMenuForDate(Date theDate) {
        for(Tagesmenue t : dayMenues)
            if(t.getTag().equals(theDate)) return t;
        return null;
    }

    /**
     * gibt das datum des fruehsten momentan bekannten tagesmenues zurueck
     * @return das datum des fruehesten momentan bekannten tagesmenues
     */
    public Date getFirstDate() {
        return dayMenues.first().getTag();
    }
    /**
     * gibt das datum des letzen momentan bekannten tagesmenues zurueck
     * @return das datum des letzten momentan bekannten tagesmeneues
     */
    public Date getLastDate() {
        return dayMenues.last().getTag();
    }
}
