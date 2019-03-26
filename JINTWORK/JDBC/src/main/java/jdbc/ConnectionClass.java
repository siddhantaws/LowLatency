package jdbc;

import java.sql.*;
import java.io.*;
import java.text.*;

public class ConnectionClass
{
	public void initialize() throws SQLException,ClassNotFoundException ,Exception{
		try{
			Class.forName("com.mysql.jdbc.Driver");

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","tiger");
		}catch(SQLException sqle){System.out.println("HI1");throw new Exception("JustTesting1");}
		finally{
			
			try{
				System.out.println("HI2");
				Class.forName("com.mysql.jdbc.Driver");
			}catch(ClassNotFoundException e){throw new Exception("JustTesting");}
			finally{System.out.println("HI3");}
			

		}
			

		
	} 



};  

