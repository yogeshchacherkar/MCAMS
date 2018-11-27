package com.mcams.dao;

import java.util.ArrayList;

import com.mcams.bean.SongBean;
/**
 * Interface contains all functions of User dao
 *
 */
public interface IUserDao {
	
	
	/**Method to search artist
	 * @param name Name of artist in string
	 * @return Array list of songs
	 */
	ArrayList<SongBean> searchArtist(String name);


	/**Method to change password
	 * @param userId User Id in integer
	 * @param newPassword New password in String
	 * @return integer value:
	 * 		0 = Password changed successfully
	 * 		1 = Exception occurs		
	 */
	int changePassword(int userId, String newPassword);
	
}
