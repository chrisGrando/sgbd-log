/*
 * This is free and unencumbered software released into the public domain.
 *
 * Copyright 2022 @chrisGrando.
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 *
 */

/**
*** @author chrisGrando
*** Classe destinada somente para inicialização do sistema.
*** Lógica do software fica localizada em AppStates.
**/
package app;

import globals.AppSystem;
import java.io.PrintStream;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args)
      throws Exception {
        //Força saídas de texto para arquivos de log e codificação como UTF-8
        globalOutput("UTF8");
        
        //Modifica Logger global
        globalLogger("UTF8");
        
        //Executa aplicativo
        System.out.println("Hello World!");
    }
    
    //Altera o método e as codificações das saídas de texto
    private static void globalOutput(String charset)
      throws Exception {
        //Print, Println and Codificações de arquivos
        System.setOut(new PrintStream(System.out, true, charset));
        System.setErr(new PrintStream(System.err, true, charset));
        System.setProperty("file.encoding", charset);
        
        //Salva cada print/println em arquivos de log
        LogOutput logOutput = new LogOutput();
        logOutput.changeOUT("/output/console.log");
        logOutput.changeERR("/output/error.log");
    }
    
    //Altera o método e a codificação de texto do Logger global
    private static void globalLogger(String charset)
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
