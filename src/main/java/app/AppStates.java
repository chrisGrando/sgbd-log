/**
*** @author chrisGrando
*** Classe com a lógica global do aplicativo.
**/
package app;

import globals.AppSystem;
import globals.PSQL;
import sgdb.PostgreSQL;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AppStates {
    
    //Inicialização do aplicativo
    public void start() {
        this.machineInfo();
    }
    
    //Execução do aplicativo
    public void execute() {
        this.psqlInfo();
        PostgreSQL psql = new PostgreSQL();
        psql.connectToPostgres();
    }
    
    //Exibe informações da máquina
    private void machineInfo() {
        System.out.println("################## PC ##################");
        
        //Sistema Operacional
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        System.out.println("SO: " + osName + " | " + osArch + " | " + osVersion);
        
        //Charset local do sistema
        String sysLang = Locale.getDefault().getDisplayLanguage(Locale.ENGLISH);
        String sysCharset = Charset.defaultCharset().displayName(Locale.ENGLISH);
        System.out.println("Charset: " + sysLang + " | " + sysCharset);
        
        //Data e Hora
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy | HH:mm:ss");  
        LocalDateTime ldt = LocalDateTime.now();  
        System.out.println("Data: " + dtf.format(ldt));
        
        //Pasta do aplicativo OU diretório atual
        String myDir;
        try {
            myDir = AppSystem.getJarFolder();
        }
        catch(URISyntaxException error) {
            String msg = "[ERRO] Não foi possível obter caminho do diretório...";
            Logger.getGlobal().log(Level.SEVERE, msg, error);
            myDir = System.getProperty("user.dir");
        }
        System.out.println("Diretório: " + myDir);
    }
    
    //Exibe dados globais do PostgreSQL
    private void psqlInfo() {
        System.out.println("\n################# PSQL #################");
        System.out.println("Host: " + PSQL.HOST);
        System.out.println("Port: " + PSQL.PORT);
        System.out.println("Database: " + PSQL.DATABASE);
        System.out.println("User: " + PSQL.USER);
    }
    
}
