<%--
  Created by IntelliJ IDEA.
  User: ttanupri
  Date: 11/09/19
  Time: 11:35 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register a User</title>
</head>
<script>
    function validateForm(){
        var doc = document.newUserValidationForm;
        var password = doc.password.value;
        var confirmPassword = doc.confirmPassword.value;
        if (password !== confirmPassword)
            alert("Password and confirm password does not match");
        else{
            doc.action = "${pageContext.request.contextPath}/RegisterUserServlet" ;
            doc.method="POST";
            doc.submit();
        }
    }
</script>

<body>
<form name = "newUserValidationForm" method="post" onsubmit="validateForm()">
    <center>Enter User Name:<input type="text" name ="userid" style="font-size: 20pt"/></center>
    <br>
    <center>New Password: <input type="password" name ="password" style="font-size: 20pt"/></center>
    <br>
    <center>Confirm New Password: <input type="password" name ="confirmPassword" style="font-size: 20pt"/></center>
    <br>
    <center><input type="submit" value="login" style="font-size: 20pt"></center>
</form>
</body>
</html>
