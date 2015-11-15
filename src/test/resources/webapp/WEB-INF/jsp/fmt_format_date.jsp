<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
#1: <fmt:formatDate type="time" value="${now}" />
#2: <fmt:formatDate type="date" value="${now}" />
#3: <fmt:formatDate type="both" value="${now}" />
#4: <fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${now}" />
#5: <fmt:formatDate type="both" dateStyle="medium" timeStyle="medium" value="${now}" />
#6: <fmt:formatDate type="both" dateStyle="long" timeStyle="long" value="${now}" />
#7: <fmt:formatDate pattern="yyyy-MM-dd" value="${now}" />
#8: <fmt:formatDate type="both" dateStyle="short" timeStyle="long" value="${now}" var="formatresult"/><c:out value="${formatresult}"/>