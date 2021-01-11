<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<${tagName}
someattribute="somevalue1"
<c:choose>
<c:when test="${condition}">
someattribute="somevalue2"
</c:when>
</c:choose>
>sometext</${tagName}>