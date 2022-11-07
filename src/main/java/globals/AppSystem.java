/**
*** @author chrisGrando
*** Classe destinada a funções e variáveis globais do sistema.
**/
package globals;

import java.io.File;
import java.io.PrintStream;
import java.net.URISyntaxException;

public class AppSystem {
    public static PrintStream CONSOLE_OUTPUT = null;
    public static PrintStream ERROR_OUTPUT = null;
    
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
    
}