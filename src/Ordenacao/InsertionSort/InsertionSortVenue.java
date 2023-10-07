package Ordenacao.InsertionSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class InsertionSortVenue {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/InsertionSort/";
    private String outputMedio = path + "matches_t2_venues_insertionSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_insertionSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_insertionSort_piorCaso.csv";
    private int venueIndex = 7;

    public InsertionSortVenue(String inputFile) {
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
        int rowCount = contarLinhas(inputFile);
        String[][] data = carregarArquivoEmArray(inputFile, rowCount);
        ordenarArray(data, venueIndex, rowCount);
        escreverDados(data, outputMelhor);
    }

    private void criarCasoPior() {
        int rowCount = contarLinhas(inputFile);
        String[][] data = carregarArquivoEmArray(inputFile, rowCount);
        ordenarArray(data, venueIndex, rowCount);
        inverterDados(data);
        escreverDados(data, outputPior);
    }

    private void copiarArquivo(String origem, String destino) {
        try (BufferedReader br = new BufferedReader(new FileReader(origem));
             BufferedWriter writer = new BufferedWriter(new FileWriter(destino))) {
            String line;
            while ((line = br.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int contarLinhas(String file) {
        int rowCount = 0;
        try (BufferedReader counter = new BufferedReader(new FileReader(file))) {
            while (counter.readLine() != null) rowCount++;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowCount;
    }

    private String[][] carregarArquivoEmArray(String file, int rowCount) {
        String[][] data = new String[rowCount - 1][]; // Excluindo o cabeçalho na contagem
        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // Leitura da primeira linha (cabeçalho) sem adicionar ao array
            String line;
            while ((line = br.readLine()) != null) {
                data[index++] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }    

    private void escreverDados(String[][] data, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Escreva o cabeçalho
            writer.write("id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date");
            writer.newLine();
            
            // Escreva os dados
            for (int i = 0; i < data.length; i++) {
                writer.write(String.join(",", data[i]));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void inverterDados(String[][] data) {
        for (int i = 0; i < data.length / 2; i++) {
            String[] temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
    }

    private void ordenarEImprimirTempo(String fileToOrder) {
        int rowCount = contarLinhas(fileToOrder);
        String[][] data = carregarArquivoEmArray(fileToOrder, rowCount);

        long startTime = System.currentTimeMillis();
        ordenarArray(data, venueIndex, rowCount);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
    }

    private void ordenarArray(String[][] data, int columnIndex, int rowCount) {
        for (int i = 1; i < rowCount; i++) {
            String[] key = data[i];
            int j = i - 1;
    
            // Remove as aspas duplas e converte para maiúsculas antes de comparar
            String currentData = data[j][columnIndex].replace("\"", "").toUpperCase();
            String keyData = key[columnIndex].replace("\"", "").toUpperCase();
    
            while (j >= 0 && currentData.compareTo(keyData) > 0) {
                data[j + 1] = data[j];
                j--;
                
                if (j >= 0) {
                    currentData = data[j][columnIndex].replace("\"", "").toUpperCase();
                }
            }
            data[j + 1] = key;
        }
    }  
}
