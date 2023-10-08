package Ordenacao.HeapSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A classe {@code HeapSortFullDate} realiza a ordenação de dados em arquivos
 * CSV usando o algoritmo de ordenação Heap Sort.
 * Ela é projetada para criar três casos de ordenação (melhor, médio e pior) e
 * medir o tempo de execução.
 * Os resultados ordenados são escritos nos arquivos de saída correspondentes.
 */
public class HeapSortFullDate {

    private String inputFile;
    private String path = "src/OrdenacaoResultados/HeapSort/";
    private String outputMedio = path + "matches_t2_full_date_heapSort_medioCaso.csv";
    private String outputMelhor = path + "matches_t2_full_date_heapSort_melhorCaso.csv";
    private String outputPior = path + "matches_t2_full_date_heapSort_piorCaso.csv";
    private int fullDateIndex = 13;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Cria uma nova instância de HeapSortFullDate com o caminho do arquivo de
     * entrada especificado.
     *
     * @param inputFile O caminho do arquivo de entrada contendo os dados a serem
     *                  ordenados.
     */
    public HeapSortFullDate(String inputFile) {
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
     * Cria o caso de cenário melhor ordenando os dados com o algoritmo Heap Sort
     * e, em seguida, escreve os resultados no arquivo de saída correspondente.
     */
    private void criarCasoMelhor() {
        String[][] data = carregarArquivoEmArray(inputFile);
        heapSort(data, fullDateIndex);
        escreverDados(data, outputMelhor);
    }

    /**
     * Cria o caso de cenário pior ordenando os dados com o algoritmo Heap Sort
     * em ordem decrescente e, em seguida, escreve os resultados no arquivo de saída
     * correspondente.
     */
    private void criarCasoPior() {
        String[][] data = carregarArquivoEmArray(inputFile);
        heapSort(data, fullDateIndex);
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
            data = br.lines().skip(1).map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)).toArray(String[][]::new);
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
            writer.write("id,home,away,date,year,time (utc),attendance,venue,league,home_score,away_score,home_goal_scorers,away_goal_scorers,full_date");
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
     * Realiza a ordenação e mede o tempo de execução usando o algoritmo Heap Sort.
     * O tempo de execução é impresso no console.
     *
     * @param fileToOrder O caminho do arquivo a ser ordenado e medido.
     */
    private void ordenarEImprimirTempo(String fileToOrder) {
        String[][] data = carregarArquivoEmArray(fileToOrder);

        long startTime = System.currentTimeMillis();
        heapSort(data, fullDateIndex);
        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de execução para " + fileToOrder + ": " + (endTime - startTime) + " ms");
    }

    /**
     * Ordena os dados usando o algoritmo Heap Sort.
     * Este método usa o método heapify para criar uma estrutura de heap e, em seguida, 
     * ordena os dados usando a propriedade de heap.
     *
     * @param data        O array bidimensional contendo os dados a serem ordenados.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     */
    private void heapSort(String[][] data, int columnIndex) {
        int rowCount = data.length;
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
     * Método auxiliar do algoritmo Heap Sort para garantir a propriedade do heap.
     *
     * @param data        O array bidimensional contendo os dados.
     * @param columnIndex O índice da coluna pela qual os dados serão ordenados.
     * @param rowCount    O número total de linhas.
     * @param i           O índice do nó atual.
     */
    private void heapify(String[][] data, int columnIndex, int rowCount, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < rowCount && isDateGreater(data[left][columnIndex], data[largest][columnIndex])) {
            largest = left;
        }

        if (right < rowCount && isDateGreater(data[right][columnIndex], data[largest][columnIndex])) {
            largest = right;
        }

        if (largest != i) {
            String[] swap = data[i];
            data[i] = data[largest];
            data[largest] = swap;

            heapify(data, columnIndex, rowCount, largest);
        }
    }

    /**
     * Determina se uma data é maior do que outra.
     * Utiliza o formato "dd/MM/yyyy" para comparar as datas.
     *
     * @param date1 A primeira data.
     * @param date2 A segunda data.
     * @return true se a primeira data for maior do que a segunda, false caso contrário.
     */
    private boolean isDateGreater(String date1, String date2) {
        try {
            Date d1 = sdf.parse(date1.replace("\"", ""));
            Date d2 = sdf.parse(date2.replace("\"", ""));
            return d1.compareTo(d2) > 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
