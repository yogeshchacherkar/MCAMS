package com.mcams.service;

import com.mcams.bean.AuthenticationBean;
import com.mcams.bean.SecQueBean;
import com.mcams.bean.UserBean;
import com.mcams.exception.AppException;

public interface IAuthenticationService {
	public UserBean checkCredentials(AuthenticationBean bean) throws AppException;

	int updateSecQue(int userId, int queNo, String answer);

	SecQueBean searchSecQue(String userId);

	UserBean getUser(String userId);

	int resetPassword(int userId, int randomPassword);

	int checkUser(String username);

	int register(UserBean userBean);
}
