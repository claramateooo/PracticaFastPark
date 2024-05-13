import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class TicketReservaFrame extends JFrame {
    private JLabel fechaValueLabel;
    private String username;
    private String password;
    public TicketReservaFrame(int numeroPlaza, LocalDate selectedDate, String username,String password) {
        this.username = username; // Almacenar el nombre de usuario recibido en una variable de instancia
        this.password=password;
        setTitle("Ticket de Reserva");
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel fechaLabel = new JLabel("Fecha:");
        if (selectedDate != null) { // Verificar si selectedDate no es nulo
            fechaValueLabel = new JLabel(selectedDate.toString());
        } else {
            fechaValueLabel = new JLabel("Fecha no disponible");
        }
        JLabel plazaLabel = new JLabel("Número de Plaza Reservada:");
        JLabel plazaValueLabel = new JLabel(String.valueOf(numeroPlaza)); // Mostrar el número de plaza seleccionada
        JLabel estacionamientoLabel = new JLabel("Estacionamiento:");
        JLabel estacionamientoValueLabel = new JLabel("Centro Comercial de Chamberí");

        JButton volverMenuButton = new JButton("Volver al Menú Principal");
        volverMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ocultar la ventana actual y mostrar el menú principal (WelcomeFrame) con el nombre de usuario
                dispose();
                new WelcomeFrame(username,password);
            }
        });

        JButton salirButton = new JButton("Salir");
        salirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Cerrar la aplicación al hacer clic en Salir
                System.exit(0);
            }
        });

        panel.add(fechaLabel);
        panel.add(fechaValueLabel);
        panel.add(plazaLabel);
        panel.add(plazaValueLabel);
        panel.add(estacionamientoLabel);
        panel.add(estacionamientoValueLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(volverMenuButton);
        buttonPanel.add(salirButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actualizarFecha(LocalDate nuevaFecha) {
        fechaValueLabel.setText(nuevaFecha.toString());
    }
}
