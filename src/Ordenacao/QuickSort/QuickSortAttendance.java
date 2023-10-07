package Ordenacao.QuickSort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A classe {@code QuickSortAttendance} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Quick Sort.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class QuickSortAttendance {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/QuickSort/";
    private String outputMedio = path + "matches_t2_attendance_quickSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_attendance_quickSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_attendance_quickSort_piorCaso.csv";
    private int attendanceIndex = 6;

    /**
     * Cria uma nova instância do QuickSortAttendance.
     *
     * @param inputFile O arquivo de entrada contendo os dados de atendimento a
     *                  serem ordenados.
     */
    public QuickSortAttendance(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Realiza a ordenação dos casos de melhor, médio e pior cenários e imprime os
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
     * Cria um arquivo de caso médio que é uma cópia do arquivo de entrada.
     */
    private void criarCasoMedio() {
        copiarArquivo(inputFile, outputMedio);
    }

    /**
     * Cria um arquivo de melhor caso ordenando os dados em ordem crescente de
     * atendimento.
     */
    private void criarCasoMelhor() {
        try {
            int rowCount = contarLinhas(inputFile);
            String[][] data = carregarArquivoEmArray(inputFile, rowCount);

            quickSort(data, attendanceIndex, 0, rowCount - 1); // Ordena o array

            try (FileWriter writer = new FileWriter(outputMelhor)) {
                for (int i = 0; i < rowCount; i++) {
                    writer.write(String.join(",", data[i]) + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cria um arquivo de pior caso ordenando os dados em ordem decrescente de
     * atendimento.
     */
    private void criarCasoPior() {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                FileWriter writer = new FileWriter(outputPior, false)) {

            int rowCount = 0;
            while (br.readLine() != null)
                rowCount++;

            br.close();

            String[][] data = new String[rowCount][14];

            BufferedReader newBr = new BufferedReader(new FileReader(inputFile));
            int index = 0;
            String line;
            while ((line = newBr.readLine()) != null) {
                data[index++] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            }
            newBr.close();

            String[] header = data[0];
            String[][] dataArray = new String[rowCount - 1][14];
            System.arraycopy(data, 1, dataArray, 0, rowCount - 1);

            quickSort(dataArray, attendanceIndex, 0, rowCount - 2);

            for (int i = 0; i < dataArray.length / 2; i++) {
                String[] temp = dataArray[i];
                dataArray[i] = dataArray[dataArray.length - i - 1];
                dataArray[dataArray.length - i - 1] = temp;
            }

            writer.write(String.join(",", header) + "\n");
            for (int i = 0; i < rowCount - 1; i++) {
                writer.write(String.join(",", dataArray[i]) + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copia um arquivo de origem para um arquivo de destino.
     *
     * @param origem  O arquivo de origem.
     * @param destino O arquivo de destino.
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
     * Realiza a ordenação e imprime o tempo de execução.
     *
     * @param fileToOrder O arquivo a ser ordenado.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        try {
            int rowCount = contarLinhas(fileToOrder);
            String[][] data = carregarArquivoEmArray(fileToOrder, rowCount);

            String[][] dataArray = new String[rowCount - 1][14];
            System.arraycopy(data, 1, dataArray, 0, rowCount - 1);

            long startTime = System.currentTimeMillis();

            quickSort(dataArray, attendanceIndex, 0, rowCount - 2);

            long endTime = System.currentTimeMillis();
            System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Conta o número de linhas em um arquivo.
     *
     * @param fileToOrder O arquivo para o qual as linhas serão contadas.
     * @return O número de linhas no arquivo.
     * @throws IOException Se ocorrer um erro de leitura do arquivo.
     */
    private int contarLinhas(String fileToOrder) throws IOException {
        int rowCount = 0;
        try (BufferedReader counter = new BufferedReader(new FileReader(fileToOrder))) {
            while (counter.readLine() != null)
                rowCount++;
        }
        return rowCount;
    }

    /**
     * Carrega os dados de um arquivo CSV em um array bidimensional.
     *
     * @param fileToOrder O arquivo CSV a ser carregado.
     * @param rowCount    O número de linhas no arquivo.
     * @return Um array bidimensional contendo os dados do arquivo.
     * @throws IOException Se ocorrer um erro de leitura do arquivo.
     */
    private String[][] carregarArquivoEmArray(String fileToOrder, int rowCount) throws IOException {
        String[][] data = new String[rowCount][14];

        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileToOrder))) {
            String line;
            while ((line = br.readLine()) != null) {
                data[index++] = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            }
        }
        return data;
    }

    /**
     * Ordena um array usando o algoritmo Quick Sort.
     *
     * @param data        O array a ser ordenado.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param low         O índice mais baixo do array.
     * @param high        O índice mais alto do array.
     */
    private void quickSort(String[][] data, int columnIndex, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(data, columnIndex, low, high);

            quickSort(data, columnIndex, low, pivotIndex - 1);
            quickSort(data, columnIndex, pivotIndex + 1, high);
        }
    }

    /**
     * Realiza a partição de um array para o algoritmo Quick Sort.
     *
     * @param data        O array a ser particionado.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param low         O índice mais baixo do array.
     * @param high        O índice mais alto do array.
     * @return O índice do pivô após a partição.
     */
    private int partition(String[][] data, int columnIndex, int low, int high) {
        String pivot = data[high][columnIndex];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (Integer.parseInt(data[j][columnIndex]) <= Integer.parseInt(pivot)) {
                i++;
                String[] temp = data[i];
                data[i] = data[j];
                data[j] = temp;
            }
        }

        String[] temp = data[i + 1];
        data[i + 1] = data[high];
        data[high] = temp;

        return i + 1;
    }
}