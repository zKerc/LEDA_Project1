package Ordenacao.QuickSortMediana3;

import java.io.BufferedReader;
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
     * Cria uma nova instância de {@code QuickSortMediana3Venue} com o arquivo de
     * entrada especificado.
     *
     * @param inputFile O arquivo de entrada a ser ordenado.
     */
    public QuickSortMediana3Venue(String inputFile) {
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
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                FileWriter writer = new FileWriter(outputMedio)) {
            String line;
            while ((line = br.readLine()) != null) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cria o caso de ordenação melhor ordenando o arquivo de entrada de forma
     * crescente.
     */
    private void criarCasoMelhor() {
        ordenacao(inputFile, outputMelhor, false, true);
    }

    /**
     * Cria o caso de ordenação pior ordenando o arquivo de entrada de forma
     * decrescente.
     */
    private void criarCasoPior() {
        ordenacao(inputFile, outputPior, true, false);
    }

    /**
     * Ordena os dados no arquivo especificado e imprime o tempo de execução.
     *
     * @param fileToOrder O arquivo a ser ordenado.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        try {
            // Contar linhas do arquivo
            int rowCount = 0;
            try (BufferedReader counter = new BufferedReader(new FileReader(fileToOrder))) {
                while (counter.readLine() != null)
                    rowCount++;
            }

            String[][] data = new String[rowCount][14];

            // Carregar arquivo em um array
            int index = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(fileToOrder))) {
                String line;
                while ((line = br.readLine()) != null) {
                    data[index++] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                }
            }

            // Início da medição de tempo
            long startTime = System.currentTimeMillis();

            quickSort(data, venueIndex, 0, rowCount - 1);

            // Fim da medição de tempo
            long endTime = System.currentTimeMillis();
            System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");

            // Escrever no arquivo ordenado
            try (FileWriter writer = new FileWriter(fileToOrder)) {
                for (int i = 0; i < rowCount; i++) {
                    writer.write(String.join(",", data[i]) + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implementa o algoritmo Quick Sort com escolha da mediana de três para ordenar
     * um array bidimensional de strings.
     *
     * @param data       O array a ser ordenado.
     * @param venueIndex O índice da coluna usada como chave de ordenação.
     * @param low        O índice inferior do subarray a ser ordenado.
     * @param high       O índice superior do subarray a ser ordenado.
     */
    private void quickSort(String[][] data, int venueIndex, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(data, venueIndex, low, high);

            quickSort(data, venueIndex, low, pivotIndex - 1);
            quickSort(data, venueIndex, pivotIndex + 1, high);
        }
    }

    /**
     * Particiona o array em torno de um pivô e retorna o índice do pivô.
     *
     * @param data       O array a ser particionado.
     * @param venueIndex O índice da coluna usada como chave de ordenação.
     * @param low        O índice inferior do subarray a ser particionado.
     * @param high       O índice superior do subarray a ser particionado.
     * @return O índice do pivô após a partição.
     */
    private int partition(String[][] data, int venueIndex, int low, int high) {
        int mid = low + (high - low) / 2;

        // Encontrar a mediana de três
        if (data[low][venueIndex].compareTo(data[mid][venueIndex]) > 0) {
            swap(data, low, mid);
        }
        if (data[low][venueIndex].compareTo(data[high][venueIndex]) > 0) {
            swap(data, low, high);
        }
        if (data[mid][venueIndex].compareTo(data[high][venueIndex]) > 0) {
            swap(data, mid, high);
        }

        // Colocar o pivô na posição correta
        swap(data, mid, high);

        String[] pivot = data[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (data[j][venueIndex].compareTo(pivot[venueIndex]) <= 0) {
                i++;
                swap(data, i, j);
            }
        }

        swap(data, i + 1, high);
        return i + 1;
    }

    /**
     * Troca duas linhas no array bidimensional.
     *
     * @param data O array que contém as linhas.
     * @param i    O índice da primeira linha.
     * @param j    O índice da segunda linha.
     */
    private void swap(String[][] data, int i, int j) {
        String[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    /**
     * Ordena o arquivo de entrada especificado e escreve os resultados no arquivo
     * de saída correspondente.
     *
     * @param inputFile  O arquivo de entrada a ser ordenado.
     * @param outputFile O arquivo de saída onde o resultado ordenado será escrito.
     * @param inverter   Indica se os dados devem ser invertidos.
     * @param melhorCaso Indica se este é o caso de melhor ordenação.
     */
    private void ordenacao(String inputFile, String outputFile, boolean inverter, boolean melhorCaso) {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                FileWriter writer = new FileWriter(outputFile, true)) {

            String line;
            if ((line = br.readLine()) != null) {
                writer.write(line + "\n");
            }

            int venueIndex = 7;
            int rowCount = 0;

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            while (reader.readLine() != null)
                rowCount++;
            reader.close();

            String[][] data = new String[rowCount][14];

            rowCount = 0;
            while ((line = br.readLine()) != null) {
                String[] values = new String[14];
                StringBuilder sb = new StringBuilder();
                int valueCount = 0;
                boolean insideQuotes = false;
                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        insideQuotes = !insideQuotes;
                    } else if (c == ',' && !insideQuotes) {
                        values[valueCount++] = sb.toString();
                        sb.setLength(0);
                    } else {
                        sb.append(c);
                    }
                }
                values[valueCount] = sb.toString();
                data[rowCount++] = values;
            }

            quickSort(data, venueIndex, 0, rowCount - 1);

            if (inverter) {
                String[][] dataInvertida = new String[rowCount][14];
                for (int i = 0; i < rowCount; i++) {
                    dataInvertida[i] = data[rowCount - i - 1];
                }
                data = dataInvertida;
            }

            for (int i = 0; i < rowCount; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < data[i].length; j++) {
                    String value = data[i][j];
                    if (value.contains(",")) {
                        sb.append('"').append(value).append('"');
                    } else {
                        sb.append(value);
                    }
                    if (j < data[i].length - 1) {
                        sb.append(",");
                    }
                }
                writer.write(sb.toString() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
