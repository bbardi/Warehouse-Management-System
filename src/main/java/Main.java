import presentation.FileReader;

public class Main {
  /**
   * Main procedure of the application.
   *
   * @param args arguments of the application
   */
  public static void main(String[] args) {
    try {
      FileReader reader = new FileReader(args[0]);
      reader.start();
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Not enough arguments");
    }
  }
}
