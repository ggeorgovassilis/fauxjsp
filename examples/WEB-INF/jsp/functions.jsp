<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${fn:startsWith(s1,s2)}">Condition 1</c:if>
<c:if test="${fn:startsWith(s1,s3)}">Condition 2</c:if>