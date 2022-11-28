/**
*** @author chrisGrando
*** Classe destinada para interpretação e controle do "arquivo de log" do SGBD.
**/
package sgdb.file.log;

import java.util.ArrayList;
import java.util.List;

public class LogBehavior {
    private List<String[]> logList = null;
    
    //Abre arquivo de log
    public void openFile(String path) {
        //Lê o arquivo de texto
        LogReader lr = new LogReader();
        lr.read(path);
        
        //Converte texto em lista
        String txt = lr.getFileText();
        this.logList = this.makeAnList(txt);
        
        /*for(String[] row : this.logList) {
            for(String item : row) {
                System.out.print(item + " | ");
            }
            System.out.println();
        }*/
    }
    
    //Monta uma lista com os comandos do arquivo lido
    private List<String[]> makeAnList(String src) {
        List<String[]> list = new ArrayList<>();
        List<String> line = new ArrayList<>();
        String element = "";
        boolean isParenthesisOpen = false;
        
        //Percorre todos os caracteres do texto
        for(char c : src.toCharArray()) {
            //Limpa a lista e inicia nova linha
            if(Character.compare(c, '<') == 0) {
                if(!line.isEmpty())
                    line.clear();
                continue;
            }
            
            //Encerra a linha e adiciona na lista
            if(Character.compare(c, '>') == 0) {
                //Adiciona último elemento na linha (se houver)
                if(!element.isBlank()) {
                    line.add(element);
                    element = "";
                }
                
                //Adiciona linha na lista (se não estiver vazia)
                if(!line.isEmpty()) {
                    String[] newLine = this.arrayListToVector(line);
                    list.add(newLine);
                }
                continue;
            }
            
            //Abre parênteses na linha
            if(Character.compare(c, '(') == 0)
                isParenthesisOpen = true;
            
            //Fecha parênteses na linha
            if(Character.compare(c, ')') == 0)
                isParenthesisOpen = false;
            
            //Não procura separadores se parênteses abertos
            if(!isParenthesisOpen) {
                //Adiciona elemento atual na lista se encontrar separador
                if(Character.compare(c, ',') == 0 || Character.isWhitespace(c)) {
                    if(!element.isBlank()) {
                        line.add(element);
                        element = "";
                    }
                    continue;
                }
            }
            
            //Adiciona caractere atual no elemento atual
            element += Character.toString(c);
        }
        
        return list;
    }
    
    //Converte lista para vetor
    private String[] arrayListToVector(List<String> list) {
        final int size = list.size();
        String[] vector = new String[size];
        
        //Faz a conversão
        for(int i = 0; i < size; i++) {
            vector[i] = list.get(i);
        }
        
        return vector;
    }
    
}
