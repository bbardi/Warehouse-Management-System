package dataaccess;

import connections.ConnectionFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class which provides implemented generic methods to access database tables.
 *
 * @param <T> the class for which the table is accessed
 */
public abstract class AbstractDatabaseAccessObject<T> {
  /** Contains the type of the generic class. */
  public final Class<T> type;

  /** Abstract super class method to initialize the type field. */
  @SuppressWarnings("unchecked")
  public AbstractDatabaseAccessObject() {
    this.type =
        (Class<T>)
            ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  private String createValuesString(T toBeAdded) {
    StringBuilder ret = new StringBuilder();
    boolean addComma = false;
    for (Field fields : type.getDeclaredFields()) {
      if (addComma) {
        ret.append(",");
      }
      fields.setAccessible(true);
      try {
        if (!fields.getName().toLowerCase().equals("id")) {
          ret.append("'");
          ret.append(fields.get(toBeAdded));
          ret.append("'");
          addComma = true;
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace(); // despite setting it to be accessible it still wants me to catch
        // exceptions
      }
    }
    return (ret.toString());
  }

  private String createFieldsString() {
    StringBuilder ret = new StringBuilder();
    boolean addComma = false;
    for (Field fields : type.getDeclaredFields()) {
      if (addComma) {
        ret.append(",");
      }
      if (!fields.getName().toLowerCase().equals("id")) {
        ret.append(fields.getName().toLowerCase());
        addComma = true;
      }
    }
    return (ret.toString());
  }

  private String createUpdateString(T toUpdate) {
    StringBuilder ret = new StringBuilder();
    boolean addComma = false;
    for (Field fields : type.getDeclaredFields()) {
      if (addComma) {
        ret.append(",");
      }
      fields.setAccessible(true);
      try {
        if (!fields.getName().toLowerCase().equals("id")) {
          ret.append(' ');
          ret.append(fields.getName().toLowerCase());
          ret.append("=");
          ret.append("'");
          ret.append(fields.get(toUpdate));
          ret.append("'");
          addComma = true;
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace(); // despite setting it to be accessible it still wants me to catch
        // exceptions
      }
    }
    return (ret.toString());
  }

  private void castResultSetIntoObject(T returnValue, ResultSet results)
      throws IllegalArgumentException, SQLException, IllegalAccessException {
    for (Field field : type.getDeclaredFields()) {
      field.setAccessible(true);
      String name = field.getName();
      Object value = results.getObject(name);
      Class<?> type = field.getType();
      if (isPrimitive(type)) {
        Class<?> boxed = boxPrimitiveClass(type);
        value = boxed.cast(value);
      }
      field.set(returnValue, value);
    }
  }

  private boolean isPrimitive(Class<?> type) {
    return (type == int.class
        || type == long.class
        || type == double.class
        || type == float.class
        || type == boolean.class
        || type == byte.class
        || type == char.class
        || type == short.class);
  }

  private Class<?> boxPrimitiveClass(Class<?> type) {
    if (type == int.class) {
      return Integer.class;
    } else if (type == long.class) {
      return Long.class;
    } else if (type == double.class) {
      return Double.class;
    } else if (type == float.class) {
      return Float.class;
    } else if (type == boolean.class) {
      return Boolean.class;
    } else if (type == byte.class) {
      return Byte.class;
    } else if (type == char.class) {
      return Character.class;
    } else if (type == short.class) {
      return Short.class;
    } else {
      String string = "class '" + type.getName() + "' is not a primitive";
      throw new IllegalArgumentException(string);
    }
  }

  /**
   * Adds an element to its corresponding database.
   *
   * @param toBeAdded element to be added
   */
  public void add(T toBeAdded) {
    StringBuilder insertString = new StringBuilder("INSERT INTO ");
    insertString.append(type.getSimpleName().toLowerCase());
    insertString.append(" (");
    insertString.append(createFieldsString());
    insertString.append(") VALUES (");
    insertString.append(createValuesString(toBeAdded));
    insertString.append(");");
    Connection dbConnection = ConnectionFactory.getConnection();
    PreparedStatement insertStatement;
    try {
      insertStatement = dbConnection.prepareStatement(insertString.toString());
      insertStatement.execute();
      ConnectionFactory.close(insertStatement);
      ConnectionFactory.close(dbConnection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Finds the id of the element named.
   *
   * @param name name of the element in the database
   * @return id of the element
   */
  public int findIDbyName(String name) {
    String getString =
        "SELECT id,deleted from " + type.getSimpleName().toLowerCase() + " WHERE name = ?";
    Connection dbConnection = ConnectionFactory.getConnection();
    PreparedStatement queryStatement = null;
    ResultSet results;
    int id = -1;
    try {
      queryStatement = dbConnection.prepareStatement(getString);
      queryStatement.setString(1, name);
      results = queryStatement.executeQuery();
      while (results.next()) {
        if (results.getInt("deleted") == 0) {
          id = results.getInt("id");
        }
      }
      ConnectionFactory.close(results);
      ConnectionFactory.close(queryStatement);
      ConnectionFactory.close(dbConnection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return id;
  }

  /**
   * Fetches the the generic type element based on its id.
   *
   * @param id id of the element
   * @return an instance of the element
   */
  public T getByID(int id) {
    String getString =
        "SELECT * from " + type.getSimpleName().toLowerCase() + " WHERE id = '" + id + "'";
    Connection dbConnection = ConnectionFactory.getConnection();
    PreparedStatement queryStatement = null;
    ResultSet results;
    T returnValue = null;
    try {
      returnValue = type.getDeclaredConstructor().newInstance();
      queryStatement = dbConnection.prepareStatement(getString);
      results = queryStatement.executeQuery();
      while (results.next()) {
        castResultSetIntoObject(returnValue, results);
      }
      ConnectionFactory.close(results);
      ConnectionFactory.close(queryStatement);
      ConnectionFactory.close(dbConnection);
    } catch (SQLException
        | NoSuchMethodException
        | IllegalAccessException
        | InstantiationException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
    return returnValue;
  }

  /**
   * Fetches all the elements from the database based on the Generic Type.
   *
   * @return list of elemnts
   */
  public List<T> getAll() {
    String getString = "SELECT * from " + type.getSimpleName().toLowerCase();
    Connection dbConnection = ConnectionFactory.getConnection();
    PreparedStatement queryStatement = null;
    ResultSet results;
    List<T> returnValue = new LinkedList<>();
    T elem;
    try {
      queryStatement = dbConnection.prepareStatement(getString);
      results = queryStatement.executeQuery();
      while (results.next()) {
        elem = type.getDeclaredConstructor().newInstance();
        castResultSetIntoObject(elem, results);
        returnValue.add(elem);
      }
      ConnectionFactory.close(results);
      ConnectionFactory.close(queryStatement);
      ConnectionFactory.close(dbConnection);
    } catch (SQLException
        | NoSuchMethodException
        | IllegalAccessException
        | InstantiationException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
    return returnValue;
  }

  /**
   * Updates an entry in the database.
   *
   * @param updatedValues the instance containing the updated values
   */
  public void update(T updatedValues) {
    String updateString =
        "UPDATE "
            + type.getSimpleName().toLowerCase()
            + " SET"
            + createUpdateString(updatedValues)
            + " WHERE id = ?";
    try {
      int id = (int) type.getMethod("getId").invoke(updatedValues);
      Connection dbConnection = ConnectionFactory.getConnection();
      PreparedStatement updateStatement;
      try {
        updateStatement = dbConnection.prepareStatement(updateString);
        updateStatement.setInt(1, id);
        updateStatement.execute();
        ConnectionFactory.close(updateStatement);
        ConnectionFactory.close(dbConnection);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  /*public void delete(int id) {
    String deleteString =
        "DELETE from " + type.getSimpleName().toLowerCase() + " WHERE id = '" + id + "'";
    Connection dbConnection = ConnectionFactory.getConnection();
    PreparedStatement queryStatement = null;
    try {
      queryStatement = dbConnection.prepareStatement(deleteString);
      queryStatement.execute();
      ConnectionFactory.close(queryStatement);
      ConnectionFactory.close(dbConnection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }*/
}
