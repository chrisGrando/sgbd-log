/**
*** @author chrisGrando
*** Classe destinada para execução dos comandos do "arquivo de log" do SGBD.
**/
package sgbd.file.log;

import sgbd.PostgreSQL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogCommands {
    private final PostgreSQL psql;
    private final String tableName;
    private final List<String> transactionHistory;
    private final List<String[]> notYetCommitedData;
    private final List<String[]> alreadyCommitedData;
    private final List<String[]> checkpointData;
    
    //Construtor
    public LogCommands(PostgreSQL psql, String tableName) {
        this.psql = psql;
        this.tableName = tableName;
        this.transactionHistory = new ArrayList<>();
        this.notYetCommitedData = new ArrayList<>();
        this.alreadyCommitedData = new ArrayList<>();
        this.checkpointData = new ArrayList<>();
    }
    
    //Inicia transação
    public void start(String op) {
        this.transactionHistory.add(op);
        System.out.print("Start " + op);
        System.out.println(" (Nº " + this.findTransactionIndex(op) + ")");
    }
    
    //Registra transação
    public void transaction(String[] op) {
        //Checa se a transação já foi declarada
        if(this.findTransactionIndex(op[0]) > -1) {
            //Checa se o array da transação possui 5 campos
            if(op.length == 5) {
                this.notYetCommitedData.add(op);
                System.out.println(this.showTransactionData(op));
            }
            //Array possui formato incorreto
            else {
                String msg = "[AVISO] Formatação incorreta da transação <" + op[0] + ">. Ignorando...";
                System.err.println(msg);
                System.out.println(msg);
            }
        }
        //Transação ainda não foi declarada
        else {
            String msg = "[AVISO] Transação <" + op[0] + "> não foi iniciada. Ignorando...";
            System.err.println(msg);
            System.out.println(msg);
        }
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
    
    //Encontra e retorna a linha com a transação requisitada
    private String[] findTransactionArray(String id, List<String[]> list) {
        String[] transactionLine = null;
        
        //Procura pelo ID da transação na lista
        for(String[] row : list) {
            //ID encontrado
            if(row[0].equals(id)) {
                transactionLine = row;
                break;
            }
        }
        
        return transactionLine;
    }
    
    //Encontra o índice da transação no histórico
    private int findTransactionIndex(String id) {
        int index = -1;
        final int size = this.transactionHistory.size();
        
        //Procura pelo ID da transação no histórico
        for(int i = 0; i < size; i++) {
            if(this.transactionHistory.get(i).equals(id)) {
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    //Exibe dados da transação com formatação
    private String showTransactionData(String[] line) {
        String contents = "Transação " + line[0] + ":\n{\n";
        contents += "    ID da tupla: " + line[1] + ",\n";
        contents += "    Coluna: " + line[2] + ",\n";
        contents += "    Valor antigo: " + line[3] + ",\n";
        contents += "    Valor novo: " + line[4] + "\n";
        contents += "}";
        
        return contents;
    }
    
}
