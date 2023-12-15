package stocktrader.model.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileHandle {
    private String filename;

    public void Close() {

    }

    public FileHandle(String filename) {
        this.filename = filename;
    }

    public boolean loadFromFile(String filename) {
        return true;
    }

}
