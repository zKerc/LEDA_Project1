package Ordenacao.MergeSort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A classe {@code MergeSortVenue} realiza a ordenação de dados em arquivos CSV
 * usando o algoritmo de ordenação Merge Sort.
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
     * Cria uma nova instância de {@code MergeSortVenue} com o arquivo de entrada
     * especificado.
     *
     * @param inputFile O arquivo de entrada a ser ordenado.
     */
    public MergeSortVenue(String inputFile) {
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

            mergeSort(data, venueIndex, rowCount);

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
     * Ordena um array bidimensional de strings usando o algoritmo de ordenação
     * Merge Sort.
     *
     * @param data       O array a ser ordenado.
     * @param venueIndex O índice da coluna usada como chave de ordenação.
     * @param rowCount   O número de linhas no array.
     */
    private void mergeSort(String[][] data, int venueIndex, int rowCount) {
        if (rowCount < 2) {
            return;
        }

        int mid = rowCount / 2;
        String[][] left = new String[mid][14];
        String[][] right = new String[rowCount - mid][14];

        // Copiar dados para arrays esquerdo e direito
        for (int i = 0; i < mid; i++) {
            left[i] = data[i];
        }
        for (int i = mid; i < rowCount; i++) {
            right[i - mid] = data[i];
        }

        // Recursivamente ordenar as duas metades
        mergeSort(left, venueIndex, mid);
        mergeSort(right, venueIndex, rowCount - mid);

        // Mesclar as duas metades ordenadas
        merge(data, left, right, venueIndex);
    }

    /**
     * Mescla dois arrays ordenados em um único array ordenado.
     *
     * @param data       O array a ser preenchido com os dados mesclados.
     * @param left       O array esquerdo a ser mesclado.
     * @param right      O array direito a ser mesclado.
     * @param venueIndex O índice da coluna usada como chave de ordenação.
     */
    private void merge(String[][] data, String[][] left, String[][] right, int venueIndex) {
        int leftLength = left.length;
        int rightLength = right.length;
        int i = 0, j = 0, k = 0;

        while (i < leftLength && j < rightLength) {
            if (left[i][venueIndex].compareTo(right[j][venueIndex]) <= 0) {
                data[k++] = left[i++];
            } else {
                data[k++] = right[j++];
            }
        }

        while (i < leftLength) {
            data[k++] = left[i++];
        }

        while (j < rightLength) {
            data[k++] = right[j++];
        }
    }

    /**
     * Realiza a ordenação do arquivo de entrada e escreve o resultado no arquivo de
     * saída.
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

            mergeSort(data, venueIndex, rowCount);

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
