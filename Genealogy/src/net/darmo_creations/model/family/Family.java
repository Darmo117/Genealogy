package net.darmo_creations.model.family;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A family has members and each member can be married.
 * 
 * @author Damien Vergnet
 */
public class Family {
  /** The global ID */
  private long globalId;
  /** THis family's name */
  private String name;
  /** Members */
  private Set<FamilyMember> members;
  /** Weddings */
  private Set<Wedding> weddings;

  /**
   * Creates a family with no members and no weddings.
   * 
   * @param name family's name
   */
  public Family(String name) {
    this(0, name, new HashSet<>(), new HashSet<>());
  }

  /**
   * Creates a family with given members and weddings.
   * 
   * @param globalId global ID
   * @param name family's name
   * @param members the members
   * @param weddings the weddings
   */
  public Family(long globalId, String name, Set<FamilyMember> members, Set<Wedding> weddings) {
    this.globalId = globalId;
    this.name = name;
    this.members = members;
    this.weddings = weddings;
  }

  /**
   * @return this family's name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the family name.
   * 
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return all the members
   */
  public Set<FamilyMember> getAllMembers() {
    return new HashSet<>(this.members);
  }

  /**
   * Gets the member with the given ID.
   * 
   * @param id the ID
   * @return the member or nothing if none were found
   */
  public Optional<FamilyMember> getMember(long id) {
    return this.members.stream().filter(member -> member.getId() == id).findAny();
  }

  /**
   * Adds a member to the family.
   * 
   * @param member the new member
   */
  public void addMember(FamilyMember member) {
    if (!this.members.contains(member)) {
      this.members.add(member.copy(getNextMemberId()));
    }
  }

  /**
   * Updates an existing member.
   * 
   * @param member the member's updated data
   */
  public void updateMember(FamilyMember member) {
    Optional<FamilyMember> optional = getMember(member.getId());

    if (optional.isPresent()) {
      FamilyMember m = optional.get();

      m.setImage(member.getImage().orElse(null));
      m.setName(member.getName().orElse(null));
      m.setFirstName(member.getFirstName().orElse(null));
      m.setGender(member.getGender());
      m.setBirthDate(member.getBirthDate().orElse(null));
      m.setBirthPlace(member.getBirthLocation().orElse(null));
      m.setDeathDate(member.getDeathDate().orElse(null));
      m.setDeathLocation(member.getDeathLocation().orElse(null));
    }
  }

  /**
   * Deletes a member from the family. Also removes associated wedding.
   * 
   * @param member the member to remove
   */
  public void removeMember(FamilyMember member) {
    for (Iterator<Wedding> it = this.weddings.iterator(); it.hasNext();) {
      Wedding wedding = it.next();
      if (wedding.isMarried(member))
        it.remove();
      else if (wedding.isChild(member))
        wedding.removeChild(member);
    }
    this.members.remove(member);
  }

  /**
   * @return all the weddings
   */
  public Set<Wedding> getAllWeddings() {
    return new HashSet<>(this.weddings);
  }

  /**
   * Gets the wedding for the given member.
   * 
   * @param member the member
   * @return the wedding or nothing if none were found
   */
  public Optional<Wedding> getWedding(FamilyMember member) {
    return this.weddings.stream().filter(wedding -> wedding.getSpouse1().equals(member) || wedding.getSpouse2().equals(member)).findAny();
  }

  /**
   * Adds a wedding.
   * 
   * @param wedding the new wedding
   */
  public void addWedding(Wedding wedding) {
    // TODO vérifier que les époux ne soient pas déjà mariés.
    if (!this.weddings.contains(wedding)) {
      // #f:0
      FamilyMember[] children = wedding.getChildren().stream()
          .map(child -> getMember(child.getId()).get())
          .toArray(FamilyMember[]::new);

      this.weddings.add(new Wedding(wedding.getDate().orElse(null), wedding.getLocation().orElse(null),
          getMember(wedding.getSpouse1().getId()).get(), getMember(wedding.getSpouse2().getId()).get(),
          children));
      // #f:1
    }
  }

  /**
   * Updates the given wedding.
   * 
   * @param wedding the wedding's new data
   */
  public void updateWedding(Wedding wedding) {
    Optional<Wedding> optional = getWedding(wedding.getSpouse1());

    if (optional.isPresent()) {
      Wedding w = optional.get();

      w.setDate(wedding.getDate().orElse(null));
      w.setLocation(wedding.getLocation().orElse(null));
      Set<FamilyMember> children = w.getChildren();
      children.forEach(child -> w.removeChild(child));
      wedding.getChildren().forEach(child -> w.addChild(Family.this.getMember(child.getId()).orElseThrow(IllegalStateException::new)));
    }
  }

  /**
   * Deletes a wedding.
   * 
   * @param wedding the wedding to delete
   */
  public void removeWedding(Wedding wedding) {
    this.weddings.remove(wedding);
  }

  /**
   * Tells if a member is married.
   * 
   * @param member the member
   * @return true if and only if the member is married
   */
  public boolean isMarried(FamilyMember member) {
    return this.weddings.stream().anyMatch(wedding -> wedding.isMarried(member));
  }

  /**
   * Returns all members that can be children of the given couple. If the argument is null, all
   * members are returned.
   * 
   * @param wedding the couple
   * @return a list of all potential children
   */
  public Set<FamilyMember> getPotentialChildren(Wedding wedding) {
    if (wedding == null)
      return getAllMembers();
    // #f:0
    return getAllMembers().stream()
        .filter(m -> !m.equals(wedding.getSpouse1()) && !m.equals(wedding.getSpouse2()) && !wedding.isChild(m))
        .collect(Collectors.toCollection(HashSet::new));
    // #f:1
  }

  /**
   * @return the global member ID
   */
  public long getGlobalId() {
    return this.globalId;
  }

  /**
   * @return the next member ID
   */
  private long getNextMemberId() {
    return this.globalId++;
  }

  @Override
  public String toString() {
    return getName() + this.members + "," + this.weddings;
  }
}