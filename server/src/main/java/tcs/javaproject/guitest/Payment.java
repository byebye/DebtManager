package tcs.javaproject.guitest;

/**
 * Created by krzysiek on 07.05.15.
 */
public class Payment {
    private String who, what;
    private int amount;

    public Payment(String who, String what, int amount){
        this.who = who;
        this.what = what;
        this.amount = amount;
    }

    public String getWho(){
        return who;
    }

    public String getWhat(){
        return what;
    }

    public int getAmount(){
        return amount;
    }
}
