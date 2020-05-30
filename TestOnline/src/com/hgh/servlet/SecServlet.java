package com.hgh.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.hgh.domain.User;
import com.hgh.service.SecService;
import com.hgh.service.UserService;





public class SecServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService =new  UserService();
	private SecService secService=new SecService();
    String role="admin";
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
		if("exportcourse".equals(action)){
			exportcourse(request,response);
			System.out.println(action+"已执行");
		}
		if("addcourse".equals(action)){
			addcourse(request,response);
			System.out.println(action+"已执行");
		}
		if("deleteteacher".equals(action)){
			deleteteacher(request,response);
			System.out.println(action+"已执行");
		}
		if("exportteacher".equals(action)){
			exportteacher(request,response);
			System.out.println(action+"已执行");
		}
		if("deletecourse".equals(action)){
			deletecourse(request,response);
			System.out.println(action+"已执行");
		}
		if("login".equals(action)){
			login(request,response);
			System.out.println(action+"已执行");
		}
		if("resetpassword".equals(action)){
			resetpassword(request,response);
			System.out.println(action+"已执行");
		}
		if("addclass".equals(action)){
			addclass(request,response);
			System.out.println(action+"已执行");
		}
		if("find".equals(action)){
			find(request,response);
			System.out.println(action+"已执行");
		}if("findteacher".equals(action)){
			findteacher(request,response);
			System.out.println(action+"已执行");
		}
		if("findstudent".equals(action)){
			findstudent(request,response);
			System.out.println(action+"已执行");
		}
		if("updateclass".equals(action)){
			updateclass(request,response);
			System.out.println(action+"已执行");
		}
		if("updateteacher".equals(action)){
			updateteacher(request,response);
			System.out.println(action+"已执行");
		}
		if("updatestudent".equals(action)){
			updatestudent(request,response);
			System.out.println(action+"已执行");
		}
		if("findcourse".equals(action)){
			findcourse(request,response);
			System.out.println(action+"已执行");
		}
		if("findByID".equals(action)){
			findByID(request,response);
			System.out.println(action+"已执行");
		}
		if("findByTeacherID".equals(action)){
			findByTeacherID(request,response);
			System.out.println(action+"已执行");
		}
		if("importteacher".equals(action)){
			importteacher(request,response);
			System.out.println(action+"已执行");
		}
		if("findByStudentID".equals(action)){
			findByStudentID(request,response);
			System.out.println(action+"已执行");
		}
		if("delete".equals(action)){
			delete(request,response);
			System.out.println(action+"已执行");
		}
		if("exportstudent".equals(action)){
			exportstudent(request,response);
			System.out.println(action+"已执行");
		}
		if("deletestudent".equals(action)){
			deletestudent(request,response);
			System.out.println(action+"已执行");
		}
		if("addteacher".equals(action)){
			addteacher(request,response);
			System.out.println(action+"已执行");
		}
		if("addstudent".equals(action)){
			addstudent(request,response);
			System.out.println(action+"已执行");
		}
		
	}


	private void exportcourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Course> list=secService.findAllCourse();
		ExportcourseServlet downloadCourse=new ExportcourseServlet();
		downloadCourse.doPost(request, response, list);
	}


	private void exportstudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String classname=request.getParameter("classname");
		
		//先按条件查询
		List<User> list=secService.exportStudent(classname);
		
		//下载列表
		ExportStudentServlet downloadStudent=new ExportStudentServlet();
		downloadStudent.doPost(request, response,list);
	}


	private void exportteacher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<User> list=secService.findAllTeacher();
		ExportteacherServlet downloadTeacher=new ExportteacherServlet();
		downloadTeacher.doPost(request,response,list);
		
	}


	private void importteacher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取路径
				String fi = request.getParameter("afiles");
				String key=request.getParameter("key");
				if ("course".equals(key)) {
					List<Course> list = parseExcel1( fi);
					
						for(Course tea:list){
							System.out.println(tea);
							secService.addcourse(tea);
						}
						//保存成功,跳转到老师列表
						findcourse(request, response);
				}else{
				List<User> list1 = parseExcel( fi);
				if ("teacher".equals(key)) {
					for(User tea:list1){
						System.out.println(tea);
						secService.addteacher(tea);
					}
					//保存成功,跳转到老师列表
					findteacher(request, response);
					
				}else {
					
					
						for(User tea:list1){
							System.out.println(tea);
							secService.addstudent(tea);
						}
						//保存成功,跳转到老师列表
						findstudent(request, response);
				}
				
				}
	}


	private List<Course> parseExcel1(String path) {
		// 解析Excel,读取内容,path Excel路径
		
					List<Course> list = new ArrayList<Course>();
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
							list = getContent1(sheet);
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
							list = getContent1(sheet);
						}
					} else {
						System.out.println("非法的文件路径!");
					}
					return list;
	}


	// 获取Excel内容
		@SuppressWarnings("deprecation")
		public static List<Course> getContent1(Sheet sheet) {
			List<Course> list = new ArrayList<Course>();
			// Excel数据总行数
			int rowCount = sheet.getPhysicalNumberOfRows();
			// 遍历数据行，略过标题行，从第二行开始
			for (int i = 1; i < rowCount+1; i++) {
				Course teacherBean = new Course();
				
				Row row = sheet.getRow(i);
				int cellCount = row.getPhysicalNumberOfCells();
				// 遍历行单元格
				for (int j = 0; j < cellCount; j++) {
					Cell cell = row.getCell(j);
					cell.setCellType(CellType.STRING);

					switch (j) {
					
					case 0:
						
						cell.setCellType(CellType.NUMERIC);
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							teacherBean.setId((int) cell.getNumericCellValue());
						}
						break;
					case 1:
						if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							teacherBean.setCoursename(cell.getStringCellValue());
						}
						break;
					
					case 2:
						if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							teacherBean.setCourseteacher(cell.getStringCellValue());
						}
						break;
					}
				}

				list.add(teacherBean);
			}
			
			return list;
			
		}


	private List<User> parseExcel(String path) {
		// 解析Excel,读取内容,path Excel路径
		
			List<User> list = new ArrayList<User>();
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
					list = getContent(sheet);
				}
			} else {
				System.out.println("非法的文件路径!");
			}
			return list;
		}
	// 获取Excel内容
		@SuppressWarnings("deprecation")
		public static List<User> getContent(Sheet sheet) {
			List<User> list = new ArrayList<User>();
			// Excel数据总行数
			int rowCount = sheet.getPhysicalNumberOfRows();
			// 遍历数据行，略过标题行，从第二行开始
			for (int i = 1; i < rowCount+1; i++) {
				User teacherBean = new User();
				
				Row row = sheet.getRow(i);
				int cellCount = row.getPhysicalNumberOfCells();
				// 遍历行单元格
				for (int j = 0; j < cellCount; j++) {
					Cell cell = row.getCell(j);
					cell.setCellType(CellType.STRING);

					switch (j) {
					
					case 0:
						
						if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							teacherBean.setUsername(cell.getStringCellValue());
						}
						break;
					case 1:
						if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							teacherBean.setName(cell.getStringCellValue());
						}
						break;
					
					case 2:
						cell.setCellType(CellType.NUMERIC);
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							teacherBean.setAge((int) cell.getNumericCellValue());
						}
						break;
					case 3:
						
						if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							teacherBean.setDepart(cell.getStringCellValue());
						}
						break;
					
					}
				}

				list.add(teacherBean);
			}
			return list;
		}
	


	private void deletestudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.获取id
		 * 2.合法，删除
		 * 3.存入request
		 * 4.转回原页面
		 */
		
		String key=request.getParameter("key");
		int id=Integer.valueOf(request.getParameter("id")).intValue();
		System.out.println(id);
		secService.deletestudent(id,key);
		
		HttpSession session = request.getSession();
		session.setAttribute("R", 1);
		request.getRequestDispatcher("/studentlist.jsp").forward(request, response);
		
	}


	private void deleteteacher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.获取id
		 * 2.合法，删除
		 * 3.存入request
		 * 4.转回原页面
		 */
		
		String key=request.getParameter("key");
		int id=Integer.valueOf(request.getParameter("id")).intValue();
		System.out.println(id);
		secService.deleteteacher(id,key);
		
		HttpSession session = request.getSession();
		session.setAttribute("R", 1);
		request.getRequestDispatcher("/teacherlist.jsp").forward(request, response);
		
	}


	private void deletecourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.获取id
		 * 2.合法，删除
		 * 3.存入request
		 * 4.转回原页面
		 */
		
		String key=request.getParameter("key");
		int id=Integer.valueOf(request.getParameter("id")).intValue();
		System.out.println(id);
		secService.deletecourse(id,key);
		
		HttpSession session = request.getSession();
		session.setAttribute("R", 1);
		request.getRequestDispatcher("/courselist.jsp").forward(request, response);
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
						request.getRequestDispatcher("/courselist.jsp").forward(request, response);
		
	}


	private void addcourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			List<Map<String, Object>> list=secService.findcourseid(coursename,courseteacher);
			HttpSession session = request.getSession();
			session.setAttribute("list", list);
			request.getRequestDispatcher("/addcourse.jsp").forward(request, response);
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


	private void findByStudentID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
				/*
				 * 1.获取id
				 * 2.合法，查询关键字记录
				 * 3.存入request
				 * 4.转回原页面
				 */
				String key=request.getParameter("key");
				String id=request.getParameter("id");
				
				List<Map<String, Object>> list=SecService.findByID(id,key);
				System.out.println(list);
				request.setAttribute("list", list);
				request.setAttribute("id", id);
				System.out.println(id);
				request.setAttribute("key", key);
				request.getRequestDispatcher("/editstudent.jsp").forward(request, response);
		
	}


	private void addstudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
				String studentname=request.getParameter("studentname");
				System.out.println(studentname);
				int age=Integer.valueOf(request.getParameter("age")).intValue();
				String depart=request.getParameter("depart");
				String username=request.getParameter("username");
				
				User user=new User();
				user.setAge(age);
				user.setDepart(depart);
				user.setName(studentname);
				user.setUsername(username);
				System.out.println(user);
				secService.addstudent(user);
				HttpSession session = request.getSession();
				session.setAttribute("R", 1);
				request.getRequestDispatcher("/addstudent.jsp").forward(request, response);
		
	}


	private void findstudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		String depart = null;
		if(index==null||"".equals(index)){
			index=null;
		}
		if ("computer01".equals(depart1)) {
			depart="计算机科学与技术";
		}else if ("computer02".equals(depart1)) {
			depart="软件工程";
		}else if ("computer03".equals(depart1)) {
			depart="新数字媒体";
		}
		System.out.println("部门："+depart+"关键字："+index);
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
				Page page=secService.findstudent(depart, index, current);
				request.setAttribute("page", page );
				request.setAttribute("index", index);
				request.setAttribute("depart", depart1);
				request.getRequestDispatcher("/studentlist.jsp").forward(request, response);
		
	}


	private void resetpassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		String key=request.getParameter("key");
		int id=Integer.valueOf(request.getParameter("id")).intValue();
		System.out.println(id);
		secService.resetpassword( key , id);
		findByTeacherID(request, response);
	}


	private void findteacher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		String depart = null;
		if(index==null||"".equals(index)){
			index=null;
		}
		if ("computer01".equals(depart1)) {
			depart="信息技术系";
		}else if ("computer02".equals(depart1)) {
			depart="基础学科部";
		}else if ("computer03".equals(depart1)) {
			depart="其他";
		}
		System.out.println("部门："+depart+"关键字："+index);
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
				Page page=secService.findteacher(depart, index, current);
				request.setAttribute("page", page );
				request.setAttribute("index", index);
				request.setAttribute("depart", depart1);
				request.getRequestDispatcher("/teacherlist.jsp").forward(request, response);
	}


	private void updateteacher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		String key=request.getParameter("key");
		int id=Integer.valueOf(request.getParameter("id")).intValue();
		System.out.println(id);
		String teachername=request.getParameter("teachername");
		String depart=request.getParameter("depart");
		int age=Integer.valueOf(request.getParameter("age")).intValue();
		System.out.println(age);
		String username=request.getParameter("username");
		User user=new User();
		user.setAge(age);
		user.setDepart(depart);
		user.setName(teachername);
		user.setUsername(username);
		System.out.println(user);
		user.setId(id);
		secService.updateteacher(user,key);
		findByTeacherID(request, response);
	}
	private void updatestudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		String key=request.getParameter("key");
		int id=Integer.valueOf(request.getParameter("id")).intValue();
		System.out.println(id);
		String studentname=request.getParameter("studentname");
		String depart=request.getParameter("depart");
		int age=Integer.valueOf(request.getParameter("age")).intValue();
		String username=request.getParameter("username");
		User user=new User();
		user.setAge(age);
		user.setDepart(depart);
		user.setName(studentname);
		user.setUsername(username);
		System.out.println(user);
		user.setId(id);
		secService.updatestudent(user,key);
		findByStudentID(request, response);
	}


	private void findByTeacherID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		/*
		 * 1.获取id
		 * 2.合法，查询关键字记录
		 * 3.存入request
		 * 4.转回原页面
		 */
		String key=request.getParameter("key");
		String id=request.getParameter("id");
		
		List<Map<String, Object>> list=SecService.findByID(id,key);
		System.out.println(list);
		request.setAttribute("list", list);
		request.setAttribute("id", id);
		System.out.println(id);
		request.setAttribute("key", key);
		request.getRequestDispatcher("/editeacher.jsp").forward(request, response);
	}


	private void addteacher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		String teachername=request.getParameter("teachername");
		System.out.println(teachername);
		int age=Integer.valueOf(request.getParameter("age")).intValue();
		String depart=request.getParameter("depart");
		String username=request.getParameter("username");
		
		User user=new User();
		user.setAge(age);
		user.setDepart(depart);
		user.setName(teachername);
		user.setUsername(username);
		System.out.println(user);
		secService.addteacher(user);
		HttpSession session = request.getSession();
		session.setAttribute("R", 1);
		request.getRequestDispatcher("/addteacher.jsp").forward(request, response);
	}


	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.获取id
		 * 2.合法，删除
		 * 3.存入request
		 * 4.转回原页面
		 */
		
		String key=request.getParameter("key");
		int id=Integer.valueOf(request.getParameter("id")).intValue();
		System.out.println(id);
		int people=secService.selectpeople(id);
		if (people!=0) {
			secService.delete(id,key);
		}else{
			HttpSession session = request.getSession();
			session.setAttribute("error", "该班级下仍有同学，无法进行删除操作");
			request.getRequestDispatcher("/Error.jsp").forward(request, response);
		}
		
		
	}


	private void findByID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.获取id
		 * 2.合法，查询关键字记录
		 * 3.存入request
		 * 4.转回原页面
		 */
		String key=request.getParameter("key");
		String id=request.getParameter("id");
		
		List<Map<String, Object>> list=SecService.findByID(id,key);
		System.out.println(list);
		request.setAttribute("list", list);
		request.setAttribute("id", id);
		System.out.println(id);
		request.setAttribute("key", key);
		request.getRequestDispatcher("/editclass.jsp").forward(request, response);
	}


	private void updateclass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key=request.getParameter("key");
		int id=Integer.valueOf(request.getParameter("id")).intValue();
		System.out.println(id);
		String classname=request.getParameter("classname");
		String depart=request.getParameter("depart");
		int people=Integer.valueOf(request.getParameter("people")).intValue();
		String manager=request.getParameter("manager");
		Class1 class1  = new Class1();
		class1.setClassname(classname);
		class1.setDepart(depart);
		class1.setManager(manager);
		class1.setPeople(people);
		class1.setId(id);
		secService.updateclass(class1,key);
		findByID(request, response);
	}


	private void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		String depart = null;
		if(index==null||"".equals(index)){
			index=null;
		}
		if ("computer01".equals(depart1)) {
			depart="计算机科学与技术";
		}else if ("computer02".equals(depart1)) {
			depart="软件工程";
		}else if ("computer03".equals(depart1)) {
			depart="新数字媒体";
		}
		System.out.println("部门："+depart+"关键字："+index);
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
				Page page=secService.find(depart, index, current);
				request.setAttribute("page", page );
				request.setAttribute("index", index);
				request.setAttribute("depart", depart1);
				request.getRequestDispatcher("/classlist.jsp").forward(request, response);
	}


	private void addclass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		String classname=request.getParameter("classname");
		System.out.println(classname);
		int people=Integer.valueOf(request.getParameter("people")).intValue();
		String depart=request.getParameter("depart");
		String manager=request.getParameter("manager");
		if(manager==null || "".equals(manager)){
			manager="无";
		}
		Class1 class1=new Class1();
		class1.setClassname(classname);
		class1.setPeople(people);
		class1.setDepart(depart);
		class1.setManager(manager);
		System.out.println(class1);
		secService.addclass(class1);
		HttpSession session = request.getSession();
		session.setAttribute("R", 1);
		request.getRequestDispatcher("/addclass.jsp").forward(request, response);
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
			request.setAttribute("msg1", "注册成功！");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			
		}else {
			request.setAttribute("msg1", "注册失败！");
			request.getRequestDispatcher("/regist.jsp").forward(request, response);
			
	}}
			
}



