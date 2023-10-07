package Ordenacao.QuickSortMediana3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A classe {@code QuickSortMediana3FullDate} realiza a ordenação de dados em
 * arquivos CSV usando o algoritmo de ordenação Quick Sort com escolha da
 * mediana de três.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class QuickSortMediana3FullDate {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/QuickSortMediana3/";
    private String outputMedio = path + "matches_t2_full_date_quickSortMediana3_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_full_date_quickSortMediana3_melhorCaso.csv";
    private String outputPior = path + "matches_t2_full_date_quickSortMediana3_piorCaso.csv";
    private int fullDateIndex = 13;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Construtor da classe QuickSortMediana3FullDate.
     *
     * @param inputFile O caminho do arquivo de entrada que contém os dados a serem
     *                  ordenados.
     */
    public QuickSortMediana3FullDate(String inputFile) {
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

        ordenarEImprimirTempo(outputMelhor);
        ordenarEImprimirTempo(outputMedio);
        ordenarEImprimirTempo(outputPior);
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
        quickSortMediana3(data, 0, data.length - 1, fullDateIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o cenário de pior caso ordenando os dados em ordem decrescente.
     */
    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        quickSortMediana3(data, 0, data.length - 1, fullDateIndex);
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
     * Ordena os dados contidos no arquivo especificado e imprime o tempo de
     * execução em milissegundos.
     *
     * @param fileToOrder O caminho do arquivo contendo os dados a serem ordenados.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        String[][] data = carregarArquivoEmArray(fileToOrder);

        long startTime = System.currentTimeMillis();
        quickSortMediana3(data, 0, data.length - 1, fullDateIndex);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
    }

    /**
     * Ordena os dados em uma matriz bidimensional usando o algoritmo Quick Sort com
     * mediana de três elementos.
     *
     * @param data        A matriz bidimensional contendo os dados a serem
     *                    ordenados.
     * @param low         O índice baixo inicial para a ordenação.
     * @param high        O índice alto inicial para a ordenação.
     * @param columnIndex O índice da coluna de datas completas (full_date) nos
     *                    dados.
     */
    private void quickSortMediana3(String[][] data, int low, int high, int columnIndex) {
        if (low < high) {
            int pivotIndex = partitionMediana3(data, low, high, columnIndex);
            quickSortMediana3(data, low, pivotIndex - 1, columnIndex);
            quickSortMediana3(data, pivotIndex + 1, high, columnIndex);
        }
    }

    /**
     * Particiona os dados em uma matriz bidimensional para a ordenação rápida com
     * mediana de três elementos.
     *
     * @param data        A matriz bidimensional contendo os dados a serem
     *                    particionados.
     * @param low         O índice baixo inicial para a particionamento.
     * @param high        O índice alto inicial para a particionamento.
     * @param columnIndex O índice da coluna de datas completas (full_date) nos
     *                    dados.
     * @return O índice da posição de partição.
     */
    private int partitionMediana3(String[][] data, int low, int high, int columnIndex) {
        int middle = (low + high) / 2;
        if (isDateGreater(data[low][columnIndex], data[middle][columnIndex]))
            swap(data, low, middle);
        if (isDateGreater(data[low][columnIndex], data[high][columnIndex]))
            swap(data, low, high);
        if (isDateGreater(data[middle][columnIndex], data[high][columnIndex]))
            swap(data, middle, high);

        String pivot = data[middle][columnIndex];
        swap(data, middle, high - 1);
        int i = low;
        int j = high - 1;
        while (true) {
            while (isDateGreater(data[++i][columnIndex], pivot))
                ;
            while (isDateGreater(pivot, data[--j][columnIndex]))
                ;
            if (i < j)
                swap(data, i, j);
            else
                break;
        }
        swap(data, i, high - 1);
        return i;
    }

    /**
     * Verifica se uma data1 é maior do que uma data2.
     *
     * @param date1 A primeira data a ser comparada.
     * @param date2 A segunda data a ser comparada.
     * @return true se a data1 for maior que a data2, caso contrário, false.
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

    /**
     * Troca dois elementos em uma matriz bidimensional.
     *
     * @param data   A matriz bidimensional contendo os elementos.
     * @param index1 O índice do primeiro elemento a ser trocado.
     * @param index2 O índice do segundo elemento a ser trocado.
     */
    private void swap(String[][] data, int index1, int index2) {
        String[] temp = data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }
}
