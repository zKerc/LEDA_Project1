package Ordenacao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectionSortVenue {
    public static void main(String[] args) {
        String inputFile = "src/TransformacaoResultados/matches_T2.csv";

        String path = "src/OrdenacaoResultados/SelectionSort/";

        String outputMedio = path + "matches_t2_venues_selectionSort_medioCaso.csv";
        String outputMelhor = path + "matches_t2_venues_selectionSort_melhorCaso.csv";
        String outputPior = path + "matches_t2_venues_selectionSort_piorCaso.csv";

        ordenar(inputFile, outputMedio); // Para caso médio

        // Para melhor caso: suponha que o array já está ordenado, então não faça nada.
        ordenar(inputFile, outputMelhor);

        // Para pior caso: inverta o array depois de ordenado no caso médio e reordene.
        ordenar(outputMedio, outputPior, true);
    }

    public static void ordenar(String inputFile, String outputFile) {
        ordenar(inputFile, outputFile, false);
    }

    public static void ordenar(String inputFile, String outputFile, boolean inverter) {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                FileWriter writer = new FileWriter(outputFile)) {

            String line;
            if ((line = br.readLine()) != null) {
                writer.write(line + "\n"); // Escreve o cabeçalho no arquivo de saída
            }

            int venueIndex = 7; // 'venue' é a 8ª coluna, então o índice é 7
            int rowCount = 0;

            // Para calcular o número de linhas no arquivo
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            while (reader.readLine() != null)
                rowCount++;
            reader.close();

            // Ajustar o tamanho do array baseado no número de linhas
            String[][] data = new String[rowCount][14];

            rowCount = 0; // Resetar rowCount para reutilizar na leitura das linhas
            // Ler as linhas restantes e armazenar em um array
            while ((line = br.readLine()) != null) {
                List<String> values = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                boolean insideQuotes = false;
                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        insideQuotes = !insideQuotes;
                    } else if (c == ',' && !insideQuotes) {
                        values.add(sb.toString());
                        sb.setLength(0);
                    } else {
                        sb.append(c);
                    }
                }
                values.add(sb.toString());
                data[rowCount++] = values.toArray(new String[0]);
            }

            // Obter o tempo inicial
            long startTime = System.currentTimeMillis();

            // Implementação do Selection Sort
            for (int i = 0; i < rowCount - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < rowCount; j++) {
                    if (data[j][venueIndex].compareTo(data[minIndex][venueIndex]) < 0) {
                        minIndex = j;
                    }
                }
                // Troca os elementos
                String[] temp = data[minIndex];
                data[minIndex] = data[i];
                data[i] = temp;
            }

            // Se pior caso, inverter o array
            if (inverter) {
                String[][] dataInvertida = new String[rowCount][14];
                for (int i = 0; i < rowCount; i++) {
                    dataInvertida[i] = data[rowCount - i - 1];
                }
                data = dataInvertida;
            }

            // Obter o tempo final
            long endTime = System.currentTimeMillis();

            // Calcular e imprimir a diferença
            long elapsedTime = endTime - startTime;
            System.out.println("Tempo de execução para " + outputFile + ": " + elapsedTime + " ms");

            // Escrever as linhas ordenadas no arquivo de saída
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
