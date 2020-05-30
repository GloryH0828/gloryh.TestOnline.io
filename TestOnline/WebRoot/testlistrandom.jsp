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
    <li><a href="#">随机组卷</a></li>
    </ul>
    </div>
    
    <div class="formbody">
    
    
   
    
    
  	<div id="tab2" class="tabson">
    
     <form action="${pageContext.request.contextPath}/TeaServlet?action=findrandom&username=${username}" method="post">
    
    Hi，<b>"${username}"</b>，欢迎您使用题库管理功能！<label>&nbsp;</label><input name="" type="submit" class="scbtn" value="查询"/>
   
   
    </form>
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th><input name="" type="checkbox" value="" checked="checked"/></th>
        <th>题目名称<i class="sort"><img src="images/px.gif" /></i></th>
        <th style="width: 523px; ">题目内容</th>
        <th>对应课程</th>
        <th>题目类型</th>
        <th>题目答案</th>
        
        <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="n" items="${page.list}">
        <tr>
        <td><input name="" type="checkbox" value="${n.id}" /></td>
        <td>${n.questionname }</td>
        <td>${n.questionmatter }</td>
        <td>${n.coursename }</td>
         <td>${n.type}</td>
        <td>${n.answer}</td>
        
        <td>
         <a href="${pageContext.request.contextPath }/TeaServlet?action=deletetestrandom&id=${n.id}&coursename=${n.coursename }&username=${username}" class="tablelink"> 删除</a></td>
        
        </tr> </c:forEach>
        
   
        </tbody>
    </table>
   
   <!-- 分页 start -->
						 <tr>
								<td>

									<form id="pageForm" method="post">
										<table align="center" cellpadding="0" cellspacing="0"
											class="admin_table font03">
											<tr>
												<td><label>&nbsp;</label> <a href="${pageContext.request.contextPath }/TeaServlet?action=random&username=${username}" class="tablelink">重新组卷</a>

												</td>
											</tr>
										</table></form>
								</td>
							</tr>	
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
