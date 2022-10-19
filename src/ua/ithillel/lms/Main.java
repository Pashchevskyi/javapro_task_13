package ua.ithillel.lms;

import java.io.IOException;
import java.util.List;
import ua.ithillel.lms.javapro.pavlopashchevskyi.task13.FileData;
import ua.ithillel.lms.javapro.pavlopashchevskyi.task13.FileNavigator;

public class Main {

  public static void main(String[] args) {
    try {
      FileNavigator fn = new FileNavigator("./files");

      fn.add("test3.txt");
      fn.add("subfiles/subtest1.txt");
      fn.add("subfiles/subtest2.txt");
      System.out.println("After adding:");
      System.out.println(fn);

      System.out.println("After finding:");
      List<FileData> foundFiles = fn.find("./files/subfiles");
      for (FileData foundFile : foundFiles) {
        System.out.println(foundFile);
      }

      List<FileData> filteredFiles = fn.filterBySize(32);
      System.out.println("After filtering:");
      for (FileData filteredFile : filteredFiles) {
        System.out.println(filteredFile);
      }

      fn.remove("dopontanus.txt");
      System.out.println("After removing");
      System.out.println(fn);
      fn.remove("subfiles/dopontanus.txt");
      System.out.println("After removing from subfiles");
      System.out.println(fn);

      System.out.println("After sorting");
      List<FileData> sortedFiles = fn.sortBySize();
      for (FileData sortedFile : sortedFiles) {
        System.out.println(sortedFile);
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}