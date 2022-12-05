/**
*** @author chrisGrando
*** Classe destinada para alterar a codificação, métodos de saída de texto do
*** console/terminal e dos arquivos.
**/
package app;

import globals.AppSystem;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.logging.Logger;

public class SystemEncoding {
    
    //Altera o método e as codificações das saídas de texto
    public void setConsoleOutput(String charset)
      throws Exception {
        //Obtém codificação nativa do sistema
        Charset nativeCode = Charset.forName(System.getProperty("native.encoding"));
        String nativeName = nativeCode.displayName(Locale.ENGLISH).toLowerCase();
        
        /*
        Se a codificação nativa for a do Windows, então mantém a nativa para ser
        usada nas saídas de texto do console/terminal. O processo é feito dessa
        forma porque, se qualquer outra codificação for usada, os caracteres
        especiais não irão funcionar.
        */
        if(!nativeName.contains("windows")) {
            //Print, Println and Codificações de arquivos
            System.setOut(new PrintStream(System.out, true, charset));
            System.setErr(new PrintStream(System.err, true, charset));
            System.setProperty("stdout.encoding", charset);
            System.setProperty("file.encoding", charset);
        }
        
        //Salva cada print/println em arquivos de log
        LogOutput logOutput = new LogOutput();
        logOutput.changeOUT("/output/console.log");
        logOutput.changeERR("/output/error.log");
    }
    
    //Altera o método e a codificação de texto do Logger global
    public void setLoggerOutput(String charset)
      throws Exception {
        final Logger myLogger = Logger.getGlobal();
        
        //Codificação dos logs
        String[] charsets = {
            null,   //Isto é para a saída do CONSOLE
            charset //Isto é para a saída de ARQUIVO
        };
        
        //Modifica o Logger...
        LoggerOutput loggerOutput = new LoggerOutput();
        loggerOutput.replaceRootOutput(myLogger, AppSystem.ERROR_OUTPUT);
        loggerOutput.addOutput(myLogger, System.err);
        loggerOutput.setLoggerEncoding(myLogger, charsets);
    }
    
}
