package presentation;

import business.CommandProcess;
import business.ElemType;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Reads and processes the input file.
 */
public class FileReader {
  Scanner fileScanner;

  /**
   * Creates a new file reader.
   * @param fileName name of the file/path of the file to be read
   */
  public FileReader(String fileName) {
    try {
      fileScanner = new Scanner(new File(fileName));
    } catch (FileNotFoundException e) {
      System.out.println("File not found\n");
    }
  }

  private void processOrder(String[] splitCommand) {
    String[] splitArgs = splitCommand[1].split(", ");
    CommandProcess.order(splitArgs[0], splitArgs[1], splitArgs[2]);
  }

  private void processDelete(String[] splitCommand) {
    if (splitCommand[0].equalsIgnoreCase("delete client")) {
      CommandProcess.delete(ElemType.Client, splitCommand[1]);
    } else if (splitCommand[0].equalsIgnoreCase("delete product")) {
      CommandProcess.delete(ElemType.Product, splitCommand[1]);
    } else {
      throw new IllegalArgumentException("Invalid Delete command\n");
    }
  }

  private void processInsert(String[] splitCommand) {
    String[] splitArgs = splitCommand[1].split(", ");
    if (splitCommand[0].equalsIgnoreCase("insert client")) {
      CommandProcess.insert(splitArgs[0], splitArgs[1]);
    } else if (splitCommand[0].equalsIgnoreCase("insert product")) {
      CommandProcess.insert(splitArgs[0], splitArgs[1], splitArgs[2]);
    } else {
      throw new IllegalArgumentException("Invalid Insert command\n");
    }
  }

  /**
   * Starts the reading and processing of the input file.
   */
  public void start() {
    while (fileScanner.hasNextLine()) {
      String lineRead = fileScanner.nextLine();
      String[] splitCommand = lineRead.split(": ");
      try {
        if (splitCommand.length == 1) {
          String[] spaceSplit = splitCommand[0].split(" ");
          CommandProcess.report(spaceSplit[1]);
        } else {
          if (splitCommand[0].startsWith("Insert")) {
            processInsert(splitCommand);
          }
          if (splitCommand[0].startsWith("Delete")) {
            processDelete(splitCommand);
          }
          if (splitCommand[0].startsWith("Order")) {
            processOrder(splitCommand);
          }
        }
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      } catch (ArrayIndexOutOfBoundsException e) {
        System.out.println("Invalid command\n");
      }
    }
  }
}
