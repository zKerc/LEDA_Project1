package Ordenacao.InsertionSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * Classe responsável por realizar a ordenação de datas completas utilizando o algoritmo Insertion Sort.
 */
public class InsertionSortFullDate {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/InsertionSort/";
    private String outputMedio = path + "matches_t2_full_date_insertionSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_full_date_insertionSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_full_date_insertionSort_piorCaso.csv";
    private int fullDateIndex = 13;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Construtor que inicializa a classe com o arquivo de entrada.
     *
     * @param inputFile Caminho do arquivo de entrada.
     */
    public InsertionSortFullDate(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Função principal que orquestra o processo de ordenação em três cenários: melhor caso, caso médio e pior caso.
     */
    public void ordenar() {
        criarCasoMelhor();
        criarCasoMedio();
        criarCasoPior();

        System.out.println("Ordenando utilizando o algoritmo Insertion Sort...");

        ordenarEImprimirTempo(outputMelhor);
        ordenarEImprimirTempo(outputMedio);
        ordenarEImprimirTempo(outputPior);

        System.out.println("\nOrdenação concluída com sucesso!");
    }

    /**
     * Cria um arquivo para o caso médio, que é uma cópia do arquivo original.
     */
    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    /**
     * Cria um arquivo para o melhor caso, que é o arquivo original ordenado.
     */
    private void criarCasoMelhor() {
        String[][] data = carregarArquivoEmArray(inputFile);
        ordenarArray(data, fullDateIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria um arquivo para o pior caso, que é o arquivo original ordenado em ordem inversa.
     */
    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        ordenarArray(data, fullDateIndex);
        inverterDados(data);
        escreverDados(data, outputPior);
    }

    /**
     * Copia um arquivo para outro local.
     *
     * @param origem Caminho do arquivo de origem.
     * @param destino Caminho do arquivo de destino.
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
     * @param file Caminho do arquivo CSV.
     * @return Array bidimensional com os dados do arquivo.
     */
    private String[][] carregarArquivoEmArray(String file) {
        String[][] data;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            data = br.lines().skip(1)
                     .map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1))
                     .toArray(String[][]::new);
        } catch (IOException e) {
            e.printStackTrace();
            data = new String[0][];
        }
        return data;
    }

    /**
     * Escreve os dados de um array bidimensional em um arquivo CSV.
     *
     * @param data Array bidimensional com os dados.
     * @param outputFile Caminho do arquivo de saída.
     */
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

    /**
     * Inverte a ordem dos elementos de um array bidimensional.
     *
     * @param data Array bidimensional a ser invertido.
     */
    private void inverterDados(String[][] data) {
        for (int i = 0; i < data.length / 2; i++) {
            String[] temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
    }

    /**
     * Ordena os dados de um arquivo usando o algoritmo Insertion Sort e imprime o tempo gasto na operação.
     *
     * @param fileToOrder Caminho do arquivo a ser ordenado.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        String[][] data = carregarArquivoEmArray(fileToOrder);
        long startTime = System.currentTimeMillis();
        ordenarArray(data, fullDateIndex);
        long endTime = System.currentTimeMillis();
        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
        imprimirConsumoMemoria();
    }

    /**
     * Implementação do algoritmo Insertion Sort para a ordenação de datas.
     *
     * @param data Array bidimensional contendo as datas.
     * @param columnIndex Índice da coluna que contém as datas a serem ordenadas.
     */
    private void ordenarArray(String[][] data, int columnIndex) {
        for (int i = 1; i < data.length; i++) {
            String[] key = data[i];
            int j = i - 1;
            while (j >= 0 && isDateGreater(data[j][columnIndex], key[columnIndex])) {
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = key;
        }
    }

    /**
     * Compara duas datas e retorna verdadeiro se a primeira data for maior que a segunda.
     *
     * @param date1 Primeira data.
     * @param date2 Segunda data.
     * @return Verdadeiro se a primeira data for maior, falso caso contrário.
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
     * Imprime o consumo atual de memória do programa.
     */
    private void imprimirConsumoMemoria() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
        long usedMemory = heapMemoryUsage.getUsed();
        System.out.println("Consumo de memória: " + usedMemory + " bytes");
    }
}