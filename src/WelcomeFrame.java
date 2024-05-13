import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class WelcomeFrame extends JFrame {
    private String username;
    private String password;
    private LocalDate selectedDate;

    public WelcomeFrame(String username,String password) {
        this.username = username;
        this.password = password;
        setTitle("Bienvenido a FastPark");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Centrar el frame en la pantalla

        ImageIcon icon = new ImageIcon("C:\\Users\\clara\\Downloads\\d2196d35-2773-4a97-99e0-25b880c6776d.jpeg");
        Image image = icon.getImage().getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(image);

        JLabel logoLabel = new JLabel(scaledIcon);
        JLabel welcomeLabel = new JLabel("<html><center>¡Bienvenido a FastPark!<br>A continuación podrás seleccionar tu tipo de vehículo.</center></html>");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logoLabel, BorderLayout.CENTER);
        panel.add(welcomeLabel, BorderLayout.SOUTH);

        JButton viewReservationsButton = new JButton("Ver Reservas");
        JButton reservePlazaButton = new JButton("Reservar Plaza");
        JButton backButton = new JButton("Volver");

        viewReservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Integer> reservedPlazas = DatabaseManager.getReservedPlazas(username);
                if (reservedPlazas.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay ninguna reserva de plaza.");
                } else {
                    // Crear un array de opciones para el JOptionPane
                    String[] options = new String[reservedPlazas.size()];
                    for (int i = 0; i < reservedPlazas.size(); i++) {
                        options[i] = "Ticket " + (i + 1);
                    }

                    // Mostrar un JOptionPane para que el usuario seleccione el ticket
                    String selectedOption = (String) JOptionPane.showInputDialog(
                            null,
                            "Selecciona el ticket:",
                            "Ver Reservas",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);

                    if (selectedOption != null) { // Si el usuario selecciona una opción
                        int selectedIndex = Integer.parseInt(selectedOption.split(" ")[1]) - 1;
                        int plazaNumber = reservedPlazas.get(selectedIndex);
                        // Obtener la fecha de reserva desde la base de datos
                        LocalDate selectedDate =DatabaseManager.getReservationDate(plazaNumber);
                        TicketReservaFrame ticketFrame = new TicketReservaFrame(plazaNumber, selectedDate,username,password);
                        ticketFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        ticketFrame.setVisible(true);
                    }
                }
            }
        });

        reservePlazaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar la ventana actual
                dispose();

                // Abrir la ventana de selección de vehículo
                SwingUtilities.invokeLater(() -> {
                    VehicleSelectionFrame vehicleSelectionFrame = new VehicleSelectionFrame(selectedDate,username,password);
                    vehicleSelectionFrame.setVisible(true);
                });

                // Abrir la ventana de calendario
                SwingUtilities.invokeLater(() -> {
                    CalendarFrame calendarFrame = new CalendarFrame(username,password);
                    calendarFrame.setVisible(true);
                });
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Volver al inicio
                setVisible(false);
                new LoginFrame();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(viewReservationsButton);
        buttonPanel.add(reservePlazaButton);
        buttonPanel.add(backButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true); // Hacer visible el frame
    }
}
