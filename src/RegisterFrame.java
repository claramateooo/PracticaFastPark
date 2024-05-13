import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterFrame() {
        setTitle("Registrarse");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Centrar el frame en la pantalla

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Nuevo Usuario:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Nueva Contraseña:");
        passwordField = new JPasswordField();

        JButton registerButton = new JButton("Registrarse");

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Verificar si los campos están vacíos
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "Por favor complete todos los campos obligatorios antes de registrar su cuenta");
                    return; // Detener el proceso de registro
                }

                // Registrar al usuario en la base de datos
                boolean success = DatabaseManager.registerUser(username, password);
                if (success) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "Usuario registrado con éxito");
                    dispose(); // Cerrar el marco de registro después del registro exitoso
                } else {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "Error al registrar usuario");
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(registerButton);

        add(panel, BorderLayout.CENTER);

        setVisible(true); // Hacer visible el frame
    }
}
