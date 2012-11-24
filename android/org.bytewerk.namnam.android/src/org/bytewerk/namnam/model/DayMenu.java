package org.bytewerk.namnam.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class contains all {@link Meal} for one {@link Date}
 *
 * @author nati
 * @author testicle
 * @author fake
 * @author Jan Knieling
 */

public class DayMenu implements Comparable<DayMenu> {

    private Date date;
    private List<Meal> menuList = new ArrayList<Meal>();

    public DayMenu() {
    	//Intentionally left empty
    }

    public DayMenu(Date date) {
        this.date = date;
    }

    public void addMenu(Meal e) {
        this.menuList.add(e);
    }
 
    public List<Meal> getMenues() {
        return menuList;
    }
  
    public void setMenues(List<Meal> menues) {
        this.menuList = menues;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date){
    	this.date = date;
    }
    
    /**
     * {@link DayMenu} should be sorted in the order of their {{@link #date}
     */
    public int compareTo(DayMenu rhs) {
        return this.date.compareTo(rhs.date);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((menuList == null) ? 0 : menuList.hashCode());
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
		DayMenu other = (DayMenu) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (menuList == null) {
			if (other.menuList != null) {
				return false;
			}
		} else if (!menuList.equals(other.menuList)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "DayMenu [date=" + date + ", menuList=" + menuList + "]";
	}
    
    

}
