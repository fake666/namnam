package de.metalab.namnam.parser;

import de.metalab.namnam.model.Mensa;

/**
 * @author fake
 */
public interface NamNamParser {
	
    public String getMensaName();

    public Mensa getCurrentMenues() throws Exception;

}
