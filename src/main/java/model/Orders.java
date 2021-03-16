package model;

/**
 * Class to handle the orders.
 */
public class Orders {
  private int id;
  private int clientId;
  private int productId;
  private int amount;

  /** Constructs an empty order. */
  public Orders() {
    this.id = -1;
    this.clientId = -1;
    this.productId = -1;
    this.amount = -1;
  }

  /**
   * Constructs an order.
   *
   * @param clientId id of client
   * @param productId id of product
   * @param amount amount of product
   */
  public Orders(int clientId, int productId, int amount) {
    this.id = -1;
    this.clientId = clientId;
    this.productId = productId;
    this.amount = amount;
  }

  public int getOrderId() {
    return id;
  }

  public int getClientId() {
    return clientId;
  }

  public int getProductId() {
    return productId;
  }

  public int getAmount() {
    return amount;
  }
}
