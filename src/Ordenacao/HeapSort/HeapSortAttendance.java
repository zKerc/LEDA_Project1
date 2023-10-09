package Ordenacao.HeapSort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * A classe {@code HeapSortAttendance} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Heap Sort.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class HeapSortAttendance {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/HeapSort/";
    private String outputMedio = path + "matches_t2_attendance_heapSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_attendance_heapSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_attendance_heapSort_piorCaso.csv";
    private int attendanceIndex = 6;

    /**
     * Cria uma nova instância do HeapSortAttendance.
     *
     * @param inputFile O arquivo de entrada contendo os dados de atendimento a
     *                  serem ordenados.
     */
    public HeapSortAttendance(String inputFile) {
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

        System.out.println("Ordenando utilizando o algoritmo Heap Sort...");

        ordenarEImprimirTempo(outputMelhor);

        ordenarEImprimirTempo(outputMedio);

        ordenarEImprimirTempo(outputPior);
        System.out.println("\nOrdenação concluída com sucesso!");
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

            // Salvando o cabeçalho antes da ordenação
            String[] header = data[0];

            // Criando um novo array sem o cabeçalho para a ordenação
            String[][] dataArray = new String[rowCount - 1][];
            System.arraycopy(data, 1, dataArray, 0, rowCount - 1);

            heapSort(dataArray, attendanceIndex, rowCount - 1); // Ordena o array

            // Escrevendo o resultado no arquivo, incluindo o cabeçalho
            try (FileWriter writer = new FileWriter(outputMelhor)) {
                writer.write(String.join(",", header) + "\n");
                for (int i = 0; i < rowCount - 1; i++) {
                    writer.write(String.join(",", dataArray[i]) + "\n");
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

            heapSort(dataArray, attendanceIndex, rowCount - 1);

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

            heapSort(dataArray, attendanceIndex, rowCount - 1);

            long endTime = System.currentTimeMillis();
            System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
            imprimirConsumoMemoria(); // Imprimir consumo de memória após a ordenação

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
     * Ordena um array bidimensional com base em uma coluna específica usando o
     * algoritmo Heap Sort.
     *
     * @param data        O array a ser ordenado.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param rowCount    O número de linhas no array.
     */
    private void heapSort(String[][] data, int columnIndex, int rowCount) {
        for (int i = rowCount / 2 - 1; i >= 0; i--) {
            heapify(data, columnIndex, rowCount, i);
        }

        for (int i = rowCount - 1; i > 0; i--) {
            String[] temp = data[0];
            data[0] = data[i];
            data[i] = temp;

            heapify(data, columnIndex, i, 0);
        }
    }

    /**
     * Limpa e converte a string de atendimento em um número inteiro.
     *
     * @param attendance A string de atendimento a ser limpa e convertida.
     * @return O valor numérico do atendimento.
     */
    private int sanitizeAttendance(String attendance) {
        if (attendance == null || attendance.isEmpty()) {
            return 0;
        }

        // Remove as aspas extras e substitui vírgulas.
        attendance = attendance.replace("\"", "").replace(",", "");
        return Integer.parseInt(attendance);
    }

    /**
     * Transforma um array em um heap binário, usando a coluna de atendimento como
     * critério de ordenação.
     *
     * @param data        O array a ser transformado em um heap.
     * @param columnIndex O índice da coluna pela qual os dados são comparados.
     * @param rowCount    O número de elementos no heap.
     * @param root        O índice da raiz do heap.
     */
    private void heapify(String[][] data, int columnIndex, int rowCount, int root) {
        int largest = root;
        int left = 2 * root + 1;
        int right = 2 * root + 2;

        // Usa a função sanitizeAttendance para limpar e converter a string de
        // atendimento em um inteiro.
        if (left < rowCount
                && sanitizeAttendance(data[left][columnIndex]) > sanitizeAttendance(data[largest][columnIndex])) {
            largest = left;
        }

        if (right < rowCount
                && sanitizeAttendance(data[right][columnIndex]) > sanitizeAttendance(data[largest][columnIndex])) {
            largest = right;
        }

        if (largest != root) {
            String[] swap = data[root];
            data[root] = data[largest];
            data[largest] = swap;

            heapify(data, columnIndex, rowCount, largest);
        }
    }

    /**
     * Imprime o consumo de memória atual do sistema.
     */
    private void imprimirConsumoMemoria() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();

        long usedMemory = heapMemoryUsage.getUsed();

        System.out.println("Consumo de memória: " + usedMemory + " bytes");
    }
}
