package model;

/**
 * Class to handle the cities.
 */
public class City {
  private int id;
  private String name;

  public City() {
    id = -1;
    name = "";
  }

  public City(String city) {
    this.name = city;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
