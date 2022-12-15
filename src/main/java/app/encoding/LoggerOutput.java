/**
*** @author chrisGrando
*** Classe destinada a substituir os métodos de saída do Logger.
**/
package app.encoding;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.util.logging.Handler;
import java.io.PrintStream;

public class LoggerOutput {
    
    //Substitui a saída original por uma personalizada
    public void replaceRootOutput(Logger logger, PrintStream customOutput) {
        //Remove handler(s) atual(is)
        logger.setUseParentHandlers(false);
        Handler[] handlers = logger.getHandlers();
        for(Handler h : handlers) {
            logger.removeHandler(h);
        }
        
        //Adiciona um novo handler com uma saída personalizada
        this.addOutput(logger, customOutput);
    }
    
    //Adiciona uma saída personalizada
    public void addOutput(Logger logger, PrintStream customOutput) {
        SimpleFormatter fmt = new SimpleFormatter();
        StreamHandler sh = new StreamHandler(customOutput, fmt);
        logger.addHandler(sh);
    }
    
    //Altera a codificação de texto do Logger
    public void setLoggerEncoding(Logger logger, String[] charsets)
      throws Exception {
        Handler[] currentHandlers = logger.getHandlers();
        int size = currentHandlers.length;
        
        /*
        Se o array de codificações for menor que o de handlers, então o
        comprimento do array de codificações será usado para a condição
        de parada do loop.
        */
        if(charsets.length < size)
            size = charsets.length;
        
        //Modifica todos os handlers
        for(int i = 0; i < size; i++) {
            //Handler antigo
            Handler h = currentHandlers[i];
            logger.removeHandler(h);
            
            //Handler novo
            h.setEncoding(charsets[i]);
            logger.addHandler(h);
        }
    }
    
}
