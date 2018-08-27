import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.BiConsumer;

public class ConvertUI implements KeyListener, ActionListener {
    public JPanel mainPanel;
    private JTextField textField;
    private JButton exportButton;

    public ConvertUI(BiConsumer<String, Runnable> export) {
        textField.addKeyListener(this);
        textField.addActionListener(this);
        textField.setText("Username/Repository");
        textField.setForeground(Color.GRAY);
        exportButton.setEnabled(false);
        exportButton.addActionListener((e) -> {
            exportButton.setEnabled(false);
            textField.setEnabled(false);
            export.accept(textField.getText(), this::callRecover); // require to be async
        });
        mainPanel.requestFocus();
    }

    private void callRecover() {
        textField.setEnabled(true);
        textField.setText("Username/Repository");
        textField.setForeground(Color.GRAY);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (textField.getText().equals("Username/Repository")) {
            textField.setText("");
            textField.setForeground(Color.BLACK);
        }
        if (textField.getText().matches(".+/.+"))
            exportButton.setEnabled(true);
        else exportButton.setEnabled(false);
        if (e.getKeyChar() == KeyEvent.VK_ENTER && exportButton.isEnabled()) exportButton.doClick();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
