package vn.nhan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import vn.nhan.model.Video_24110291;
import vn.nhan.util.DBConnection_24110291;

public class VideoDAOImpl_24110291 implements IVideoDAO_24110291 {

    /** Video kem ten category, so Share, so Like. */
    private static final String SELECT = "SELECT v.*, c.Categoryname, "
            + "(SELECT COUNT(*) FROM Shares s WHERE s.VideoId = v.VideoId) AS ShareCount, "
            + "(SELECT COUNT(*) FROM Favorites f WHERE f.VideoId = v.VideoId) AS LikeCount "
            + "FROM Videos v LEFT JOIN Category c ON c.CategoryId = v.CategoryId ";

    private Video_24110291 map(ResultSet rs) throws SQLException {
        Video_24110291 v = new Video_24110291();
        v.setVideoId(rs.getString("VideoId"));
        v.setTitle(rs.getString("Title"));
        v.setPoster(rs.getString("Poster"));
        v.setViews(rs.getInt("Views"));
        v.setDescription(rs.getString("Description"));
        v.setActive(rs.getBoolean("Active"));
        v.setCategoryId(rs.getInt("CategoryId"));
        v.setCategoryName(rs.getString("Categoryname"));
        v.setShareCount(rs.getInt("ShareCount"));
        v.setLikeCount(rs.getInt("LikeCount"));
        return v;
    }

    @Override
    public Video_24110291 findById(String videoId) {
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT + "WHERE v.VideoId = ?")) {
            ps.setString(1, videoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Video_24110291> findByCategory(int categoryId, int page, int size) {
        String sql = SELECT + "WHERE v.CategoryId = ? ORDER BY v.VideoId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<Video_24110291> list = new ArrayList<>();
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.setInt(2, (page - 1) * size);
            ps.setInt(3, size);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public int countByCategory(int categoryId) {
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM Videos WHERE CategoryId = ?")) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
