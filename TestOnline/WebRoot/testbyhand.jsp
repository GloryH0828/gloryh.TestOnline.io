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
var n1=0;
var n2=0;
var n3=0;
var n4=0;

function xuanze(fV,rK){
	
	var aa=fV;//获取你选中的答案
	
	if(aa==rK){
		n1++;
		
	}
	
}
function panduan(fV,rK){
	
	var aa=fV;//获取你选中的答案
	
	if(aa==rK){
		n2++;
		
	}
	
}
function tiankong(fV,rK){
	
	var aa=document.getElementById(fV);;
	
	if(aa.value==rK){
		n3++;
		
	}
	alert("提交成功，请继续作答");
	
}
function jianda(){
	
	alert("本系统不支持简答题评分，请自行评测！");
	
}
function zongfen(){
	var sum=n1*10+n2*10+n3*10;
	
	alert("本次测试成绩为"+sum+"分（不包含简答题分数）。\n选择题答对"+n1+"道\n判断题答对"+n2+"道\n填空题答对"+n3+"道\n");
	
	
}



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
  	
    <li><a href="#tab1" class="selected" >题目练习</a></li> 
    
  	</ul>
    </div> 
    <%int tiankong=1;
    int jianda=1; %>
    <form action="${pageContext.request.contextPath}/StuServlet?action=testbyhand&username=${username}" method="post" align="center">
    <ul class="seachform" >
    <div class="formtext">Hi,<b>"${username}"</b>，欢迎您使用题目练习功能！</div>
    <li>请选择课程：</li>  
    <li><div class="vocation">
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
     <li>  
    <div class="vocation">
    
    </div>
    </li>
    
 
    
    <li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="查询"/></li>
    
    </ul>
    </form>
    
  	<div id="tab1" class="tabson" align="center">
  <br>
  <br>
  <h3 align="center"><font color="red">选择题（5分/题）</font>
  </h3>
   <c:forEach var="n" items="${page1.list }">
    <ul class="forminfo" >
    <li>${n.id}题目名称<input name="questionname" type="text" class="dfinput" readonly="readonly" value="${n.questionname }"  style="width:518px;"/></li>
    <li>题目难度
    <input name="questionname" type="text" class="dfinput" readonly="readonly" value="${n.level}"  style="width:518px;"/>
    </li>
    <li>题目内容
    </li>
    <li><textarea id="content" name="questionmatter" readonly="readonly" style="width:700px;height:200px;">${n.questionmatter }</textarea></li>
    <li>选项A:<input name="A" type="text" class="dfinput" readonly="readonly" value="${n.A}"   style="width:518px;" /></li>
    <li>选项B:<input name="B" type="text" class="dfinput"  readonly="readonly" value="${n.B}" style="width:518px;"/></li>
    <li>选项C:<input name="C" type="text" class="dfinput"  readonly="readonly" value="${n.C} "style="width:518px;"/></li>
    <li>选项D:<input name="D" type="text" class="dfinput"  readonly="readonly" value="${n.D} "style="width:518px;"/></li>
    <li>请作答</li>
    <li><input type="radio"   onclick="xuanze('A','${n.answer }')" />A&nbsp;&nbsp;
    <input type="radio"  onclick="xuanze('B','${n.answer }')" />B&nbsp;&nbsp;
    <input type="radio"  onclick="xuanze('C','${n.answer }')" />C&nbsp;&nbsp;
    <input type="radio"  onclick="xuanze('D','${n.answer }')" />D </li>
    
    </ul>
 </c:forEach>
 <h3 align="center"><font color="red">判断题（5分/题）</font></h3>
  
  <c:forEach var="n" items="${page2.list }">
    <ul class="forminfo" >
    <li>${n.id}题目名称<input name="questionname" type="text" class="dfinput" readonly="readonly" value="${n.questionname }"  style="width:518px;"/></li>
    <li>题目难度
    <input name="questionname" type="text" class="dfinput" readonly="readonly" value="${n.level}"  style="width:518px;"/>
    </li>
    <li>题目内容
    </li>
    <li><textarea id="content" name="questionmatter" readonly="readonly" style="width:700px;height:200px;">${n.questionmatter }</textarea></li>
    <li>请作答</li>
    <li><input type="radio"  onclick="panduan('对','${n.answer }')" />对 &nbsp;
    <input type="radio"  onclick="panduan('错','${n.answer }')" />错</li>
    
    </ul>
 </c:forEach>
  <h3 align="center"><font color="red">填空题（5分/题）</font>
  
  </h3>
  <c:forEach var="n" items="${page3.list }">
    <ul class="forminfo" >
    <li>${n.id}题目名称<input name="questionname" type="text" class="dfinput" readonly="readonly" value="${n.questionname }"  style="width:518px;"/></li>
    <li>题目难度
    <input name="questionname" type="text" class="dfinput" readonly="readonly" value="${n.level}"  style="width:518px;"/>
    </li>
    <li>题目内容
    </li>
    <li><textarea id="content" name="questionmatter" readonly="readonly" style="width:700px;height:200px;">${n.questionmatter }</textarea></li>
    <li>请作答</li>
    <%request.setAttribute("tiankong","tiankong"+(tiankong++)); %>
    <li><input type="text" name="${ tiankong} " id="${ tiankong}" placeholder="请输入答案"  />
    <input type="button"  value="提交" onclick="tiankong('${tiankong }','${n.answer }')" /></li>
     
    </ul>
 </c:forEach>
  <h3 align="center"><font color="red">简答题（20分/题）</font>
  </h3>
  <c:forEach var="n" items="${page4.list }">
    <ul class="forminfo" >
    <li>${n.id}题目名称<input name="questionname" type="text" class="dfinput" readonly="readonly" value="${n.questionname }"  style="width:518px;"/></li>
    <li>题目难度
    <input name="questionname" type="text" class="dfinput" readonly="readonly" value="${n.level}"  style="width:518px;"/>
    </li>
    <li>题目内容
    </li>
    <li><textarea id="content" name="questionmatter" readonly="readonly" style="width:700px;height:200px;">${n.questionmatter }</textarea></li>
    <li>请作答</li>
    <%request.setAttribute("jianda","jianda"+(jianda++)); %>
    <li><textarea id="content" name="${jianda }" id=${jianda }  style="width:700px;height:200px;">请在此处作答</textarea></li>
    <li><input type="button"  value="提交" onclick="jianda()" /></li>
    </ul>
 </c:forEach>
 <ul><li> <label>&nbsp;</label><input name="" type="button" class="scbtn" value="查看成绩" onclick="zongfen()" /></li></ul>
     <div class="tools">
    
       
    
    </div>
    </div> 
    
    
  	<div id="tab2" class="tabson">
    
     
    
    
   
  
    
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
