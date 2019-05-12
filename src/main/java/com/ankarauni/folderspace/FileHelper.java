/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ankarauni.folderspace;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicLong;

/**
 * File helper class
 * @author Gorkem
 */
public class FileHelper {
    
    // Converts bytes to B, kB, MB, etc.
    // Result is formatted like ##.#
    // 1000 bytes convert into 1.0 KB. This is an intended behaviour observed
    // in the "du" command in Linux systems.
    private static String convertBytesToString(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ("kMGTPE").charAt(exp-1) + ("");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
    
    // Displayed file creation dates are all in Turkish Date/Time Locale
    public static String convertFileDateToTurkishLocale
        (BasicFileAttributes fileAttributes) {
        
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateCreated = df.format(fileAttributes.creationTime()
                .toMillis());
        
        return dateCreated;
    }
    
    public static BasicFileAttributes getFileAttributes(File file) 
            throws IOException {
        return Files.readAttributes(file.toPath(),BasicFileAttributes.class);
    }
    
    // Folder sizes cannot be obtained from file attributes. We need to traverse
    // the folders and sum all ancestor file sizes in order to 
    // obtain the actual folder size.
    // With the modified file visitor class, the algorithm can continue to run
    // even when encountering symbolic links, missing permissions and concurrent
    // modification exceptions.
    public static String fileSize(Path filePath) {
        // size variable is accessed from anonymous class, therefore needs to
        // be final. 
        final AtomicLong size = new AtomicLong(0);
        
        try {
            Files.walkFileTree(filePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    // add file size to the result
                    size.addAndGet(attributes.size());
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException ex) {
                    // skip directory
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException ex) {
                    // ignore errors
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        return convertBytesToString(size.get());
    }
    
    // getFreeSpace() and getUsableSpace() methods have a significant difference
    // when it comes to cross-platform compatibility.
    // getUsableSpace() returns the number of bytes available to this virtual
    // machine on the partition named by this abstract pathname. When possible,
    // this method checks for write permissions and other OS restrictions and
    // will therefore usually provide a more accurate estimate of how much
    // new data can actually be written than getFreeSpace()
    public static String getUsableSpaceOnDisk(File file) {
        return convertBytesToString(file.getUsableSpace());
    }
}
