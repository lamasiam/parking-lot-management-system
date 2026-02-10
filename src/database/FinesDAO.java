package database;

import models.fine.Fine;
import java.sql.*;

public class FinesDAO {
    private Connection connection;

    public FinesDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS fines (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "license_plate VARCHAR(20) NOT NULL, " +
                     "amount DECIMAL(10,2) NOT NULL, " +
                     "reason VARCHAR(255), " +
                     "status VARCHAR(20) DEFAULT 'UNPAID', " +
                     "date_issued TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                     ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFine(Fine fine) {
        String sql = "INSERT INTO fines (license_plate, amount, reason, status) VALUES (?, ?, ?, 'UNPAID')";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, fine.getLicensePlate());
            pstmt.setDouble(2, fine.getAmount());
            pstmt.setString(3, fine.getReason());
            pstmt.executeUpdate();
            System.out.println("⚠️ Fine added for " + fine.getLicensePlate() + ": RM " + fine.getAmount());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getUnpaidFinesTotal(String licensePlate) {
        String sql = "SELECT SUM(amount) as total FROM fines WHERE license_plate = ? AND status = 'UNPAID'";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, licensePlate);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public void markFinesAsPaid(String licensePlate) {
        String sql = "UPDATE fines SET status = 'PAID' WHERE license_plate = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, licensePlate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}