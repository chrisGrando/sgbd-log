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
    //Mensagens de aviso
    private final String INCORRECT_USE;
    private final String CALL_HELP;
    
    //Construtor
    public AppLogic() {
        INCORRECT_USE = "[AVISO] Parâmetro desconhecido ou uso incorreto!";
        CALL_HELP = "Use [-h] para ver a lista de parâmetros disponíveis.";
    }
    
    //Inicialização do aplicativo
    public void start(String[] args) {
        //Número total de parâmetros
        final int size = args.length;
        
        //Parâmetros de linha de comando
        for(int i = 0; i < size; i++) {
            char firstChar = args[i].charAt(0);
            
            //Pula parâmetro se não conter hífen no início
            if(Character.compare(firstChar, '-') != 0)
                continue;
            
            //Checa se é um parâmetro válido
            if(this.isValid(args, i, size)) {
               //Processa os parâmetros
                switch (args[i]) {
                    case "-v":
                        String info = AppSystem.getAppName() + " ~ " + AppSystem.getAppVersion();
                        AppSystem.CONSOLE_OUTPUT.println(info);
                        System.exit(0);
                        break;
                    case "-h":
                        String help = AppSystem.getHelpText();
                        AppSystem.CONSOLE_OUTPUT.println(help);
                        System.exit(0);
                        break;
                    case "-host":
                        PSQL.HOST = args[i + 1];
                        break;
                    case "-port":
                        PSQL.PORT = args[i + 1];
                        break;
                    case "-database":
                        PSQL.DATABASE = args[i + 1];
                        break;
                    case "-user":
                        PSQL.USER = args[i + 1];
                        break;
                    case "-password":
                        PSQL.PASSWORD = args[i + 1];
                        break;
                    case "-log":
                        AppSystem.SGBD_LOG = args[i + 1];
                        break;
                    case "-table":
                        AppSystem.TABLE_NAME = args[i + 1];
                        break;
                    case "-json":
                        AppSystem.JSON_TABLE = args[i + 1];
                        break;
                    default:
                        System.out.println(this.INCORRECT_USE);
                        System.out.println("Comando: " + args[i]);
                        System.out.println(this.CALL_HELP);
                        System.out.println();
                }
            }
        }
    }
    
    //Execução do aplicativo
    public void execute() {
        //Informações do PC
        this.machineInfo();
        
        //Informações do PostgreSQL
        this.psqlInfo();
        
        /*PostgreSQL psql = new PostgreSQL();
        psql.connectToPostgres();
        psql.runQuery("select * from departamentos;");
        psql.runQuery("update empregados set salario = 8000 where emp_id = 1;");
        psql.runQuery("select * from empregados;");*/
    }
    
    //Checa se um parâmetro é válido
    private boolean isValid(String[] list, int position, int max) {
        boolean isLastItem = false;
        
        //Checa se é o último item da lista
        if(position == (max - 1))
            isLastItem = true;
        
        //Se o parâmetro não for o último da lista...
        if(!isLastItem) {
            char checkNext = list[position + 1].charAt(0);
            
            //Checa se o próximo parâmetro não contém hífen no início
            if(Character.compare(checkNext, '-') != 0)
                return true;
        }
        
        //Checa se o parâmetro é para listar comandos ou exibir versão
        if(list[position].equals("-h") || list[position].equals("-v"))
            return true;
        
        //Parâmetro não é válido
        System.out.println(this.INCORRECT_USE);
        System.out.println("Comando: " + list[position]);
        System.out.println(this.CALL_HELP);
        System.out.println();
        
        return false;
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
