package tcs.javaproject.guitest;

import java.util.Objects;

public class User {

    private int id;
    private String name;
    private String email;
    private String bankAccount;

    public User(int id, String name, String email, String bankAccount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bankAccount = bankAccount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getBankAccount() {
        return bankAccount;
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
                       "id=" + id +
                       ", name='" + name + '\'' +
                       ", email='" + email + '\'' +
                       ", bankAccount='" + bankAccount + '\'' +
                       '}';
    }

}