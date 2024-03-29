/**
*** @author chrisGrando
*** Classe destinada ao gerenciamento do PostgreSQL.
*** Referência(s):
*** < https://www.enterprisedb.com/postgres-tutorials/how-connect-postgres-database-using-eclipse-and-netbeans >
*** < https://jdbc.postgresql.org/documentation/query/ >
*** < https://www.tutorialspoint.com/what-is-the-difference-between-execute-executequery-and-executeupdate-methods-in-jdbc >
**/
package sgbd;

import globals.PSQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;

public class PostgreSQL {
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    private String url;
    private Connection connection;
    private boolean isConnected;
    
    //Construtor
    public PostgreSQL() {
        host = PSQL.HOST;
        port = PSQL.PORT;
        database = PSQL.DATABASE;
        user = PSQL.USER;
        password = PSQL.PASSWORD;
        url = PSQL.GET_URL();
        connection = null;
        isConnected = false;
    }
    
    //Conexão básica
    public void connectToPostgres() {
        try {
            //Se já estiver conectado, encerra a conexão atual primeiro
            this.disconnectFromPostgres();
            
            //Realiza a conexão
            this.connection = DriverManager.getConnection(
                this.url,
                this.user,
                this.password
            );
            
            //Conexão realizada com sucesso
            this.isConnected = true;
            
            //Exibe informações da conexão
            String client = this.connection.getClientInfo("ApplicationName");
            System.out.println("*** " + client + " ***");
            System.out.println("Conectado em: " + this.database);
            System.out.println("Host: " + this.host);
            System.out.println("Porta: " + this.port);
            System.out.println("Usuário: " + this.user);
            System.out.println("URL: " + this.url);
            System.out.println();
        }
        catch (SQLException error) {
            String msg = "[FATAL] Não foi possível conectar-se ao PostgreSQL...";
            Logger.getGlobal().log(Level.SEVERE, msg, error);
            System.out.println(msg); //Mensagem no console normal
            this.isConnected = false;
        }
    }
    
    //Executa comandos de SQL
    public List<String[]> runQuery(String sql) {
        Statement st;
        ResultSet rs = null;
        TableBehavior tb;
        List<Object[]> objQuery;
        List<String[]> strQuery = null;
        
        try {
            //Executa query no Postgres
            st = this.connection.createStatement();
            boolean isResultSetNeeded = st.execute(sql);
            
            //Checa se houve retorno no query
            if(isResultSetNeeded) {
                //Obtém output do query
                rs = st.getResultSet();
                rs.next();
                
                //Executa leitura do output do query
                tb = new TableBehavior();
                objQuery = tb.generateListFromQuery(rs);
                strQuery = tb.convertTableToString(objQuery);

                //Exibe output do query
                System.out.println("*** QUERY ***");
                System.out.print(tb.showAllData(strQuery));
                System.out.println();
            }
            //Sem retorno
            else {
                System.out.println("*** FEITO ***");
                System.out.println(sql);
                System.out.println();
            }
            
            //Encerra query
            if(rs != null)
                rs.close();
            st.close();
        }
        catch (SQLException error) {
            String msg = "[AVISO] Ocorreu um problema ao executar o query...";
            Logger.getGlobal().log(Level.WARNING, msg, error);
            System.err.println("*** COMANDO ***");
            System.err.println(sql + "\n");
            System.out.println(msg); //Mensagem no console normal
        }
        
        //Retorna dados do query (se houverem)
        return strQuery;
    }
    
    //Encerra a conexão atual
    public void disconnectFromPostgres()
      throws SQLException {
        //Checa se está conectado
        if(this.isConnected) {
            //Mensagem da operação
            String client = this.connection.getClientInfo("ApplicationName");
            String currentURL = this.connection.getMetaData().getURL();
            System.out.println("*** " + client + " ***");
            System.out.println("Desconectado de: " + currentURL);
            System.out.println();
            
            //Desconecta-se do PostgreSQL
            this.connection.close();
            this.isConnected = false;
        }
    }
    
    //Retorna se foi estabelecida uma conexão
    public boolean isConnectedOnPostgres() {
        return this.isConnected;
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
