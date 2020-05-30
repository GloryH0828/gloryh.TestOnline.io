package com.hgh.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.management.relation.Role;
import javax.servlet.jsp.jstl.core.Config;

import com.hgh.domain.User;
import com.hgh.utils.JDBCUtils;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

public class UserDao {
	private static String driver;
	private static String url;
	private static String name;
	private static String password;
        private static ThreadLocal<Connection>tl=new ThreadLocal<Connection>();
		
        static{
        	//加载驱动
        	//读取文件
        	InputStream in=JDBCUtils.class.getClassLoader().getResourceAsStream("dbconfig.properties");
        	Properties prop=new Properties();
        	
				try {
					prop.load(in);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				driver = prop.getProperty("driverClassName");
				
				url=prop.getProperty("url");
				name=prop.getProperty("name");
				password=prop.getProperty("password");
				try {
					if(driver!=null&&!"".equals(driver)){
					Class.forName(driver);
					}
				} catch (ClassNotFoundException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
		
        }
    	private Connection getConnection() throws SQLException {
			Connection conn=tl.get();
			if(conn!=null){
				return conn;
			}
			return DriverManager.getConnection(url,name,password);
		}
    	private void fillstatement(PreparedStatement pre, Object[] params) throws SQLException {
			// TODO 自动生成的方法存根
    		if(params!=null){
		    	for (int i = 0; i < params.length; i++) {
					pre.setObject(i+1, params[i]);
					
				}
    		}
		}
    	private void release(Connection conn, Statement stmt, ResultSet rs) {
			// TODO 自动生成的方法存根
    		if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					throw new RuntimeException(e);
				}finally{
					if(stmt!=null){
						try {
							stmt.close();
						} catch (Exception e) {
							// TODO 自动生成的 catch 块
							throw new RuntimeException(e);
						}finally {
							Connection tconn=tl.get();
							if (tconn==conn) {
								return;
							}
							if (tconn!=null) {
								try {
									conn.close();
								} catch (Exception e) {
									// TODO 自动生成的 catch 块
									throw new RuntimeException(e);
								}
							}
						
						}
						}
					}
				}
		}
    	private List<Map<String, Object>> RsTolist(ResultSet rs) throws SQLException {
			// TODO 自动生成的方法存根
    		//将ResultSet对象转换为list
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			//获取表结构
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
			//通过rs逐条读取记录
			while (rs.next()) {
				//每条记录放入1个Map中
				Map<String, Object> map=new HashMap<String, Object>();
				//rsmd.getColumCount(),字段的列数
				for (int i = 1; i <=rsmd.getColumnCount(); i++) {
					//逐个字段读取出来放入map中,以键值形式存入，字段名：rsmd.getColumnName(i)，对应值 rs.getObject(i)
					map.put(rsmd.getColumnName(i), rs.getObject(i));
				}
				list.add(map);
			}
			return list;
		}
        public void insertUser(User auser,String role) {
    		
    		String sql="";
    		if(role.equals("student")){
    			sql="insert into student(username,name,password,age,depart) values(?,?,?,?,?)";
    		}else if(role.equals("teacher")){
    			sql="insert into teacher(username,name,password,age,depart) values(?,?,?,?,?)";
    		}else if(role.equals("admin")){
    			sql="insert into admin(username,name,password,age,depart) values(?,?,?,?,?)";
    		}
    		Object params[]={
    				auser.getUsername(),auser.getName(),auser.getPassword(),auser.getAge(),auser.getDepart()
    		};
    		//连接数据库
    		Connection conn=null;
    		PreparedStatement pre=null;
    		ResultSet rs=null;
    		//1.加载驱动
    		/*try {
				Class.forName("com.mysql.jdbc.Driver");
				url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
				
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				throw new ExceptionInInitializerError(e);
			}*/
    		
    		//2.获取数据库链接
    		try {
				//conn=DriverManager.getConnection(url);
    			
    			conn=getConnection();
    		//3.获取statement连接
				pre=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			    /*if(params!=null){
			    	for (int i = 0; i < params.length; i++) {
						pre.setObject(i+1, params[i]);
						
					}
			    }*/
				fillstatement(pre,params);
			//4.使用Statement对象方法executeUpdate
				pre.executeUpdate();
			//5.获取主键
				rs=pre.getGeneratedKeys();
				Object key=null;
				if(rs.next()){
					key=rs.getObject(1);
				}
				
    		} catch (Exception e) {
				// TODO 自动生成的 catch 块
				throw new RuntimeException(e);
			}finally {
				//关闭连接
				/*if(rs!=null){
					try {
						rs.close();
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						throw new RuntimeException(e);
					}finally{
						if(pre!=null){
							try {
								pre.close();
							} catch (Exception e) {
								// TODO 自动生成的 catch 块
								throw new RuntimeException(e);
							}finally {
								Connection tconn=tl.get();
								if (tconn==conn) {
									return;
								}
								if (tconn!=null) {
									try {
										conn.close();
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
										throw new RuntimeException(e);
									}
								}
							
							}
						
					}
				}*/
				release(conn,(Statement) pre,rs);
			}
    	}
		public List<Map<String, Object>> login(User user, String role) {
			// TODO 自动生成的方法存根
			String sql="";
			if(role.equals("student")){
				 sql="select * from student where username=? and password=?";
			}else if(role.equals("teacher")){
				 sql="select * from teacher where username=? and password=?";
			}else if(role.equals("admin")){
				 sql="select * from admin where username=? and password=?";
			}
    		Object params[]={
    				user.getUsername(),user.getPassword()
    		};
    		
			//连接数据库
			ResultSet rs=null;
    		Connection conn=null;
    		PreparedStatement pre=null;
    		List<Map<String,Object>> list =null;
    		//1.加载驱动
    		/*try {
				Class.forName("com.mysql.jdbc.Driver");
				url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
				
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				throw new ExceptionInInitializerError(e);
			}*/
			
    		
			//2.获取数据库链接
    		try {
				//conn=DriverManager.getConnection(url);
    			
    			conn=getConnection();
    		//3.获取statement连接
				pre=conn.prepareStatement(sql);
			    /*if(params!=null){
			    	for (int i = 0; i < params.length; i++) {
						pre.setObject(i+1, params[i]);
						
					}
			    }*/
				fillstatement(pre,params);
			//4.使用Statement对象方法executeQuery
				rs=pre.executeQuery();
				/*//将ResultSet对象转换为list
				List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
				//获取表结构
				ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
				//通过rs逐条读取记录
				while (rs.next()) {
					//每条记录放入1个Map中
					Map<String, Object> map=new HashMap<String, Object>();
					//rsmd.getColumCount(),字段的列数
					for (int i = 1; i <=rsmd.getColumnCount(); i++) {
						//逐个字段读取出来放入map中,以键值形式存入，字段名：rsmd.getColumnName(i)，对应值 rs.getObject(i)
						map.put(rsmd.getColumnName(i), rs.getObject(i));
					}
					list.add(map);
				}*/
				
				list=RsTolist(rs);
			
				
				
    		} catch (Exception e) {
				// TODO 自动生成的 catch 块
				throw new RuntimeException(e);
			}finally {
				//关闭连接
				/*if(rs!=null){
					try {
						rs.close();
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						throw new RuntimeException(e);
					}finally{
						if(pre!=null){
							try {
								pre.close();
							} catch (Exception e) {
								// TODO 自动生成的 catch 块
								throw new RuntimeException(e);
							}finally {
								Connection tconn=tl.get();
								if (tconn==conn) {
									return;
								}
								if (tconn!=null) {
									try {
										conn.close();
									} catch (Exception e) {
										// TODO 自动生成的 catch 块
										throw new RuntimeException(e);
									}
								}
							
							}
						
					}
				}*/
				release(conn,(Statement) pre,rs);
			}
			return list;
		}
		public User state(User user,String role) {
			// TODO 自动生成的方法存根
			String username=user.getUsername();
			
			return findByNumber( username, role);
		}
		//按账号查找
	public User findByNumber(String username,String role) {
		User user=null;
		String sql="";
		if(role.equals("student")){
			sql="select * from student where username=?";
		}else if(role.equals("teacher")){
			sql="select * from teacher where username=?";
		}else if(role.equals("admin")){
			sql="select * from admin where username=?";
		}
		//将number存入params[],以list形式与sql连结起来
		Object params[]={username};
		
		//连接数据库
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;
		List<Map<String,Object>> list =null;
		//1.加载驱动
		/*try {
			Class.forName("com.mysql.jdbc.Driver");
			url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			throw new ExceptionInInitializerError(e);
		}*/
		
		
		//2.获取数据库链接
		try {
			//conn=DriverManager.getConnection(url);
			
			conn=getConnection();
		//3.获取statement连接
			pre=conn.prepareStatement(sql);
		    /*if(params!=null){
		    	for (int i = 0; i < params.length; i++) {
					pre.setObject(i+1, params[i]);
					
				}
		    }*/
			fillstatement(pre,params);
		//4.使用Statement对象方法executeQuery
			rs=pre.executeQuery();
			/*//将ResultSet对象转换为list
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			//获取表结构
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
			//通过rs逐条读取记录
			while (rs.next()) {
				//每条记录放入1个Map中
				Map<String, Object> map=new HashMap<String, Object>();
				//rsmd.getColumCount(),字段的列数
				for (int i = 1; i <=rsmd.getColumnCount(); i++) {
					//逐个字段读取出来放入map中,以键值形式存入，字段名：rsmd.getColumnName(i)，对应值 rs.getObject(i)
					map.put(rsmd.getColumnName(i), rs.getObject(i));
				}
				list.add(map);
			}*/
			
			list=RsTolist(rs);
		
			
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			throw new RuntimeException(e);
		}finally {
			//关闭连接
			/*if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					throw new RuntimeException(e);
				}finally{
					if(pre!=null){
						try {
							pre.close();
						} catch (Exception e) {
							// TODO 自动生成的 catch 块
							throw new RuntimeException(e);
						}finally {
							Connection tconn=tl.get();
							if (tconn==conn) {
								return;
							}
							if (tconn!=null) {
								try {
									conn.close();
								} catch (Exception e) {
									// TODO 自动生成的 catch 块
									throw new RuntimeException(e);
								}
							}
						
						}
					
				}
			}*/
			release(conn,(Statement) pre,rs);
		}
		
		//list不为空
		if(!list.isEmpty()){
			user=new User();
			user.setId((Integer)list.get(0).get("id"));
			user.setUsername((String)list.get(0).get("username"));
			user.setPassword((String)list.get(0).get("password"));
			user.setState( (Integer) list.get(0).get("state"));
			user.setName((String)list.get(0).get("name"));
			user.setDepart((String)list.get(0).get("depart"));
			user.setAge((Integer)list.get(0).get("age"));
		}
		System.out.println("list"+list);
		System.out.println("user"+user);
		return user;
		
	}
		
		
		
		
	
}
