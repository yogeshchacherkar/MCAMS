

package com.mcams.ui;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import com.mcams.bean.ArtistBean;
import com.mcams.bean.AuthenticationBean;
import com.mcams.bean.ComposerBean;
import com.mcams.bean.SecQueBean;
import com.mcams.bean.SongBean;
import com.mcams.bean.UserBean;
import com.mcams.dao.AuthenticationDAO;
import com.mcams.exception.AppException;
import com.mcams.service.AdminService;
import com.mcams.service.AuthenticationService;
import com.mcams.service.UserService;
import com.mcams.service.ValidationService;

public class MCAMS {
	public static String path;
	//setting log file path in properties file
	static {
		//Check folder for existence
		path =System.getProperty("user.home")+"\\AppData\\Local\\MCAMS";
		
		File file = new File(path);
		
		//If directory not exist then create it
		if(!file.exists()) {
			System.out.println("Creating MCAMS directory...");
			file.mkdirs();
			System.out.println("MCAMS directory created!");
		}
		//Append filename to path
		String logPath = System.getProperty("user.home").replace("\\", "/")+"/AppData/Local/MCAMS";
		path = path+"\\log4j.properties";
		
		file = new File(path);
		
		try {
			if(!file.exists()) {
				System.out.println("Log file creating...");
				file.createNewFile();
				System.out.println("Log file created!");
				System.out.println(path+"\n");
			}
		} catch (IOException e) {
			System.out.println("Cannot create directory!");
		}
		
		//Generate log4j.properties file
		try {
			FileWriter fileWriter = new FileWriter(path);
	        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	        
	        String text;
	        
	        text = "# Log levels";
	        bufferedWriter.write(text);
	        bufferedWriter.newLine();
	        
	        text = "log4j.rootLogger=debug, myAppender";
	        bufferedWriter.write(text);
	        bufferedWriter.newLine();
	        
	        text = "log4j.appender.myAppender=org.apache.log4j.FileAppender";
	        bufferedWriter.write(text);
	        bufferedWriter.newLine();
	        
	        text = "log4j.appender.myAppender.layout=org.apache.log4j.HTMLLayout";
	        bufferedWriter.write(text);
	        bufferedWriter.newLine();
	        
//	        text = "log4j.appender.myAppender.File="+path;
	        text = "log4j.appender.myAppender.File="+logPath+"/Logs.html";
	        bufferedWriter.write(text);
	        

	        bufferedWriter.close();
		}catch(Exception e) {
			System.out.println("Error occured while creating log file!");
		}
	}
	
	static Console console = System.console();
	static Scanner scan = new Scanner(System.in);
	
	static int attempt = 3;
	static String newPass;
	static AuthenticationBean authBean = new AuthenticationBean();
	static UserBean userBean = new UserBean();
	static SecQueBean secQueBean = new SecQueBean();
	static ArtistBean artBean = new ArtistBean();
	static ComposerBean compBean = new ComposerBean();
	static SongBean songBean = new SongBean();
	static AuthenticationService authService = new AuthenticationService();
	static ValidationService valService = new ValidationService();
	static AdminService adminService = new AdminService();
	static UserService userService = new UserService();
	
	/**
	 * @param args Command line argument
	 * @throws AppException
	 * 
	 */
	
	public static void main(String[] args) throws AppException {
		if(AuthenticationDAO.conn == null) {
			System.out.println("Error while connecting to the database");
			System.out.println("Exiting Program.......");
			sleep(3);
			exit();
		}
		loginHome();
	}

	/**
	 * @throws AppException
	 */
	private static void loginHome() throws AppException {
		boolean isContinue;
		while(true) {			
			String userId;
			String password;
			
			int choice;
			while(true){
				clearScreen();			
				System.out.println("*************Music_Composer_and_Artist_Management_System*************\n");
				
				System.out.println("1. Register");
				System.out.println("2. Login");
				System.out.println("3. Forgot Password");
				System.out.println("4. Exit");
				System.out.print("Enter your choice: ");
				choice = valService.validateChoice(scan.nextLine());
				
				if(choice == 1) register();
				else if(choice == 2) break;
				else if(choice == 3) forgotPassword();
				else if(choice == 4) exit();
				else System.out.println("\nPlease enter valid choice!\n");
			}
			
			
				
			while(true) {
				System.out.print("\nEnter user id: ");
				userId = scan.nextLine();
				boolean isValid = valService.validateUserId(userId);
				if(isValid) break;
				else System.out.println("\nInvalid ID! (ID should be 6 digit number)\n");
			}
				
			while(true){
				if(attempt<3)
					System.out.println("Login (attempt remaining: "+attempt+")");
				
				System.out.print("Enter password: ");
				if(console != null)
					password = new String(console.readPassword());
				else
					password = scan.nextLine();
		
				authBean.setUserId(Integer.parseInt(userId));
				authBean.setPassword(password);
				
				/*
				 * userBean.getUserId():
				 *  0 => Correct credentials
				 *  1 => User record found but password not matched
				 * -1 => User record not present in database
				 */
				try {
					userBean = authService.checkCredentials(authBean);
				} catch (AppException e) {
					throw new AppException(e.getMessage());
				}
				
				if(userBean.getUserId() == 0) {
					System.out.println("\nLogin Successful!\n\n");
					isContinue = false;
					break;
				}
				else {
					if(userBean.getUserId() == 1)
						System.out.println("\nERROR: Invalid Password!\n");
					else{
						System.out.println("\nERROR: user "+authBean.getUserId()+" doesn't exist.\n");
						isContinue = true;
						break;
					}
						
					
					attempt--;
					
					if(attempt==0) {
						System.out.println("\nMaximum attempt reached!");
						System.out.println("Redirecting to home page...");
						sleep(2);
						attempt = 3;
						isContinue = true;
						break;
					}
				}		
			}
			
			if(isContinue) continue;
			
			clearScreen();
			
			if(userId.equals("100000") || userId.equals("100001"))
				adminConsole(authBean.getUserId(),authBean.getPassword(),userBean.getUserName());
			else
				clientConsole(authBean.getUserId(),authBean.getPassword(),userBean.getUserName());
		}
	}

	/**
	 * Method for user registration 
	 */
	private static void register() {
		String username;
		String password;
		int validity;
		clearScreen();
		System.out.println("REGISTER\n");
		while(true) {
			System.out.print("Enter username: ");
			username = scan.nextLine();
			boolean isValid = valService.validateUsername(username);
			if(!isValid) {
				System.out.println("Invalid username!");
				System.out.println("Username must start with letter.");
				System.out.println("Allowed characters are a-z (only lower case),0-9 and .(dot)");
				System.out.println("Size ==> Minimum:3 Maximum:50\n");
				
				String choice;
				while(true) {
					System.out.print("\nDo you want to continue? (y/n): ");
					choice = scan.nextLine();
					
					if(choice.equalsIgnoreCase("y")) break;
					else if(choice.equalsIgnoreCase("n")) return;
					else System.out.println("\nPlease enter valid choice!\n");
				}
				continue;
			}
			validity = authService.checkUser(username);
			
			if(validity == 0) {
				while(true) {
					System.out.print("Enter password: ");
					password = scan.nextLine();
					isValid = valService.validatePassword(password);
					if(isValid) break;
					else {
						System.out.println("Invalid Password!");
						System.out.println("Password length should be between 8 and 50\n");
					}
				}
				
				int secQueNo;
				while(true) {
					System.out.println("Security questions ");
					System.out.println("1. In which city you were born?");
					System.out.println("2. What was your first pet's name?");
					System.out.println("3. What is the name of your favorite book?");
					System.out.println("4. Who is your favorite cartoon character?");
					System.out.print("Select security question: ");
					secQueNo = valService.validateChoice(scan.nextLine());
					
					if(secQueNo>0 && secQueNo<5) break;
					else System.out.println("\nPlease enter valid choice!\n");
				}
				
				String answer;
				while(true) {
					System.out.print("Your answer: ");
					answer = scan.nextLine();
					isValid = valService.validateAnswer(answer);
					if(isValid) break;
					else {
						System.out.println("\nInvalid answer!");
						System.out.println("Answer should not be empty and should contain only letters and numbers.");
						System.out.println("Maximum size allowed: 60 characters\n");
					}
				}
				
				userBean.setUserName(username);
				userBean.setUserPassword(password);
				userBean.setSecQueId(secQueNo);
				userBean.setSecQueAnswer(answer);
				
				int getId = authService.register(userBean);
				
				if(getId>0) {
					clearScreen();
					System.out.println("REGISTRATION SUCCESSFULL!\n");
					System.out.println("Your ID: "+getId+"\n");
					System.out.println("NOTE: Use this ID for Login!");
					
					while(true) {
						System.out.print("Enter 'CONFIRM' to proceed: ");
						if(scan.nextLine().equalsIgnoreCase("CONFIRM")) return;
					}
				}
				else System.out.println("\nRegistration Failed due to internal problem.\nPlease try again later...\n");
				sleep(3);
				return;
			}
			else if(validity == 1) {
				System.out.println("\nThis username already used! Please enter different username.\n");
			}
			else {
				System.out.println("\nUnable to proceed! please try again later...");
				sleep(2);
				clearScreen();
				return;
			}
		}
	}

	/**
	 * Method for forgot password
	 */
	private static void forgotPassword() {
		String userId;
		String answer;
		clearScreen();
		while(true) {
			System.out.println("FORGOT PASSWORD\n");
			while(true) {
				System.out.print("Enter user id: ");
				userId = scan.nextLine();
				boolean isValid = valService.validateUserId(userId);
				if(isValid) break;
				else System.out.println("\nInvalid ID! (ID should be 6 digit number)\n");
			}
			
			userBean = authService.getUser(userId);
			if(userBean.getUserId()<0) {
				System.out.println("\nUnable to proceed... Please try again later.\n"); //SQLException Handled
				sleep(2);
				clearScreen();
				return;
			}
			else if(userBean.getUserId()==0) {
				System.out.println("\nThis user doesn't exist!\n");
				
				String choice;
				while(true) {
					System.out.print("\nDo you want to continue? (y/n): ");
					choice = scan.nextLine();
					
					if(choice.equalsIgnoreCase("y")) break;
					else if(choice.equalsIgnoreCase("n")) return;
					else System.out.println("\nPlease enter valid choice!\n");
				}
				continue;
			}
			
			secQueBean = authService.searchSecQue(userId);
			
			if(secQueBean.getSecQueId()<=0) {
				System.out.println("\nUnable to proceed... Please try again later.\n"); //SQLException Handled
				sleep(2);
				clearScreen();
				return;
			}
			
			int attempts = 3;
			while(true) {
				if(attempts<3) System.out.println("Attempts remaining: "+attempts);
				
				System.out.println(secQueBean.getSecQuestion());
				System.out.print("Your answer: ");
				answer = scan.nextLine();
				
				if(answer.equals(userBean.getSecQueAnswer())) {
					Random random = new Random();
					int randomPassword = random.nextInt(999999);
					
					int result = authService.resetPassword(userBean.getUserId(),randomPassword);
					
					if(result==0) {
						System.out.println("\nYour password reset to: "+randomPassword);
						System.out.println("Please change password after next login!\n");
						while(true) {
							System.out.print("Enter 'CONFIRM' to proceed: ");
							if(scan.nextLine().equalsIgnoreCase("CONFIRM")) return;
						}
					}
					else System.out.println("\nUnable to proceed! Please try again later...");	//SQLException Handled
					sleep(3);
					return;
				}
				else {
					System.out.println("Incorrect answer!\n");
					attempts--;
				}
				
				if(attempts==0) {
					System.out.println("Maximum attempt reached!");
					sleep(2);
					return;
				}
			}
		}	
	}
	

	/**This method is to  display all the functions options and then user/admin will choose from those options
	 * @param userId User Id in integer
	 * @param password Password in string
	 * @param username User name in string
	 * @throws AppException
	 * 	
	 */
	private static void adminConsole(int userId,String password,String username) throws AppException {
		int choice;
		System.out.println("Welcome "+username+"!\n");
		
		while(true){
			
			System.out.println("1. Create Artist/Composer");
			System.out.println("2. Search Artist/Composer");
			System.out.println("3. Edit Artist/Composer");
			System.out.println("4. Delete Artist/Composer");
			System.out.println("5. Associate song to Artist/Composer");
			System.out.println("6. Search Artist/Composer song(s)");
			System.out.println("7. Delete Song");
			System.out.println("8. ACCOUNT SETTING");
			System.out.println("9. LOGOUT");
			System.out.println("10. EXIT");
			System.out.print("Enter your choice: ");
			
			choice = valService.validateChoice(scan.nextLine());
			
			switch(choice){
			case 1: createAC(userId,password); break;
			case 2: searchAC(); break;
			case 3: editAC(userId); break;	
			case 4: deleteAC(userId); break;
			case 5: songAssociate(userId); break;
			case 6: searchSong(userId); break;
			case 7: deleteSong(userId); break;
			case 8: 
					int result = accountSetting(userId,password);
					if(result==0) return;
					else if(result==1){password=newPass; break;}
					else break;
			case 9: attempt=3; return;
			case 10: exit();
			default: System.out.println("Please enter valid choice!\n");
			}
		}
	}
	
	/**Method for account setting
	 * @param userId User Id in integer
	 * @param password Password in string
	 * 
	 */
	private static int accountSetting(int userId, String password) {
		clearScreen();
		System.out.println("ACCOUNT SETTING\n");
		while(true) {
			System.out.println("1. Change Password");
			System.out.println("2. Update Security Question");
			System.out.println("3. Back");
			System.out.print("Enter your choice: ");
			
			int choice = valService.validateChoice(scan.nextLine());
			switch(choice) {
			case 1: return changePassword(userId,password);
			case 2: return updateSecurityQuestion(userId);
			case 3: clearScreen(); return 1;
			default: System.out.println("Please enter valid choice!\n");
			}
		}
	}
	/**
	 * @param userId User Id in integer
	 * @return Integer value:
	 * 		0 = Security question updated successfully
	 * 		1 = Unable to update security question
	 */
	private static int updateSecurityQuestion(int userId) {
		clearScreen();
		int queNo;
		String answer;
		System.out.println("UPDATE SECURITY QUESTION\n");
		
		while(true) {
			System.out.println("Select Question");
			System.out.println("1. In which city you were born?");
			System.out.println("2. What was your first pet's name?");
			System.out.println("3. What is the name of your favorite book?");
			System.out.println("4. Who is your favorite cartoon character?\n");
			System.out.print("Enter you choice: ");
			queNo = valService.validateChoice(scan.nextLine());
			if(queNo>0 && queNo<5) break;
			else System.out.println("\nPlease enter valid choice!\n");
		}
		
		while(true){
			System.out.print("Your answer: ");
			answer = scan.nextLine();
			boolean isValid = valService.validateName(answer);
			if(isValid) break;
			else System.out.println("\nPlease enter valid answer! (MinChar:3, MaxChar:50)\n");
		}
		
		int result = authService.updateSecQue(userId,queNo,answer);
		
		if(result==0) System.out.println("Security question updated successfully!\n");
		else System.out.println("Unable to update security. Please try again later...\n");
		
		sleep(3);
		return 2;
	}
	
	/** This method is for deleting song by using song name
	 * @param userId UserId in integer
	 */
	private static void deleteSong(int userId) {
		while(true) {
			clearScreen();
			String name;
			while(true){
				System.out.println("\nDELETE SONG");
				System.out.print("Enter Song Name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			SongBean getSong = adminService.searchSong(name);
			
			if(getSong!=null) {
				int result = adminService.deleteSong(getSong.getId(), userId);
				if(result==0) System.out.println("Song Deleted Successfully!");
				else System.out.println("Something went wrong! Please try again later...");
			}
			else System.out.println("\nNo Record Found!\n");
			
			while(true) {
				System.out.print("\nDo you want to continue? (y/n): ");
				String choice = scan.nextLine();
				
				if(choice.equalsIgnoreCase("y")) break;
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); return;}
				else System.out.println("\nPlease enter valid choice!\n");
			}
		}
	}
	

	/**This method is to delete Artist/Composer by choosing right option.
	 * @param userId UserId in integer
	 */
	private static void deleteAC(int userId) {
		int choice;
		clearScreen();
		System.out.println("DELETE ARTIST/COMPOSER\n");
		while(true) {
			System.out.println("1. Delete Artist");
			System.out.println("2. Delete Composer");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter your choice: ");
		
			choice = valService.validateChoice(scan.nextLine());
			
			switch(choice) {
			case 1: deleteArtist(userId); break;
			case 2: deleteComposer(userId); break;
			case 3: {clearScreen(); return;}
			case 4: exit();
			default: System.out.println("\nPlease enter valid choice!\n");
			}
		}
	}
	
	/**This method is to delete Composer.
	 * @param userId User Id in integer
	 */
	private static void deleteComposer(int userId) {
		while(true) {
			clearScreen();
			int choice1;
			String name,bornDate,diedDate;
			while(true){
				System.out.println("\nDELETE COMPOSER");
				System.out.print("Enter Composer name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ArrayList<ComposerBean> beanList = adminService.searchComposer(name);
			ComposerBean bean = new ComposerBean();
			if(beanList.isEmpty()) System.out.println("\nNo Record Found!\n");
			else if(beanList.size() == 1){
				for (ComposerBean composerBean : beanList) {
					bean = composerBean;
					if(bean.getBornDate() == null) bornDate="NA";
					else bornDate = bean.getBornDate().toString();
					if(bean.getDiedDate() == null) diedDate="NA";
					else diedDate = bean.getDiedDate().toString();
					System.out.println("Composer's Id: "+bean.getId());
					System.out.println("Composer's Name: "+bean.getName());
					System.out.println("Composer's Born Date: "+bornDate);
					System.out.println("Composer's Died Date: "+diedDate);
					System.out.println("Composer's CAE IPI Number: "+bean.getCaeipiNumber());
					System.out.println("Composer's Music Society Id: "+new String(bean.getMusicSocietyId()));	
				}
			}
			else{
				System.out.println("No record matched with this name\n");
				System.out.println("Related Searches:");
				int i =1;
				for (ComposerBean composerBean : beanList) {
					System.out.println(i+". "+composerBean.getName());
					i++;
				}
				
				while(true){
					System.out.print("Enter your choice: ");
					choice1 = valService.validateChoice(scan.nextLine());
					if(choice1 <= beanList.size() && choice1>0){
						bean = beanList.get(choice1-1);
						if(bean.getBornDate() == null) bornDate="NA";
						else bornDate = bean.getBornDate().toString();
						if(bean.getDiedDate() == null) diedDate="NA";
						else diedDate = bean.getDiedDate().toString();
						
						System.out.println("Composer's Id: "+bean.getId());
						System.out.println("Composer's Name: "+bean.getName());
						System.out.println("Composer's Born Date: "+bornDate);
						System.out.println("Composer's Died Date: "+diedDate);
						System.out.println("Composer's CAE IPI Number: "+bean.getCaeipiNumber());
						System.out.println("Composer's Music Society Id: "+new String(bean.getMusicSocietyId()));	
						break;
					}
					else System.out.println("Please Enter valid choice");
				}
			}
			
			System.out.print("\nDo you want to delete this Composer? (y/n): ");
			String choice = scan.nextLine();
			if(choice.equalsIgnoreCase("y")) {
				int result = adminService.deleteComposer(bean.getId(),userId);
				if(result==0) System.out.println("Composer Deleted successfully!");
				else System.out.println("\nSomething went wrong! Please try again later...");
			}
			else return;

			while(true) {
				System.out.print("\nDo you want to continue? (y/n): ");
				choice = scan.nextLine();
				
				if(choice.equalsIgnoreCase("y")) break;
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); return;}
				else System.out.println("\nPlease enter valid choice!\n");
			}
		}
		
	}

	/**This method is to delete Artist. 
	 * @param userId UserId in integer
	 */
	private static void deleteArtist(int userId) {
		while(true) {
			clearScreen();
			int choice1;
			String name,bornDate,diedDate;
			while(true){
				System.out.println("\nDELETE ARTIST");
				System.out.print("Enter Artist name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ArrayList<ArtistBean> beanList = adminService.searchArtist(name);
			ArtistBean bean = new ArtistBean();
			if(beanList.isEmpty()) System.out.println("\nNo Record Found!\n");
			else if(beanList.size() == 1){
				for (ArtistBean artistBean : beanList) {
					bean = artistBean;
					if(bean.getBornDate() == null) bornDate="NA";
					else bornDate = bean.getBornDate().toString();
					if(bean.getDiedDate() == null) diedDate="NA";
					else diedDate = bean.getDiedDate().toString();
					System.out.println("Artist's Id: "+bean.getId());
					System.out.println("Artist Name: "+bean.getName());
					System.out.println("Artist's Born Date: "+bornDate);
					System.out.println("Artist's Died Date: "+diedDate);	
				}
			}
			else{
				System.out.println("No record matched with this name\n");
				System.out.println("Related Searches:");
				int i =1;
				for (ArtistBean artistBean : beanList) {
					System.out.println(i+". "+artistBean.getName());
					i++;
				}
				
				while(true){
					System.out.print("Enter your choice: ");
					choice1 = valService.validateChoice(scan.nextLine());
					if(choice1 <= beanList.size() && choice1>0){
						bean = beanList.get(choice1-1);
						if(bean.getBornDate() == null) bornDate="NA";
						else bornDate = bean.getBornDate().toString();
						if(bean.getDiedDate() == null) diedDate="NA";
						else diedDate = bean.getDiedDate().toString();
						
						System.out.println("Artist's Id: "+bean.getId());
						System.out.println("Artist Name: "+bean.getName());
						System.out.println("Artist's Born Date: "+bornDate);
						System.out.println("Artist's Died Date: "+diedDate);	
						break;
					}
					else System.out.println("Please Enter valid choice");
				}
			}
			
			System.out.print("\nDo you want to delete this artist? (y/n): ");
			String choice = scan.nextLine();
			if(choice.equalsIgnoreCase("y")) {
				int result = adminService.deleteArtist(bean.getId(),userId);
				if(result==0) System.out.println("Artist Deleted successfully!");
				else System.out.println("\nSomething went wrong! Please try again later...");
			}
			else return;

			while(true) {
				System.out.print("\nDo you want to continue? (y/n): ");
				choice = scan.nextLine();
				
				if(choice.equalsIgnoreCase("y")) break;
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); return;}
				else System.out.println("\nPlease enter valid choice!\n");
			}
		}
	}

	/**
	 * @param userId User Id in integer
	 */
	private static void songAssociate(int userId) {
		int choice;
		clearScreen();
		System.out.println("ARTIST/COMPOSER SONG ASSOCIATION\n");
		while(true) {
			System.out.println("1. Associate songs to Artist");
			System.out.println("2. Associate songs to Composer");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter your choice: ");
		
			choice = valService.validateChoice(scan.nextLine());
			
			switch(choice) {
			case 1: artSongAssoc(userId); break;
			case 2: compSongAssoc(userId); break;
			case 3: {clearScreen(); return;}
			case 4: exit();
			default: System.out.println("\nPlease enter valid choice!\n");
			}
		}
	}

	/**
	 * @param userId User Id in integer
	 */
	private static void compSongAssoc(int userId) {
		while(true) {
			clearScreen();
			System.out.println("COMPOSER SONG ASSOCIATION\n");
			
			String songName, compName;
			LocalTime duration;
			int choice1;
			
			while(true){
				System.out.print("Song name: ");
				songName = scan.nextLine();
				boolean isValid = valService.validateName(songName);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			while(true){
				System.out.print("Duration: ");
				duration = valService.validateDuration(scan.nextLine());
				if(duration!=null) break;
				else System.out.println("\nPlease enter valid Duration! (mm:ss)\n");
			}
			songBean.setName(songName);
			songBean.setDuration(duration);
			songBean.setCreatedBy(userId);
			songBean.setUpdatedBy(userId);
			SongBean getSongBean = new SongBean();
			while(true){
				
				while(true){
					System.out.print("Enter Composer Name you want to associate with: ");
					compName = scan.nextLine();
					boolean isValid = valService.validateName(compName);
					if(isValid) break;
					else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
				}
				
				ArrayList<ComposerBean> beanList = adminService.searchComposer(compName);
				if(beanList.isEmpty()) System.out.println("\nComposer not present! Please try with others!\n");
				else if(beanList.size() == 1){
					for (ComposerBean compBean : beanList) {
						MCAMS.compBean = compBean;
						getSongBean = adminService.assocComposer(songBean, MCAMS.compBean, userId, false);
					}
					break;
				}
				else{
					System.out.println("No record matched with this name\n");
					System.out.println("Related Searches:");
					int i =1;
					for (ComposerBean compBean : beanList) {
						System.out.println(i+". "+compBean.getName());
						i++;
					}
					
					while(true){
						System.out.print("Enter your choice: ");
						choice1 = valService.validateChoice(scan.nextLine());
						if(choice1 <= beanList.size() && choice1>0){
							compBean = beanList.get(choice1-1);
							getSongBean = adminService.assocComposer(songBean, compBean, userId, false);
							break;
						}
						else System.out.println("Please Enter valid choice");
					}
					break;
				}
			}
						
				if(getSongBean.getId()<0) {
					if(getSongBean.getId()>-400000) {
						songBean.setId(Math.abs(getSongBean.getId()));
						while(true){
							System.out.print("\nThis song already present.\nDo you want to update it with this information? (y/n): ");
							String persist = scan.nextLine();
							
							if(persist.equalsIgnoreCase("y")){		
								songBean.setCreatedOn(getSongBean.getCreatedOn());
								songBean = adminService.assocComposer(songBean,compBean,userId,true);
								System.out.println("\nSong record submitted with id: "+songBean.getId()+"\n");
								break;
							}
							else if(persist.equalsIgnoreCase("n")) {
								System.out.println("\nOperation Cancelled!\n");
								break;
							}
							else System.out.println("\nPlease enter valid input! (Y or N)\n");
						}
					}
					else {
						songBean.setId(Math.abs(getSongBean.getId()+100000));
						songBean.setCreatedOn(LocalDate.now());
						songBean = adminService.assocComposer(songBean,compBean,userId,true);
						if(songBean.getId()==0) System.out.println("Something went wrong");
						else System.out.println("\nSong record submitted with id: "+songBean.getId()+"\n");
					}
				}
				else {
					System.out.println("\nSong record submitted with id: "+songBean.getId()+"\n");
					break;
				}
		
			boolean isContinue;
			while(true) {
				System.out.println("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}

	/**
	 * @param userId User Id in integer
	 */
	private static void artSongAssoc(int userId) {
		while(true) {
			clearScreen();
			System.out.println("ARTIST SONG ASSOCIATION\n");
			
			String songName, artName;
			LocalTime duration;
			int choice1;
			
			while(true){
				System.out.print("Song name: ");
				songName = scan.nextLine();
				boolean isValid = valService.validateName(songName);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			while(true){
				System.out.print("Duration: ");
				duration = valService.validateDuration(scan.nextLine());
				if(duration!=null) break;
				else System.out.println("\nPlease enter valid Duration! (mm:ss)\n");
			}
			songBean.setName(songName);
			songBean.setDuration(duration);
			songBean.setCreatedBy(userId);
			songBean.setUpdatedBy(userId);
			SongBean getSongBean = new SongBean();
			while(true){
				
				while(true){
					System.out.print("Enter Artist Name you want to associate with: ");
					artName = scan.nextLine();
					boolean isValid = valService.validateName(artName);
					if(isValid) break;
					else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
				}
				
				ArrayList<ArtistBean> beanList = adminService.searchArtist(artName);
				if(beanList.isEmpty()) System.out.println("\nArtist not present! Please try with others!\n");
				else if(beanList.size() == 1){
					for (ArtistBean artBean : beanList) {
						MCAMS.artBean = artBean;
						getSongBean = adminService.assocArtist(songBean, MCAMS.artBean, userId, false);
					}
					break;
				}
				else{
					System.out.println("No record matched with this name\n");
					System.out.println("Related Searches:");
					int i =1;
					for (ArtistBean artistBean : beanList) {
						System.out.println(i+". "+artistBean.getName());
						i++;
					}
					
					while(true){
						System.out.print("Enter your choice: ");
						choice1 = valService.validateChoice(scan.nextLine());
						if(choice1 <= beanList.size() && choice1>0){
							artBean = beanList.get(choice1-1);
							getSongBean = adminService.assocArtist(songBean, artBean, userId, false);
							break;
						}
						else System.out.println("Please Enter valid choice");
					}
					break;
				}
			}
						
				if(getSongBean.getId()<0) {
					if(getSongBean.getId()>-400000) {
						songBean.setId(Math.abs(getSongBean.getId()));
						while(true){
							System.out.print("\nThis song already present.\nDo you want to update it with this information? (y/n): ");
							String persist = scan.nextLine();
							
							if(persist.equalsIgnoreCase("y")){		
								songBean.setCreatedOn(getSongBean.getCreatedOn());
								songBean = adminService.assocArtist(songBean,artBean,userId,true);
								System.out.println("\nSong record submitted with id: "+songBean.getId()+"\n");
								break;
							}
							else if(persist.equalsIgnoreCase("n")) {
								System.out.println("\nOperation Cancelled!\n");
								break;
							}
							else System.out.println("\nPlease enter valid input! (Y or N)\n");
						}
					}
					else {
						songBean.setId(Math.abs(getSongBean.getId()+100000));
						songBean.setCreatedOn(LocalDate.now());
						songBean = adminService.assocArtist(songBean,artBean,userId,true);
						if(songBean.getId()==0) System.out.println("Something went wrong");
						else System.out.println("\nSong record submitted with id: "+songBean.getId()+"\n");
					}
				}
				else {
					System.out.println("\nSong record submitted with id: "+songBean.getId()+"\n");
					break;
				}
		
			boolean isContinue;
			while(true) {
				System.out.println("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}

	/**
	 * @param userId User Id in integer
	 * @param password Password in string
	 * @param username User name in string
	 */
	private static void clientConsole(int userId, String password, String username) {
		int choice;
		
		while(true) {
			System.out.println("Welcome user: "+username+"\n");
			System.out.println("1. Search Artist/Composer's songs");
			System.out.println("2. ACCOUNT SETTING");
			System.out.println("3. LOGOUT");
			System.out.println("4. EXIT");
			System.out.print("Enter your choice: ");
			
			choice = valService.validateChoice(scan.nextLine());
			
			switch(choice) {
			case 1: searchSong(userId); break;
			case 2: 
				int result = accountSetting(userId,password);
				if(result==0) return;
				else if(result==1){password=newPass; break;}
				else break;
			case 3: {attempt=3; return;}
			case 4: exit();
			default: System.out.println("\nPlease enter valid choice!\n");
			}
		}
	}
	
	/**
	 * @param userId User Id in integer
	 * @param password Password in string
	 * @throws AppException
	 */
	private static void createAC(int userId, String password) throws AppException {
		String choice;
		
		clearScreen();
		System.out.println("CREATE ARTIST/COMPOSER\n");
		
		while(true) {
			System.out.println("1. Create Artist");
			System.out.println("2. Create Composer");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter choice: ");
			choice = scan.nextLine();
					
			if(choice.equals("1")) createArtist(userId);
			else if(choice.equals("2")) createComposer(userId);
			else if(choice.equals("3")) {clearScreen(); return;}
			else if(choice.equals("4")) exit();
			else System.out.println("\nPlease enter valid choice!\n");
		}
	
	}



	/**
	 * @param userId User Id in integer
	 */
	private static void editAC(int userId) {
		String choice;
		
		clearScreen();
		System.out.println("EDIT COMPOSER/ARTIST\n");
		
		while(true) {
			System.out.println("1. Edit Artist");
			System.out.println("2. Edit Composer");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter choice: ");
			choice = scan.nextLine();
		
			if(choice.equals("1")) editArtist(userId);
			else if(choice.equals("2")) editComposer(userId);
			else if(choice.equals("3")) {clearScreen(); return;}
			else if(choice.equals("4")) exit();
			else System.out.println("\nPlease enter valid choice!\n");
		}
	}
	
	
	
	/**Method to edit composer details
	 * @param userId User Id in integer
	 */
	private static void editComposer(int userId) {
		while(true){
			clearScreen();
			String name, bornDate="", diedDate="", caeIpi;
			char[] mSocietyId;
			int choice1;
			LocalDate bDate, dDate;
			while(true){
				System.out.println("\nEDIT COMPOSER");
				System.out.print("Enter Composer name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ArrayList<ComposerBean> beanList = adminService.searchComposer(name);
			ComposerBean bean = new ComposerBean();
			if(beanList.isEmpty()) System.out.println("\nNo Record Found!\n");
			else if(beanList.size() == 1){
				System.out.println("******Existing Information*******");
				for (ComposerBean composerBean : beanList) {
					bean = composerBean;
					if(bean.getBornDate() == null) bornDate="NA";
					else bornDate = bean.getBornDate().toString();
					if(bean.getDiedDate() == null) diedDate="NA";
					else diedDate = bean.getDiedDate().toString();
					
					System.out.println("Composer's Id: "+bean.getId());
					System.out.println("Composer's Name: "+bean.getName());
					System.out.println("Composer's Born Date: "+bornDate);
					System.out.println("Composer's Died Date: "+diedDate);
					System.out.println("Composer's CAE IPI Number: "+bean.getCaeipiNumber());
					System.out.println("Composer's Music Society Id: "+new String(bean.getMusicSocietyId()));
					System.out.println("Created By: "+bean.getCreatedBy());
					System.out.println("Created On: "+bean.getCreatedOn());
					System.out.println("Updated By: "+bean.getUpdatedBy());
					System.out.println("Updated On: "+bean.getUpdatedOn());
				}
			}
			else{
				System.out.println("No record matched with this name\n");
				System.out.println("Related Searches:");
				int i =1;
				for (ComposerBean composerBean : beanList) {
					System.out.println(i+". "+composerBean.getName());
					i++;
				}
				
				while(true){
					System.out.print("Enter your choice: ");
					choice1 = valService.validateChoice(scan.nextLine());
					if(choice1 <= beanList.size() && choice1>0){
						bean = beanList.get(choice1-1);
						
						if(bean.getBornDate() == null) bornDate="NA";
						else bornDate = bean.getBornDate().toString();
						if(bean.getDiedDate() == null) diedDate="NA";
						else diedDate = bean.getDiedDate().toString();
						System.out.println("******Existing Information*******");
						System.out.println("Composer's Id: "+bean.getId());
						System.out.println("Composer's Name: "+bean.getName());
						System.out.println("Composer's Born Date: "+bornDate);
						System.out.println("Composer's Died Date: "+diedDate);
						System.out.println("Composer's CAE IPI Number: "+bean.getCaeipiNumber());
						System.out.println("Composer's Music Society Id: "+new String(bean.getMusicSocietyId()));
						System.out.println("Created By: "+bean.getCreatedBy());
						System.out.println("Created On: "+bean.getCreatedOn());
						System.out.println("Updated By: "+bean.getUpdatedBy());
						System.out.println("Updated On: "+bean.getUpdatedOn());
						break;
					}
					else System.out.println("Please Enter valid choice");
				}
			}

			System.out.println("Enter Details you want to update");
			while(true){
				System.out.print("Enter name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
		
			while(true){
				System.out.print("Enter Born Date (dd/mm/yyyy) (If not available then type 'NA'): ");
				String input = scan.nextLine();
			
				if(!input.equalsIgnoreCase("NA")){
					bDate = valService.validateDate(input);
					if(bDate!=null) break;
					else System.out.println("\nPlease enter valid date! (dd/mm/yyyy)\n");
				}
				else{
					bDate=null;
					break;
				}
			}
		
			while(true){
				System.out.print("Enter Died Date (mm/dd/yyyy) (If not available then type 'NA'): ");
				String input = scan.nextLine();
			
				if(!input.equalsIgnoreCase("NA")){
					dDate = valService.validateDate(input);
					if(bDate.isAfter(dDate)){
						System.out.println("\nDied date should be after Born date");
						continue;
					}
					if(dDate!=null) break;
					else System.out.println("\nPlease enter valid date! (dd/mm/yyyy)\n");
				}
				else{
					dDate=null;
					break;
				}
			}
			
			while(true){
				System.out.print("Enter CAE IPI number: ");
				caeIpi = scan.nextLine();
				boolean isValid = valService.validateCaeIpi(caeIpi);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
		
			while(true) {
				System.out.print("Enter music society ID: ");
				mSocietyId = scan.nextLine().toCharArray();
				boolean isValid = valService.validateMSocietyId(mSocietyId);
				if(isValid) break;
				else System.out.println("\nPlease enter valid input!\n");
			}
		
			compBean.setId(bean.getId());
			compBean.setName(name);
			compBean.setBornDate(bDate);
			compBean.setDiedDate(dDate);
			compBean.setCaeipiNumber(caeIpi);
			compBean.setMusicSocietyId(mSocietyId);
			compBean.setUpdatedBy(userId);
		
			bean = adminService.updateComposer(compBean);
			if(bean!=null) System.out.println("Composer's information updated successfully with id: "+bean.getId());
			else System.out.println("\nProblem occured while updating artist's information...\n");
			
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}

	

	/**Method to edit artist details
	 * @param userId User Id in integer
	 */
	private static void editArtist(int userId) {
		while(true){
			clearScreen();
			String name, bornDate="", diedDate="";
			int choice1;
			LocalDate bDate, dDate;
			while(true){
				System.out.println("\nEDIT ARTIST");
				System.out.print("Enter Artist name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ArrayList<ArtistBean> beanList = adminService.searchArtist(name);
			ArtistBean bean = new ArtistBean();
			if(beanList.isEmpty()) System.out.println("\nNo Record Found!\n");
			else if(beanList.size() == 1){
				System.out.println("******Existing Information*******");
				for (ArtistBean artistBean : beanList) {
					if(artistBean.getBornDate() == null) bornDate="NA";
					else bornDate = artistBean.getBornDate().toString();
					if(artistBean.getDiedDate() == null) diedDate="NA";
					else diedDate = artistBean.getDiedDate().toString();
					
					System.out.println("Artist's Id: "+artistBean.getId());
					System.out.println("Artist Name: "+artistBean.getName());
					System.out.println("Artist's Born Date: "+bornDate);
					System.out.println("Artist's Died Date: "+diedDate);
					System.out.println("Created By: "+artistBean.getCreatedBy());
					System.out.println("Created On: "+artistBean.getCreatedOn());
					System.out.println("Updated By: "+artistBean.getUpdatedBy());
					System.out.println("Updated On: "+artistBean.getUpdatedOn());
				}
			}
			else{
				System.out.println("No record matched with this name\n");
				System.out.println("Related Searches:");
				int i =1;
				for (ArtistBean artistBean : beanList) {
					System.out.println(i+". "+artistBean.getName());
					i++;
				}
				
				while(true){
					System.out.print("Enter your choice: ");
					choice1 = valService.validateChoice(scan.nextLine());
					if(choice1 <= beanList.size() && choice1>0){
						bean = beanList.get(choice1-1);
						
						if(bean.getBornDate() == null) bornDate="NA";
						else bornDate = bean.getBornDate().toString();
						if(bean.getDiedDate() == null) diedDate="NA";
						else diedDate = bean.getDiedDate().toString();
						System.out.println("******Existing Information*******");
						System.out.println("Artist's Id: "+bean.getId());
						System.out.println("Artist Name: "+bean.getName());
						System.out.println("Artist's Born Date: "+bornDate);
						System.out.println("Artist's Died Date: "+diedDate);
						System.out.println("Created By: "+bean.getCreatedBy());
						System.out.println("Created On: "+bean.getCreatedOn());
						System.out.println("Updated By: "+bean.getUpdatedBy());
						System.out.println("Updated On: "+bean.getUpdatedOn());
						break;
					}
					else System.out.println("Please Enter valid choice");
				}
			}

			System.out.println("Enter Details you want to update");
			while(true){
				System.out.print("Enter name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
		
			while(true){
				System.out.print("Enter Born Date (dd/mm/yyyy) (If not available then type 'NA'): ");
				String input = scan.nextLine();
			
				if(!input.equalsIgnoreCase("NA")){
					bDate = valService.validateDate(input);
					if(bDate!=null) break;
					else System.out.println("\nPlease enter valid date! (dd/mm/yyyy)\n");
				}
				else{
					bDate=null;
					break;
				}
			}
		
			while(true){
				System.out.print("Enter Died Date (mm/dd/yyyy) (If not available then type 'NA'): ");
				String input = scan.nextLine();
			
				if(!input.equalsIgnoreCase("NA")){
					dDate = valService.validateDate(input);
					if(bDate.isAfter(dDate)){
						System.out.println("\nDied date should be after Born date");
						continue;
					}
					if(dDate!=null) break;
					else System.out.println("\nPlease enter valid date! (dd/mm/yyyy)\n");
				}
				else{
					dDate=null;
					break;
				}
			}
		
			artBean.setId(bean.getId());
			artBean.setName(name);
			artBean.setBornDate(bDate);
			artBean.setDiedDate(dDate);
			artBean.setUpdatedBy(userId);
		
			bean = adminService.updateArtist(artBean);
			if(bean!=null) System.out.println("Artist's information updated successfully with id: "+bean.getId());
			else System.out.println("\nProblem occured while updating artist's information...\n");
			
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}

	/**
	 * Method to search artist/composer
	 */
	private static void searchAC() {
		clearScreen();
		System.out.println("SEARCH ARTIST/COMPOSER\n");
		
		String choice;
		while(true) {
			System.out.println("1. Search Artist");
			System.out.println("2. Search Composer");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter your choice: ");
			choice = scan.nextLine();
			
			if(choice.equals("1")) seachArtist();
			else if(choice.equals("2")) searchComposer();
			else if(choice.equals("3")) {clearScreen(); return;}
			else if(choice.equals("4")) exit();
			else System.out.println("\nPlease enter valid choice!\n");
		}
		
	}

	/**
	 * Method to search composer
	 */
	private static void searchComposer() {
		int choice1;
		while(true){
			clearScreen();
			String name;
			while(true){
				System.out.println("\nSEARCH COMPOSER");
				System.out.print("Enter Composer name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ArrayList<ComposerBean> beanList = adminService.searchComposer(name);
			String bornDate,diedDate;
			if(beanList.isEmpty()) System.out.println("\nNo Record Found!\n");
			else if(beanList.size() == 1){
				for (ComposerBean composerBean : beanList) {
					if(composerBean.getBornDate() == null) bornDate="NA";
					else bornDate = composerBean.getBornDate().toString();
					if(composerBean.getDiedDate() == null) diedDate="NA";
					else diedDate = composerBean.getDiedDate().toString();
					
					System.out.println("Composer's Id: "+composerBean.getId());
					System.out.println("Composer's Name: "+composerBean.getName());
					System.out.println("Composer's Born Date: "+bornDate);
					System.out.println("Composer's Died Date: "+diedDate);
					System.out.println("Composer's CAE IPI Number: "+composerBean.getCaeipiNumber());
					System.out.println("Composer's Music Society Id: "+new String(composerBean.getMusicSocietyId()));
					System.out.println("Created By: "+composerBean.getCreatedBy());
					System.out.println("Created On: "+composerBean.getCreatedOn());
					System.out.println("Updated By: "+composerBean.getUpdatedBy());
					System.out.println("Updated On: "+composerBean.getUpdatedOn());
				}
				
			}
			else{
				System.out.println("No record matched with this name\n");
				System.out.println("Related Searches:");
				int i =1;
				for (ComposerBean composerBean : beanList) {
					System.out.println(i+". "+composerBean.getName());
					i++;
				}
				
				while(true){
					System.out.print("Enter your choice: ");
					choice1 = valService.validateChoice(scan.nextLine());
					if(choice1 <= beanList.size() && choice1>0){
						ComposerBean bean = beanList.get(choice1-1);
						if(bean.getBornDate() == null) bornDate="NA";
						else bornDate = bean.getBornDate().toString();
						if(bean.getDiedDate() == null) diedDate="NA";
						else diedDate = bean.getDiedDate().toString();
						
						System.out.println("Composer's Id: "+bean.getId());
						System.out.println("Composer's Name: "+bean.getName());
						System.out.println("Composer's Born Date: "+bornDate);
						System.out.println("Composer's Died Date: "+diedDate);
						System.out.println("Composer's CAE IPI Number: "+bean.getCaeipiNumber());
						System.out.println("Composer's Music Society Id: "+new String(bean.getMusicSocietyId()));
						System.out.println("Created By: "+bean.getCreatedBy());
						System.out.println("Created On: "+bean.getCreatedOn());
						System.out.println("Updated By: "+bean.getUpdatedBy());
						System.out.println("Updated On: "+bean.getUpdatedOn());
						break;
					}
					else System.out.println("Please Enter valid choice");
				}
			}
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}

	/**
	 * Method to search artist
	 */
	private static void seachArtist() {
		int choice1;
		while(true){
			clearScreen();
			String name;
			while(true){
				System.out.println("\nSEARCH ARTIST");
				System.out.print("Enter Artist name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ArrayList<ArtistBean> beanList = adminService.searchArtist(name);
			String bornDate,diedDate;
			if(beanList.isEmpty()) System.out.println("\nNo Record Found!\n");
			else if(beanList.size() == 1){
				for (ArtistBean artistBean : beanList) {
					if(artistBean.getBornDate() == null) bornDate="NA";
					else bornDate = artistBean.getBornDate().toString();
					if(artistBean.getDiedDate() == null) diedDate="NA";
					else diedDate = artistBean.getDiedDate().toString();
					
					System.out.println("Artist's Id: "+artistBean.getId());
					System.out.println("Artist Name: "+artistBean.getName());
					System.out.println("Artist's Born Date: "+bornDate);
					System.out.println("Artist's Died Date: "+diedDate);
					System.out.println("Created By: "+artistBean.getCreatedBy());
					System.out.println("Created On: "+artistBean.getCreatedOn());
					System.out.println("Updated By: "+artistBean.getUpdatedBy());
					System.out.println("Updated On: "+artistBean.getUpdatedOn());
				}
				
			}
			else{
				System.out.println("No record matched with this name\n");
				System.out.println("Related Searches:");
				int i =1;
				for (ArtistBean artistBean : beanList) {
					System.out.println(i+". "+artistBean.getName());
					i++;
				}
				
				while(true){
					System.out.print("Enter your choice: ");
					choice1 = valService.validateChoice(scan.nextLine());
					if(choice1 <= beanList.size() && choice1>0){
						ArtistBean bean = beanList.get(choice1-1);
						if(bean.getBornDate() == null) bornDate="NA";
						else bornDate = bean.getBornDate().toString();
						if(bean.getDiedDate() == null) diedDate="NA";
						else diedDate = bean.getDiedDate().toString();
						
						System.out.println("Artist's Id: "+bean.getId());
						System.out.println("Artist Name: "+bean.getName());
						System.out.println("Artist's Born Date: "+bornDate);
						System.out.println("Artist's Died Date: "+diedDate);
						System.out.println("Created By: "+bean.getCreatedBy());
						System.out.println("Created On: "+bean.getCreatedOn());
						System.out.println("Updated By: "+bean.getUpdatedBy());
						System.out.println("Updated On: "+bean.getUpdatedOn());
						break;
					}
					else System.out.println("Please Enter valid choice");
				}
			}
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}

	/**Method to search artist/composer songs
	 * @param userId User Id in integer
	 */
	private static void searchSong(int userId) {
		clearScreen();
		System.out.println("SEARCH ARTIST/COMPOSER\n");
		
		String choice;
		while(true) {
			System.out.println("1. Search Artist's songs");
			System.out.println("2. Search Composer's songs");
			System.out.println("3. Back");
			System.out.println("4. Exit");
			System.out.print("Enter your choice: ");
			choice = scan.nextLine();
			
			if(choice.equals("1")) seachArtistSongs(userId);
			else if(choice.equals("2")) searchComposerSongs(userId);
			else if(choice.equals("3")) {clearScreen(); return;}
			else if(choice.equals("4")) exit();
			else System.out.println("\nPlease enter valid choice!\n");
		}
	}

	/**Method to create artist
	 * @param userId User Id in integer
	 */
	private static void createArtist(int userId) {
		
		String name;
		LocalDate bDate, dDate;
		
		while(true){
			clearScreen();
			System.out.println("CREATE ARTIST\n");
			
			while(true){
				System.out.print("Enter name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
		
			while(true){
				System.out.print("Enter Born Date (dd/mm/yyyy) (If not available then type 'NA'): ");
				String input = scan.nextLine();
			
				if(!input.equalsIgnoreCase("NA")){
					bDate = valService.validateDate(input);
					if(bDate!=null) break;
					else System.out.println("\nPlease enter valid date! (dd/mm/yyyy)\n");
				}
				else{
					bDate=null;
					break;
				}
			}
		
			while(true){
				System.out.print("Enter Died Date (mm/dd/yyyy) (If not available then type 'NA'): ");
				String input = scan.nextLine();
			
				if(!input.equalsIgnoreCase("NA")){
					dDate = valService.validateDate(input);
					if(dDate!=null){
						if(bDate.isAfter(dDate)){
							System.out.println("\nDied date should be after Born date");
							continue;
						}
						break;
					}
					else System.out.println("\nPlease enter valid date! (dd/mm/yyyy)\n");
				}
				else{
					dDate=null;
					break;
				}
			}
		
			artBean.setName(name);
			artBean.setBornDate(bDate);
			artBean.setDiedDate(dDate);
			artBean.setUpdatedBy(userId);
			artBean.setCreatedBy(userId);
		
			ArtistBean getBean = adminService.createArtist(artBean,false);
			
			if(getBean.getId()>0) System.out.println("\nArtist record submitted with id: "+getBean.getId()+"\n");
			
			else if(getBean.getId()<0){
				
				if(getBean.getId()>-500000) {
					artBean.setId(Math.abs(getBean.getId()));
					
					while(true){
						System.out.print("\nThis Artist already present.\nDo you want to update it with this information? (y/n): ");
						String persist = scan.nextLine();
						
						if(persist.equalsIgnoreCase("y")){						
							getBean = adminService.createArtist(artBean,true);
							System.out.println("\nArtist record submitted with id: "+getBean.getId()+"\n");
							break;
						}
						else if(persist.equalsIgnoreCase("n")) {
							System.out.println("\nOperation Cancelled!\n");
							break;
						}
						else System.out.println("\nPlease enter valid input! (Y or N)\n");
					}
				}
				else {
					artBean.setId(Math.abs(getBean.getId()+100000));
					getBean = adminService.createArtist(artBean,true);
					System.out.println("\nArtist record submitted with id: "+getBean.getId()+"\n");
				}
			}
			else System.out.println("Something went wrong... please try again later.");
			
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}	
	}
	
	/**Method to create composer
	 * @param userId User Id in integer
	 */
	private static void createComposer(int userId) {
		
		String name,caeIpi;
		LocalDate bDate, dDate;
		char[] mSocietyId;
		String mSocietyName=null;
		
		while(true){
			clearScreen();
			System.out.println("CREATE COMPOSER\n");
			
			while(true){
				System.out.print("Enter name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
		
			while(true){
				System.out.print("Enter Born Date (dd/mm/yyyy) (If not available then type 'NA'): ");
				String input = scan.nextLine();
			
				if(!input.equalsIgnoreCase("NA")){
					bDate = valService.validateDate(input);
					if(bDate!=null) break;
					else System.out.println("\nPlease enter valid date! (dd/mm/yyyy)\n");
				}
				else{
					bDate=null;
					break; 
				}
			}
		
			while(true){
				System.out.print("Enter Died Date (mm/dd/yyyy) (If not available then type 'NA'): ");
				String input = scan.nextLine();
			
				if(!input.equalsIgnoreCase("NA")){
					dDate = valService.validateDate(input);
					if(dDate!=null){
						if(bDate.isAfter(dDate)){
							System.out.println("\nDied date should be after Born date");
							continue;
						}
						break;
					}
					else System.out.println("\nPlease enter valid date! (dd/mm/yyyy)\n");
				}
				else{
					dDate=null;
					break;
				}
			}
			
			while(true){
				System.out.print("Enter CAE IPI number: ");
				caeIpi = scan.nextLine();
				boolean isValid = valService.validateCaeIpi(caeIpi);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
		
			while(true) {
				System.out.print("Enter music society ID: ");
				mSocietyId = scan.nextLine().toCharArray();
				boolean isValid = valService.validateMSocietyId(mSocietyId);
				if(isValid) break;
				else System.out.println("\nPlease enter valid input!\n");
			}
			
			if(!adminService.checkMSociety(new String(mSocietyId))) {
				System.out.print("Enter music society name: ");
				mSocietyName = scan.nextLine();
			}
			
			compBean.setName(name);
			compBean.setBornDate(bDate);
			compBean.setDiedDate(dDate);
			compBean.setCaeipiNumber(caeIpi);
			compBean.setMusicSocietyId(mSocietyId);
			compBean.setUpdatedBy(userId);
			compBean.setCreatedBy(userId);
			
			int genId = adminService.createComposer(compBean,false,mSocietyName);
			
			if(genId>0) System.out.println("\nComposer record submitted with id: "+genId+"\n");
			else if(genId<0){
				if(genId>-300000) {
					compBean.setId(Math.abs(genId));
					while(true){
						System.out.print("\nThis Composer already present.\nDo you want to update it with this information? (y/n): ");
						String persist = scan.nextLine();
						
						if(persist.equalsIgnoreCase("y")){
							genId = adminService.createComposer(compBean,true,mSocietyName);
							System.out.println("\nComposer record submitted with id: "+genId+"\n");
							break;
						}
						else if(persist.equalsIgnoreCase("n")) {
							System.out.println("\nOperation Cancelled!\n");
							break;
						}
						else System.out.println("\nPlease enter valid input! (Y or N)\n");
					}
				// TODO 2sec thread sleep
				}
				else {
					compBean.setId(Math.abs(genId+100000));
					genId = adminService.createComposer(compBean,true,mSocietyName);
					System.out.println("\nComposer record submitted with id: "+genId+"\n");
				}
			}
			else System.out.println("Something went wrong... please try again later.");
			
			boolean isContinue;
			while(true) {
				System.out.print("Do you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}
	
	/**Method to search artist songs
	 * @param userId User Id in integer
	 */
	private static void seachArtistSongs(int userId) {
		String name, adminFormat = "%-2s %-8s %2s %-35s %2s %-8s %2s %-10s %2s %-10s %2s %-10s %2s %-10s %2s", userFormat = "%-2s %-6s %2s %-30s %2s %-8s %2s";
		int choice1;
		ArrayList<SongBean> songList;
		clearScreen();
		System.out.println("SEARCH ARTIST SONGS\n");
		while(true) {
			
			while(true){
				System.out.print("Enter name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ArrayList<ArtistBean> beanList = adminService.searchArtist(name);
			if(beanList.isEmpty()) System.out.println("\nArtist not found!\n");
			else if(beanList.size() == 1){
				for (ArtistBean artistBean : beanList) {
					artBean = artistBean;
				}
			}
			else{
				System.out.println("No record matched with this name\n");
				System.out.println("Related Searches:");
				int i =1;
				for (ArtistBean artistBean : beanList) {
					System.out.println(i+". "+artistBean.getName());
					i++;
				}
				while(true){
					System.out.print("Enter your choice: ");
					choice1 = valService.validateChoice(scan.nextLine());
					if(choice1 <= beanList.size() && choice1>0){
						artBean = beanList.get(choice1-1);
						break;
					}
					else System.out.println("Please Enter valid choice");
				}
			}
			
			if(!beanList.isEmpty()){
				if(userId==100000 || userId==100001) songList=adminService.searchArtistSong(artBean.getName());
				else songList=userService.searchArtistSong(artBean.getName());
				
				if(songList.isEmpty()) System.out.println("\nNo record found!\n");
				else{
					System.out.println("\nSongs associated to "+artBean.getName()+":\n");
					int i=1;
					if(userId==100000 || userId==100001) {
						System.out.println("-------------------------------------------------------------------------------------------------------------------------");
						System.out.printf(adminFormat, "|", "Song_Id","|", "Song_Name","|", "Duration","|", "Created_By","|", "Created_On","|", "Updated_By","|", "Updated_On","|");
						System.out.println("\n-------------------------------------------------------------------------------------------------------------------------");
					}
					else {
						System.out.println("----------------------------------------------------------");
						System.out.printf(userFormat, "|", "Sr.No.","|", "Song_Name","|", "Duration", "|");
						System.out.println("\n----------------------------------------------------------");
					}
					for (SongBean songBean : songList) {
						String duration;
						if(songBean.getDuration().getSecond()<10) duration=songBean.getDuration().getMinute()+":0"+songBean.getDuration().getSecond();
						else duration=songBean.getDuration().getMinute()+":"+songBean.getDuration().getSecond();
						if(userId==100000 || userId==100001){
							System.out.printf(adminFormat,"|", songBean.getId(),"|", songBean.getName(),"|", duration,"|", songBean.getCreatedBy(),"|", songBean.getCreatedOn(),"|", songBean.getUpdatedBy(),"|", songBean.getUpdatedOn(),"|");
							System.out.println();
						}
						else {
							System.out.printf(userFormat,"|", i+")","|", songBean.getName(),"|", duration,"|");
							System.out.println();
							i++;
						}
					}
					if(userId==100000 || userId==100001)
						System.out.println("-------------------------------------------------------------------------------------------------------------------------");
					else
						System.out.println("----------------------------------------------------------");
				}
			}
			
			boolean isContinue;
			while(true) {
				System.out.print("\nDo you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}

	/**Method to search composer songs
	 * @param userId User Id in integer
	 */
	private static void searchComposerSongs(int userId) {
		String name, adminFormat = "%-2s %-8s %2s %-35s %2s %-8s %2s %-10s %2s %-10s %2s %-10s %2s %-10s %2s", userFormat = "%-2s %-6s %2s %-30s %2s %-8s %2s";
		int choice1;
		ArrayList<SongBean> songList;
		
		while(true) {
			clearScreen();
			System.out.println("SEARCH COMPOSER SONGS\n");
			
			while(true){
				System.out.print("Enter Composer name: ");
				name = scan.nextLine();
				boolean isValid = valService.validateName(name);
				if(isValid) break;
				else System.out.println("\nPlease enter valid name! (MinChar:3, MaxChar:50)\n");
			}
			
			ArrayList<ComposerBean> beanList = adminService.searchComposer(name);
			if(beanList.isEmpty()) System.out.println("\nComposer not found!\n");
			else if(beanList.size() == 1){
				for (ComposerBean composerBean : beanList) {
					compBean = composerBean;
				}
			}
			else{
				System.out.println("No record matched with this name\n");
				System.out.println("Related Searches:");
				int i =1;
				for (ComposerBean composerBean : beanList) {
					System.out.println(i+". "+composerBean.getName());
					i++;
				}
				while(true){
					System.out.print("Enter your choice: ");
					choice1 = valService.validateChoice(scan.nextLine());
					if(choice1 <= beanList.size() && choice1>0){
						compBean = beanList.get(choice1-1);
						break;
					}
					else System.out.println("Please Enter valid choice");
				}
			}
			
			if(!beanList.isEmpty()){
				if(userId==100000 || userId==100001) songList=adminService.searchComposerSong(compBean.getName());
				else songList=userService.searchComposerSong(compBean.getName());
				
				if(songList.isEmpty()) System.out.println("\nNo record found!\n");
				else{
					System.out.println("\nSongs associated to "+compBean.getName()+":\n");
					int i=1;
					if(userId==100000 || userId==100001) {
						System.out.println("-------------------------------------------------------------------------------------------------------------------------");
						System.out.printf(adminFormat,"|", "Song_Id","|", "Song_Name","|", "Duration","|", "Created_By","|", "Created_On","|", "Updated_By","|", "Updated_On", "|");
						System.out.println("\n-------------------------------------------------------------------------------------------------------------------------");
					}
					else {
						System.out.println("----------------------------------------------------------");
						System.out.printf(userFormat, "|", "Sr.No.", "|", "Song_Name", "|", "Duration", "|");
						System.out.println("\n----------------------------------------------------------");
					}
					for (SongBean songBean : songList) {
						String duration;
						if(songBean.getDuration().getSecond()<10) duration=songBean.getDuration().getMinute()+":0"+songBean.getDuration().getSecond();
						else duration=songBean.getDuration().getMinute()+":"+songBean.getDuration().getSecond();
						if(userId==100000 || userId==100001){
							System.out.printf(adminFormat,"|", songBean.getId(),"|", songBean.getName(),"|", duration,"|", songBean.getCreatedBy(),"|", songBean.getCreatedOn(),"|", songBean.getUpdatedBy(),"|", songBean.getUpdatedOn(), "|");
							System.out.println();
						}
						else {
							System.out.printf(userFormat, "|", i+")", "|", songBean.getName(), "|", duration, "|");
							System.out.println();
							i++;
						}
					}
					if(userId==100000 || userId==100001)
						System.out.println("-------------------------------------------------------------------------------------------------------------------------");
					else
						System.out.println("----------------------------------------------------------");
				}
			}
			
			boolean isContinue;
			while(true) {
				System.out.print("\nDo you want to continue? (y/n): ");
				String choice = scan.nextLine();
			
				if(choice.equalsIgnoreCase("y")) {clearScreen(); isContinue=true; break;}
				else if(choice.equalsIgnoreCase("n")) {clearScreen(); isContinue=false; break;}
				else System.out.println("\nPlease enter valid choice!");
			}
			
			if(!isContinue) break;
		}
	}
	
	/**
	 * @param userId User Id in integer
	 * @param password Password in string
	 * @return integer value:
	 * 			0 = password changed
	 * 			1 = password not changed due to SQLException
	 */
	private static int changePassword(int userId, String password) {
		clearScreen();
		String oldPassword;
		String newPassword;
		String confirmPassword;
		System.out.println("CHANGE PASSWORD\n");
		while(true) {
			
			System.out.print("Old Password: ");
			if(console!=null) oldPassword = new String(console.readPassword());
			else oldPassword = scan.nextLine();
			if(!oldPassword.equals(password)) {
				System.out.println("Password Incorrect\n");
				continue;
			}
 			while(true) {
				System.out.print("New Password: ");
				if(console!=null) newPassword = new String(console.readPassword());
				else newPassword = scan.nextLine();
				System.out.print("Confirm Password: ");
				if(console!=null) confirmPassword = new String(console.readPassword());
				else confirmPassword = scan.nextLine();
 				if(!newPassword.matches(confirmPassword)){
					System.out.println("\nPassword mismatch!\n");
					continue;
				}
				break;
			}

			/**
			 * result=0  => password changed
			 * result=1  => password not changed due to SQLException
			 */
			int result;
			if(userId==100000 || userId==100001) result=adminService.changePassword(userId,newPassword);
			else result=userService.changePassword(userId,newPassword);
			
			if(result==0) {
				System.out.println("\nPassword changed!\n");
				
				String choice;
				while(true) {
					System.out.print("LOGOUT NOW? (y/n): ");
					choice = scan.nextLine();
					
					if(choice.equals("y")) {clearScreen(); return 0;}
					else if(choice.equals("n")) {clearScreen(); newPass=newPassword; return 1;}
					else System.out.println("\nPlease enter valid choice!\n");
				}
			}
			else {
				System.out.println("Unable to change password! Please try again...");
				while(true) {
					String choice;
					System.out.println("Do you want to continue? (y/n): ");
					choice = scan.nextLine();
					
					if(choice.equals("y")) {clearScreen(); break;}
					if(choice.equals("n")) {clearScreen(); return 1;}
					else System.out.println("\nPlease enter valid choice!\n");
				}
			}
			
			
			while(true) {
				String choice;
				System.out.println("Do you want to continue? (y/n): ");
				choice = scan.nextLine();
				
				if(choice.equals("y")) {clearScreen(); break;}
				if(choice.equals("n")) {clearScreen(); return 1;}
				else System.out.println("\nPlease enter valid choice!\n");
			}
		}
	}
	
	/**
	 * Method to clear screen
	 */
	public static void clearScreen() {
		if(console != null)
			try {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} catch (Exception e) {
				//e.printStackTrace();
			}
	}
	

	/**
	 * @param i Specifies seconds in integer
	 */
	private static void sleep(int i) {
		try {
			Thread.sleep(i*1000);
			return;
		} catch (InterruptedException e) {
			return;
		}	
	}
	
	/**
	 *Method to exit program 
	 */
	public static void exit() {
		System.out.println("\n<-PROGRAM TERMINATED->");
		System.exit(0);		
	}

}
