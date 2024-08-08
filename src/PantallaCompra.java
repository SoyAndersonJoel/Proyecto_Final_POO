import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.swing.*;

public class PantallaCompra {
    public JPanel mainPanel;
    private JTextArea detalleCompraArea;
    private JButton confirmarCompraB;
    private JButton cancelarCompraB;
    private JTextField nombreCompradorTf;
    private JTextField direccionTf;

    private String productName;
    private int quantity;
    private double totalPrice;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public PantallaCompra(String productName, int quantity, double totalPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;

        // Initialize MongoDB client and collection
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("celltechhub");
        collection = database.getCollection("Compras");

        // Ensure the mainPanel is initialized
        mainPanel = new JPanel();
        detalleCompraArea = new JTextArea(10, 30);
        confirmarCompraB = new JButton("Confirmar Compra");
        cancelarCompraB = new JButton("Cancelar Compra");
        nombreCompradorTf = new JTextField(20);
        direccionTf = new JTextField(20);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel("Detalle de la Compra:"));
        mainPanel.add(new JScrollPane(detalleCompraArea));
        mainPanel.add(new JLabel("Nombre del Comprador:"));
        mainPanel.add(nombreCompradorTf);
        mainPanel.add(new JLabel("Dirección:"));
        mainPanel.add(direccionTf);
        mainPanel.add(confirmarCompraB);
        mainPanel.add(cancelarCompraB);

        confirmarCompraB.addActionListener(e -> confirmarCompra());
        cancelarCompraB.addActionListener(e -> cancelarCompra());

        // Set the initial detail of the purchase
        setDetalleCompra("Producto: " + productName + "\nCantidad: " + quantity + "\nPrecio Total: $" + totalPrice);
    }

    public void setDetalleCompra(String detalle) {
        detalleCompraArea.setText(detalle);
    }

    private void confirmarCompra() {
        String nombreComprador = nombreCompradorTf.getText();
        String direccion = direccionTf.getText();
        if (nombreComprador.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Por favor, complete todos los campos.");
            return;
        }

        // Create a document to insert into the MongoDB collection
        Document compra = new Document("productName", productName)
                .append("quantity", quantity)
                .append("totalPrice", totalPrice)
                .append("nombreComprador", nombreComprador)
                .append("direccion", direccion)
                .append("fecha", new java.util.Date());

        collection.insertOne(compra);

        JOptionPane.showMessageDialog(mainPanel, "Compra confirmada para: " + nombreComprador);
        // Close the purchase screen window
        SwingUtilities.getWindowAncestor(mainPanel).dispose();
    }

    private void cancelarCompra() {
        // Implementar lógica de cancelación de compra
        JOptionPane.showMessageDialog(mainPanel, "Compra cancelada");
        // Close the purchase screen window
        SwingUtilities.getWindowAncestor(mainPanel).dispose();
    }
}
