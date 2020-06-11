package dao;

import vo.*;
import util.*;
import java.util.*;
import java.sql.*;

public class AddressDao {
	
	//현재 페이지, 페이지에 몇개 리스트 출력할 지
	public int selectLastPage(String searchWord, int rowPerPage) throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT count(*) FROM sakila_address WHERE address like ?";
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
	
	
	
	public int selectTotalCount() throws Exception{
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql="select Count(*) cnt from sakila_address";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		int totalRow=0;
		if(rs.next()) {
			totalRow=rs.getInt("cnt");
		}
		return totalRow;
	}
	
	
	
	//주소 페이징 
	public ArrayList<AddressAndCityAndCountry> selectAddressListAll(int beginPage,int rowPerPage,String searchWord) throws Exception{
		System.out.println(searchWord+"<---- AddressDao searchWord"); // 검색값이 넘어오는지 디버깅
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		//address , city , country 3개의 테이블을 조인
		PreparedStatement stmt = conn.prepareStatement("SELECT ad.address_id, ad.address, ad.address2, cr.country_id, cr.country, ad.city_id, ct.city ,  ad.district, ad.postal_code, ad.phone, ad.last_update "
				+ "FROM sakila_address ad INNER JOIN sakila_city ct INNER JOIN sakila_country cr "
				+ "ON ad.city_id = ct.city_id AND ct.country_id = cr.country_id WHERE ad.address like ? order by ad.address_id asc limit ?,?");
		stmt.setString(1, "%"+searchWord+"%"); // ?에 들어갈 검색값
		stmt.setInt(2, beginPage);
		stmt.setInt(3, rowPerPage);
		ResultSet rs = stmt.executeQuery();
		ArrayList<AddressAndCityAndCountry> list = new ArrayList<AddressAndCityAndCountry>();
		while(rs.next()) {
			AddressAndCityAndCountry addressAndCityAndCountry = new AddressAndCityAndCountry();
			Address address = new Address();
			//address 값
			//ad.address_id, ad.address, ad.address2, cr.country_id, cr.country, ad.city_id, ct.city , 
			//ad.district, ad.postal_code, ad.phone, ad.last_update
			address.setAddressId(rs.getInt("ad.address_id"));
			address.setAddress(rs.getString("ad.address"));
			address.setAddress2(rs.getString("ad.address2"));
			address.setCityId(rs.getInt("ad.city_id"));
			address.setDistrict(rs.getString("ad.district"));
			address.setPostalCode(rs.getString("ad.postal_code"));
			address.setPhone(rs.getString("ad.phone"));
			address.setLastUpdate(rs.getString("ad.last_update"));
			addressAndCityAndCountry.setAddress(address);
			//city 값
			City city = new City();
			city.setCity(rs.getString("ct.city"));
			addressAndCityAndCountry.setCity(city);
			//country 값
			Country country = new Country();
			country.setCountryId(rs.getInt("cr.country_id"));
			country.setCountry(rs.getString("cr.country"));
			addressAndCityAndCountry.setCountry(country);
			// 위 내요을 list에 추가
			list.add(addressAndCityAndCountry);
		}
		return list;
	}
	
	
	
	
	public ArrayList<Integer> selectAddressIdList() throws Exception{
		//SELECT store_id FROM store
		DBUtil dbUtil=new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "SELECT address_id FROM sakila_address";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs= stmt.executeQuery();
		ArrayList<Integer> list=new ArrayList<Integer>();
		while (rs.next()) {
			list.add(rs.getInt("address_id"));
		}
		return list;
	}	
	
	
	
	
	
	
	
	
	public int insertAddressAndSelectKey(Address address) throws Exception {
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		String sql = "INSERT INTO sakila_address(address_id,address,address2,district,city_id,postal_code,phone,last_update) "
				+ "VALUES(?,?,?,?,?,?,?,now())";
		PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
															//옵션이 들어가면 sql문 먼저 실행하여 insert를 먼저 실행하고, select를 다시 호출하는  옵션
		stmt.setInt(1,address.getAddressId());
		stmt.setString(2, address.getAddress());
		stmt.setString(3, address.getAddress2());
		stmt.setString(4, address.getDistrict());
		stmt.setInt(5, address.getCityId());
		stmt.setString(6, address.getPostalCode());
		stmt.setString(7, address.getPhone());
		
		stmt.executeUpdate();	//insert만 실행하는 부분
		ResultSet rs = stmt.getGeneratedKeys();	//select부분을 실행해주는 부분(프라이머리 키 값이 들어감)
		//SELECT address_id FROM address WHERE 원래 실행하려던 쿼리 -> 없는 쿼리이기 때문에 위에 같은 옵션을 넣어줌
		
		int addressId = 0;
		if(rs.next()) {
			addressId = rs.getInt(1);	//select에서 첫 번째 있는 부분을 가지고 올 때 1로 호출 가능 (컬럼 이름을 모르기 때문에)
		}
		
		return addressId;
	}
	

	
	//주소 리스트
	public ArrayList<AddressAndCityAndCountry> selectAddressListAll(String searchWord) throws Exception{
		System.out.println(searchWord+"<---- AddressDao searchWord"); // 검색값이 넘어오는지 디버깅
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		//address , city , country 3개의 테이블을 조인
		PreparedStatement stmt = conn.prepareStatement("SELECT ad.address_id, ad.address, ad.address2, cr.country_id, cr.country, ad.city_id, ct.city ,  ad.district, ad.postal_code, ad.phone, ad.last_update "
				+ "FROM sakila_address ad INNER JOIN sakila_city ct INNER JOIN sakila_country cr "
				+ "ON ad.city_id = ct.city_id AND ct.country_id = cr.country_id WHERE ad.address like ?");
		stmt.setString(1, "%"+searchWord+"%"); // ?에 들어갈 검색값
		ResultSet rs = stmt.executeQuery();
		ArrayList<AddressAndCityAndCountry> list = new ArrayList<AddressAndCityAndCountry>();
		while(rs.next()) {
			AddressAndCityAndCountry addressAndCityAndCountry = new AddressAndCityAndCountry();
			Address address = new Address();
			//address 값
			//ad.address_id, ad.address, ad.address2, cr.country_id, cr.country, ad.city_id, ct.city , 
			//ad.district, ad.postal_code, ad.phone, ad.last_update
			address.setAddressId(rs.getInt("ad.address_id"));
			address.setAddress(rs.getString("ad.address"));
			address.setAddress2(rs.getString("ad.address2"));
			address.setCityId(rs.getInt("ad.city_id"));
			address.setDistrict(rs.getString("ad.district"));
			address.setPostalCode(rs.getString("ad.postal_code"));
			address.setPhone(rs.getString("ad.phone"));
			address.setLastUpdate(rs.getString("ad.last_update"));
			addressAndCityAndCountry.setAddress(address);
			//city 값
			City city = new City();
			city.setCity(rs.getString("ct.city"));
			addressAndCityAndCountry.setCity(city);
			//country 값
			Country country = new Country();
			country.setCountryId(rs.getInt("cr.country_id"));
			country.setCountry(rs.getString("cr.country"));
			addressAndCityAndCountry.setCountry(country);
			// 위 내요을 list에 추가
			list.add(addressAndCityAndCountry);
		}
		return list;
	}
	
	//주소 리스트
		public ArrayList<AddressAndCityAndCountry> selectAddressList(int searchWord) throws Exception{
			System.out.println(searchWord+"<---- AddressDao searchWord"); // 검색값이 넘어오는지 디버깅
			DBUtil dbUtil = new DBUtil();
			Connection conn = dbUtil.getConnection();
			//address , city , country 3개의 테이블을 조인
			PreparedStatement stmt = conn.prepareStatement("SELECT ad.address_id, ad.address, ad.address2, cr.country_id, cr.country, ad.city_id, ct.city ,  ad.district, ad.postal_code, ad.phone, ad.last_update "
					+ "FROM sakila_address ad INNER JOIN sakila_city ct INNER JOIN sakila_country cr ON ad.city_id = ct.city_id AND ct.country_id = cr.country_id WHERE ad.city_id = ?");
			stmt.setInt(1, searchWord); // ?에 들어갈 검색값
			ResultSet rs = stmt.executeQuery();
			ArrayList<AddressAndCityAndCountry> list = new ArrayList<AddressAndCityAndCountry>();
			while(rs.next()) {
				AddressAndCityAndCountry addressAndCityAndCountry = new AddressAndCityAndCountry();
				Address address = new Address();
				//address 값
				//ad.address_id, ad.address, ad.address2, cr.country_id, cr.country, ad.city_id, ct.city , 
				//ad.district, ad.postal_code, ad.phone, ad.last_update
				address.setAddressId(rs.getInt("ad.address_id"));
				address.setAddress(rs.getString("ad.address"));
				address.setAddress2(rs.getString("ad.address2"));
				address.setCityId(rs.getInt("ad.city_id"));
				address.setDistrict(rs.getString("ad.district"));
				address.setPostalCode(rs.getString("ad.postal_code"));
				address.setPhone(rs.getString("ad.phone"));
				address.setLastUpdate(rs.getString("ad.last_update"));
				addressAndCityAndCountry.setAddress(address);
				//city 값
				City city = new City();
				city.setCity(rs.getString("ct.city"));
				addressAndCityAndCountry.setCity(city);
				//country 값
				Country country = new Country();
				country.setCountryId(rs.getInt("cr.country_id"));
				country.setCountry(rs.getString("cr.country"));
				addressAndCityAndCountry.setCountry(country);
				// 위 내요을 list에 추가
				list.add(addressAndCityAndCountry);
			}
			return list;
		}
	
		//주소 추가
		public void insertAdrress(Address addr) throws Exception {
			System.out.println(addr+"<--addrdao");
			System.out.println(addr.getAddressId()+"<--addraddrIddao");
			System.out.println(addr.getAddress()+"<--addraddrdao");
			System.out.println(addr.getAddress2()+"<--addraddr2dao");
			System.out.println(addr.getDistrict()+"<--addrdisdao");
			System.out.println(addr.getCityId()+"<--addrCitydao");
			System.out.println(addr.getPostalCode()+"<--addrpostaldao");
			System.out.println(addr.getPhone()+"<--addrphonedao");
			DBUtil dbUtil = new DBUtil();
			Connection conn = dbUtil.getConnection();
			String sql = "INSERT INTO sakila_address(address_id, address, address2, district, city_id, postal_code, phone, last_update) VALUES(?, ?, ?, ?, ?, ?, ?, now())";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, addr.getAddressId());
			stmt.setString(2, addr.getAddress());
			stmt.setString(3, addr.getAddress2());
			stmt.setString(4, addr.getDistrict());
			stmt.setInt(5,  addr.getCityId());
			stmt.setString(6, addr.getPostalCode());
			stmt.setString(7,  addr.getPhone());
			stmt.executeUpdate();
		}
		
		//AddressId Max
		public int selectAddressIdMax() throws Exception{
			DBUtil dbUtil = new DBUtil();
			Connection conn = dbUtil.getConnection();
			//address , city , country 3개의 테이블을 조인
			PreparedStatement stmt = conn.prepareStatement("SELECT MAX(address_id) FROM sakila_address");
			ResultSet rs = stmt.executeQuery();
			
			int addressIdMax = 0;
			if(rs.next()) {
				addressIdMax = (rs.getInt("MAX(address_id)")) + 1;
			}
			
			return addressIdMax;
		}
		
		public HashMap<String, Object> selectAddressOne(int addressId) throws Exception{
			DBUtil dbUtil = new DBUtil();
			Connection conn = dbUtil.getConnection();
			String sql = "SELECT ad.address, ad.address2, ad.district, ad.city_id, c.city, ad.postal_code, ad.phone FROM sakila_address ad INNER JOIN sakila_city c ON ad.city_id=c.city_id WHERE ad.address_id=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, addressId);
			
			ResultSet rs = stmt.executeQuery();
			
			HashMap<String, Object> add = new HashMap<String, Object>();
			
			if(rs.next()) {
				add.put("address", rs.getString("ad.address"));
				add.put("address2", rs.getString("ad.address2"));
				add.put("district", rs.getString("ad.district"));
				add.put("cityId", rs.getString("ad.city_id"));
				add.put("city", rs.getString("c.city"));
				add.put("postalCode", rs.getString("ad.postal_code"));
				add.put("phone", rs.getString("ad.phone"));
			}
			
			
			return add;
		}
}
