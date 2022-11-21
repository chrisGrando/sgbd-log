/**
*** @author chrisGrando
*** Classe destinada para gravar cada print/println em arquivos de log.
**/
package app;

import globals.AppSystem;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;

public class LogOutput {
    
    //Saída normal
    public void changeOUT(String path)
      throws Exception {
        final PrintStream stdout = System.out;
        AppSystem.CONSOLE_OUTPUT = stdout;
        
        //Diretório do arquivo de texto
        File txtLog = new File(AppSystem.getJarFolder() + path);
        
        //Cria arquivo caso não exista
        if(!txtLog.exists())
            this.createNewFile(txtLog);
        //Se o arquivo já existir, renomeie/exclua e crie um novo
        else {
            this.setAsOldFile(txtLog);
            this.createNewFile(txtLog);
        }
        
        //Gera uma nova saída
        PrintStream ps = new PrintStream(txtLog) {
            //PRINTLN
            @Override public void println() {
                stdout.println();
                super.print("\n");
            }
            @Override public void println(Object obj) {
                stdout.println(obj);
                super.print(obj);
                super.print("\n");
            }
            @Override public void println(String s) {
                stdout.println(s);
                super.print(s);
                super.print("\n");
            }
            @Override public void println(boolean b) {
                stdout.println(b);
                super.print(b);
                super.print("\n");
            }
            @Override public void println(char c) {
                stdout.println(c);
                super.print(c);
                super.print("\n");
            }
            @Override public void println(char[] cv) {
                stdout.println(cv);
                super.print(cv);
                super.print("\n");
            }
            @Override public void println(double d) {
                stdout.println(d);
                super.print(d);
                super.print("\n");
            }
            @Override public void println(float f) {
                stdout.println(f);
                super.print(f);
                super.print("\n");
            }
            @Override public void println(int i) {
                stdout.println(i);
                super.print(i);
                super.print("\n");
            }
            @Override public void println(long l) {
                stdout.println(l);
                super.print(l);
                super.print("\n");
            }
            //PRINT
            @Override public void print(Object obj) {
                stdout.print(obj);
                super.print(obj);
            }
            @Override public void print(String s) {
                stdout.print(s);
                super.print(s);
            }
            @Override public void print(boolean b) {
                stdout.print(b);
                super.print(b);
            }
            @Override public void print(char c) {
                stdout.print(c);
                super.print(c);
            }
            @Override public void print(char[] cv) {
                stdout.print(cv);
                super.print(cv);
            }
            @Override public void print(double d) {
                stdout.print(d);
                super.print(d);
            }
            @Override public void print(float f) {
                stdout.print(f);
                super.print(f);
            }
            @Override public void print(int i) {
                stdout.print(i);
                super.print(i);
            }
            @Override public void print(long l) {
                stdout.print(l);
                super.print(l);
            }
        };
        
        //Substitui a saída antiga
        System.setOut(ps);
    }
    
    //Saída de erro
    public void changeERR(String path)
      throws Exception {
        final PrintStream stderr = System.err;
        AppSystem.ERROR_OUTPUT = stderr;
        
        //Diretório do arquivo de texto
        File txtLog = new File(AppSystem.getJarFolder() + path);
        
        //Cria arquivo caso não exista
        if(!txtLog.exists())
            this.createNewFile(txtLog);
        //Se o arquivo já existir, renomeie/exclua e crie um novo
        else {
            this.setAsOldFile(txtLog);
            this.createNewFile(txtLog);
        }
        
        //Gera uma nova saída
        PrintStream ps = new PrintStream(txtLog) {
            //PRINTLN
            @Override public void println() {
                stderr.println();
                super.print("\n");
            }
            @Override public void println(Object obj) {
                stderr.println(obj);
                super.print(obj);
                super.print("\n");
            }
            @Override public void println(String s) {
                stderr.println(s);
                super.print(s);
                super.print("\n");
            }
            @Override public void println(boolean b) {
                stderr.println(b);
                super.print(b);
                super.print("\n");
            }
            @Override public void println(char c) {
                stderr.println(c);
                super.print(c);
                super.print("\n");
            }
            @Override public void println(char[] cv) {
                stderr.println(cv);
                super.print(cv);
                super.print("\n");
            }
            @Override public void println(double d) {
                stderr.println(d);
                super.print(d);
                super.print("\n");
            }
            @Override public void println(float f) {
                stderr.println(f);
                super.print(f);
                super.print("\n");
            }
            @Override public void println(int i) {
                stderr.println(i);
                super.print(i);
                super.print("\n");
            }
            @Override public void println(long l) {
                stderr.println(l);
                super.print(l);
                super.print("\n");
            }
            //PRINT
            @Override public void print(Object obj) {
                stderr.print(obj);
                super.print(obj);
            }
            @Override public void print(String s) {
                stderr.print(s);
                super.print(s);
            }
            @Override public void print(boolean b) {
                stderr.print(b);
                super.print(b);
            }
            @Override public void print(char c) {
                stderr.print(c);
                super.print(c);
            }
            @Override public void print(char[] cv) {
                stderr.print(cv);
                super.print(cv);
            }
            @Override public void print(double d) {
                stderr.print(d);
                super.print(d);
            }
            @Override public void print(float f) {
                stderr.print(f);
                super.print(f);
            }
            @Override public void print(int i) {
                stderr.print(i);
                super.print(i);
            }
            @Override public void print(long l) {
                stderr.print(l);
                super.print(l);
            }
        };
        
        //Substitui a saída antiga
        System.setErr(ps);
    }
    
    //Obtém a extensão do arquivo
    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
          .filter(f -> f.contains("."))
          .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    
    //Renomeia ou exclui arquivo antigo
    private void setAsOldFile(File file)
      throws Exception {
        //Excluir arquivo se estiver vazio
        if(file.length() == 0)
            file.delete();
        
        //O arquivo não está vazio
        else {
            //ID do arquivo antigo
            long id = 1;
            
            //Nome do arquivo (sem extensão)
            String name = file.getName().replaceFirst("[.][^.]+$", "");
            
            //Se não houver nome antes da extensão
            if(name.isBlank())
                name = "untitled";

            //Extensão do arquivo
            Optional<String> optExt = this.getExtensionByStringHandling(file.getName());
            String extension;

            //O arquivo não tem extensão
            if(optExt.isEmpty())
                extension = "";
            //O arquivo TEM extensão
            else
                extension = "." + optExt.get();
            
            //Gera um novo nome de arquivo
            String newFileName = file.getParent();
            newFileName += ("/OLD_" + name + "_" + Long.toString(id) + extension);
            File fileThatDoesNotExist = new File(newFileName);
            
            //Executa loop até que um nome não usado seja encontrado
            while(fileThatDoesNotExist.exists()) {
                id++;
                newFileName = file.getParent();
                newFileName += ("/OLD_" + name + "_" + Long.toString(id) + extension);
                fileThatDoesNotExist = new File(newFileName);
            }
            
            //Renomeia arquivo antigo
            boolean success = file.renameTo(fileThatDoesNotExist);
            
            //Ocorreu um erro!
            if(!success)
                throw new IOException("Unable to rename file...");
        }
    }
    
    //Cria um novo arquivo de log
    private void createNewFile(File file)
      throws Exception {
        //Cria diretório(s)
        File fileDir = new File(file.getParent());
        fileDir.mkdirs();
        
        //Cria arquivo
        file.createNewFile();
    }
    
}
