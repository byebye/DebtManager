package tcs.javaproject.guitest;

/**
 * Created by Vsmasster on 29.04.15.
 */
public class Budget {
    private final String name;
    private final String description;
    private final int partNum;

    public Budget(String _name,String _description,int _partNum){
        this.name = _name;
        this.description = _description;
        this.partNum = _partNum;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public int getPartNum(){
        return partNum;
    }

}
