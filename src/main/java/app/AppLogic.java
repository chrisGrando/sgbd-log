/**
*** @author chrisGrando
*** Classe com a lógica global do aplicativo.
**/
package app;

import globals.AppSystem;
import globals.PSQL;
//import sgdb.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AppLogic {
    
    //Inicialização do aplicativo
    public void start() {
        this.machineInfo();
    }
    
    //Execução do aplicativo
    public void execute() {
        this.psqlInfo();
        /*PostgreSQL psql = new PostgreSQL();
        psql.connectToPostgres();
        psql.runQuery("select * from departamentos;");
        psql.runQuery("update empregados set salario = 8000 where emp_id = 1;");
        psql.runQuery("select * from empregados;");*/
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
            System.out.println("Diretório: " + myDir);
        }
        catch(URISyntaxException error) {
            String msg = "[FATAL] Não foi possível obter diretório do aplicativo...";
            Logger.getGlobal().log(Level.SEVERE, msg, error);
            System.out.println(msg); //Mensagem no console normal
            System.exit(1); //Fecha aplicativo
        }
        
        System.out.println();
    }
    
    //Exibe dados globais do PostgreSQL
    private void psqlInfo() {
        System.out.println("################# PSQL #################");
        System.out.println("Host: " + PSQL.HOST);
        System.out.println("Port: " + PSQL.PORT);
        System.out.println("Database: " + PSQL.DATABASE);
        System.out.println("User: " + PSQL.USER);
        System.out.println("URL: " + PSQL.GET_URL());
        System.out.println();
    }
    
}
