<%@page import="vo.City"%>
<%@page import="dao.CityDao"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%
	//���ڵ� 
	request.setCharacterEncoding("utf-8");

	
	String city=request.getParameter("cityName");
	System.out.println(city+"<--city");
	
	int countryId=Integer.parseInt(request.getParameter("countryId"));
	System.out.println(countryId+"<--countryId");
	
	//���� ����
	City c=new City();
	c.setCity(city);
	c.setCountryId(countryId);
	
	//Daoȣ��
	CityDao cityDao = new CityDao();
	cityDao.insertCity(c);
	
	//����Ʈ�� �����ֱ�
	response.sendRedirect(request.getContextPath()+"/city/cityList.jsp");
%>
