package com.mcams.service;

import java.util.ArrayList;

import com.mcams.bean.SongBean;

/**
 * Interface contains all functions of User service
 *
 */
public interface IUserService {
	

	/**Method to search artist songs
	 * @param name Name of artist in string
	 * @return Array list of songs
	 */
	ArrayList<SongBean> searchArtistSong(String name);

	/**Method to search composer songs
	 * @param name Name of composer in string
	 * @return Array list of songs
	 */
	ArrayList<SongBean> searchComposerSong(String name);

	/**Method to change password
	 * @param userId User Id in integer
	 * @param newPassword New password in String
	 * @return integer value:
	 * 		0 = Password changed successfully
	 * 		1 = Exception occurs	
	 */
	int changePassword(int userId, String newPassword);

}
