package com.mcams.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mcams.bean.AuthenticationBean;
import com.mcams.bean.SecQueBean;
import com.mcams.bean.UserBean;
import com.mcams.exception.AppException;
import com.mcams.ui.MCAMS;
import com.mcams.util.DBUtil;

public class AuthenticationDAO implements IAuthenticationDAO {
	public static Connection conn = DBUtil.getConnection();
	Logger myLogger =  Logger.getLogger(AuthenticationDAO.class.getName());
	
	public UserBean checkCredentials(AuthenticationBean bean) throws AppException {
		UserBean userBean = new UserBean();
		PropertyConfigurator.configure(MCAMS.path);
		String sql = "SELECT User_Password, User_Name FROM User_Master WHERE User_Id='"+bean.getUserId()+"'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			if(rs.next()) {
				if(bean.getPassword().equals(rs.getString(1))){
					myLogger.info("Login Successfully");
					userBean.setUserId(0);
					userBean.setUserName(rs.getString(2));
					return userBean;
				}
				else{
					myLogger.info("Password not matched");
					userBean.setUserId(1);
					return userBean;
				}
			}
			else{
				myLogger.info("User not exist");
				userBean.setUserId(-1);
				return userBean;
			}
			
		} catch (SQLException e) {
			myLogger.error(e);
			throw new AppException(e.getMessage());
		}
	}

	@Override
	public int updateSecQue(int userId, int queNo, String answer) {
		String sql = "UPDATE User_Master SET SecQue_Id="+queNo+", SecQue_Ans='"+answer+"' WHERE User_Id="+userId;
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			myLogger.info("Security question updated");
			return 0;
		} catch (SQLException e) {
			myLogger.error(e);
			return 1;
		}
	}

	public SecQueBean searchSecQue(String userId) {
		SecQueBean secQueBean = new SecQueBean();
		
		try {
			String sql = "SELECT SecQue_Id FROM User_Master WHERE User_Id="+userId;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			if(rs.next()) {
				sql = "SELECT * FROM SecQue_Master WHERE SecQue_Id="+rs.getInt(1);
				rs = st.executeQuery(sql);
				rs.next();
				secQueBean.setSecQueId(rs.getInt(1));
				secQueBean.setSecQuestion(rs.getString(2));
				return secQueBean;
			}
			else {
				secQueBean.setSecQueId(0);
				return secQueBean;
			}
		} catch (SQLException e) {
			myLogger.error(e);
			secQueBean.setSecQueId(-1);
			return secQueBean;
		}
	}

	@Override
	public UserBean getUser(String userId) {
		UserBean userBean = new UserBean();
		
		try {
			String sql = "SELECT SecQue_Answer FROM User_Master WHERE User_Id="+userId;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			userBean.setUserId(Integer.parseInt(userId));
			userBean.setSecQueAnswer(rs.getString(1));
			return userBean;
		} catch (Exception e) {
			myLogger.error(e);
			userBean.setUserId(0);
			return userBean;
		}
	}

	@Override
	public int resetPassword(int userId, int randomPassword) {
		
		try {
			String sql = "UPDATE User_Master SET User_Password='"+randomPassword+"' WHERE User_Id="+userId;
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			myLogger.info("User's Password reset");
			return 0;
		} catch (SQLException e) {
			myLogger.error(e);
			return 1;
		}
	}

	public int checkUser(String username) {
		try {
			String sql = "SELECT User_Id FROM User_Master WHERE LOWER(User_Name)=LOWER('"+username+"')";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()) return 1;
			else return 0;
		}catch(SQLException e) {
			myLogger.error(e);
			return -1;
		}
	}

	public int register(UserBean userBean) {
		try {
			//Adding record
			String sql = "INSERT INTO User_Master VALUES(userSeq.NEXTVAL,'"+userBean.getUserName()+"','"+userBean.getUserPassword()+"',"+userBean.getSecQueId()+",'"+userBean.getSecQueAnswer()+"',userSeq.NEXTVAL,SYSDATE,userSeq.NEXTVAL,SYSDATE)";
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
			
			//Fetching generated record id
			sql = "SELECT userSeq.CURRVAL FROM DUAL";
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			return rs.getInt(1);
		}catch(SQLException e) {
			return 0;
		}
	}

}
