/**
*** @author chrisGrando
*** Classe destinada para execução dos comandos do "arquivo de log" do SGBD.
**/
package sgbd.file.log;

import sgbd.PostgreSQL;
import java.util.ArrayList;
import java.util.List;

public class LogCommands {
    private final PostgreSQL psql;
    private final String tableName;
    private final List<String> activeTransactions;
    private final List<String[]> transactionsData;
    private final List<String> checkpointFlag;
    
    //Construtor
    public LogCommands(PostgreSQL psql, String tableName) {
        this.psql = psql;
        this.tableName = tableName;
        this.activeTransactions = new ArrayList<>();
        this.transactionsData = new ArrayList<>();
        this.checkpointFlag = new ArrayList<>();
    }
    
    //Inicia transação
    public void start(String op) {
        this.activeTransactions.add(op);
        System.out.print("Iniciar <" + op + ">");
        System.out.println(" (Posição Nº " + this.activeTransactions.indexOf(op) + ")\n");
    }
    
    //Registra transação
    public void transaction(String[] op) {
        //Checa se a transação está ativa
        if(this.activeTransactions.indexOf(op[0]) > -1) {
            //Checa se o array da transação possui 5 campos
            if(op.length == 5) {
                this.transactionsData.add(op);
                System.out.println(this.showTransactionData(op) + "\n");
            }
            //Array possui formato incorreto
            else {
                String msg = "[AVISO] Formatação incorreta da transação <" + op[0] + ">. Ignorando...\n";
                System.err.println(msg);
            }
        }
        //Transação não está ativa
        else {
            String msg = "[AVISO] Transação <" + op[0] + "> não está ativa. Ignorando...\n";
            System.err.println(msg);
        }
    }
    
    //Comita transação
    public void commit(String op) {
        final int activeIndex = this.activeTransactions.indexOf(op);
        
        //Obtém todas as transações
        List<String[]> aux = new ArrayList<>();
        aux.addAll(this.transactionsData);
        
        //Checa se a transação está ativa
        if(activeIndex > -1) {
            //Remove da lista de transações ativas
            final String id = this.activeTransactions.remove(activeIndex);
            
            //Roda enquando houverem dados para registrar
            while(this.findTransactionArray(id, aux) != null) {
                //Obtém dados da transação
                int dataIndex = aux.indexOf(this.findTransactionArray(id, aux));
                String[] data = aux.remove(dataIndex);

                //Salva os dados no Postgres
                System.out.println("Comitar <" + id + ">");
                this.writeIntoPostgres(data);
            }
        }
        //Transação não está ativa
        else {
            String msg = "[AVISO] Transação <" + op + "> não está ativa. Ignorando...\n";
            System.err.println(msg);
        }
    }
    
    //Registra checkpoint das transações
    public void checkpoint(String op) {
        String[] list = this.generateCheckpointList(op);
        
        //Processa todos os itens da lista
        for(String item : list) {
            final int index = this.activeTransactions.indexOf(item);
            
            //Checa se a transação está ativa
            if(index > -1) {
                //Marca como checkpoint
                this.checkpointFlag.add(item);
                System.out.println("CHECKPOINT => " + item);
            }
            //Transação não está ativa
            else {
                String msg = "[AVISO] Transação <" + item + "> não está ativa. Ignorando...";
                System.err.println(msg);
            }
        }
        
        System.out.println();
    }
    
    //Simula erro no SGBD
    public void crash() {
        System.out.println("*** CRASH! ***");
        
        //Refaz as transações que foram marcadas pelo checkpoint e foram comitadas
        for(int i = 0; i < this.transactionsData.size(); i++) {
            String[] row = this.transactionsData.get(i);
            
            //Checa se foi marcada pelo checkpoint
            if(this.checkpointFlag.contains(row[0])) {
                //Checa se a transação foi comitada
                if(this.activeTransactions.indexOf(row[0]) < 0) {
                    System.out.println("Transação <" + row[0] + "> sofreu REDO\n");
                    this.writeIntoPostgres(row);
                }
                //Transação não foi comitada
                else {
                    System.out.println("Transação <" + row[0] + "> não sofreu REDO\n");
                }
            }
            //Transação não foi marcada pelo checkpoint
            else {
                System.out.println("Transação <" + row[0] + "> não sofreu REDO\n");
            }
        }
    }
    
    //Gera lista de transações para marcar no checkpoint
    private String[] generateCheckpointList(String txt) {
        String[] list;
        String item = "T0"; //T0 = Inválido
        List<String> aux = new ArrayList<>();
        
        //Vasculha todos os caracteres
        for(char c : txt.toCharArray()) {
            //Se for um '('
            if(Character.compare(c, '(') == 0) {
                item = "";
                continue;
            }
            
            //Se for um ')'
            if(Character.compare(c, ')') == 0) {
                //Checa se "item" não está vazio
                if(!item.isBlank()) {
                    aux.add(item);
                    item = "T0";
                }
                continue;
            }
            
            //Se for uma ','
            if(Character.compare(c, ',') == 0) {
                //Checa se "item" não está vazio
                if(!item.isBlank()) { 
                    aux.add(item);
                    item = "";
                }
                continue;
            }
            
            //Adiciona caractere como item
            item += Character.toString(c);
        }
        
        //Transforma array em vetor
        final int size = aux.size();
        list = new String[size];
        
        //Insere todos os itens no vetor
        for(int i = 0; i < size; i++) {
            list[i] = aux.get(i);
        }
        
        return list;
    }
    
    //Registra dados no Postgres
    private void writeIntoPostgres(String[] data) {
        final String ROW = data[1];
        final String CELL = data[2];
        final String VALUE = data[4];
        String updateData;
        
        //SQL de atualização
        updateData = "update " + this.tableName + "\n";
        updateData += "set " + CELL + " = " + VALUE + "\n";
        updateData += "where id = " + ROW + ";";
        
        //Executa atualização no SGBD
        this.psql.runQuery(updateData);
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
    
    //Exibe dados da transação com formatação
    private String showTransactionData(String[] line) {
        String contents = "Transação <" + line[0] + ">\n{\n";
        contents += "    ID da tupla: " + line[1] + ",\n";
        contents += "    Coluna: " + line[2] + ",\n";
        contents += "    Valor antigo: " + line[3] + ",\n";
        contents += "    Valor novo: " + line[4] + "\n";
        contents += "}";
        
        return contents;
    }
    
}
