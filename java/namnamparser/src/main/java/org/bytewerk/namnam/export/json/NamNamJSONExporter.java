package org.bytewerk.namnam.export.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bytewerk.namnam.export.NamNamExportException;
import org.bytewerk.namnam.export.NamNamExporter;
import org.bytewerk.namnam.model.Mensaessen;
import org.bytewerk.namnam.model.Tagesmenue;

/**
 * module to export a mensa object to json format
 * @author fake
 */
public class NamNamJSONExporter extends NamNamExporter {

    private static Logger logger = Logger.getLogger(NamNamJSONExporter.class.getName());
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public NamNamJSONExporter(String path) {
        super(path);
    }

    protected void doExport(OutputStream os) throws NamNamExportException{
        try {
            JSONObject jmensa = new JSONObject();
            jmensa.put("name",mensa.getName());
            jmensa.put("firstDate", sdf.format(mensa.getFirstDate()));
            jmensa.put("lastDate", sdf.format(mensa.getLastDate()));

            Iterator<Tagesmenue> dMit = mensa.getDayMenues().iterator();
            JSONArray dayMenueAr = new JSONArray();

            while(dMit.hasNext()) {
                Tagesmenue t = dMit.next();
                JSONObject jtm = new JSONObject();
                jtm.put("tag", sdf.format(t.getTag()));

                JSONArray foodAr = new JSONArray();
                Iterator<Mensaessen> eIt = t.getMenues().iterator();
                while(eIt.hasNext()) {
                    Mensaessen m = eIt.next();
                    JSONObject jm = new JSONObject();
                    jm.put("beschreibung", m.getBeschreibung());
                    jm.put("studentenPreis", m.getStudentenPreis().getCents());
                    jm.put("normalerPreis", m.getPreis().getCents());
                    if(m.getToken() != null){
                    	jm.put("tokens", m.getToken().getTokenValue());
                    }
                    foodAr.put(jm);
                }
                jtm.put("Mensaessen", foodAr);
                dayMenueAr.put(jtm);
            }
            jmensa.put("Tagesmenue", dayMenueAr);

            JSONObject parent = new JSONObject();
            parent.put("Mensa",jmensa);

            Writer w = new OutputStreamWriter(os,Charset.forName("UTF-8"));
            parent.write(w);
            w.flush();
            w.close();
        } catch  (JSONException jsex) {
            logger.log(Level.SEVERE, "JSON Exception while exporting", jsex);
            throw new NamNamExportException("JSON error while exporting",jsex);
        } catch  (IOException ioex) {
            logger.log(Level.SEVERE, "IO Exception while exporting", ioex);
            throw new NamNamExportException("IO error while exporting",ioex);
        }

    }

    @Override
    public String getFileName() {
        return mensa.getName()+".json";
    }

}
