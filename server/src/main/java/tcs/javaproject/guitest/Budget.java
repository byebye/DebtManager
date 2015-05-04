package tcs.javaproject.guitest;

public class Budget {
   private final int id;
   private final String name;
   private final String description;
   private final int partNum;

   public Budget(int id, String name, String description, int partNum) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.partNum = partNum;
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
      return partNum;
   }

}
