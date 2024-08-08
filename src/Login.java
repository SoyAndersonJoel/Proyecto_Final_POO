import javax.swing.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    public JPanel mainPanel;
    private JButton aceptarB;
    private JButton eliminarB;
    private JTextField usuarioTF;
    private JPasswordField passwordTF;

    public Login() {
        aceptarB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioTF.getText();
                String password = passwordTF.getText();
                if (usuario.isEmpty() || password.isEmpty()) {
                    return;
                }

                // Conectar a MongoDB y buscar el usuario
                try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                    MongoDatabase database = mongoClient.getDatabase("celltechhub");
                    MongoCollection<Document> collection = database.getCollection("Usuarios");

                    // Buscar el usuario administrador
                    Document adminUser = collection.find(Filters.and(
                            Filters.eq("username", usuario),
                            Filters.eq("password", password)
                    )).first();

                    if (adminUser != null) {
                        if (adminUser.getString("username").equals("Anderson") && adminUser.getString("password").equals("123456")) {
                            JFrame frame = new JFrame("Administrador");
                            frame.setContentPane(new Administrador().mainPanel);
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame.pack();
                            frame.setResizable(false);
                            frame.setLocationRelativeTo(null);
                            frame.setSize(1000, 630);
                            frame.setVisible(true);
                            ((JFrame) SwingUtilities.getWindowAncestor(mainPanel)).dispose();

                        }else {
                            JFrame frame = new JFrame("Cajero");
                            frame.setContentPane(new Cajero().mainPanel);
                            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            frame.pack();
                            frame.setResizable(false);
                            frame.setLocationRelativeTo(null);
                            frame.setSize(1000, 630);
                            frame.setVisible(true);
                            ((JFrame) SwingUtilities.getWindowAncestor(mainPanel)).dispose();
                        }
                    } else {
                            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                            usuarioTF.setText("");
                            passwordTF.setText("");


                    }
                }
            }
        });

        eliminarB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usuarioTF.setText("");
                passwordTF.setText("");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new Login().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 200);
        frame.setVisible(true);
    }
}
