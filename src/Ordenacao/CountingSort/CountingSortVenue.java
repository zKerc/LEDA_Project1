package Ordenacao.CountingSort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A classe {@code CountingSortVenue} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Counting Sort.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class CountingSortVenue {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/CountingSort/";
    private String outputMedio = path + "matches_t2_venues_countingSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_venues_countingSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_venues_countingSort_piorCaso.csv";
    private int venueIndex = 7;

    /**
     * Cria uma nova instância de {@code CountingSortVenue} com o arquivo de entrada
     * especificado.
     *
     * @param inputFile O arquivo de entrada a ser ordenado.
     */
    public CountingSortVenue(String inputFile) {
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

            countingSort(data, venueIndex);

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
     * Implementa o algoritmo Counting Sort para ordenar um array bidimensional de
     * strings.
     *
     * @param data       O array a ser ordenado.
     * @param venueIndex O índice da coluna usada como chave de ordenação.
     */
    private void countingSort(String[][] data, int venueIndex) {
        int rowCount = data.length;
        int maxVenue = Integer.MIN_VALUE;
        int minVenue = Integer.MAX_VALUE;

        // Encontrar o valor máximo e mínimo em venueIndex
        for (int i = 0; i < rowCount; i++) {
            int venueValue = Integer.parseInt(data[i][venueIndex]);
            if (venueValue > maxVenue) {
                maxVenue = venueValue;
            }
            if (venueValue < minVenue) {
                minVenue = venueValue;
            }
        }

        int range = maxVenue - minVenue + 1;
        int[] countArray = new int[range];
        String[][] outputArray = new String[rowCount][14];

        // Inicializar o array de contagem
        for (int i = 0; i < rowCount; i++) {
            int venueValue = Integer.parseInt(data[i][venueIndex]);
            countArray[venueValue - minVenue]++;
        }

        // Atualizar o array de contagem com as contagens acumulativas
        for (int i = 1; i < range; i++) {
            countArray[i] += countArray[i - 1];
        }

        // Construir o array de saída
        for (int i = rowCount - 1; i >= 0; i--) {
            int venueValue = Integer.parseInt(data[i][venueIndex]);
            outputArray[countArray[venueValue - minVenue] - 1] = data[i];
            countArray[venueValue - minVenue]--;
        }

        // Copiar o array de saída de volta para o array original
        System.arraycopy(outputArray, 0, data, 0, rowCount);
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

            countingSort(data, venueIndex);

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
