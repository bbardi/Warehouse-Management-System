package connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates MySQL Database connections.
 */
public class ConnectionFactory {
  private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
  private static final String DBURL = "jdbc:mysql://localhost:3306/orderdb";
  private static final String USER = "root";
  private static final String PASS = "root";

  private static ConnectionFactory singleInstance = new ConnectionFactory();

  private ConnectionFactory() {
    try {
      Class.forName(DRIVER);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private Connection createConnection() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(DBURL, USER, PASS);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return conn;
  }

  public static Connection getConnection() {
    return singleInstance.createConnection();
  }

  /**
   * Closes a connection.
   * @param connection connection to be closed
   */
  public static void close(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Closes a statement.
   * @param statement statement to be closed
   */
  public static void close(Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Closes a result set.
   * @param results result set to be closed
   */
  public static void close(ResultSet results) {
    if (results != null) {
      try {
        results.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
