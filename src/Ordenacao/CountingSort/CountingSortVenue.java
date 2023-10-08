package Ordenacao.CountingSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CountingSortVenue {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/CountingSort/";
    private String outputMedio = path + "matches_t2_venues_countingSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_countingSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_countingSort_piorCaso.csv";
    private int venueIndex = 7;

    public CountingSortVenue(String inputFile) {
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
        String[][] data = carregarArquivoEmArray(inputFile);
        sortByVenueString(data, venueIndex);
        escreverDados(data, outputMelhor);
    }

    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        sortByVenueString(data, venueIndex);
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

    private String[][] carregarArquivoEmArray(String file) {
        String[][] data;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            data = br.lines().skip(1).map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1))
                    .toArray(String[][]::new);
        } catch (IOException e) {
            e.printStackTrace();
            data = new String[0][];
        }
        return data;
    }

    private void escreverDados(String[][] data, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date");
            writer.newLine();
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
        String[][] data = carregarArquivoEmArray(fileToOrder);
        long startTime = System.currentTimeMillis();
        countingSort(data, venueIndex);
        long endTime = System.currentTimeMillis();
        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
    }

    private void countingSort(String[][] data, int columnIndex) {
        int maxLen = getMaxStringLen(data, columnIndex);
        int minLen = getMinStringLen(data, columnIndex);
        int range = maxLen - minLen + 1;

        int[] countingArray = new int[range];
        String[][] outputArray = new String[data.length][];

        for (int i = 0; i < data.length; i++) {
            int len = sanitizedLength(data[i][columnIndex]);
            countingArray[len - minLen]++;
        }

        for (int i = 1; i < range; i++) {
            countingArray[i] += countingArray[i - 1];
        }

        for (int i = data.length - 1; i >= 0; i--) {
            int len = sanitizedLength(data[i][columnIndex]);
            outputArray[countingArray[len - minLen] - 1] = data[i];
            countingArray[len - minLen]--;
        }

        for (int i = 0; i < data.length; i++) {
            data[i] = outputArray[i];
        }
    }

    private int getMaxStringLen(String[][] data, int columnIndex) {
        int maxLen = Integer.MIN_VALUE;
        for (int i = 0; i < data.length; i++) {
            int len = sanitizedLength(data[i][columnIndex]);
            if (len > maxLen) {
                maxLen = len;
            }
        }
        return maxLen;
    }

    private int getMinStringLen(String[][] data, int columnIndex) {
        int minLen = Integer.MAX_VALUE;
        for (int i = 0; i < data.length; i++) {
            int len = sanitizedLength(data[i][columnIndex]);
            if (len < minLen) {
                minLen = len;
            }
        }
        return minLen;
    }

    private int sanitizedLength(String str) {
        return str.toLowerCase().replace("\"", "").length();
    }

    private void sortByVenueString(String[][] data, int columnIndex) {
        for (int i = 1; i < data.length; i++) {
            String[] key = data[i];
            int j = i - 1;

            String currentData = sanitize(data[j][columnIndex]);
            String keyData = sanitize(key[columnIndex]);

            while (j >= 0 && currentData.compareTo(keyData) > 0) {
                data[j + 1] = data[j];
                j--;

                if (j >= 0) {
                    currentData = sanitize(data[j][columnIndex]);
                }
            }
            data[j + 1] = key;
        }
    }

    private String sanitize(String str) {
        return str.toLowerCase().replace("\"", "");
    }
}
