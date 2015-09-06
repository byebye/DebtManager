package common.data;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

  public final static int MAX_NAME_LENGTH = 60;

  private final int id;
  private final String name;
  private final Email email;
  private final BankAccount bankAccount;
  private double spentMoney = 0;

  public User(int id, String name, String email, String bankAccount) {
    this(id, name, new Email(email), new BankAccount(bankAccount));
  }

  public User(int id, String name, Email email, BankAccount bankAccount) {
    this.bankAccount = bankAccount;
    this.id = id;
    this.name = name;
    this.email = email;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Email getEmail() {
    return email;
  }

  public BankAccount getBankAccount() {
    return bankAccount;
  }

  public double getSpentMoney() {
    return spentMoney;
  }

  public void setSpentMoney(double spentMoney) {
    this.spentMoney = spentMoney;
  }

  public void addSpentMoney(double spentMoney) {
    this.spentMoney += spentMoney;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "User{" +
           "name='" + name + '\'' +
           ", email='" + email + '\'' +
           '}';
  }
}