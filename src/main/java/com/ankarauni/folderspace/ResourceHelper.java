/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ankarauni.folderspace;

import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Resource helper class that utilizes lazy loading.
 * 
 * @author Gorkem
 */
public class ResourceHelper {
    
    private static final String FILE_ICON_RESOURCE_PATH = "icons/file.png";
    private static final String FOLDER_ICON_RESOURCE_PATH = "icons/folder.png";
    
    private static Icon fileIcon, folderIcon;
    
    public static Icon getFileIcon() {
        if (fileIcon == null) {
            fileIcon = createImageIcon(FILE_ICON_RESOURCE_PATH);
        }
        
        return fileIcon;
    }
    
    public static Icon getFolderIcon() {
        if (folderIcon == null) {
            folderIcon = createImageIcon(FOLDER_ICON_RESOURCE_PATH);
        }
        
        return folderIcon;
    }
    
    private static Icon createImageIcon(String resourcePath) {
        return new ImageIcon(getResourceUrl(resourcePath));
    }
    
    private static URL getResourceUrl(String resourcePath) {
        return ClassLoader.getSystemResource(resourcePath);
    }
}
