/**
*** @author chrisGrando
*** Classe destinada ao armazenamento dos dados padrões do PostgreSQL.
*** Usado SOMENTE na inicialização do aplicativo.
**/
package globals;

public class PSQL {
    public static String HOST = "localhost";
    public static String PORT = "5432";
    public static String DATABASE = "postgres";
    public static String USER = "postgres";
    public static String PASSWORD = "postgres";
    
    //URL de conexão do PostgreSQL
    public static String GET_URL() {
        String url;
        
        //Monta a URL completa
        url = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;
        
        return url;
    }
    
}
