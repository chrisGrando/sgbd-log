/**
*** @author chrisGrando
*** Classe destinada ao gerenciamento geral do arquivo JSON.
**/
package sgbd.file.json;

import java.util.ArrayList;
import java.util.List;
import sgbd.PostgreSQL;
import sgbd.TableBehavior;

public class JsonBehavior {
    private List<Object[]> fileContents = null;
    
    //Abre arquivo Json
    public void openJsonFile(String path) {
        JsonReader jr = new JsonReader();
        jr.read(path);
        this.fileContents = jr.getJsonData();
    }
    
    //Exibe o conteúdo lido do arquivo Json
    public void showContents() {
        TableBehavior tb = new TableBehavior();
        List<String[]> ls = tb.convertTableToString(this.fileContents);
        
        System.out.println("*** JSON ***");
        System.out.println(tb.showAllData(ls));
        System.out.println();
    }
    
    //Insere os dados do arquivo JSON no SGBD
    public void insertDataIntoPostgres(PostgreSQL psql, String tableName) {
        List<String> columns = new ArrayList<>();
        List<String[]> dataInOrder = new ArrayList<>();
        String createTable;
        String insertIntoTable;
        
        //Obtém nome das colunas da tabela
        for(Object[] row : this.fileContents) {
            String cell = row[0].toString();
            columns.add(cell);
        }
        
        //Inicia SQL da criação da tabela
        createTable = "create table " + tableName + "(\n";
        createTable += "id serial primary key";
        
        //Inclui colunas na tabela
        for(String item: columns) {
            createTable += ",\n" + item + " integer";
        }
        
        //Encerra SQL da criação da tabela
        createTable += "\n);";
        
        //Deleta tabela caso já exista
        psql.runQuery("drop table if exists " + tableName + ";");
        
        //Cria tabela no SGBD
        psql.runQuery(createTable);
        
        //Obtém os valores do arquivo em ordem
        for(int i = 0; i < this.fileContents.size(); i++) {
            final int size = this.fileContents.get(i).length;
            String[] line = new String[size - 1];
            
            //Linhas viram colunas e colunas viram linhas
            for(int j = 0; j < line.length; j++) {
                line[j] = this.fileContents.get(j)[i+1].toString();
            }
            
            //Adiciona linha em ordem
            dataInOrder.add(line);
        }
        
        //Inicia SQL da inserção de dados na tabela
        insertIntoTable = "insert into " + tableName + "(";
        
        //Colunas da tabela
        for(int i = 0; i < columns.size(); i++) {
            insertIntoTable += columns.get(i);
            
            //Se não for a última coluna, insere a vírgula
            if(i < (columns.size() - 1))
                insertIntoTable += ", ";
        }
        
        //Valores a serem inseridos
        insertIntoTable += ") values\n";
        
        //Insere os valores em ordem
        for(int i = 0; i < dataInOrder.size(); i++) {
            insertIntoTable += "(";
            
            //Colunas da linha atual
            final int size = dataInOrder.get(i).length;
            for(int j = 0; j < size; j++) {
                insertIntoTable += dataInOrder.get(i)[j];
                
                //Se não for a última coluna, insere a vírgula
                if(j < (size - 1))
                    insertIntoTable += ",";
            }
            
            insertIntoTable += ")";
            
            //Se não for o último item, insere a vírgula
            if(i < (dataInOrder.size() - 1))
                insertIntoTable += ",\n";
        }
        
        //Após o último item, encerra SQL
        insertIntoTable += ";";
        
        //Insere os dados na tabela do SGBD
        psql.runQuery(insertIntoTable);
    }
    
}
