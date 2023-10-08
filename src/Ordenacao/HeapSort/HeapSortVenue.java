package Ordenacao.HeapSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A classe {@code HeapSortVenue} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Heap Sort.
 * Ela é projetada para criar três cenários de teste (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os dados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class HeapSortVenue {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/HeapSort/";
    private String outputMedio = path + "matches_t2_venues_heapSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_heapSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_heapSort_piorCaso.csv";
    private int venueIndex = 7;

    /**
     * Construtor que inicializa uma nova instância da classe HeapSortVenue com o arquivo de entrada especificado.
     *
     * @param inputFile O arquivo de entrada contendo os dados a serem ordenados.
     */
    public HeapSortVenue(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Ordena os dados nos cenários de melhor, médio e pior caso,
     * medindo o tempo de execução e imprimindo-o no console.
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
     * Cria um cenário de caso médio copiando o arquivo de entrada para um arquivo
     * de saída correspondente.
     */
    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    /**
     * Cria um cenário de melhor caso, ordenando os dados com o algoritmo Heap Sort
     * e salvando-os em um arquivo de saída correspondente.
     */
    private void criarCasoMelhor() {
        String[][] data = carregarArquivoEmArray(inputFile);
        heapSort(data, venueIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria um cenário de pior caso, ordenando os dados com o algoritmo Heap Sort
     * e salvando-os em um arquivo de saída correspondente.
     */
    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        heapSort(data, venueIndex);
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
     * Carrega um arquivo CSV em um array bidimensional de strings, excluindo o
     * cabeçalho.
     *
     * @param file O caminho do arquivo CSV a ser carregado.
     * @return Um array bidimensional de strings representando os dados do arquivo.
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
     * Escreve os dados de um array bidimensional em um arquivo CSV especificado.
     *
     * @param data       O array bidimensional de dados a serem escritos.
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
     * Inverte a ordem dos dados em um array bidimensional.
     *
     * @param data O array bidimensional de dados a ser invertido.
     */
    private void inverterDados(String[][] data) {
        for (int i = 0; i < data.length / 2; i++) {
            String[] temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
    }

    /**
     * Realiza a ordenação do arquivo especificado com o algoritmo Heap Sort e mede
     * o tempo
     * de execução da ordenação. O tempo de execução é impresso no console.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        String[][] data = carregarArquivoEmArray(fileToOrder);

        long startTime = System.currentTimeMillis();
        heapSort(data, venueIndex);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
    }

    /**
     * Ordena um conjunto de dados usando o algoritmo Heap Sort baseado na coluna venue.
     *
     * @param data       O conjunto de dados a ser ordenado.
     * @param venueIndex O índice da coluna venue.
     */
    private void heapSort(String[][] data, int venueIndex) {
        int n = data.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(data, n, i, venueIndex);
        }

        for (int i = n - 1; i > 0; i--) {
            String[] temp = data[0];
            data[0] = data[i];
            data[i] = temp;

            heapify(data, i, 0, venueIndex);
        }
    }

    /**
     * Realiza a operação de heapify em um conjunto de dados.
     *
     * @param data       O conjunto de dados a ser heapificado.
     * @param n          O tamanho do heap.
     * @param i          O índice do elemento raiz.
     * @param venueIndex O índice da coluna venue.
     */
    private void heapify(String[][] data, int n, int i, int venueIndex) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && sanitize(data[left][venueIndex]).compareTo(sanitize(data[largest][venueIndex])) > 0) {
            largest = left;
        }

        if (right < n && sanitize(data[right][venueIndex]).compareTo(sanitize(data[largest][venueIndex])) > 0) {
            largest = right;
        }

        if (largest != i) {
            String[] swap = data[i];
            data[i] = data[largest];
            data[largest] = swap;

            heapify(data, n, largest, venueIndex);
        }
    }

    /**
     * Sanitiza uma string removendo as aspas e convertendo-a para letras minúsculas.
     *
     * @param str A string a ser sanitizada.
     * @return A string sanitizada.
     */
    private String sanitize(String str) {
        return str.replace("\"", "").toLowerCase();
    }
}
