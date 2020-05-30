package com.hgh.service;

import java.util.List;
import java.util.Map;

import com.hgh.dao.UserDao;
import com.hgh.domain.User;

public class UserService {
UserDao userDao =new UserDao();
public void regist(User user,String role) {
	userDao.insertUser(user, role);
}
public boolean login(User user, String role) {
	//用户名和密码正确，返回true，用户名或密码不正确，返回false
			/*
			 * 1.调用Dao层中的findByUsername方法
			 *   返回一个User对象
			 * 2. User对象是否为空，用户名不存在，返回false
			 * 3. User对象不为空，判断密码是否正确
			 *    3.1 比较密码是否正确，正确，返回true
			 *    3.2 密码不正确，返回false
			 */
	boolean flag=false;
	List<Map<String, Object>> list=userDao.login(user,role);
	if(list!=null&&!list.isEmpty()){
		flag = true;
		System.out.println("登陆成功");
	}
	return flag;
	
}
public boolean state(User user,String role) {
	User user1=userDao.state(user,role);
	 int state =user1.getState();
	 System.out.println(state);
	 boolean flag=true;
		
		if (state!=0) {
			flag =false;
		}
		return flag;
}
public String getname(User user,String role) {
	// TODO 自动生成的方法存根
	return userDao.state(user,role).getName();
	
}
}
