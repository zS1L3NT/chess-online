package chess_client.online.Menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.*;

import chess_client.online.HTTP;
import chess_client.online.Threads.ClientThread;

public class UsnmSelectMenu implements KeyListener, ActionListener {
    private String host, usnm;

    private JFrame frame;
    private JPanel panel;
    private JLabel header, error;
    private JTextField input;
    private JButton button;

    public UsnmSelectMenu(String host) {
        this.frame = new JFrame("Username Select Menu");
        this.panel = new JPanel();
        this.host = host;

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(300, 400);
        this.frame.setVisible(true);
        this.frame.add(panel);

        this.panel.setLayout(null);

        this.header = new JLabel("Type in username!");
        this.header.setBounds(10, 20, 290, 25);
        this.panel.add(this.header);

        this.input = new JTextField(20);
        this.input.setBounds(10, 50, 165, 25);
        this.input.addKeyListener(this);
        this.panel.add(this.input);

        this.error = new JLabel("");
        this.error.setBounds(10, 80, 290, 25);
        this.panel.add(this.error);

        this.button = new JButton("Start Game");
        this.button.setBounds(10, 110, 100, 25);
        this.button.addActionListener(this);

        this.frame.revalidate();
        this.frame.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.error.setText("");
        this.panel.remove(this.button);
        reload();

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                String usnm = this.input.getText();
                if (isAlphanumeric(usnm)) {
                    String available = HTTP.GET(this.host + "/CheckUserExists", HTTP.MakeParamsWithUsnm(usnm));
                    if (available.equals("true")) {
                        this.panel.add(this.button);
                        this.usnm = usnm;
                        reload();
                    } else {
                        this.error.setText("\"" + usnm + "\" is already taken");
                        this.usnm = "";
                    }
                } else {
                    this.error.setText("Username must be alphanumeric");
                    this.usnm = "";
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.dispose();
        new ClientThread(this.host, this.usnm).start();
    }

    private void reload() {
        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * Method to check if string is alphanumeric
     * 
     * @param str String
     * @return boolean
     */
    private boolean isAlphanumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < 0x30 || (c >= 0x3a && c <= 0x40) || (c > 0x5a && c <= 0x60) || c > 0x7a)
                return false;
        }

        return true;
    }

}