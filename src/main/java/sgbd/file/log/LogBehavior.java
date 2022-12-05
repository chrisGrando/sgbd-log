/**
*** @author chrisGrando
*** Classe destinada para interpretação e controle do "arquivo de log" do SGBD.
**/
package sgbd.file.log;

import sgbd.PostgreSQL;
import java.util.ArrayList;
import java.util.Arrays;
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
    }
    
    //Interpreta os comandos do arquivo de log
    public void runLogInterpreter(PostgreSQL psql) {
        final LogCommands lc = new LogCommands(psql);
        
        System.out.println("*** SGBD LOG ***");
        
        //Percorre todas as linhas do arquivo de log
        for(String[] line : this.logList) {
            String cmd = line[0];
            
            //Verifica o nome do comando
            switch (cmd) {
                //Inicia transação
                case "start":
                    lc.start(line[1]);
                    break;
                //Comita transação
                case "commit":
                    lc.commit(line[1]);
                    break;
                //Registra checkpoint da transação
                case "CKPT":
                    lc.checkpoint(line);
                    break;
                //Simula erro no SGBD
                case "crash":
                    lc.crash();
                    break;
                //Nenhuma das opções acima
                default:
                    //Executa transação
                    if(this.isTransaction(cmd) && line.length > 1)
                        lc.transaction(line);
                    //Comando inválido
                    else {
                        System.err.println("[AVISO] Comando não reconhecido:");
                        System.err.println(Arrays.toString(line));
                        System.err.println();
                    }
            }
        }
        
        System.out.println();
    }
    
    //Checa se comando é uma transação
    private boolean isTransaction(String cmd) {
        int check = 0;
        
        //Vasculha todos os caracteres do comando
        for(char c : cmd.toCharArray()) {
            //Pula caractere se for espaço em branco
            if(Character.isWhitespace(c))
                continue;
            
            //Primeiro caractere (letra "T")
            if(Character.compare(c, 'T') == 0 && check == 0)
                check++;
            //Segundo caractere e em diante (número)
            else if(Character.isDigit(c) && check >= 1)
                check++;
            //Não é transação
            else {
                check = -1;
                break;
            }
        }
        
        //Não é transação
        if(check < 2)
            return false;
        
        //É transação
        return true;
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
    
    //Converte lista de Strings para vetor
    private String[] arrayListToVector(List<String> list) {
        final int size = list.size();
        String[] vector = new String[size];
        
        //Lista (posição 'i') => Vetor [posição 'i']
        for(int i = 0; i < size; i++) {
            vector[i] = list.get(i);
        }
        
        return vector;
    }
    
}
