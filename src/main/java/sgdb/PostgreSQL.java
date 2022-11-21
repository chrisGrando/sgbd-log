/**
*** @author chrisGrando
*** Classe destinada ao gerenciamento do PostgreSQL.
*** Referência(s):
*** <https://www.enterprisedb.com/postgres-tutorials/how-connect-postgres-database-using-eclipse-and-netbeans>
**/
package sgdb;

import globals.PSQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class PostgreSQL {
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    private String url;
    private Connection connection;
    
    //Construtor
    public PostgreSQL() {
        host = PSQL.HOST;
        port = PSQL.PORT;
        database = PSQL.DATABASE;
        user = PSQL.USER;
        password = PSQL.PASSWORD;
        url = PSQL.GET_URL();
        connection = null;
    }
    
    //Conexão básica
    public void connectToPostgres() {
        try {
            //Realiza a conexão
            this.connection = DriverManager.getConnection(
                this.url,
                this.user,
                this.password
            );
            
            //Exibe informações da conexão
            String client = this.connection.getClientInfo("ApplicationName");
            System.out.println("\n*** " + client + " ***");
            System.out.println("Conectado em: " + this.database);
            System.out.println("Host: " + this.host);
            System.out.println("Porta: " + this.port);
            System.out.println("Usuário: " + this.user);
        }
        catch (SQLException error) {
            String msg = "[ERRO] Não foi possível conectar-se ao PostgreSQL...";
            Logger.getGlobal().log(Level.SEVERE, msg, error);
        }
    }
    
    //Gera nova URL de conexão
    public void generateURL() {
        this.url = "jdbc:postgresql://"
            + this.host + ":"
            + this.port + "/"
            + this.database;
    }
    
    //Configura o host e o ID da porta
    public void setHostWithPort(String host, String port) {
        this.host = host;
        this.port = port;
    }
    
    //Configura o nome do banco de dados
    public void setDatabase(String database) {
        this.database = database;
    }
    
    //Configura o usuário e a senha
    public void setUserWithPassword(String user, String password) {
        this.user = user;
        this.password = password;
    }
    
}
