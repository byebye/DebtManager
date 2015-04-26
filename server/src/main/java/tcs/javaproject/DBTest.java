package tcs.javaproject;

import static org.jooq.impl.DSL.*;

import java.sql.*;

import org.jooq.*;
import org.jooq.impl.*;
import tcs.javaproject.database.tables.*;

/**
 * Created by byebye on 26.04.15.
 */
public class DBTest {

   public static void main(String[] args) {
      String userName = "debtmanager";
      String password = "debtmanager";
      String url = "jdbc:postgresql://localhost/debtmanager";

      // Connection is the only JDBC resource that we need
      // PreparedStatement and ResultSet are handled by jOOQ, internally
      try(Connection conn = DriverManager.getConnection(url, userName, password)) {
         DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
         Result<Record> result = create.select().from(Users.USERS).fetch();

         for (Record r : result) {
            Integer id = r.getValue(Users.USERS.ID);
            String email = r.getValue(Users.USERS.EMAIL);
            String name = r.getValue(Users.USERS.NAME);

            System.out.println("ID: " + id + " name: " + name + " email: " + email);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

}
