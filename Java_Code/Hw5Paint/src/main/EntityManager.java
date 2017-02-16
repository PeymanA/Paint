package main;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EntityManager {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/paint";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "";
	static Connection conn;
	static Statement stmt;
	static ResultSet rs;
	private static int userID;

	// check username and password
	@SuppressWarnings("finally")
	public static User checkUser(String userName, String passWord) {

		User temp = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt = conn.createStatement();
			String sql;
			sql = "select * from user where username='" + userName
					+ "' and password='" + passWord + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String uName = rs.getString("username");
				String pass = rs.getString("password");
				userID = rs.getInt("id");
				temp = new User(userID, uName, pass);
			}

			stmt.close();
			conn.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}

			return temp;
		}

	}

	// store shapes and colors into database
	public static void addIntoDatabase(int x, int y, int height, int weight,
			int r, int g, int b, int type) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt = conn.createStatement();
			String sql = "insert into shapes (user_id,x,y,height,weight,r,g,b,type) values(" + userID 
					+ "," + x + "," + y + "," + height + "," + weight + "," + r + "," + g
					+ "," + b + "," + type + ")";
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

	// retrive shapes with their colors from database
	public static void readFromDatabase() {

		Shape shape = null;
		Color color;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt = conn.createStatement();
			String sql;
			sql = "select * from shapes where user_id='" + userID + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {

				int x=rs.getInt("x");
				int y=rs.getInt("y");
				int height=rs.getInt("height");
				int weight=rs.getInt("weight");
				int r=rs.getInt("r");
				int g=rs.getInt("g");
				int b=rs.getInt("b");
				int type=rs.getInt("type");
				
				switch(type){
				case 2:
					shape=new Line2D.Float(x, weight, height, y);
					break;
				case 3:
					shape=new Ellipse2D.Float(x, y, height, weight);
					break;
				case 4:
					shape=new Rectangle2D.Float(x, y, height, weight);
					break;
				}
				color = new Color(r,g,b);

				DrawingBoard.shapes.add(shape);
				DrawingBoard.colors.add(color);

			}

			stmt.close();
			conn.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}
	
	public static void deleteAll() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			stmt = conn.createStatement();
			String sql = "delete from shapes where user_id='" + userID + "'";
			stmt.executeUpdate(sql);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

	}

}
