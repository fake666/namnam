package namnam.parser;

import java.util.Date;
import java.util.Map;
import namnam.model.Tagesmenue;

/**
 * @author fake
 */
public interface NamNamParser {

    public Map<Date, Tagesmenue> getCurrentMenues() throws Exception;

}
