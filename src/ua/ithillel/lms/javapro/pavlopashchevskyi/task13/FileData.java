package ua.ithillel.lms.javapro.pavlopashchevskyi.task13;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FileData {

  private final String fileName;
  private final long fileSizeInBytes;
  private final String filePath;

  public FileData(File fileObject) throws IOException {
    this.fileName = fileObject.getName();
    this.fileSizeInBytes = fileObject.length();
    this.filePath = fileObject.getParentFile().getCanonicalPath();
  }

  public long getFileSizeInBytes() {
    return this.fileSizeInBytes;
  }

  public String getFilePath() {
    return this.filePath;
  }

  public String getFileName() {
    return this.fileName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FileData fileData = (FileData) o;
    return fileSizeInBytes == fileData.fileSizeInBytes && Objects.equals(fileName,
        fileData.fileName) && Objects.equals(filePath, fileData.filePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileName, fileSizeInBytes, filePath);
  }

  public String toString() {
    String result = "";

    result += getFilePath();
    result += "\t";
    result += getFileName();
    result += "\t";
    result += getFileSizeInBytes();
    result += "\n";

    return result;
  }

}
