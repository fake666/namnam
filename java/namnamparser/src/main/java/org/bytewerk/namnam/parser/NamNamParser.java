package org.bytewerk.namnam.parser;

import org.bytewerk.namnam.model.Mensa;

/**
 * @author fake
 */
public interface NamNamParser {
	
    public String getMensaName();

    public Mensa getCurrentMenues() throws Exception;

}
