package org.bytewerk.namnam.android;

import java.net.MalformedURLException;
import java.net.URL;

import org.bytewerk.namnam.android.importer.NamNamXMLImporter;

import android.os.Bundle;

public class MainActivity {

	private static final String LOCAL_MENU_FILE = "Menus.xml";
	private static final String URL_MENU_FILE = "http://namnam.bytewerk.org/files/Studiwerk-Erlangen-Nuernberg-Mensa-IN.jxml";

	public void onCreate(Bundle icicle) {

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					new NamNamXMLImporter()
							.loadFromURL(new URL(
									"http://namnam.bytewerk.org/files/Studiwerk-Erlangen-Nuernberg-Mensa-IN.xml"));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}	
			}
		}).start();
	}

}
