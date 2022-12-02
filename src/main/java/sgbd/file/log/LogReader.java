/**
*** @author chrisGrando
*** Classe destinada para leitura do "arquivo de log" do SGBD.
*** Referência:
*** < https://www.javatpoint.com/how-to-read-file-line-by-line-in-java >
**/
package sgbd.file.log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class LogReader {
    private String fileText = null;
    
    //Abre e lê o arquivo
    public void read(String path) {
        try {
            //Abre o arquivo
            FileInputStream fis = new FileInputStream(path);       
            Scanner sc = new Scanner(fis);
            
            //Lê o conteúdo do arquivo, linha por linha
            this.fileText = "";
            while(sc.hasNextLine()) {
                //Linha atual
                String line = sc.nextLine();
                
                //Armazena linha atual se não estiver vazia
                if(!line.isBlank()) {
                    this.fileText += line;
                    this.fileText += "\n";
                }
            }
            
            //Fecha o arquivo
            sc.close();
        }
        catch (IOException error) {
            String msg = "[FATAL] Não foi possível abrir o arquivo <" + path + ">...";
            Logger.getGlobal().log(Level.SEVERE, msg, error);
            System.out.println(msg); //Mensagem no console normal
        }
    }
    
    //Retorna o texto do arquivo
    public String getFileText() {
        return this.fileText;
    }
    
}
