
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
    
    <title>添加教师</title>
    
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
   

var r=session.getAttribute("teachername");
if(r==1){
alert("添加成功！");
}


 
 
    function addstudent(){
    
    
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
    <li><a href="addstudent.jsp">添加学生</a></li>
    </ul>
    </div>

<div class="tools">
    <form action="${pageContext.request.contextPath}/SecServlet" method="post">
    <input type="hidden" name="action" value="importteacher"/>
     <input type="hidden" name="key" value="student"/>
    <ul class="toolbar">
    	
        <li> 批量导入： </li>
        
        <li> <input id="files" name="afiles" type="file" class="find"/></li>
        <li><button type="submit" class="find" ><span><img src="images/t04.png"/></span><font align="center">确定</font></button></li>
        </ul>
        
        
        </form>
       
    
    </div>
   
    
     <form action="${pageContext.request.contextPath }/SecServlet" method="post" ><!-- class="stu_login_error" -->
      <input type="hidden" name="action" value="addstudent" />
    <div class="formbody">
    
    <div class="formtitle"><span>基本信息</span></div>
    
    <ul class="forminfo">
    <li><label>学生姓名</label><input id="studentname" name="studentname" type="text" class="dfinput" /><i>不能超过10个字符</i></li>
    <li><label>用户名</label><input id="username" name="username" type="text" class="dfinput" /><i>输入用户名</i></li>
    <li><label>所属专业</label><select id="depart" name="depart" type="text" class="dfinput">
							<option value="计算机科学与技术">计算机科学与技术</option>
							<option value="软件工程">软件工程</option>
							<option value="新数字媒体">新数字媒体</option>
						</select><i>输入部门</i></li>
    <li><label>年龄</label><input id="age" name="age" type="text" class="dfinput" /><i>输入年龄</i></li>
    <li><label>&nbsp;</label><button  type="submit" onclick="addstudent()" class="btn" >添加</button></li>
    </ul>
    
    
    </div>
    </form>
    
</body>
</html>