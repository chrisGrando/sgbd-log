/**
*** @author chrisGrando
*** Classe com a lógica global do aplicativo.
**/
package app;

import app.gui.MainWindow;
import globals.AppSystem;
import globals.PSQL;
import sgbd.file.log.LogBehavior;
import sgbd.file.json.JsonBehavior;
import sgbd.PostgreSQL;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AppLogic {
    
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
            
            //Processa os parâmetros
            switch (args[i]) {
                case "-v":
                    String info = AppSystem.getAppName() + " ~ " + AppSystem.getAppVersion();
                    System.out.println(info);
                    System.exit(0);
                    break;
                case "-h":
                    String help = AppSystem.getHelpText();
                    System.out.println(help);
                    System.exit(0);
                    break;
                case "-gui":
                    AppSystem.GUI = true;
                    break;
                case "-host":
                    if(this.isDoubleCommand(args, i, size))
                        PSQL.HOST = args[i + 1];
                    else
                        this.invalidParameter(args, i, false);
                    break;
                case "-port":
                    if(this.isDoubleCommand(args, i, size))
                        PSQL.PORT = args[i + 1];
                    else
                        this.invalidParameter(args, i, false);
                    break;
                case "-database":
                    if(this.isDoubleCommand(args, i, size))
                        PSQL.DATABASE = args[i + 1];
                    else
                        this.invalidParameter(args, i, false);
                    break;
                case "-user":
                    if(this.isDoubleCommand(args, i, size))
                        PSQL.USER = args[i + 1];
                    else
                        this.invalidParameter(args, i, false);
                    break;
                case "-password":
                    if(this.isDoubleCommand(args, i, size))
                        PSQL.PASSWORD = args[i + 1];
                    else
                        this.invalidParameter(args, i, false);
                    break;
                case "-log":
                    if(this.isDoubleCommand(args, i, size))
                        AppSystem.SGBD_LOG = args[i + 1];
                    else
                        this.invalidParameter(args, i, false);
                    break;
                case "-table":
                    if(this.isDoubleCommand(args, i, size))
                        AppSystem.TABLE_NAME = args[i + 1];
                    else
                        this.invalidParameter(args, i, false);
                    break;
                case "-json":
                    if(this.isDoubleCommand(args, i, size))
                        AppSystem.JSON_TABLE = args[i + 1];
                    else
                        this.invalidParameter(args, i, false);
                    break;
                default:
                    if(this.isDoubleCommand(args, i, size))
                        this.invalidParameter(args, i, true);
                    else
                        this.invalidParameter(args, i, false);
            }
        }
    }
    
    //Execução do aplicativo
    public void execute() {
        //Informações do PC
        this.machineInfo();
        
        //Variáveis padrões do PostgreSQL
        this.psqlInfo();
        
        //Modo de interface gráfica
        if(AppSystem.GUI) {
            MainWindow mw = new MainWindow();
            mw.start();
        }
        //Modo de linha de comando
        else {
            //Inicializa e conecta-se ao PostgreSQL
            PostgreSQL psql = new PostgreSQL();
            psql.connectToPostgres();

            //Obtém dados do arquivo JSON 
            JsonBehavior jb = new JsonBehavior();
            jb.openJsonFile(AppSystem.JSON_TABLE);

            //Exibe conteúdo do arquivo
            System.out.println(jb.showContents());

            //Insere dados obtidos no Postgres
            jb.insertDataIntoPostgres(psql, AppSystem.TABLE_NAME);

            //Exibe o conteúdo da tabela no Postgres
            psql.runQuery("select * from " + AppSystem.TABLE_NAME + ";");

            //Simula a execução dos comandos do arquivo de log do SGBD
            LogBehavior lb = new LogBehavior();
            lb.openFile(AppSystem.SGBD_LOG);
            lb.runLogInterpreter(psql, AppSystem.TABLE_NAME);

            //Exibe a tabela no Postgres após a execução da simulação
            psql.runQuery("select * from " + AppSystem.TABLE_NAME + " order by id asc;");
        }
    }
    
    //Checa se um parâmetro possui dois comandos
    private boolean isDoubleCommand(String[] list, int position, int max) {
        boolean isLastItem = false;
        
        //Checa se é o último item da lista
        if(position == (max - 1))
            isLastItem = true;
        
        //Se o parâmetro não for o último da lista...
        if(!isLastItem) {
            char checkNext = list[position + 1].charAt(0);
            
            //Checa se o próximo parâmetro não contém hífen no início
            if(Character.compare(checkNext, '-') != 0) {
                //Parâmetro possui dois comandos
                return true;
            }
        }
        
        //Parâmetro NÃO possui dois comandos
        return false;
    }
    
    //Exibe mensagem de erro para parâmetros inválidos
    private void invalidParameter(String[] list, int position, boolean doubleArg) {
        final String INCORRECT_USE = "[AVISO] Parâmetro desconhecido ou uso incorreto!";
        final String CALL_HELP = "Use [-h] para ver a lista de parâmetros disponíveis.";
        
        System.out.println(INCORRECT_USE);
        
        //Parâmetro duplo
        if(doubleArg) {
            String DOUBLE_COMMAND = "Comando: " + list[position] + " " + list[position + 1];
            System.out.println(DOUBLE_COMMAND);
        }
        //Parâmetro único
        else {
            String SOLO_COMMAND = "Comando: " + list[position];
            System.out.println(SOLO_COMMAND);
        }
        
        System.out.println(CALL_HELP);
        System.out.println();
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
        String sysLang = Locale.getDefault()
            .getDisplayLanguage(Locale.forLanguageTag("pt-br"))
            .toUpperCase();
        String sysCharset = Charset.defaultCharset()
            .displayName(Locale.ENGLISH)
            .toUpperCase();
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
