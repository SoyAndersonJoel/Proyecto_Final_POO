import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bson.conversions.Bson;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Cajero {

    private JButton cerrarCesionB;
    private JPanel mainPanel;
    private JTable table1;
    private DefaultTableModel model;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public Cajero() {
        // Initialize the MongoDB client and fetch data
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("celltechhub");
        collection = database.getCollection("Productos");

        List<Document> documents = (List<Document>) collection.find().into(new ArrayList<>());

        // Create table model
        model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 5) { // Assuming the 6th column is the image column
                    return ImageIcon.class;
                }
                return Object.class;
            }
        };

        if (!documents.isEmpty()) {
            // Add columns to the model
            Document firstDoc = documents.get(0);
            for (String key : firstDoc.keySet()) {
                model.addColumn(key);
            }

            // Add rows to the model
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

        // Assign the model to the table
        table1.setModel(model);

        // Add mouse listener for row click
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && table1.getSelectedRow() != -1) {
                    int selectedRow = table1.getSelectedRow();
                    String productId = table1.getValueAt(selectedRow, 0).toString(); // Assuming the product ID is in the first column
                    String productName = table1.getValueAt(selectedRow, 1).toString(); // Assuming the product name is in the second column
                    double productPrice = Double.parseDouble(table1.getValueAt(selectedRow, 3).toString()); // Assuming the price is in the fourth column

                    // Prompt for quantity
                    String quantityString = JOptionPane.showInputDialog(mainPanel, "Ingrese la cantidad para " + productName + ":");
                    if (quantityString != null && !quantityString.isEmpty()) {
                        try {
                            int quantity = Integer.parseInt(quantityString);
                            double totalPrice = productPrice * quantity;
                            // Update the database and navigate to the purchase screen
                            updateProductQuantity(productId, quantity);
                            showPurchaseScreen(productName, quantity, totalPrice);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(mainPanel, "Por favor, ingrese un número válido.");
                        }
                    }
                }
            }
        });
    }

    private void updateProductQuantity(String productId, int quantity) {
        Document product = collection.find(new Document("_id", new ObjectId(productId))).first();
        if (product != null) {
            int currentQuantity = product.getInteger("cantidad");
            if (currentQuantity >= quantity) {
                Bson updates = Updates.inc("cantidad", -quantity);
                collection.updateOne(new Document("_id", new ObjectId(productId)), updates);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Cantidad insuficiente en el inventario.");
            }
        }
    }

    private void showPurchaseScreen(String productName, int quantity, double totalPrice) {
        JFrame frame = new JFrame("Pantalla de Compra");
        PantallaCompra pantallaCompra = new PantallaCompra(productName, quantity, totalPrice);
        pantallaCompra.setDetalleCompra("Producto: " + productName + "\nCantidad: " + quantity + "\nPrecio Total: $" + totalPrice);
        frame.setContentPane(pantallaCompra.mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cajero");
        frame.setContentPane(new Cajero().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
