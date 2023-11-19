package trabalho_avaliativo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexão {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/trabalho";
        String username = "root";
        String password = "3382";

        System.out.println("Conectando ao banco de dados...");

        long startTime = System.nanoTime();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Conexão bem-sucedida!");
        } catch (SQLException e) {
            throw new IllegalStateException("Não foi possível conectar ao banco de dados!", e);
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  // tempo em nanosegundos
        double seconds = (double)duration / 1_000_000_000.0; // convertendo para segundos

        System.out.println("Tempo consumido no processamento: " + seconds + " segundos");
    }
}
