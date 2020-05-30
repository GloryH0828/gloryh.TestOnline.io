package com.hgh.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.New;
import javax.imageio.spi.RegisterableService;
import javax.json.JsonObject;
import javax.mail.Address;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.tree.DefaultTreeCellEditor.EditorContainer;

import com.hgh.domain.Course;
import com.hgh.domain.Page;
import com.hgh.domain.Question;
import com.hgh.domain.User;
import com.hgh.service.StuService;
import com.hgh.service.TeaService;
import com.hgh.service.UserService;

public class StuServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService =new  UserService();
	private StuService stuService =new  StuService();
    String role="student";
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);

	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		System.out.println(action);
		if("regist".equals(action)){
			regist(request,response);
			System.out.println(action+"已执行");
		}
		if("testbyhand".equals(action)){
			test(request,response,action);
			System.out.println(action+"已执行");
		}
		if("testbyrandom".equals(action)){
			test(request,response,action);
			System.out.println(action+"已执行");
		}
		if("login".equals(action)){
			login(request,response);
			System.out.println(action+"已执行");
		}
		if("exportquestion".equals(action)){
			exportquestion(request,response);
			System.out.println(action+"已执行");
		}
		if("findquestion".equals(action)){
			findquestion(request,response);
			System.out.println(action+"已执行");
		}
		
	}

	private void test(HttpServletRequest request, HttpServletResponse response, String action) throws ServletException, IOException {
		/*
		 * 1.获取关键字的值index
		 * 2.判断其合法性
		 * 3.不合法，查询所有记录
		 * 4.合法，查询关键字记录
		 * 5.存入request
		 * 6.转回原页面
		 */
		String username=request.getParameter("username");
		
		String coursename1=request.getParameter("coursename");
		String coursename=null;
		if ("1".equals(coursename1)) {
			 coursename="计算机组成原理";
		}
		if ("2".equals(coursename1)) {
			 coursename="编译原理";
		}
		if ("3".equals(coursename1)) {
			 coursename="Java程序设计";
		}
		if ("4".equals(coursename1)) {
			 coursename="网页设计";
		}
		if ("5".equals(coursename1)) {
			 coursename="Android开发";
		}
		if ("6".equals(coursename1)) {
			 coursename="软件工程";
		}
		if ("7".equals(coursename1)) {
			 coursename="UI设计";
		}
		if ("8".equals(coursename1)) {
			 coursename="马克思基本原理概论";
		}
		if ("9".equals(coursename1)) {
			 coursename="高数";
		}
		TeaService teaService=new TeaService();
		String class1=stuService.findclass(username);
		String class2=teaService.findcourseclass(coursename);
		
		if (class1.equals(class2)) {
			Page page1=stuService.findtest(coursename,"1",action);
			Page page2=stuService.findtest(coursename,"2",action);
			Page page3=stuService.findtest(coursename,"3",action);
			Page page4=stuService.findtest(coursename,"4",action);
			
			HttpSession session = request.getSession();
			session.setAttribute("page1", page1);
			session.setAttribute("page2", page2);
			session.setAttribute("page3", page3);
			session.setAttribute("page4", page4);
			session.setAttribute("coursename", coursename);
			session.setAttribute("username", username);
			request.getRequestDispatcher("/testbyhand.jsp").forward(request, response);
		}else{
			HttpSession session = request.getSession();
			session.setAttribute("error", "你所在班级并没有该课程！");
			request.getRequestDispatcher("/Error.jsp").forward(request, response);
		}
	}


	private void findquestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.获取关键字的值index
		 * 2.判断其合法性
		 * 3.不合法，查询所有记录
		 * 4.合法，查询关键字记录
		 * 5.存入request
		 * 6.转回原页面
		 */
		String username=request.getParameter("username");
		String type=request.getParameter("type");
		String coursename1=request.getParameter("coursename");
		String coursename=null;
		if ("1".equals(coursename1)) {
			 coursename="计算机组成原理";
		}
		if ("2".equals(coursename1)) {
			 coursename="编译原理";
		}
		if ("3".equals(coursename1)) {
			 coursename="Java程序设计";
		}
		if ("4".equals(coursename1)) {
			 coursename="网页设计";
		}
		if ("5".equals(coursename1)) {
			 coursename="Android开发";
		}
		if ("6".equals(coursename1)) {
			 coursename="软件工程";
		}
		if ("7".equals(coursename1)) {
			 coursename="UI设计";
		}
		if ("8".equals(coursename1)) {
			 coursename="马克思基本原理概论";
		}
		if ("9".equals(coursename1)) {
			 coursename="高数";
		}
		TeaService teaService=new TeaService();
		String class1=stuService.findclass(username);
		String class2=teaService.findcourseclass(coursename);
		
		if (class1.equals(class2)) {
			Page page=teaService.findquestion(coursename,type);
			HttpSession session = request.getSession();
			session.setAttribute("page", page);
			session.setAttribute("coursename", coursename);
			session.setAttribute("type", type);
			session.setAttribute("username", username);
			if ("1".equals(type)) {
				request.getRequestDispatcher("/questiontest.jsp").forward(request, response);
			}else {
				request.getRequestDispatcher("/questiontest2.jsp").forward(request, response);
			}
			
		}else{
			HttpSession session = request.getSession();
			session.setAttribute("error", "你所在班级并没有该课程！");
			request.getRequestDispatcher("/Error.jsp").forward(request, response);
		}
		
	}
	private void exportquestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String coursename=request.getParameter("coursename");
		System.out.println("类型："+coursename);
		List<Question> list=stuService.findAllQuestion(coursename);
		ExportquestionServlet downloadQuestion=new ExportquestionServlet();
		downloadQuestion.doPost(request, response, list);
		
	}
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		User user=new User();
		user.setPassword(password);
    	user.setUsername(username);
    	boolean flag=userService.login(user, role);
    	if (flag) {
    		 boolean flag1=userService.state(user,role);
    		 if (flag1) {
    			 String name=userService.getname(user,role);
    			 System.out.println(name);
    		HttpSession session = request.getSession();
			session.setAttribute("name", name);
			session.setAttribute("username", username);
			session.setAttribute("role", role);
    		System.out.printf(name,username);
			request.getRequestDispatcher("/main.jsp").forward(request, response);
    		 }else {
    			 request.setAttribute("state", 0);
    			 request.getRequestDispatcher("/login.jsp").forward(request, response);
			}
		}else {
			request.setAttribute("state", 2);
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			
	}
	}

	@SuppressWarnings("deprecation")
	private void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		
		UserService userService=new UserService();
		String username=request.getParameter("username");
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		String depart=request.getParameter("depart");
		String age1=request.getParameter("age");
		int age=Integer.valueOf(age1).intValue();
		User user=new User();
    	user.setAge(age);
    	user.setDepart(depart);
    	user.setName(name);
    	user.setPassword(password);
    	user.setUsername(username);
    	userService.regist(user, role);
    	boolean flag=userService.login(user,role);
    	System.out.println(flag);
    	if (flag) {
    		
    		
    		HttpSession session = request.getSession();
			session.setAttribute("name", name);
			session.setAttribute("number", username);
			session.setAttribute("role", role);
    		
			
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		
		}else {
			response.setStatus(HttpServletResponse.SC_OK, "OK");
			
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			
		}
	}
			
}



