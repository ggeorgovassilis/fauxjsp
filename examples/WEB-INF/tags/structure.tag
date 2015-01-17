<!doctype html>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<head>
</head>
<body>
<t:navigation navigation="${navigation}"/>
<div class=menu>
	<t:stocks listOfStocks="${listOfStocks}"/>
	<t:news listOfNews="${listOfNews}"/>
</div>
<div class=main>
<jsp:doBody/>
</div>
</body>
</html>