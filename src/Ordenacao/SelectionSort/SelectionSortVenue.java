package Ordenacao.SelectionSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * A classe {@code SelectionSortVenue} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Selection Sort.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class SelectionSortVenue {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/SelectionSort/";
    private String outputMedio = path + "matches_t2_venues_selectionSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_selectionSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_selectionSort_piorCaso.csv";
    private int venueIndex = 7;

    /**
     * Construtor da classe SelectionSortVenue.
     *
     * @param inputFile O caminho do arquivo de entrada que contém os dados a serem
     *                  ordenados.
     */
    public SelectionSortVenue(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Ordena os dados nos cenários de melhor, médio e pior caso e imprime o tempo
     * de execução.
     */
    public void ordenar() {
        criarCasoMelhor();
        criarCasoMedio();
        criarCasoPior();

        System.out.println("Ordenando utilizando o algoritmo Selection Sort...");

        ordenarEImprimirTempo(outputMelhor);
        ordenarEImprimirTempo(outputMedio);
        ordenarEImprimirTempo(outputPior);

        System.out.println("\nOrdenação concluída com sucesso!");
    }

    /**
     * Cria o cenário de caso médio copiando o arquivo de entrada para o arquivo de
     * saída.
     */
    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    /**
     * Cria o cenário de melhor caso ordenando os dados em ordem crescente.
     */
    private void criarCasoMelhor() {
        String[][] data = carregarArquivoEmArray(inputFile);
        selectionSort(data, venueIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o cenário de pior caso invertendo a ordem dos dados previamente ordenados.
     */
    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(outputMelhor); // Carrega os dados já ordenados do melhor caso
        inverterDados(data);
        escreverDados(data, outputPior);
    }

    /**
     * Copia um arquivo de origem para um arquivo de destino.
     *
     * @param origem  O caminho do arquivo de origem.
     * @param destino O caminho do arquivo de destino.
     */
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

    /**
     * Carrega os dados de um arquivo CSV em uma matriz bidimensional.
     *
     * @param file O caminho do arquivo CSV a ser carregado.
     * @return Uma matriz bidimensional contendo os dados do arquivo CSV.
     */
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

    /**
     * Escreve os dados de uma matriz bidimensional em um arquivo CSV.
     *
     * @param data       A matriz bidimensional contendo os dados a serem escritos.
     * @param outputFile O caminho do arquivo CSV de saída.
     */
    private void escreverDados(String[][] data, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Escreva o cabeçalho
            writer.write(
                    "id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date");
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

    /**
     * Inverte a ordem dos dados em uma matriz bidimensional.
     *
     * @param data A matriz bidimensional a ser invertida.
     */
    private void inverterDados(String[][] data) {
        for (int i = 0; i < data.length / 2; i++) {
            String[] temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
    }

    /**
     * Ordena os dados e imprime o tempo de execução.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        String[][] data = carregarArquivoEmArray(fileToOrder);

        long startTime = System.currentTimeMillis();
        selectionSort(data, venueIndex);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");

        imprimirConsumoMemoria(); // Imprimir consumo de memória após a ordenação
    }

    /**
     * Ordena os dados em uma matriz bidimensional usando o algoritmo Selection Sort.
     *
     * @param data        A matriz bidimensional contendo os dados a serem
     *                    ordenados.
     * @param columnIndex O índice da coluna a ser usada para a ordenação.
     */
    private void selectionSort(String[][] data, int columnIndex) {
        int n = data.length;
        for (int i = 0; i < n - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                String currentData = data[j][columnIndex].replace("\"", "").toUpperCase();
                String minData = data[min_idx][columnIndex].replace("\"", "").toUpperCase();
                if (currentData.compareTo(minData) < 0) {
                    min_idx = j;
                }
            }

            // Trocar o elemento mínimo encontrado pelo primeiro elemento
            String[] temp = data[min_idx];
            data[min_idx] = data[i];
            data[i] = temp;
        }
    }

    /**
     * Imprime o consumo atual de memória.
     */
    private void imprimirConsumoMemoria() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();

        long usedMemory = heapMemoryUsage.getUsed();

        System.out.println("Consumo de memória: " + usedMemory + " bytes");
    }
}
