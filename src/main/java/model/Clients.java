package model;

/**
 * Class to handle the clients.
 */
public class Clients {
  private int id;
  private String name;
  private int cityID;
  private int deleted;

  /**
   * Constructs an empty element.
   */
  public Clients() {
    this.id = -1;
    this.name = "no name";
    this.cityID = -1;
    this.deleted = 0;
  }

  /**
   * Constructs a new Client.
   * @param name Name of client
   * @param cityID id of its city
   */
  public Clients(String name, int cityID) {
    this.id = -1;
    this.name = name;
    this.cityID = cityID;
    this.deleted = 0;
  }

  public int getCityID() {
    return cityID;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int isDeleted() {
    return deleted;
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }

  @Override
  public String toString() {
    return (id + " " + name + " " + cityID);
  }
}
