package net.storages.filesystem;

public interface IFileSystemItem {
    boolean isDirectory();

    boolean exists();

    String getName();

    String getPath();

}
