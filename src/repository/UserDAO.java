package repository;

/**
 * @author Xuesong Meng&Yidu Liang
 * @author Joanne Zhuo
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import domain.User;

public class UserDAO {

	/**
	 * Add an user into DB (user name, password, email, project Id, security question, security answer)
	 * 
	 * @param User object
     * 			userName, password, email, sercurityQuestion, securityAnswer
     * @return true if success; false if fail
	 */
	public static boolean addUser(User user) {
		ResultSet rs;
		// set projectId using the only one project in DB
		//user.setProjectId(ProjectDAO.getProjectId());

		try {
			Connection conn = DbManager.getConnection();
			//PreparedStatement pstmt = conn.prepareStatement(
			//				"INSERT into user(userName, password, email, project_Id, securityQuestion, securityAnswer) VALUES(?,?,?,?,?,?);",
			//				Statement.RETURN_GENERATED_KEYS);
			// Modified by Xuesong Meng
			PreparedStatement pstmt = conn
					.prepareStatement(
							"INSERT into user(userName,email,password, securityQ, securityA) VALUES(?,?,?,?,?);",
							Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, user.getUserName());			
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getPassword());
			//No projectId in user table;
			//pstmt.setInt(4, user.getProjectId());
			pstmt.setString(4, user.getSecurityQuestion());
			pstmt.setString(5, user.getSecurityAnswer());

			// Execute the SQL statement and update database accordingly.
			pstmt.executeUpdate();

			// Get userId generated by DB back, and set it in user object
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				int newId = rs.getInt(1);
				user.setUserId(newId);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
		return true;
	}

	/**
	 * Get an user from DB by name and password
	 * 
     * @param username
     * @param password
     * @return User object
     */
	public static User getUser(String username, String password) {
		try {
			Connection conn = DbManager.getConnection();
			PreparedStatement pstmt;
			pstmt = conn.prepareStatement("SELECT * FROM user where userName = ? and password = ?;");
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			// Execute the SQL statement and store result into the ResultSet
			ResultSet rs = pstmt.executeQuery();
			
			// Modified by Xuesong Meng
			if(rs.next()){
			User user;
			user = new User(rs.getInt("userId"), username, password,
					rs.getString("email"), rs.getString("securityQ"),
					rs.getString("securityA"));
			rs.close();
			pstmt.close();
			conn.close();
			return user;
			}
			else return null;			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("Using default model.");
		}

		return null;
	}

	/**
	 * Get an user from DB by name
	 * 
     * @param username
     * @return User object
     */
	public static User getUser(String username) {
		try {
			Connection conn = DbManager.getConnection();
			PreparedStatement pstmt;

			pstmt = conn.prepareStatement("SELECT * FROM user where userName = ?;");
			pstmt.setString(1, username);
			
			// Execute the SQL statement and store result into the ResultSet
			ResultSet rs = pstmt.executeQuery();

			if (!rs.next()) {
				return null;
			}

			// Modified by Xuesong Meng
			User user;
			user = new User(rs.getInt("userId"), username, "",
					rs.getString("email"), rs.getString("securityQ"),
					rs.getString("securityA"));

			rs.close();
			pstmt.close();
			conn.close();
			return user;
		} catch (SQLException e) {
			System.out.println("Using default model.");
		}

		return null;
	}
	
	/**
	 * Get user from DB by userId
	 * 
     * @param userId
     * @return User object
	 */
	public static User getUser(int userId) {
		try {
			Connection conn = DbManager.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("SELECT * FROM user where userId = ?;");
			pstmt.setInt(1, userId);

			// Execute the SQL statement and store result into the ResultSet
			ResultSet rs = pstmt.executeQuery();

			if (!rs.next()) {
				return null;
			}

			User user;
			user = new User(rs.getInt("userId"), rs.getString("userName"), "",
					rs.getString("email"), "", "");
			rs.close();
			pstmt.close();
			conn.close();
			return user;
		} catch (SQLException e) {
			System.out.println("Using default model.");
		}
		return null;
	}
	
	/**
	 * Remove a user from DB
	 * 
     * @param User object
     * @return true if success; false if fail
	 */
	public static boolean removeUser(User user) {
		if (user == null) {
			return false;
		}
		try {
			Connection conn = DbManager.getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("DELETE FROM user where userId = ?;");
			pstmt.setInt(1, user.getUserId());

			// Execute the SQL statement and update database accordingly.
			pstmt.executeUpdate();

			pstmt.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	/**
	 * Update user in DB
	 * 
     * @param User object
     * 			userName, password, email, securityQuestion, securityAnswer, userId
     * @return true if success; false if fail
	 */
	public static boolean updateUser(User user) {
		try {
			Connection conn = DbManager.getConnection();
			// Modified by Xuesong Meng
			//PreparedStatement pstmt = conn
			//		.prepareStatement("UPDATE user SET userName=? , password=?, email=?, securityQuestion =?, securityAnswer=? where user_Id = ?;");
			PreparedStatement pstmt = conn
					.prepareStatement("UPDATE user SET userName=? , password=?, email=?, securityQ =?, securityA=? where userId = ?;");
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getEmail());
			pstmt.setString(4, user.getSecurityQuestion());
			pstmt.setString(5, user.getSecurityAnswer());
			pstmt.setInt(6, user.getUserId());
			// Execute the SQL statement and update database accordingly.
			pstmt.executeUpdate();

			pstmt.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}
}
