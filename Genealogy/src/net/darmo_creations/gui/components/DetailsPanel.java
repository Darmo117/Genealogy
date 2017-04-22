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
package net.darmo_creations.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.Period;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.darmo_creations.model.Date;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Wedding;
import net.darmo_creations.util.CalendarUtil;
import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Images;

/**
 * This class displays all data of a person.
 *
 * @author Damien Vergnet
 */
public class DetailsPanel extends JPanel {
  private static final long serialVersionUID = 2109771883765681092L;

  public static final String UNKNOWN_DATA = "?";

  private JLabel nameLbl;
  private JLabel birthLbl;
  private JLabel birthPlaceLbl;
  private JLabel weddingLbl;
  private JLabel weddingPlaceLbl;
  private JLabel deathLbl;
  private JLabel deathPlaceLbl;
  private JLabel ageLbl;

  public DetailsPanel() {
    super(new BorderLayout());
    setPreferredSize(new Dimension(300, 110));
    setLayout(new BorderLayout());

    this.nameLbl = new JLabel();
    this.nameLbl.setHorizontalAlignment(JLabel.CENTER);
    this.nameLbl.setBorder(new EmptyBorder(0, 10, 0, 10));
    add(this.nameLbl, BorderLayout.NORTH);

    JPanel infoPnl = new JPanel(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.insets = new Insets(2, 2, 2, 2);
    gbc.weightx = gbc.weighty = 1;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    infoPnl.add(new JLabel(Images.BABY), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = 5;
    infoPnl.add(this.birthLbl = new JLabel(), gbc);
    gbc.gridx = 6;
    gbc.gridwidth = 1;
    infoPnl.add(new JLabel(I18n.getLocalizedWord("in", false)), gbc);
    gbc.gridx = 7;
    gbc.gridwidth = 5;
    infoPnl.add(this.birthPlaceLbl = new JLabel(), gbc);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    infoPnl.add(new JLabel(Images.HEART), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = 5;
    infoPnl.add(this.weddingLbl = new JLabel(), gbc);
    gbc.gridx = 6;
    gbc.gridwidth = 1;
    infoPnl.add(new JLabel(I18n.getLocalizedWord("in", false)), gbc);
    gbc.gridx = 7;
    gbc.gridwidth = 5;
    infoPnl.add(this.weddingPlaceLbl = new JLabel(), gbc);
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    infoPnl.add(new JLabel(Images.CROSS), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = 5;
    infoPnl.add(this.deathLbl = new JLabel(), gbc);
    gbc.gridx = 6;
    gbc.gridwidth = 1;
    infoPnl.add(new JLabel(I18n.getLocalizedWord("in", false)), gbc);
    gbc.gridx = 7;
    gbc.gridwidth = 5;
    infoPnl.add(this.deathPlaceLbl = new JLabel(), gbc);
    gbc.gridx = 1;
    gbc.gridy = 3;
    infoPnl.add(this.ageLbl = new JLabel(), gbc);

    add(infoPnl, BorderLayout.CENTER);
  }

  /**
   * Sets displayed data.
   * 
   * @param member the person
   * @param wedding the wedding it is part of
   */
  public void setInfo(FamilyMember member, Wedding wedding) {
    this.nameLbl.setText(member.toString());
    this.birthLbl.setText(getDate(member.getBirthDate()));
    this.birthPlaceLbl.setText(member.getBirthLocation().orElse(UNKNOWN_DATA));
    this.weddingLbl.setText(wedding != null ? getDate(wedding.getDate()) : UNKNOWN_DATA);
    this.weddingPlaceLbl.setText(wedding != null ? wedding.getLocation().orElse(UNKNOWN_DATA) : UNKNOWN_DATA);
    this.deathLbl.setText(getDate(member.getDeathDate()));
    this.deathPlaceLbl.setText(member.getDeathLocation().orElse(UNKNOWN_DATA));
    this.ageLbl.setText(getAge(member.getAge()));
    revalidate();
  }

  /**
   * Returns the age of the dislayed member.
   * 
   * @param period the time period
   * @return the formatted age
   */
  private String getAge(Optional<Period> period) {
    if (period.isPresent()) {
      Period p = period.get();
      int years = p.getYears();
      int months = p.getMonths();
      String res = years + " " + I18n.getLocalizedWord("year", years > 1);

      if (months > 0)
        res += months + " " + I18n.getLocalizedWord("month", months > 1);

      return res;
    }

    return UNKNOWN_DATA;
  }

  /**
   * Formats the given date.
   * 
   * @param date the date
   * @return the formatted date
   */
  private String getDate(Optional<Date> date) {
    return CalendarUtil.formatDate(date).orElse(UNKNOWN_DATA);
  }
}
