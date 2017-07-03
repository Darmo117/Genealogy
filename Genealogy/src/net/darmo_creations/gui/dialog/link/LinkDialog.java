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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import net.darmo_creations.gui.MainFrame;
import net.darmo_creations.gui.components.DateField;
import net.darmo_creations.gui.dialog.AbstractDialog;
import net.darmo_creations.model.Date;
import net.darmo_creations.model.family.FamilyMember;
import net.darmo_creations.model.family.Wedding;
import net.darmo_creations.util.I18n;
import net.darmo_creations.util.Images;

/**
 * This dialog lets the user add or edit links.
 *
 * @author Damien Vergnet
 */
public class LinkDialog extends AbstractDialog {
  private static final long serialVersionUID = -6591620133064467367L;

  private LinkController controller;
  private DateField dateFld;
  private JTextField locationFld;
  private JTextField spouse1Field, spouse2Field;
  private JList<FamilyMember> childrenList, availChildrenList;
  private JButton addBtn, removeBtn;
  private JTextField searchFld;

  /**
   * Creates a new dialog.
   * 
   * @param owner the owner
   */
  public LinkDialog(MainFrame owner) {
    super(owner, Mode.VALIDATE_CANCEL_OPTION, true);
    setResizable(false);
    setIconImage(Images.JENEALOGIO.getImage());

    this.controller = new LinkController(this);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        LinkDialog.this.dateFld.requestFocus();
      }
    });

    this.dateFld = new DateField(I18n.getLocalizedString("date.format"), FlowLayout.LEFT);
    this.locationFld = new JTextField();
    this.spouse1Field = new JTextField();
    this.spouse1Field.setEnabled(false);
    this.spouse2Field = new JTextField();
    this.spouse2Field.setEnabled(false);
    this.childrenList = new JList<>(new DefaultListModel<>());
    this.childrenList.addListSelectionListener(this.controller);
    this.childrenList.setName("children");
    this.availChildrenList = new JList<>(new DefaultListModel<>());
    this.availChildrenList.addListSelectionListener(this.controller);
    this.availChildrenList.setName("available-children");
    this.addBtn = new JButton(Images.ARROW_UP);
    this.addBtn.setActionCommand("add");
    this.addBtn.addActionListener(this.controller);
    this.removeBtn = new JButton(Images.ARROW_DOWN);
    this.removeBtn.setActionCommand("remove");
    this.removeBtn.addActionListener(this.controller);
    this.searchFld = new JTextField();
    this.searchFld.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void changedUpdate(DocumentEvent e) {
        update(e);
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        update(e);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        update(e);
      }

      private void update(DocumentEvent e) {
        try {
          String text = e.getDocument().getText(0, e.getDocument().getLength()).toLowerCase();
          DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) LinkDialog.this.availChildrenList.getModel();

          for (int i = 0; i < model.size(); i++) {
            FamilyMember m = model.getElementAt(i);

            if (m.toString().toLowerCase().contains(text)) {
              model.remove(i);
              model.insertElementAt(m, 0);
            }
          }
        }
        catch (BadLocationException __) {}
      }
    });
    this.searchFld.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        // System.out.println(e.getKeyChar());
      }
    });

    JPanel fieldsPnl = new JPanel(new GridBagLayout());
    fieldsPnl.setBorder(new EmptyBorder(5, 5, 5, 5));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(2, 2, 2, 2);

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridwidth = 2;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.wedding_date.text")), gbc);
    gbc.gridy = 1;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.wedding_location.text")), gbc);
    gbc.gridy = 2;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.spouse1.text")), gbc);
    gbc.gridy = 3;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.spouse2.text")), gbc);
    gbc.gridy = 4;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.children.text")), gbc);
    gbc.gridwidth = 3;
    gbc.gridy = 7;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.available_children.text")), gbc);
    gbc.gridwidth = 2;
    gbc.gridy = 11;
    fieldsPnl.add(new JLabel(I18n.getLocalizedString("label.search.text")), gbc);
    gbc.gridwidth = 5;
    gbc.weightx = 1;
    gbc.gridx = 2;
    gbc.gridy = 0;
    fieldsPnl.add(this.dateFld, gbc);
    gbc.gridy = 1;
    fieldsPnl.add(this.locationFld, gbc);
    gbc.gridy = 2;
    fieldsPnl.add(this.spouse1Field, gbc);
    gbc.gridy = 3;
    fieldsPnl.add(this.spouse2Field, gbc);
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridheight = 3;
    fieldsPnl.add(new JScrollPane(this.childrenList), gbc);
    gbc.gridy = 7;
    gbc.gridheight = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JPanel p = new JPanel();
    p.add(this.addBtn);
    p.add(this.removeBtn);
    fieldsPnl.add(p, gbc);
    gbc.gridwidth = 5;
    gbc.weightx = 1;
    gbc.gridx = 2;
    gbc.gridy = 8;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridheight = 3;
    fieldsPnl.add(new JScrollPane(this.availChildrenList), gbc);
    gbc.gridy = 11;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.gridheight = 1;
    fieldsPnl.add(this.searchFld, gbc);

    add(fieldsPnl, BorderLayout.CENTER);

    setActionListener(this.controller);

    pack();
    setLocationRelativeTo(owner);
  }

  /**
   * Sets the dialog to "add link" mode. The two spouses must be different
   * 
   * @param spouse1 one spouse
   * @param spouse2 the other spouse
   * @param children the potential children
   */
  public void addLink(FamilyMember spouse1, FamilyMember spouse2, Set<FamilyMember> children) {
    setTitle(I18n.getLocalizedString("dialog.add_link.title"));
    this.controller.reset(spouse1, spouse2, children);
  }

  /**
   * Sets the dialog to "update link" mode.
   * 
   * @param wedding the link
   * @param children the potential children
   */
  public void updateLink(Wedding wedding, Set<FamilyMember> children) {
    setTitle(I18n.getLocalizedString("dialog.update_link.title"));
    this.controller.reset(wedding, children);
  }

  /**
   * @return the created/updated link or nothing if the dialog was canceled
   */
  public Optional<Wedding> getLink() {
    if (!isCanceled())
      return Optional.of(this.controller.getLink());
    return Optional.empty();
  }

  /**
   * Enables/disables the "add" button.
   * 
   * @param enabled
   */
  void setAddButtonEnabled(boolean enabled) {
    this.addBtn.setEnabled(enabled);
  }

  /**
   * Enables/disables the "remove" button.
   * 
   * @param enabled
   */
  void setDeleteButtonEnabled(boolean enabled) {
    this.removeBtn.setEnabled(enabled);
  }

  /**
   * Sets the first spouse.
   * 
   * @param name the name
   */
  void setSpouse1(String name) {
    this.spouse1Field.setText(name);
  }

  /**
   * Sets the second spouse.
   * 
   * @param name the name
   */
  void setSpouse2(String name) {
    this.spouse2Field.setText(name);
  }

  /**
   * @return the date
   */
  Date getDate() {
    return this.dateFld.getDate().orElse(null);
  }

  /**
   * Sets the date.
   * 
   * @param date the new date
   */
  void setDate(Optional<Date> date) {
    this.dateFld.setDate(date.orElse(null));
  }

  /**
   * @return the wedding location
   */
  String getWeddingLocation() {
    return this.locationFld.getText();
  }

  /**
   * Sets the weding location.
   * 
   * @param location the new location
   */
  void setWeddingLocation(Optional<String> location) {
    this.locationFld.setText(location.orElse(""));
  }

  /**
   * @return the children
   */
  FamilyMember[] getChildren() {
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.childrenList.getModel();
    FamilyMember[] children = new FamilyMember[model.size()];

    for (int i = 0; i < model.size(); i++) {
      children[i] = model.getElementAt(i);
    }

    return children;
  }

  /**
   * Sets the list of children.
   * 
   * @param children the children
   */
  void setChildren(Set<FamilyMember> children) {
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.childrenList.getModel();
    model.removeAllElements();
    children.forEach(child -> model.addElement(child));
  }

  /**
   * Sets the list of potential children.
   * 
   * @param children the potential children
   */
  void setAvailableChildren(Set<FamilyMember> children) {
    DefaultListModel<FamilyMember> model = (DefaultListModel<FamilyMember>) this.availChildrenList.getModel();
    model.removeAllElements();
    children.forEach(child -> model.addElement(child));
    this.searchFld.setText(null);
  }

  /**
   * Add the selected children to the list.
   */
  void addSelectedChildren() {
    transfertItems(this.availChildrenList, this.childrenList);
    if (this.availChildrenList.isSelectionEmpty())
      this.addBtn.setEnabled(false);
  }

  /**
   * Remove the selected children from the list.
   */
  void removeSelectedChildren() {
    transfertItems(this.childrenList, this.availChildrenList);
    if (this.childrenList.isSelectionEmpty())
      this.removeBtn.setEnabled(false);
  }

  /**
   * Transfer selected items from {@code source} to {@code destination}.
   * 
   * @param source source list
   * @param destination destination list
   */
  private void transfertItems(JList<FamilyMember> source, JList<FamilyMember> destination) {
    if (!source.isSelectionEmpty()) {
      List<FamilyMember> items = source.getSelectedValuesList();
      DefaultListModel<FamilyMember> srcModel = (DefaultListModel<FamilyMember>) source.getModel();
      DefaultListModel<FamilyMember> destModel = (DefaultListModel<FamilyMember>) destination.getModel();

      items.forEach(child -> {
        destModel.addElement(child);
        srcModel.removeElement(child);
      });
    }
  }

  /**
   * Shows an error message.
   * 
   * @param message the message
   */
  void showErrorDialog(String message) {
    ((MainFrame) getParent()).showErrorDialog(message);
  }
}
