package com.mcams.dao;

import java.util.ArrayList;

import com.mcams.bean.ArtistBean;
import com.mcams.bean.ComposerBean;
import com.mcams.bean.SongBean;

/**
 *Interface contains all functions of Admin dao
 *
 */
public interface IAdminDao {

	/**Method to create new Artist
	 * @param artBean Object of ArtistBean class
	 * @param isUpdate Boolean attribute if true then update existing artist else create new artist
	 * @return artBean Object of ArtistBean class
	 */
	ArtistBean createArtist(ArtistBean artBean, boolean isUpdate);

	/**Method to create new composer
	 * @param compBean Object of ComposerBean class
	 * @param isUpdate Boolean attribute if true then update existing composer else create new composer
	 * @param mSocietyName Name of music society
	 * @return Id in integer
	 */
	int createComposer(ComposerBean compBean, boolean isUpdate, String mSocietyName);

	/**Method to search artist songs
	 * @param name Name of song in string
	 * @return List of songs
	 */
	ArrayList<SongBean> searchArtistSong(String name);

	/**Method to search composer songs
	 * @param name Name of song in string
	 * @return List of songs
	 */
	ArrayList<SongBean> searchComposerSong(String name);

	/**Method to search artist
	 * @param name Name of artist in string
	 * @return Object of ArtistBean class
	 */
	ArrayList<ArtistBean> searchArtist(String name);

	/**Method to search artist
	 * @param name Name of composer in string
	 * @return Object of ComposerBean class
	 */
	ArrayList<ComposerBean> searchComposer(String name);

	/**Method to update artist
	 * @param artBean Object of ArtistBean class
	 * @return artBean object of ArtistBean class
	 */
	ArtistBean updateArtist(ArtistBean artBean);

	/**Method to update composer
	 * @param compBean object of SongBean class
	 * @return compBean object of ComposerBean class
	 */
	ComposerBean updateComposer(ComposerBean compBean);

	/**
	 * @param songBean Object of SongBean class
	 * @param artBean Object of ArtistBean class
	 * @param userId User Id in integer
	 * @param isUpdate  Boolean attribute if true then update existing song else create new song
	 * @return songBean object of SongBean class
	 */
	SongBean assocArtist(SongBean songBean, ArtistBean artBean, int userId, boolean isUpdate);
	
	/**
	 * @param songBean Object of SongBean class
	 * @param compBean Object of ComposerBean class
	 * @param userId User Id in integer
	 * @param isUpdate Boolean attribute if true then update existing song else create new song
	 * @return songBean object of SongBean class
	 */
	SongBean assocComposer(SongBean songBean, ComposerBean compBean, int userId, boolean isUpdate);

	/**Method to delete artist
	 * @param artistId Artist Id in integer
	 * @param userId User Id in integer
	 * @return integer value:
	 * 		0 = Artist deleted successfully
	 * 		1 = Exception occurs		
	 */
	int deleteArtist(int artistId, int userId);

	/**Method to delete song
	 * @param songId Song Id in integer
	 * @param userId User Id in integer
	 * @return integer value:
	 * 		0 = Song deleted successfully
	 * 		1 = Exception occurs			
	 */
	int deleteSong(int songId, int userId);

	/**
	 * @param userId User Id in integer
	 * @param newPassword  New password in String
	 * @return Integer value:
	 * 		0 = Password changed successfully
	 * 		1 = Exception occurs		
	 */
	int changePassword(int userId, String newPassword);

}
