package chess_client.online.Menus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import chess_client.online.HTTP;
import chess_client.online.User;

public class OpponentSelectMenu implements KeyListener {
    private List<User> users;
    private String host, usnm;
    public String request = "";
    public boolean done = false;

    private JFrame frame;
    private JPanel panel;
    private JLabel header, status, hashText, error, usersText;
    private JTextField input;

    public OpponentSelectMenu(String host, String usnm, List<User> users) {
        this.frame = new JFrame("Opponent Select Menu");
        this.panel = new JPanel();
        this.host = host;
        this.usnm = usnm;

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(300, 400);
        this.frame.setVisible(true);
        this.frame.add(panel);

        this.panel.setLayout(null);

        this.header = new JLabel("Waiting for list of users...");
        this.header.setBounds(10, 20, 290, 25);
        this.panel.add(this.header);

        this.status = new JLabel("Requested: " + this.request);
        this.status.setBounds(10, 50, 290, 25);
        this.panel.add(this.status);

        this.hashText = new JLabel("#  >  ");
        this.hashText.setBounds(10, 80, 280, 25);
        this.panel.add(this.hashText);

        this.input = new JTextField(20);
        this.input.setBounds(40, 80, 165, 25);
        this.input.addKeyListener(this);
        this.panel.add(this.input);

        this.error = new JLabel("");
        this.error.setBounds(10, 110, 280, 25);
        this.panel.add(this.error);

        this.usersText = new JLabel("<html><body></body></html>");
        this.usersText.setBounds(10, 140, 280, 290);
        this.usersText.setVerticalAlignment(JLabel.TOP);
        this.usersText.setHorizontalAlignment(JLabel.LEFT);
        this.panel.add(this.usersText);

        updateUsers(users);
    }

    public List<User> getOpponents() {
        int indexOfMe = -1;
        for (int i = 0; i < this.users.size(); i++) {
            if (this.users.get(i).usnm().equals(this.usnm)) {
                indexOfMe = i;
                break;
            }
        }

        if (indexOfMe == -1)
            return new ArrayList<User>();

        List<User> opponents = new ArrayList<User>(this.users);
        opponents.remove(indexOfMe);
        return opponents;
    }

    /**
     * Method to display menu
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    private void displayMenu() throws IOException, InterruptedException {
        List<User> opponents = getOpponents();

        if (opponents.size() == 0) {
            this.header.setText("Sorry " + this.usnm + ", nobody is online now");
            this.usersText.setText("");
        } else {
            this.header.setText(this.usnm + ", select new user to play with!");

            String text = "<html><body>";
            for (int i = 0; i < opponents.size(); i++) {
                User user = opponents.get(i);
                if (user.inGame()) continue;
                text += "(" + (i + 1) + ")  " + user.usnm() + "<" + user.request() + "><br>";
            }
            text += "</body></html>";
            this.usersText.setText(text);
        }
    }

    /**
     * Method to update users in radios and reprint them to the screen
     * 
     * @param users
     */
    public void updateUsers(List<User> users) {
        this.users = users;

        // Check if game starting
        this.users.forEach(user -> {
            if (user.usnm().equals(this.usnm) && user.inGame()) {
                this.frame.dispose();
                this.done = true;
            }
        });

        refresh();
    }

    /**
     * Refresh current frame and calls displayMenu()
     */
    public void refresh() {
        this.frame.revalidate();
        this.frame.repaint();

        try {
            displayMenu();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wait a specific amount of time in milliseconds
     * 
     * @param ms
     */
    public final static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        HashMap<String, String> params = HTTP.MakeParamsWithUsnm(usnm);
        this.error.setText("");

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String input = this.input.getText();
            this.input.setText("");
            try {
                if (input.equals("")) {
                    if (this.request.equals(""))
                        return;
                    params.put("request", "");

                    this.request = "";
                    this.status.setText("Requested: ");
                    HTTP.GET(this.host + "/SetRequest", params);
                } else {
                    int number = Integer.parseInt(input) - 1;
                    List<User> opponents = getOpponents();

                    if (number < 0 || number >= opponents.size()) {
                        this.error.setText("Number isn't bound to any of the listed users");
                    } else {
                        User user = opponents.get(number);
                        String newStatus = "Requested: " + user.usnm();

                        if (this.status.getText().equals(newStatus)) {
                            this.error.setText("Already requested to play with " + user.usnm() + "!");
                        } else {
                            this.status.setText(newStatus);
                            this.request = user.usnm();

                            params.put("request", user.usnm());
                            HTTP.GET(this.host + "/SetRequest", params);
                        }

                    }
                }
            } catch (NumberFormatException ex) {
                this.error.setText("Input not a number");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}