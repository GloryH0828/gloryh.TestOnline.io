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

import javax.xml.namespace.QName;

import com.hgh.domain.Course;
import com.hgh.domain.Question;
import com.hgh.utils.JDBCUtils;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;

public class TeaDao {

	
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
	    	private static Connection getConnection() throws SQLException {
				Connection conn=tl.get();
				if(conn!=null){
					return conn;
				}
				return DriverManager.getConnection(url,name,password);
			}
	    	private static void fillstatement(PreparedStatement pre, Object[] params) throws SQLException {
				// TODO 自动生成的方法存根
	    		if(params!=null){
			    	for (int i = 0; i < params.length; i++) {
						pre.setObject(i+1, params[i]);
						
					}
	    		}
			}
	    	private static void release(Connection conn, Statement stmt, ResultSet rs) {
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
	    	private static List<Map<String, Object>> RsTolist(ResultSet rs) throws SQLException {
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
			public void addmycourse(int id, String username) {
				String sql="";
				
					sql="update course set courseteacher=? where id=?";
				
				Object params[]={
						username,id
				};
				/*
				 * 1.获取数据库连接
				 * 2.获取Statement对象
				 * 3.使用statement对象方法executeUpdate
				 * 4.关闭连接
				 */
				//连接数据库
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				
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
				//4.使用Statement对象方法executeUpdate
					pre.executeUpdate();
					
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
			public int findmycourse(String username) {
				String sql="";
				Object params[]={};
				
				
				sql="select count(*) from course WHERE courseteacher ='"+username+"'and state=0 ";
				 
				
				
				//连接数据库
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				Object result = null;
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
					
					if(rs.next()){
						result= rs.getObject(1);
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
				System.out.println(result);
				Number number=(Number)result;
				return number.intValue();
			}
			public List<Map<String, Object>> findmycourse(String username, int startIndex, int pageSize) {
				String sql="";
				
						sql="select * from course  where courseteacher  = '"+username+"'and state=0 limit ?,?";
				
				Object params[]={
					startIndex,pageSize
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
			public Course toaddcourseclass(int id) {
				  Course tea=new Course();
				String sql="";
				
				sql="select * from course  where  id = ? ";
				
				
		Object params[]={
			id
		};
		//连接数据库
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;

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
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();*/
			//通过rs逐条读取记录
			while (rs.next()) {
				//每条记录放入1个Map中
				
				  tea.setId(rs.getInt("id"));
				  
				  tea.setCoursename(rs.getString("coursename"));
				  tea.setCourseteacher(rs.getString("courseteacher"));
				  
				  tea.setState(rs.findColumn("state"));
				  
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

		   return tea;
			}
			public String findcourseclass(String coursename) {
				String sql="";
				
				
				
				sql="select classname from coursetoclass WHERE coursename=? ";
				 
				
				Object params[]={coursename};
				
				//连接数据库
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				Object result = null;
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
					
					if(rs.next()){
						result= rs.getObject(1);
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
				System.out.println(result);
				
				return (String) result;
				
			}
			public void addcourseclass(String coursename, String courseclass) {
				String sql="insert into coursetoclass(coursename,classname) values(?,?)";
				Object params[]={
						coursename,courseclass
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
			public void deletecourseclass(String coursename) {
				String sql="";
					sql="delete from coursetoclass where coursename=?";
				
				Object params[]={
						coursename
				};
				/*
				 * 1.获取数据库连接
				 * 2.获取Statement对象
				 * 3.使用statement对象方法executeUpdate
				 * 4.关闭连接
				 */
				//连接数据库
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				
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
				//4.使用Statement对象方法executeUpdate
					pre.executeUpdate();
					
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
			public void addquestion(Question question) {

				String sql="insert into question(coursename,questionname,questionmatter,answer,level,type) values(?,?,?,?,?,?)";
				Object params[]={
						question.getCoursename(),question.getQuestionname(),question.getQuestionmatter(),question.getAnswer(),question.getLevel(),question.getType()
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
			public List<Map<String, Object>> findquestion(String coursename, String type) {
				String sql="";
				if ("1".equals(type)) {
					sql="select * from selectquestion  where coursename =?";
				}else {
					sql="select * from question  where coursename =? and type='"+type+"'";
				}
						
				
				Object params[]={
					coursename
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
			public void deletequestion(String id) {
				String sql="";
				sql="delete from question where id=?";
			
			Object params[]={
					id
			};
			/*
			 * 1.获取数据库连接
			 * 2.获取Statement对象
			 * 3.使用statement对象方法executeUpdate
			 * 4.关闭连接
			 */
			//连接数据库
			ResultSet rs=null;
			Connection conn=null;
			PreparedStatement pre=null;
			
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
			//4.使用Statement对象方法executeUpdate
				pre.executeUpdate();
				
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
			public void addtotest(Question question) {
				String sql="insert into maketestbyhand(questionname,questionmatter,answer,coursename,level,A,B,C,D,type) values(?,?,?,?,?,?,?,?,?,?)";
				Object params[]={
						question.getQuestionname(),question.getQuestionmatter(),question.getAnswer(),question.getCoursename(),question.getLevel(),question.getA(),question.getB(),question.getC(),
						question.getD(),question.getType()
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
			public int countquestion(String string) {
				String sql="";
				
				
				
				sql="select count(*) from question where type=?";
				 
				Object params[]={string};
				
				//连接数据库
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				Object result = null;
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
					
					if(rs.next()){
						result= rs.getObject(1);
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
				System.out.println(result);
				Number number=(Number)result;
				return number.intValue();
			}
			public void empty() {
				String sql="";
				sql="delete from maketestrandom";
			
			Object params[]={
					
			};
			/*
			 * 1.获取数据库连接
			 * 2.获取Statement对象
			 * 3.使用statement对象方法executeUpdate
			 * 4.关闭连接
			 */
			//连接数据库
			ResultSet rs=null;
			Connection conn=null;
			PreparedStatement pre=null;
			
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
			//4.使用Statement对象方法executeUpdate
				pre.executeUpdate();
				
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
			public int[] questionid(int count, String string) {
				String sql="";
				int []list=new int[count];
				sql="select id from question where type=?";

			

		Object params[]={
			string
		};
		//连接数据库
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;

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
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();*/
			//通过rs逐条读取记录
			int i=0;
			while (rs.next()) {
				//每条记录放入1个Map中
				 list[i]=rs.getInt("id");
				 i++;
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

		   return list;
			}
			public void addrandom(Question question) {
				String sql="insert into maketestrandom(questionname,questionmatter,answer,coursename,level,A,B,C,D,type) values(?,?,?,?,?,?,?,?,?,?)";
				Object params[]={
						question.getQuestionname(),question.getQuestionmatter(),question.getAnswer(),question.getCoursename(),question.getLevel(),
						question.getA(),question.getB(),question.getC(),question.getD(),question.getType()
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
			public int countrandom() {
				String sql="";
				Object params[]={};
				
				
				sql="select count(*) from maketestrandom ";
				 
				
				
				//连接数据库
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				Object result = null;
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
					
					if(rs.next()){
						result= rs.getObject(1);
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
				System.out.println(result);
				Number number=(Number)result;
				return number.intValue();
			}
			public int[] randomquestionid(int count) {
				String sql="";
				int []list=new int[count];
				sql="select questionid from maketestrandom ";

			

		Object params[]={
			
		};
		//连接数据库
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;

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
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();*/
			//通过rs逐条读取记录
			int i=0;
			while (rs.next()) {
				//每条记录放入1个Map中
				 list[i]=rs.getInt("questionid");
				 i++;
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

		   return list;
			}
			public List<Map<String, Object>> randomid(int i)  {
				String sql="";
				if (i==1) {
					sql="select * from maketestbyhand  ";
				}
				if (i==0) {
					sql="select * from maketestrandom  ";
				}
				
		
		Object params[]={
			
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
			public Question findbyid(int id) {
				String sql="";
				 Question tea=new Question();
				sql="select * from question  where  id = ? ";

			

		Object params[]={
			id
		};
		//连接数据库
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;

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
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();*/
			//通过rs逐条读取记录
			while (rs.next()) {
				//每条记录放入1个Map中
				 
				  tea.setId(rs.getInt("id"));
				  
				  tea.setCoursename(rs.getString("coursename"));
				  tea.setQuestionmatter(rs.getString("questionmatter"));
				  tea.setQuestionname(rs.getString("questionname"));
				  tea.setLevel(rs.getInt("level"));
				  tea.setAnswer(rs.getString("answer"));
				  tea.setType(rs.getString("type"));
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

		   return tea;
			}
			public void addselectquestion(Question question) {
				String sql="insert into selectquestion(coursename,questionname,questionmatter,answer,level,A,B,C,D) values(?,?,?,?,?,?,?,?,?)";
				Object params[]={
						question.getCoursename(),question.getQuestionname(),question.getQuestionmatter(),question.getAnswer(),question.getLevel(),
						question.getA(),question.getB(),question.getC(),question.getD()
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
			public Question findbyselectid(int id) {
				String sql="";
				 Question tea=new Question();
				sql="select * from selectquestion  where  id = ? ";

			

		Object params[]={
			id
		};
		//连接数据库
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;

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
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();*/
			//通过rs逐条读取记录
			while (rs.next()) {
				//每条记录放入1个Map中
				 
				  tea.setId(rs.getInt("id"));
				  
				  tea.setCoursename(rs.getString("coursename"));
				  tea.setQuestionmatter(rs.getString("questionmatter"));
				  tea.setQuestionname(rs.getString("questionname"));
				  tea.setLevel(rs.getInt("level"));
				  tea.setAnswer(rs.getString("answer"));
				  String a=rs.getString("A");
				  String b=rs.getString("B");
				  String c=rs.getString("C");
				  String d=rs.getString("D");
				  tea.setA(a);
				  
				  tea.setB(b);
				  tea.setC(c);
				  tea.setD(d);
				  tea.setType("1");
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
		return tea;
			}
			public void deletetestbyhand(String id, int i) {
				String sql="";
				if (i==1) {
					sql="delete from maketestbyhand where id=?";
				}
				if (i==2) {
					sql="delete from maketestrandom where id=?";
				}
				
			
			Object params[]={
					id
			};
			/*
			 * 1.获取数据库连接
			 * 2.获取Statement对象
			 * 3.使用statement对象方法executeUpdate
			 * 4.关闭连接
			 */
			//连接数据库
			ResultSet rs=null;
			Connection conn=null;
			PreparedStatement pre=null;
			
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
			//4.使用Statement对象方法executeUpdate
				pre.executeUpdate();
				
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
			public int countselect() {
				String sql="";
				
				
				
				sql="select count(*) from selectquestion ";
				 
				Object params[]={};
				
				//连接数据库
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				Object result = null;
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
					
					if(rs.next()){
						result= rs.getObject(1);
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
				System.out.println(result);
				Number number=(Number)result;
				return number.intValue();
			}
			public int[] questionid(int select) {
				String sql="";
				int []list=new int[select];
				sql="select id from selectquestion ";

			

		Object params[]={
			
		};
		//连接数据库
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;

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
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();*/
			//通过rs逐条读取记录
			int i=0;
			while (rs.next()) {
				//每条记录放入1个Map中
				 list[i]=rs.getInt("id");
				 i++;
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

		   return list;
			}
			public static int conutbyhand(String type) {
				String sql="";
				
				
				
				sql="select count(*) from maketestbyhand where type=? ";
				 
				Object params[]={type};
				
				//连接数据库
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				Object result = null;
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
					
					if(rs.next()){
						result= rs.getObject(1);
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
				System.out.println(result);
				Number number=(Number)result;
				return number.intValue();
			}
			
			
			
	

}
