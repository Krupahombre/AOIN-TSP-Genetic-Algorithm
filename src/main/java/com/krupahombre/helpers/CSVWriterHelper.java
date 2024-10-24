package com.krupahombre.helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriterHelper {
    private String csvFile;

    public CSVWriterHelper(String csvFile) {
        this.csvFile = csvFile;
    }

    public void writeToCsvFinalResults(String[] data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                sb.append(data[i]);
                if (i < data.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("\n");
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
