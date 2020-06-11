package dao;

import vo.*;

import util.*;
import java.sql.*;
import java.util.*;


public class LanguageDao {
	public Language insertLanguage(Language language) throws Exception {
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "INSERT INTO sakila_language(name, last_update) VALUES(?, now())";
		PreparedStatement stmt = conn.prepareStatement(sql);		
		stmt.setString(1, language.getName());
		stmt.executeQuery();
		return language;
	}
	
	
	
	
	
	public ArrayList<Language> selectLanguageList(String searchWord) throws Exception {
		System.out.println("LanguageDao ----------");
		System.out.println(searchWord+"<----searchWord"); 
		DBUtil dbutil = new DBUtil();
		Connection conn = dbutil.getConnection();
		String sql = "SELECT language_id, name, last_update FROM sakila_language WHERE name like ? ORDER BY language_id ASC";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%" + searchWord + "%");
		ResultSet rs = stmt.executeQuery();
		ArrayList<Language> list = new ArrayList<Language>();
		while(rs.next()) {
			Language language = new Language();
			language.setLanguageId(rs.getInt("language_id"));
			language.setName(rs.getString("name"));
			language.setLastUpdate(rs.getString("last_update"));
			list.add(language);
		}
		return list;
	}
	
	
}
