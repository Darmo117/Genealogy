package net.darmo_creations.model.family;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Family {
  private long globalId;
  private String name;
  private Set<FamilyMember> members;
  private Set<Wedding> weddings;

  public Family(String name) {
    this(0, name, new HashSet<>(), new HashSet<>());
  }

  public Family(long globalId, String name, Set<FamilyMember> members, Set<Wedding> weddings) {
    this.globalId = globalId;
    this.name = name;
    this.members = members;
    this.weddings = weddings;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<FamilyMember> getAllMembers() {
    return new HashSet<>(this.members);
  }

  public Optional<FamilyMember> getMember(long id) {
    return this.members.stream().filter(member -> member.getId() == id).findAny();
  }

  public void addMember(FamilyMember member) {
    if (!this.members.contains(member)) {
      this.members.add(member.copy(getNextMemberId()));
    }
  }

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

  public Set<Wedding> getAllWeddings() {
    return new HashSet<>(this.weddings);
  }

  public Optional<Wedding> getWedding(FamilyMember member) {
    return this.weddings.stream().filter(wedding -> wedding.getSpouse1().equals(member) || wedding.getSpouse2().equals(member)).findAny();
  }

  public void addWedding(Wedding wedding) {
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

  public void removeWedding(Wedding wedding) {
    this.weddings.remove(wedding);
  }

  public boolean isMarried(FamilyMember member) {
    return this.weddings.stream().anyMatch(wedding -> wedding.isMarried(member));
  }

  /**
   * Retourne toutes les personnes éligibles pour être les enfants du couple donné. Si l'argument
   * est null, tous les membres de la famille sont retournés.
   * 
   * @param wedding le mariage
   * @return la liste des enfants potentiels
   */
  public Set<FamilyMember> getPotentialChildren(Wedding wedding) {
    if (wedding == null)
      return getAllMembers();
    return getAllMembers().stream().filter(
        m -> !m.equals(wedding.getSpouse1()) && !m.equals(wedding.getSpouse2()) && !wedding.isChild(m)).collect(
            Collectors.toCollection(HashSet::new));
  }

  public long getGlobalId() {
    return this.globalId;
  }

  private long getNextMemberId() {
    return this.globalId++;
  }

  @Override
  public String toString() {
    return getName() + this.members + "," + this.weddings;
  }
}