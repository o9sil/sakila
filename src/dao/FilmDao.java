package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import util.DBUtil;
import vo.Film;

public class FilmDao {
	public ArrayList<Film> selectFilmList(String searchWord) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT film_id, title, description, release_year, language_id, original_language_id, rental_duration, rental_rate, length, "
				+ "replacement_cost, rating, special_features, last_update FROM sakila_film WHERE title LIKE ?";
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%" + searchWord +"%");
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<Film> list = new ArrayList<Film>();
		while(rs.next()) {
			Film f = new Film();
			f.setFilmId(rs.getInt("film_id"));
			f.setTitle(rs.getString("title"));
			f.setDescription(rs.getString("description"));
			f.setReleaseYear(rs.getString("release_year"));
			f.setLanguageId(rs.getInt("language_id"));
			f.setOrininalLanguageId(rs.getInt("original_language_id"));
			f.setRentalDuration(rs.getInt("rental_duration"));
			f.setRentalRate(rs.getDouble("rental_rate"));
			f.setLength(rs.getInt("length"));
			f.setReplacementCost(rs.getDouble("replacement_cost"));
			f.setRating(rs.getString("rating"));
			f.setSpecialFeatures(rs.getString("special_features"));
			f.setLastUpdate(rs.getString("last_update"));
			
			list.add(f);
		}
		
		return list;
	}
	
	public void insertFilm(Film film) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "INSERT INTO sakila_film(title, description, release_year, language_id, rental_duration, "
				+ "rental_rate, length, replacement_cost, rating, special_features, last_update) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";
		
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, film.getTitle());
		stmt.setString(2, film.getDescription());		
		stmt.setString(3, film.getReleaseYear());
		stmt.setInt(4, film.getLanguageId());
		stmt.setInt(5, film.getRentalDuration());
		stmt.setDouble(6, film.getRentalRate());
		stmt.setInt(7, film.getLength());
		stmt.setDouble(8, film.getReplacementCost());
		stmt.setString(9, film.getRating());
		stmt.setInt(10, Integer.parseInt(film.getSpecialFeatures()));		
		
		stmt.executeUpdate();
	}
	
	//현재 페이지, 페이지에 몇개 리스트 출력할지 
	public int selectLastPage(int rowPerPage) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT count(*) FROM sakila_film";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		
		int totalPage = 0;
		int filmCount = 0;
		if(rs.next()) {
			filmCount = rs.getInt("count(*)");
		}
		
		if(filmCount % rowPerPage == 0) {
			totalPage = filmCount /rowPerPage;
		}else {
			totalPage = (filmCount /rowPerPage)+1;
		}
		
		return totalPage;
	}
	
	//searchWord에 따라 페이징
	public ArrayList<Film> selectFilmBypage(String searchWord, int beginRow, int rowPerPage) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();		
		String sql = "SELECT * FROM sakila_film WHERE title like ? LIMIT ?, ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%"+searchWord+"%");
		stmt.setInt(2, beginRow);
		stmt.setInt(3, rowPerPage);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<Film> list = new ArrayList<Film>();
		while(rs.next()) {
			Film f = new Film();
			f.setFilmId(rs.getInt("film_id"));
			f.setTitle(rs.getString("title"));
			f.setDescription(rs.getString("description"));
			f.setReleaseYear(rs.getString("release_year"));
			f.setLanguageId(rs.getInt("language_id"));
			f.setOrininalLanguageId(rs.getInt("original_language_id"));
			f.setRentalDuration(rs.getInt("rental_duration"));
			f.setRentalRate(rs.getDouble("rental_rate"));
			f.setLength(rs.getInt("length"));
			f.setReplacementCost(rs.getDouble("replacement_cost"));
			f.setRating(rs.getString("rating"));
			f.setSpecialFeatures(rs.getString("special_features"));
			f.setLastUpdate(rs.getString("last_update"));
			list.add(f);
		}
		
		return list;
	}
	
	//현재 페이지, 페이지에 몇개 리스트 출력할 지
	public int selectLastPage(String searchWord, int rowPerPage) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();		
		String sql = "SELECT count(*) FROM sakila_film WHERE title like ? ";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%"+searchWord+"%");
		ResultSet rs = stmt.executeQuery();
		
		int totalPage = 0;
		int filmCount = 0;
		
		if(rs.next()) {
			filmCount = rs.getInt("count(*)");
		}
		
		if(filmCount % rowPerPage == 0) {
			totalPage = filmCount / rowPerPage;
		}else {
			totalPage = (filmCount/rowPerPage) + 1;
		}
		
		return totalPage;
	}
	
}
