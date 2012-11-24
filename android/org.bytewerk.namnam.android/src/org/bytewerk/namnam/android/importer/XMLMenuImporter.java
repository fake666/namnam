package org.bytewerk.namnam.android.importer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.bytewerk.namnam.model.Cafeteria;

/**
 * 
 * @author Jan Knieling
 *
 */
public class XMLMenuImporter {

	private static final long MILLIS_PER_DAY = 86400000L;
	
	private final String LOCAL_MENU_FILE;
	private final String URL_MENU_FILE;
	
	
	public XMLMenuImporter(String localMenuFilePath, String remoteMenuFileUrl) {
		this.LOCAL_MENU_FILE = localMenuFilePath;
		this.URL_MENU_FILE = remoteMenuFileUrl;
	}
	
	public Cafeteria bla() {
		final NamNamXMLImporter importer = new NamNamXMLImporter();
		final File cafeteriaFile = new File(LOCAL_MENU_FILE);
		Cafeteria cafeteria = null;
		
		if (cafeteriaFile.exists() && cafeteriaFile.canRead()) {
			try {
				cafeteria = importer.load(new FileInputStream(cafeteriaFile));
				// If error occurs just log why it happened and try to get the
				// file via internet
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			// We just want to get the full days
			final long currentTime = System.currentTimeMillis()
					/ MILLIS_PER_DAY;
			final boolean cafeteriaMenuIsUpToDate = currentTime >= cafeteria
					.getFirstDate().getTime()
					&& currentTime <= cafeteria.getLastDate().getTime();
			if (!cafeteriaMenuIsUpToDate) {
				cafeteriaFile.delete();
				cafeteria = null;
			}
		}

		if (cafeteria == null) {
			final String fileContent = readRemoteFile(URL_MENU_FILE);

			writeLocalFile(cafeteriaFile, fileContent);

			// FIXME: Load file as object
		}

		if (cafeteria == null) {
			// FIXME: Show Errordialog in UI
		}
		
		return cafeteria;
	}

	private String readRemoteFile(final String fileURL) {
		final StringBuilder fileContent = new StringBuilder();
		BufferedReader br = null;
		try {
			final URL url = new URL(fileURL);

			final InputStreamReader isr = new InputStreamReader(
					url.openStream());
			br = new BufferedReader(isr);
			String tmp = "";
			while ((tmp = br.readLine()) != null) {
				fileContent.append(tmp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileContent.toString();
	}

	private void writeLocalFile(final File cafeteriaFile,
			final String fileContent) {

		BufferedWriter bw = null;
		try {
			final FileWriter fw = new FileWriter(cafeteriaFile);
			bw = new BufferedWriter(fw);
			bw.append(fileContent);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
