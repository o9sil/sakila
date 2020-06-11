package dao;

import java.sql.*;
import java.util.*;

import util.DBUtil;
import vo.*;

public class CategoryDao {
	
	public ArrayList<Category> selectCategoryList(String searchWord) throws Exception{
		System.out.println("CategoryDao ---------------");
		System.out.println(searchWord+"<----searchWord"); // 검색값이 넘어오는지 확인
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		PreparedStatement stmt = conn.prepareStatement("SELECT category_id, name, last_update from sakila_category WHERE name LIKE ? order by category_id asc ");
		stmt.setString(1, "%"+searchWord+"%");
		ResultSet rs = stmt.executeQuery();
		ArrayList<Category> list = new ArrayList<Category>();
		Category category = null;
		while(rs.next()) {
			category = new Category();
			category.setCategoryId(rs.getInt("category_id"));
			category.setName(rs.getString("name"));
			category.setLastUpdate(rs.getString("last_update"));
			list.add(category);
		}
		System.out.println(list.size()+"<---list.size");
		return list;
	}
	public Category insertCategory(Category category) throws Exception {
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "INSERT INTO sakila_category(name, last_update) VALUES(?,now())";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, category.getName());
		stmt.executeQuery();
		return category;
	}
}
