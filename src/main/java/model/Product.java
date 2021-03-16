package model;

/**
 * Class to handle the products.
 */
public class Product {
  private int id;
  private String name;
  private int stock;
  private Number price;
  private int deleted;

  /** Creates an empty product. */
  public Product() {
    this.id = -1;
    this.name = "";
    this.stock = -1;
    this.price = -1;
    this.deleted = 0;
  }

  /**
   * Creates a product.
   *
   * @param name name of product
   * @param stock amount of product in stock
   * @param price price of product
   */
  public Product(String name, int stock, float price) {
    this.id = -1;
    this.name = name;
    this.stock = stock;
    this.price = price;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getStock() {
    return stock;
  }

  public float getPrice() {
    return price.floatValue();
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }

  public int isDeleted() {
    return deleted;
  }
}
