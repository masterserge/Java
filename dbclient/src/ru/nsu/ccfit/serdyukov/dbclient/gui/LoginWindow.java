package ru.nsu.ccfit.serdyukov.dbclient.gui;

import ru.nsu.ccfit.serdyukov.dbclient.res.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;

public class LoginWindow extends JDialog {

    private MainWindow mainWindow;

    private JTextField loginTextField;
    private JPasswordField passwordTestField;
    private JButton loginButton;


    LoginWindow(MainWindow mainWindow) {

        super();
        this.mainWindow = mainWindow;
        constructLoginFrame();
    }

    private void constructLoginFrame() {

        Container pane = getContentPane();
        pane.setLayout(new GridBagLayout());

        loginTextField = new JTextField("serdukov");
        passwordTestField = new JPasswordField("12345");
        loginButton = new JButton(Strings.SUBMIT_LOGIN_BUTTON);

        loginButton.addActionListener(new LoginListener());

        JPanel loginPanel = new JPanel();
        loginPanel.add(loginTextField);
        loginPanel.add(passwordTestField);
        loginPanel.add(loginButton);

        pane.add(loginPanel);
    }

    private class LoginListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                Class.forName(Strings.DRIVER_NAME);
                mainWindow.connection =
                        DriverManager.getConnection(Strings.URL, loginTextField.getText(),
                                new String(passwordTestField.getPassword()));
                mainWindow.connection.setAutoCommit(true);
                mainWindow.reloadAndShowTables();
                mainWindow.showLogonInfo();

                dispose();
            } catch (Exception e1) {
                mainWindow.errorLabel.setText(e1.getMessage());
                e1.printStackTrace();
            }
        }
    }
}
