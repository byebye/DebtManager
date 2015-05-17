package common;

import java.util.List;
import java.util.Objects;

public class Budget {
   private final int id;
   private final User owner;
   private final String name;
   private final String description;
   private List<User> participants;

   public Budget(User owner, String name, String description, List<User> participants) {
      this(-1, owner, name, description, participants);
   }

   public Budget(int id, User owner, String name, String description, List<User> participants) {
      this.id = id;
      this.owner = owner;
      this.name = name;
      this.description = description;
      this.participants = participants;
   }

   public int getId() {
      return id;
   }

   public User getOwner() {
      return owner;
   }

   public String getName() {
      return name;
   }

   public String getDescription() {
      return description;
   }

   public int getPartNum() {
      return participants == null ? 0 : participants.size();
   }

   public List<User> getParticipants() {
      return participants;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Budget budget = (Budget) o;
      return Objects.equals(id, budget.id);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }
}
