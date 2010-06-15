package org.bytewerk.namnam.importer;

/**
 * ein simpler exception-wrapper, damit man nacher noch feststellen kann, dass die exception
 * mal aus dem importer kam.
 * 
 * @author fake
 */
public class NamNamImportException extends Exception {

    public NamNamImportException(String msg, Exception cause) {
        super(msg,cause);
    }

}
