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
	        	//��������
	        	//��ȡ�ļ�
	        	InputStream in=JDBCUtils.class.getClassLoader().getResourceAsStream("dbconfig.properties");
	        	Properties prop=new Properties();
	        	
					try {
						prop.load(in);
					} catch (IOException e) {
						// TODO �Զ����ɵ� catch ��
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
						// TODO �Զ����ɵ� catch ��
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
				// TODO �Զ����ɵķ������
	    		if(params!=null){
			    	for (int i = 0; i < params.length; i++) {
						pre.setObject(i+1, params[i]);
						
					}
	    		}
			}
	    	private static void release(Connection conn, Statement stmt, ResultSet rs) {
				// TODO �Զ����ɵķ������
	    		if(rs!=null){
					try {
						rs.close();
					} catch (Exception e) {
						// TODO �Զ����ɵ� catch ��
						throw new RuntimeException(e);
					}finally{
						if(stmt!=null){
							try {
								stmt.close();
							} catch (Exception e) {
								// TODO �Զ����ɵ� catch ��
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
										// TODO �Զ����ɵ� catch ��
										throw new RuntimeException(e);
									}
								}
							
							}
							}
						}
					}
			}
	    	private static List<Map<String, Object>> RsTolist(ResultSet rs) throws SQLException {
				// TODO �Զ����ɵķ������
	    		//��ResultSet����ת��Ϊlist
				List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
				//��ȡ��ṹ
				ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
				//ͨ��rs������ȡ��¼
				while (rs.next()) {
					//ÿ����¼����1��Map��
					Map<String, Object> map=new HashMap<String, Object>();
					//rsmd.getColumCount(),�ֶε�����
					for (int i = 1; i <=rsmd.getColumnCount(); i++) {
						//����ֶζ�ȡ��������map��,�Լ�ֵ��ʽ���룬�ֶ�����rsmd.getColumnName(i)����Ӧֵ rs.getObject(i)
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
				 * 1.��ȡ���ݿ�����
				 * 2.��ȡStatement����
				 * 3.ʹ��statement���󷽷�executeUpdate
				 * 4.�ر�����
				 */
				//�������ݿ�
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeUpdate
					pre.executeUpdate();
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
				 
				
				
				//�������ݿ�
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				Object result = null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeQuery
					rs=pre.executeQuery();
					/*//��ResultSet����ת��Ϊlist
					List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
					//��ȡ��ṹ
					ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
					//ͨ��rs������ȡ��¼
					while (rs.next()) {
						//ÿ����¼����1��Map��
						Map<String, Object> map=new HashMap<String, Object>();
						//rsmd.getColumCount(),�ֶε�����
						for (int i = 1; i <=rsmd.getColumnCount(); i++) {
							//����ֶζ�ȡ��������map��,�Լ�ֵ��ʽ���룬�ֶ�����rsmd.getColumnName(i)����Ӧֵ rs.getObject(i)
							map.put(rsmd.getColumnName(i), rs.getObject(i));
						}
						list.add(map);
					}*/
					
					if(rs.next()){
						result= rs.getObject(1);
					}
				
					
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
				//�������ݿ�
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				List<Map<String,Object>> list =null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeQuery
					rs=pre.executeQuery();
					/*//��ResultSet����ת��Ϊlist
					List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
					//��ȡ��ṹ
					ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
					//ͨ��rs������ȡ��¼
					while (rs.next()) {
						//ÿ����¼����1��Map��
						Map<String, Object> map=new HashMap<String, Object>();
						//rsmd.getColumCount(),�ֶε�����
						for (int i = 1; i <=rsmd.getColumnCount(); i++) {
							//����ֶζ�ȡ��������map��,�Լ�ֵ��ʽ���룬�ֶ�����rsmd.getColumnName(i)����Ӧֵ rs.getObject(i)
							map.put(rsmd.getColumnName(i), rs.getObject(i));
						}
						list.add(map);
					}*/
					
					list=RsTolist(rs);
				
					
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
		//�������ݿ�
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;

		//1.��������
		/*try {
			Class.forName("com.mysql.jdbc.Driver");
			url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new ExceptionInInitializerError(e);
		}*/


		//2.��ȡ���ݿ�����
		try {
			//conn=DriverManager.getConnection(url);
			
			conn=getConnection();
		//3.��ȡstatement����
			pre=conn.prepareStatement(sql);
		    /*if(params!=null){
		    	for (int i = 0; i < params.length; i++) {
					pre.setObject(i+1, params[i]);
					
				}
		    }*/
			fillstatement(pre,params);
		//4.ʹ��Statement���󷽷�executeQuery
			rs=pre.executeQuery();
			/*//��ResultSet����ת��Ϊlist
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			//��ȡ��ṹ
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();*/
			//ͨ��rs������ȡ��¼
			while (rs.next()) {
				//ÿ����¼����1��Map��
				
				  tea.setId(rs.getInt("id"));
				  
				  tea.setCoursename(rs.getString("coursename"));
				  tea.setCourseteacher(rs.getString("courseteacher"));
				  
				  tea.setState(rs.findColumn("state"));
				  
			}
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new RuntimeException(e);
		}finally {
			//�ر�����
			/*if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally{
					if(pre!=null){
						try {
							pre.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
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
									// TODO �Զ����ɵ� catch ��
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
				
				//�������ݿ�
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				Object result = null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeQuery
					rs=pre.executeQuery();
					/*//��ResultSet����ת��Ϊlist
					List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
					//��ȡ��ṹ
					ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
					//ͨ��rs������ȡ��¼
					while (rs.next()) {
						//ÿ����¼����1��Map��
						Map<String, Object> map=new HashMap<String, Object>();
						//rsmd.getColumCount(),�ֶε�����
						for (int i = 1; i <=rsmd.getColumnCount(); i++) {
							//����ֶζ�ȡ��������map��,�Լ�ֵ��ʽ���룬�ֶ�����rsmd.getColumnName(i)����Ӧֵ rs.getObject(i)
							map.put(rsmd.getColumnName(i), rs.getObject(i));
						}
						list.add(map);
					}*/
					
					if(rs.next()){
						result= rs.getObject(1);
					}
				
					
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
				//�������ݿ�
				Connection conn=null;
				PreparedStatement pre=null;
				ResultSet rs=null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeUpdate
					pre.executeUpdate();
				//5.��ȡ����
					rs=pre.getGeneratedKeys();
					Object key=null;
					if(rs.next()){
						key=rs.getObject(1);
					}
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
				 * 1.��ȡ���ݿ�����
				 * 2.��ȡStatement����
				 * 3.ʹ��statement���󷽷�executeUpdate
				 * 4.�ر�����
				 */
				//�������ݿ�
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeUpdate
					pre.executeUpdate();
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
				//�������ݿ�
				Connection conn=null;
				PreparedStatement pre=null;
				ResultSet rs=null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeUpdate
					pre.executeUpdate();
				//5.��ȡ����
					rs=pre.getGeneratedKeys();
					Object key=null;
					if(rs.next()){
						key=rs.getObject(1);
					}
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
				//�������ݿ�
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				List<Map<String,Object>> list =null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeQuery
					rs=pre.executeQuery();
					/*//��ResultSet����ת��Ϊlist
					List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
					//��ȡ��ṹ
					ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
					//ͨ��rs������ȡ��¼
					while (rs.next()) {
						//ÿ����¼����1��Map��
						Map<String, Object> map=new HashMap<String, Object>();
						//rsmd.getColumCount(),�ֶε�����
						for (int i = 1; i <=rsmd.getColumnCount(); i++) {
							//����ֶζ�ȡ��������map��,�Լ�ֵ��ʽ���룬�ֶ�����rsmd.getColumnName(i)����Ӧֵ rs.getObject(i)
							map.put(rsmd.getColumnName(i), rs.getObject(i));
						}
						list.add(map);
					}*/
					
					list=RsTolist(rs);
				
					
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
			 * 1.��ȡ���ݿ�����
			 * 2.��ȡStatement����
			 * 3.ʹ��statement���󷽷�executeUpdate
			 * 4.�ر�����
			 */
			//�������ݿ�
			ResultSet rs=null;
			Connection conn=null;
			PreparedStatement pre=null;
			
			//1.��������
			/*try {
				Class.forName("com.mysql.jdbc.Driver");
				url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
				
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				throw new ExceptionInInitializerError(e);
			}*/
			
			
			//2.��ȡ���ݿ�����
			try {
				//conn=DriverManager.getConnection(url);
				
				conn=getConnection();
			//3.��ȡstatement����
				pre=conn.prepareStatement(sql);
			    /*if(params!=null){
			    	for (int i = 0; i < params.length; i++) {
						pre.setObject(i+1, params[i]);
						
					}
			    }*/
				fillstatement(pre,params);
			//4.ʹ��Statement���󷽷�executeUpdate
				pre.executeUpdate();
				
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				throw new RuntimeException(e);
			}finally {
				//�ر�����
				/*if(rs!=null){
					try {
						rs.close();
					} catch (Exception e) {
						// TODO �Զ����ɵ� catch ��
						throw new RuntimeException(e);
					}finally{
						if(pre!=null){
							try {
								pre.close();
							} catch (Exception e) {
								// TODO �Զ����ɵ� catch ��
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
										// TODO �Զ����ɵ� catch ��
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
				//�������ݿ�
				Connection conn=null;
				PreparedStatement pre=null;
				ResultSet rs=null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeUpdate
					pre.executeUpdate();
				//5.��ȡ����
					rs=pre.getGeneratedKeys();
					Object key=null;
					if(rs.next()){
						key=rs.getObject(1);
					}
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
				
				//�������ݿ�
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				Object result = null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeQuery
					rs=pre.executeQuery();
					/*//��ResultSet����ת��Ϊlist
					List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
					//��ȡ��ṹ
					ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
					//ͨ��rs������ȡ��¼
					while (rs.next()) {
						//ÿ����¼����1��Map��
						Map<String, Object> map=new HashMap<String, Object>();
						//rsmd.getColumCount(),�ֶε�����
						for (int i = 1; i <=rsmd.getColumnCount(); i++) {
							//����ֶζ�ȡ��������map��,�Լ�ֵ��ʽ���룬�ֶ�����rsmd.getColumnName(i)����Ӧֵ rs.getObject(i)
							map.put(rsmd.getColumnName(i), rs.getObject(i));
						}
						list.add(map);
					}*/
					
					if(rs.next()){
						result= rs.getObject(1);
					}
				
					
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
			 * 1.��ȡ���ݿ�����
			 * 2.��ȡStatement����
			 * 3.ʹ��statement���󷽷�executeUpdate
			 * 4.�ر�����
			 */
			//�������ݿ�
			ResultSet rs=null;
			Connection conn=null;
			PreparedStatement pre=null;
			
			//1.��������
			/*try {
				Class.forName("com.mysql.jdbc.Driver");
				url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
				
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				throw new ExceptionInInitializerError(e);
			}*/
			
			
			//2.��ȡ���ݿ�����
			try {
				//conn=DriverManager.getConnection(url);
				
				conn=getConnection();
			//3.��ȡstatement����
				pre=conn.prepareStatement(sql);
			    /*if(params!=null){
			    	for (int i = 0; i < params.length; i++) {
						pre.setObject(i+1, params[i]);
						
					}
			    }*/
				fillstatement(pre,params);
			//4.ʹ��Statement���󷽷�executeUpdate
				pre.executeUpdate();
				
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				throw new RuntimeException(e);
			}finally {
				//�ر�����
				/*if(rs!=null){
					try {
						rs.close();
					} catch (Exception e) {
						// TODO �Զ����ɵ� catch ��
						throw new RuntimeException(e);
					}finally{
						if(pre!=null){
							try {
								pre.close();
							} catch (Exception e) {
								// TODO �Զ����ɵ� catch ��
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
										// TODO �Զ����ɵ� catch ��
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
		//�������ݿ�
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;

		//1.��������
		/*try {
			Class.forName("com.mysql.jdbc.Driver");
			url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new ExceptionInInitializerError(e);
		}*/


		//2.��ȡ���ݿ�����
		try {
			//conn=DriverManager.getConnection(url);
			
			conn=getConnection();
		//3.��ȡstatement����
			pre=conn.prepareStatement(sql);
		    /*if(params!=null){
		    	for (int i = 0; i < params.length; i++) {
					pre.setObject(i+1, params[i]);
					
				}
		    }*/
			fillstatement(pre,params);
		//4.ʹ��Statement���󷽷�executeQuery
			rs=pre.executeQuery();
			/*//��ResultSet����ת��Ϊlist
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			//��ȡ��ṹ
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();*/
			//ͨ��rs������ȡ��¼
			int i=0;
			while (rs.next()) {
				//ÿ����¼����1��Map��
				 list[i]=rs.getInt("id");
				 i++;
			}
			
			
			
			
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new RuntimeException(e);
		}finally {
			//�ر�����
			/*if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally{
					if(pre!=null){
						try {
							pre.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
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
									// TODO �Զ����ɵ� catch ��
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
				//�������ݿ�
				Connection conn=null;
				PreparedStatement pre=null;
				ResultSet rs=null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeUpdate
					pre.executeUpdate();
				//5.��ȡ����
					rs=pre.getGeneratedKeys();
					Object key=null;
					if(rs.next()){
						key=rs.getObject(1);
					}
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
				 
				
				
				//�������ݿ�
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				Object result = null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeQuery
					rs=pre.executeQuery();
					/*//��ResultSet����ת��Ϊlist
					List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
					//��ȡ��ṹ
					ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
					//ͨ��rs������ȡ��¼
					while (rs.next()) {
						//ÿ����¼����1��Map��
						Map<String, Object> map=new HashMap<String, Object>();
						//rsmd.getColumCount(),�ֶε�����
						for (int i = 1; i <=rsmd.getColumnCount(); i++) {
							//����ֶζ�ȡ��������map��,�Լ�ֵ��ʽ���룬�ֶ�����rsmd.getColumnName(i)����Ӧֵ rs.getObject(i)
							map.put(rsmd.getColumnName(i), rs.getObject(i));
						}
						list.add(map);
					}*/
					
					if(rs.next()){
						result= rs.getObject(1);
					}
				
					
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
		//�������ݿ�
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;

		//1.��������
		/*try {
			Class.forName("com.mysql.jdbc.Driver");
			url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new ExceptionInInitializerError(e);
		}*/


		//2.��ȡ���ݿ�����
		try {
			//conn=DriverManager.getConnection(url);
			
			conn=getConnection();
		//3.��ȡstatement����
			pre=conn.prepareStatement(sql);
		    /*if(params!=null){
		    	for (int i = 0; i < params.length; i++) {
					pre.setObject(i+1, params[i]);
					
				}
		    }*/
			fillstatement(pre,params);
		//4.ʹ��Statement���󷽷�executeQuery
			rs=pre.executeQuery();
			/*//��ResultSet����ת��Ϊlist
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			//��ȡ��ṹ
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();*/
			//ͨ��rs������ȡ��¼
			int i=0;
			while (rs.next()) {
				//ÿ����¼����1��Map��
				 list[i]=rs.getInt("questionid");
				 i++;
			}
			
			
			
			
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new RuntimeException(e);
		}finally {
			//�ر�����
			/*if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally{
					if(pre!=null){
						try {
							pre.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
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
									// TODO �Զ����ɵ� catch ��
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
		//�������ݿ�
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;
		List<Map<String,Object>> list =null;
		//1.��������
		/*try {
			Class.forName("com.mysql.jdbc.Driver");
			url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new ExceptionInInitializerError(e);
		}*/
		
		
		//2.��ȡ���ݿ�����
		try {
			//conn=DriverManager.getConnection(url);
			
			conn=getConnection();
		//3.��ȡstatement����
			pre=conn.prepareStatement(sql);
		    /*if(params!=null){
		    	for (int i = 0; i < params.length; i++) {
					pre.setObject(i+1, params[i]);
					
				}
		    }*/
			fillstatement(pre,params);
		//4.ʹ��Statement���󷽷�executeQuery
			rs=pre.executeQuery();
			/*//��ResultSet����ת��Ϊlist
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			//��ȡ��ṹ
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
			//ͨ��rs������ȡ��¼
			while (rs.next()) {
				//ÿ����¼����1��Map��
				Map<String, Object> map=new HashMap<String, Object>();
				//rsmd.getColumCount(),�ֶε�����
				for (int i = 1; i <=rsmd.getColumnCount(); i++) {
					//����ֶζ�ȡ��������map��,�Լ�ֵ��ʽ���룬�ֶ�����rsmd.getColumnName(i)����Ӧֵ rs.getObject(i)
					map.put(rsmd.getColumnName(i), rs.getObject(i));
				}
				list.add(map);
			}*/
			
			list=RsTolist(rs);
		
			
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new RuntimeException(e);
		}finally {
			//�ر�����
			/*if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally{
					if(pre!=null){
						try {
							pre.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
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
									// TODO �Զ����ɵ� catch ��
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
		//�������ݿ�
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;

		//1.��������
		/*try {
			Class.forName("com.mysql.jdbc.Driver");
			url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new ExceptionInInitializerError(e);
		}*/


		//2.��ȡ���ݿ�����
		try {
			//conn=DriverManager.getConnection(url);
			
			conn=getConnection();
		//3.��ȡstatement����
			pre=conn.prepareStatement(sql);
		    /*if(params!=null){
		    	for (int i = 0; i < params.length; i++) {
					pre.setObject(i+1, params[i]);
					
				}
		    }*/
			fillstatement(pre,params);
		//4.ʹ��Statement���󷽷�executeQuery
			rs=pre.executeQuery();
			/*//��ResultSet����ת��Ϊlist
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			//��ȡ��ṹ
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();*/
			//ͨ��rs������ȡ��¼
			while (rs.next()) {
				//ÿ����¼����1��Map��
				 
				  tea.setId(rs.getInt("id"));
				  
				  tea.setCoursename(rs.getString("coursename"));
				  tea.setQuestionmatter(rs.getString("questionmatter"));
				  tea.setQuestionname(rs.getString("questionname"));
				  tea.setLevel(rs.getInt("level"));
				  tea.setAnswer(rs.getString("answer"));
				  tea.setType(rs.getString("type"));
			}
			
			
			
			
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new RuntimeException(e);
		}finally {
			//�ر�����
			/*if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally{
					if(pre!=null){
						try {
							pre.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
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
									// TODO �Զ����ɵ� catch ��
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
				//�������ݿ�
				Connection conn=null;
				PreparedStatement pre=null;
				ResultSet rs=null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeUpdate
					pre.executeUpdate();
				//5.��ȡ����
					rs=pre.getGeneratedKeys();
					Object key=null;
					if(rs.next()){
						key=rs.getObject(1);
					}
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
		//�������ݿ�
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;

		//1.��������
		/*try {
			Class.forName("com.mysql.jdbc.Driver");
			url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new ExceptionInInitializerError(e);
		}*/


		//2.��ȡ���ݿ�����
		try {
			//conn=DriverManager.getConnection(url);
			
			conn=getConnection();
		//3.��ȡstatement����
			pre=conn.prepareStatement(sql);
		    /*if(params!=null){
		    	for (int i = 0; i < params.length; i++) {
					pre.setObject(i+1, params[i]);
					
				}
		    }*/
			fillstatement(pre,params);
		//4.ʹ��Statement���󷽷�executeQuery
			rs=pre.executeQuery();
			/*//��ResultSet����ת��Ϊlist
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			//��ȡ��ṹ
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();*/
			//ͨ��rs������ȡ��¼
			while (rs.next()) {
				//ÿ����¼����1��Map��
				 
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
			// TODO �Զ����ɵ� catch ��
			throw new RuntimeException(e);
		}finally {
			//�ر�����
			/*if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally{
					if(pre!=null){
						try {
							pre.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
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
									// TODO �Զ����ɵ� catch ��
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
			 * 1.��ȡ���ݿ�����
			 * 2.��ȡStatement����
			 * 3.ʹ��statement���󷽷�executeUpdate
			 * 4.�ر�����
			 */
			//�������ݿ�
			ResultSet rs=null;
			Connection conn=null;
			PreparedStatement pre=null;
			
			//1.��������
			/*try {
				Class.forName("com.mysql.jdbc.Driver");
				url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
				
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				throw new ExceptionInInitializerError(e);
			}*/
			
			
			//2.��ȡ���ݿ�����
			try {
				//conn=DriverManager.getConnection(url);
				
				conn=getConnection();
			//3.��ȡstatement����
				pre=conn.prepareStatement(sql);
			    /*if(params!=null){
			    	for (int i = 0; i < params.length; i++) {
						pre.setObject(i+1, params[i]);
						
					}
			    }*/
				fillstatement(pre,params);
			//4.ʹ��Statement���󷽷�executeUpdate
				pre.executeUpdate();
				
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				throw new RuntimeException(e);
			}finally {
				//�ر�����
				/*if(rs!=null){
					try {
						rs.close();
					} catch (Exception e) {
						// TODO �Զ����ɵ� catch ��
						throw new RuntimeException(e);
					}finally{
						if(pre!=null){
							try {
								pre.close();
							} catch (Exception e) {
								// TODO �Զ����ɵ� catch ��
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
										// TODO �Զ����ɵ� catch ��
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
				
				//�������ݿ�
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				Object result = null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeQuery
					rs=pre.executeQuery();
					/*//��ResultSet����ת��Ϊlist
					List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
					//��ȡ��ṹ
					ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
					//ͨ��rs������ȡ��¼
					while (rs.next()) {
						//ÿ����¼����1��Map��
						Map<String, Object> map=new HashMap<String, Object>();
						//rsmd.getColumCount(),�ֶε�����
						for (int i = 1; i <=rsmd.getColumnCount(); i++) {
							//����ֶζ�ȡ��������map��,�Լ�ֵ��ʽ���룬�ֶ�����rsmd.getColumnName(i)����Ӧֵ rs.getObject(i)
							map.put(rsmd.getColumnName(i), rs.getObject(i));
						}
						list.add(map);
					}*/
					
					if(rs.next()){
						result= rs.getObject(1);
					}
				
					
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
		//�������ݿ�
		ResultSet rs=null;
		Connection conn=null;
		PreparedStatement pre=null;

		//1.��������
		/*try {
			Class.forName("com.mysql.jdbc.Driver");
			url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new ExceptionInInitializerError(e);
		}*/


		//2.��ȡ���ݿ�����
		try {
			//conn=DriverManager.getConnection(url);
			
			conn=getConnection();
		//3.��ȡstatement����
			pre=conn.prepareStatement(sql);
		    /*if(params!=null){
		    	for (int i = 0; i < params.length; i++) {
					pre.setObject(i+1, params[i]);
					
				}
		    }*/
			fillstatement(pre,params);
		//4.ʹ��Statement���󷽷�executeQuery
			rs=pre.executeQuery();
			/*//��ResultSet����ת��Ϊlist
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
			//��ȡ��ṹ
			ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();*/
			//ͨ��rs������ȡ��¼
			int i=0;
			while (rs.next()) {
				//ÿ����¼����1��Map��
				 list[i]=rs.getInt("id");
				 i++;
			}
			
			
			
			
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new RuntimeException(e);
		}finally {
			//�ر�����
			/*if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally{
					if(pre!=null){
						try {
							pre.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
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
									// TODO �Զ����ɵ� catch ��
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
				
				//�������ݿ�
				ResultSet rs=null;
				Connection conn=null;
				PreparedStatement pre=null;
				Object result = null;
				//1.��������
				/*try {
					Class.forName("com.mysql.jdbc.Driver");
					url="jdbc:mysql://localhost:3306/manager?user=root&password=1234&useUnicode=true&characterEncoding=utf8";
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new ExceptionInInitializerError(e);
				}*/
				
				
				//2.��ȡ���ݿ�����
				try {
					//conn=DriverManager.getConnection(url);
					
					conn=getConnection();
				//3.��ȡstatement����
					pre=conn.prepareStatement(sql);
				    /*if(params!=null){
				    	for (int i = 0; i < params.length; i++) {
							pre.setObject(i+1, params[i]);
							
						}
				    }*/
					fillstatement(pre,params);
				//4.ʹ��Statement���󷽷�executeQuery
					rs=pre.executeQuery();
					/*//��ResultSet����ת��Ϊlist
					List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
					//��ȡ��ṹ
					ResultSetMetaData rsmd= (ResultSetMetaData) rs.getMetaData();
					//ͨ��rs������ȡ��¼
					while (rs.next()) {
						//ÿ����¼����1��Map��
						Map<String, Object> map=new HashMap<String, Object>();
						//rsmd.getColumCount(),�ֶε�����
						for (int i = 1; i <=rsmd.getColumnCount(); i++) {
							//����ֶζ�ȡ��������map��,�Լ�ֵ��ʽ���룬�ֶ�����rsmd.getColumnName(i)����Ӧֵ rs.getObject(i)
							map.put(rsmd.getColumnName(i), rs.getObject(i));
						}
						list.add(map);
					}*/
					
					if(rs.next()){
						result= rs.getObject(1);
					}
				
					
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					throw new RuntimeException(e);
				}finally {
					//�ر�����
					/*if(rs!=null){
						try {
							rs.close();
						} catch (Exception e) {
							// TODO �Զ����ɵ� catch ��
							throw new RuntimeException(e);
						}finally{
							if(pre!=null){
								try {
									pre.close();
								} catch (Exception e) {
									// TODO �Զ����ɵ� catch ��
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
											// TODO �Զ����ɵ� catch ��
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
