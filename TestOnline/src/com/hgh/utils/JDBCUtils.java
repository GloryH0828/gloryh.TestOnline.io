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
	
	private static ThreadLocal<Connection> tl=new ThreadLocal<Connection>();//����ר��
	
	//��̬ģ���еĴ������౻����ʱִ���ҽ�ִ��һ��
	static{
		//�������ļ��ж�ȡ����
		InputStream in =JDBCUtils.class.getClassLoader().getResourceAsStream("dbConfig.properties");
		Properties prop =new Properties();
		try {
			prop.load(in);
			driver =prop.getProperty("driverClassName");
			url =prop.getProperty("url");
			name =prop.getProperty("name");
			password =prop.getProperty("password");
			//��������
			Class.forName(driver);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
		
	}
	//��ȡ���ݿ�����
	//  ���д ������������ύ����tl�е�conn�����������񣬸�������һ���µ�
	
	public static Connection getConnection() throws SQLException{
		Connection conn =tl.get();
		if(conn!=null){//�����ѿ���
			return conn;
		}
			return DriverManager.getConnection(url,name,password);
	
	}
	//�ر�����
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
				if(tconn==conn){//������
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
	//��������
	public static void beginTranscation() throws SQLException{
		/*
		 * 1.��tl�л�ȡConnection����
		 * 2.���tl��ֵ���ʾ�ѿ����������쳣
		 * 3.tl��ֵ���ʾ�޿���������Connection���񣬿�������
		 * 4.�����������tl
		 */
		// 1.��tl�л�ȡConnection����
		Connection conn= tl.get();
		//2.���tl��ֵ���ʾ�ѿ����������쳣
		if(conn!=null){
			throw new SQLException("�����ѿ����������ظ��˲�����");
		}
		// 3.tl��ֵ���ʾ�޿���������Connection���񣬿�������
		else{
			conn=getConnection();
			conn.setAutoCommit(false);//������
			tl.set(conn);
		}
	}
	//�ύ����
	public static void commitTranscation() throws SQLException{
		/*
		 * 1.��ȡconnection����
		 * 2.��connection����Ϊ�գ��������� �׳��쳣
		 * 3.����Ϊ�գ��ύ���񣬹ر����ӣ�tl�е�conn���
		 */
		
		//1.��ȡconnection����
		Connection conn=tl.get();
		//2.��connection����Ϊ�գ��������� �׳��쳣
		if(conn==null){
			throw new SQLException("�޿��ύ������");
		}
		//3.����Ϊ�գ��ύ���񣬹ر����ӣ�tl�е�conn���
		else{
			conn.commit();
			conn.close();
			conn=null;
			tl.remove();
		}
	}
	//�ع�����
	public static void rollbacktTranscation() throws SQLException{
		/*
		 * 1.��ȡconnection����
		 * 2.��connection����Ϊ�գ��������� �׳��쳣
		 * 3.����Ϊ�գ��ύ���񣬹ر����ӣ�tl�е�conn���
		 */
		
		//1.��ȡconnection����
		Connection conn=tl.get();
		//2.��connection����Ϊ�գ���������ع� �׳��쳣
		if(conn==null){
			throw new SQLException("�޿ɻع�������");
		}
		//3.����Ϊ�գ��ع����񣬹ر����ӣ�tl�е�conn���
		else{
			conn.rollback();
			conn.close();
			conn=null;
			tl.remove();
		}
	}
	
	
	//��ɾ�Ĳ�
	//����
	public static List<Map<String,Object>> select(String sql,Object ...params){
		
		/*
		 * 1.��ȡ���ݿ�����
		 * 2.��ȡStatement����
		 * 3.ʹ��statement���󷽷�executeQuery
		 * 4.�ر�����
		 */
		
		
		Connection conn=null;
		PreparedStatement pre=null;
		ResultSet rs=null;
		List<Map<String,Object>> list =null;
		
		try {
			//1.��ȡ���ݿ�����
			
			conn=getConnection();
			//2.1��ȡStatement����
			pre=conn.prepareStatement(sql);
			fillStatement(pre, params);
			//3.ʹ��statement���󷽷�executeQuery
			rs=pre.executeQuery();
			list=RsToList(rs);
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			throw new RuntimeException(e);
		}finally{
			release(conn,pre,rs);
		}
		
		return list;
	}
	//��ResultSet����ת��Ϊlist
	private static List<Map<String,Object>> RsToList(ResultSet rs)throws SQLException {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		//��ȡ��ṹ
		ResultSetMetaData	rsmd = rs.getMetaData();
		//ͨ��rs������ȡ��¼
			while(rs.next()){
			//ÿ����¼����1��Map��
			Map<String,Object> map=new HashMap<String,Object>();
			//rsmd.getColumCount(),�ֶε�����
			for(int i=1;i<=rsmd.getColumnCount();i++){
				//����ֶζ�ȡ��������map��,�Լ�ֵ��ʽ���룬�ֶ�����rsmd.getColumnName(i)����Ӧֵ rs.getObject(i)
				map.put(rsmd.getColumnName(i), rs.getObject(i));
				}
			    list.add(map);
			}
		return list;
		
	}
	//��ɾ��
	public static void update(String sql,Object...params){
		/*
		 * 1.��ȡ���ݿ�����
		 * 2.��ȡStatement����
		 * 3.ʹ��statement���󷽷�executeUpdate
		 * 4.�ر�����
		 */
		
		//1.��ȡ���ݿ�����
		Connection conn=null;
		PreparedStatement pre=null;
		
		try {
			//1.1��������
			
			//1.2��ȡ���ݿ�����
			conn=getConnection();//   ���д ������������ύ����tl�е�conn�����������񣬸�������һ���µ�
			
			//2.1��ȡstatement����
			pre=conn.prepareStatement(sql);
			//2.2���ò���
			fillStatement(pre, params);
			//3.ʹ��statement���󷽷�executeUpdate
			pre.executeUpdate();
			
			
			} catch (Exception e) {
			
			throw new RuntimeException(e);
		}finally{
				//4.�ر�����
				release(conn,pre,null);   //���д�����������conn���رգ�������ǣ���ر�conn
		}
			}
	private static void fillStatement(PreparedStatement pre, Object... params) throws SQLException {
		if(params!=null){
			for(int i=0;i<params.length;i++){
				pre.setObject(i+1, params[i]);
			}
		}
	}
		//��Ӳ�����������������
		public static Object insert(String sql,Object...params){
			/*
			 * 1.��ȡ���ݿ�����
			 * 2.��ȡStatement����
			 * 3.ʹ��statement���󷽷�executeUpdate
			 * 4.��ȡ����
			 * 5.�ر�����
			 */
			
			//1.��ȡ���ݿ�����
			Connection conn=null;
			PreparedStatement pre=null;
			ResultSet rs=null;
			try {
				//1.1��������
				
				//1.2��ȡ���ݿ�����
				conn=getConnection();
				//2.1��ȡstatement����
				pre=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				fillStatement(pre, params);
				//3.ʹ��statement���󷽷�executeUpdate
				pre.executeUpdate();
				//4.��ȡ����
				rs=pre.getGeneratedKeys();
				Object key=null;
				if(rs.next()){
					key=rs.getObject(1);
					}
				return key;
				} catch (Exception e) {
				
				throw new RuntimeException(e);
			}finally{
					//5.�ر�����
				release(conn,pre,rs);
			}
	}
		//����  update insert delete  ���������(����ȡ����)
		public static int[] updatebatch(String sql,Object[][]params){
			Connection conn =null;
			PreparedStatement pre=null;
			ResultSet rs=null;//��������
			try {
				conn=getConnection();
				pre=conn.prepareStatement(sql);
				//���ò���
				if(params!=null){
					for(int i=0;i<params.length;i++){
						//����ÿ����¼�Ĳ���
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
		//����  update insert delete  �������������ȡ������
				public static <T> List<T> insertbatch(String sql,Object[][]params){
					Connection conn =null;
					PreparedStatement pre=null;
					ResultSet rs=null;//��������
					try {
						conn=getConnection();
						pre=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);//Statement.RETURN_GENERATED_KEYS  �������в�������
						//���ò���
						if(params!=null){
							for(int i=0;i<params.length;i++){
								//����ÿ����¼�Ĳ���
								fillStatement(pre,params[i]);
								pre.addBatch();
							}
						}
						 pre.executeBatch();
						 rs=pre.getGeneratedKeys();//��ȡ����
						 List<T> list=new ArrayList<T>();//�Է����б���ʽ����������һ�����
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
				//���ڷ��ص�ֵ�Ĳ���������ȷ��Ҫ���в����ĵ�ֵ��ȷ��ʱ  ���÷��ͽ��ա�
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
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}finally{
					release(conn, pre, rs);
				}
				return result;
			}
}
