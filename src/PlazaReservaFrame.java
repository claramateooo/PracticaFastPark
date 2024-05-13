import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class PlazaReservaFrame extends JFrame {
    private JButton[][] plazaButtons;
    private JLabel estadoLabel;
    private boolean plazaSeleccionada;
    private LocalDate selectedDate;
    private Set<Integer> plazasReservadas;
    private String username;
    private String password;
    private String selectedVehicle;

    public PlazaReservaFrame(String selectedVehicle, LocalDate selectedDate, String username, String password) {
        this.selectedDate = selectedDate;
        this.plazasReservadas = new HashSet<>();
        this.username = username;
        this.password = password;
        this.selectedVehicle = selectedVehicle;

        setTitle("Reservar Plaza Centro Comercial Chamberi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 5, 10, 10));
        plazaButtons = new JButton[4][5];
        plazaSeleccionada = false;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                int numeroPlaza = i * 5 + j + 1;
                plazaButtons[i][j] = new JButton(getPlazaLabel(i) + "\n" + numeroPlaza);
                if (!checkPlazaType(numeroPlaza)) {
                    plazaButtons[i][j].setBackground(Color.RED);
                    plazaButtons[i][j].setEnabled(false);
                } else if (DatabaseManager.checkReservation(numeroPlaza, selectedDate)) {
                    plazaButtons[i][j].setBackground(Color.RED);
                    plazasReservadas.add(numeroPlaza);
                } else {
                    plazaButtons[i][j].setBackground(Color.GREEN);
                }

                plazaButtons[i][j].setFont(new Font("Arial", Font.BOLD, 16));
                plazaButtons[i][j].setForeground(Color.WHITE);
                plazaButtons[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                plazaButtons[i][j].addActionListener(new PlazaActionListener(i, j));
                panel.add(plazaButtons[i][j]);
            }
        }

        estadoLabel = new JLabel("<html><font color='blue'><b>C</b></font> Plaza Coche | <font color='black'><b>Min</b></font> Plaza Minúsvalido | <font color='green'><b>E</b></font> Plaza Coche Eléctrico | <font color='orange'><b>M</b></font> Plaza Moto</html>");
        estadoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        estadoLabel.setForeground(Color.BLACK);

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new VehicleSelectionFrame(selectedDate, username,password);
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(backButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(estadoLabel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        add(mainPanel);

        setVisible(true);
    }

    private boolean checkPlazaType(int plazaNumber) {
        switch (selectedVehicle) {
            case "C":
                return plazaNumber >= 16 && plazaNumber <= 20; // Coche
            case "E":
                return plazaNumber >= 6 && plazaNumber <= 10; // Eléctrico
            case "M":
                return plazaNumber >= 1 && plazaNumber <= 5; // Moto
            case "Min":
                return plazaNumber >= 11 && plazaNumber <= 15; // Minusválido
            default:
                return false;
        }
    }

    private String getPlazaLabel(int index) {
        switch (index) {
            case 0:
                return "<html><font color='orange'><b>M</b></font>";
            case 1:
                return "<html><font color='green'><b>E</b></font>";
            case 2:
                return "<html><font color='black'><b>Min</b></font>";
            case 3:
                return "<html><font color='blue'><b>C</b></font>";
            default:
                return "";
        }
    }

    private class PlazaActionListener implements ActionListener {
        private int fila, columna;

        public PlazaActionListener(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
        }

        public void actionPerformed(ActionEvent e) {
            if (!plazaSeleccionada) {
                int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro que desea reservar esta plaza?", "Confirmar Reserva", JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION) {
                    int numeroPlaza = fila * 5 + columna + 1;
                    if (plazasReservadas.contains(numeroPlaza)) {
                        JOptionPane.showMessageDialog(null, "Esta plaza ya está reservada.");
                    } else {
                        plazaButtons[fila][columna].setBackground(Color.RED);
                        plazaSeleccionada = true;
                        estadoLabel.setText("Plaza " + (fila * 5 + columna + 1) + " reservada");
                        plazasReservadas.add(numeroPlaza);
                        if (DatabaseManager.makeReservation(numeroPlaza, selectedDate, username, password)) {
                            mostrarTicket(numeroPlaza);
                            JOptionPane.showMessageDialog(null, "Plaza reservada con éxito.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al reservar la plaza.");
                        }
                    }
                }
            }
        }

        private void mostrarTicket(int numeroPlaza) {
            TicketReservaFrame ticketFrame = new TicketReservaFrame(numeroPlaza, selectedDate, username,password);
            ticketFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ticketFrame.setVisible(true);
        }
    }
}

