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

public class GestionProductos {
    public JPanel mainPanel;
    private JTable table1;
    private JButton añadirButton;
    private JButton actualizarButton;
    private JButton button2;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton cerrarCesionB;
    private JButton eliminarButton;
    private JButton regresarB;

    private MongoClient mongoClient;
    private MongoCollection<Document> collection;
    private DefaultTableModel model;

    private ObjectId selectedId; // Id del documento seleccionado

    public GestionProductos() {
        // Conectar a MongoDB y obtener los datos
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("celltechhub");
        collection = database.getCollection("Productos");

        List<Document> documents = (List<Document>) collection.find().into(new ArrayList<>());

        // Crear modelo de tabla
        model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 5) { // Suponiendo que la columna de la imagen es la sexta
                    return ImageIcon.class;
                }
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
                    if (key.equals("foto")) {
                        row[i++] = new ImageIcon(doc.getString(key));
                    } else {
                        row[i++] = doc.get(key);
                    }
                }
                model.addRow(row);
            }
        }

        // Asignar el modelo a table1
        table1.setModel(model);
        table1.setRowHeight(50);

        añadirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("CRUD Productos");
                frame.setContentPane(new Crear().mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setSize(1000, 630);
                frame.setVisible(true);
                ((JFrame) SwingUtilities.getWindowAncestor(mainPanel)).dispose();
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

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {
                    selectedId = (ObjectId) model.getValueAt(selectedRow, 0); // Guardar el id del documento seleccionado

                    // Obtener los datos del documento seleccionado
                    Document selectedDocument = collection.find(new Document("_id", selectedId)).first();

                    // Mostrar los datos en un formulario de actualización
                    JFrame frame = new JFrame("Actualizar Producto");
                    frame.setContentPane(new Crear(selectedDocument).mainPanel);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.pack();
                    frame.setResizable(false);
                    frame.setLocationRelativeTo(null);
                    frame.setSize(1000, 630);
                    frame.setVisible(true);
                    ((JFrame) SwingUtilities.getWindowAncestor(mainPanel)).dispose();
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Seleccione una fila para actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        regresarB.addActionListener(new ActionListener() {
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


}