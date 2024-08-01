import com.mongodb.client.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.bson.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GestionProductos {
    public JPanel mainPanel;
    private JButton eliminarB;
    private JButton editarB;
    private JButton cerrarCesionButton;
    private JLabel foto;
    private JPanel uno;
    private JButton agregarProductoButton;
    private JButton eliminarButton;
    private JButton editarButton;
    private JLabel foto2;
    private JLabel nombreTxt;
    private JLabel presio2Txt;
    private JLabel id2Txt;
    private JLabel Id1Txt;
    private JLabel nombre1Txt;
    public JLabel UltIdTxt;
    private JLabel UltNameTxt;
    public JLabel UltPrecioTxt;
    private JLabel UltCantidadTxt;
    private JLabel UltDescripTxt;
    private JLabel UltFoto;

    private NuevoProducto nuevoproducto;

    public GestionProductos() {
        nuevoproducto =new NuevoProducto();
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
        editarB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Cell Tech Hub");
                frame.setContentPane(new Editar().mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setSize(1000, 630);
                frame.setVisible(true);
                ((JFrame) SwingUtilities.getWindowAncestor(mainPanel)).dispose();
            }
        });
        agregarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Cell Tech Hub");
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

        Productos ultimoProducto = nuevoproducto.obtenerProducto();
        if (ultimoProducto != null) {
            UltNameTxt.setText(ultimoProducto.getNombre());
            UltPrecioTxt.setText(String.valueOf(ultimoProducto.getPrecio()));
            UltCantidadTxt.setText(String.valueOf(ultimoProducto.getCantidad()));
            UltDescripTxt.setText(ultimoProducto.getDescripcion());

            String fotoPath = ultimoProducto.getFoto();
            if (fotoPath != null) {
                try {
                    BufferedImage img = ImageIO.read(new File(fotoPath));
                    ImageIcon icon = new ImageIcon(img);
                    UltFoto.setIcon(icon);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        nuevoproducto.cerrarConexion();





    }

}
