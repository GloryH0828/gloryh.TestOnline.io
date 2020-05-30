<%@ page language="java" import="java.util.*,com.hgh.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery.idTabs.min.js"></script>
<script type="text/javascript" src="js/select-ui.min.js"></script>
<script type="text/javascript" src="editor/kindeditor.js"></script>

<script type="text/javascript">
    KE.show({
        id : 'content7',
        cssPath : './index.css'
    });
  </script>
  
<script type="text/javascript">
$(document).ready(function(e) {
    $(".select1").uedSelect({
		width : 345			  
	});
	$(".select2").uedSelect({
		width : 167  
	});
	$(".select3").uedSelect({
		width : 100
	});
});
</script>
</head>

<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">首页</a></li>
    <li><a href="#">题库管理</a></li>
    </ul>
    </div>
    
    <div class="formbody">
    
    
    <div id="usual1" class="usual"> 
    
    <div class="itab">
  	<ul> 
  	<li><a href="#tab2" class="selected">题库列表</a></li> 
     
    
  	</ul>
    </div> 
    
  	
    
    
  	<div id="tab2" class="tabson">
    
     <form action="${pageContext.request.contextPath}/StuServlet?action=findquestion&username=${username}" method="post">
    <ul class="seachform">
    <div class="formtext">Hi，<b>"${username}"</b>，欢迎您使用题库管理功能！</div>
    <li><label>综合查询:</label>
    <li><label>课程名称</label>  
    <div class="vocation">
    <select class="select1" name="coursename">
    <option value="1">计算机组成原理</option>
    <option value="2">编译原理</option>
    <option value="3">Java程序设计</option>
    <option value="4">网页设计</option>
    <option value="5">Android开发</option>
    <option value="6">软件工程</option>
    <option value="7">UI设计</option>
    <option value="8">马克思基本原理概论</option>
    <option value="9">高数</option>
    </select>
    </div>
    </li>
     <li><label>题目类型</label>  
    <div class="vocation">
    <select class="select1" name="type">
    <option value="1">选择题</option>
    <option value="2">判断题</option>
    <option value="3">填空题</option>
    <option value="4">简答题</option>
   
    </select>
    </div>
    </li>
    
 
    
    <li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="查询"/></li>
    
    </ul>
    </form>
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th><input name="" type="checkbox" value="" checked="checked"/></th>
        <th>题目名称<i class="sort"><img src="images/px.gif" /></i></th>
        <th style="width: 595px; ">题目内容</th>
        <th>题目等级</th>
        <th>题目答案</th>
        
        
        </tr>
        </thead>
        <tbody>
        <c:forEach var="n" items="${page.list}">
        <tr>
        <td><input name="" type="checkbox" value="${n.id}" /></td>
        <td>${n.questionname }</td>
        <td>${n.questionmatter }</td>
        <td>${n.level}</td>
        <td>${n.answer}</td>
        
        
        </tr> </c:forEach>
        
   
        </tbody>
    </table>
   
   <!-- 分页 start -->
						

									
										<table align="center" cellpadding="0" cellspacing="0"
											class="admin_table font03">
											<tr>
												<td><label>&nbsp;</label>
												<form action="${pageContext.request.contextPath}/StuServlet?coursename=${coursename}" method="post">
                                                      <input type="hidden" name="action" value="exportquestion"/>
    
                                                       <ul class="toolbar">
    	
                                                        <li><input type="submit" value="导出" class="btn"></li></ul>
        
                                                      </form>

												
										</table>
								
								
							<!-- 分页 end -->
  
    
    </div>  
       
	</div> 
 
	<script type="text/javascript"> 
      $("#usual1 ul").idTabs(); 
    </script>
    
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	</script>
    
    
    
    
    
    </div>

</body>
</html>
