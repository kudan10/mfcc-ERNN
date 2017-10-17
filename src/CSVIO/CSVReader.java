/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CSVIO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author DAN
 */
public class CSVReader {    
    public int hitungJumlahDataTraining(String lokasiDataTraining){
        int index = 0;
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(lokasiDataTraining));
            while ((line=br.readLine()) != null) {
                index++;                
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return index;
    }
    
    /**
     *
     * @param lokasiDataTraining lokasi file training
     * @param jumlahBaris jumlah sample data training pada file CSV
     * @param jumlahKolom jumlah input neuron (fitur yang dimiliki oleh dataTraining)
     * @return array2d dari data training
     */
    public double[][] bacaDataTraining(String lokasiDataTraining, int jumlahBaris, int jumlahKolom) {
        //String csvFile = "E:\\MACHINELEARNING\\Iris dataset training.csv";
        double[][] dataTraining = new double[jumlahBaris][jumlahKolom];
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        int hitung = 0;
        int MAX_EPOCH = 10000;
        
        try {       
            br = new BufferedReader(new FileReader(lokasiDataTraining));
            int index = 0;

            while ((line = br.readLine()) != null){
                String[] data = line.split(csvSplitBy);
                for (int i = 0; i < jumlahKolom; i++) {
                    dataTraining[index][i] = Double.valueOf(data[i]);
                }
                
                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataTraining;
    }
    
    /**
     *
     * @param lokasiDataTraining file location of CSV file
     * @param jumlahIndeks banyaknya sample data
     * @param OUTPUT_NEURON jumlah output neuron
     * @return array target pelatihan untuk tiap-tiap sample data training
     */
    public double[][] bacaDataTarget(String lokasiDataTraining, int jumlahIndeks, int OUTPUT_NEURON, int banyakKolom){
        double[][] dataTarget = new double[jumlahIndeks][OUTPUT_NEURON];
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        int hitung = 0;
        int MAX_EPOCH = 10000;
        
        try {
            br = new BufferedReader(new FileReader(lokasiDataTraining));
            int index = 0;

            while ((line = br.readLine()) != null){
                String[] data = line.split(csvSplitBy);
                
                if(data[banyakKolom].equalsIgnoreCase("atas")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;
                    }
                    dataTarget[index][0] = 0.25;
                }else if(data[banyakKolom].equalsIgnoreCase("bawah")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][1] = 0.25;
                }else if(data[banyakKolom].equalsIgnoreCase("hapus")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][2] = 0.25;
                }else if(data[banyakKolom].equalsIgnoreCase("kanan")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][3] = 0.25;
                }else if(data[banyakKolom].equalsIgnoreCase("kiri")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][4] = 0.25;
                }else if(data[banyakKolom].equalsIgnoreCase("naik")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][5] = 0.25;
                }else if(data[banyakKolom].equalsIgnoreCase("potong")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][6] = 0.25;
                }else if(data[banyakKolom].equalsIgnoreCase("salin")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][7] = 0.25;
                }else if(data[banyakKolom].equalsIgnoreCase("tempel")){
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][8] = 0.25;
                }else{ //turun
                    for (int i = 0; i < 10; i++) {
                        dataTarget[index][i] = 0.0833;                        
                    }
                    dataTarget[index][9] = 0.25;
                }
                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dataTarget;
    }
}