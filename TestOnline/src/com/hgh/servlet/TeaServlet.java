package com.hgh.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hgh.domain.Class1;
import com.hgh.domain.Course;
import com.hgh.domain.Page;
import com.hgh.domain.Question;
import com.hgh.domain.Test;
import com.hgh.domain.User;
import com.hgh.service.SecService;
import com.hgh.service.TeaService;
import com.hgh.service.UserService;

public class TeaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService =new  UserService();
	private TeaService teaService=new TeaService();
	private SecService secService=new SecService();
    String role="teacher";
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
		if("importquestion".equals(action)){
			importquestion(request,response);
			System.out.println(action+"已执行");
		}
		if("login".equals(action)){
			login(request,response);
			System.out.println(action+"已执行");
		}
		if("findcourse".equals(action)){
			findcourse(request,response);
			System.out.println(action+"已执行");
		}
		if("addmycourse".equals(action)){
			addmycourse(request,response);
			System.out.println(action+"已执行");
		}
		if("addmycourse".equals(action)){
			addmycourse(request,response);
			System.out.println(action+"已执行");
		}
		if("addtotest".equals(action)){
			addtotest(request,response);
			System.out.println(action+"已执行");
		}
		if("addmynewcourse".equals(action)){
			addmynewcourse(request,response);
			System.out.println(action+"已执行");
		}
		if("findmycourse".equals(action)){
			findmycourse(request,response);
			System.out.println(action+"已执行");
		}
		if("addquestion".equals(action)){
			addquestion(request,response);
			System.out.println(action+"已执行");
		}
		if("findquestion".equals(action)){
			findquestion(request,response);
			System.out.println(action+"已执行");
		}
		if("findbyhand".equals(action)){
			
			findbyhand(request,response);
			
			System.out.println(action+"已执行");
		}
		if("random".equals(action)){
			maketestrandom(request,response);
			System.out.println(action+"已执行");
		}
		if("deletemycourse".equals(action)){
			deletemycourse(request,response);
			System.out.println(action+"已执行");
		}
		if("toaddcourseclass".equals(action)){
			toaddcourseclass(request,response);
			System.out.println(action+"已执行");
		}
		if("findrandom".equals(action)){
			findrandom(request,response);
			System.out.println(action+"已执行");
		}
		if("addcourseclass".equals(action)){
			addcourseclass(request,response);
			System.out.println(action+"已执行");
		}
		if("deleteintestbyhand".equals(action)){
			deleteintestbyhand(request,response);
			System.out.println(action+"已执行");
		}
		if("deletetestrandom".equals(action)){
			deleteintestrandom(request,response);
			System.out.println(action+"已执行");
		}
		if("deletecourseclass".equals(action)){
			deletecourseclass(request,response);
			System.out.println(action+"已执行");
		}
		if("deletequestion".equals(action)){
			deletequestion(request,response);
			System.out.println(action+"已执行");
		}
		
	}

	


	private void deleteintestrandom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=request.getParameter("username");
		System.out.println("username:"+username);
		String id=request.getParameter("id");
		teaService.deletetestbyhand(id,2);
		findrandom(request, response);
	}


	private void deleteintestbyhand(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=request.getParameter("username");
		System.out.println("username:"+username);
		String id=request.getParameter("id");
		teaService.deletetestbyhand(id,1);
		findbyhand(request, response);
	}


	private void importquestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type=request.getParameter("questiontype");
		// 获取路径
		String fi = request.getParameter("afiles");
		if ("1".equals(type)) {
            List<Question> list = parseExcel( fi,type);
			
			for(Question tea:list){
				System.out.println(tea);
				teaService.addselectquestion(tea);
				}
			
		}else {
			List<Question> list = parseExcel( fi,type);
			
			for(Question tea:list){
				System.out.println(tea);
				teaService.addquestion(tea);
				}
			
		}
		
			
		//保存成功,跳转到老师列表
		findquestion(request, response);
		
		
	}


	private List<Question> parseExcel(String path, String type) {
		// 解析Excel,读取内容,path Excel路径
		
					List<Question> list = new ArrayList<Question>();
					File file = null;
					InputStream input = null;
					if (path != null && path.length() > 7) {
						// 判断文件是否是Excel(2003、2007)
						String suffix = path.substring(path.lastIndexOf("."), path.length());
						file = new File(path);
						System.out.println(file);
						try {
							input = new FileInputStream(file);
						} catch (FileNotFoundException e) {
							System.out.println("未找到指定的文件！");
						}
						// Excel2003
						if (".xls".equals(suffix)) {
							POIFSFileSystem fileSystem = null;
							// 工作簿
							HSSFWorkbook workBook = null;
							try {
								fileSystem = new POIFSFileSystem(input);
								workBook = new HSSFWorkbook(fileSystem);
							} catch (IOException e) {
								e.printStackTrace();
							}
							// 获取第一个工作簿
							HSSFSheet sheet = workBook.getSheetAt(0);
							list = getContent(sheet);
							// Excel2007
						} else if (".xlsx".equals(suffix)) {
							XSSFWorkbook workBook = null;
							try {
								workBook = new XSSFWorkbook(input);
							} catch (IOException e) {
								e.printStackTrace();
							}
							// 获取第一个工作簿
							XSSFSheet sheet = workBook.getSheetAt(0);
							
							if ("1".equals(type)) {
								list = getContent1(sheet);
							}else {
								list = getContent(sheet);
							}
							
						}
					} else {
						System.out.println("非法的文件路径!");
					}
					return list;
	}

	@SuppressWarnings("deprecation")
	private List<Question> getContent1(Sheet sheet) {
		List<Question> list = new ArrayList<Question>();
		// Excel数据总行数
		int rowCount = sheet.getPhysicalNumberOfRows();
		// 遍历数据行，略过标题行，从第二行开始
		for (int i = 0; i < rowCount; i++) {
			Question teacherBean = new Question();
			
			Row row = sheet.getRow(i);
			int cellCount = row.getPhysicalNumberOfCells();
			// 遍历行单元格
			for (int j = 0; j < cellCount; j++) {
				Cell cell = row.getCell(j);
				cell.setCellType(CellType.STRING);

				switch (j) {
				
				case 0:
					
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						teacherBean.setQuestionname(cell.getStringCellValue());
					}
					break;
				case 1:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						teacherBean.setQuestionmatter(cell.getStringCellValue());
					}
					break;
				
				case 2:

					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						teacherBean.setCoursename(cell.getStringCellValue());
					}
					break;
					
					
				case 3:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						teacherBean.setAnswer(cell.getStringCellValue());
					}
					break;
				case 4:
					
					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						teacherBean.setLevel((int) cell.getNumericCellValue());
					}else {
						teacherBean.setLevel(Integer.valueOf(cell.getStringCellValue()).intValue());
					}
					break;
				case 5:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						teacherBean.setA(cell.getStringCellValue());
					}
					break;
				case 6:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						teacherBean.setB(cell.getStringCellValue());
					}
					break;
				case 7:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						teacherBean.setC(cell.getStringCellValue());
					}
					break;
				case 8:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						teacherBean.setD(cell.getStringCellValue());
					}
					break;
				
				}
			}

			list.add(teacherBean);
		}
		return list;
	}


	@SuppressWarnings("deprecation")
	private List<Question> getContent(Sheet sheet) {
		List<Question> list = new ArrayList<Question>();
		// Excel数据总行数
		int rowCount = sheet.getPhysicalNumberOfRows();
		// 遍历数据行，略过标题行，从第二行开始
		for (int i = 0; i < rowCount; i++) {
			Question teacherBean = new Question();
			
			Row row = sheet.getRow(i);
			int cellCount = row.getPhysicalNumberOfCells();
			// 遍历行单元格
			for (int j = 0; j < cellCount; j++) {
				Cell cell = row.getCell(j);
				cell.setCellType(CellType.STRING);

				switch (j) {
				
				case 0:
					
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						teacherBean.setQuestionname(cell.getStringCellValue());
					}
					break;
				case 1:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						teacherBean.setQuestionmatter(cell.getStringCellValue());
					}
					break;
				
				case 2:

					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						teacherBean.setCoursename(cell.getStringCellValue());
					}
					break;
					
					
				case 3:
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						teacherBean.setAnswer(cell.getStringCellValue());
					}
					break;
				case 4:
					
					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						teacherBean.setLevel((int) cell.getNumericCellValue());
					}else {
						teacherBean.setLevel(Integer.valueOf(cell.getStringCellValue()).intValue());
					}
					break;
				
				}
			}

			list.add(teacherBean);
		}
		return list;
	}


	private void findrandom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=request.getParameter("username");
		Page page=teaService.findrandom();
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		session.setAttribute("page", page);
		request.getRequestDispatcher("/testlistrandom.jsp").forward(request, response);
	}


	private void findbyhand(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		String username=request.getParameter("username");
		
		
		Page page=teaService.randomid();
		int n1=teaService.countbyhand("1");
		int n2=teaService.countbyhand("2");
		int n3=teaService.countbyhand("3");
		int n4=teaService.countbyhand("4");
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		session.setAttribute("page", page);
		session.setAttribute("n1", n1);
		session.setAttribute("n2", n2);
		session.setAttribute("n3", n3);
		session.setAttribute("n4", n4);
		request.getRequestDispatcher("/testlistbyhand.jsp").forward(request, response);
	}


	private void maketestrandom(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=request.getParameter("username");
		teaService.empty();
		Random r=new Random();
		System.out.println(username);
		//两道填空，两道判断，一道简答，两道选择
		
		//xuanze
		int select=teaService.countselect();
		int[] sel=new int[select];
		sel=teaService.selectid(select);
		for (int i = 0; i < 2; i++) {
			int question=r.nextInt(select);
			teaService.addrandomsel(sel[question]);
		}
		//panduan
		int count=teaService.countquestion("2");
		int[] random=new int[count];
		random=teaService.questionid(count,"2");
		for (int i = 0; i < 2; i++) {
			int question=r.nextInt(count);
			teaService.addrandom(random[question]);
		}
		//tiankong
		int count1=teaService.countquestion("3");
		int[] random1=new int[count1];
		random1=teaService.questionid(count1,"3");
		for (int i = 0; i < 2; i++) {
			int question=r.nextInt(count1);
			teaService.addrandom(random1[question]);
		}
		//jianda
		int count2=teaService.countquestion("4");
		int[] random2=new int[count2];
		random2=teaService.questionid(count2,"4");
		for (int i = 0; i < 1; i++) {
			int question=r.nextInt(count2);
			teaService.addrandom(random2[question]);
		}	
				
		
		
		
		HttpSession session = request.getSession();
		session.setAttribute("username", username);
		request.getRequestDispatcher("/testlistrandom.jsp").forward(request, response);
	}


	private void addtotest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				String username=request.getParameter("username");
				String type=request.getParameter("type");
				System.out.println(username);
				System.out.println(type);
				int id=0;
				id=Integer.valueOf(request.getParameter("id")).intValue();
				teaService.addtotest(id,type);
				
				HttpSession session = request.getSession();
				session.setAttribute("username", username);
				session.setAttribute("type", type);
				request.getRequestDispatcher("/questionlist.jsp").forward(request, response);
	}


	private void deletequestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=request.getParameter("username");
		System.out.println("username:"+username);
		String id=request.getParameter("id");
		teaService.deletequestion(id);
		HttpSession session = request.getSession();
		session.setAttribute("username", username );
		request.getRequestDispatcher("/questionlist.jsp").forward(request, response);
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
		String type=request.getParameter("questiontype");
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
		Page page=teaService.findquestion(coursename,type);
		
		request.setAttribute("page", page);
		request.setAttribute("coursename", coursename);
		request.setAttribute("username", username);
		request.setAttribute("type", type);
		request.getRequestDispatcher("/questionlist.jsp").forward(request, response);
	}


	private void addquestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=request.getParameter("username");
		String questionname=request.getParameter("questionname");
		String coursename1=request.getParameter("coursename");
		String type=request.getParameter("questiontype");
		String coursename = null;
		
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
		
		int level=Integer.valueOf(request.getParameter("level")).intValue();
		String questionmatter=request.getParameter("questionmatter");
		String answer=request.getParameter("answer");
		Question question=new Question();
		
		
		question.setCoursename(coursename);
		question.setLevel(level);
		question.setQuestionmatter(questionmatter);
		question.setQuestionname(questionname);
		if ("1".equals(type)) { //选择
			String A=request.getParameter("A");
			String B=request.getParameter("B");
			String C=request.getParameter("C");
			String D=request.getParameter("D");
			question.setA(A);
			question.setB(B);
			question.setC(C);
			question.setD(D);
			question.setAnswer(answer);
			System.out.println(question);
			teaService.addselectquestion(question);
		}else{
			question.setAnswer(answer);
			question.setType(type);
			System.out.println(question);
			teaService.addquestion(question);
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("username", username );
		request.getRequestDispatcher("/questionlist.jsp").forward(request, response);
	}


	private void deletecourseclass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=request.getParameter("username");
		System.out.println("username:"+username);
		String coursename=request.getParameter("coursename");
		teaService.deletecourseclass(coursename);
		HttpSession session = request.getSession();
		session.setAttribute("username", username );
		
		request.getRequestDispatcher("/mycourse.jsp").forward(request, response);
	}


	private void addcourseclass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username=request.getParameter("username");
		System.out.println("username:"+username);
		String coursename=request.getParameter("coursename");
		String courseclass=request.getParameter("courseclass");
		teaService.addcourseclass(coursename,courseclass);
		HttpSession session = request.getSession();
		session.setAttribute("username", username );
		
		request.getRequestDispatcher("/mycourse.jsp").forward(request, response);
	}


	private void toaddcourseclass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id1=Integer.valueOf(request.getParameter("id")).intValue();
		String username=request.getParameter("username");
		Course course=new Course();
		course=teaService.toaddcourseclass(id1);
		
		int id=course.getId();
		
		String coursename=course.getCoursename();
		String courseclass=teaService.findcourseclass(coursename);
		HttpSession session = request.getSession();
		session.setAttribute("username", username );
		session.setAttribute("id", id);
		session.setAttribute("coursename", coursename );
		session.setAttribute("courseclass", courseclass );
		request.getRequestDispatcher("/addcourseclass.jsp").forward(request, response);
	}


	private void deletemycourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.获取id
		 * 2.合法，删除
		 * 3.存入request
		 * 4.转回原页面
		 */
		
		String key=request.getParameter("key");
		String username=request.getParameter("username");
		int id=Integer.valueOf(request.getParameter("id")).intValue();
		System.out.println(id);
		secService.deletecourse(id,key);
		
		HttpSession session = request.getSession();
		session.setAttribute("username", username );
		request.getRequestDispatcher("/mycourse.jsp").forward(request, response);
	}


	private void findmycourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		/*
		 * 1.获取关键字的值index
		 * 2.判断其合法性
		 * 3.不合法，查询所有记录
		 * 4.合法，查询关键字记录
		 * 5.存入request
		 * 6.转回原页面
		 */
       
		String depart1=request.getParameter("depart");
		String index=request.getParameter("index");
		String username=request.getParameter("username");
		int depart = 0;
		if(index==null||"".equals(index)){
			index=null;
		}
		if ("computer01".equals(depart1)) {
			depart=1;
		}else if ("computer02".equals(depart1)) {
			depart=2;
		}
		System.out.println("类型："+depart+"关键字："+index);
		//获取当前页
				String currentPage=request.getParameter("currentPage");
				//有当前页，CurrentPage，无，默认为1
				int current;
				try {
					current=Integer.parseInt(currentPage);
				} catch (Exception e) {
					// TODO: handle exception
					current=1;
				}
				System.out.println("页码："+current);
				Page page=teaService.findmycourse(username,current);
				request.setAttribute("page", page );
				request.setAttribute("index", index);
				request.setAttribute("depart", depart1);
				request.setAttribute("username", username );
				request.getRequestDispatcher("/mycourse.jsp").forward(request, response);
		
	}


	private void addmynewcourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String coursename=request.getParameter("coursename");
		int id=Integer.valueOf(request.getParameter("courseid")).intValue();
		System.out.println(coursename);
		String courseteacher=request.getParameter("courseteacher");
		List<Map<String, Object>> flag1=secService.findcoursebyid(id);
		List<Map<String, Object>> flag=secService.findteacherbyteachername(courseteacher);
		if (flag1.isEmpty()) {
		if (!flag.isEmpty()) {
			System.out.println(flag+"555645");
			Course course=new Course();
			course.setCoursename(coursename);
			course.setId(id);
			course.setCourseteacher(courseteacher);
			System.out.println(course);
			secService.addcourse(course);
			
			HttpSession session = request.getSession();
			session.setAttribute("username", courseteacher);
			request.getRequestDispatcher("/addcourse1.jsp").forward(request, response);
		}else{
			HttpSession session = request.getSession();
			session.setAttribute("error", "该教师工号输入有误或不存在，请返回重新输入！");
			request.getRequestDispatcher("/Error.jsp").forward(request, response);
		}
		}else{
			HttpSession session = request.getSession();
			session.setAttribute("error", "该课程号已经被使用，请返回重新输入！");
			request.getRequestDispatcher("/Error.jsp").forward(request, response);
		}
	}


	private void findcourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		/*
		 * 1.获取关键字的值index
		 * 2.判断其合法性
		 * 3.不合法，查询所有记录
		 * 4.合法，查询关键字记录
		 * 5.存入request
		 * 6.转回原页面
		 */
       String usernameString=request.getParameter("username");
		String depart1=request.getParameter("depart");
		String index=request.getParameter("index");
		int depart = 0;
		if(index==null||"".equals(index)){
			index=null;
		}
		if ("computer01".equals(depart1)) {
			depart=1;
		}else if ("computer02".equals(depart1)) {
			depart=2;
		}
		System.out.println("类型："+depart+"关键字："+index);
		//获取当前页
				String currentPage=request.getParameter("currentPage");
				//有当前页，CurrentPage，无，默认为1
				int current;
				try {
					current=Integer.parseInt(currentPage);
				} catch (Exception e) {
					// TODO: handle exception
					current=1;
				}
				System.out.println("页码："+current);
				Page page=secService.findcourse(depart, index, current);
				request.setAttribute("page", page );
				request.setAttribute("index", index);
				request.setAttribute("depart", depart1);
				request.setAttribute("username", usernameString);
				request.getRequestDispatcher("/courselist1.jsp").forward(request, response);
	}


	private void addmycourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id1=request.getParameter("id");
		int id=Integer.valueOf(id1).intValue();
		String username=request.getParameter("username");
		System.out.println("id："+id+"\nusername:"+username);
		teaService.addmycourse(id,username);
		request.setAttribute("username", username);
		request.getRequestDispatcher("/courselist1.jsp").forward(request, response);
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
    		
    		
    		
			
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		
		}else {
			response.setStatus(HttpServletResponse.SC_OK, "OK");
			
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			
		}
	}
			
}



