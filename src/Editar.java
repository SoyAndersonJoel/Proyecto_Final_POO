import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Editar {
    private JButton cerrarCesionButton;
    private JTextField textField1;
    private JTextField textField2;
    private JSpinner spinner1;
    public JPanel mainPanel;
    private JTextField textField3;
    private JTextField textField4;
    private JButton agregarUnaImagenButton;
    private JButton button1;
    private JButton button2;
    private JButton regresarButton;

    public Editar() {
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Cell Tech Hub");
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
    }
}
