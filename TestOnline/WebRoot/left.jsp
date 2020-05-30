<%@ page language="java" import="java.util.*,com.hgh.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<% String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>无标题文档</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="js/jquery.js"></script>

<script type="text/javascript">
$(function(){	
	//导航切换
	$(".menuson li").click(function(){
		$(".menuson li.active").removeClass("active")
		$(this).addClass("active");
	});
	
	$('.title').click(function(){
		var $ul = $(this).next('ul');
		$('dd').find('ul').slideUp();
		if($ul.is(':visible')){
			$(this).next('ul').slideUp();
		}else{
			$(this).next('ul').slideDown();
		}
	});
})	
</script>


</head>

<body style="background:#f0f9fd;">
	<div class="lefttop"><span></span>所有功能</div>
     <%
	
	  String role=(String)session.getAttribute("role");
	  String name=(String)session.getAttribute("name");
	  if(role!=null){
		  if(role.equals("student")){
		  
		%>
    <dl class="leftmenu">
       
    
        
    
    <dd>
    <div class="title">
    <span><img src="images/leftico02.png" /></span>题库与练习</div>
    <ul class="menuson">
        <li><cite></cite><a href="questionlist1.jsp" target="rightFrame">查看题库</a><i></i></li>
        <li><cite></cite><a href="questiontest.jsp" target="rightFrame">题目练习</a><i></i></li>
       
        </ul>     
    </dd> 
    
    
    <dd><div class="title"><span><img src="images/leftico03.png" /></span>在线测试</div>
    <ul class="menuson">
        <li><cite></cite><a href="testbyhand.jsp" target="rightFrame">手动组卷测试</a><i></i></li>
        <li><cite></cite><a href="testbyrandom.jsp" target="rightFrame">随即组卷测试</a><i></i></li>
        
    </ul>    
    </dd>  
    
    
    
    
    </dl>
    <%
	
	  
	}else if(role.equals("teacher")){
		  
		%>
		<dl class="leftmenu">
       
    
        
    
    <dd>
    <div class="title">
    <span><img src="images/leftico02.png" /></span>课程管理
    </div>
    <ul class="menuson">
        <li><cite></cite><a href="courselist1.jsp" target="rightFrame">查询课程</a><i></i></li>
        <li><cite></cite><a href="addcourse1.jsp" target="rightFrame">添加课程</a><i></i></li>
        <li><cite></cite><a href="mycourse.jsp" target="rightFrame">我的课程</a><i></i></li>
        </ul>     
    </dd> 
    
    
    <dd><div class="title"><span><img src="images/leftico03.png" /></span>班级/学生管理</div>
    <ul class="menuson">
        
        <li><cite></cite><a href="studentlist.jsp" target="rightFrame">查询学生信息</a><i></i></li>
        
    </ul>    
    </dd>  
    
    
    <dd><div class="title"><span><img src="images/leftico04.png" /></span>题库/组卷管理</div>
    <ul class="menuson">
        <li><cite></cite><a href="questionlist.jsp" target="rightFrame">题库管理</a><i></i></li>
        <li><cite></cite><a href="${pageContext.request.contextPath }/TeaServlet?action=findrandom" target="rightFrame">查看随机组卷</a><i></i></li>
        <li><cite></cite><a href="${pageContext.request.contextPath }/TeaServlet?action=findbyhand" target="rightFrame">查看手动组卷</a><i></i></li>
    </ul>
    
    </dd>   
    
    </dl>
    
		   <%
	
	  
	}else if(role.equals("admin")){
		  
		%>
		<dl class="leftmenu">
       
    
        
    
    <dd>
    <div class="title">
    <span><img src="images/leftico02.png" /></span>班级管理
    </div>
    <ul class="menuson">
        <li><cite></cite><a href="addclass.jsp"  target="rightFrame">添加新的班级</a><i></i></li>
        <li><cite></cite><a href="classlist.jsp"  target="rightFrame">查询班级信息</a><i></i></li>
        
        </ul>     
    </dd> 
    
    
    <dd><div class="title"><span><img src="images/leftico03.png" /></span>教师管理</div>
    <ul class="menuson">
        <li><cite></cite><a href="addteacher.jsp"  target="rightFrame">添加教师信息</a><i></i></li>
         <li><cite></cite><a href="#">导入教师信息</a><i></i></li>
        <li><cite></cite><a href="teacherlist.jsp"  target="rightFrame">查询教师信息</a><i></i></li>
        
        <li><cite></cite><a href="#">导出教师信息</a><i></i></li>
    </ul>    
    </dd>  
    
    
    <dd><div class="title"><span><img src="images/leftico04.png" /></span>学生管理</div>
    <ul class="menuson">
        <li><cite></cite><a href="addstudent.jsp" target="rightFrame">添加学生信息</a><i></i></li>
        <li><cite></cite><a href="studentlist.jsp" target="rightFrame">查询学生信息</a><i></i></li>
        
        <li><cite></cite><a href="#" target="rightFrame">导出学生信息</a><i></i></li>
    </ul>
    
    </dd>   
     <dd>
    <div class="title">
    <span><img src="images/leftico02.png" /></span>课程管理
    </div>
    <ul class="menuson">
        <li><cite></cite><a href="addcourse.jsp" target="rightFrame">添加课程信息</a><i></i></li>
        <li><cite></cite><a href="courselist.jsp" target="rightFrame">查询课程信息</a><i></i></li>
        <li><cite></cite><a href="#" target="rightFrame">导出课程信息</a><i></i></li>
        
        </ul>     
    </dd> 
    </dl>
    
		   <%	
		   
		  }
	  }else{
	     //out.print("<script>window.location.href='login.jsp'</script>");
	     
	     response.sendRedirect("login.jsp");
	     return;
	  }
	  
	  
	 %>
		   
</body>
</html>
