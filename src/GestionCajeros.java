import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GestionCajeros {
    public JPanel mainPanel;
    private JButton cerrarCesionB;
    private JTable table1;
    private JButton actualizarButton;
    private JButton eliminarButton;
    private JButton añadirButton;

    private MongoClient mongoClient;
    private MongoCollection<Document> collection;
    private DefaultTableModel model;

    private ObjectId selectedId; // Id del documento seleccionado

    public GestionCajeros() {
        // Conectar a MongoDB y obtener los datos
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("celltechhub");
        collection = database.getCollection("Usuarios");

        List<Document> documents = (List<Document>) collection.find().into(new ArrayList<>());

        // Crear modelo de tabla
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Username");
        model.addColumn("Password");

        if (!documents.isEmpty()) {
            // Añadir filas al modelo
            for (Document doc : documents) {
                Object[] row = new Object[3];
                row[0] = doc.getObjectId("_id");
                row[1] = doc.getString("username");
                row[2] = doc.getString("password");
                model.addRow(row);
            }
        }

        // Asignar el modelo a table1
        table1.setModel(model);

        añadirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCajeroDialog(null); // Modo creación
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    selectedId = (ObjectId) model.getValueAt(selectedRow, 0); // Guardar el id del documento seleccionado
                    // Obtener los datos del documento seleccionado
                    Document selectedDocument = collection.find(new Document("_id", selectedId)).first();
                    showCajeroDialog(selectedDocument); // Modo actualización
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Seleccione una fila para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    // Obtener el id del documento a eliminar
                    ObjectId id = (ObjectId) model.getValueAt(selectedRow, 0);

                    // Eliminar el documento de la base de datos
                    collection.deleteOne(new Document("_id", id));

                    // Eliminar la fila del modelo de la tabla
                    model.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Seleccione una fila para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cerrarCesionB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Regresar a la vista principal
                JFrame frame = new JFrame("Login");
                frame.setContentPane(new Login().mainPanel); // Asegúrate de tener una clase Login
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setSize(300, 200);
                frame.setVisible(true);
                ((JFrame) SwingUtilities.getWindowAncestor(mainPanel)).dispose();
            }
        });
    }

    private void showCajeroDialog(Document document) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField usernameTf = new JTextField();
        JPasswordField passwordTf = new JPasswordField();

        if (document != null) {
            usernameTf.setText(document.getString("username"));
            passwordTf.setText(document.getString("password"));
        }

        panel.add(new JLabel("Username:"));
        panel.add(usernameTf);
        panel.add(new JLabel("Password:"));
        panel.add(passwordTf);

        int option = JOptionPane.showConfirmDialog(null, panel,
                document == null ? "Crear Cajero" : "Actualizar Cajero",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameTf.getText();
            String password = new String(passwordTf.getPassword());

            try {
                if (document == null) {
                    // Modo creación
                    Document doc = new Document("username", username)
                            .append("password", password);
                    collection.insertOne(doc);
                    JOptionPane.showMessageDialog(null, "Cajero creado con éxito");
                } else {
                    // Modo actualización
                    collection.updateOne(new Document("_id", document.getObjectId("_id")),
                            new Document("$set", new Document("username", username)
                                    .append("password", password)));
                    JOptionPane.showMessageDialog(null, "Cajero actualizado con éxito");
                }

                // Actualizar la tabla
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    private void refreshTable() {
        List<Document> documents = (List<Document>) collection.find().into(new ArrayList<>());
        model.setRowCount(0); // Limpiar filas existentes
        for (Document doc : documents) {
            Object[] row = new Object[3];
            row[0] = doc.getObjectId("_id");
            row[1] = doc.getString("username");
            row[2] = doc.getString("password");
            model.addRow(row);
        }
    }
}
