package dao;

import vo.*;
import util.*;
import java.sql.*;
import java.util.*;

public class ActorDao {
	
	//Actor list
	public ArrayList<Actor> selectActorList(String searchWord, int beginRow, int rowPerPage) throws Exception{
		System.out.println(searchWord+"<---searchWord");
		System.out.println(beginRow+"<--beginRow");
		System.out.println(rowPerPage+"<--rowPerPage");
		System.out.println("AcotrDao ---------------");
		
		System.out.println(searchWord+"<----searchWord"); // 검색값이 넘어오는지 확인
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT actor_id, first_name, last_name, last_update FROM sakila_actor WHERE first_name like ? or last_name like ? ORDER BY actor_id ASC LIMIT ?,?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%"+searchWord+"%");
		stmt.setString(2, "%"+searchWord+"%");
		stmt.setInt(3, beginRow);
		stmt.setInt(4, rowPerPage);
		ResultSet rs = stmt.executeQuery();
		ArrayList<Actor> list = new ArrayList<Actor>();
		while(rs.next()) {
			Actor actor = new Actor();
			actor.setActorId(rs.getInt("actor_id"));
			actor.setFirstName(rs.getString("first_name"));
			actor.setLastName(rs.getString("last_name"));
			actor.setLastUpdate(rs.getString("last_update"));
			list.add(actor);
		}
		return list;
	}
	//insert Actor
	public void insertActor(Actor actor) throws Exception {
		System.out.println("dao로 넘어옴!");
		System.out.println(actor.getFirstName()+"<----firstname");
		System.out.println(actor.getLastName()+"<---lastname");
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "INSERT INTO sakila_actor(first_name, last_name, last_update) VALUES (?,?, NOW())";
		PreparedStatement stmt = conn.prepareStatement(sql);		
		stmt.setString(1, actor.getFirstName());
		stmt.setString(2, actor.getLastName());
		stmt.executeUpdate();
	}
	// total count 값 구하기 , 검색값이 없을때
	public int totalCount() throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT count(*) from sakila_actor";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		int totalCount = 0;
		if(rs.next()) {
			totalCount = rs.getInt("count(*)");
		}
		return totalCount;
	}
	//last page 값 구하기 , 검색값이 있을때
	public int selectLastPage(String searchWord, int rowPerPage) throws Exception{
	      DBUtil dbUtil = new DBUtil();
	      Connection conn = dbUtil.getConnection();
	      String sql = "SELECT count(*) FROM sakila_actor WHERE first_name like ? or last_name like ? ";
	      PreparedStatement stmt = conn.prepareStatement(sql);
	      stmt.setString(1, "%"+searchWord+"%");
	      stmt.setString(2, "%"+searchWord+"%");
	      ResultSet rs = stmt.executeQuery();
	      
	      
	      int totalPage = 0;
	      int staffCount = 0;
	      if(rs.next()) {
	         staffCount = rs.getInt("count(*)");
	      }         
	      
	      
	      if(staffCount % rowPerPage == 0) {
	         totalPage = staffCount / rowPerPage;
	      }else {
	         totalPage = (staffCount / rowPerPage) + 1;
	      }
	      
	      return totalPage;
	   }
}
