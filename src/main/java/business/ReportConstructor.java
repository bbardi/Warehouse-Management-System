package business;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.City;
import model.Clients;
import model.Orders;
import model.Product;

/** Creates the PDF Reports. */
public class ReportConstructor {
  private static int reportClientsCount = 1;
  private static int reportProductsCount = 1;
  private static int reportOrdersCount = 1;
  private static int invoicesCount = 1;

  private static Clients getClient(int clientId, List<Clients> clientsList) {
    for (Clients currentClient : clientsList) {
      if (currentClient.getId() == clientId) {
        return currentClient;
      }
    }
    return null;
  }

  private static String getCityName(int cityId, List<City> cityList) {
    for (City currentCity : cityList) {
      if (currentCity.getId() == cityId) {
        return currentCity.getName();
      }
    }
    return "Unknown";
  }

  private static Product getProduct(int productID, List<Product> productList) {
    for (Product currentProduct : productList) {
      if (currentProduct.getId() == productID) {
        return currentProduct;
      }
    }
    return null;
  }

  private static PdfPTable createTable(List<Clients> clientsList, List<City> cityList) {
    PdfPTable table = new PdfPTable(3);
    table.addCell("ID");
    table.addCell("Name");
    table.addCell("City");
    for (Clients currentClient : clientsList) {
      if (currentClient.isDeleted() == 0) {
        table.addCell(Integer.toString(currentClient.getId()));
        table.addCell(currentClient.getName());
        table.addCell(getCityName(currentClient.getCityID(), cityList));
      }
    }
    return table;
  }

  private static PdfPTable createTable(List<Product> productList) {
    PdfPTable table = new PdfPTable(4);
    table.addCell("ID");
    table.addCell("Name");
    table.addCell("Amount");
    table.addCell("Price");
    for (Product currentProduct : productList) {
      if (currentProduct.isDeleted() == 0) {
        table.addCell(Integer.toString(currentProduct.getId()));
        table.addCell(currentProduct.getName());
        table.addCell(Integer.toString(currentProduct.getStock()));
        table.addCell(Float.toString(currentProduct.getPrice()));
      }
    }
    return table;
  }

  private static PdfPTable createTable(
      List<Orders> ordersList,
      List<Clients> clientsList,
      List<City> cityList,
      List<Product> productList) {
    PdfPTable table = new PdfPTable(6);
    table.addCell("ID");
    table.addCell("Name");
    table.addCell("Address");
    table.addCell("Ordered Product");
    table.addCell("Amount");
    table.addCell("Total paid");
    for (Orders currentOrder : ordersList) {
      table.addCell(Integer.toString(currentOrder.getOrderId()));
      Clients client = getClient(currentOrder.getClientId(), clientsList);
      Product product = getProduct(currentOrder.getProductId(), productList);
      if (client == null || product == null) {
        continue;
      }
      table.addCell(client.getName());
      table.addCell(getCityName(client.getCityID(), cityList));
      table.addCell(product.getName());
      table.addCell(Integer.toString(currentOrder.getAmount()));
      table.addCell(Float.toString(currentOrder.getAmount() * product.getPrice()));
    }
    return table;
  }

  private static PdfPTable createTable(
      Orders currentOrder,
      List<Clients> clientsList,
      List<City> cityList,
      List<Product> productList) {
    PdfPTable table = new PdfPTable(5);
    table.addCell("Name");
    table.addCell("Address");
    table.addCell("Ordered Product");
    table.addCell("Amount");
    table.addCell("Total paid");
    Clients client = getClient(currentOrder.getClientId(), clientsList);
    Product product = getProduct(currentOrder.getProductId(), productList);
    if (client == null || product == null) {
      throw new IllegalArgumentException("Invalid client or product");
    }
    table.addCell(client.getName());
    table.addCell(getCityName(client.getCityID(), cityList));
    table.addCell(product.getName());
    table.addCell(Integer.toString(currentOrder.getAmount()));
    table.addCell(Float.toString(currentOrder.getAmount() * product.getPrice()));
    return table;
  }

  /**
   * Generates a Client report.
   *
   * @param clientsList List of Clients
   * @param cityList List of Cities
   */
  public static void generateReport(List<Clients> clientsList, List<City> cityList) {
    Document report = new Document();
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH-mm-ss-dd-MM-yyyy");
    Date date = new Date(System.currentTimeMillis());
    try {
      PdfWriter.getInstance(
          report,
          new FileOutputStream(
              "ClientReport-" + reportClientsCount + "-" + dateFormat.format(date) + ".pdf"));
      report.open();
      report.add(createTable(clientsList, cityList));
      report.close();
      reportClientsCount++;
    } catch (FileNotFoundException | DocumentException e) {
      e.printStackTrace();
    }
  }

  /**
   * Generates a product report.
   *
   * @param productList List of Product
   */
  public static void generateReport(List<Product> productList) {
    Document report = new Document();
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH-mm-ss-dd-MM-yyyy");
    Date date = new Date(System.currentTimeMillis());
    try {
      PdfWriter.getInstance(
          report,
          new FileOutputStream(
              "ProductsReport-" + reportProductsCount + "-" + dateFormat.format(date) + ".pdf"));
      report.open();
      report.add(createTable(productList));
      report.close();
      reportProductsCount++;
    } catch (FileNotFoundException | DocumentException e) {
      e.printStackTrace();
    }
  }

  /**
   * Generates an Order report.
   *
   * @param ordersList List of orders
   * @param clientsList List of clients
   * @param cityList List of cities
   * @param productList List of products
   */
  public static void generateReport(
      List<Orders> ordersList,
      List<Clients> clientsList,
      List<City> cityList,
      List<Product> productList) {
    Document report = new Document();
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH-mm-ss-dd-MM-yyyy");
    Date date = new Date(System.currentTimeMillis());
    try {
      PdfWriter.getInstance(
          report,
          new FileOutputStream(
              "OrdersReport-" + reportOrdersCount + "-" + dateFormat.format(date) + ".pdf"));
      report.open();
      report.add(createTable(ordersList, clientsList, cityList, productList));
      report.close();
      reportOrdersCount++;
    } catch (FileNotFoundException | DocumentException e) {
      e.printStackTrace();
    }
  }

  /**
   * Generates an invoice.
   *
   * @param order Order details
   * @param clientsList List of clients
   * @param cityList List of cities
   * @param productList List of products
   */
  public static void generateInvoice(
      Orders order, List<Clients> clientsList, List<City> cityList, List<Product> productList) {
    Document report = new Document();
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH-mm-ss-dd-MM-yyyy");
    Date date = new Date(System.currentTimeMillis());
    try {
      PdfWriter.getInstance(
          report,
          new FileOutputStream(
              "Invoice-" + invoicesCount + "-" + dateFormat.format(date) + ".pdf"));
      report.open();
      report.add(createTable(order, clientsList, cityList, productList));
      report.close();
      invoicesCount++;
    } catch (FileNotFoundException | DocumentException e) {
      e.printStackTrace();
    }
  }

  /**
   * Generates an Error Report.
   *
   * @param errorMessage error message
   */
  public static void generateErrorReport(String errorMessage) {
    Document report = new Document();
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH-mm-ss-dd-MM-yyyy");
    Date date = new Date(System.currentTimeMillis());
    try {
      PdfWriter.getInstance(
          report, new FileOutputStream("ErrorMessage-" + dateFormat.format(date) + ".pdf"));
      report.open();
      report.add(new Paragraph(errorMessage));
      report.close();
    } catch (FileNotFoundException | DocumentException e) {
      e.printStackTrace();
    }
  }
}
