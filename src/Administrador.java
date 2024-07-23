import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Administrador {
    public JPanel mainPanel;
    private JButton eliminarB;
    private JButton editarB;
    private JButton cerrarCesionButton;
    private JLabel Foto;
    private JPanel uno;

    public Administrador() {
        eliminarB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                ImageIcon nuevaImagen = new ImageIcon(getClass().getResource("/imagenes/img_4.png"));
//                Foto.setIcon(nuevaImagen);
//                // Cambiar el texto del botón eliminarB a "agregar"
//                eliminarB.setText("agregar");
//
//                // Ocultar el botón editarB
//                editarB.setVisible(false);
//                JFrame uno = new JFrame();
//                uno.setContentPane(new Administrador().uno);
//                uno.setLocationRelativeTo(null);
//                uno.setResizable(false);
//                uno.setSize(500,500);

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Seleccionar imagen");

                // Filtrar solo archivos de imagen
                fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$");
                    }

                    @Override
                    public String getDescription() {
                        return "Archivos de imagen (jpg, jpeg, png, gif)";
                    }
                });

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    ImageIcon nuevaImagen = new ImageIcon(selectedFile.getAbsolutePath());
                    Foto.setIcon(nuevaImagen);
                }

                // Cambiar el texto del botón eliminarB a "agregar"
                eliminarB.setText("agregar");

                // Ocultar el botón editarB
                editarB.setVisible(false);


            }
        });
    }
}
