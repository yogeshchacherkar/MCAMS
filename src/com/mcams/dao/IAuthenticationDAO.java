package com.mcams.dao;

import com.mcams.bean.AuthenticationBean;
import com.mcams.bean.UserBean;
import com.mcams.exception.AppException;

public interface IAuthenticationDAO {
	public UserBean checkCredentials(AuthenticationBean bean) throws AppException;

	int updateSecQue(int userId, int queNo, String answer);

	UserBean getUser(String userId);

	int resetPassword(int userId, int randomPassword);
}
