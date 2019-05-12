/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ankarauni.folderspace;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gorkem
 */
public class FolderSpace {
    
    public static void main(String args[]) {
        MainForm form = new MainForm();
        
        DefaultTableModel model = CustomTableModel.getEmptyTable();
        
        form.registerTableModel(model);
        form.setVisible(true);
    }
    
    
}
