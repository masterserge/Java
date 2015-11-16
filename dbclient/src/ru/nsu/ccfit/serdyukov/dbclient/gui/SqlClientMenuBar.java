package ru.nsu.ccfit.serdyukov.dbclient.gui;

import ru.nsu.ccfit.serdyukov.dbclient.res.Strings;

import javax.swing.*;
import java.awt.event.ActionListener;

public class SqlClientMenuBar extends JMenuBar {

    private ActionListener menuBarListener;

    public SqlClientMenuBar(ActionListener menuBarListener) {
        super();

        this.menuBarListener = menuBarListener;

        createConnectionMenu();
        createQueryMenu();
        createRowMenu();
        createTableMenu();
        createColumnMenu();
        //createConstraintMenu();
    }

    private void createConnectionMenu() {
        JMenu menu = new JMenu(Strings.MENU_CONNECTION);
        JMenuItem menuItem = new JMenuItem(Strings.LOG_ON);

        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menuItem = new JMenuItem(Strings.LOG_OUT);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem(Strings.EXIT);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        add(menu);
    }

    private void createQueryMenu() {
        JMenu menu = new JMenu(Strings.MENU_QUERY);
        JMenuItem menuItem = new JMenuItem(Strings.EXECUTE_QUERY);

        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        add(menu);
    }

    private void createRowMenu() {
        JMenu menu = new JMenu(Strings.ROW_MENU);
        JMenuItem menuItem = new JMenuItem(Strings.INSERT_ROW);

        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menuItem = new JMenuItem(Strings.EDIT_ROW);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menuItem = new JMenuItem(Strings.DELETE_ROW);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        add(menu);
    }

    private void createTableMenu() {
        JMenu menu = new JMenu(Strings.TABLE_MENU);

        JMenuItem menuItem = new JMenuItem(Strings.DELETE_TABLE);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menuItem = new JMenuItem(Strings.RENAME_TABLE);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menuItem = new JMenuItem(Strings.CREATE_TABLE);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menuItem = new JMenuItem(Strings.DESCRIBE_TABLE);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        add(menu);
    }

    private void createColumnMenu() {

        JMenu menu = new JMenu(Strings.COLUMN_MENU);

        JMenuItem menuItem = new JMenuItem(Strings.ADD_COLUMN);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menuItem = new JMenuItem(Strings.MODIFY_COLUMN);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menuItem = new JMenuItem(Strings.RENAME_COLUMN);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menuItem = new JMenuItem(Strings.DROP_COLUMN);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        add(menu);
    }

    private void createConstraintMenu() {
        JMenu menu = new JMenu(Strings.CONSTRAINT_MENU);

        JMenuItem menuItem = new JMenuItem(Strings.WATCH_CONSTRAINTS);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menuItem = new JMenuItem(Strings.DROP_CONSTRAINT);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menuItem = new JMenuItem(Strings.CREATE_PK);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        menuItem = new JMenuItem(Strings.CREATE_FK);
        menuItem.addActionListener(menuBarListener);
        menu.add(menuItem);

        add(menu);
    }
}
