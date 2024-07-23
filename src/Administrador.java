import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Administrador {
    public JPanel mainPanel;
    private JButton eliminarB;
    private JButton editarB;
    private JButton cerrarCesionButton;
    private JLabel Foto;

    public Administrador() {
        eliminarB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon nuevaImagen = new ImageIcon(getClass().getResource("/imagenes/img_4.png"));
                Foto.setIcon(nuevaImagen);

            }
        });
    }
}
