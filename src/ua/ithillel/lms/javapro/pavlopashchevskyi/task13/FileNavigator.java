package ua.ithillel.lms.javapro.pavlopashchevskyi.task13;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileNavigator {

  private final String filesPath;

  private final Map<String, List<FileData>> files = new HashMap<>();

  public FileNavigator(String filesPath) throws IOException {
    File fileObject = new File(filesPath);
    this.filesPath = (fileObject.isFile()) ? fileObject.getParent() : fileObject.getCanonicalPath();
    this.synchronize(this.filesPath);
  }

  public boolean add(String path) throws IOException {
    if (path == null) {
      return false;
    }
    File fileObject = new File(this.filesPath, path);
    boolean directoryCreated = false;
    if (!fileObject.getParentFile().isDirectory()) {
      directoryCreated = fileObject.getParentFile().mkdir();
    }
    List<FileData> lfd = new ArrayList<>();
    FileData fd = new FileData(fileObject);
    if (this.files.containsKey(fd.getFilePath())) {
      if (this.files.get(fd.getFilePath()).contains(fd)) {
        return false;
      }
      this.files.get(fd.getFilePath()).add(fd);
      return directoryCreated & fileObject.createNewFile();
    }
    lfd.add(fd);
    this.files.putIfAbsent(fd.getFilePath(), lfd);
    return directoryCreated & fileObject.createNewFile();
  }

  public List<FileData> find(String path) throws IOException {
    List<FileData> emptyResult = new ArrayList<>();
    if (path == null) {
      return emptyResult;
    }
    File fileObject = new File(path);
    if (!fileObject.isDirectory() && !fileObject.isFile()) {
      return emptyResult;
    }
    String dirName = (fileObject.isDirectory()) ? fileObject.getCanonicalPath() :
        fileObject.getParentFile().getCanonicalPath();
    if (this.files.containsKey(dirName)) {
      return this.files.get(dirName);
    }
    return emptyResult;
  }

  public List<FileData> filterBySize(long size) {
    Collection<List<FileData>> filesFolder = this.files.values();
    List<FileData> filteredFilesData = new ArrayList<>();
    for (List<FileData> lfd : filesFolder) {
      for (FileData fd : lfd) {
        if (fd.getFileSizeInBytes() <= size) {
          filteredFilesData.add(fd);
        }
      }
    }
    return filteredFilesData;
  }

  public boolean remove(String path) throws IOException {
    boolean deletedFromFileSystem = false;
    boolean deletedFromList = false;
    if (path == null) {
      return false;
    }
    File fileObject = new File(this.filesPath, path);
    if (fileObject.isFile() || fileObject.isDirectory()) {
      deletedFromFileSystem = fileObject.delete();
    }

    FileData fd = new FileData(fileObject);
    if (this.files.containsKey(fd.getFilePath())) {
      if (this.files.get(fd.getFilePath()).contains(fd)) {
        deletedFromList = this.files.get(fd.getFilePath()).remove(fd);
      }
    }
    return deletedFromFileSystem && deletedFromList;
  }

  public List<FileData> sortBySize() {
    List<FileData> result = new ArrayList<>();
    for (String key : this.files.keySet()) {
      List<FileData> preResult = new ArrayList<>(this.files.get(key));
      result.addAll(preResult);
      result.sort(Comparator.comparing(FileData::getFileSizeInBytes));
    }
    return result;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (String key : this.files.keySet()) {
      sb.append("Files in ");
      sb.append(key);
      sb.append(" directory:\nFile path\tFile name\tSize in bytes\n");
      for (FileData fd : this.files.get(key)) {
        sb.append(fd.toString());
      }
    }
    return sb.toString();
  }

  private boolean synchronize(String path) throws IOException {
    if (path == null) {
      return false;
    }
    if (this.files.containsKey(path)) {
      List<FileData> filesToRemove = this.files.remove(path);
      for (FileData fdr : filesToRemove) {
        filesToRemove.remove(fdr);
      }
    }
    File fileObject = new File(path);
    List<FileData> lfd = new ArrayList<>();
    if (fileObject.isFile()) {
      FileData fd = new FileData(fileObject);
      lfd.add(fd);
      this.files.put(fileObject.getParent(), lfd);
    }
    if (fileObject.isDirectory()) {
      for (File fi : fileObject.listFiles()) {
        if (fi.isFile()) {
          FileData fd = new FileData(fi);
          lfd.add(fd);
        }
        if (fi.isDirectory()) {
          synchronize(fi.getPath());
        }
      }
      this.files.put(path, lfd);
    }
    return true;
  }
}
