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
	//�û�����������ȷ������true���û��������벻��ȷ������false
			/*
			 * 1.����Dao���е�findByUsername����
			 *   ����һ��User����
			 * 2. User�����Ƿ�Ϊ�գ��û��������ڣ�����false
			 * 3. User����Ϊ�գ��ж������Ƿ���ȷ
			 *    3.1 �Ƚ������Ƿ���ȷ����ȷ������true
			 *    3.2 ���벻��ȷ������false
			 */
	boolean flag=false;
	List<Map<String, Object>> list=userDao.login(user,role);
	if(list!=null&&!list.isEmpty()){
		flag = true;
		System.out.println("��½�ɹ�");
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
	// TODO �Զ����ɵķ������
	return userDao.state(user,role).getName();
	
}
}
