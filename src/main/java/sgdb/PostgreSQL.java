/**
*** @author chrisGrando
*** Classe destinada ao gerenciamento do PostgreSQL.
*** Referência(s):
*** < https://www.enterprisedb.com/postgres-tutorials/how-connect-postgres-database-using-eclipse-and-netbeans >
*** < https://jdbc.postgresql.org/documentation/query/ >
**/
package sgdb;

import globals.PSQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
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
            System.out.println("*** " + client + " ***");
            System.out.println("Conectado em: " + this.database);
            System.out.println("Host: " + this.host);
            System.out.println("Porta: " + this.port);
            System.out.println("Usuário: " + this.user);
            System.out.println();
        }
        catch (SQLException error) {
            String msg = "[FATAL] Não foi possível conectar-se ao PostgreSQL...";
            Logger.getGlobal().log(Level.SEVERE, msg, error);
            System.out.println(msg); //Mensagem no console normal
        }
    }
    
    //Executa comandos de SQL (sem armazenar)
    public void runQuery(String sql) {
        TableBehavior tb;
        Statement st;
        ResultSet rs;
        
        try {
            //Executa query no Postgres
            st = this.connection.createStatement();
            rs = st.executeQuery(sql);
            
            //Obtém output do query
            tb = new TableBehavior();
            List<Object[]> objQuery = this.generateListFromQuery(rs);
            List<String[]> strQuery = tb.convertTableToString(objQuery);
            
            //Exibe output do query
            System.out.println("*** QUERY ***");
            System.out.println(tb.showAll(strQuery));
            System.out.println();
            
            //Encerra query
            rs.close();
            st.close();
        }
        catch (SQLException error) {
            String msg = "[AVISO] Ocorreu um problema ao executar o query...";
            Logger.getGlobal().log(Level.WARNING, msg, error);
            System.err.println("*** COMANDO ***");
            System.err.println(sql);
            System.out.println(msg); //Mensagem no console normal
        }
    }
    
    //Executa query e armazena saída em array de objetos
    public List<Object[]> getDataFromQuery(String sql) {
        List<Object[]> objList = null;
        Statement st;
        ResultSet rs;
        
        try {
            //Executa query no Postgres
            st = this.connection.createStatement();
            rs = st.executeQuery(sql);
            
            //Armazena resultados
            objList = this.generateListFromQuery(rs);
            
            //Encerra query
            rs.close();
            st.close();
        }
        catch (SQLException error) {
            String msg = "[AVISO] Não foi possível armazenar os dados...";
            Logger.getGlobal().log(Level.WARNING, msg, error);
            System.err.println("*** COMANDO ***");
            System.err.println(sql);
            System.out.println(msg); //Mensagem no console normal
        }
        
        return objList;
    }
    
    //Gera array de objetos do query
    private List<Object[]> generateListFromQuery(ResultSet query) 
      throws SQLException {
        List<Object[]> newObjectList = new ArrayList<>();
        ResultSetMetaData rsmd = query.getMetaData();
        final int size = rsmd.getColumnCount();
        Object labels[] = new Object[size];
        
        //Obtém nome das colunas
        for(int i = 0; i < size; i++) {
            int column = i + 1;
            labels[i] = rsmd.getColumnName(column);
        }
        //Adiciona os rótulos na lista
        newObjectList.add(labels);
        
        //Vasculha cada linha
        while(query.next()) {
            Object row[] = new Object[size];
            
            //Vasculha cada coluna
            for(int i = 0; i < size; i++) {
                int column = i + 1;
                row[i] = query.getObject(column);
            }
            
            //Adiciona linha na lista
            newObjectList.add(row);
        }
        
        return newObjectList;
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
