package business;

import static business.ElemType.Client;

import model.City;
import model.Clients;
import model.Orders;
import model.Product;

/** Provides static methods to process the commands from the inputFile. */
public class CommandProcess {
  private static WarehouseManager manager = new WarehouseManager();

  /**
   * Insert a new Client.
   *
   * @param name Name of the Client
   * @param city Name of the city
   */
  public static void insert(String name, String city) {
    int cityID = manager.getID(ElemType.City, city);
    if (cityID == -1) {
      manager.add(new City(city));
      cityID = manager.getID(ElemType.City, city);
    }
    manager.add(new Clients(name, cityID));
  }

  /**
   * Inserts a new Product.
   *
   * @param name name of the product
   * @param amount amount of product
   * @param price price of product
   */
  public static void insert(String name, String amount, String price) {
    int productID = manager.getID(ElemType.Product, name);
    if (productID != -1) {
      Product productToBeUpdated = manager.getByID(productID);
      productToBeUpdated.setStock(Integer.parseInt(amount) + productToBeUpdated.getStock());
      manager.update(productToBeUpdated);
    } else {
      manager.add(new Product(name, Integer.parseInt(amount), Float.parseFloat(price)));
    }
  }

  /**
   * Deletes an elemnt from the list.
   *
   * @param type type of element to be deleted from the database
   * @param name name of the element
   */
  public static void delete(ElemType type, String name) {
    String[] splitName = name.split(", ");
    int idToBeDeleted = manager.getID(type, splitName[0]);
    if (idToBeDeleted != -1) {
      manager.delete(type, idToBeDeleted);
    }
  }

  /**
   * Creates a new order.
   *
   * @param name name of the client
   * @param productName name of the product
   * @param amount amount of product bought
   */
  public static void order(String name, String productName, String amount) {
    int clientID = manager.getID(Client, name);
    int productID = manager.getID(ElemType.Product, productName);
    if (clientID == -1) {
      ReportConstructor.generateErrorReport("Non-existent Client");
    } else if (productID == -1) {
      ReportConstructor.generateErrorReport("Non-existent Product");
    } else {
      Product productSold = manager.getByID(productID);
      if (productSold.getStock() < Integer.parseInt(amount)) {
        ReportConstructor.generateErrorReport("Out of Stock:" + productName);
      } else {
        manager.add(new Orders(clientID, productID, Integer.parseInt(amount)));
        productSold.setStock(productSold.getStock() - Integer.parseInt(amount));
        manager.update(productSold);
        ReportConstructor.generateInvoice(
            new Orders(clientID, productID, Integer.parseInt(amount)),
            manager.getClientsList(),
            manager.getCityList(),
            manager.getProductList());
      }
    }
  }

  /**
   * Generates a report.
   *
   * @param reportToGenerate a string containing which report to generate
   */
  public static void report(String reportToGenerate) {
    switch (reportToGenerate) {
      case "client":
        ReportConstructor.generateReport(manager.getClientsList(), manager.getCityList());
        break;
      case "order":
        ReportConstructor.generateReport(
            manager.getOrdersList(),
            manager.getClientsList(),
            manager.getCityList(),
            manager.getProductList());
        break;
      case "product":
        ReportConstructor.generateReport(manager.getProductList());
        break;
      default:
        throw new IllegalArgumentException("Invalid Report Command\n");
    }
  }
}
