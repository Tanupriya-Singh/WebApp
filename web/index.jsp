<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title> My search engine </title>
</head>

<%--<SCRIPT type="text/javascript">--%>
<%--  window.history.forward();--%>
<%--  function noBack() { window.history.forward(); }--%>
<%--</SCRIPT>--%>

<body
<%--        onload="noBack();"--%>
<%--      onpageshow="if (event.persisted) noBack();" onunload=""--%>
>

<center><h1> Login to see past searches</h1></center>
<br>

<a href="${pageContext.request.contextPath}/registerUser.jsp" style="float: right;">Register a New User</a>

<br><br>
<form action="${pageContext.request.contextPath}/LoginAction" method="post">
  <center>User name:<input type="text" name ="username" style="font-size: 20pt"/></center>
  <br>
  <center>Password: <input type="password" name ="password" style="font-size: 20pt"/></center>
  <br>
  <center><input type="submit" value="login" style="font-size: 20pt"></center>
</form>

</body>
</html>
