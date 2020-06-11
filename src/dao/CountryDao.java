package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import util.DBUtil;
import vo.Country;

public class CountryDao {
	
	//국가 리스트
	public ArrayList<Country> selectCountryListAll(String searchWord) throws Exception{
		System.out.println(searchWord + " <-- searchWord");
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT * FROM sakila_country WHERE country like ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%" + searchWord + "%");
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<Country> list = new ArrayList<Country>();
		while(rs.next()) {
			Country country = new Country();
			country.setCountryId(rs.getInt("country_id"));
			country.setCountry(rs.getString("country"));
			country.setLastUpdate(rs.getString("last_update"));			
			list.add(country);
		}
		
		return list;
	}
	
	//입력할 국가 번호 자동으로 확인
	public int selectCountryIdMax() throws Exception{
		int countryIdMax = 0;
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT MAX(country_id) FROM sakila_country";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()) {
			countryIdMax = (rs.getInt("MAX(country_id)")) + 1;
		}
		
		return countryIdMax;
	}
	
	//국가 추가
	public void insertCountry(Country country) throws Exception {
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "INSERT INTO sakila_country(country_id, country, last_update) VALUES (?, ?, NOW())";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, country.getCountryId());
		stmt.setString(2, country.getCountry());
		
		stmt.executeUpdate();
	}
	
	//현재 페이지, 페이지에 몇개 리스트 출력할 지
	public int selectLastPage(int rowPerPage) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT count(*) FROM sakila_country";
		PreparedStatement stmt = conn.prepareStatement(sql);
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
	
	
	public ArrayList<Country> selectStaffByPage(String searchWord, int beginRow, int rowPerPage) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();		
		String sql = "SELECT * FROM sakila_country WHERE country like ? LIMIT ?, ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%"+searchWord+"%");
		stmt.setInt(2, beginRow);
		stmt.setInt(3, rowPerPage);
		ResultSet rs = stmt.executeQuery();
		
		
		ArrayList<Country> list=new ArrayList<Country>();
		while(rs.next()) {
			Country country = new Country();
			country.setCountryId(rs.getInt("country_id"));
			country.setCountry(rs.getString("country"));
			country.setLastUpdate(rs.getString("last_update"));			
			list.add(country);		
			
		}
		
		return list;
	}
	
	//현재 페이지, 페이지에 몇개 리스트 출력할 지
	public int selectLastPage(String searchWord, int rowPerPage) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT count(*) FROM sakila_country WHERE country like ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%"+searchWord+"%");
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
