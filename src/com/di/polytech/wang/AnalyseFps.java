package com.di.polytech.wang;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by martinwang on 16/11/16.
 */
public class AnalyseFps extends JFrame {
    private File[] files = null;

    public AnalyseFps(){
        files =  SelectFiles();
        if (files == null) return;
        else {
            Histogram histogram = new Histogram("Fps statistic in " + files.length + "*10 min", "fps", "counter",
                    FileUtils.ReadDataFromFiles(files), files[0].getParentFile().getParent(),800, 600);
            histogram.drawAndSaveHistogram();
            dispose();
        }
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        new AnalyseFps();
    }

    /**
     * to select the files.
     * @return the selected files
     */
    private File[] SelectFiles(){
        JFileChooser fileChooser = new JFileChooser("Select csv file");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int showOpenDialog = fileChooser.showOpenDialog(this);

        if(showOpenDialog != JFileChooser.APPROVE_OPTION){
            dispose();
            return null;
        }
        return fileChooser.getSelectedFiles();
    }
}
