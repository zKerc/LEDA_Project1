package Ordenacao.InsertionSort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InsertionSortVenue {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/InsertionSort/";
    private String outputMedio = path + "matches_t2_venues_insertionSort_medioCaso.csv";
    private int venueIndex = 7;

    public InsertionSortVenue(String inputFile) {
        this.inputFile = inputFile;
    }

    public void ordenar() {
        ordenarMelhorCaso();
        ordenarMedioCaso();
        ordenarPiorCaso();
    }

    private void ordenarMedioCaso() {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(outputMedio)) {

            // Escrever cabeçalho
            String line;
            if ((line = br.readLine()) != null) {
                writer.write(line + "\n");
            }

            int rowCount = 0;

            // Calcular o número de linhas no arquivo
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            while (reader.readLine() != null) rowCount++;
            reader.close();

            String[][] data = new String[rowCount][14];

            rowCount = 0;
            while ((line = br.readLine()) != null) {
                data[rowCount++] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            }

            // Embaralhar metade dos dados
            Random rand = new Random();
            for (int i = 0; i < rowCount / 2; i++) {
                int j = rand.nextInt(rowCount / 2);
                String[] temp = data[i];
                data[i] = data[j];
                data[j] = temp;
            }

            ordenacao(data, venueIndex, rowCount);

            // Escrever no arquivo
            for (int i = 0; i < rowCount; i++) {
                writer.write(String.join(",", data[i]) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ordenarMelhorCaso() {
        String outputFile = "src/OrdenacaoResultados/InsertionSort/matches_t2_venues_insertionSort_melhorCaso.csv";
        ordenacao(inputFile, outputFile, false, true);
    }

    private void ordenarPiorCaso() {
        String outputFile = "src/OrdenacaoResultados/InsertionSort/matches_t2_venues_insertionSort_piorCaso.csv";
        ordenacao(inputFile, outputFile, true, false);
    }

    private void ordenacao(String[][] data, int venueIndex, int rowCount) {
        for (int i = 1; i < rowCount; i++) {
            String[] key = data[i];
            int j = i - 1;
    
            while (j >= 0 && (data[j][venueIndex].isEmpty() || data[j][venueIndex].compareTo(key[venueIndex]) > 0)) {
                data[j + 1] = data[j];
                j = j - 1;
            }
            data[j + 1] = key;
        }
    }    

    private void ordenacao(String inputFile, String outputFile, boolean inverter, boolean melhorCaso) {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(outputFile)) {

            String line;
            if ((line = br.readLine()) != null) {
                writer.write(line + "\n");
            }

            int venueIndex = 7;
            int rowCount = 0;

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            while (reader.readLine() != null) rowCount++;
            reader.close();

            String[][] data = new String[rowCount][14];

            rowCount = 0;
            while ((line = br.readLine()) != null) {
                String[] values = new String[14];
                StringBuilder sb = new StringBuilder();
                int valueCount = 0;
                boolean insideQuotes = false;
                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        insideQuotes = !insideQuotes;
                    } else if (c == ',' && !insideQuotes) {
                        values[valueCount++] = sb.toString();
                        sb.setLength(0);
                    } else {
                        sb.append(c);
                    }
                }
                values[valueCount] = sb.toString();
                data[rowCount++] = values;
            }

            for (int i = 1; i < rowCount; i++) {
                String[] key = data[i];
                int j = i - 1;
        
                if (melhorCaso) {
                    while (j >= 0 && data[j][venueIndex].compareTo(key[venueIndex]) > 0) {
                        data[j + 1] = data[j];
                        j = j - 1;
                    }
                } else {
                    while (j >= 0 && (data[j][venueIndex].isEmpty() || data[j][venueIndex].compareTo(key[venueIndex]) > 0)) {
                        data[j + 1] = data[j];
                        j = j - 1;
                    }
                }
                data[j + 1] = key;
            }

            if (inverter) {
                String[][] dataInvertida = new String[rowCount][14];
                for (int i = 0; i < rowCount; i++) {
                    dataInvertida[i] = data[rowCount - i - 1];
                }
                data = dataInvertida;
            }

            for (int i = 0; i < rowCount; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < data[i].length; j++) {
                    String value = data[i][j];
                    if (value.contains(",")) {
                        sb.append('"').append(value).append('"');
                    } else {
                        sb.append(value);
                    }
                    if (j < data[i].length - 1) {
                        sb.append(",");
                    }
                }
                writer.write(sb.toString() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
