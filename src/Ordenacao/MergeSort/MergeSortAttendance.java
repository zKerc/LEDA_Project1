package Ordenacao.MergeSort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A classe {@code MergeSortAttendance} realiza a ordenação de dados em
 * arquivos CSV usando o algoritmo de ordenação Merge Sort.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class MergeSortAttendance {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/MergeSort/";
    private String outputMedio = path + "matches_t2_attendance_mergeSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_attendance_mergeSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_attendance_mergeSort_piorCaso.csv";
    private int attendanceIndex = 6;

    /**
     * Cria uma nova instância de {@code MergeSortAttendance} com o arquivo de
     * entrada especificado.
     *
     * @param inputFile O arquivo de entrada a ser ordenado.
     */
    public MergeSortAttendance(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Realiza a ordenação dos dados nos casos de melhor, médio e pior e imprime os
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
     * Cria o caso de ordenação médio copiando o conteúdo do arquivo de entrada para
     * o arquivo de saída.
     */
    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    /**
     * Cria o caso de ordenação melhor ordenando o arquivo de entrada de forma
     * crescente usando o algoritmo Merge Sort.
     */
    private void criarCasoMelhor() {
        String[][] data = carregarArquivoEmArray(inputFile);
        mergeSort(data, attendanceIndex, 0, data.length - 1);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de ordenação pior ordenando o arquivo de entrada de forma
     * decrescente usando o algoritmo Merge Sort.
     */
    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        mergeSort(data, attendanceIndex, 0, data.length - 1);
        inverterDados(data);
        escreverDados(data, outputPior);
    }

    /**
     * Copia um arquivo de origem para um arquivo de destino.
     *
     * @param origem  O arquivo de origem a ser copiado.
     * @param destino O arquivo de destino onde o conteúdo será copiado.
     */
    private void copiarArquivo(String origem, String destino) {
        try (BufferedReader br = new BufferedReader(new FileReader(origem));
             FileWriter writer = new FileWriter(destino)) {
            String line;
            while ((line = br.readLine()) != null) {
                writer.write(line + "\n");
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
     * Escreve os dados de um array bidimensional em um arquivo CSV.
     *
     * @param data       O array bidimensional contendo os dados a serem escritos.
     * @param outputFile O arquivo CSV de saída.
     */
    private void escreverDados(String[][] data, String outputFile) {
        try (FileWriter writer = new FileWriter(outputFile)) {
            // Escreva o cabeçalho
            writer.write(
                    "id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date\n");

            // Escreva os dados
            for (int i = 0; i < data.length; i++) {
                writer.write(String.join(",", data[i]) + "\n");
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
     * @param fileToOrder O arquivo a ser ordenado.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        String[][] data = carregarArquivoEmArray(fileToOrder);

        long startTime = System.currentTimeMillis();
        mergeSort(data, attendanceIndex, 0, data.length - 1);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
    }

    /**
     * Realiza a ordenação de um subarray usando o algoritmo Merge Sort.
     *
     * @param data       O array bidimensional contendo os dados a serem ordenados.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param left       O índice de início do subarray.
     * @param right      O índice de fim do subarray.
     */
    private void mergeSort(String[][] data, int columnIndex, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(data, columnIndex, left, mid);
            mergeSort(data, columnIndex, mid + 1, right);
            merge(data, columnIndex, left, mid, right);
        }
    }

    /**
     * Esta função realiza a fusão (merge) de dois subarrays ordenados em um único subarray ordenado.
     * O processo de fusão é um passo fundamental no algoritmo de ordenação Merge Sort.
     *
     * @param data       O array bidimensional contendo os dados a serem mesclados.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param left       O índice de início do primeiro subarray.
     * @param mid        O índice de meio que divide os dois subarrays.
     * @param right      O índice de fim do segundo subarray.
     */
    private void merge(String[][] data, int columnIndex, int left, int mid, int right) {
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
            int leftVal = parseToInt(leftArray[i][columnIndex]);
            int rightVal = parseToInt(rightArray[j][columnIndex]);
    
            if (leftVal <= rightVal) {
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
     * Converte uma string em um número inteiro. 
     * Se a string estiver vazia ou não for numérica, retorna 0.
     * O valor pode ter aspas duplas e vírgulas, que são removidas antes da conversão.
     *
     * @param value A string a ser convertida em um número inteiro.
     * @return O valor inteiro correspondente à string ou 0 se a string não for numérica.
     */
    private int parseToInt(String value) {
        value = value.replace("\"", "").replace(",", "");
        if (value.isEmpty() || !isNumeric(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }
    
    /**
     * Verifica se uma string pode ser convertida em um número inteiro.
     * Esta função é usada para garantir que tentativas de conversão sejam seguras.
     *
     * @param str A string a ser verificada.
     * @return {@code true} se a string for um número inteiro válido, caso contrário {@code false}.
     */
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
