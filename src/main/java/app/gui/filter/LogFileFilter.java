/** 
*** @author chrisGrando
*** Classe destinada ao filtro de extensão do arquivo de LOG do SGBD para abrir.
**/
package app.gui.filter;

import java.io.File;

public class LogFileFilter
  extends javax.swing.filechooser.FileFilter {
    
    @Override
    public boolean accept(File file) {
        //Permite apenas diretórios ou arquivos com extensão ".txt"
        return file.isDirectory() || file.getAbsolutePath().endsWith(".txt");
    }
    
    @Override
    public String getDescription() {
        //Esta descrição será exibida na caixa de diálogo
        return "LOG do SGBD (*.txt)";
    }
    
}
