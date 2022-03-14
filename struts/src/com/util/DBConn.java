package com.util;

// util 패키지 : 공통적으로 사용하는 클래스 
// 주로 여기서 dbconn 을 만듬 


import java.sql.Connection;
import java.sql.DriverManager;

public class DBConn {
		
	private static Connection conn = null;
		
	
	public static Connection getConnection() {	
		// this : 최소한의 정보 
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "suzi";
		String pwd = "a123";
		
		if(conn==null) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(url,user,pwd);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		return conn;
	}	
		public static void close() {
			if(conn==null) {
				return;
			}
			try {
				if(!conn.isClosed())
					conn.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			conn=null;
		}
	}
