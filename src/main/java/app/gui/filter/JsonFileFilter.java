/** 
*** @author chrisGrando
*** Classe destinada ao filtro de extensão do arquivo JSON para abrir.
**/
package app.gui.filter;

import java.io.File;

public class JsonFileFilter
  extends javax.swing.filechooser.FileFilter {
    
    @Override
    public boolean accept(File file) {
        //Permite apenas diretórios ou arquivos com extensão ".json"
        return file.isDirectory() || file.getAbsolutePath().endsWith(".json");
    }
    
    @Override
    public String getDescription() {
        //Esta descrição será exibida na caixa de diálogo
        return "Metadado (*.json)";
    }
    
}
