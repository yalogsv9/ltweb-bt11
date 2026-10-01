package vn.nhan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import vn.nhan.model.OrderDetail_24110291;
import vn.nhan.model.Order_24110291;
import vn.nhan.util.DBConnection_24110291;

public class OrderDAOImpl_24110291 implements IOrderDAO_24110291 {

    static {
        ensureTablesExist();
    }

    /**
     * Tu dong kiem tra va khoi tao bang Orders va OrderDetails neu chua ton tai tren SQL Server.
     */
    public static void ensureTablesExist() {
        String sqlOrders = "IF OBJECT_ID('Orders', 'U') IS NULL "
                + "CREATE TABLE Orders ("
                + "    OrderId INT IDENTITY(1,1) PRIMARY KEY,"
                + "    Username NVARCHAR(50) NULL,"
                + "    ReceiverName NVARCHAR(100) NOT NULL,"
                + "    ReceiverPhone NVARCHAR(20) NOT NULL,"
                + "    ReceiverAddress NVARCHAR(255) NOT NULL,"
                + "    Note NVARCHAR(500) NULL,"
                + "    TotalAmount DECIMAL(18,2) NOT NULL,"
                + "    PaymentMethod NVARCHAR(50) NOT NULL DEFAULT N'COD',"
                + "    Status NVARCHAR(50) NOT NULL DEFAULT N'Đơn hàng mới',"
                + "    CreatedAt DATETIME DEFAULT GETDATE()"
                + ");";

        String sqlDetails = "IF OBJECT_ID('OrderDetails', 'U') IS NULL "
                + "CREATE TABLE OrderDetails ("
                + "    DetailId INT IDENTITY(1,1) PRIMARY KEY,"
                + "    OrderId INT NOT NULL,"
                + "    VideoId NVARCHAR(50) NOT NULL,"
                + "    Price DECIMAL(18,2) NOT NULL,"
                + "    Quantity INT NOT NULL,"
                + "    Amount DECIMAL(18,2) NOT NULL"
                + ");";

        try (Connection c = DBConnection_24110291.getConnection();
             Statement st = c.createStatement()) {
            st.execute(sqlOrders);
            st.execute(sqlDetails);
        } catch (Exception e) {
            // Neu SQL Server chua san sang luc khoi dong thi bo qua de luc truy van thuc hien lai
        }
    }

    private Order_24110291 mapOrder(ResultSet rs) throws SQLException {
        Order_24110291 o = new Order_24110291();
        o.setOrderId(rs.getInt("OrderId"));
        o.setUsername(rs.getString("Username"));
        o.setReceiverName(rs.getString("ReceiverName"));
        o.setReceiverPhone(rs.getString("ReceiverPhone"));
        o.setReceiverAddress(rs.getString("ReceiverAddress"));
        o.setNote(rs.getString("Note"));
        o.setTotalAmount(rs.getDouble("TotalAmount"));
        o.setPaymentMethod(rs.getString("PaymentMethod"));
        o.setStatus(rs.getString("Status"));
        o.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return o;
    }

    private OrderDetail_24110291 mapDetail(ResultSet rs) throws SQLException {
        OrderDetail_24110291 d = new OrderDetail_24110291();
        d.setDetailId(rs.getInt("DetailId"));
        d.setOrderId(rs.getInt("OrderId"));
        d.setVideoId(rs.getString("VideoId"));
        d.setPrice(rs.getDouble("Price"));
        d.setQuantity(rs.getInt("Quantity"));
        d.setAmount(rs.getDouble("Amount"));

        try {
            d.setVideoTitle(rs.getString("Title"));
            d.setVideoPoster(rs.getString("Poster"));
        } catch (Exception ignored) {
        }
        return d;
    }

    @Override
    public int createOrder(Order_24110291 order, List<OrderDetail_24110291> details) {
        ensureTablesExist();
        String sqlOrder = "INSERT INTO Orders (Username, ReceiverName, ReceiverPhone, ReceiverAddress, Note, TotalAmount, PaymentMethod, Status, CreatedAt) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
        String sqlDetail = "INSERT INTO OrderDetails (OrderId, VideoId, Price, Quantity, Amount) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = DBConnection_24110291.getConnection()) {
            c.setAutoCommit(false);
            int orderId = -1;
            try (PreparedStatement psOrder = c.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setString(1, order.getUsername());
                psOrder.setString(2, order.getReceiverName());
                psOrder.setString(3, order.getReceiverPhone());
                psOrder.setString(4, order.getReceiverAddress());
                psOrder.setString(5, order.getNote());
                psOrder.setDouble(6, order.getTotalAmount());
                psOrder.setString(7, order.getPaymentMethod() != null ? order.getPaymentMethod() : "COD");
                psOrder.setString(8, order.getStatus() != null ? order.getStatus() : "Đơn hàng mới");
                psOrder.executeUpdate();

                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    }
                }
            }

            if (orderId <= 0) {
                c.rollback();
                throw new SQLException("Khong the lay ID don hang vua tao!");
            }

            try (PreparedStatement psDetail = c.prepareStatement(sqlDetail)) {
                for (OrderDetail_24110291 d : details) {
                    psDetail.setInt(1, orderId);
                    psDetail.setString(2, d.getVideoId());
                    psDetail.setDouble(3, d.getPrice());
                    psDetail.setInt(4, d.getQuantity());
                    psDetail.setDouble(5, d.getAmount());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            c.commit();
            return orderId;
        } catch (SQLException e) {
            throw new RuntimeException("Loi khi tao don hang: " + e.getMessage(), e);
        }
    }

    @Override
    public Order_24110291 findById(int orderId) {
        ensureTablesExist();
        String sql = "SELECT * FROM Orders WHERE OrderId = ?";
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order_24110291 order = mapOrder(rs);
                    order.setItems(findDetailsByOrderId(orderId));
                    return order;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<OrderDetail_24110291> findDetailsByOrderId(int orderId) {
        ensureTablesExist();
        String sql = "SELECT d.*, v.Title, v.Poster FROM OrderDetails d "
                + "LEFT JOIN Videos v ON v.VideoId = d.VideoId "
                + "WHERE d.OrderId = ? ORDER BY d.DetailId";
        List<OrderDetail_24110291> list = new ArrayList<>();
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapDetail(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Order_24110291> findByUsername(String username) {
        return findByUsernameAndStatus(username, null);
    }

    @Override
    public List<Order_24110291> findByUsernameAndStatus(String username, String status) {
        ensureTablesExist();
        StringBuilder sql = new StringBuilder("SELECT * FROM Orders WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (username != null && !username.trim().isEmpty()) {
            sql.append("AND Username = ? ");
            params.add(username.trim());
        }

        if (status != null && !status.trim().isEmpty() && !"all".equalsIgnoreCase(status.trim())) {
            sql.append("AND Status = ? ");
            params.add(status.trim());
        }

        sql.append("ORDER BY OrderId DESC");

        List<Order_24110291> list = new ArrayList<>();
        try (Connection c = DBConnection_24110291.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order_24110291 o = mapOrder(rs);
                    o.setItems(findDetailsByOrderId(o.getOrderId()));
                    list.add(o);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Order_24110291> findAll(String status) {
        return findByUsernameAndStatus(null, status);
    }
}
