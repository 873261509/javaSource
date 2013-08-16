package pharos.eht.autoClaim.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class JDBCHelper {
	
	private static JDBCHelper instance;
	
	private JDBCHelper(){}
	
	public static JDBCHelper getInstance() {
		if(instance==null) {
			instance = new JDBCHelper();
		}
		return instance;
	}
	
	public void closeAllSQL(ResultSet rs, Statement stmt, Connection conn) {
		closeResultSet(rs);
		closePstmt(stmt);
		closeConn(conn);
	}
	
	public void closeConn(Connection conn) {
		if (conn != null) { 
            try {
				conn.close();
	            conn = null;
            } catch (SQLException e) {
				e.printStackTrace();
			} 
 
        } 
	}
	
	public void closePstmt(Statement pstmt) {
		if (pstmt != null) { 
            try {
            	pstmt.close();
            	pstmt = null;
            } catch (SQLException e) {
				e.printStackTrace();
			} 
 
        } 
	}
	
	public void closeResultSet(ResultSet rs) {
		if (rs != null) { 
            try {
            	rs.close();
            	rs = null;
            } catch (SQLException e) {
				e.printStackTrace();
			} 
 
        } 
	}
	
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(PropertiesReader.getConnURL(),PropertiesReader.getConnUserName(),PropertiesReader.getConnPassword()); 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return conn;
	}
}
