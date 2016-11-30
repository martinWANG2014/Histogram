package com.di.polytech.wang;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by martinwang on 16/11/16.
 */
public class FileUtils {
    /**
     * the method to read data into data map from the files.
     * @param files
     * @return
     */
    public static Map<Integer, Integer>  ReadDataFromFiles(File[] files) {
        Map<Integer, Integer> map = new HashMap<>();
        FileReader reader = null;
        BufferedReader bufferedReader = null;
        String line;
        for( File file : files){
            try {
                reader = new FileReader(file);
                bufferedReader = new BufferedReader(reader);

                while((line = bufferedReader.readLine()) != null){
                    String[] objects  = line.split(";");
                    if(objects.length == 2){
                        int value = Integer.parseInt(objects[1]);
                        int key = Integer.parseInt(objects[0]);
                        if (key > 0){
                            if(map.containsKey(key)){//objects[0])){
                                map.put(key, map.get(key) + value);
                                //map.put(objects[0], map.get(objects[0]) + value);
                            }
                            else{
                                map.put(key, value);
                                //map.put(objects[0], value);
                            }
                        }
                    }
                }

                bufferedReader.close();
                reader.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    bufferedReader.close();
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

}
