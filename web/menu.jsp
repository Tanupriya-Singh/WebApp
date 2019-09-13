<%@ page import="com.search.ui.PastResultServlet" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="com.search.service.SearchService" %>
<%@ page import="com.search.service.JDBCService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> My search engine </title>
</head>
<body>
<br>

<form action="${pageContext.request.contextPath}/ButtonAction" method="post">
    <center>Search:<input type="text" name="searchWord" style="font-size: 20pt"/></center>
    <br>
    <center><input type="submit" value="Search file directory" style="font-size: 20pt"></center>
</form>

<br><br>
<center><h2>Searched in the past</h2></center>

<%
    PrintWriter printWriter = response.getWriter();
    JDBCService jdbcService = new JDBCService(session, printWriter);
    String history = jdbcService.getHistoryOfUser();

  if (history.length() == 0)
    out.println("<center> No search history to display</center>");
    String[] arrSplit = history.split(",");
    response.setContentType("text/html");


        for (String s : arrSplit) {
            out.println("<center>" + s + "</center>");
    }
%>
<br>
</form>
</body>
</html>

