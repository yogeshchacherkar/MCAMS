package com.mcams.service;

import com.mcams.bean.AuthenticationBean;
import com.mcams.bean.SecQueBean;
import com.mcams.bean.UserBean;
import com.mcams.exception.AppException;
/**
 * This interface contains method for checking credentials
 *
 */

public interface IAuthenticationService {
	
	/**Method to check credentials
	 * @param bean Object of AuthenticationBean class 
	 * @return integer result:
				  0 = Correct credentials
				  1 = User record found but password not matched
				 -1 = User record not present in database
	 * @throws AppException User defined exception for methods
	 */
	public UserBean checkCredentials(AuthenticationBean bean) throws AppException;

	/**Method to update security question
	 * @param userId User Id in integer
	 * @param queNo Question number in integer
	 * @param answer Answer in string
	 * @return integer value:
	 * 			0 = Security question updated
	 * 			1 = Exception occurs	
	 */
	int updateSecQue(int userId, int queNo, String answer);

	
	/**Method to search security question
	 * @param userId User Id in string
	 * @return secQueBean Object of SecQueBean class 
	 */
	SecQueBean searchSecQue(String userId);

	/**Method to get user
	 * @param userId User Id in string 
	 * @return userBean Object of UserBean class 
	 */
	UserBean getUser(String userId);

	/**Method to reset password
	 * @param userId User Id in integer
	 * @param randomPassword Random password in integer
	 * @return integer value:
	 * 		0 = User's Password reset
	 * 		1 = Exception occurs
	 */
	int resetPassword(int userId, int randomPassword);

	/**Method to check user
	 * @param username User name in string
	 * @return integer value:
	 * 		0 = User is present 
	 * 		1 = User is not present 
	 * 	   -1 = Exception occurs
	 * 		
	 */
	int checkUser(String username);

	/**Method for registration
	 * @param userBean Object of UserBean class
	 * @return Integer value:
	 * 		1 = User Id in integer
	 * 		0 = Exception occurs		
	 */
	int register(UserBean userBean);
}
