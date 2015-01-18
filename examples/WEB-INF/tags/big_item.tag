<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="item" required="true" rtexprvalue="true" type="jspparser.dto.Item"%> 
<item id="${item.id}" childcount="${fn:length(item.items)}">
<c:forEach var="child" items="${item.items}">
<t:big_item item="${child}"></t:big_item>
</c:forEach>
</item>