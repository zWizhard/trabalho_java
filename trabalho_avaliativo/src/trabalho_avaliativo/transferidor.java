package trabalho_avaliativo;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class transferidor {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/trabalho";
        String username = "root";
        String password = "3382";

        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o caminho completo para o arquivo CSV:");
        String csvFile = scanner.nextLine();

        long startTime = System.nanoTime();
        try (CSVReader reader = new CSVReader(new FileReader(csvFile));
             Connection connection = DriverManager.getConnection(url, username, password)) {

            String[] headers = reader.readNext();
            if (headers == null) {
                throw new IllegalStateException("O arquivo CSV está vazio!");
            }

            StringBuilder sql = new StringBuilder("CREATE TABLE Dados_Bilionários (");
            for (String header : headers) {
                sql.append(header).append(" VARCHAR(255), ");
            }
            sql.delete(sql.length() - 2, sql.length()).append(")");

            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
                pstmt.execute();
            }

            System.out.println("Tabela criada com sucesso!");

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                StringBuilder insertSql = new StringBuilder("INSERT INTO Dados_Bilionários VALUES (");
                for (String value : nextLine) {
                    insertSql.append("?, ");
                }
                insertSql.delete(insertSql.length() - 2, insertSql.length()).append(")");

                try (PreparedStatement pstmt = connection.prepareStatement(insertSql.toString())) {
                    for (int i = 0; i < nextLine.length; i++) {
                        if (nextLine[i].matches("-?\\d+")) { // Inteiro
                            pstmt.setInt(i + 1, Integer.parseInt(nextLine[i]));
                        } else if (nextLine[i].matches("-?\\d+\\.\\d+")) { // Ponto flutuante
                            pstmt.setDouble(i + 1, Double.parseDouble(nextLine[i]));
                        } else if (nextLine[i].equalsIgnoreCase("true") || nextLine[i].equalsIgnoreCase("false")) { // Booleano
                            pstmt.setBoolean(i + 1, Boolean.parseBoolean(nextLine[i]));
                        } else { // String
                            pstmt.setString(i + 1, nextLine[i]);
                        }
                    }
                    pstmt.executeUpdate();
                }
            }

            System.out.println("Dados inseridos com sucesso!");

            // Contar o número de registros
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Dados_Bilionários")) {
                if (rs.next()) {
                    System.out.println("Número de registros: " + rs.getInt(1));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Coordenadas do diretório errado, tente novamente");
        } catch (IOException | CsvValidationException | SQLException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  // tempo em nanosegundos
        double seconds = (double)duration / 1_000_000_000.0; // convertendo para segundos

        System.out.println("Tempo consumido no processamento: " + seconds + " segundos");
    }
}
