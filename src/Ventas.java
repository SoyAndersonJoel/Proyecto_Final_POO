import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Ventas {
    private JButton cerrarCesionB;
    private JTable table1;
    private JButton regresarButton;
    public JPanel mainPanel;
    private JButton fechaButton;
    private JButton IDButton;
    private JButton cantidadButton;
    private JButton productoButton;
    private JButton totalButton;
    private JButton nombreButton;
    private JButton lugarButton1;

    public Ventas() {
        // Configurar conexión a MongoDB
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("celltechhub");
        MongoCollection<Document> collection = database.getCollection("Compras");

        // Recuperar datos de la colección "Compras"
        List<Document> documents = (List<Document>) collection.find().into(new ArrayList<>());

        // Crear y configurar el modelo de la tabla
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int column) {
                // Puedes ajustar esta parte según el tipo de datos de cada columna
                return Object.class;
            }
        };

        if (!documents.isEmpty()) {
            // Añadir columnas al modelo
            Document firstDoc = documents.get(0);
            for (String key : firstDoc.keySet()) {
                model.addColumn(key);
            }

            // Añadir filas al modelo
            for (Document doc : documents) {
                Object[] row = new Object[doc.size()];
                int i = 0;
                for (String key : doc.keySet()) {
                    row[i++] = doc.get(key);
                }
                model.addRow(row);
            }
        }

        // Asignar el modelo a la tabla
        table1.setModel(model);
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("");
                frame.setContentPane(new Administrador().mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setSize(1000, 630);
                frame.setVisible(true);
                ((JFrame) SwingUtilities.getWindowAncestor(mainPanel)).dispose();
            }
        });
        cerrarCesionB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Cell Tech Hub");
                frame.setContentPane(new Login().mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setSize(1000, 630);
                frame.setVisible(true);
                ((JFrame) SwingUtilities.getWindowAncestor(mainPanel)).dispose();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ventas");
        frame.setContentPane(new Ventas().table1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

