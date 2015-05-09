package tcs.javaproject.guitest;

import java.util.List;

public class Budget {
   private final int id;
   private final String name;
   private final String description;
   private List<User> participants;

   public Budget(int id, String name, String description, List<User> participants) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.participants = participants;
   }

   public int getId() {
      return id;
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
