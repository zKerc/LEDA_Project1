import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String inputMatches = "src/data/matches.csv";
        String outputMatchesT1 = "src/TransformaçõesResultados/matches_T1.csv";
        String[] columnsToKeep = {"id", "home", "away", "date", "year", "time (utc)", "attendance", "venue", "league", "home_score", "away_score", "home_goal_scorers", "away_goal_scorers"};
        
        try (BufferedReader br = new BufferedReader(new FileReader(inputMatches));
                FileWriter writer = new FileWriter(outputMatchesT1)) {
            
            String line;
            int[] indicesToKeep = new int[columnsToKeep.length];
            
            if ((line = br.readLine()) != null) {
                String[] headers = line.split(",");
                
                for (int i = 0; i < headers.length; i++) {
                    for (int j = 0; j < columnsToKeep.length; j++) {
                        if (headers[i].equals(columnsToKeep[j])) {
                            indicesToKeep[j] = i;
                        }
                    }
                }
                
                for (int i = 0; i < indicesToKeep.length; i++) {
                    writer.write(headers[indicesToKeep[i]]);
                    if (i < indicesToKeep.length - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
            
            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                boolean insideQuotes = false;
                int valueCount = 0;
                String[] values = new String[50];
                
                for (char c : line.toCharArray()) {
                    if (c == '\"') {
                        insideQuotes = !insideQuotes;
                    }
                    if (c == ',' && !insideQuotes) {
                        if (valueCount == values.length) {
                            String[] newValues = new String[values.length * 2];
                            System.arraycopy(values, 0, newValues, 0, values.length);
                            values = newValues;
                        }
                        values[valueCount] = sb.toString();
                        sb = new StringBuilder();
                        valueCount++;
                    } else {
                        sb.append(c);
                    }
                }
                if (valueCount == values.length) {
                    String[] newValues = new String[values.length * 2];
                    System.arraycopy(values, 0, newValues, 0, values.length);
                    values = newValues;
                }
                values[valueCount] = sb.toString(); 
                
                for (int i = 0; i < indicesToKeep.length; i++) {
                    if (indicesToKeep[i] <= valueCount) {
                        writer.write(values[indicesToKeep[i]]);
                    }
                    if (i < indicesToKeep.length - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputMatchesT1 = "src/TransformaçõesResultados/matches_T1.csv";
        String outputMatchesT2 = "src/TransformaçõesResultados/matches_T2.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(inputMatchesT1));
             FileWriter writer = new FileWriter(outputMatchesT2)) {

            String line;

            if ((line = br.readLine()) != null) {
                writer.write(line + ",full_date\n");
            }

            SimpleDateFormat inputFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");

            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                boolean insideQuote = false;
                String[] values = new String[50];
                int valueCount = 0;

                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        insideQuote = !insideQuote;
                    }
                    if (c == ',' && !insideQuote) {
                        values[valueCount] = sb.toString().trim();
                        sb.setLength(0); 
                        valueCount++;
                    } else {
                        sb.append(c);
                    }
                }
                values[valueCount] = sb.toString().trim();

                String dateStr = values[3].replaceAll("\"", "") + ", " + values[4];
                Date date = inputFormat.parse(dateStr);
                String fullDate = outputFormat.format(date);

                writer.write(line + "," + fullDate + "\n");
            }

        } catch (IOException | java.text.ParseException e) {
            e.printStackTrace();
        }

        String inputMatchesT2 = "src/TransformaçõesResultados/matches_T2.csv";
        String outputMatchesF1 = "src/TransformaçõesResultados/matches_F1.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(inputMatchesT2));
             FileWriter writer = new FileWriter(outputMatchesF1)) {

            String line;

            if ((line = br.readLine()) != null) {
                writer.write(line + "\n");
            }
            
            int leagueIndex = 8;

            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                boolean insideQuote = false;
                String[] values = new String[50];
                int valueCount = 0;

                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        insideQuote = !insideQuote;
                    }
                    if (c == ',' && !insideQuote) {
                        values[valueCount] = sb.toString().trim();
                        sb.setLength(0);
                        valueCount++;
                    } else {
                        sb.append(c);
                    }
                }
                values[valueCount] = sb.toString().trim();

                if (values[leagueIndex].contains("English Premier League")) {
                    writer.write(line + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputMatchesF1 = "src/TransformaçõesResultados/matches_F1.csv";
        String outputMatchesF2 = "src/TransformaçõesResultados/matches_F2.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(inputMatchesF1));
             FileWriter writer = new FileWriter(outputMatchesF2)) {

            String line;

            if ((line = br.readLine()) != null) {
                writer.write(line + "\n"); 
            }
            
            int attendanceIndex = 6;

            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder();
                boolean insideQuote = false;
                String[] values = new String[50];
                int valueCount = 0;

                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        insideQuote = !insideQuote;
                    }
                    if (c == ',' && !insideQuote) {
                        values[valueCount] = sb.toString().trim();
                        sb.setLength(0);
                        valueCount++;
                    } else {
                        sb.append(c);
                    }
                }
                values[valueCount] = sb.toString().trim();

                if (!values[attendanceIndex].isEmpty()) {
                    int attendance = Integer.parseInt(values[attendanceIndex].replaceAll("[^0-9]", ""));

                    if (attendance > 20000) {
                        writer.write(line + "\n");
                    }
                }
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        int fieldChoice, algorithmChoice;

        do {
            System.out.println("Escolha o campo para ordenar:");
            System.out.println("1- Ordenar por 'venue'");
            System.out.println("2- Ordenar por 'attendance'");
            System.out.println("3- Ordenar por 'full_date'");
            System.out.println("0- Sair");
            System.out.print("Sua escolha: ");
            fieldChoice = scanner.nextInt();

            if (fieldChoice == 0) break;

            do {
                System.out.println("Escolha o algoritmo de ordenação:");
                System.out.println("1- InsertionSort");
                System.out.println("2- SelectionSort");
                // Adicione as outras opções de algoritmo aqui
                System.out.println("0- Voltar");
                System.out.print("Sua escolha: ");
                algorithmChoice = scanner.nextInt();

                if (algorithmChoice == 0) break;

                switch (fieldChoice) {
                    case 1:
                        // Chame a função de ordenação correspondente para 'venue' e o algoritmo escolhido aqui
                        break;
                    case 2:
                        // Chame a função de ordenação correspondente para 'attendance' e o algoritmo escolhido aqui
                        break;
                    case 3:
                        // Chame a função de ordenação correspondente para 'full_date' e o algoritmo escolhido aqui
                        break;
                }

            } while (algorithmChoice != 0);

        } while (fieldChoice != 0);

        scanner.close();
    }
}

