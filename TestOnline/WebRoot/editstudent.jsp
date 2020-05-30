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
   




 
 
    function addteacher(){
    
    
    var studentname=document.getElementById("studentname");
		if(studentname.value==""||studentname.value==null){
		alert("请输入学生姓名！");
		studentname.focus();
		
		return false;
	}
	var username=document.getElementById("username");
	if(username.value==""||username.value==null){
		alert("请输入人数！");
		username.focus();
		return false;
	}
	var depart=document.getElementById("depart");
		if(depart.value==""||depart.value==null){
		alert("请输入所属专业！");
		depart.focus();
		return false;
	}
	var age=document.getElementById("age");
		if(age.value==""||age.value==null){
		alert("请输入年龄！");
		age.focus();
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
    <li>修改学生信息</li>
    </ul>
    </div>
     
    <div class="formbody">
    
    <div class="formtitle"><span>基本信息</span></div>
    
    <ul class="forminfo">
    <c:forEach items="${list}" var="n">
     <li><label>姓名</label><input id="studentname" name="studentname" type="text" class="dfinput" value="${n.name}"/><i>不能超过10个字符</i></li>
    <li><label>用户名</label><input id="username" name="username" type="text" class="dfinput" value="${n.username}"/><i>输入用户名</i></li>
    <li><label>所属专业</label><input id="depart" name="depart" type="text" class="dfinput"value="${n.depart}" /><i>输入部门</i></li>
    <li><label>年龄</label><input id="age" name="age" type="text" class="dfinput" value="${n.age}" /><i>输入年龄</i></li>
    <li><label>&nbsp;</label>&nbsp;&nbsp;<a href="${pageContext.request.contextPath }/SecServlet?action=updatestudent&key=${key}&id=${id}&age=${n.age}&studentname=${n.name}&username=${n.username}&depart=${n.depart}"><button type="submit" onclick="addteacher()" class="btn">确认修改</button></a><a href="${pageContext.request.contextPath }/SecServlet?action=resetpassword&key=${key}&id=${id}"><button type="submit" onclick="addteacher()" class="btn">重置密码</button></a></li>
    
    </c:forEach>
    </ul>
    
    
    </div>
   </body>
</html>