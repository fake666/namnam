package org.bytewerk.namnam.model;

import java.math.BigDecimal;

/**
 * This class represents one meal with it's attributes like price, description.
 * Furthermore it contains information about the ingredient like if it's
 * vegetarian, with beef or without pork
 * 
 * @see org.bytewerk.namnam.model.Mensaessen
 * @author fake
 * @author testicle
 * @author nati
 * @author Jan Knieling
 */
public class Meal {

	public static final char withoutPorkToken = 'X';
	public static final char veggieToken = 'V';
	public static final char beefToken = 'R';

	private BigDecimal studentPrice;
	private BigDecimal normalPrice;
	private String description;
	
	private boolean vegetarian = false;
	private boolean noPork = false;
	private boolean beef = false;

	/**
	 * This initializes a {@link Meal} object and sets all of it's attribute.
	 * You may notice that this class do not have any setters, that means the
	 * constructor is the only way to set values and this object is immutable.
	 * 
	 * @param description 
	 * @param normalPrice
	 * @param vegetarian is this an vegetarian meal
	 * @param noPork does this meal contain pork
	 * @param beef does this meal contain beef
	 */
	public Meal(String description, BigDecimal normalPrice, BigDecimal studentPrice, boolean vegetarian,
			boolean noPork, boolean beef) {
		this.normalPrice = normalPrice;
		this.studentPrice = studentPrice;
		this.description = description.replaceAll("\n", "").replaceAll("\t", "");
		this.vegetarian = vegetarian;
		this.noPork = noPork;
		this.beef = beef;
	}


	/**
	 * This return a plain text which contains the following tokens at the end:
	 * - X means that this meal does NOT contain pork
	 * - V means that this meal is vegetable
	 * - R means that this meal contains beef
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public boolean isBeef() {
		return beef;
	}

	public boolean isWithoutPork() {
		return noPork;
	}

	public boolean isVegetarian() {
		return vegetarian;
	}

	public BigDecimal getNormalPrice() {
		return normalPrice;
	}
	
	
    public BigDecimal getStudentPr() {
        return studentPrice;
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (beef ? 1231 : 1237);
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (noPork ? 1231 : 1237);
		result = prime * result
				+ ((normalPrice == null) ? 0 : normalPrice.hashCode());
		result = prime * result
				+ ((studentPrice == null) ? 0 : studentPrice.hashCode());
		result = prime * result + (vegetarian ? 1231 : 1237);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Meal other = (Meal) obj;
		if (beef != other.beef) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (noPork != other.noPork) {
			return false;
		}
		if (normalPrice == null) {
			if (other.normalPrice != null) {
				return false;
			}
		} else if (!normalPrice.equals(other.normalPrice)) {
			return false;
		}
		if (studentPrice == null) {
			if (other.studentPrice != null) {
				return false;
			}
		} else if (!studentPrice.equals(other.studentPrice)) {
			return false;
		}
		if (vegetarian != other.vegetarian) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return "Meal [studentPrice=" + studentPrice + ", normalPrice="
				+ normalPrice + ", description=" + description
				+ ", vegetarian=" + vegetarian + ", noPork=" + noPork
				+ ", beef=" + beef + "]";
	}
	



}
