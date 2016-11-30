package com.di.polytech.wang;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * this class is for the purpose to draw a histogram according to the data map.
 * and save it into jpeg.
 * Created by martinwang on 15/11/16.
 */
public class Histogram {
    /**
     * the histogram's title.
     */
    private final String Title;

    /**
     * the X axis's title.
     */
    private final String xAxisTitle;

    /**
     * the Y axis's title.
     */
    private final String yAxisTitle;

    /**
     * the date map.
     */
    private Map<Integer, Integer> dataMap;

    /**
     * the default value of image's width.
     */
    private int Width = 600;

    /**
     * the default value of image's height.
     */
    private int Height = 800;

    /**
     * the path to save the histogram image.
     */
    private String outputPath;

    private BufferedImage bufferedImage;
    /**
     * Constructor
     * @param Title the histogram's title.
     * @param xAxisTitle the X axis's title.
     * @param yAxisTitle the Y axis's title.
     * @param dataMap  the date map.
     * @param outputPath the path to save the histogram image.
     */
    public Histogram(String Title, String xAxisTitle, String yAxisTitle,
                     Map<Integer, Integer>dataMap, String outputPath){
        this.xAxisTitle = xAxisTitle;
        this.yAxisTitle = yAxisTitle;
        this.dataMap = dataMap;
        this.outputPath = outputPath;
        this.Title = Title + " mu: " + String.format("%.3f", calculateMu()) + " ,sigma: " +  String.format("%.3f",
                calculateSigma());
    }

    /**
     * Constructor
     * @param Title the histogram's title.
     * @param xAxisTitle the X axis's title.
     * @param yAxisTitle  the Y axis's title.
     * @param dataMap the date map.
     * @param outputPath the path to save the histogram image.
     * @param Width the value of image's width.
     * @param Height the value of image's height.
     */
    public Histogram(String Title, String xAxisTitle, String yAxisTitle,
                     Map<Integer, Integer>dataMap, String outputPath, int Width, int Height){
        this.xAxisTitle = xAxisTitle;
        this.yAxisTitle = yAxisTitle;
        this.dataMap = dataMap;
        this.outputPath = outputPath;
        if(Height > this.Height)
            this.Height = Height;
        if(Width > this.Width)
            this.Width = Width;
        this.Title = Title + " mu: " + String.format("%.3f", calculateMu()) + " ,sigma: " +  String.format("%.3f",
                calculateSigma());
    }

    /**
     * to draw and to save the histogram
     */
    public void drawAndSaveHistogram(){
        drawHistogram();
        saveHistogram();
    }

    /**
     * to draw the histogram
     */
    private void drawHistogram(){
        bufferedImage = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = bufferedImage.createGraphics();
        Font font = new Font("TimesRoman", Font.BOLD, 20);

        ig2.setFont(font);
        int edge = 60;
        int countKey = dataMap.keySet().size();
        int maxValue = Collections.max(dataMap.values()) + 1;
        //System.out.println(maxValue);
        int Ox = edge;
        int Oy  = Height - edge;
        int incX = (int)Math.round(( Width - 2 * edge ) * 1.0 / countKey);
        double incY = (Height - 2 * edge) * 1.0 / maxValue ;
        //System.out.println("incX:" + incX + " incY: " + incY);
        ig2.setBackground(Color.WHITE);

        ig2.setColor(Color.BLACK);
        AffineTransform orig = ig2.getTransform();
        ig2.rotate(-Math.PI/2);
        ig2.drawString(yAxisTitle, Height/2, edge/2);
        ig2.setTransform(orig);

        ig2.drawString(Title, edge, edge/2);
        ig2.drawString(xAxisTitle, Width - edge, Height - edge/2);

        ig2.drawLine(Ox, Oy, edge, edge);
        ig2.drawLine(Ox, Oy, Width - edge, Height - edge);

        List keyList = new ArrayList(dataMap.keySet());
        Collections.sort(keyList);

        for (Object key  : keyList) {
            int y = (int)Math.round(incY * dataMap.get(key));

            ig2.setColor(Color.BLUE);
            ig2.fillRect(Ox, Oy - y, incX/3 , y);
            ig2.setColor(Color.RED);
            ig2.drawString("" + key, Ox, Height - edge/2);
            ig2.setColor(Color.MAGENTA);
            ig2.drawString("" + dataMap.get(key), Ox, Oy - y - edge/6);
            Ox += incX;
        }
    }

    /**
     * to save the histogram
     */
    private void saveHistogram(){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss");
            ImageIO.write(bufferedImage, "JPEG", new File(outputPath, "Histogram_" + format.format(new Date()) + "_C-"+
                    calculateCount() + ".JPG" ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * to calculate the mean
     * @return the mean
     */
    private double calculateMu(){
        int count = calculateCount();
        double mean = 0.0;

        for (int key : dataMap.keySet()){
            mean += dataMap.get(key) * 1.0 / count * key;
        }
        return mean;
    }

    /**
     * to calculate the standard deviation
     * @return the deviation
     */
    private double calculateSigma(){
        int count = calculateCount();
        double variance = 0.0;
        double mean = calculateMu();

        for (int key : dataMap.keySet()){
            variance += dataMap.get(key) * 1.0 / count * Math.abs(key - mean);
            //System.out.println("key:" + key + " pro:" + dataMap.get(key) * 1.0 / count + " abs:" +  Math.abs(key -
            //mean));
        }

        //System.out.println(count);

        //System.out.println(mean);

        //System.out.println(Math.sqrt(variance));
        return Math.sqrt(variance);
    }

    /**
     * to calculate the count of instances.
     * @return the count of instances.
     */
    private int calculateCount(){
        int count = 0;
        for (int key : dataMap.keySet()){
            count += dataMap.get(key);
        }
        return count;
    }
}
