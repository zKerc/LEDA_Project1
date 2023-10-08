package Ordenacao.MergeSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A classe {@code MergeSortVenue} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Merge Sort.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class MergeSortVenue {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/MergeSort/";
    private String outputMedio = path + "matches_t2_venues_mergeSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_mergeSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_mergeSort_piorCaso.csv";
    private int venueIndex = 7;

    /**
     * Cria uma nova instância de MergeSortVenue com o caminho do arquivo de entrada
     * especificado.
     *
     * @param inputFile O caminho do arquivo de entrada contendo os dados a serem
     *                  ordenados.
     */
    public MergeSortVenue(String inputFile) {
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

    /**
     * Cria o caso de cenário melhor ordenando os dados com o algoritmo Merge Sort
     * e, em seguida, escreve os resultados no arquivo de saída correspondente.
     */
    private void criarCasoMelhor() {
        String[][] data = carregarArquivoEmArray(inputFile);
        mergeSort(data, venueIndex, 0, data.length - 1);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de cenário pior ordenando os dados com o algoritmo Merge Sort
     * em ordem decrescente e, em seguida, escreve os resultados no arquivo de saída
     * correspondente.
     */
    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        mergeSort(data, venueIndex, 0, data.length - 1);
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
     * Carrega os dados de um arquivo CSV em um array bidimensional.
     *
     * @param file O caminho do arquivo CSV a ser carregado.
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
     * Realiza a ordenação e mede o tempo de execução usando o algoritmo Merge Sort.
     * O tempo de execução é impresso no console.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        String[][] data = carregarArquivoEmArray(fileToOrder);

        long startTime = System.currentTimeMillis();
        mergeSort(data, venueIndex, 0, data.length - 1);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
    }

    /**
     * Realiza a ordenação de um subarray usando o algoritmo Merge Sort.
     *
     * @param data       O array bidimensional contendo os dados a serem ordenados.
     * @param venueIndex O índice da coluna de locais (venue) nos dados.
     * @param left       O índice de início do subarray.
     * @param right      O índice de fim do subarray.
     */
    private void mergeSort(String[][] data, int venueIndex, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(data, venueIndex, left, mid);
            mergeSort(data, venueIndex, mid + 1, right);
            merge(data, venueIndex, left, mid, right);
        }
    }

    /**
     * Realiza a fusão (merge) de dois subarrays ordenados em um único subarray
     * ordenado.
     *
     * @param data       O array bidimensional contendo os dados a serem mesclados.
     * @param venueIndex O índice da coluna de locais (venue) nos dados.
     * @param left       O índice de início do primeiro subarray.
     * @param mid        O índice de meio que divide os dois subarrays.
     * @param right      O índice de fim do segundo subarray.
     */
    private void merge(String[][] data, int venueIndex, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        String[][] leftArray = new String[n1][];
        String[][] rightArray = new String[n2][];

        for (int i = 0; i < n1; i++) {
            leftArray[i] = data[left + i];
        }
        for (int i = 0; i < n2; i++) {
            rightArray[i] = data[mid + 1 + i];
        }

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (compareStrings(leftArray[i][venueIndex], rightArray[j][venueIndex]) <= 0) {
                data[k++] = leftArray[i++];
            } else {
                data[k++] = rightArray[j++];
            }
        }

        while (i < n1) {
            data[k++] = leftArray[i++];
        }

        while (j < n2) {
            data[k++] = rightArray[j++];
        }
    }

    /**
     * Compara duas strings de acordo com a ordem lexicográfica, mas de forma insensível
     * a maiúsculas/minúsculas e ignorando as aspas duplas.
     *
     * @param str1 A primeira string a ser comparada.
     * @param str2 A segunda string a ser comparada.
     * @return Um valor negativo se str1 < str2, um valor positivo se str1 > str2,
     *         ou zero se str1 == str2.
     */
    private int compareStrings(String str1, String str2) {
        // Removendo as aspas duplas e convertendo as strings para minúsculas
        str1 = str1.replace("\"", "").toLowerCase();
        str2 = str2.replace("\"", "").toLowerCase();
        
        int minLength = Math.min(str1.length(), str2.length());
        for (int i = 0; i < minLength; i++) {
            char c1 = str1.charAt(i);
            char c2 = str2.charAt(i);
            if (c1 < c2) {
                return -1;
            } else if (c1 > c2) {
                return 1;
            }
        }
        return Integer.compare(str1.length(), str2.length());
    }
}
