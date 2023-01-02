<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<title>Forget Password</title>

<script type="text/javascript">
	function validation(form) {
		var password1 = document.getElementById("password1").value;
		var password2 = document.getElementById("password2").value;

		if (password1 === password2) {
			alert("match");
		} else {
			alert("not match");
		}

	}
</script>

</head>
<body>

	<h3>Forgot Password</h3>

	<form action="/app/forgotpassword/submit" method="post">
		<table>
			<th></th>
			<tr>
				<td>User Name</td>
				<td>
					<h5>${username}</h5> <input type="hidden" name="username"
					value=${username } /> <input type="hidden" name="token"
					value=${token } />
				</td>
			</tr>
			<tr>
				<td>New Password</td>
				<td><input type="password" name="password1" id="password1"
					value="" /></td>
			</tr>
			<tr>
				<td>Confirm New Password</td>
				<td><input type="password" name="password2" id="password2"
					value="" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" name="Submit" value="Submit"
					onclick="validation(this.form)" /></td>
			</tr>

			</tr>
		</table>
	</form>

</body>
</html>