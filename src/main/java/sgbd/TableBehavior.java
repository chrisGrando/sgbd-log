/**
*** @author chrisGrando
*** Classe destinada para operações com tabelas obtidas pelo PostgreSQL.
**/
package sgbd;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableBehavior {
    
    //Gera array de objetos do query
    public List<Object[]> generateListFromQuery(ResultSet query) 
      throws SQLException {
        List<Object[]> newObjectList = new ArrayList<>();
        ResultSetMetaData rsmd = query.getMetaData();
        final int size = rsmd.getColumnCount();
        Object labels[] = new Object[size];
        
        //Obtém nome das colunas
        for(int i = 0; i < size; i++) {
            int column = i + 1;
            labels[i] = rsmd.getColumnName(column);
        }
        
        //Adiciona os rótulos na lista
        newObjectList.add(labels);
        
        //Percorre cada linha
        do {
            Object row[] = new Object[size];
            
            //Vasculha cada coluna
            for(int i = 0; i < size; i++) {
                int column = i + 1;
                row[i] = query.getObject(column);
            }
            
            //Adiciona linha na lista
            newObjectList.add(row);
            
        } while(query.next()); //Checa se há mais linhas
        
        return newObjectList;
    }
    
    //Converte tabela de objetos em tabela de Strings
    public List<String[]> convertTableToString(List<Object[]> srcTable) {
        List<String[]> newTable = new ArrayList<>();
        
        //Percorre todas as linhas
        for(Object[] row : srcTable) {
            int size = row.length;
            String[] fullLine = new String[size];
            
            //Percorre todas as colunas
            for(int i = 0; i < size; i++) {
                //Converte coluna em String (se não estiver vazia)
                if(row[i] != null)
                    fullLine[i] = row[i].toString();
                else
                    fullLine[i] = "";
            }
            
            //Adiciona linha convertida em String
            newTable.add(fullLine);
        }
        
        return newTable;
    }
    
    //Exibe todo o conteúdo da tabela (com formatação de espaçamento)
    public String showAllData(List<String[]> srcTable) {
        String table = "";
        int maxSize = 0;
        
        //Procura pela cédula de maior tamanho
        for(String[] row : srcTable) {
            for(String cell : row) {
                if(cell.length() > maxSize)
                    maxSize = cell.length();
            }
        }
        
        //Checa se o tamanho da maior cédula é ímpar
        if(maxSize % 2 != 0)
            maxSize++; //Transforma em par
        
        //Monta a tabela
        for (String[] row : srcTable) {
            table = table + "|";
            
            //Monta a linha
            for (String cell : row) {
                //Quantidade de espaços antes e depois da cédula
                int evenCell = cell.length() - 1;
                if(evenCell % 2 != 0)
                    evenCell++;
                int spaces = (maxSize - evenCell) / 2;
                
                //String de espaço extra (caso tamanho da cédula seja par)
                String evenExtraSpace = "";
                if(cell.length() % 2 == 0)
                    evenExtraSpace = " ";
                
                //String de espaços em branco
                String cellVoid = "";
                for(int i = 0; i < spaces; i++)
                    cellVoid += " ";
                
                //Monta a cédula
                String newCell = cellVoid + cell + cellVoid + evenExtraSpace;
                table += newCell + "|";
            }
            
            //Nova linha
            table = table + "\n";
        }
        
        //Retorna tabela completa
        return table;
    }
    
}
