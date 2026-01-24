package database;

import java.sql.*;

/**
 * Database Manager - handles SQLite database connections
 * Singleton pattern to ensure only one connection
 * 
 * @author Member 1 - Parking Structure Lead
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:parking_lot.db";
    
    // Private constructor for Singleton
    private DatabaseManager() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Create connection
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("✓ Database connection established: parking_lot.db");
            
            // Create tables if they don't exist
            createTables();
        } catch (ClassNotFoundException e) {
            System.err.println("✗ SQLite JDBC driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed!");
            e.printStackTrace();
        }
    }
    
    /**
     * Get DatabaseManager instance (Singleton pattern)
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Get database connection
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Create all required tables
     */
    private void createTables() throws SQLException {
        Statement stmt = connection.createStatement();
        
        // Create parking_spots table
        String createParkingSpotsTable = 
            "CREATE TABLE IF NOT EXISTS parking_spots (" +
            "spot_id VARCHAR(20) PRIMARY KEY, " +
            "floor_number INTEGER NOT NULL, " +
            "row_number INTEGER NOT NULL, " +
            "spot_number INTEGER NOT NULL, " +
            "spot_type VARCHAR(20) NOT NULL, " +
            "hourly_rate DECIMAL(10,2) NOT NULL, " +
            "status VARCHAR(20) NOT NULL, " +
            "current_vehicle VARCHAR(20), " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")";
        
        stmt.execute(createParkingSpotsTable);
        System.out.println("✓ Table 'parking_spots' ready");
        
        // Create floors table (optional - for tracking floor metadata)
        String createFloorsTable = 
            "CREATE TABLE IF NOT EXISTS floors (" +
            "floor_number INTEGER PRIMARY KEY, " +
            "total_spots INTEGER DEFAULT 0, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")";
        
        stmt.execute(createFloorsTable);
        System.out.println("✓ Table 'floors' ready");
        
        stmt.close();
    }
    
    /**
     * Test database connection
     */
    public boolean testConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error closing database connection");
            e.printStackTrace();
        }
    }
}
