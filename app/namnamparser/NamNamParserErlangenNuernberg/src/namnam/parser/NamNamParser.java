package namnam.parser;

import java.util.Date;
import java.util.Map;

/**
 * @author fake
 */
public interface NamNamParser {

    public Map<Date,Map<String,Integer[]>> getCurrentMenues() throws Exception;

}
