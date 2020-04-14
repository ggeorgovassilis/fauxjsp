<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:tag_with_optional_attribute/>
<t:tag_with_optional_attribute X="2"/>
<c:set var="X" value="3"/>
<t:tag_with_optional_attribute/>
<t:tag_with_optional_attribute X="4"/>
