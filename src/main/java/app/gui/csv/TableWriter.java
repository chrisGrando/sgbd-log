/**
*** @author chrisGrando
*** Classe destinada para gravar a tabela do query em uma planilha CSV.
**/
package app.gui.csv;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableWriter {
    
    //Grava o arquivo
    public void write(String path, List<String[]> table) {
        try {
            //Cria arquivo CSV
            CSVWriter csvWriter;
            File file = new File(path);
            FileWriter outputFile = new FileWriter(file);
            csvWriter = new CSVWriter(outputFile);

            //Converte tabela em String, linha por linha, para arquivo CSV
            for (String[] line : table) {
                csvWriter.writeNext(line);
            }

            //Fecha o arquivo
            csvWriter.close();
        }
        catch(IOException error) {
            String msg = "[FATAL] Não foi possível gravar a planilha <" + path + ">...";
            Logger.getGlobal().log(Level.SEVERE, msg, error);
            System.out.println(msg);
        }
    }
    
}
