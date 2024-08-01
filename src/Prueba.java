import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class Prueba {
    public JPanel mainPanel;
    private JTable table2;

    public Prueba() {
        // Inicializa la tabla y carga los datos
        table2.setModel(new CustomTableModel());
        loadData();
    }

    private void loadData() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("celltechhub");
            MongoCollection<Document> collection = database.getCollection("Productos");

            List<Document> productos = collection.find().into(new ArrayList<>());
            CustomTableModel model = (CustomTableModel) table2.getModel();

            for (Document doc : productos) {
                ObjectId id = doc.getObjectId("_id");
                String nombre = doc.getString("nombre");
                String descripcion = doc.getString("descripcion");
                int cantidad = doc.getInteger("cantidad");
                double precio = doc.getDouble("precio");
                String fotoUrl = doc.getString("foto");

                model.addRow(new Object[]{id.toString(), nombre, descripcion, cantidad, precio, fotoUrl});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos");
        }
    }

    private class CustomTableModel extends DefaultTableModel {
        public CustomTableModel() {
            super(new String[]{"ID", "Nombre", "Descripción", "Cantidad", "Precio", "Foto"}, 0);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 5) {
                return ImageIcon.class; // Columna de la foto
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            if (column == 5) {
                // Convertir la URL de la foto a ImageIcon
                try {
                    URL url = new URL((String) aValue);
                    BufferedImage img = ImageIO.read(url);
                    aValue = new ImageIcon(img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            super.setValueAt(aValue, row, column);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Visualización de Productos");
        frame.setContentPane(new Prueba().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}