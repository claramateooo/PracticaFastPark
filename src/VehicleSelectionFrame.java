// VehicleSelectionFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class VehicleSelectionFrame extends JFrame {
    private LocalDate selectedDate;
   private String username;
   private String password;

    public VehicleSelectionFrame(LocalDate selectedDate,String username,String password) {
        this.selectedDate = selectedDate;
        this.username = username;
        this.password = password;

        this.setTitle("Selección de Vehículo");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 250);
        this.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel de título
        JLabel titleLabel = new JLabel("Seleccione el tipo de vehículo");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel de selección de vehículos
        JPanel vehiclePanel = new JPanel(new GridLayout(4, 1, 0, 10));
        vehiclePanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        ButtonGroup vehicleGroup = new ButtonGroup();

        JRadioButton carRadioButton = createRadioButton("Coche", vehicleGroup);
        JRadioButton electricCarRadioButton = createRadioButton("Coche Eléctrico", vehicleGroup);
        JRadioButton motorcycleRadioButton = createRadioButton("Moto", vehicleGroup);
        JRadioButton disabledRadioButton = createRadioButton("Minusválido", vehicleGroup);

        vehiclePanel.add(carRadioButton);
        vehiclePanel.add(electricCarRadioButton);
        vehiclePanel.add(motorcycleRadioButton);
        vehiclePanel.add(disabledRadioButton);
        mainPanel.add(vehiclePanel, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 20, 50));

        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedVehicle = null;
                if (carRadioButton.isSelected()) {
                    selectedVehicle = "C"; // Coche
                } else if (electricCarRadioButton.isSelected()) {
                    selectedVehicle = "E"; // Eléctrico
                } else if (motorcycleRadioButton.isSelected()) {
                    selectedVehicle = "M"; // Moto
                } else if (disabledRadioButton.isSelected()) {
                    selectedVehicle = "Min"; // Minusválido
                }
                if (selectedVehicle != null) {
                    VehicleSelectionFrame.this.setVisible(false);

                    new PlazaReservaFrame(selectedVehicle, selectedDate,username,password);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un tipo de vehículo");
                }
            }
        });

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new CalendarFrame(username,password);
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(confirmButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Agregar el panel principal al marco
        this.add(mainPanel);
        this.setVisible(true);
    }

    // Método para crear botones de radio con un estilo común
    private JRadioButton createRadioButton(String text, ButtonGroup group) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setFont(new Font("Arial", Font.PLAIN, 16));
        radioButton.setFocusPainted(false);
        group.add(radioButton);
        return radioButton;
    }
}
