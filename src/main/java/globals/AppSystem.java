/**
*** @author chrisGrando
*** Classe destinada a funções e variáveis globais do sistema.
**/
package globals;

import app.Main;
import java.io.File;
import java.io.PrintStream;
import java.net.URISyntaxException;

public class AppSystem {
    //Consoles do software
    public static PrintStream CONSOLE_OUTPUT = null;
    public static PrintStream ERROR_OUTPUT = null;
    
    //Arquivo de log do SGBD
    public static String SGBD_LOG = null;
    
    //Nome da tabela do SGBD
    public static String TABLE_NAME = "homework";
    
    //Arquivo JSON da tabela do SGBD
    public static String JSON_TABLE = null;
    
    //Lista de parâmetros para linha de comando
    public static String getHelpText() {
        String help = "* Comandos:\n\n";
        help += "java -jar sgbd-log.jar [-v | -h] -host [NOME DO HOST] -port [NÚMERO DA PORTA]\n";
        help += "-database [NOME DO BANCO] -user [USUÁRIO] -password [SENHA] -log [SGBD LOG]\n";
        help += "-table [NOME DA TABELA] -json [TABELA JSON]\n\n";
        
        help += "* Exemplo:\n\n";
        help += "java -jar SGBD-log.jar -host localhost -port 5432 -database postgres\n\n";
        
        help += "* Lista de parâmetros:\n\n";
        help += "-v: Exibe a versão do aplicativo e sai\n";
        help += "-h: Exibe esta mensagem e sai\n";
        help += "-host [...]: Nome do host (padrão: localhost)\n";
        help += "-port [...]: Número da porta (padrão: 5432)\n";
        help += "-database [...]: Nome do banco de dados (padrão: postgres)\n";
        help += "-user [...]: Nome de usuário (padrão: postgres)\n";
        help += "-password [...]: Senha do usuário (padrão: postgres)\n";
        help += "-log [...]: Diretório do arquivo de log do SGBD\n";
        help += "-table [...]: Nome da tabela do SGBD (padrão: homework)\n";
        help += "-json [...]: Diretório do arquivo JSON da tabela do SGBD\n";
        
        return help;
    }
    
    //Obtém o diretório do aplicativo
    public static String getJarFolder()
      throws URISyntaxException {
        File folder;
        
        //Diretório da classe AppSystem
        File classFileDir = new File(
          AppSystem.class.getProtectionDomain().getCodeSource().getLocation().toURI()
        );
        
        /*
        Se "classFileDir" não terminar com ".jar", então esta classe não está
        sendo executada a partir de um arquivo JAR, mas (provavelmente) na IDE.
        Nesse caso, o diretório atual será usado.
        */
        if(!classFileDir.getAbsolutePath().endsWith(".jar"))
            folder = new File(System.getProperty("user.dir"));
        /*
        Mas se a classe AppSystem *estiver* sendo executada a partir de um
        arquivo JAR, então o caminho da pasta na qual o arquivo JAR está
        localizado será usado.
        */
        else
            folder = new File(classFileDir.getParent());
        
        return folder.getAbsolutePath();
    }
    
    /*
    Propriedades do aplicativo obtidos pelo arquivo "pom". Referência:
    < https://igorski.co/how-to-get-the-pom-properties-at-runtime/ >
    */
    
    //Nome do aplicativo
    public static String getAppName() {
        Package mainPackage = Main.class.getPackage();
        String name = mainPackage.getImplementationTitle();
        
        //Se for nulo, então é uma build em desenvolvimento
        if(name == null)
            name = "[DEBUG MODE] SGBD-LOG";
        
        return name;
    }
    
    //Versão do aplicativo
    public static String getAppVersion() {
        Package mainPackage = Main.class.getPackage();
        String version = mainPackage.getImplementationVersion();
        
        //Se for nulo, então é uma build em desenvolvimento
        if(version == null)
            version = "DEVELOPMENT BUILD";
        
        return version;
    }
    
}
