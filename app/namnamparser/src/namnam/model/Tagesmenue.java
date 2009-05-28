package namnam.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * model class for the meneus of 1 day
 * @author fake
 */
public class Tagesmenue {

    private Date tag;
    private List<Mensaessen> menue;

    public Tagesmenue(Date tag) {
        this.tag = tag;
        menue = new ArrayList<Mensaessen>();
    }

    public void addMensaessen(Mensaessen e) {
        this.menue.add(e);
    }
    public List<Mensaessen> getMensaessen() {
        return menue;
    }

}
