package com.hgh.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class JDBCUtils {
	private static String driver;
	private static String url;
	private static String name;
	private static String password;
	
	private static ThreadLocal<Connection> tl=new ThreadLocal<Connection>();//事务专用
	
	//静态模块中的代码在类被加载时执行且仅执行一次
	static{
		//从配置文件中读取参数
		InputStream in =JDBCUtils.class.getClassLoader().getResourceAsStream("dbConfig.properties");
		Properties prop =new Properties();
		try {
			prop.load(in);
			driver =prop.getProperty("driverClassName");
			url =prop.getProperty("url");
			name =prop.getProperty("name");
			password =prop.getProperty("password");
			//加载驱动
			Class.forName(driver);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
		
	}
	//获取数据库链接
	//  需改写 ，如果是事务，提交给它tl中的conn，若不是事务，给他创建一个新的
	
	public static Connection getConnection() throws SQLException{
		Connection conn =tl.get();
		if(conn!=null){//事务已开启
			return conn;
		}
			return DriverManager.getConnection(url,name,password);
	
	}
	//关闭连接
	public static void release(Connection conn,Statement stmt,ResultSet rs){
		try{
			if(rs!=null){
				rs.close();
			}
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			try{
				if(stmt!=null){
					stmt.close();
				}
			}catch(SQLException e){
				throw new RuntimeException(e);
			}finally{
				Connection tconn=tl.get();
				if(tconn==conn){//是事务
					return;
				}
				try{
					if(conn!=null){
						conn.close();
					}
				}catch(SQLException e){
					throw new RuntimeException(e);
				}
			}
		}
	}
	//开启事务
	public static void beginTranscation() throws SQLException{
		/*
		 * 1.从tl中获取Connection对象
		 * 2.如果tl有值则表示已开启，跳出异常
		 * 3.tl无值则表示无开启，创建Connection事务，开启事务
		 * 4.。将事务存入tl
		 */
		// 1.从tl中获取Connection对象
		Connection conn= tl.get();
		//2.如果tl有值则表示已开启，跳出异常
		if(conn!=null){
			throw new SQLException("事务已开启，请勿重复此操作！");
		}
		// 3.tl无值则表示无开启，创建Connection事务，开启事务
		else{
			conn=getConnection();
			conn.setAutoCommit(false);//事务开启
			tl.set(conn);
		}
	}
	//提交事务
	public static void commitTranscation() throws SQLException{
		/*
		 * 1.获取connection对象，
		 * 2.若connection对象为空，则无事务 抛出异常
		 * 3.若不为空，提交事务，关闭连接，tl中的conn清空
		 */
		
		//1.获取connection对象
		Connection conn=tl.get();
		//2.若connection对象为空，则无事务 抛出异常
		if(conn==null){
			throw new SQLException("无可提交的事务！");
		}
		//3.若不为空，提交事务，关闭连接，tl中的conn清空
		else{
			conn.commit();
			conn.close();
			conn=null;
			tl.remove();
		}
	}
	//回滚事务
	public static void rollbacktTranscation() throws SQLException{
		/*
		 * 1.获取connection对象，
		 * 2.若connection对象为空，则无事务 抛出异常
		 * 3.若不为空，提交事务，关闭连接，tl中的conn清空
		 */
		
		//1.获取connection对象
		Connection conn=tl.get();
		//2.若connection对象为空，则无事务回滚 抛出异常
		if(conn==null){
			throw new SQLException("无可回滚的事务！");
		}
		//3.若不为空，回滚事务，关闭连接，tl中的conn清空
		else{
			conn.rollback();
			conn.close();
			conn=null;
			tl.remove();
		}
	}
	
	
	//增删改查
	//查找
	public static List<Map<String,Object>> select(String sql,Object ...params){
		
		/*
		 * 1.获取数据库连接
		 * 2.获取Statement对象
		 * 3.使用statement对象方法executeQuery
		 * 4.关闭连接
		 */
		
		
		Connection conn=null;
		PreparedStatement pre=null;
		ResultSet rs=null;
		List<Map<String,Object>> list =null;
		
		try {
			//1.获取数据库连接
			
			conn=getConnection();
			//2.1获取Statement对象
			pre=conn.prepareStatement(sql);
			fillStatement(pre, params);
			//3.使用statement对象方法executeQuery
			rs=pre.executeQuery();
			list=RsToList(rs);
			
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			throw new RuntimeException(e);
		}finally{
			release(conn,pre,rs);
		}
		
		return list;
	}
	//将ResultSet对象转换为list
	private static List<Map<String,Object>> RsToList(ResultSet rs)throws SQLException {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		//获取表结构
		ResultSetMetaData	rsmd = rs.getMetaData();
		//通过rs逐条读取记录
			while(rs.next()){
			//每条记录放入1个Map中
			Map<String,Object> map=new HashMap<String,Object>();
			//rsmd.getColumCount(),字段的列数
			for(int i=1;i<=rsmd.getColumnCount();i++){
				//逐个字段读取出来放入map中,以键值形式存入，字段名：rsmd.getColumnName(i)，对应值 rs.getObject(i)
				map.put(rsmd.getColumnName(i), rs.getObject(i));
				}
			    list.add(map);
			}
		return list;
		
	}
	//增删改
	public static void update(String sql,Object...params){
		/*
		 * 1.获取数据库连接
		 * 2.获取Statement对象
		 * 3.使用statement对象方法executeUpdate
		 * 4.关闭连接
		 */
		
		//1.获取数据库连接
		Connection conn=null;
		PreparedStatement pre=null;
		
		try {
			//1.1加载驱动
			
			//1.2获取数据库连接
			conn=getConnection();//   需改写 ，如果是事务，提交给它tl中的conn，若不是事务，给他创建一个新的
			
			//2.1获取statement对象
			pre=conn.prepareStatement(sql);
			//2.2设置参数
			fillStatement(pre, params);
			//3.使用statement对象方法executeUpdate
			pre.executeUpdate();
			
			
			} catch (Exception e) {
			
			throw new RuntimeException(e);
		}finally{
				//4.关闭连接
				release(conn,pre,null);   //需改写，如果是事务，conn不关闭，如果不是，则关闭conn
		}
			}
	private static void fillStatement(PreparedStatement pre, Object... params) throws SQLException {
		if(params!=null){
			for(int i=0;i<params.length;i++){
				pre.setObject(i+1, params[i]);
			}
		}
	}
		//添加操作，返回自增主键
		public static Object insert(String sql,Object...params){
			/*
			 * 1.获取数据库连接
			 * 2.获取Statement对象
			 * 3.使用statement对象方法executeUpdate
			 * 4.获取主键
			 * 5.关闭连接
			 */
			
			//1.获取数据库连接
			Connection conn=null;
			PreparedStatement pre=null;
			ResultSet rs=null;
			try {
				//1.1加载驱动
				
				//1.2获取数据库连接
				conn=getConnection();
				//2.1获取statement对象
				pre=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				fillStatement(pre, params);
				//3.使用statement对象方法executeUpdate
				pre.executeUpdate();
				//4.获取主键
				rs=pre.getGeneratedKeys();
				Object key=null;
				if(rs.next()){
					key=rs.getObject(1);
					}
				return key;
				} catch (Exception e) {
				
				throw new RuntimeException(e);
			}finally{
					//5.关闭连接
				release(conn,pre,rs);
			}
	}
		//用于  update insert delete  批处理操作(不获取主键)
		public static int[] updatebatch(String sql,Object[][]params){
			Connection conn =null;
			PreparedStatement pre=null;
			ResultSet rs=null;//接收主键
			try {
				conn=getConnection();
				pre=conn.prepareStatement(sql);
				//设置参数
				if(params!=null){
					for(int i=0;i<params.length;i++){
						//设置每条记录的参数
						fillStatement(pre,params[i]);
						pre.addBatch();
					}
				}
				 return pre.executeBatch();
				
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}finally{
				release(conn,pre,rs);
			}
		}
		//用于  update insert delete  批处理操作（获取主键）
				public static <T> List<T> insertbatch(String sql,Object[][]params){
					Connection conn =null;
					PreparedStatement pre=null;
					ResultSet rs=null;//接收主键
					try {
						conn=getConnection();
						pre=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);//Statement.RETURN_GENERATED_KEYS  在数据中查入主键
						//设置参数
						if(params!=null){
							for(int i=0;i<params.length;i++){
								//设置每条记录的参数
								fillStatement(pre,params[i]);
								pre.addBatch();
							}
						}
						 pre.executeBatch();
						 rs=pre.getGeneratedKeys();//获取主键
						 List<T> list=new ArrayList<T>();//以泛型列表形式将逐渐与数据一起接收
						 while(rs.next()){
							 list.add((T)rs.getObject(1));
						 }
						 return list;
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}finally{
						release(conn,pre,rs);
					}
				}
				//用于返回单值的操作《当不确定要进行操作的单值不确定时  ，用泛型接收》
			public static <T>T selectScalar(String sql,Object ...params){
				Connection conn=null;
				PreparedStatement pre=null;
				ResultSet rs=null;
				T result=null;
				try {
					conn=getConnection();
					pre=conn.prepareStatement(sql);
					fillStatement(pre, params);
					rs=pre.executeQuery();
					if(rs.next()){
						result=(T)rs.getObject(1);
					}
					
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}finally{
					release(conn, pre, rs);
				}
				return result;
			}
}
