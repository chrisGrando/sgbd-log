/**
*** @author chrisGrando
*** Classe destinada ao gerenciamento geral do arquivo JSON.
**/
package sgbd.file.json;

import java.util.List;
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
    
}
