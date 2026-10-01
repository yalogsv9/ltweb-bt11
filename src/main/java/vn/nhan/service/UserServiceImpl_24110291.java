package vn.nhan.service;

import java.util.List;
import vn.nhan.dao.IUserDAO_24110291;
import vn.nhan.dao.UserDAOImpl_24110291;
import vn.nhan.model.User_24110291;

public class UserServiceImpl_24110291 implements IUserService_24110291 {
    private final IUserDAO_24110291 dao = new UserDAOImpl_24110291();

    @Override
    public User_24110291 login(String username, String password) {
        User_24110291 u = dao.login(username, password);
        return (u != null && u.isActive()) ? u : null;
    }

    @Override public boolean existsUsername(String username) { return dao.findByUsername(username) != null; }
    @Override public boolean existsEmail(String email) { return dao.findByEmail(email) != null; }
    @Override public User_24110291 findByUsername(String username) { return dao.findByUsername(username); }
    @Override public List<User_24110291> findPage(int page, int size) { return dao.findPage(page, size); }

    @Override
    public int totalPages(int size) {
        return (int) Math.ceil(dao.count() / (double) size);
    }

    @Override public void insert(User_24110291 u) { dao.insert(u); }
    @Override public void update(User_24110291 u) { dao.update(u); }
    @Override public void delete(String username) { dao.delete(username); }
}
