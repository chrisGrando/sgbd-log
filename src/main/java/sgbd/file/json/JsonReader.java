/**
*** @author chrisGrando
*** Classe destinada para leitura do arquivo JSON.
*** Referência(s):
*** < https://www.javaguides.net/p/java-jackson-json-tutorial-with-examples.html >
*** < https://www.javaguides.net/2019/04/how-to-read-write-json-using-jackson-jsonparser-and-jsongenerator.html >
**/
package sgbd.file.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonReader {
    private final List<Object[]> jsonData;
    
    //Construtor
    public JsonReader() {
        jsonData = new ArrayList<>();
    }
    
    //Abre e lê o arquivo Json
    public void read(String path) {
        try {
            JsonFactory factory = new JsonFactory();
            File file = new File(path);
            JsonParser parser = factory.createParser(file);
            JsonToken token = parser.nextToken();
            
            //Avança leitura do arquivo até encontrar o objeto "INITIAL"
            while(parser.getText() != null) {
                //Condição de parada
                if (token == JsonToken.FIELD_NAME && "INITIAL".equals(parser.getCurrentName())) {
                    //Avança para o próximo elemento após o "INITIAL" e a "{"
                    parser.nextToken();
                    token = parser.nextToken();
                    break;
                }
                
                //Próximo item...
                token = parser.nextToken();
            }
            
            //Lista com o conteúdo do objeto "INITIAL"
            List<Object> contents = new ArrayList<>();
            
            //Avança leitura do arquivo até o fim do objeto "INITIAL"
            while(token != JsonToken.END_OBJECT) {
                //Armazena conteúdo lido e limpa a lista
                if(token == JsonToken.END_ARRAY && !contents.isEmpty()) {
                    Object[] aux = this.arrayListToVector(contents);
                    this.jsonData.add(aux);
                    contents.clear();
                }
                //Nome do campo
                else if(token == JsonToken.FIELD_NAME)
                    contents.add(parser.getText());
                //Conteúdo do campo
                else if(token == JsonToken.VALUE_NUMBER_INT)
                    contents.add(parser.getIntValue());
                
                //Próximo item...
                token = parser.nextToken();
            }
            
            //Fecha arquivo Json
            parser.close();
        }
        catch (IOException error) {
            String msg = "[FATAL] Não foi possível abrir o arquivo <" + path + ">...";
            Logger.getGlobal().log(Level.SEVERE, msg, error);
            System.out.println(msg); //Mensagem no console normal
        }
    }
    
    //Converte lista de objetos para vetor
    private Object[] arrayListToVector(List<Object> list) {
        final int size = list.size();
        Object[] vector = new Object[size];
        
        //Lista (posição 'i') => Vetor [posição 'i']
        for(int i = 0; i < size; i++)
            vector[i] = list.get(i);
        
        return vector;
    }
    
    //Retorna os dados lidos do arquivo Json
    public List<Object[]> getJsonData() {
        return this.jsonData;
    }
    
}
