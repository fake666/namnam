package de.metalab.namnam.model;

/**
 * happa happa! yummy!
 * @author fake
 */
public class Mensaessen extends Essen {

    private Preis studentenPreis;

    public Mensaessen() {
        super();
    }

    public Mensaessen(String desc, Integer bPreis, Integer sPreis) {
        super(desc,bPreis);
        this.studentenPreis = new Preis(sPreis);
    }

    public Preis getStudentenPreis() {
        return studentenPreis;
    }

    public void setStudentenPreis(Preis studentenPreis) {
        this.studentenPreis = studentenPreis;
    }
}
