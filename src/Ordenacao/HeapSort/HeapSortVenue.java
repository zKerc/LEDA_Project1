package Ordenacao.HeapSort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A classe {@code HeapSortVenue} realiza a ordenação de dados em arquivos CSV
 * usando o algoritmo de ordenação Heap Sort.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class HeapSortVenue {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/HeapSort/";
    private String outputMedio = path + "matches_t2_venues_heapSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_heapSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_heapSort_piorCaso.csv";
    private int venueIndex = 7;

    /**
     * Cria uma nova instância de {@code HeapSortVenue} com o arquivo de entrada
     * especificado.
     *
     * @param inputFile O arquivo de entrada a ser ordenado.
     */
    public HeapSortVenue(String inputFile) {
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

            heapSort(data, venueIndex);

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
     * Implementa o algoritmo Heap Sort para ordenar um array bidimensional de
     * strings.
     *
     * @param data       O array a ser ordenado.
     * @param venueIndex O índice da coluna usada como chave de ordenação.
     */
    private void heapSort(String[][] data, int venueIndex) {
        int rowCount = data.length;

        // Construir um heap máximo
        for (int i = rowCount / 2 - 1; i >= 0; i--) {
            heapify(data, venueIndex, rowCount, i);
        }

        // Extrair elementos do heap e rearranjar o heap
        for (int i = rowCount - 1; i >= 0; i--) {
            // Mova a raiz atual para o final do array
            swap(data, 0, i);

            // Chame o heapify na subárvore reduzida
            heapify(data, venueIndex, i, 0);
        }
    }

    /**
     * Mantém a propriedade de heap máximo em uma subárvore com a raiz em 'root'.
     *
     * @param data       O array a ser ordenado.
     * @param venueIndex O índice da coluna usada como chave de ordenação.
     * @param rowCount   O número de elementos no heap.
     * @param root       O índice da raiz da subárvore.
     */
    private void heapify(String[][] data, int venueIndex, int rowCount, int root) {
        int largest = root;
        int left = 2 * root + 1;
        int right = 2 * root + 2;

        if (left < rowCount && data[left][venueIndex].compareTo(data[largest][venueIndex]) > 0) {
            largest = left;
        }

        if (right < rowCount && data[right][venueIndex].compareTo(data[largest][venueIndex]) > 0) {
            largest = right;
        }

        if (largest != root) {
            swap(data, root, largest);

            // Recursivamente heapify a subárvore afetada
            heapify(data, venueIndex, rowCount, largest);
        }
    }

    /**
     * Troca dois elementos em um array bidimensional.
     *
     * @param data O array que contém os elementos a serem trocados.
     * @param i    O índice do primeiro elemento.
     * @param j    O índice do segundo elemento.
     */
    private void swap(String[][] data, int i, int j) {
        String[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    /**
     * Realiza a ordenação do arquivo de entrada especificado e escreve os
     * resultados no arquivo de saída correspondente.
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

            heapSort(data, venueIndex);

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
