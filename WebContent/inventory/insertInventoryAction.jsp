<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import = "vo.*" %>
<%@ page import = "dao.*" %>
<%
	//���ڵ�
	request.setCharacterEncoding("utf-8");

	//form���� �� ��������
	int film = Integer.parseInt(request.getParameter("film"));
	System.out.println(film+"<--film");
	
	int storeId = Integer.parseInt(request.getParameter("storeId"));
	System.out.println(storeId+"<--storeId");
	
	//���� ����
	Inventory inventory = new Inventory();
	inventory.setFilmId(film);
	inventory.setStoreId(storeId);
	
	//Dao
	InventoryDao inventoryDao = new InventoryDao();
	inventoryDao.insertInventory(inventory);
	
	//����Ʈ�� �����ֱ�
	response.sendRedirect(request.getContextPath()+"/inventory/inventoryList.jsp");
%>