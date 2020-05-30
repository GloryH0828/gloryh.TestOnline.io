package com.hgh.service;

import java.util.List;
import java.util.Map;

import com.hgh.dao.SecDao;
import com.hgh.dao.UserDao;
import com.hgh.domain.Class1;
import com.hgh.domain.Course;
import com.hgh.domain.Page;
import com.hgh.domain.User;

public class SecService {
	SecDao secDao=new  SecDao();
	
	public void addclass(Class1 class1) {
		// TODO 自动生成的方法存根
		
		secDao.addclass(class1);
	}

	public Page find(String depart, String index, int currentPage) {
		//查询数据库，获取总条数
		int total=   secDao.findCount(depart,index);
		//构造Page对象
		Page page=new Page(currentPage, total);
		//调用Dao获取列表
		List<Map<String, Object>> list=secDao.find(depart,index,page.getStartIndex(),page.getPageSize());
		page.setList(list);
		//将获取的列表设置给Page的list属性
		System.out.println(page);
		return page;
		
	}

	public static  List<Map<String, Object>> findByID(String id, String key) {
		List<Map<String, Object>> list =SecDao.findById(id,key);
		return list;
	
		
	}

	public void updateclass(Class1 class1, String key) {
		secDao.updateclass(class1,key);
		
	}

	public void delete(int id, String key) {
		secDao.delete(id,key);
	}

	public void addteacher(User user) {
		// TODO 自动生成的方法存根
		secDao.addteacher(user);
	}

	public void updateteacher(User user, String key) {
		// TODO 自动生成的方法存根
		secDao.updateuser(user ,key);
	}
	public void updatestudent(User user, String key) {
		// TODO 自动生成的方法存根
		secDao.updateuser(user ,key);
	}

	public Page findteacher(String depart, String index, int currentPage) {
		//查询数据库，获取总条数
				int total=   secDao.findCountteacher(depart, index);
				//构造Page对象
				Page page=new Page(currentPage, total);
				//调用Dao获取列表
				List<Map<String, Object>> list=secDao.findteacher(depart,index,page.getStartIndex(),page.getPageSize());
				System.out.println(page);
				page.setList(list);
				//将获取的列表设置给Page的list属性
				System.out.println(page);
				return page;
	}

	public void resetpassword(String key, int id) {
		// TODO 自动生成的方法存根
		secDao.resetpassword(key,id);
		
	}

	public void addstudent(User user) {
		// TODO 自动生成的方法存根
		secDao.addstudent(user);
	}

	public Page findstudent(String depart, String index, int current) {
		//查询数据库，获取总条数
		int total=   secDao.findCountstudent(depart, index);
		//构造Page对象
		Page page=new Page(current, total);
		//调用Dao获取列表
		List<Map<String, Object>> list=secDao.findstudent(depart,index,page.getStartIndex(),page.getPageSize());
		page.setList(list);
		//将获取的列表设置给Page的list属性
		System.out.println(page);
		return page;
	}

	public void addcourse(Course course) {
		// TODO 自动生成的方法存根
		secDao.addcourse(course);
	}

	public List<Map<String, Object>> findcourseid(String coursename, String courseteacher) {
		List<Map<String, Object>> list=secDao.findcourseid(coursename,courseteacher);
		
		System.out.println(list);
		return list;
		
	}

	public List<Map<String, Object>> findteacherbyteachername(String courseteacher) {
		
		List<Map<String, Object>> flag=secDao.findbyteachername(courseteacher);
		return flag;
	}

	public List<Map<String, Object>> findcoursebyid(int id) {
		List<Map<String, Object>> flag=secDao.findcoursebyid(id);
		return flag;
	}

	public Page findcourse(int depart, String index, int current) {
		//查询数据库，获取总条数
				int total=   secDao.findCountcourse(depart, index);
				//构造Page对象
				Page page=new Page(current, total);
				//调用Dao获取列表
				List<Map<String, Object>> list=secDao.findcourse(depart,index,page.getStartIndex(),page.getPageSize());
				page.setList(list);
				//将获取的列表设置给Page的list属性
				System.out.println(page);
				return page;
	}

	public void deletecourse(int id, String key) {
		secDao.deletecourse(id,key);
		
	}

	public void deleteteacher(int id, String key) {
		secDao.deleteteacher(id,key);
		
	}

	public int selectpeople(int id) {
		int people=secDao.selectpeople(id);
		return people;
		
	}

	public void deletestudent(int id, String key) {
		secDao.deleteteacher(id,key);
		
	}

	public List<User> findAllTeacher() {
		return secDao.findAllTeacher();
	}

	public List<User> exportStudent(String classname) {
		return secDao.exportStudent(classname);
	}

	public List<Course> findAllCourse() {
		return secDao.findAllCourse();
	}

	

	

}
