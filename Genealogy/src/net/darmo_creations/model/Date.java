/*
 * Copyright © 2017 Damien Vergnet
 * 
 * This file is part of Jenealogio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.darmo_creations.model;

import java.util.Calendar;

/**
 * This class represents a date. It uses an instance of {@link java.util.Calendar} to handle data.
 * <b>N.B.</b>: unlike the Calendar class, January is 1 not 0, and so on.
 * 
 * @author Damien Vergnet
 */
public final class Date implements Comparable<Date>, Cloneable {
  private final Calendar calendar;

  /**
   * Creates a date from a year, a month and a date (day in the month).
   * 
   * @param year the year
   * @param month the month (1 for January)
   * @param date the day in the month (1 for the first day)
   */
  public Date(int year, int month, int date) {
    this.calendar = Calendar.getInstance();
    this.calendar.set(year, month - 1, date);
  }

  /**
   * @return the year
   */
  public int getYear() {
    return this.calendar.get(Calendar.YEAR);
  }

  /**
   * @return the month (1 for January)
   */
  public int getMonth() {
    return this.calendar.get(Calendar.MONTH) + 1;
  }

  /**
   * @return the day in the month (1 for the first day)
   */
  public int getDate() {
    return this.calendar.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Returns whether this date represents a time before the time represented by the specified date.
   * 
   * @param date the date to be compared
   * @return true if the time of this date is before the time represented by {@code date}; false
   *         otherwise
   */
  public boolean before(Date date) {
    return this.calendar.before(date.calendar);
  }

  /**
   * Returns whether this date represents a time after the time represented by the specified date.
   * 
   * @param date the date to be compared
   * @return true if the time of this date is after the time represented by {@code date}; false
   *         otherwise
   */
  public boolean after(Date date) {
    return this.calendar.after(date.calendar);
  }

  @Override
  public int compareTo(Date date) {
    return this.calendar.compareTo(date.calendar);
  }

  @Override
  public Date clone() {
    return new Date(getYear(), getMonth(), getDate());
  }
}
