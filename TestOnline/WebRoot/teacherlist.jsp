<%@ page language="java" import="java.util.*,com.hgh.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script language="javascript">
$(function(){	
	//导航切换
	$(".imglist li").click(function(){
		$(".imglist li.selected").removeClass("selected")
		$(this).addClass("selected");
	})	
})	
</script>
</head>


<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li>首页</li>
    <li>模块设计</li>
    <li>查询老师信息</li>
    </ul>
    </div>
    
    <div class="rightinfo">
    
    <div class="tools">
    <form action="${pageContext.request.contextPath}/SecServlet" method="post">
    <input type="hidden" name="action" value="findteacher"/>
    <ul class="toolbar">
    	
        <li> 专业： </li>
        <li><select id="depart" name="depart" class="find">
							<option value="computer01">信息技术系</option>
							<option value="computer02">基础学科部</option>
							<option value="computer03">其他</option>
						</select> </li>
        <li> <input  type="text" id="index" name="index" placeholder="请输入关键字" class="find" /></li>
        <li><button type="submit" class="find" ><span><img src="images/t04.png"/></span><font align="center">搜索</font></button></li>
        </ul>
        
        
        </form>
       
    <form action="${pageContext.request.contextPath}/SecServlet"
												 method="post">
    <input type="hidden" name="action" value="exportteacher"/>
    
    <ul class="toolbar">
    	
       <li><input type="submit" value="导出" class="btn"></li></ul>
        
        </form>
    </div>
    
    
    <ul class="imglist">
    <c:forEach items="${page.list}" var="n">
    <li class="selected">
    <span><input type ="checkbox" value="${n.id }" id=select><img src="images/img01.png" /></span>
    <h4 align="center"><a href="#">${n.usernamename}</a></h4>
    <h5 align="center">年龄：${n.age}</h5>
    <h5 align="center">姓名：${n.name}</h5>
    <p><a href="${pageContext.request.contextPath}/SecServlet?action=findByTeacherID&id=${n.id}&key=teacher">编辑</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:if(confirm('确定要删除吗？')) location='${pageContext.request.contextPath}/SecServlet?action=deleteteacher&id=${n.id}&key=teacher'">删除</a></p>
    </li>
    </c:forEach>
   
    
    
    
    </ul>
    
    
   
    
    
    
    <div class="tip">
    	<div class="tiptop"><span>提示信息</span><a></a></div>
        
      <div class="tipinfo">
        <span><img src="images/ticon.png" /></span>
        <div class="tipright">
        <p>是否确认对信息的修改 ？</p>
        <cite>如果是请点击确定按钮 ，否则请点取消。</cite>
        </div>
        </div>
        
        <div class="tipbtn">
        <input name="" type="button"  class="sure" value="确定" />&nbsp;
        <input name="" type="button"  class="cancel" value="取消" />
        </div>
    
    </div>
    
    <div class="pagin">
    	<div class="message">共<i class="blue">${page.totalSize}</i>条记录，每页${page.pageSize}条，分${page.totalPage}页，当前显示第&nbsp;<i class="blue">${page.currentPage}&nbsp;</i>页</div>
        <ul class="paginList">
        <c:choose>
        <c:when test="${page.currentPage==1}">
		<li class="paginItem"><span class="pagepre"></span></li>
		</c:when>
        <c:otherwise>
        <li class="paginItem"><a href="${pageContext.request.contextPath}/SecServlet?action=findteacher&currentPage=${page.currentPage-1}&index=${index}&depart=${depart}"><span class="pagepre"></span></a></li>
        </c:otherwise>
        </c:choose>
        <c:forEach begin="${page.start}" end="${page.end}" var="i">
        <li class="paginItem"><a href="${pageContext.request.contextPath}/SecServlet?action=findteacher&currentPage=${i}&index=${index}&depart=${depart}">${i}</a></li>
        </c:forEach>
        <c:choose>
        <c:when test="${page.currentPage==page.totalPage}">
        <li class="paginItem"><span class="pagenxt"></span></li>
        </c:when>
        <c:otherwise>
        <li class="paginItem"><a href="${pageContext.request.contextPath}/SecServlet?action=findteacher&currentPage=${page.currentPage+1}&index=${index}&depart=${depart}"><span class="pagenxt"></span></a></li>
        </c:otherwise>
        </c:choose>
        
        </ul>
    </div>
  
    
    </div>
</body>
</html>
