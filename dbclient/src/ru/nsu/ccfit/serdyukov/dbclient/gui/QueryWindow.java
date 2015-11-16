package ru.nsu.ccfit.serdyukov.dbclient.gui;

import ru.nsu.ccfit.serdyukov.dbclient.res.Strings;
import ru.nsu.ccfit.serdyukov.dbclient.sql.QueryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryWindow extends JDialog {

    private MainWindow mainWindow;

    private JPanel queryPanel;
    private JTextArea queryTextArea;
    private JButton queryExecuteButton;

    QueryWindow(MainWindow mainWindow) {

        super();
        this.mainWindow = mainWindow;
        constructQueryFrame();
    }

    private void constructQueryFrame() {

        Container pane = getContentPane();

        queryPanel = new JPanel();
        queryTextArea = new JTextArea(10, 50);
        queryExecuteButton = new JButton(Strings.EXECUTE_QUERY_BUTTON);
        queryPanel.add(queryTextArea);
        queryPanel.add(queryExecuteButton);

        queryExecuteButton.addActionListener(new QueryListener());
        pane.add(queryPanel);
    }

    private class QueryListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String query = queryTextArea.getText();

            if (query == null || query.length() == 0) {
                mainWindow.errorLabel.setText(Strings.EMPTY_QUERY);
                return;
            }
            if (mainWindow.connection == null) {
                mainWindow.errorLabel.setText(Strings.NO_CONNECTION);
                return;
            }

            try {
                ResultSet rs = QueryBuilder.executeQueryAndReturnResult(mainWindow.connection, query);
                mainWindow.showQueryResult(rs);
                mainWindow.resetTableListSelection();
                mainWindow.errorLabel.setText(query);
                dispose();
            } catch (SQLException e1) {
                mainWindow.errorLabel.setText(e1.getMessage());
                e1.printStackTrace();
            }
        }
    }
}
