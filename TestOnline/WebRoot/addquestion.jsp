<%@ page language="java" import="java.util.*,com.hgh.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    
    <title>添加班级</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script>
   

var r=session.getAttribute("username");
if(r==1){
alert("添加成功！");
}


 
 
    function addclass(){
    
    
    var classname=document.getElementById("classname");
		if(classname.value==""||classname.value==null){
		alert("请输入班级名！");
		classname.focus();
		
		return false;
	}
	var people=document.getElementById("people");
	if(people.value==""||people.value==null){
		alert("请输入人数！");
		people.focus();
		return false;
	}
	var depart=document.getElementById("depart");
		if(depart.value==""||depart.value==null){
		alert("请输入专业！");
		depart.focus();
		return false;
	}
/* 	var imagecode=document.getElementById("imagecode");
	if(imagecode.value==""||imagecode.value==null){
		alert("验证码");
		imagecode.focus();
		return false;
	} */
	
	
    }
    
    
    
</script>
  </head>
   
  <body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="index.jsp">首页</a></li>
    <li><a href="addcourse.jsp">添加课程</a></li>
    </ul>
    </div>
 
     <form action="${pageContext.request.contextPath }/TeaServlet" method="post" ><!-- class="stu_login_error" -->
      <input type="hidden" name="action" value="addmynewcourse" />
    <div class="formbody">
    
    <div class="formtitle"><span>基本信息</span></div>
    
    <ul class="forminfo">
  
    <li><label>课程号</label><input id="people" name="courseid" type="text" class="dfinput" /><i>输入课程号</i></li>
    <li><label>课程名称</label><input id="classname" name="coursename" type="text" class="dfinput"   /><i>不能超过10个字符</i></li>
    <li><label>所属教师</label><input id="people" name="courseteacher" type="text" class="dfinput" value="${username }" /><i>输入教师工号</i></li>
    
   
    
    
    <li><label>&nbsp;</label><button  type="submit" onclick="addclass()" class="btn" >添加课程</button></li>
    </ul>
    
    </div>
    </form>
</body>
</html>