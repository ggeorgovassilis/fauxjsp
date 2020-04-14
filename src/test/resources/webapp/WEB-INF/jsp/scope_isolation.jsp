<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
0=${a}
<c:set var="a" value="1"/>
1=${a}
<t:scope-isolation-test a="2"/>
1=${a}
<t:scope-isolation-set-var-test a="3"/>
4=${a}