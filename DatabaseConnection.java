import java.sql.*;
import java.util.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/car_showroom";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            } catch (Exception e) {
                System.out.println("❌ Database connection failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }


    public static boolean registerUser(String username, String email, String fullName, String password) {
        String query = "INSERT INTO users (username, email, full_name, password, role) VALUES (?, ?, ?, ?, 'customer')";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, fullName);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    public static int loginUser(String username, String password, String role) {
        String query = "SELECT id, role FROM users WHERE (username = ? OR email = ?) AND password = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String userRole = rs.getString("role");
                if (role.equals("ADMINISTRATOR") && userRole.equals("admin")) {
                    return rs.getInt("id");
                } else if (role.equals("CUSTOMER") && userRole.equals("customer")) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getUserName(int userId) {
        String query = "SELECT full_name FROM users WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("full_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "User";
    }

    public static int getUserId(String username) {
        String query = "SELECT id FROM users WHERE username = ? OR email = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static List<UserDashboard.Car> getAllCars() {
        List<UserDashboard.Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                UserDashboard.Car car = new UserDashboard.Car(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("year"),
                        rs.getDouble("price"),
                        rs.getString("color"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission"),
                        rs.getBoolean("available"),
                        rs.getString("image_url")
                );
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static void updateCarAvailability(int carId, boolean available) {
        String query = "UPDATE cars SET available = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setBoolean(1, available);
            pstmt.setInt(2, carId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void savePurchase(int userId, int carId, String phone, String paymentMethod) {
        String query = "INSERT INTO purchased_cars (user_id, car_id, phone, payment_method) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, carId);
            pstmt.setString(3, phone);
            pstmt.setString(4, paymentMethod);
            pstmt.executeUpdate();
            updateCarAvailability(carId, false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<UserDashboard.Car> getPurchasedCars(int userId) {
        List<UserDashboard.Car> purchased = new ArrayList<>();
        String query = "SELECT c.* FROM cars c INNER JOIN purchased_cars pc ON c.id = pc.car_id WHERE pc.user_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                UserDashboard.Car car = new UserDashboard.Car(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("year"),
                        rs.getDouble("price"),
                        rs.getString("color"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission"),
                        false,
                        rs.getString("image_url")
                );
                purchased.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchased;
    }

    public static List<AdminDashboard.Car> getAllCarsForAdmin() {
        List<AdminDashboard.Car> cars = new ArrayList<>();
        String query = "SELECT * FROM cars";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                AdminDashboard.Car car = new AdminDashboard.Car(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("year"),
                        rs.getDouble("price"),
                        rs.getString("color"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission"),
                        rs.getBoolean("available"),
                        rs.getString("image_url")
                );
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static void addCar(int id, String brand, String model, String year, double price,
                              String color, String fuel, String trans, String imageUrl) {
        String query = "INSERT INTO cars (id, brand, model, year, price, color, fuel_type, transmission, available, image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, true, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, brand);
            pstmt.setString(3, model);
            pstmt.setString(4, year);
            pstmt.setDouble(5, price);
            pstmt.setString(6, color);
            pstmt.setString(7, fuel);
            pstmt.setString(8, trans);
            pstmt.setString(9, imageUrl);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCar(int carId) {
        String query = "DELETE FROM cars WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setInt(1, carId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getNextCarId() {
        String query = "SELECT MAX(id) FROM cars";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 7; // Default next ID
    }

    // ==================== CUSTOMER METHODS (ADMIN) ====================

    public static List<AdminDashboard.Customer> getAllCustomers() {
        List<AdminDashboard.Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                AdminDashboard.Customer customer = new AdminDashboard.Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("cnic")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public static void addCustomer(int id, String name, String phone, String email, String address, String cnic) {
        String query = "INSERT INTO customers (id, name, phone, email, address, cnic) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);
            pstmt.setString(4, email);
            pstmt.setString(5, address);
            pstmt.setString(6, cnic);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCustomer(int customerId) {
        String query = "DELETE FROM customers WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setInt(1, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getNextCustomerId() {
        String query = "SELECT MAX(id) FROM customers";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 3; // Default next ID
    }

    // ==================== SALE METHODS (ADMIN) ====================

    public static List<AdminDashboard.Sale> getAllSales() {
        List<AdminDashboard.Sale> sales = new ArrayList<>();
        String query = "SELECT * FROM sales";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                AdminDashboard.Sale sale = new AdminDashboard.Sale(
                        rs.getInt("id"),
                        rs.getInt("car_id"),
                        rs.getInt("customer_id"),
                        rs.getDouble("amount"),
                        rs.getString("payment_method"),
                        rs.getString("sale_date")
                );
                sales.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales;
    }

    public static void addSale(int carId, int customerId, double amount, String paymentMethod, String date) {
        String query = "INSERT INTO sales (car_id, customer_id, amount, payment_method, sale_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setInt(1, carId);
            pstmt.setInt(2, customerId);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, paymentMethod);
            pstmt.setString(5, date);
            pstmt.executeUpdate();
            updateCarAvailability(carId, false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getNextSaleId() {
        String query = "SELECT MAX(id) FROM sales";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 3; // Default next ID
    }

    public static List<AdminDashboard.User> getAllUsers() {
        List<AdminDashboard.User> users = new ArrayList<>();
        String query = "SELECT username, full_name, email, phone, role FROM users";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                AdminDashboard.User user = new AdminDashboard.User(
                        rs.getString("username"),
                        "",
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone") != null ? rs.getString("phone") : "",
                        rs.getString("role"),
                        "Active"
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void addUser(String username, String password, String fullName, String email, String phone, String role) {
        String query = "INSERT INTO users (username, email, full_name, password, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, fullName);
            pstmt.setString(4, password);
            pstmt.setString(5, phone);
            pstmt.setString(6, role.toLowerCase());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getTotalCars() {
        String query = "SELECT COUNT(*) FROM cars";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTotalAvailableCars() {
        String query = "SELECT COUNT(*) FROM cars WHERE available = true";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTotalCustomers() {
        String query = "SELECT COUNT(*) FROM customers";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTotalSales() {
        String query = "SELECT COUNT(*) FROM sales";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double getTotalRevenue() {
        String query = "SELECT COALESCE(SUM(amount), 0) FROM sales";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static void saveTestDrive(String customerName, String phone, int carId, String date, String time) {
        String query = "INSERT INTO test_drives (customer_name, phone, car_id, test_date, test_time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, customerName);
            pstmt.setString(2, phone);
            pstmt.setInt(3, carId);
            pstmt.setString(4, date);
            pstmt.setString(5, time);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✅ Database connection closed!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}