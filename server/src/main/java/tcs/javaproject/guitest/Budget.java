package tcs.javaproject.guitest;

import java.util.List;

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
}
