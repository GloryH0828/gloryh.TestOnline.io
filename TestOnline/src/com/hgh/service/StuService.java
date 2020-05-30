package com.hgh.service;

import java.util.List;
import java.util.Map;

import javax.enterprise.inject.New;

import com.hgh.dao.StuDao;
import com.hgh.domain.Page;
import com.hgh.domain.Question;

public class StuService {

	StuDao stuDao=new StuDao();

	public List<Question> findAllQuestion(String coursename) {
		 return stuDao.findAllCourse(coursename);
	}

	public String findclass(String username) {
		// TODO 自动生成的方法存根
		return stuDao.findclass(username);
	}

	public Page findtest(String coursename, String type, String action) {
		Page page=new Page(0, 0);
		
		//调用Dao获取列表
		List<Map<String, Object>> list=stuDao.findtest(coursename,type,action);
		
		page.setList(list);
		//将获取的列表设置给Page的list属性
		System.out.println(page);
	
	
	return page;
	}
}
