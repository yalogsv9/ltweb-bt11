package vn.nhan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vn.nhan.model.User_24110291;
import vn.nhan.util.DBConnection_24110291;

public class UserDAOImpl_24110291 implements IUserDAO_24110291 {

    private User_24110291 map(ResultSet rs) throws SQLException {
        User_24110291 u = new User_24110291();
        u.setUsername(rs.getString("Username"));
        u.setPassword(rs.getString("Password"));
        u.setPhone(rs.getString("Phone"));
        u.setFullname(rs.getString("Fullname"));
        u.setEmail(rs.getString("Email"));
        u.setAdmin(rs.getBoolean("Admin"));
        u.setActive(rs.getBoolean("Active"));
        u.setImages(rs.getString("Images"));
        return u;
    }

    private User_24110291 findOne(String sql, String... args) {
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) ps.setString(i + 1, args[i]);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User_24110291 findByUsername(String username) {
        return findOne("SELECT * FROM Users WHERE Username = ?", username);
    }

    @Override
    public User_24110291 findByEmail(String email) {
        return findOne("SELECT * FROM Users WHERE Email = ?", email);
    }

    @Override
    public User_24110291 login(String username, String password) {
        return findOne("SELECT * FROM Users WHERE Username = ? AND Password = ?", username, password);
    }

    @Override
    public List<User_24110291> findPage(int page, int size) {
        String sql = "SELECT * FROM Users ORDER BY Username OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<User_24110291> list = new ArrayList<>();
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, (page - 1) * size);
            ps.setInt(2, size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public int count() {
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM Users");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(User_24110291 u) {
        String sql = "INSERT INTO Users (Username, Password, Phone, Fullname, Email, Admin, Active, Images) "
                + "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getPhone());
            ps.setString(4, u.getFullname());
            ps.setString(5, u.getEmail());
            ps.setBoolean(6, u.isAdmin());
            ps.setBoolean(7, u.isActive());
            ps.setString(8, u.getImages());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User_24110291 u) {
        String sql = "UPDATE Users SET Password=?, Phone=?, Fullname=?, Email=?, Admin=?, Active=?, Images=? "
                + "WHERE Username=?";
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getPassword());
            ps.setString(2, u.getPhone());
            ps.setString(3, u.getFullname());
            ps.setString(4, u.getEmail());
            ps.setBoolean(5, u.isAdmin());
            ps.setBoolean(6, u.isActive());
            ps.setString(7, u.getImages());
            ps.setString(8, u.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String username) {
        try (Connection c = DBConnection_24110291.getConnection()) {
            c.setAutoCommit(false);
            try {
                for (String sql : new String[]{"DELETE FROM Favorites WHERE Username=?",
                        "DELETE FROM Shares WHERE Username=?", "DELETE FROM Users WHERE Username=?"}) {
                    try (PreparedStatement ps = c.prepareStatement(sql)) {
                        ps.setString(1, username);
                        ps.executeUpdate();
                    }
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
