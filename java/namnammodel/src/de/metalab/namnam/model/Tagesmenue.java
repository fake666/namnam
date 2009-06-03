package de.metalab.namnam.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * eine klasse, die die menues fuer einen tag zusammenfasst.
 *
 * @author nati
 * @author testicle
 * @author fake
 */

public class Tagesmenue implements Comparable<Tagesmenue> {

    private Date tag;
    private List<Mensaessen> menues = new ArrayList<Mensaessen>();;

    public Tagesmenue() {
    }

    /**
     * erstellt eine bereits auf einen tag initialiserte instanz
     * @param tag der tag fuer dieses tagesmenue
     */
    public Tagesmenue(Date tag) {
        this.tag = tag;
    }

    /**
     * fuegt ein mensamenue zu der liste der mensaessen dieses tages hinzu
     * @param e das Mensaessen
     */
    public void addMenu(Mensaessen e) {
        this.menues.add(e);
    }
    /**
     * gibt die liste der Mensaessen fuer diesen Tag zurueck
     * @return die liste der menasessen fuer diesen tag
     */
    public List<Mensaessen> getMenues() {
        return menues;
    }
    /**
     * setzt die liste der mensaessen fuer diesen tag
     * @param menues liste der mensaessen fuer diesen tag
     */
    public void setMenues(List<Mensaessen> menues) {
        this.menues = menues;
    }

    /**
     * gibt den tag dieses tagesmenues zurueck
     * @return der tag
     */
    public Date getTag() {
        return tag;
    }
    /**
     * setzt den tag dieses tagesmenues
     * @param tag der tag
     */
    public void setTag(Date tag) {
        this.tag = tag;
    }

    /**
     * erlaubt, ein tagesmenue mit einem anderen zu vergleichen
     * dies ist wichtig fuer die sortierung nach datum im TreeSet
     * der Mensa-Klasse.
     *
     * @see de.metalab.namnam.model.Mensa
     * @see java.util.TreeSet
     * @param rhs die instanz, mit der diese instanz verglichen werden soll
     * @return der vergleich vom datum dieser instanz mit dem datum der anderen instanz
     */
    public int compareTo(Tagesmenue rhs) {
        return this.tag.compareTo(rhs.tag);
    }

}
