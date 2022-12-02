/**
*** @author chrisGrando
*** Classe destinada para execução dos comandos do "arquivo de log" do SGBD.
**/
package sgbd.file.log;

import sgbd.PostgreSQL;
import java.util.Arrays;

public class LogCommands {
    private final PostgreSQL psql;
    
    //Construtor
    public LogCommands(PostgreSQL psql) {
        this.psql = psql;
    }
    
    //Inicia transação
    public void start(String op) {
        System.out.println("Start " + op);
    }
    
    //Executa transação
    public void transaction(String[] op) {
        System.out.println(Arrays.toString(op));
    }
    
    //Comita transação
    public void commit(String op) {
        System.out.println("Commit " + op);
    }
    
    //Registra checkpoint da transação
    public void checkpoint(String[] op) {
        System.out.println(Arrays.toString(op));
    }
    
    //Simula erro no SGBD
    public void crash() {
        System.out.println("CRASH!");
    }
    
}
