import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Java";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "olaquetal";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static boolean checkLogin(String username, String password) {
        String sql = "SELECT * FROM reservationss WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Si hay resultados, el usuario existe y la contraseña es correcta
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registerUser(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("El nombre de usuario y la contraseña no pueden ser nulos ni estar vacíos.");
            return false;
        }
        String sql = "INSERT INTO reservationss(username, password) VALUES(?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Si se insertó correctamente, retorna verdadero
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean makeReservation(int plazaNumber, LocalDate selectedDate, String username, String password) {
        // Verificar si hay un registro con plaza_number y reservation_date NULL para el usuario dado
        String checkNullSql = "SELECT id FROM reservationss WHERE plaza_number IS NULL AND reservation_date IS NULL AND username IS NOT NULL AND username = ?";
        String updateSql = "UPDATE reservationss SET plaza_number = ?, reservation_date = ? WHERE id = ?";
        String insertSql = "INSERT INTO reservationss(plaza_number, reservation_date, username, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement checkNullStmt = conn.prepareStatement(checkNullSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            checkNullStmt.setString(1, username);
            ResultSet rs = checkNullStmt.executeQuery();

            if (rs.next()) { // Si se encuentra un registro con plaza_number y reservation_date NULL, actualizarlo
                int id = rs.getInt("id");
                System.out.println("ID del registro encontrado: " + id); // Impresión de depuración
                updateStmt.setInt(1, plazaNumber);
                updateStmt.setDate(2, java.sql.Date.valueOf(selectedDate));
                updateStmt.setInt(3, id);
                int rowsAffected = updateStmt.executeUpdate();
                System.out.println("Filas afectadas por la actualización: " + rowsAffected); // Impresión de depuración
                return rowsAffected > 0; // Si se actualizó correctamente, retorna verdadero
            } else { // Si no se encuentra un registro NULL, insertar un nuevo registro
                // Verificar si el nombre de usuario no es nulo
                if (username != null) {
                    insertStmt.setInt(1, plazaNumber);
                    insertStmt.setDate(2, java.sql.Date.valueOf(selectedDate));
                    insertStmt.setString(3, username);
                    insertStmt.setString(4, password);
                    int rowsAffected = insertStmt.executeUpdate();
                    System.out.println("Filas afectadas por la inserción: " + rowsAffected); // Impresión de depuración
                    return rowsAffected > 0; // Si se insertó correctamente, retorna verdadero
                } else {
                    System.out.println("El nombre de usuario no puede ser nulo.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkReservation(int plazaNumber, LocalDate selectedDate) {
        String sql = "SELECT * FROM reservationss WHERE plaza_number = ? AND reservation_date = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, plazaNumber);
            pstmt.setDate(2, java.sql.Date.valueOf(selectedDate));
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Si hay resultados, la plaza está reservada para esa fecha
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener la fecha de reserva para una plaza específica
    public static LocalDate getReservationDate(int plazaNumber) {
        String sql = "SELECT reservation_date FROM reservationss WHERE plaza_number = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, plazaNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDate("reservation_date").toLocalDate();
            } else {
                return null; // Devolver null si no se encuentra ninguna fecha de reserva para la plaza
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para obtener la lista de plazas reservadas por un usuario
    public static List<Integer> getReservedPlazas(String username) {
        List<Integer> reservedPlazas = new ArrayList<>();
        String sql = "SELECT plaza_number FROM reservationss WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reservedPlazas.add(rs.getInt("plaza_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservedPlazas;
    }
}
