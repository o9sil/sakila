package dao;
import java.sql.*;
import java.util.*;
import util.DBUtil;
import vo.*;

public class StoreDao {
	
	
	public ArrayList<Integer> selectStoreIdList() throws Exception{
		//SELECT store_id FROM store
		DBUtil dbUtil=new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT store_id FROM sakila_store";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs= stmt.executeQuery();
		ArrayList<Integer> list=new ArrayList<Integer>();
		while (rs.next()) {
			list.add(rs.getInt("store_id"));
		}
		return list;
	}	
	
	public ArrayList<StoreAndStaffAndAddress> selectStoreListAll(String searchWord) throws Exception{
		DBUtil dbUtil=new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT st.* , sf.* , ad.* FROM sakila_store st INNER JOIN sakila_staff sf INNER JOIN sakila_address ad ON st.manager_staff_id=sf.staff_id And st.address_id=ad.address_id";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs= stmt.executeQuery();
		ArrayList<StoreAndStaffAndAddress> list=new ArrayList<StoreAndStaffAndAddress>();
		while (rs.next()) {
			StoreAndStaffAndAddress staffAndAddress=new StoreAndStaffAndAddress();
			
			Store store=new Store();
			store.setStoreId(rs.getInt("st.store_id"));
			store.setManagerStaffId(rs.getInt("st.manager_staff_id"));
			store.setAddressId(rs.getInt("st.address_id"));
			store.setLastUpdate(rs.getString("st.last_update"));
			staffAndAddress.setStore(store);
			
			Staff staff=new Staff();
			staff.setFirstName(rs.getString("sf.first_name"));
			staff.setLastName(rs.getString("sf.last_name"));
			staffAndAddress.setStaff(staff);
			
			Address address=new Address();
			address.setAddress(rs.getString("ad.address"));
			staffAndAddress.setAddress(address);
			
			list.add(staffAndAddress);		
		}
		return list;
	}
	
	public ArrayList<StoreAndStaffAndAddress> selectStoreAll(int searchWord) throws Exception{
		DBUtil dbUtil=new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT st.* , sf.* , ad.* FROM store st INNER JOIN sakila_staff sf INNER JOIN sakila_address ad ON st.manager_staff_id=sf.staff_id And st.address_id=ad.address_id where st.store_id like ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, searchWord);
		ResultSet rs= stmt.executeQuery();
		ArrayList<StoreAndStaffAndAddress> list=new ArrayList<StoreAndStaffAndAddress>();
		while (rs.next()) {
			StoreAndStaffAndAddress staffAndAddress=new StoreAndStaffAndAddress();
			
			Store store=new Store();
			store.setStoreId(rs.getInt("st.store_id"));
			store.setManagerStaffId(rs.getInt("st.manager_staff_id"));
			store.setAddressId(rs.getInt("st.address_id"));
			store.setLastUpdate(rs.getString("st.last_update"));
			staffAndAddress.setStore(store);
			
			Staff staff=new Staff();
			staff.setFirstName(rs.getString("sf.first_name"));
			staff.setLastName(rs.getString("sf.last_name"));
			staffAndAddress.setStaff(staff);
			
			Address address=new Address();
			address.setAddress(rs.getString("ad.address"));
			staffAndAddress.setAddress(address);
			
			list.add(staffAndAddress);		
		}
		return list;
	}
}	
