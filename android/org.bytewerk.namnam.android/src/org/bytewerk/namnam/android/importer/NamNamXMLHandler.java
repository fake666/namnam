package org.bytewerk.namnam.android.importer;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;

import org.bytewerk.namnam.model.Cafeteria;
import org.bytewerk.namnam.model.DayMenu;
import org.bytewerk.namnam.model.Meal;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NamNamXMLHandler extends DefaultHandler {

	private enum Tags {
		CAFETERIA("Mensa"), FIRST_DATE("firstDate"), LAST_DATE("lastDate"), DAYMENU(
				"Tagesmenue"), MEAL("Mensaessen"), DESCRIPTION("beschreibung"), STUDENT_PRICE(
				"studentenPreis"), NORMAL_PRICE("normalerPreis"), DAY("tag");

		private String tag;

		private Tags(String tag) {
			this.tag = tag;
		}

		public String getTag() {
			return tag;
		}
	}

	private Cafeteria cafeteria;
	private DayMenu tagesmenue;
	private StringBuilder builder;

	// The last attribues of "Mensaessen"-tag
	private Attributes attributes;
	private String description;
	private String normalPrice;
	private String studentPrice;
	private String date;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		builder.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);
		// I'd null out every thing which is out of the "scope" because I wanted
		// the program to throw NullPointerExceptions if it does not work as I expected

		if (localName.equalsIgnoreCase(Tags.DAYMENU.getTag())) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
			try {
				this.tagesmenue.setDate(df.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.cafeteria.addDayMenue(tagesmenue);
			this.tagesmenue = null;
			this.date = null;
		} else if (localName.equalsIgnoreCase(Tags.MEAL.getTag())) {
			final boolean noPork = Boolean.parseBoolean(attributes
					.getValue("moslem"));
			final boolean beef = Boolean.parseBoolean(attributes
					.getValue("rind"));
			final boolean veggie = Boolean.parseBoolean(attributes
					.getValue("vegetarisch"));
			// Divide through 100 because price is in cent now
			final BigDecimal normalPriceBig = new BigDecimal(
					(Double.parseDouble(normalPrice) / 100));
			final BigDecimal studentPriceBig = new BigDecimal(
					(Double.parseDouble(studentPrice) / 100));

			this.tagesmenue.addMenu(new Meal(this.description, normalPriceBig,
					studentPriceBig, veggie, noPork, beef));

			this.attributes = null;
			this.description = null;
			this.normalPrice = null;
			this.studentPrice = null;

		} else if (localName.equalsIgnoreCase(Tags.DESCRIPTION.getTag())) {
			this.description = builder.toString();
		} else if (localName.equalsIgnoreCase(Tags.STUDENT_PRICE.getTag())) {
			this.studentPrice = builder.toString();
		} else if (localName.equalsIgnoreCase(Tags.NORMAL_PRICE.getTag())) {
			this.normalPrice = builder.toString();
		} else if (localName.equalsIgnoreCase(Tags.DAY.getTag())) {
			this.date = builder.toString();
		}

		builder.setLength(0);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		builder = new StringBuilder();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);

		if (localName.equalsIgnoreCase(Tags.CAFETERIA.getTag())) {
			this.cafeteria = new Cafeteria(attributes.getValue("name"));

		} else if (localName.equalsIgnoreCase(Tags.DAYMENU.getTag())) {
			this.tagesmenue = new DayMenu();

		} else if (localName.equalsIgnoreCase(Tags.MEAL.getTag())) {
			this.attributes = attributes;
		}

	}
	
	public Cafeteria getCafeteria(){
		return cafeteria;
	}

}
