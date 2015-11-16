package ru.nsu.ccfit.serdyukov.dbclient.gui;

import ru.nsu.ccfit.serdyukov.dbclient.sql.QueryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RenameColumnWindow extends JDialog {

    private MainWindow mainWindow;

    private JPanel columnPanel;
    private JComboBox cb;
    private JTextField nameField;

    private RenameActionListener ral = new RenameActionListener();

    private String tableName;

    RenameColumnWindow(MainWindow mainWindow) {
        super();
        this.mainWindow = mainWindow;
        tableName = mainWindow.getNameOfSelectedTable();
        constructAddColumnFrame();
    }

    private void constructAddColumnFrame() {

        Container pane = getContentPane();

        columnPanel = new JPanel(new GridLayout(0, 3));

        String[] columns = null;
        try {
            columns = QueryBuilder.selectColumnsFromTable(mainWindow.connection, tableName);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        cb = new JComboBox(columns);
        columnPanel.add(cb);

        nameField = new JTextField(10);
        columnPanel.add(nameField);

        JButton dropButton = new JButton("Rename");
        dropButton.addActionListener(ral);
        columnPanel.add(dropButton);

        pane.add(columnPanel, BorderLayout.SOUTH);
    }

    private class RenameActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String oldColumnName = (String)cb.getSelectedItem();
            String newColumnName = nameField.getText();

            try {
                ResultSet rs = QueryBuilder.renameColumnAndReturnUpdatedTable(
                        mainWindow.connection,
                        tableName,
                        mainWindow.tableOfResultsModel,
                        oldColumnName,
                        newColumnName);
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