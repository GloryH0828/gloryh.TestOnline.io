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
    <li><a href="addclass.jsp">添加班级</a></li>
    </ul>
    </div>
     <form action="${pageContext.request.contextPath }/SecServlet" method="post" ><!-- class="stu_login_error" -->
      <input type="hidden" name="action" value="addclass" />
    <div class="formbody">
    
    <div class="formtitle"><span>基本信息</span></div>
    
    <ul class="forminfo">
    <li><label>班级名称</label><input id="classname" name="classname" type="text" class="dfinput" /><i>不能超过10个字符</i></li>
    <li><label>总人数</label><input id="people" name="people" type="text" class="dfinput" /><i>输入班级人数</i></li>
    <li><label>所属专业</label>
    <select id="depart" name="depart" type="text" class="dfinput">
							<option value="computer01">计算机科学与技术</option>
							<option value="computer02">软件工程</option>
							<option value="computer03">新数字媒体</option>
						</select><i>输入班级专业</i></li>
    <li><label>管理人</label><input id="manager" name="manager" type="text" class="dfinput" /><i>没有管理人则不填该选项</i></li>
    <li><label>&nbsp;</label><button  type="submit" onclick="addclass()" class="btn" >添加班级</button></li>
    </ul>
    
    
    </div>
    </form>
    
</body>
</html>