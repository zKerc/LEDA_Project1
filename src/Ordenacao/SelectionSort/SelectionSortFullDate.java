package Ordenacao.SelectionSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A classe {@code SelectionSortFullDate} realiza a ordenação de dados em
 * arquivos
 * CSV usando o algoritmo de ordenação Selection Sort.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class SelectionSortFullDate {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/SelectionSort/";
    private String outputMedio = path + "matches_t2_full_date_selectionSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_full_date_selectionSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_full_date_selectionSort_piorCaso.csv";
    private int fullDateIndex = 13;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Construtor da classe SelectionSortFullDate.
     *
     * @param inputFile O caminho do arquivo de entrada contendo os dados não
     *                  ordenados.
     */
    public SelectionSortFullDate(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Método principal que realiza a ordenação em diferentes cenários (melhor,
     * médio e pior caso).
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
     * Cria o cenário de caso médio, copiando o arquivo de entrada para um novo
     * arquivo de saída.
     */
    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    /**
     * Cria o cenário de caso melhor, ordenando os dados em ordem crescente de
     * datas.
     */
    private void criarCasoMelhor() {
        String[][] data = carregarArquivoEmArray(inputFile);
        selectionSort(data, fullDateIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o cenário de caso pior, ordenando os dados em ordem decrescente de
     * datas.
     */
    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        selectionSort(data, fullDateIndex);
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
     * @param file O caminho do arquivo CSV.
     * @return Uma matriz bidimensional contendo os dados do arquivo.
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
     * @param data       A matriz bidimensional de dados a ser escrita.
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
     * Inverte os dados em uma matriz bidimensional.
     *
     * @param data A matriz bidimensional de dados a ser invertida.
     */
    private void inverterDados(String[][] data) {
        for (int i = 0; i < data.length / 2; i++) {
            String[] temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
    }

    /**
     * Imprime o tempo de execução da ordenação de um arquivo especificado.
     *
     * @param fileToOrder O caminho do arquivo contendo os dados a serem ordenados.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        String[][] data = carregarArquivoEmArray(fileToOrder);

        long startTime = System.currentTimeMillis();
        selectionSort(data, fullDateIndex);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
    }

    /**
     * Ordena os dados em uma matriz bidimensional usando o algoritmo Selection
     * Sort.
     *
     * @param data        A matriz bidimensional de dados a ser ordenada.
     * @param columnIndex O índice da coluna de datas completas.
     */
    private void selectionSort(String[][] data, int columnIndex) {
        int n = data.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (isDateGreater(data[j][columnIndex], data[minIndex][columnIndex])) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                String[] temp = data[i];
                data[i] = data[minIndex];
                data[minIndex] = temp;
            }
        }
    }

    /**
     * Verifica se uma data é maior que outra.
     *
     * @param date1 A primeira data a ser comparada.
     * @param date2 A segunda data a ser comparada.
     * @return true se date1 for maior que date2, caso contrário, false.
     */
    private boolean isDateGreater(String date1, String date2) {
        try {
            Date d1 = sdf.parse(date1.replace("\"", ""));
            Date d2 = sdf.parse(date2.replace("\"", ""));
            return d1.compareTo(d2) > 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
