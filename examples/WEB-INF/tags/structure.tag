<!doctype html>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="az" tagdir="/WEB-INF/tags" %>
<head>
<az:css/>
</head>
<body>
<az:navigation navigation="${navigation}"/>
<div class=menu>
	<az:stocks listOfStocks="${listOfStocks}"/>
	<az:news listOfNews="${listOfNews}"/>
</div>
<div class=main>
<jsp:doBody/>
</div>
</body>
</html>