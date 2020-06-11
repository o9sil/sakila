package dao;
import vo.*;
import util.*;
import java.sql.*;
import java.util.*;

public class CustomerDao {
	//rantal join을 위해 받을 값
	public ArrayList<Customer> selectCustomerIdList() throws Exception{
		DBUtil dbUtil=new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT customer_id, first_name, last_name FROM sakila_customer";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs= stmt.executeQuery();
		ArrayList<Customer> list=new ArrayList<Customer>();
		while (rs.next()) {
			Customer c = new Customer();
			c.setCustomerId(rs.getInt("customer_id"));
			c.setFirstName(rs.getString("first_name"));
			c.setLastName(rs.getString("last_name"));
			
			list.add(c);
		}
		return list;
	}
	public void insertCustomer(Customer customer) throws Exception {
	DBUtil dbUtil = new DBUtil();
	Connection conn = dbUtil.getConnection();
	String sql = "INSERT INTO sakila_customer(store_id, first_name, last_name, email, address_id, create_date, last_update) VALUES(?, ?, ?, ?, ?, ?, now())";
	PreparedStatement stmt = conn.prepareStatement(sql);
	stmt.setInt(1, customer.getStoreId());
	stmt.setString(2, customer.getFirstName());
	stmt.setString(3, customer.getLastName());
	stmt.setString(4, customer.getEmail());
	stmt.setInt(5, customer.getAddressId());
	stmt.setString(6, customer.getCreateDate());
	
	stmt.executeQuery();
	}
	

	
	//Customer list
	public ArrayList<Customer> selectCustomerList(String searchWord) throws Exception{
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT customer_id, store_id, CONCAT(first_name,' ',last_name) AS full_name, email, address_id, active, create_date, last_update FROM sakila_customer WHERE first_name like ? or last_name like ? ORDER BY customer_id asc";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%"+searchWord+"%"); // first name 검색값
		stmt.setString(2, "%"+searchWord+"%"); // last name 검색값
		ResultSet rs = stmt.executeQuery();
		ArrayList<Customer> list = new ArrayList<Customer>();
		while(rs.next()) {
			Customer customer = new Customer();
			customer.setCustomerId(rs.getInt("customer_id"));
			customer.setStoreId(rs.getInt("store_id"));
			customer.setFullName(rs.getString("full_name")); // full name = first_name + last_name
			customer.setEmail(rs.getString("email"));
			customer.setAddressId(rs.getInt("address_id"));
			customer.setActive(rs.getInt("active"));
			customer.setCreateDate(rs.getString("create_date"));
			customer.setLastUpdate(rs.getString("last_update"));
			list.add(customer);	
		}
		return list;
	}
	
	
		public ArrayList<Customer> selectCustomerAll(int searchWord) throws Exception{
		
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT customer_id, store_id, CONCAT(first_name,' ',last_name) AS full_name, email, address_id, active, create_date, last_update FROM sakila_customer WHERE customer_id=? ORDER BY customer_id asc";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, searchWord);
		
		ResultSet rs = stmt.executeQuery();
		ArrayList<Customer> list = new ArrayList<Customer>();
		while(rs.next()) {
			Customer customer = new Customer();
			customer.setCustomerId(rs.getInt("customer_id"));
			customer.setStoreId(rs.getInt("store_id"));
			customer.setFullName(rs.getString("full_name")); // full name = first_name + last_name
			customer.setEmail(rs.getString("email"));
			customer.setAddressId(rs.getInt("address_id"));
			customer.setActive(rs.getInt("active"));
			customer.setCreateDate(rs.getString("create_date"));
			customer.setLastUpdate(rs.getString("last_update"));
			list.add(customer);	
		}
		return list;
	}
		
		
		//현재 페이지, 페이지에 몇개 리스트 출력할 지
		public int selectLastPage(String searchWord, int rowPerPage) throws Exception{
			DBUtil dbUtil = new DBUtil();
			Connection conn = dbUtil.getConnection();
			String sql = "SELECT count(*) FROM sakila_customer WHERE first_name like ? or last_name like ?";
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
		
		//Customer list
		public ArrayList<Customer> selectCustomerList(String searchWord, int beginRow, int rowPerPage) throws Exception{
			
			DBUtil dbUtil = new DBUtil();
			Connection conn = dbUtil.getConnection();
			String sql = "SELECT customer_id, store_id, CONCAT(first_name,' ',last_name) AS full_name, email, address_id, active, create_date, last_update FROM sakila_customer WHERE first_name like ? or last_name like ? ORDER BY customer_id asc LIMIT ?, ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%"+searchWord+"%"); // first name 검색값
			stmt.setString(2, "%"+searchWord+"%"); // last name 검색값
			stmt.setInt(3, beginRow);
			stmt.setInt(4, rowPerPage);
			ResultSet rs = stmt.executeQuery();
			ArrayList<Customer> list = new ArrayList<Customer>();
			while(rs.next()) {
				Customer customer = new Customer();
				customer.setCustomerId(rs.getInt("customer_id"));
				customer.setStoreId(rs.getInt("store_id"));
				customer.setFullName(rs.getString("full_name")); // full name = first_name + last_name
				customer.setEmail(rs.getString("email"));
				customer.setAddressId(rs.getInt("address_id"));
				customer.setActive(rs.getInt("active"));
				customer.setCreateDate(rs.getString("create_date"));
				customer.setLastUpdate(rs.getString("last_update"));
				list.add(customer);	
			}
			return list;
		}
}
