import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class CalendarFrame extends JFrame {
    private JCalendar calendar;
    private JButton confirmButton;
    private JLabel selectedDateLabel;
    private static LocalDate selectedDate;
    private TicketReservaFrame ticketFrame;
   private  String username;
   private String password;
    public CalendarFrame(String username, String password) {
        this.username = username;
        this.password = password;
        setTitle("Seleccione la fecha de reserva");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Centrar el frame en la pantalla

        JPanel panel = new JPanel(new BorderLayout());

        calendar = new JCalendar();
        calendar.setTodayButtonVisible(true);

        confirmButton = new JButton("Confirmar");
        selectedDateLabel = new JLabel("Fecha seleccionada: ");

        panel.add(calendar, BorderLayout.CENTER);
        panel.add(confirmButton, BorderLayout.SOUTH);
        panel.add(selectedDateLabel, BorderLayout.NORTH);

        add(panel);

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedDate = LocalDate.of(calendar.getCalendar().get(Calendar.YEAR),
                        calendar.getCalendar().get(Calendar.MONTH) + 1,
                        calendar.getCalendar().get(Calendar.DAY_OF_MONTH));

                // Mostrar la fecha seleccionada en el formato deseado
                String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                selectedDateLabel.setText("Fecha seleccionada: " + formattedDate);

                // Actualizar la fecha en el TicketReservaFrame
                if (ticketFrame == null) {
                    ticketFrame = new TicketReservaFrame(-1, selectedDate,username,password);
                }
                ticketFrame.actualizarFecha(selectedDate);

                // Mostrar mensaje de confirmación con la fecha seleccionada
                JOptionPane.showMessageDialog(null, "Fecha seleccionada correcta: " + formattedDate);

                // Cerrar la ventana después de confirmar la fecha
                dispose();

                // Abrir la ventana de selección de vehículo con la fecha seleccionada


                new VehicleSelectionFrame(selectedDate,username,password);
            }
        });

        setVisible(true);
    }

}
