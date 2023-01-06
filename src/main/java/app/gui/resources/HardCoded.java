/** 
*** @author chrisGrando
*** Classe destinada para carregar arquivos alocados dentro do executável.
**/
package app.gui.resources;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HardCoded {
    
    //Obtém ícone do aplicativo
    public Image getIconFile(String path) {
        Image icon;
        
        //Acessa arquivo no caminho especificado (relativo à classe)
        icon = Toolkit.getDefaultToolkit().getImage(
            getClass().getResource(path)
        );
        
        return icon;
    }
    
    //Obtém fonte em formato tff
    public Font getTTF(String path) {
        Font fontFile = null;
        
        //Carrega o arquivo da fonte no caminho especificado (relativo à classe)
        try {
            fontFile = Font.createFont(
                Font.TRUETYPE_FONT,
                this.getClass().getResourceAsStream(path)
            );
        }
        catch (IOException | FontFormatException error) {
            String msg = "[AVISO] Não foi possível carregar a fonte em <" + path + ">...";
            Logger.getGlobal().log(Level.WARNING, msg, error);
            System.out.println(msg);
        }
        
        return fontFile;
    }
    
}
