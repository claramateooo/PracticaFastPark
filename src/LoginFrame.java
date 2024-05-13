// LoginFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class LoginFrame extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("FastPark");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); // Centrar el frame en la pantalla

        JLabel titleLabel = new JLabel("FastPark", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Usuario:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Iniciar Sesión");
        JButton registerButton = new JButton("Registrarse");

        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel, BorderLayout.CENTER);

        setVisible(true); // Hacer visible el frame
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Iniciar Sesión")) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Verificar el inicio de sesión en la base de datos
            boolean loginSuccess = DatabaseManager.checkLogin(username, password);
            if (loginSuccess) {
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
                // Mostrar la ventana de bienvenida
                LocalDate selectedDate = null;
                new WelcomeFrame(username,password);
            } else {
                JOptionPane.showMessageDialog(this, "Error: Contraseña o Usuario Incorrectos");
            }
        } else if (e.getActionCommand().equals("Registrarse")) {
            // Abrir la ventana de registro
            new RegisterFrame();
        }
    }

}
