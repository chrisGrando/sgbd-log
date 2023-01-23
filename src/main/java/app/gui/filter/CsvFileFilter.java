/** 
*** @author chrisGrando
*** Classe destinada ao filtro de extensão do arquivo CSV para salvar.
**/
package app.gui.filter;

import java.io.File;

public class CsvFileFilter
  extends javax.swing.filechooser.FileFilter {
    
    @Override
    public boolean accept(File file) {
        //Permite apenas diretórios ou arquivos com extensão ".csv"
        return file.isDirectory() || file.getAbsolutePath().endsWith(".csv");
    }
    
    @Override
    public String getDescription() {
        //Esta descrição será exibida na caixa de diálogo
        return "Tabela CSV (*.csv)";
    }
    
}
