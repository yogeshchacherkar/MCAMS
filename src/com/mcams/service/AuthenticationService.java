package com.mcams.service;

import com.mcams.bean.AuthenticationBean;
import com.mcams.bean.SecQueBean;
import com.mcams.bean.UserBean;
import com.mcams.dao.AuthenticationDAO;
import com.mcams.exception.AppException;

public class AuthenticationService implements IAuthenticationService {
	AuthenticationDAO dao = new AuthenticationDAO();
	
	@Override
	public int checkCredentials(AuthenticationBean bean) throws AppException {
		return dao.checkCredentials(bean);
	}

	@Override
	public int updateSecQue(int userId, int queNo, String answer) {
		return dao.updateSecQue(userId,queNo,answer);
	}

	@Override
	public SecQueBean searchSecQue(String userId) {
		return dao.searchSecQue(userId);
	}

	@Override
	public UserBean getUser(String userId) {
		return dao.getUser(userId);
	}

	@Override
	public int resetPassword(int userId, int randomPassword) {
		return dao.resetPassword(userId,randomPassword);
	}

	@Override
	public int checkUser(String username) {
		return dao.checkUser(username);
	}

	@Override
	public int register(UserBean userBean) {
		return dao.register(userBean);
	}

}
