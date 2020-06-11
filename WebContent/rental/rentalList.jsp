<%@page import="javax.servlet.jsp.tagext.PageData"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ page import="dao.*" %>
<%@ page import="vo.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">   
<title>RANTALLIST</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
<meta charset="UTF-8">
<style>
    body {
        padding: 0;
        margin: 0;
        width: 100%;
        height: 100%;
        overflow: hidden;
        background-position: 0 0;
        background-repeat: no-repeat;
        background-attachment: fixed;
        background-size: cover;
        position: relative;
        overflow-y: auto;
      background-color: white;
    }
    
    #aside {
        width: 200px;
        height: 3000px;
        position: fixed;
        background: white;
        
        overflow: hidden;
        float: left;
    }
    
    #section {
        margin-left: 200px;
        padding : 20px;
        background: white;
    }
</style>
</head>
<body>
   <div style="widows: 100%; padding: 0; margin: 0; height: 20px; background-color: #f5c6cb;"></div>
    <div id="aside">
         <jsp:include page="/inc/sidemenu.jsp"></jsp:include> 
    </div>
    <div id="section">
   <%
   
   request.setCharacterEncoding("utf-8");   
   
   
   //����¡
   int currentPage=1;
   if(request.getParameter("currentPage")!=null){
      currentPage=Integer.parseInt(request.getParameter("currentPage"));
   }
   int rowPerPage=10;
   int beginPage=(currentPage-1)*rowPerPage;
   

   
   
   //�˻�
   String searchWord = "";
   
   if(request.getParameter("searchWord") != null){
      searchWord = request.getParameter("searchWord");
   }
   
   //totalRow
   RentalDao rentalDao = new RentalDao();
   //int totalRow=rentalDao.selectTotlaCount(searchWord);
   //int lastPage=rentalDao.selectLastPage(searchWord, rowPerPage);
		int totalRow = 0;
		int lastPage = 0;

  
   
   ArrayList<RentalAndStaff> list = new ArrayList<RentalAndStaff>();
   int searchWordRentalId = 0;

   
   
   //ID Ŭ���ؼ� ������
   if(request.getParameter("rentalId") != null && request.getParameter("rentalId") != ""){
	   list = rentalDao.selectRentalAndStaffsearch(beginPage, rowPerPage, Integer.parseInt(request.getParameter("rentalId")));
 	totalRow = rentalDao.selectTotlaCount(Integer.parseInt(request.getParameter("rentalId")));
 	lastPage = rentalDao.selectLastPage(Integer.parseInt(request.getParameter("rentalId")), rowPerPage);
   }else{
 	  //�׿�
 	  list = rentalDao.selectRentalAndStaff(beginPage, rowPerPage, searchWord);
 	  totalRow = rentalDao.selectTotlaCount(searchWord);
 	  lastPage = rentalDao.selectLastPage(searchWord, rowPerPage);
   }
   %>
        <h1 style="padding-top: 30px;">RENTAL LIST</h1>
      <!-- �˻�, �Է� ��� -->
         <div>    
            <div id="accordion">
                <div class="card">
                   <!-- card header -->
                  <div class="card-header" style="background-color: window;">
                    <a class="card-link btn btn-outline-danger" data-toggle="collapse" href="#collapseOne">
                         	�˻�  
                    </a>
                    <a class="collapsed card-link btn btn-outline-danger" href="<%=request.getContextPath()%>/rental/insertRentalForm.jsp">
				        	�Է�  
				    </a>   
                   </div>
                  
                  
                  <!-- card body -->                  
                  <div id="collapseOne" class="collapse" data-parent="#accordion">
                    <div class="card-body">
                       <form method="post" action="<%=request.getContextPath()%>/rental/rentalList.jsp">
                        <input class="form-control" type="text" name="searchWord" value="<%=searchWord%>">
                        <br>
                        <button type="submit" class="btn btn-outline-light text-dark">Go</button>
                     </form>
                    </div>
                  </div>
                </div>
            </div>
         </div>
          <br>
            
      <table border="1" class="table table-bordered table-hover">
         <thead class="thead-light">
         <tr>
            <th>�뿩 ��ȣ</th>
			<th>�뿩 ��¥</th>
			<th>��� ��ȣ</th>
			<th>�� ��ȣ</th>
			<th>�ݳ� ��¥�� �ð�</th>
			<th>�뿩�� ó���� ����</th>
			<th>������Ʈ ��¥</th>
			<th>�ݳ� ���� �Է�</th>
         </tr>
         </thead>
         <tbody>
            <%
               for(RentalAndStaff ras : list) {
            %>
            <tr>
               <td><%=ras.getRental().getRentalId()%></td>
               <td><%=ras.getRental().getRentalDate() %></td>
               <td>
                  <a href="<%=request.getContextPath() %>/inventory/inventoryList.jsp?inventoryId=<%=ras.getRental().getInventoryId()%>">
                  	<%=ras.getRental().getInventoryId() %>
                  </a>
               	</td>
               	<td>
                  	<a href="<%=request.getContextPath() %>/customer/customerList.jsp?customerId=<%=ras.getRental().getCustomerId()%>">
                  		<%=ras.getRental().getCustomerId() %>
                  	</a>
               	</td>
               	<td><%=ras.getRental().getReturnDate() %></td>
               	<td><%=ras.getStaff().getUsername() %></td>
               	<td><%=ras.getRental().getLastUpdate() %></td>
	 			<td><a href="<%=request.getContextPath()%>/payment/insertPaymentForm.jsp?rentalId=<%=ras.getRental().getRentalId()%>">�ݳ� ���� �Է�</a></td>
            </tr>
         </tbody>
            <%
               }
            %>
      </table>
      
      <!-- ����¡ -->
		<ul class="pagination justify-content-center">		
		<%
			if(currentPage == 1){ //���� ������ ���� 1 �̸� ù��° ��ư �ӽ� ��Ȱ��ȭ
		%>	
			<li class="page-item"><a class="btn disabled" style="background-color: #f5c6cb;"
				href="<%=request.getContextPath() %>/rental/rentalList.jsp?currentPage=1&searchWord=<%=searchWord%>">ù��°</a>						
			</li>
		<%
			}else{  
		%>
				<li class="page-item"><a class="btn" style="background-color: #f5c6cb;"
					href="<%=request.getContextPath() %>/rental/rentalList.jsp?currentPage=1&searchWord=<%=searchWord%>">ù��°</a>						
				</li>
		<%		
			}
		
			if(currentPage>1){ //���� ���������� 1���� �۰ų� ������ ���� ��ư ��Ȱ��ȭ
		%>
				<li class="page-item"><a class="btn" style="background-color: #f5c6cb;"
					href="<%=request.getContextPath() %>/rental/rentalList.jsp?currentPage=<%=currentPage-1%>&searchWord=<%=searchWord%>">����</a>						
				</li>			
		<%
			}else{
		%>
				<li class="page-item"><a class="btn disabled" style="background-color: #f5c6cb;"
					href="<%=request.getContextPath() %>/rental/rentalList.jsp?currentPage=1&searchWord=<%=searchWord%>">����</a>						
				</li>
       	<%
			}
		%>
		<%//���� ����¡
			int pagePerGroup=10;
	  	    int groupStartPage=0;
			Paging paging = new Paging();
			PagingDao pagingDao = new PagingDao();
			ArrayList<Paging> plList = pagingDao.groupPaging(currentPage, pagePerGroup, lastPage);
			
			for(Paging pa : plList){
				if(pa.getPageSelect() == true){
		%>
			<li class="page-item"><a class="btn" style="background-color: #e9ecef;" href="<%=request.getContextPath() %>/rental/rentalList.jsp?currentPage=<%=pa.getPageNum()%>&searchWord=<%=searchWord%>"><%=pa.getPageNum()%></a></li>
			
		<%
				}else{
		%>
			<li class="page-item"><a class="btn" style="background-color:#f5c6cb;" href="<%=request.getContextPath() %>/rental/rentalList.jsp?currentPage=<%=pa.getPageNum()%>&searchWord=<%=searchWord%>"><%=pa.getPageNum()%></a></li>
		<%
				}
			}
		%>
		<%
			if(currentPage < lastPage){
		%>
			<li class="page-item"><a class="btn" style="background-color: #f5c6cb;"
				href="<%=request.getContextPath() %>/rental/rentalList.jsp?currentPage=<%=currentPage+1%>&searchWord=<%=searchWord%>">����</a>						
			</li>	
		<%		
			}else{
		%>
			<li class="page-item"><a class="btn disabled" style="background-color: #f5c6cb;"
					href="<%=request.getContextPath() %>/rental/rentalList.jsp?currentPage=<%=currentPage%>&searchWord=<%=searchWord%>">����</a>						
			</li>
		<%
			}
		
			if(currentPage >= lastPage){ //���� ����������  lastPage�� ������ ���� ��ư�� ������ ��ư �ӽ� ��Ȱ��ȭ  
		%>
			<li class="page-item"><a class="btn disabled" style="background-color: #f5c6cb;"
				href="<%=request.getContextPath() %>/rental/rentalList.jsp?currentPage=<%=lastPage%>&searchWord=<%=searchWord%>">������</a>						
			</li>
		<%
			}else{
		%>
			<li class="page-item"><a class="btn" style="background-color: #f5c6cb;"
				href="<%=request.getContextPath() %>/rental/rentalList.jsp?currentPage=<%=lastPage%>&searchWord=<%=searchWord%>">������</a>						
			</li>
		<%
			}
		%>
	</ul>
   </div>
</body>
</html>