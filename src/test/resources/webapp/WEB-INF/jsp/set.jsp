<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="y" value="string"/>
<c:set var="z"><c:forEach var="i" begin="1" end="5">${i},</c:forEach></c:set>
<c:set var="sum" value="${x} string"/>
${x} + ${y} = ${sum}
z = ${z}