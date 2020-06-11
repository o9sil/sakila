package dao;
import vo.*;
import java.util.*;
import util.DBUtil;
import java.sql.*;
public class FilmTextDao {
	public ArrayList<FilmText> selectFilmTextAll(String searchWord, int beginRow, int rowPerPage) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql="SELECT film_id, title, description FROM sakila_film_text WHERE title like ? ORDER BY film_id LIMIT ?,?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%"+searchWord+"%");
		stmt.setInt(2, beginRow);
		stmt.setInt(3, rowPerPage);
		
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<FilmText> list = new ArrayList<FilmText>();
		while(rs.next()){
		
		FilmText filmText = new FilmText();
		filmText.setFilmId(rs.getInt("film_id"));
		filmText.setTitle(rs.getString("title"));
		filmText.setDescription(rs.getString("description"));
		
		list.add(filmText);
	}
		
		return list;
   }
	
	public void insertFilmText(FilmText filmText) throws Exception{
		System.out.println(filmText.getTitle()+"<---title filmtextDao");
		System.out.println(filmText.getDescription()+"<---description filmtextDao");
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql="INSERT INTO sakila_film_text(film_id, title, description) VALUE(?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmText.getFilmId());
		stmt.setString(2, filmText.getTitle());
		stmt.setString(3, filmText.getDescription());
		stmt.executeUpdate();
	}
	public int selectFilmTextIdMax() throws Exception{
		int filmTextIdMax = 0;
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT MAX(film_id) max FROM sakila_film_text";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()) {
			filmTextIdMax = (rs.getInt("max")) + 1;
		}
		
		return filmTextIdMax;
	}
	
	//현재 페이지, 페이지에 몇개 리스트 출력할 지
	public int selectLastPage(String searchWord, int rowPerPage) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT count(*) FROM sakila_film_text WHERE title like ?";
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
