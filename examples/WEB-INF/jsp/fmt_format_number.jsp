<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
#1 <fmt:formatNumber value="${balance}" type="currency" />
#2 <fmt:formatNumber type="number" maxIntegerDigits="3" value="${balance}" />
#3 <fmt:formatNumber type="number" maxFractionDigits="3" groupingUsed="false" value="${balance}" />
#4 <fmt:formatNumber type="number" groupingUsed="false" value="${balance}" />
#5 <fmt:formatNumber type="percent" maxIntegerDigits="3" value="${balance}" />
#6 <fmt:formatNumber type="percent" minFractionDigits="10" value="${balance}" />
#7 <fmt:formatNumber type="percent" maxIntegerDigits="3" value="${balance}" />
#8 <fmt:formatNumber type="number" pattern="###.###E0" value="${balance}" />
#9 <fmt:setLocale value="en_US"/><fmt:formatNumber value="${balance}" type="currency"/>