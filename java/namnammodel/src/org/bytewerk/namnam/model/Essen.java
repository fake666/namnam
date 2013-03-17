package org.bytewerk.namnam.model;

/**
 * Diese Klasse repraesentiert ein generisches Essen mit einem Preis und dient
 * nur als Basisklasse fuer die Mensaessen-klasse.
 * 
 * @see org.bytewerk.namnam.model.Mensaessen
 * @author fake
 * @author testicle
 * @author nati
 * @author Jan Knieling
 */
public class Essen {

	private Preis price;
	private String description;
	private MealToken token;

	public enum MealToken {
		// Didn't use special character because this should be used in export.
		// Just to be safe.
		BEEF("R", "Rind"), PORK("S", "Schwein"), POULTRY("G", "Gefluegel"), LAMB(
				"L", "Lamm"), VENSION("W", "Wild"), SCROD("F", "Fisch"), VEGETARIAN(
				"V", "Vegetarisch"), VEGAN("../pics/vegan-logo.png", "Vegan");

		private String token;
		private String description;

		private MealToken(String token, String description) {
			this.token = token;
			this.description = description;
		}

		public String getTokenValue() {
			return token;
		}

		public String getDescription() {
			return description;
		}
	}

	public Essen() {
		// Empty for json
	}

	/**
	 * 
	 * 
	 * @param description
	 *            the description of that meal you want to create.
	 * @param priceInCents
	 * @param token which token does this meal have? For example it's vegan or with
	 *            pork. All tokens are defined in {@link MealToken}.
	 *            <code>null</code> is a valid value if no token should be
	 *            setted for this meal.
	 */
	public Essen(String description, int priceInCents, MealToken token) {
		this.price = new Preis(priceInCents);
		this.description = description.replaceAll("\n", "")
				.replaceAll("\t", "");
		this.token = token;
	}

	/**
	 * gibt den reinen text der beschreibung des essens zurueck. hier sind die
	 * token am ende (V,X,R) sowie eventuelle bis zu 2stellige zahlen am ende
	 * bereits entfernt!
	 * 
	 * @return den reinen beschreibungstext des essens.
	 */
	public String getBeschreibung() {
		return description;
	}

	/**
	 * gibt das mit diesem essen assoziierte "Preis"-objekt zurueck, das ist der
	 * nicht-reduzierte Preis, den "jedermann" zahlen muss, der kein student
	 * ist.
	 * 
	 * @return den normalen, nicht reduzierten preis des essens
	 */
	public Preis getPreis() {
		return price;
	}

	/**
	 * This describes the type of the meal. For example beef or pork.
	 * 
	 * @return the token. You may notice that this method could return
	 *         <code>null</code> if no token is set.
	 */
	public MealToken getToken() {
		return token;
	}

	public Preis getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public void setPrice(Preis price) {
		this.price = price;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setToken(MealToken token) {
		this.token = token;
	}

}
