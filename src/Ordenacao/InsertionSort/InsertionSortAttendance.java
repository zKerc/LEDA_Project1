package Ordenacao.InsertionSort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class InsertionSortAttendance {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/InsertionSort/";
    private String outputMedio = path + "matches_t2_attendance_insertionSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_attendance_insertionSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_attendance_insertionSort_piorCaso.csv";
    private int attendanceIndex = 6;

    public InsertionSortAttendance(String inputFile) {
        this.inputFile = inputFile;
    }

    public void ordenar() {
        criarCasoMelhor();
        criarCasoMedio();
        criarCasoPior();
        
        ordenarEImprimirTempo(outputMelhor);
        ordenarEImprimirTempo(outputMedio);
        ordenarEImprimirTempo(outputPior);
    }

    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    private void criarCasoMelhor() {
        try {
            // Contar linhas do arquivo
            int rowCount = contarLinhas(inputFile);
    
            String[][] data = carregarArquivoEmArray(inputFile, rowCount);
    
            ordenarArray(data, attendanceIndex, rowCount); // Ordenando o array
    
            // Escrevendo no arquivo de saída
            try (FileWriter writer = new FileWriter(outputMelhor)) {
                for (int i = 0; i < rowCount; i++) {
                    writer.write(String.join(",", data[i]) + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void criarCasoPior() {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             FileWriter writer = new FileWriter(outputPior, false)) {
    
            int rowCount = 0;
            while (br.readLine() != null) rowCount++;
    
            br.close();
    
            String[][] data = new String[rowCount][14];
    
            BufferedReader newBr = new BufferedReader(new FileReader(inputFile));
            int index = 0;
            String line;
            while ((line = newBr.readLine()) != null) {
                data[index++] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            }
            newBr.close();
    
            String[] header = data[0]; // Separando o cabeçalho
            String[][] dataArray = new String[rowCount - 1][14]; // Array para os dados sem cabeçalho
            System.arraycopy(data, 1, dataArray, 0, rowCount - 1); // Copiando os dados sem o cabeçalho
    
            ordenarArray(dataArray, attendanceIndex, rowCount - 1);
    
            for (int i = 0; i < dataArray.length / 2; i++) {
                String[] temp = dataArray[i];
                dataArray[i] = dataArray[dataArray.length - i - 1];
                dataArray[dataArray.length - i - 1] = temp;
            }
    
            writer.write(String.join(",", header) + "\n"); // Escrevendo o cabeçalho primeiro
            for (int i = 0; i < rowCount - 1; i++) { // Escrevendo os dados invertidos
                writer.write(String.join(",", dataArray[i]) + "\n");
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private void copiarArquivo(String origem, String destino) {
        try (BufferedReader br = new BufferedReader(new FileReader(origem));
             FileWriter writer = new FileWriter(destino)) {
            String line;
            while ((line = br.readLine()) != null) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ordenarEImprimirTempo(String fileToOrder) {
        try {
            // Contar linhas do arquivo
            int rowCount = contarLinhas(fileToOrder);
    
            String[][] data = carregarArquivoEmArray(fileToOrder, rowCount);
    
            String[][] dataArray = new String[rowCount - 1][14]; // Array para os dados sem cabeçalho
            System.arraycopy(data, 1, dataArray, 0, rowCount - 1); // Copiando os dados sem o cabeçalho
    
            // Início da medição de tempo
            long startTime = System.currentTimeMillis();
    
            ordenarArray(dataArray, attendanceIndex, rowCount - 1);
    
            // Fim da medição de tempo
            long endTime = System.currentTimeMillis();
            System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
    
            // NÃO gravaremos os dados ordenados no arquivo para manter os casos como estão
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private int contarLinhas(String fileToOrder) throws IOException {
        int rowCount = 0;
        try (BufferedReader counter = new BufferedReader(new FileReader(fileToOrder))) {
            while (counter.readLine() != null) rowCount++;
        }
        return rowCount;
    }
    
    private String[][] carregarArquivoEmArray(String fileToOrder, int rowCount) throws IOException {
        String[][] data = new String[rowCount][14];
    
        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileToOrder))) {
            String line;
            while ((line = br.readLine()) != null) {
                data[index++] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            }
        }
        return data;
    }           

    private void ordenarArray(String[][] data, int columnIndex, int rowCount) {
        for (int i = 0; i < rowCount; i++) {
            String[] key = data[i];
            int j = i - 1;
    
            String currentData = (j >= 0) ? data[j][columnIndex].replace("\"", "").replace(",", "") : "";
            String keyData = (key[columnIndex] != null) ? key[columnIndex].replace("\"", "").replace(",", "") : "";
    
            if (!isNumeric(currentData)) {
                currentData = "0";
            }
            if (!isNumeric(keyData)) {
                keyData = "0";
            }
    
            int currentVal = Integer.parseInt(currentData);
            int keyVal = Integer.parseInt(keyData);
    
            while (j >= 0 && currentVal > keyVal) {
                data[j + 1] = data[j];
                j--;
    
                if (j >= 0) {
                    currentData = data[j][columnIndex].replace("\"", "").replace(",", "");
                    currentVal = isNumeric(currentData) ? Integer.parseInt(currentData) : 0;
                }
            }
            data[j + 1] = key;
        }
    }  
    
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
               
}