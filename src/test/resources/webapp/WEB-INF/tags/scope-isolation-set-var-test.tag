<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="a" required="true" rtexprvalue="true" type="java.lang.String"%>
a=${a}
<c:set var="a" value="4"/>
a=${a}