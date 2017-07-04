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
package net.darmo_creations.gui.dialog.link;

import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.darmo_creations.gui.dialog.DefaultDialogController;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Relationship;

/**
 * This controller handles actions of the LinkDialog class.
 *
 * @author Damien Vergnet
 */
public class LinkController extends DefaultDialogController<LinkDialog> implements ListSelectionListener {
  private FamilyMember partner1, partner2;

  /**
   * Creates a controller.
   * 
   * @param dialog the dialog
   */
  public LinkController(LinkDialog dialog) {
    super(dialog);
  }

  /**
   * Resets the dialog.
   * 
   * @param partner1 one partner
   * @param partner2 the other partner
   * @param children the children
   */
  public void reset(FamilyMember partner1, FamilyMember partner2, Set<FamilyMember> children) {
    reset(new Relationship(null, null, true, partner1, partner2), children);
  }

  /**
   * Resets the dialog.
   * 
   * @param relation the link
   * @param children the children
   */
  public void reset(Relationship relation, Set<FamilyMember> children) {
    this.partner1 = relation.getPartner1();
    this.partner2 = relation.getPartner2();

    this.dialog.setWeddingChecked(relation.isWedding());
    this.dialog.setPartner1(relation.getPartner1().toString());
    this.dialog.setPartner2(relation.getPartner2().toString());
    this.dialog.setDate(relation.getDate());
    this.dialog.setRelationshipLocation(relation.getLocation());
    this.dialog.setAvailableChildren(children);
    this.dialog.setChildren(relation.getChildren());

    this.dialog.setCanceled(false);
    this.dialog.setAddButtonEnabled(false);
    this.dialog.setDeleteButtonEnabled(false);
  }

  /**
   * @return the link
   */
  public Relationship getLink() {
    return new Relationship(this.dialog.getDate(), this.dialog.getRelationshipLocation(), this.dialog.isWeddingChecked(), this.partner1,
        this.partner2, this.dialog.getChildren());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);

    if (!this.dialog.isVisible())
      return;

    switch (e.getActionCommand()) {
      case "add":
        this.dialog.addSelectedChildren();
        break;
      case "remove":
        this.dialog.removeSelectedChildren();
        break;
    }
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {
      JList<?> list = (JList<?>) e.getSource();

      if (list.getName().equals("children")) {
        this.dialog.setDeleteButtonEnabled(e.getFirstIndex() != -1);
      }
      else if (list.getName().equals("available-children")) {
        this.dialog.setAddButtonEnabled(e.getFirstIndex() != -1);
      }
    }
  }
}
