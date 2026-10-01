package vn.nhan.dao;

import java.util.List;
import vn.nhan.model.User_24110291;

public interface IUserDAO_24110291 {
    User_24110291 findByUsername(String username);
    User_24110291 findByEmail(String email);
    User_24110291 login(String username, String password);
    List<User_24110291> findPage(int page, int size);
    int count();
    void insert(User_24110291 u);
    void update(User_24110291 u);
    void delete(String username);
}
