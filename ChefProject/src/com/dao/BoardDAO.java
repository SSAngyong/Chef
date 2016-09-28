package com.dao;

import java.util.*;
import java.sql.*;

public class BoardDAO {
	private Connection conn;
	private PreparedStatement ps;
	private final String URL="jdbc:oracle:thin:@211.238.142.77:1521:ORCL";
	
	public void BoardDAO(){
		try{
			Class.forName("oracle.jdbc.driver.OracleDirver");			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void getConnection(){
		try{
			conn=DriverManager.getConnection(URL,"scott","tiger");
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void disConnection(){
		try{
			if(ps!=null)ps.close();
			if(conn!=null)conn.close();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public List<BoardDTO> chefListData(int page){
		ArrayList<BoardDTO> list=new ArrayList();
		try{
			getConnection();
			String sql="SELECT no,name,subject,regdate,hit,group_tab,TO_CHAR(regdate,'YYYY-MM-DD'),kind "
					+ "FROM chef ORDER BY group_id DESC,group_step ASC";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			int rowSize=10;
			int i=0, j=0;
			int pagecnt=(page*rowSize)-rowSize;
			while(rs.next()){
				if(i<rowSize && j>=pagecnt){
					BoardDTO d=new BoardDTO();
					d.setNo(rs.getInt(1));
					d.setName(rs.getString(2));
					d.setSubject(rs.getString(3));
					d.setRegdate(rs.getDate(4));
					d.setHit(rs.getInt(5));
					d.setGroup_tab(rs.getInt(6));
					d.setDbday(rs.getString(7));
					d.setKind(rs.getString(8));					
					list.add(d);
					i++;
				}
				j++;
			}
			rs.close();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}finally{
			disConnection();
		}
		return list;		
	}
	
	public int boardTotal(){
		int total=0;
		try{
			getConnection();
			String sql="SELECT CEIL(COUNT(*)/10) FROM chef";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			total=rs.getInt(1);
			rs.close();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}finally{
			disConnection();
		}
		return total;
	}
	
	public int boardCount(){
		int total=0;
		try{
			getConnection();
			String sql="SELECT COUNT(*) FROM chef";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			total=rs.getInt(1);
			rs.close();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}finally{
			disConnection();
		}
		return total;
	}
	
}
