import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Crear {
    public JPanel mainPanel;
    private JButton cerrarCesionButton;
    private JTextField idTf;
    private JSpinner cantidadTf;
    private JTextField precioTf;
    private JTextField nombreTf;
    private JTextField descripcionTf;
    private JButton ImagenB;
    private JButton aceptarB;
    private JButton cancelarB;
    private JButton regresarB;
    private JLabel imagenTf;
    private String imagenRuta;
    private Document documento; // Documento para actualizar

    // Constructor para modo creación
    public Crear() {
        initComponents();
        setupCreateMode();
        cerrarCesionButton.addActionListener(new ActionListener() {
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
        cancelarB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idTf.setText("");
                cantidadTf.setValue(0);
                precioTf.setText("");
                nombreTf.setText("");
                descripcionTf.setText("");
                imagenTf.setText("");
                imagenRuta = "";
            }
        });
    }

    // Constructor para modo actualización
    public Crear(Document documento) {
        this.documento = documento;
        initComponents();
        setupUpdateMode();
    }

    private void initComponents() {
        ImagenB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de Imagen", "jpg", "jpeg", "png", "gif");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    imagenRuta = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        BufferedImage img = ImageIO.read(new File(imagenRuta));
                        ImageIcon icon = new ImageIcon(img.getScaledInstance(imagenTf.getWidth(), imagenTf.getHeight(), java.awt.Image.SCALE_SMOOTH));
                        imagenTf.setIcon(icon);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        regresarB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Cell Tech Hub");
                frame.setContentPane(new GestionProductos().mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setSize(1000, 630);
                frame.setVisible(true);
                ((JFrame) SwingUtilities.getWindowAncestor(mainPanel)).dispose();
            }
        });

        aceptarB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreTf.getText();
                String descripcion = descripcionTf.getText();
                int cantidad = Integer.parseInt(cantidadTf.getValue().toString());
                double precio = Double.parseDouble(precioTf.getText());

                // Crear o actualizar el producto
                try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                    MongoDatabase database = mongoClient.getDatabase("celltechhub");
                    MongoCollection<Document> collection = database.getCollection("Productos");

                    Document doc = new Document("nombre", nombre)
                            .append("descripcion", descripcion)
                            .append("cantidad", cantidad)
                            .append("precio", precio)
                            .append("foto", imagenRuta);

                    if (documento == null) {
                        // Modo creación
                        doc.append("_id", new ObjectId()); // Generar un nuevo ID
                        collection.insertOne(doc);
                        JOptionPane.showMessageDialog(null, "Producto insertado con éxito");
                    } else {
                        // Modo actualización
                        collection.updateOne(new Document("_id", documento.getObjectId("_id")), new Document("$set", doc));
                        JOptionPane.showMessageDialog(null, "Producto actualizado con éxito");
                    }

                    JFrame frame = new JFrame("Cell Tech Hub");
                    frame.setContentPane(new GestionProductos().mainPanel);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setResizable(false);
                    frame.setLocationRelativeTo(null);
                    frame.setSize(1000, 630);
                    frame.setVisible(true);
                    ((JFrame) SwingUtilities.getWindowAncestor(mainPanel)).dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });
    }

    private void setupCreateMode() {
        // Configuración específica para el modo creación
        idTf.setEnabled(false);
        imagenRuta = "";
    }

    private void setupUpdateMode() {
        // Configuración específica para el modo actualización
        nombreTf.setText(documento.getString("nombre"));
        descripcionTf.setText(documento.getString("descripcion"));
        cantidadTf.setValue(documento.getInteger("cantidad"));
        precioTf.setText(documento.getDouble("precio").toString());
        imagenRuta = documento.getString("foto");

        // Cargar la imagen si está disponible
        if (imagenRuta != null && !imagenRuta.isEmpty()) {
            try {
                BufferedImage img = ImageIO.read(new File(imagenRuta));
                ImageIcon icon = new ImageIcon(img.getScaledInstance(imagenTf.getWidth(), imagenTf.getHeight(), java.awt.Image.SCALE_SMOOTH));
                imagenTf.setIcon(icon);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}