package Ordenacao.CountingSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A classe {@code CountingSortVenue} realiza a ordenação de dados em
 * arquivos CSV usando o algoritmo de ordenação Counting Sort.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class CountingSortVenue {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/CountingSort/";
    private String outputMedio = path + "matches_t2_venues_countingSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_countingSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_countingSort_piorCaso.csv";
    private int venueIndex = 7;

    /**
     * Cria uma nova instância do CountingSortVenue.
     *
     * @param inputFile O arquivo de entrada contendo os dados de venues a serem
     *                  ordenados.
     */
    public CountingSortVenue(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Realiza a ordenação dos casos de melhor, médio e pior cenários e imprime os
     * tempos de execução.
     */
    public void ordenar() {
        criarCasoMelhor();
        criarCasoMedio();
        criarCasoPior();

        ordenarEImprimirTempo(outputMelhor);
        ordenarEImprimirTempo(outputMedio);
        ordenarEImprimirTempo(outputPior);
    }

    /**
     * Cria um arquivo de caso médio que é uma cópia do arquivo de entrada.
     */
    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    /**
     * Cria um arquivo de melhor caso ordenando os dados em ordem crescente de
     * venues.
     */
    private void criarCasoMelhor() {
        String[][] data = carregarArquivoEmArray(inputFile);
        countingSort(data, venueIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria um arquivo de pior caso ordenando os dados em ordem decrescente de
     * venues.
     */
    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        countingSort(data, venueIndex);
        inverterDados(data);
        escreverDados(data, outputPior);
    }

    /**
     * Copia um arquivo de origem para um arquivo de destino.
     *
     * @param origem  O arquivo de origem.
     * @param destino O arquivo de destino.
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
     * Carrega os dados de um arquivo CSV em um array bidimensional.
     *
     * @param file O arquivo CSV a ser carregado.
     * @return Um array bidimensional contendo os dados do arquivo.
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
     * Escreve os dados de um array em um arquivo CSV.
     *
     * @param data       O array contendo os dados.
     * @param outputFile O arquivo de saída para escrever os dados.
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
     * Inverte os dados em um array.
     *
     * @param data O array a ser invertido.
     */
    private void inverterDados(String[][] data) {
        for (int i = 0; i < data.length / 2; i++) {
            String[] temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
    }

    /**
     * Realiza a ordenação Counting Sort em um arquivo especificado, mede o tempo de
     * execução e imprime-o.
     *
     * @param fileToOrder O arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        // Carrega os dados do arquivo em um array
        String[][] data = carregarArquivoEmArray(fileToOrder);

        // Inicia a contagem do tempo
        long startTime = System.currentTimeMillis();

        // Realiza a ordenação Counting Sort nos dados usando o índice da coluna de
        // venues
        countingSort(data, venueIndex);

        // Finaliza a contagem do tempo
        long endTime = System.currentTimeMillis();

        // Calcula e imprime o tempo de execução
        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
    }

    /**
     * Realiza a ordenação Counting Sort em um array de acordo com o índice da
     * coluna do venue.
     *
     * @param data       O array a ser ordenado.
     * @param venueIndex O índice da coluna pela qual os dados serão ordenados.
     */
    private void countingSort(String[][] data, int venueIndex) {
        int maxVenue = getMaxVenue(data, venueIndex);
        int minVenue = getMinVenue(data, venueIndex);
        int range = maxVenue - minVenue + 1;

        int[] countingArray = new int[range];
        String[][] outputArray = new String[data.length][];

        for (int i = 0; i < data.length; i++) {
            int venueValue = Integer.parseInt(data[i][venueIndex]);
            countingArray[venueValue - minVenue]++;
        }

        for (int i = 1; i < range; i++) {
            countingArray[i] += countingArray[i - 1];
        }

        for (int i = data.length - 1; i >= 0; i--) {
            int venueValue = Integer.parseInt(data[i][venueIndex]);
            outputArray[countingArray[venueValue - minVenue] - 1] = data[i];
            countingArray[venueValue - minVenue]--;
        }

        for (int i = 0; i < data.length; i++) {
            data[i] = outputArray[i];
        }
    }

    /**
     * Obtém o valor máximo na coluna de venues.
     *
     * @param data       O array contendo os dados.
     * @param venueIndex O índice da coluna de venues.
     * @return O valor máximo na coluna de venues.
     */
    private int getMaxVenue(String[][] data, int venueIndex) {
        int maxVenue = Integer.MIN_VALUE;
        for (int i = 0; i < data.length; i++) {
            int venueValue = Integer.parseInt(data[i][venueIndex]);
            if (venueValue > maxVenue) {
                maxVenue = venueValue;
            }
        }
        return maxVenue;
    }

    /**
     * Obtém o valor mínimo na coluna de venues.
     *
     * @param data       O array contendo os dados.
     * @param venueIndex O índice da coluna de venues.
     * @return O valor mínimo na coluna de venues.
     */
    private int getMinVenue(String[][] data, int venueIndex) {
        int minVenue = Integer.MAX_VALUE;
        for (int i = 0; i < data.length; i++) {
            int venueValue = Integer.parseInt(data[i][venueIndex]);
            if (venueValue < minVenue) {
                minVenue = venueValue;
            }
        }
        return minVenue;
    }
}
