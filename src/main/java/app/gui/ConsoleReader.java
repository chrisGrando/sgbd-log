/**
*** @author chrisGrando
*** Classe destinada para leitura do arquivo do console.
**/
package app.gui;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class ConsoleReader {
    private String fileContents = null;
    
    //Lê arquivo
    public void read(String path) {
        try {
            //Abre o arquivo
            FileInputStream fis = new FileInputStream(path);       
            Scanner sc = new Scanner(fis);
            
            //Inicializa o conteúdo do arquivo
            this.fileContents = "";
            
            //Lê o conteúdo do arquivo, linha por linha
            while(sc.hasNextLine()) {
                //Armazena a linha atual
                String line = sc.nextLine();
                this.fileContents += line + "\n";
            }

            //Fecha o arquivo
            sc.close();
        }
        catch (IOException error) {
            String msg = "[AVISO] Não foi possível ler o arquivo <" + path + ">...";
            Logger.getGlobal().log(Level.WARNING, msg, error);
            System.out.println(msg); //Mensagem no console normal
            this.fileContents = msg;
        }
    }
    
    //Retorna texto lido do arquivo
    public String getText() {
        return this.fileContents;
    }
}
