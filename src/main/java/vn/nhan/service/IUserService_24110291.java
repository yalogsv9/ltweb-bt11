package vn.nhan.service;

import java.util.List;
import vn.nhan.model.User_24110291;

public interface IUserService_24110291 {
    User_24110291 login(String username, String password);
    boolean existsUsername(String username);
    boolean existsEmail(String email);
    User_24110291 findByUsername(String username);
    List<User_24110291> findPage(int page, int size);
    int totalPages(int size);
    void insert(User_24110291 u);
    void update(User_24110291 u);
    void delete(String username);
}
