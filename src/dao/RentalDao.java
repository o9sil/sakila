package dao;
import java.sql.*;
import java.util.*;

import util.DBUtil;
import vo.*;

public class RentalDao {
	
	public void insertRental(Rental rental) throws Exception {
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "INSERT INTO sakila_rental(rental_date, inventory_id, customer_id, return_date, staff_id, last_update) VALUES(?, ?, ?, now(), ?, now())";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, rental.getRentalDate());
		stmt.setInt(2, rental.getInventoryId());
		stmt.setInt(3, rental.getCustomerId());
		stmt.setInt(4, rental.getStaffId());
		
		stmt.executeUpdate();
	}
	//현재 페이지, 페이지에 몇개 리스트 출력할 지
	public int selectLastPage(String searchWord, int rowPerPage) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT Count(*) FROM sakila_rental WHERE rental_id LIKE ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%"+searchWord+"%");
		ResultSet rs = stmt.executeQuery();
		
		
		int totalPage = 0;
		int staffCount = 0;
		if(rs.next()) {
			staffCount = rs.getInt("Count(*)");
		}			
		
		
		if(staffCount % rowPerPage == 0) {
			totalPage = staffCount / rowPerPage;
		}else {
			totalPage = (staffCount / rowPerPage) + 1;
		}
		
		System.out.println("staffCount : " + staffCount);		
		System.out.println("토탈페이지 : " + totalPage);
		
		return totalPage;
	}
	
	//현재 페이지, 페이지에 몇개 리스트 출력할 지
		public int selectLastPage(int searchWord, int rowPerPage) throws Exception{
			DBUtil dbUtil = new DBUtil();
			Connection conn = dbUtil.getConnection();
			String sql = "SELECT Count(*) FROM sakila_rental WHERE rental_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, searchWord);
			ResultSet rs = stmt.executeQuery();
			
			
			int totalPage = 0;
			int staffCount = 0;
			if(rs.next()) {
				staffCount = rs.getInt("Count(*)");
			}			
			
			
			if(staffCount % rowPerPage == 0) {
				totalPage = staffCount / rowPerPage;
			}else {
				totalPage = (staffCount / rowPerPage) + 1;
			}
			
			System.out.println("staffCount : " + staffCount);		
			System.out.println("토탈페이지 : " + totalPage);
			
			return totalPage;
		}
	
	
	
	//전체 행수
	public int selectTotlaCount(int searchWord) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql="SELECT Count(*) from sakila_rental where rental_id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, searchWord);
		ResultSet rs = stmt.executeQuery();
		int totalRow=0;
		if(rs.next()) {
			totalRow=rs.getInt("Count(*)");
		}
		return totalRow;
		
	}
	
	public int selectTotlaCount(String searchWord) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql="SELECT Count(*) from sakila_rental where rental_id like ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%"+searchWord+"%");
		ResultSet rs = stmt.executeQuery();
		int totalRow=0;
		if(rs.next()) {
			totalRow=rs.getInt("Count(*)");
		}
		return totalRow;
		
	}
	
	//페이징 함수
	public ArrayList<RentalAndStaff> selectRentalAndStaff(int beginPage,int rowPerPage,String searchWord) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql="SELECT r.*, s.* FROM sakila_rental r INNER JOIN sakila_staff s ON r.staff_id = s.staff_id where r.rental_id LIKE ? order By rental_id asc limit ?,?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%"+searchWord+"%");
		stmt.setInt(2, beginPage);
		stmt.setInt(3, rowPerPage);
		ResultSet rs = stmt.executeQuery();
		
		ArrayList<RentalAndStaff> list = new ArrayList<RentalAndStaff>();
		while(rs.next()) {
			RentalAndStaff rentalAndStaff = new RentalAndStaff();
			Rental rental = new Rental();
			rental.setRentalId(rs.getInt("r.rental_id"));
			rental.setRentalDate(rs.getString("r.rental_date"));
			rental.setInventoryId(rs.getInt("r.inventory_id"));
			rental.setCustomerId(rs.getInt("r.customer_id"));
			rental.setReturnDate(rs.getString("r.return_date"));
			rental.setStaffId(rs.getInt("r.staff_id"));
			rental.setLastUpdate(rs.getString("r.last_update"));
			rentalAndStaff.setRental(rental);
			
			Staff staff = new Staff();
			staff.setStaffId(rs.getInt("s.staff_id"));
			staff.setUsername(rs.getString("s.username"));
			rentalAndStaff.setStaff(staff);
			
			list.add(rentalAndStaff);
		}
		return list;
	}
	
	//페이징 함수
		public ArrayList<RentalAndStaff> selectRentalAndStaffsearch(int beginPage,int rowPerPage,int searchWord) throws Exception{
			DBUtil dbUtil = new DBUtil();
			Connection conn = dbUtil.getConnection();
			String sql="SELECT r.*, s.* FROM sakila_rental r INNER JOIN sakila_staff s ON r.staff_id = s.staff_id where r.rental_id = ? order By rental_id asc limit ?,?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, searchWord);
			stmt.setInt(2, beginPage);
			stmt.setInt(3, rowPerPage);
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<RentalAndStaff> list = new ArrayList<RentalAndStaff>();
			while(rs.next()) {
				RentalAndStaff rentalAndStaff = new RentalAndStaff();
				Rental rental = new Rental();
				rental.setRentalId(rs.getInt("r.rental_id"));
				rental.setRentalDate(rs.getString("r.rental_date"));
				rental.setInventoryId(rs.getInt("r.inventory_id"));
				rental.setCustomerId(rs.getInt("r.customer_id"));
				rental.setReturnDate(rs.getString("r.return_date"));
				rental.setStaffId(rs.getInt("r.staff_id"));
				rental.setLastUpdate(rs.getString("r.last_update"));
				rentalAndStaff.setRental(rental);
				
				Staff staff = new Staff();
				staff.setStaffId(rs.getInt("s.staff_id"));
				staff.setUsername(rs.getString("s.username"));
				rentalAndStaff.setStaff(staff);
				
				list.add(rentalAndStaff);
			}
			return list;
		}
	
	public ArrayList<RentalAndStaff> selectRentalAndStaff(String searchWord) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql="SELECT r.*, s.* FROM sakila_rental r INNER JOIN sakila_staff s ON r.staff_id = s.staff_id where r.rental_id like ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%"+searchWord+"%");
		ResultSet rs = stmt.executeQuery();
		ArrayList<RentalAndStaff> list = new ArrayList<RentalAndStaff>();
		while(rs.next()) {
			RentalAndStaff rentalAndStaff = new RentalAndStaff();
			Rental rental = new Rental();
			rental.setRentalId(rs.getInt("r.rental_id"));
			rental.setRentalDate(rs.getString("r.rental_date"));
			rental.setInventoryId(rs.getInt("r.inventory_id"));
			rental.setCustomerId(rs.getInt("r.customer_id"));
			rental.setReturnDate(rs.getString("r.return_date"));
			rental.setStaffId(rs.getInt("r.staff_id"));
			rental.setLastUpdate(rs.getString("r.last_update"));
			rentalAndStaff.setRental(rental);
			
			Staff staff = new Staff();
			staff.setStaffId(rs.getInt("s.staff_id"));
			staff.setUsername(rs.getString("s.username"));
			rentalAndStaff.setStaff(staff);
			
			list.add(rentalAndStaff);
		}
		return list;
	}
	
	public ArrayList<RentalAndStaff> selectRentalAndStaffAll(int searchWord) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql="SELECT r.*, s.* FROM sakila_rental r INNER JOIN sakila_staff s ON r.staff_id = s.staff_id where r.rental_id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, searchWord);
		ResultSet rs = stmt.executeQuery();
		ArrayList<RentalAndStaff> list = new ArrayList<RentalAndStaff>();
		while(rs.next()) {
			RentalAndStaff rentalAndStaff = new RentalAndStaff();
			Rental rental = new Rental();
			rental.setRentalId(rs.getInt("r.rental_id"));
			rental.setRentalDate(rs.getString("r.rental_date"));
			rental.setInventoryId(rs.getInt("r.inventory_id"));
			rental.setCustomerId(rs.getInt("r.customer_id"));
			rental.setReturnDate(rs.getString("r.return_date"));
			rental.setStaffId(rs.getInt("r.staff_id"));
			rental.setLastUpdate(rs.getString("r.last_update"));
			rentalAndStaff.setRental(rental);
			
			Staff staff = new Staff();
			staff.setStaffId(rs.getInt("s.staff_id"));
			staff.setUsername(rs.getString("s.username"));
			rentalAndStaff.setStaff(staff);
			
			list.add(rentalAndStaff);
		}
		return list;
	}
}
