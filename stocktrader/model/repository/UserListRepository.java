package stocktrader.model.repository;

import stocktrader.model.entity.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class UserListRepository extends FileHandle {
    private final ArrayList<User> users = new ArrayList<>();

    public UserListRepository(String filename) {
        super(filename);
    }

    public ArrayList<User> GetUserList() {
        return users;
    }

    public boolean StoreUserList() {
        return true;
    }


    @Override
    public boolean loadFromFile(String filename) {
        try {
            BufferedReader readFile = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = readFile.readLine()) != null) {
                String[] userInfo = line.split(" ");
                users.add(new User(userInfo[0], userInfo[1]));
            }
            readFile.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
