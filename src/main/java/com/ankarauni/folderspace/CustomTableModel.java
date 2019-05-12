/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ankarauni.folderspace;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 * Extending the DefaultTableModel provides future flexibility for generating
 * and presenting more complex data.
 * 
 * @author Gorkem
 */
public class CustomTableModel extends DefaultTableModel {
    private CustomTableModel() {
        super();
    }
    
    private CustomTableModel(Vector columnNames, int rowCount) {
        super(columnNames, rowCount);
    }
    
    private CustomTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }
    
    private CustomTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }
    
    private CustomTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }
    
    // JTable supports different renderers by default.
    // By overriding this method, every column is automatically gets assigned
    // the appropriate renderer by looking at object types of 
    // the first data row.
    @Override
    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }
    
    // Disable cell editing
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    public static CustomTableModel createModelFromFiles(File[] files) {
        CustomTableModel model = CustomTableModel.getEmptyTable();
        Object[] row;
        BasicFileAttributes fileAttributes;
        Icon icon;
        
        for (File file : files) {
            try {
                fileAttributes = FileHelper.getFileAttributes(file);
            } catch(IOException exception) {
                System.out.println("IO Exception: " + exception.getMessage());
                continue;
            }
            
            if (file.isDirectory()) {
                icon = ResourceHelper.getFolderIcon();
            } else {
                icon = ResourceHelper.getFileIcon();
            }
            
            String dateCreated = FileHelper
                    .convertFileDateToTurkishLocale(fileAttributes);
            
            String fileSize = FileHelper.fileSize(file.toPath());
            
            row = new Object[]{
                icon,
                file.getName(),
                dateCreated,
                fileSize,
            };
            
            model.addRow(row);
        }
        
        return model;
    }
    
    public static CustomTableModel getEmptyTable() {
        return new CustomTableModel(COLUMN_NAMES, 0);
    }
    
    private static Object[] COLUMN_NAMES = {"Icon", "File", "Created", "Size"};
}
