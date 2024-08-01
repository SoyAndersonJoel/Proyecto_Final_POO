import com.mongodb.client.*;
import org.bson.Document;

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
    private Productos producto1;

    public Crear() {

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
                int cantidad = (int) cantidadTf.getValue();
                double precio = Double.parseDouble(precioTf.getText());

                Productos producto1 = new Productos(nombre, descripcion, cantidad, precio, imagenRuta);

                // Insertar el producto en MongoDB
                try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                    MongoDatabase database = mongoClient.getDatabase("celltechhub");
                    MongoCollection<Document> collection = database.getCollection("Productos");

                    Document documento = new Document("_id", producto1.getId())
                            .append("nombre", producto1.getNombre())
                            .append("descripcion", producto1.getDescripcion())
                            .append("cantidad", producto1.getCantidad())
                            .append("precio", producto1.getPrecio())
                            .append("foto", producto1.getFoto());

                    collection.insertOne(documento);
                    JOptionPane.showMessageDialog(null, "Producto insertado con éxito");

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
                    JOptionPane.showMessageDialog(null, "Error al insertar el producto: " + ex.getMessage());
                }


            }
        });



    }
}
