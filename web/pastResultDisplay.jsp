<%--
  Created by IntelliJ IDEA.
  User: ttanupri
  Date: 03/09/19
  Time: 4:16 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <form action="${pageContext.request.contextPath}/PastResultServlet" method="post">

        <%--Do format checking here.--%>
<%
    String username = (String) session.getAttribute("userid");
    response.setContentType("text/html");
    out.println("<center><h1>"+"Welcome "+ username+"</h2></center>");

%>
        <center>Number Of Results:<input type="text" name="numberOfResult" style="font-size: 20pt"/></center>
        <br>
        <center><input type="submit" value="View" style="font-size: 20pt"></center>
    </form>

</head>
<body>
</body>
</html>
