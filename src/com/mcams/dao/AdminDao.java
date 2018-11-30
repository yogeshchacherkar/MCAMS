package com.mcams.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mcams.bean.ArtistBean;
import com.mcams.bean.ComposerBean;
import com.mcams.bean.SongBean;
import com.mcams.ui.MCAMS;

public class AdminDao implements IAdminDao {
	
	private static String sql;
	private static Statement st;
	private static Connection conn = AuthenticationDAO.conn;
	Logger myLogger =  Logger.getLogger(AdminDao.class.getName());

	@Override
	public ArtistBean createArtist(ArtistBean artBean, boolean isUpdate) {
		PropertyConfigurator.configure(MCAMS.path);
		if(isUpdate) {
			artBean = updateArtist(artBean);
			return artBean;
		}
		
		else {
			sql = "SELECT * FROM Artist_Master WHERE LOWER(Artist_Name) LIKE LOWER('%"+artBean.getName()+"%')";
			try {
				st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql);
				
				if(rs.next()) {
					if(rs.getDate(3)==null) artBean.setBornDate(null);
					else artBean.setBornDate(rs.getDate(3).toLocalDate());
					if(rs.getDate(4)==null) artBean.setDiedDate(null);
					else artBean.setDiedDate(rs.getDate(4).toLocalDate());
					artBean.setCreatedBy(rs.getInt(5));
					artBean.setCreatedOn(rs.getDate(6).toLocalDate());
					artBean.setUpdatedBy(rs.getInt(7));
					artBean.setUpdatedOn(rs.getDate(8).toLocalDate());
					artBean.setDeletedFlag(rs.getInt(9));
					
					if(artBean.getDeletedFlag()==0) {
						artBean.setId(-(rs.getInt(1)));
						return artBean;
					}
					else {
						artBean.setId(-((rs.getInt(1))+100000));
						return artBean;
					}
				}
				
				String bDate, dDate;
				if(artBean.getBornDate() == null) bDate=null;
				else bDate="TO_DATE('"+artBean.getBornDate()+"','yyyy/mm/dd')";
				if(artBean.getDiedDate() == null) dDate=null;
				else dDate="TO_DATE('"+artBean.getDiedDate()+"','yyyy/mm/dd')";
				sql = "INSERT INTO Artist_Master (Artist_Id,Artist_Name,Artist_BornDate,Artist_DiedDate,Created_By,Created_On,Updated_By,Updated_On,Artist_DeletedFlag) "
						+ "VALUES(artistSeq.NEXTVAL,'"+artBean.getName()+"',"+bDate+","+dDate+","+artBean.getCreatedBy()+",SYSDATE,"+artBean.getUpdatedBy()+",SYSDATE,0)";
				st.executeUpdate(sql);
				rs = st.executeQuery("SELECT artistSeq.CURRVAL FROM DUAL");
				rs.next();
				artBean.setId(rs.getInt(1));
				myLogger.info(" artist created " +artBean);
				return artBean;
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				artBean.setId(0);
				myLogger.info(" Exception found"+e.getMessage() );
				return artBean;
			}
			
		}
	}

	@Override
	public int createComposer(ComposerBean compBean, boolean isUpdate, String mSocietyName) {
		PropertyConfigurator.configure(MCAMS.path);
		if(isUpdate) {
			if(mSocietyName!=null) {
				try {
					sql = "INSERT INTO MusicSociety_Master VALUES('"+new String(compBean.getMusicSocietyId())+"','"+mSocietyName+"')";
					st = conn.createStatement();
					st.executeUpdate(sql);
				} catch (SQLException e) {
					myLogger.info(" Exception found" );
				}
			}
			compBean = updateComposer(compBean);
			return compBean.getId();
		}
		
		else {			
			sql = "SELECT Composer_Id, Composer_DeletedFlag FROM Composer_Master WHERE LOWER(Composer_Name) LIKE LOWER('%"+compBean.getName()+"%')";
			try {
				st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql);
				if(rs.next()) {
					if(rs.getInt(2)==0)
						return -(rs.getInt(1));
					else
						return -((rs.getInt(1))+100000);
				}
				
				if(mSocietyName!=null) {
					try {
						sql = "INSERT INTO MusicSociety_Master VALUES('"+new String(compBean.getMusicSocietyId())+"','"+mSocietyName+"')";
						st = conn.createStatement();
						st.executeUpdate(sql);
					} catch (SQLException e) {
						myLogger.info(" Exception found" +e.getMessage());
						e.printStackTrace();
					}
				}
				
				String bDate, dDate;
				if(compBean.getBornDate() == null) bDate=null;
				else bDate="TO_DATE('"+compBean.getBornDate()+"','yyyy/mm/dd')";
				if(compBean.getDiedDate() == null) dDate=null;
				else dDate="TO_DATE('"+compBean.getDiedDate()+"','yyyy/mm/dd')";
				sql = "INSERT INTO Composer_Master (Composer_Id,Composer_Name,Composer_BornDate,Composer_DiedDate,Composer_CaeipiNumber,Composer_MusicSocietyID,Created_By,Created_On,Updated_By,Updated_On,Composer_DeletedFlag) "
						+ "VALUES(composerSeq.NEXTVAL,'"+compBean.getName()+"',"+bDate+","+dDate+",'"+compBean.getCaeipiNumber()+"','"+new String(compBean.getMusicSocietyId())+"',"+compBean.getCreatedBy()+",SYSDATE,"+compBean.getUpdatedBy()+",SYSDATE,0)";
				st.executeUpdate(sql);
				rs = st.executeQuery("SELECT composerSeq.CURRVAL FROM DUAL");
				rs.next();
				myLogger.info(" composer added " + compBean);
				return rs.getInt(1);
				
			} catch (SQLException e) {
				myLogger.info(" Exception found" +e.getMessage());
				return 0;
			}
			
		}
		
		
	}
	
	@Override
	public ArrayList<SongBean> searchArtistSong(String name) {
		PropertyConfigurator.configure(MCAMS.path);
		ResultSet rs;
		ArrayList<SongBean> songList = new ArrayList<SongBean>();
		
		sql="SELECT Artist_Id from Artist_Master WHERE LOWER(Artist_Name) LIKE LOWER('%"+name+"%') AND Artist_DeletedFlag=0";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(!rs.next()) return null;
			else {
				sql =  "SELECT * from Song_Master INNER JOIN Artist_Song_Assoc ON Artist_Song_Assoc.Song_Id=Song_Master.Song_Id WHERE "
						+ "Artist_Song_Assoc.Artist_Id IN (select Artist_Id FROM Artist_Master WHERE LOWER(Artist_Name) LIKE LOWER('%"+name+"%')) AND Song_Master.Song_DeletedFlag=0";
				rs = st.executeQuery(sql);
				while(rs.next()){
					SongBean sb = new SongBean();
					sb.setId(rs.getInt(1));
					sb.setName(rs.getString(2));
					sb.setDuration(rs.getTime(3).toLocalTime());
					sb.setCreatedBy(rs.getInt(4));
					sb.setCreatedOn(rs.getDate(5).toLocalDate());
					sb.setUpdatedBy(rs.getInt(6));
					sb.setUpdatedOn(rs.getDate(7).toLocalDate());
					songList.add(sb);
				}
				myLogger.info(" artist song found ");
				return songList;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			myLogger.info(" Exception found" +e.getMessage());
			return null;
		}
	}

	@Override
	public ArrayList<SongBean> searchComposerSong(String name) {
		PropertyConfigurator.configure(MCAMS.path);
		ResultSet rs;
		ArrayList<SongBean> songList = new ArrayList<SongBean>();
		
		sql="SELECT Composer_Id from Composer_Master WHERE LOWER(Composer_Name) LIKE LOWER('%"+name+"%') AND Composer_DeletedFlag=0";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(!rs.next()) return null;
			else {
				sql =  "SELECT * from Song_Master INNER JOIN Composer_Song_Assoc ON Composer_Song_Assoc.Song_Id=Song_Master.Song_Id WHERE "
						+ "Composer_Song_Assoc.Composer_Id IN (select Composer_Id FROM Composer_Master WHERE LOWER(Composer_Name) LIKE LOWER('%"+name+"%')) AND Song_Master.Song_DeletedFlag=0";
				rs = st.executeQuery(sql);
				while(rs.next()){
					SongBean sb = new SongBean();
					sb.setId(rs.getInt(1));
					sb.setName(rs.getString(2));
					sb.setDuration(rs.getTime(3).toLocalTime());
					sb.setCreatedBy(rs.getInt(4));
					sb.setCreatedOn(rs.getDate(5).toLocalDate());
					sb.setUpdatedBy(rs.getInt(6));
					sb.setUpdatedOn(rs.getDate(7).toLocalDate());
					songList.add(sb);
				}
				myLogger.info(" composer song found ");
				return songList;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			myLogger.info(" Exception found" + e.getMessage());
			return null;
		}
	}

	@Override
	public ArrayList<ArtistBean> searchArtist(String name) {
		PropertyConfigurator.configure(MCAMS.path);
		ResultSet rs;
		ArrayList<ArtistBean> artistList = new ArrayList<ArtistBean>();
		sql="SELECT * from Artist_Master WHERE Artist_Name='"+name+"' AND Artist_DeletedFlag=0";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(!rs.next()){
				sql="SELECT * from Artist_Master WHERE LOWER(Artist_Name) LIKE LOWER('%"+name+"%') AND Artist_DeletedFlag=0";
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(!rs.next()) {
					myLogger.info(" artist not found ");
					return artistList;
				}
				do{
					ArtistBean ab = new ArtistBean();
					ab.setId(rs.getInt(1));
					ab.setName(rs.getString(2));
					if(rs.getDate(3) == null) ab.setBornDate(null);
					else ab.setBornDate(rs.getDate(3).toLocalDate());
					if(rs.getDate(4) == null) ab.setDiedDate(null);
					else ab.setDiedDate(rs.getDate(4).toLocalDate());
					ab.setCreatedBy(rs.getInt(5));
					ab.setCreatedOn(rs.getDate(6).toLocalDate());
					ab.setUpdatedBy(rs.getInt(7));
					ab.setUpdatedOn(rs.getDate(8).toLocalDate());
					artistList.add(ab);
				}while(rs.next());
				myLogger.info(" Artist found ");
				return artistList;
			}
			else {
				ArtistBean ab = new ArtistBean();
				ab.setId(rs.getInt(1));
				ab.setName(rs.getString(2));
				if(rs.getDate(3) == null) ab.setBornDate(null);
				else ab.setBornDate(rs.getDate(3).toLocalDate());
				if(rs.getDate(4) == null) ab.setDiedDate(null);
				else ab.setDiedDate(rs.getDate(4).toLocalDate());
				ab.setCreatedBy(rs.getInt(5));
				ab.setCreatedOn(rs.getDate(6).toLocalDate());
				ab.setUpdatedBy(rs.getInt(7));
				ab.setUpdatedOn(rs.getDate(8).toLocalDate());
				artistList.add(ab);
				myLogger.info(" Artist found ");
				return artistList;
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			myLogger.info(" Exception found" );
			return artistList;
		}
	}

	@Override
	public ArrayList<ComposerBean> searchComposer(String name) {
		PropertyConfigurator.configure(MCAMS.path);
		ResultSet rs;
		ArrayList<ComposerBean> composerList = new ArrayList<ComposerBean>();
		sql="SELECT * from Composer_Master WHERE Composer_Name='"+name+"' AND Composer_DeletedFlag=0";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(!rs.next()){
				sql="SELECT * from Composer_Master WHERE LOWER(Composer_Name) LIKE LOWER('%"+name+"%') AND Composer_DeletedFlag=0";
				st = conn.createStatement();
				rs = st.executeQuery(sql);
				if(!rs.next()) {
					myLogger.info(" composer not found ");
					return composerList;
				}
				do{
					ComposerBean cb = new ComposerBean();
					cb.setId(rs.getInt(1));
					cb.setName(rs.getString(2));
					if(rs.getDate(3) == null) cb.setBornDate(null);
					else cb.setBornDate(rs.getDate(3).toLocalDate());
					if(rs.getDate(4) == null) cb.setDiedDate(null);
					else cb.setDiedDate(rs.getDate(4).toLocalDate());
					cb.setCaeipiNumber(rs.getString(5));
					cb.setMusicSocietyId(rs.getString(6).toCharArray());
					cb.setCreatedBy(rs.getInt(7));
					cb.setCreatedOn(rs.getDate(8).toLocalDate());
					cb.setUpdatedBy(rs.getInt(9));
					cb.setUpdatedOn(rs.getDate(10).toLocalDate());
					composerList.add(cb);
				}while(rs.next());
				myLogger.info(" Composer found ");
				return composerList;
			}
			else {
				ComposerBean cb = new ComposerBean();
				cb.setId(rs.getInt(1));
				cb.setName(rs.getString(2));
				if(rs.getDate(3) == null) cb.setBornDate(null);
				else cb.setBornDate(rs.getDate(3).toLocalDate());
				if(rs.getDate(4) == null) cb.setDiedDate(null);
				else cb.setDiedDate(rs.getDate(4).toLocalDate());
				cb.setCaeipiNumber(rs.getString(5));
				cb.setMusicSocietyId(rs.getString(6).toCharArray());
				cb.setCreatedBy(rs.getInt(7));
				cb.setCreatedOn(rs.getDate(8).toLocalDate());
				cb.setUpdatedBy(rs.getInt(9));
				cb.setUpdatedOn(rs.getDate(10).toLocalDate());
				composerList.add(cb);
				myLogger.info(" Composer found ");
				return composerList;
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			myLogger.info(" Exception found" );
			return composerList;
		}
	}

	@Override
	public ArtistBean updateArtist(ArtistBean artBean) {
		PropertyConfigurator.configure(MCAMS.path);
		String bDate, dDate;
		
		if(artBean.getBornDate()==null) bDate=null;
		else bDate="TO_DATE('"+artBean.getBornDate()+"','yyyy/mm/dd')";
		
		if(artBean.getDiedDate()==null) dDate=null;
		else dDate="TO_DATE('"+artBean.getDiedDate()+"','yyyy/mm/dd')";
		
		sql = "UPDATE Artist_Master SET Artist_Name='"+artBean.getName()+"',Artist_BornDate="+bDate+",Artist_DiedDate="+dDate+",Updated_by="+artBean.getUpdatedBy()+",Updated_On=SYSDATE,Artist_DeletedFlag=0 WHERE Artist_Id="+artBean.getId();
		try {
			st.executeUpdate(sql);
			myLogger.info(" Artist updated ");
			return artBean;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			myLogger.info(" Exception found" );
			return artBean;
		}
	}
	
	@Override
	public ComposerBean updateComposer(ComposerBean compBean) {
		PropertyConfigurator.configure(MCAMS.path);
		String bDate, dDate;
		
		if(compBean.getBornDate()==null) bDate=null;
		else bDate="TO_DATE('"+compBean.getBornDate()+"','yyyy/mm/dd')";
		
		if(compBean.getDiedDate()==null) dDate=null;
		else dDate="TO_DATE('"+compBean.getDiedDate()+"','yyyy/mm/dd')";
		
		sql = "UPDATE Composer_Master SET Composer_Name='"+compBean.getName()+"',Composer_BornDate="+bDate+",Composer_DiedDate="+dDate+",Composer_Caeipinumber='"+compBean.getCaeipiNumber()+"',Composer_Musicsocietyid='"+new String(compBean.getMusicSocietyId())+"',Updated_by="+compBean.getUpdatedBy()+",Updated_On=SYSDATE,Composer_DeletedFlag=0 WHERE Composer_Id="+compBean.getId();
		try {
			st.executeUpdate(sql);
			myLogger.info(" Composer updated ");
			return compBean;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			myLogger.info(" Exception found" );
			return compBean;
		}
	}

	@Override
	public SongBean assocArtist(SongBean songBean, ArtistBean artBean, int userId, boolean isUpdate) {
		PropertyConfigurator.configure(MCAMS.path);
		SongBean getSong = new SongBean();
		if(isUpdate) {
			getSong = updateSong(songBean,userId);
			sql = "INSERT INTO Artist_Song_Assoc VALUES("+songBean.getId()+","+artBean.getId()+","+userId+",SYSDATE,"+userId+",SYSDATE)";
			try {
				st.executeUpdate(sql);
				return getSong;
			} catch (SQLException e) {
				songBean.setId(0);
				myLogger.info(" Exception found" );
				return songBean;
			}
			
		}
		
		else {
			sql = "SELECT * FROM Song_Master WHERE LOWER(Song_Name) LIKE LOWER('%"+songBean.getName()+"%')";
			try {
				st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql);
				
				if(rs.next()) {
					getSong.setId(rs.getInt(1));
					getSong.setName(rs.getString(2));
					getSong.setDuration(rs.getTime(3).toLocalTime());
					getSong.setCreatedBy(rs.getInt(4));
					getSong.setCreatedOn(rs.getDate(5).toLocalDate());
					getSong.setUpdatedBy(rs.getInt(6));
					getSong.setUpdatedOn(rs.getDate(7).toLocalDate());
					getSong.setDeletedFlag(rs.getInt(8));
					
					if(getSong.getDeletedFlag()==0) {
						getSong.setId(-(rs.getInt(1)));
						return getSong;
					}
					else {
						getSong.setId(-((rs.getInt(1))+100000));
						return getSong;
					}
				}
				
				sql = "INSERT INTO Song_Master VALUES (songSeq.NEXTVAL,'"+songBean.getName()+"','01-JAN-2000 1:"+songBean.getDuration().getMinute()+":"+songBean.getDuration().getSecond()+"',"+userId+",SYSDATE,"+userId+",SYSDATE,0)";
				st.executeUpdate(sql);
				rs = st.executeQuery("SELECT songSeq.CURRVAL FROM DUAL");
				rs.next();
				songBean.setId(rs.getInt(1));
				
				sql = "INSERT INTO Artist_Song_Assoc VALUES("+songBean.getId()+","+artBean.getId()+","+userId+",SYSDATE,"+userId+",SYSDATE)";
				st.executeUpdate(sql);
				myLogger.info(" artist song inserted ");
				return songBean;
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				songBean.setId(0);
				myLogger.info(" Exception found" );
				return songBean;
			}
			
		}
		
	}

	private SongBean updateSong(SongBean songBean, int userId) {
		PropertyConfigurator.configure(MCAMS.path);
		sql = "UPDATE Song_Master SET Song_Name='"+songBean.getName()+"',Song_Duration='01-JAN-2000 12:"+songBean.getDuration().getMinute()+":"+songBean.getDuration().getSecond()+"',Created_By="+songBean.getCreatedBy()+",Created_On=SYSDATE,Updated_By="+songBean.getUpdatedBy()+",Updated_On=SYSDATE,Song_DeletedFlag=0 WHERE Song_Id="+songBean.getId();
		try {
			st.executeUpdate(sql);
			myLogger.info(" song updated ");
			return songBean;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			songBean.setId(0);
			myLogger.info(" Exception found" );
			return songBean;
		}
	}

	@Override
	public SongBean assocComposer(SongBean songBean, ComposerBean compBean, int userId, boolean isUpdate) {
		PropertyConfigurator.configure(MCAMS.path);
		SongBean getSong = new SongBean();
		if(isUpdate) {
			getSong = updateSong(songBean,userId);
			sql = "INSERT INTO Composer_Song_Assoc VALUES("+songBean.getId()+","+compBean.getId()+","+userId+",SYSDATE,"+userId+",SYSDATE)";
			try {
				st.executeUpdate(sql);
				return getSong;
			} catch (SQLException e) {
				songBean.setId(0);
				myLogger.info(" Exception found" );
				return songBean;
			}
			
		}
		
		else {
			sql = "SELECT * FROM Song_Master WHERE LOWER(Song_Name) LIKE LOWER('%"+songBean.getName()+"%')";
			try {
				st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql);
				
				if(rs.next()) {
					getSong.setId(rs.getInt(1));
					getSong.setName(rs.getString(2));
					getSong.setDuration(rs.getTime(3).toLocalTime());
					getSong.setCreatedBy(rs.getInt(4));
					getSong.setCreatedOn(rs.getDate(5).toLocalDate());
					getSong.setUpdatedBy(rs.getInt(6));
					getSong.setUpdatedOn(rs.getDate(7).toLocalDate());
					getSong.setDeletedFlag(rs.getInt(8));
					
					if(getSong.getDeletedFlag()==0) {
						getSong.setId(-(rs.getInt(1)));
						return getSong;
					}
					else {
						getSong.setId(-((rs.getInt(1))+100000));
						return getSong;
					}
				}
				
				sql = "INSERT INTO Song_Master VALUES (songSeq.NEXTVAL,'"+songBean.getName()+"','01-JAN-2000 1:"+songBean.getDuration().getMinute()+":"+songBean.getDuration().getSecond()+"',"+userId+",SYSDATE,"+userId+",SYSDATE,0)";
				st.executeUpdate(sql);
				rs = st.executeQuery("SELECT songSeq.CURRVAL FROM DUAL");
				rs.next();
				songBean.setId(rs.getInt(1));
				
				sql = "INSERT INTO Composer_Song_Assoc VALUES("+songBean.getId()+","+compBean.getId()+","+userId+",SYSDATE,"+userId+",SYSDATE)";
				st.executeUpdate(sql);
				myLogger.info(" composer song inserted ");
				return songBean;
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				songBean.setId(0);
				myLogger.info(" Exception found" );
				return songBean;
			}
			
		}
	}
	
	@Override
	public int deleteArtist(int artistId, int userId) {
		PropertyConfigurator.configure(MCAMS.path);
		ResultSet rs;
		try {
			sql = "SELECT Song_Id FROM Artist_Song_Assoc WHERE Artist_Id="+artistId+" AND Song_Id NOT IN (SELECT Song_Id FROM Composer_Song_Assoc)";
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				sql = "UPDATE Song_Master SET Updated_By="+userId+", Updated_On=SYSDATE, Song_DeletedFlag=1 WHERE Song_Id="+rs.getInt(1);
				st.executeUpdate(sql);
			}
			
			sql = "DELETE FROM Artist_Song_Assoc WHERE Artist_Id="+artistId;
			st.executeUpdate(sql);
			
			sql = "UPDATE Artist_Master SET Artist_DeletedFlag=1 WHERE Artist_Id="+artistId;
			st.executeUpdate(sql);
			myLogger.info(" artist deleted ");
			return 0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			myLogger.info(" Exception found" );
			return 1;
		}
		
	}

	@Override
	public int deleteComposer(int composerId, int userId) {
		PropertyConfigurator.configure(MCAMS.path);
		ResultSet rs;
		try {
			sql = "SELECT Song_Id FROM Composer_Song_Assoc WHERE Composer_Id="+composerId+" AND Song_Id NOT IN (SELECT Song_Id FROM Artist_Song_Assoc)";
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				sql = "UPDATE Song_Master SET Updated_By="+userId+", Updated_On=SYSDATE, Song_DeletedFlag=1 WHERE Song_Id="+rs.getInt(1);
				st.executeUpdate(sql);
			}
			
			sql = "DELETE FROM Composer_Song_Assoc WHERE Composer_Id="+composerId;
			st.executeUpdate(sql);
			
			sql = "UPDATE Composer_Master SET Composer_DeletedFlag=1 WHERE Composer_Id="+composerId;
			st.executeUpdate(sql);
			myLogger.info(" composer deleted ");
			return 0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			myLogger.info(" Exception found" );
			return 1;
		}
	}

	@Override
	public int deleteSong(int songId, int userId) {
		PropertyConfigurator.configure(MCAMS.path);
		try {
			sql = "DELETE FROM Artist_Song_Assoc WHERE Song_Id="+songId;
			st.executeUpdate(sql);
			
			sql = "DELETE FROM Composer_Song_Assoc WHERE Song_Id="+songId;
			st.executeUpdate(sql);
			
			sql = "UPDATE Song_Master SET Updated_By="+userId+", Updated_On=SYSDATE, Song_DeletedFlag=1 WHERE Song_Id="+songId;
			st.executeUpdate(sql);
			myLogger.info(" Song deleted ");
			return 0;
		}catch(Exception e){
			System.out.println(e.getMessage());
			myLogger.info(" Exception found" );
			return 1;
		}
	}

	public ArrayList<SongBean> searchSong(String name) {
		PropertyConfigurator.configure(MCAMS.path);
		ArrayList<SongBean> songList = new ArrayList<SongBean>();
		ResultSet rs;
		try {
			st = conn.createStatement();
			sql = "SELECT Song_Id, Song_Name, Song_Duration FROM Song_Master WHERE LOWER(Song_Name) LIKE LOWER('%"+name+"%') AND Song_DeletedFlag=0";
			rs = st.executeQuery(sql);
			while(rs.next()) {
				SongBean sb = new SongBean();
				sb.setId(rs.getInt(1));
				sb.setName(rs.getString(2));
				sb.setDuration(rs.getTime(3).toLocalTime());
				songList.add(sb);
			}
			return songList;	
		} catch (Exception e) {
			System.out.println(e.getMessage());
			myLogger.info(" Exception found" );
			return songList;
		}
	}

	public boolean checkMSociety(String mSocietyId) {
		PropertyConfigurator.configure(MCAMS.path);
		try {
			sql = "SELECT * FROM MusicSociety_Master WHERE Composer_MusicSocietyId='"+mSocietyId+"'";
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()) {
				myLogger.info(" music society found ");
				return true;
			}
			else {
				myLogger.info(" music society does not exist ");
				return false;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			myLogger.info(" Exception found" );
			return false;
		}
	}
	
	@Override
	public int changePassword(int userId, String newPassword) {
		PropertyConfigurator.configure(MCAMS.path);
		try {
			sql = "UPDATE User_Master SET User_Password='"+newPassword+"' WHERE User_Id="+userId;
			st = conn.createStatement();
			st.executeUpdate(sql);
			myLogger.info(" Password changed" );
			return 0;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			myLogger.info(" Exception found" );
			return 1;
		}
	}

}
