package org.bytewerk.namnam.model;

import java.util.Date;
import java.util.NavigableMap;
import java.util.TreeMap;

import android.annotation.TargetApi;

/**
 * This class represents a Cafeteria and contains it's {@link DayMenu}
 * @author fake
 * @author testicle
 * @author nati
 * @author Jan Knieling
 */
@TargetApi(9)
public class Cafeteria {

    private NavigableMap<Date, DayMenu> dayMenues = new TreeMap<Date, DayMenu>();
    private String name;


    public Cafeteria(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public NavigableMap<Date, DayMenu> getDayMenues() {
        return dayMenues;
    }
    /**
     * This setter copies the given {@link NavigableMap} and overwrites the dayMenues attribute with the copy.
     * @param dayMenues
     */
    public void setDayMenues(NavigableMap<Date, DayMenu> dayMenues) {
        this.dayMenues = new TreeMap<Date, DayMenu>(dayMenues);
    }

    
    public void addDayMenue(DayMenu dayMenu) {
        this.dayMenues.put(dayMenu.getDate(), dayMenu);
    }

    
    /**
     * Checks if a {@link DayMenu} exists for a given Date. 
     * 
     * @param theDate is the {@link Date} to proof. Minutes, hours and seconds should be set to 0.
     * @return
     */
    public boolean hasMenuForDate(Date theDate) {
        return (dayMenues.get(theDate) != null);
    }
    /**
     * If a {@link DayMenu} for a given Date exists it would be returned. 
     * 
     * @param theDate is the {@link Date} to for which the Menu should be return. Minutes, hours and seconds should be set to 0.
     * @return this method will return an instance of {@link DayMenu} or <code>null</code> if no menus for the {@link Date} are available. 
     */
    public DayMenu getMenuForDate(Date theDate) {
        return dayMenues.get(theDate);
    }

    public Date getFirstDate() {
        return dayMenues.firstKey();
    }

    public Date getLastDate() {
        return dayMenues.lastKey();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dayMenues == null) ? 0 : dayMenues.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Cafeteria other = (Cafeteria) obj;
		if (dayMenues == null) {
			if (other.dayMenues != null) {
				return false;
			}
		} else if (!dayMenues.equals(other.dayMenues)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Cafeteria [dayMenues=" + dayMenues + ", name=" + name + "]";
	}

	
    
    
}
