<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${fn:startsWith(s1,s2)}">Condition 1</c:if>
<c:if test="${fn:startsWith(s1,s3)}">Condition 2</c:if>
<c:if test="${fn:contains(s1,s3)}">Condition 3</c:if>
<c:if test="${fn:contains(s1,'bla bla')}">Condition 4</c:if>
<c:if test="${fn:containsIgnoreCase('alice had a little lamb','Alice')}">Condition 5</c:if>
<c:if test="${fn:containsIgnoreCase('alice had a little lamb','Beth')}">Condition 6</c:if>
<c:if test="${fn:endsWith('alice had a little lamb','lamb')}">Condition 7</c:if>
<c:if test="${fn:endsWith('alice had a little lamb','car')}">Condition 8</c:if>
escapeXml ${fn:escapeXml(xml)}
indexOf ${fn:indexOf('the rain in spain','rain')}
join ${fn:join(arr,'_')}
length ${fn:length(arr)}, ${fn:length(list)}, ${fn:length(s1)}
replace ${fn:replace('Life is tolerable','tolerable','good')}
split ${fn:join(fn:split('The rain in Spain',' '),',')}
substring ${fn:substring('The rain in Spain',4,8)}.
toLowerCase ${fn:toLowerCase('Some Word')}
toUpperCase ${fn:toUpperCase('Some Word')}
trim (${fn:trim(' this    is  some text   ')})
