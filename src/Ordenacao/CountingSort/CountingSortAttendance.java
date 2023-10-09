package Ordenacao.CountingSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class CountingSortAttendance {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/CountingSort/";
    private String outputMedio = path + "matches_t2_attendance_countingSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_attendance_countingSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_attendance_countingSort_piorCaso.csv";
    private int attendanceIndex = 6;

    public CountingSortAttendance(String inputFile) {
        this.inputFile = inputFile;
    }

    public void ordenar() {
        criarCasoMelhor();
        criarCasoMedio();
        criarCasoPior();

        System.out.println("Ordenando utilizando o algoritmo Counting Sort...");

        ordenarEImprimirTempo(outputMelhor);

        ordenarEImprimirTempo(outputMedio);

        ordenarEImprimirTempo(outputPior);
        System.out.println("\nOrdenação concluída com sucesso!");
    }

    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    private void criarCasoMelhor() {
        String[][] data = carregarArquivoEmArray(inputFile);
        countingSortForAttendance(data, attendanceIndex);
        escreverDados(data, outputMelhor);
    }

    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        countingSortForAttendance(data, attendanceIndex);
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
            writer.write(
                    "id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date");
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
        countingSortForAttendance(data, attendanceIndex);
        long endTime = System.currentTimeMillis();
        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria(); // Imprimir consumo de memória após a ordenação

    }

    private void countingSortForAttendance(String[][] data, int columnIndex) {
        int maxAttendance = getMaxAttendance(data, columnIndex);

        int[] countingArray = new int[maxAttendance + 1];
        String[][] outputArray = new String[data.length][];

        for (int i = 0; i < data.length; i++) {
            int val = sanitizeAttendance(data[i][columnIndex]);
            countingArray[val]++;
        }

        for (int i = 1; i <= maxAttendance; i++) {
            countingArray[i] += countingArray[i - 1];
        }

        for (int i = data.length - 1; i >= 0; i--) {
            int val = sanitizeAttendance(data[i][columnIndex]);
            outputArray[countingArray[val] - 1] = data[i];
            countingArray[val]--;
        }

        for (int i = 0; i < data.length; i++) {
            data[i] = outputArray[i];
        }
    }

    private int getMaxAttendance(String[][] data, int columnIndex) {
        int maxAttendance = Integer.MIN_VALUE;
        for (int i = 0; i < data.length; i++) {
            int val = sanitizeAttendance(data[i][columnIndex]);
            if (val > maxAttendance) {
                maxAttendance = val;
            }
        }
        return maxAttendance;
    }

    private int sanitizeAttendance(String str) {
        if (str == null || str.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(str.replace("\"", "").replace(",", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void imprimirConsumoMemoria() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();

        long usedMemory = heapMemoryUsage.getUsed();

        System.out.println("Consumo de memória: " + usedMemory + " bytes");
    }
}
