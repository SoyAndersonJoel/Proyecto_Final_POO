import com.mongodb.client.*;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.*;
import org.bson.Document;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        UsuarioAdmi administrador = new UsuarioAdmi();
        String username = "Anderson";
        String password = "123456";
        administrador.insertar(username, password);

        JFrame frame = new JFrame("Cell Tech Hub");
        frame.setContentPane(new Login().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setSize(1000, 630);
        frame.setVisible(true);
    }
}
