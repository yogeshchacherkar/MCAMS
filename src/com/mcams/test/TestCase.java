package com.mcams.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.mcams.bean.ArtistBean;
import com.mcams.bean.ComposerBean;
import com.mcams.bean.SongBean;
import com.mcams.dao.AdminDao;
import com.mcams.exception.AppException;
public class TestCase {
	
	 AdminDao dao=new AdminDao();
	 SongBean sb=new SongBean();
	
	
//	 @Test(expected = SQLException.class)
//	  public void testExceptionIsThrown(){
//		 IAdminDao dao=new AdminDao();
//		 dao.createArtist(bean,true);
//	 }
	// @Test(expected=AppException.class)
	// public void createArtistTest() throws AppException, ParseException{
//		 AdminDao dao=new AdminDao();
//		 bean1.setId(20001);
//	
//		 bean1.setCreatedBy(100000);
//		 bean1.setCreatedOn(LocalDate.now());
//		 bean1.setDeletedFlag(0);
//		// bean1.setDiedDate(null);
//		 bean1.setName("Ayush");
	//	 ArtistBean bean=dao.createArtist(bean1,false);
		// bean.setId(id);
	//	 Assert.assertNotNull("Test done",bean);
	// }
	 
	@Test
	 public void searchInvalidArtistTest() throws AppException{
	
		 ArrayList<ArtistBean> bean=dao.searchArtist("Ati Aslam");
		 Assert.assertNotNull("No Match Found",bean);
	 }
	
	@Test
	 public void searchValidArtistTest() throws AppException{
		
		 ArrayList<ArtistBean> bean=dao.searchArtist("Atif Aslam");
		 Assert.assertNotNull("Test Done",bean);
	 }
	 
	 
	 
	 @Test
	 public void searchInvalidComposerTest() throws AppException{
		
		 ArrayList<ComposerBean> cBean=dao.searchComposer("Anu Mlik");
		 Assert.assertNotNull("No Match Found", cBean);
	 }
	 @Test
	 public void searchValidComposerTest() throws AppException{
			
		 ArrayList<ComposerBean> cBean=dao.searchComposer("Anu Malik");
		 Assert.assertNotNull("Match Found", cBean);
	 }
	 
	 @Test
	 public void searchInvalidArtistSongTest() throws AppException{
		 ArrayList <SongBean> expected = new ArrayList <SongBean>();
		// expected.add(new SongBean(200008,"Anu Malik",null,100000,null,100000,null,0));
		 sb.setCreatedBy(100000);
		 sb.setCreatedOn(LocalDate.of(16, 11, 18));
		 sb.setDeletedFlag(0);
		 sb.setDuration(LocalTime.parse("01:04:05", DateTimeFormatter.ofPattern("H:m:s")));
		 sb.setId(300001);
		 sb.setName("Atif Aslam");
		 sb.setUpdatedBy(100000);
		 sb.setUpdatedOn(LocalDate.of(16, 11, 18));
		 expected.add(sb);
		 assertEquals("No Match Found", expected,dao.searchArtistSong("Atif Aslam"));
		 }

}
