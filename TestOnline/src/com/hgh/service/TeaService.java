package com.hgh.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.hgh.dao.TeaDao;
import com.hgh.domain.Course;
import com.hgh.domain.Page;
import com.hgh.domain.Question;

public class TeaService {
 TeaDao teaDao=new TeaDao();

public void addmycourse(int id, String username) {
	teaDao.addmycourse(id,username);
}
public Page findmycourse(String username, int current) {
	//��ѯ���ݿ⣬��ȡ������
	int total=   teaDao.findmycourse(username);
	//����Page����
	Page page=new Page(current, total);
	//����Dao��ȡ�б�
	List<Map<String, Object>> list=teaDao.findmycourse(username,page.getStartIndex(),page.getPageSize());
	page.setList(list);
	//����ȡ���б����ø�Page��list����
	System.out.println(page);
	return page;
}
public Course toaddcourseclass(int id) {
	return teaDao.toaddcourseclass(id);
}
public String findcourseclass(String coursename) {
	// TODO �Զ����ɵķ������
	return teaDao.findcourseclass(coursename);
}
public void addcourseclass(String coursename, String courseclass) {
	teaDao.addcourseclass(coursename,courseclass);
}
public void deletecourseclass(String coursename) {
	teaDao.deletecourseclass(coursename);
}
public void addquestion(Question question) {
	teaDao.addquestion(question);
	
}
public Page findquestion(String coursename, String type) {
	Page page=new Page(0, 0);
	
		//����Dao��ȡ�б�
		List<Map<String, Object>> list=teaDao.findquestion(coursename,type);
		
		page.setList(list);
		//����ȡ���б����ø�Page��list����
		System.out.println(page);
	
	
	return page;
}
public void deletequestion(String id) {
	teaDao.deletequestion(id);
	
}
public void addtotest(int id, String type) {
	if ("1".equals(type)) {
		Question question= teaDao.findbyselectid(id);
		
		teaDao.addtotest(question);
	}else {
		Question question= teaDao.findbyid(id);
		
		teaDao.addtotest(question);
	}
	
	
}
public int countquestion(String string) {
	return teaDao.countquestion(string);
}
public void empty() {
	teaDao.empty();
	
}
public int[] questionid(int count, String string) {
	return teaDao.questionid(count,string);
}
public void addrandom(int id) {
	Question question =teaDao.findbyid(id);
	teaDao.addrandom(question);
}
public int countrandom() {
	return teaDao.countrandom();
}
public int[] randomquestionid(int count) {
	// TODO �Զ����ɵķ������
	return teaDao.randomquestionid(count);
}
public Page randomid()  {
	Page page=new Page(0, 0);
	//����Dao��ȡ�б�
	List<Map<String, Object>> list=teaDao.randomid(1);
	
	page.setList(list);
	//����ȡ���б����ø�Page��list����
	System.out.println(page);
	return page;
}
public Page findrandom() {
	Page page=new Page(0, 0);
	//����Dao��ȡ�б�
	List<Map<String, Object>> list=teaDao.randomid(0);
	
	page.setList(list);
	//����ȡ���б����ø�Page��list����
	System.out.println(page);
	return page;
}
public void addselectquestion(Question question) {
	// TODO �Զ����ɵķ������
	teaDao.addselectquestion(question);
}
public void deletetestbyhand(String id, int i) {
	teaDao.deletetestbyhand(id,i);
}
public int countselect() {
	// TODO �Զ����ɵķ������
	return teaDao.countselect();
}
public int[] selectid(int select) {
	// TODO �Զ����ɵķ������
	return teaDao.questionid(select);
}
public void addrandomsel(int i) {
	Question question =teaDao.findbyselectid(i);
	teaDao.addrandom(question);
}
public int countbyhand(String type) {
	// TODO �Զ����ɵķ������
	return TeaDao.conutbyhand(type);
}
}
