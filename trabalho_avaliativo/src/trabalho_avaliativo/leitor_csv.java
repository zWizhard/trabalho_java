package trabalho_avaliativo;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;

public class leitor_csv {
    public static void main(String[] args) {
        String csvFile = "C:\\Users\\eduar\\Downloads\\csv\\Billionaires Statistics Dataset.csv";
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(csvFile));
            String[] line;
            while ((line = reader.readNext()) != null) {
                System.out.println("Cell [1] : " + line[0] + ", Cell [2] : " + line[1] + " , etc.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
