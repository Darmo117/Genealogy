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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import net.darmo_creations.config.ColorConfigKey;
import net.darmo_creations.config.GlobalConfig;
import net.darmo_creations.gui.components.draggable.Draggable;
import net.darmo_creations.model.family.FamilyMember;

/**
 * This panel represents a family member in the tree display. It can be dragged with the mouse and
 * selected.
 *
 * @author Damien Vergnet
 */
public class FamilyMemberPanel extends JPanel implements Draggable {
  private static final long serialVersionUID = 8199650844222484357L;

  private PanelModel model;

  private Border selectedBorder, unselectedBorder;
  private JLabel nameLbl;

  /**
   * Creates a panel for the given person.
   * 
   * @param member the member to display
   */
  public FamilyMemberPanel(FamilyMember member, GlobalConfig config) {
    this.model = new PanelModel(member.getId());
    this.nameLbl = new JLabel();
    add(this.nameLbl);
    setInfo(member, config);
    setSelected(false);
  }

  /**
   * Sets the data to display.
   * 
   * @param member the member to display
   */
  public void setInfo(FamilyMember member, GlobalConfig config) {
    this.selectedBorder = new LineBorder(config.getValue(ColorConfigKey.CARD_SELECTED_BORDER), 2);
    this.unselectedBorder = new LineBorder(config.getValue(ColorConfigKey.CARD_BORDER), 2);

    switch (member.getGender()) {
      case UNKNOW:
        setBackground(config.getValue(ColorConfigKey.GENDER_UNKNOWN));
        break;
      case MAN:
        setBackground(config.getValue(ColorConfigKey.GENDER_MALE));
        break;
      case WOMAN:
        setBackground(config.getValue(ColorConfigKey.GENDER_FEMALE));
        break;
    }

    this.model.setId(member.getId());
    this.nameLbl.setText(member.toString());

    Dimension size = this.nameLbl.getPreferredSize();
    size.width += 20;
    size.height *= 2;
    setSize(size);
    revalidate();
  }

  @Override
  public void setLocation(Point p) {
    super.setLocation(p);
    getParent().repaint();
  }

  /**
   * @return true if this panel is selected; false otherwise
   */
  public boolean isSelected() {
    return this.model.isSelected();
  }

  /**
   * Sets the selection.
   * 
   * @param selected
   */
  public void setSelected(boolean selected) {
    this.model.setSelected(selected);
    setBorder(selected ? this.selectedBorder : this.unselectedBorder);
  }

  @Override
  public void doClick() {
    fireActionPerformed("select:" + this.model.getId());
  }

  /**
   * Adds an action listener.
   * 
   * @param l the listener
   */
  public void addActionListener(ActionListener l) {
    this.listenerList.add(ActionListener.class, l);
  }

  /**
   * Removes an action listener.
   * 
   * @param l the listener
   */
  public void removeActionListener(ActionListener l) {
    this.listenerList.remove(ActionListener.class, l);
  }

  /**
   * Fires an ActionEvent with the given command.
   * 
   * @param actionCommand the command
   */
  private void fireActionPerformed(String actionCommand) {
    ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand, System.currentTimeMillis(),
        ActionEvent.ACTION_PERFORMED);

    for (ActionListener l : this.listenerList.getListeners(ActionListener.class)) {
      l.actionPerformed(e);
    }
  }

  /**
   * This class is the model for the panel. It holds data about selection and the member's ID.
   *
   * @author Damien Vergnet
   */
  private class PanelModel {
    private boolean selected;
    private long id;

    public PanelModel(long id) {
      this.selected = false;
      this.id = id;
    }

    public boolean isSelected() {
      return this.selected;
    }

    public void setSelected(boolean selected) {
      this.selected = selected;
    }

    public long getId() {
      return this.id;
    }

    public void setId(long id) {
      this.id = id;
    }
  }
}
