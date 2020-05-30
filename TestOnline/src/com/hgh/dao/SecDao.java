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

import org.omg.CORBA.PUBLIC_MEMBER;

import com.hgh.domain.Class1;
import com.hgh.domain.Course;
import com.hgh.domain.User;
import com.hgh.utils.JDBCUtils;
import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;



public class SecDao {

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
	public void addclass(Class1 class1) {
		
		String sql="insert into class(classname,manager,people,depart) values(?,?,?,?)";
		Object params[]={
				class1.getClassname(),class1.getManager(),class1.getPeople(),class1.getDepart()
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
	public int  findCount(String depart, String index) {
		String sql="";
		Object params[]={};
		if(index!=null){
		sql="select count(*) from class WHERE classname LIKE '%"+index+"%' and depart='"+depart+"'  ";
		
		}else {
			 sql="select count(*) from class WHERE  depart='"+depart+"' ";
			 
		}
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
	public List<Map<String, Object>> find(String depart, String index, int startIndex, int Size) {
		String sql="";
		if(index!=null){
				sql="select * from class  where classname like '%"+index+"%' and depart = '"+depart+"' limit ?,?";
		}else {
			sql="select * from class  where  depart = '"+depart+"' limit ?,?";
		}
		Object params[]={
			startIndex,Size
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
	public static List<Map<String, Object>> findById(String id, String key){
		String sql="";
		if ("class".equals(key)) {
			sql="select * from class where id=? and state=0";
		}else if ("student".equals(key)) {
			sql="select * from student where id=? and state=0";
		}else if ("teacher".equals(key)) {
			sql="select * from teacher where id=? and state=0";
		}
		Object params[]={id};
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
	public void updateclass(Class1 class1, String key) {
		String sql="";
		if ("class".equals(key)) {
			sql="update class set classname=?,depart=?,people=?,manager=? where id=?";
		}else if ("student".equals(key)) {
			sql="";
		}else if ("teacher".equals(key)) {
			sql="";
		}
		Object params[]={
				class1.getClassname(),class1.getDepart(),class1.getPeople(),class1.getManager(),class1.getId()
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
	public void delete(int id, String key) {
		String sql="";
		if ("class".equals(key)) {
			sql="delete from class where id=?";
		}else if ("student".equals(key)) {
			sql="";
		}else if ("teacher".equals(key)) {
			sql="";
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
	public void addteacher(User user) {
		// TODO �Զ����ɵķ������
		String sql="insert into teacher(username,name,age,depart) values(?,?,?,?)";
		Object params[]={
				user.getUsername(),user.getName(),user.getAge(),user.getDepart()
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
	public void updateuser(User user, String key) {
		// TODO �Զ����ɵķ������
		String sql="";
		 if ("student".equals(key)) {
			sql="update student set name=?,depart=?,age=?,username=? where id=?";
		}else if ("teacher".equals(key)) {
			sql="update teacher set name=?,depart=?,age=?,username=? where id=?";
		}
		Object params[]={
				user.getName(),user.getDepart(),user.getAge(),user.getUsername(),user.getId()
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
	public List<Map<String, Object>> findteacher(String depart, String index, int startIndex, int Size) {
		String sql="";
		if(index!=null){
				sql="select * from teacher  where name like '%"+index+"%' and depart = '"+depart+"'and state=0 limit ?,?";
		}else {
			sql="select * from teacher  where  depart = '"+depart+"'and state=0 limit ?,?";
		}
		Object params[]={
			startIndex,Size
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
	public int  findCountteacher(String depart, String index) {
		String sql="";
		Object params[]={};
		if(index!=null){
		sql="select count(*) from teacher WHERE name LIKE '%"+index+"%' and depart='"+depart+"' and state=0  ";
		
		}else {
			 sql="select count(*) from teacher WHERE  depart='"+depart+"' and state=0 ";
			 
		}
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
	public void resetpassword(String key, int id) {
		
				String sql="";
				 if ("student".equals(key)) {
					 sql="update student set password=? where id=?";
				}else if ("teacher".equals(key)) {
					sql="update teacher set password=? where id=?";
				}
				Object params[]={
						"123456",id
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
	public void addstudent(User user) {
		// TODO �Զ����ɵķ������
				String sql="insert into student(username,name,age,depart) values(?,?,?,?)";
				Object params[]={
						user.getUsername(),user.getName(),user.getAge(),user.getDepart()
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
	public int findCountstudent(String depart, String index) {
		String sql="";
		Object params[]={};
		if(index!=null){
		sql="select count(*) from student WHERE class LIKE '%"+index+"%' and depart='"+depart+"' and state=0  ";
		
		}else {
			 sql="select count(*) from student WHERE  depart='"+depart+"' and state=0 ";
			 
		}
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
	public List<Map<String, Object>> findstudent(String depart, String index, int startIndex, int pageSize) {
		String sql="";
		if(index!=null){
				sql="select * from student  where class like '%"+index+"%' and depart = '"+depart+"'and state=0 limit ?,?";
		}else {
			sql="select * from student  where  depart = '"+depart+"' and state=0 limit ?,?";
		}
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
	public void addcourse(Course course) {
		String sql="insert into course(coursename,courseteacher) values(?,?)";
		Object params[]={
				course.getCoursename(),course.getCourseteacher()
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
	public List<Map<String, Object>> findcourseid(String coursename, String courseteacher) {
		String sql="";
		
				sql="select * from course  where coursename = ? and courseteacher = ?";
		
			
		
		Object params[]={
			coursename,courseteacher
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
	public List<Map<String, Object>> findbyteachername(String courseteacher) {
		String sql="";
		
		sql="select * from teacher  where  username = ?";

	

Object params[]={
	courseteacher
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
	
	list=RsTolist(rs);System.out.println(list+"jgsgjsd");
	
	
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
	public List<Map<String, Object>> findcoursebyid(int id) {
String sql="";
		
		sql="select * from course  where  id = ?";

	

Object params[]={
	id
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
	
	list=RsTolist(rs);System.out.println(list+"jgsgjsd");
	
	
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
	public int findCountcourse(int depart, String index) {
		String sql="";
		Object params[]={};
		if(depart==1){
		
		sql="select count(*) from course WHERE id ='"+index+"'and state=0 ";
		 
		}else {
			sql="select count(*) from course WHERE coursename ='"+index+"'and state=0  ";
		}
		
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
	public List<Map<String, Object>> findcourse(int depart, String index, int startIndex, int pageSize) {
		String sql="";
		if(depart==1){
				sql="select * from course  where id  = '"+index+"'and state=0 limit ?,?";
		}else {
			sql="select * from course  where  coursename = '"+index+"'and state=0 limit ?,?";
		}
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
	public void deletecourse(int id, String key) {
		String sql="";
		
			sql="update course set state=1 where id=?";
		
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
	public void deleteteacher(int id, String key) {
		String sql="";
		if("teacher".equals(key)){
			sql="update teacher set state=1 where id=?";
		}
		if("student".equals(key)){
			sql="update student set state=1 where id=?";
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
	public int selectpeople(int id) {
		String sql="";
		Object params[]={};
		
		
		sql="select people from class WHERE id ='"+id+"' ";
		 
		
		
		
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
	public List<User> findAllTeacher() {
		List<User> list1=new ArrayList<User>();
		
			String sql="";
			
			sql="select * from teacher  where  state = ?";

		

	Object params[]={
		0
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
			  User tea=new User();
			  tea.setId(rs.getInt("id"));
			  tea.setUsername(rs.getString("username"));
			  tea.setName(rs.getString("name"));
			  tea.setPassword(rs.getString("password"));
			  tea.setAge(rs.getInt("age"));
			  tea.setDepart("depart");
			  tea.setState(rs.findColumn("state"));
			  list1.add(tea);
		}
		for (User teacherBean : list1) {
			
			System.out.println(teacherBean);
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
	
	   return list1;
	}
	public List<User> exportStudent(String classname) {
		List<User> list1=new ArrayList<User>();
		
		String sql="";
		
		sql="select * from student  where  state = ? and class=?";

	

Object params[]={
	0,classname
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
		  User tea=new User();
		  tea.setId(rs.getInt("id"));
		  tea.setUsername(rs.getString("username"));
		  tea.setName(rs.getString("name"));
		  tea.setPassword(rs.getString("password"));
		  tea.setAge(rs.getInt("age"));
		  tea.setDepart("depart");
		  tea.setState(rs.findColumn("state"));
		  list1.add(tea);
	}
	for (User teacherBean : list1) {
		
		System.out.println(teacherBean);
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

   return list1;
	}
	public List<Course> findAllCourse() {
List<Course> list1=new ArrayList<Course>();
		
		String sql="";
		
		sql="select * from course  where  state = ? ";

	

Object params[]={
	0
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
		  Course tea=new Course();
		  tea.setId(rs.getInt("id"));
		  
		  tea.setCoursename(rs.getString("coursename"));
		  tea.setCourseteacher(rs.getString("courseteacher"));
		  
		  tea.setState(rs.findColumn("state"));
		  list1.add(tea);
	}
	for (Course teacherBean : list1) {
		
		System.out.println(teacherBean);
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

   return list1;
	}
	
}

