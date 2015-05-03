package tcs.javaproject.guitest;

public class Budget {
   private final String name;
   private final String description;
   private final int partNum;

   public Budget(String name, String description, int partNum) {
      this.name = name;
      this.description = description;
      this.partNum = partNum;
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
