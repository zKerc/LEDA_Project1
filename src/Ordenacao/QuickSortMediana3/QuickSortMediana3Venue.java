package Ordenacao.QuickSortMediana3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A classe {@code QuickSortMediana3Venue} realiza a ordenação de dados em
 * arquivos CSV usando o algoritmo de ordenação Quick Sort com escolha da
 * mediana de três.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class QuickSortMediana3Venue {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/QuickSortMediana3/";
    private String outputMedio = path + "matches_t2_venues_quickSortMediana3_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_quickSortMediana3_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_quickSortMediana3_piorCaso.csv";
    private int venueIndex = 7;

    /**
     * Cria uma nova instância de QuickSortMediana3Venue com o caminho do arquivo de
     * entrada especificado.
     *
     * @param inputFile O caminho do arquivo de entrada contendo os dados a serem
     *                  ordenados.
     */
    public QuickSortMediana3Venue(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Realiza a ordenação e gera os resultados para os casos de melhor, médio e
     * pior cenário,
     * além de medir e imprimir o tempo de execução para cada caso.
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
     * Cria o caso de cenário médio copiando o arquivo de entrada para o arquivo de
     * saída correspondente.
     */
    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    private void criarCasoMelhor() {
        String[][] data = carregarArquivoEmArray(inputFile);
        quickSortMediana3(data, venueIndex, 0, data.length - 1);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de cenário melhor ordenando os dados com o algoritmo Quick Sort
     * com Mediana de Três
     * e, em seguida, escreve os resultados no arquivo de saída correspondente.
     */
    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        quickSortMediana3(data, venueIndex, 0, data.length - 1);
        inverterDados(data);
        escreverDados(data, outputPior);
    }

    /**
     * Cria o caso de cenário pior ordenando os dados com o algoritmo Quick Sort com
     * Mediana de Três
     * em ordem decrescente e, em seguida, escreve os resultados no arquivo de saída
     * correspondente.
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
     * Copia um arquivo de origem para um arquivo de destino.
     *
     * @param origem  O caminho do arquivo de origem.
     * @param destino O caminho do arquivo de destino.
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
     * Escreve os dados de um array bidimensional em um arquivo CSV.
     *
     * @param data       O array bidimensional contendo os dados a serem escritos.
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
     * @param data O array bidimensional a ser invertido.
     */
    private void inverterDados(String[][] data) {
        for (int i = 0; i < data.length / 2; i++) {
            String[] temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }
    }

    /**
     * Realiza a ordenação e mede o tempo de execução usando o algoritmo Quick Sort
     * com Mediana de Três.
     * O tempo de execução é impresso no console.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        String[][] data = carregarArquivoEmArray(fileToOrder);

        long startTime = System.currentTimeMillis();
        quickSortMediana3(data, venueIndex, 0, data.length - 1);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
    }

    /**
     * Realiza a ordenação de um subconjunto de dados usando o algoritmo Quick Sort
     * com Mediana de Três.
     *
     * @param data       O array bidimensional contendo os dados a serem ordenados.
     * @param venueIndex O índice da coluna de locais (venue) nos dados.
     * @param low        O índice inicial do subconjunto a ser ordenado.
     * @param high       O índice final do subconjunto a ser ordenado.
     */
    private void quickSortMediana3(String[][] data, int venueIndex, int low, int high) {
        if (low < high) {
            // Encontre a mediana de 3
            int mid = low + (high - low) / 2;
            if (data[mid][venueIndex].compareTo(data[low][venueIndex]) > 0) {
                swap(data, low, mid);
            }
            if (data[high][venueIndex].compareTo(data[low][venueIndex]) > 0) {
                swap(data, low, high);
            }
            if (data[mid][venueIndex].compareTo(data[high][venueIndex]) > 0) {
                swap(data, mid, high);
            }

            int pivotIndex = partition(data, venueIndex, low, high);

            quickSortMediana3(data, venueIndex, low, pivotIndex - 1);
            quickSortMediana3(data, venueIndex, pivotIndex + 1, high);
        }
    }

    /**
     * Particiona o subconjunto de dados para o algoritmo Quick Sort com Mediana de
     * Três.
     *
     * @param data       O array bidimensional contendo os dados a serem ordenados.
     * @param venueIndex O índice da coluna de locais (venue) nos dados.
     * @param low        O índice inicial do subconjunto a ser ordenado.
     * @param high       O índice final do subconjunto a ser ordenado.
     * @return O índice do pivô após a partição.
     */
    private int partition(String[][] data, int venueIndex, int low, int high) {
        String pivot = data[high][venueIndex];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (data[j][venueIndex].compareTo(pivot) <= 0) {
                i++;
                swap(data, i, j);
            }
        }

        swap(data, i + 1, high);
        return i + 1;
    }

    /**
     * Troca duas linhas de dados em um array bidimensional.
     *
     * @param data O array bidimensional contendo os dados.
     * @param i    O índice da primeira linha a ser trocada.
     * @param j    O índice da segunda linha a ser trocada.
     */
    private void swap(String[][] data, int i, int j) {
        String[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }
}
