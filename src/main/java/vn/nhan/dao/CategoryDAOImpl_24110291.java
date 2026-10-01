package vn.nhan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import vn.nhan.model.Category_24110291;
import vn.nhan.util.DBConnection_24110291;

public class CategoryDAOImpl_24110291 implements ICategoryDAO_24110291 {

    private Category_24110291 map(ResultSet rs) throws SQLException {
        Category_24110291 c = new Category_24110291();
        c.setCategoryId(rs.getInt("CategoryId"));
        c.setCategoryname(rs.getString("Categoryname"));
        c.setCategorycode(rs.getString("Categorycode"));
        c.setImages(rs.getString("Images"));
        c.setStatus(rs.getBoolean("Status"));
        return c;
    }

    @Override
    public List<Category_24110291> findAllWithVideoCount() {
        String sql = "SELECT c.*, COUNT(v.VideoId) AS VideoCount FROM Category c "
                + "LEFT JOIN Videos v ON v.CategoryId = c.CategoryId "
                + "GROUP BY c.CategoryId, c.Categoryname, c.Categorycode, c.Images, c.Status "
                + "ORDER BY c.CategoryId";
        List<Category_24110291> list = new ArrayList<>();
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category_24110291 cat = map(rs);
                cat.setVideoCount(rs.getInt("VideoCount"));
                list.add(cat);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Category_24110291 findById(int id) {
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM Category WHERE CategoryId = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
