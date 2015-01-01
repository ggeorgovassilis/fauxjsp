<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="az" tagdir="/WEB-INF/tags" %>
<%@ attribute name="listOfStocks" required="true" rtexprvalue="true" type="java.util.List"%> 
<div class="portlet stocks">
<label>Stocks</label>
<c:forEach var="stock" items="${listOfStocks}">
<div class=stock>
<div>
<span class=symbol><c:out value="${stock.shortName}"/></span>
<az:price-change change="${stock.changePercent}"/>
<span class=price><c:out value="${stock.priceInCent / 100}"/> &euro;</span>
</div>
<a class=stockname href="stocks?id=${stock.shortName}">
<c:out value="${stock.longName}"/>
</a>
</div>
</c:forEach>
</div>