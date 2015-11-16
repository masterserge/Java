package ru.nsu.ccfit.serdyukov.dbclient.gui;

import ru.nsu.ccfit.serdyukov.dbclient.sql.QueryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DropColumnWindow extends JDialog {

    private MainWindow mainWindow;

    private JPanel columnPanel;
    private JComboBox cb;

    private DropActionListener dal = new DropActionListener();

    private String tableName;

    DropColumnWindow(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        tableName = mainWindow.getNameOfSelectedTable();
        constructAddColumnFrame();
    }

    private void constructAddColumnFrame() {

        Container pane = getContentPane();

        columnPanel = new JPanel(new GridLayout(0, 2));

        String[] columns = null;
        try {
            columns = QueryBuilder.selectColumnsFromTable(mainWindow.connection, tableName);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        cb = new JComboBox(columns);
        columnPanel.add(cb);

        JButton dropButton = new JButton("Drop");
        dropButton.setActionCommand("drop");
        dropButton.addActionListener(dal);
        columnPanel.add(dropButton, BorderLayout.WEST);

        pane.add(columnPanel, BorderLayout.SOUTH);
    }

    private class DropActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String column = (String)cb.getSelectedItem();

            try {
                ResultSet rs = QueryBuilder.dropColumnAndReturnUpdatedTable(
                        mainWindow.connection,
                        tableName,
                        mainWindow.tableOfResultsModel,
                        column);
                mainWindow.showQueryResult(rs);
                mainWindow.errorLabel.setText("");
                dispose();
            } catch (SQLException e1) {
                mainWindow.errorLabel.setText(e1.getMessage());
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private int getComponentIndex(Component component) {
        if (component != null && component.getParent() != null) {
            Container c = component.getParent();
            for (int i = 0; i < c.getComponentCount(); i++) {
                if (c.getComponent(i) == component)
                    return i;
            }
        }
        return -1;
    }
}