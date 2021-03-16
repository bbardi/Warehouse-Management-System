package business;

import dataaccess.CitiesDatabaseAccessObject;
import dataaccess.ClientDatabaseAccessObject;
import dataaccess.OrdersDatabaseAccessObject;
import dataaccess.ProductDatabaseAccessObject;
import java.util.List;
import model.City;
import model.Clients;
import model.Orders;
import model.Product;

/**
 * Manages the DAOs and provides method to perform CRUD operations to the Manager.
 */
public class WarehouseManager {
  private CitiesDatabaseAccessObject citiesDatabaseAccessObject;
  private ClientDatabaseAccessObject clientDatabaseAccessObject;
  private OrdersDatabaseAccessObject ordersDatabaseAccessObject;
  private ProductDatabaseAccessObject productDatabaseAccessObject;

  /** Initializes all the DAOs necessary for the application to interact with the database. */
  public WarehouseManager() {
    citiesDatabaseAccessObject = new CitiesDatabaseAccessObject();
    clientDatabaseAccessObject = new ClientDatabaseAccessObject();
    ordersDatabaseAccessObject = new OrdersDatabaseAccessObject();
    productDatabaseAccessObject = new ProductDatabaseAccessObject();
  }

  public List<City> getCityList() {
    return citiesDatabaseAccessObject.getAll();
  }

  public List<Clients> getClientsList() {
    return clientDatabaseAccessObject.getAll();
  }

  public List<Orders> getOrdersList() {
    return ordersDatabaseAccessObject.getAll();
  }

  public List<Product> getProductList() {
    return productDatabaseAccessObject.getAll();
  }

  public Product getByID(int id) {
    return productDatabaseAccessObject.getByID(id);
  }

  public void update(Product productToBeUpdated) {
    productDatabaseAccessObject.update(productToBeUpdated);
  }

  /**
   * Gets the id of an entry based on its name.
   *
   * @param type type of entry
   * @param name name of the entry
   * @return id of the element
   */
  public int getID(ElemType type, String name) {
    switch (type) {
      case City:
        return citiesDatabaseAccessObject.findIDbyName(name);
      case Client:
        return clientDatabaseAccessObject.findIDbyName(name);
      case Product:
        return productDatabaseAccessObject.findIDbyName(name);
      default:
        return -1;
    }
  }

  public void add(Product productToBeAdded) {
    productDatabaseAccessObject.add(productToBeAdded);
  }

  public void add(Orders orderToBeAdded) {
    ordersDatabaseAccessObject.add(orderToBeAdded);
  }

  public void add(City cityToBeAdded) {
    citiesDatabaseAccessObject.add(cityToBeAdded);
  }

  public void add(Clients clientToBeAdded) {
    clientDatabaseAccessObject.add(clientToBeAdded);
  }

  /**
   * Deletes an element from the database.
   *
   * @param type type of elemnt
   * @param id id of the entry
   */
  public void delete(ElemType type, int id) {
    switch (type) {
      case Client:
        Clients deletedClient = clientDatabaseAccessObject.getByID(id);
        deletedClient.setDeleted(1);
        clientDatabaseAccessObject.update(deletedClient);
        break;
      case Product:
        Product deletedProduct = productDatabaseAccessObject.getByID(id);
        deletedProduct.setStock(0);
        deletedProduct.setDeleted(1);
        productDatabaseAccessObject.update(deletedProduct);
        break;
      default:
        break;
    }
  }
}
