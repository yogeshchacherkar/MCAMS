package com.mcams.dao;

import com.mcams.bean.AuthenticationBean;
import com.mcams.bean.UserBean;
import com.mcams.exception.AppException;

/**
 * This interface contains methods related to credentials
 *
 */
public interface IAuthenticationDAO {
	
	
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

	/**Method to get user
	 * @param userId User Id string
	 * @return Object of UserBean class
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
}
