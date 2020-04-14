<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:structure>
<p>This is the content of the main page. We are free to do whatever JSP allows us to do here:</p>
<ul>
<li>HTML</li>
<li>Images</li>
<li>Javascript</li>
</ul>
<p>
The content of this box (with the dashed border) will change as you navigate around. The content of the stocks and news boxes (on the right) will stay the same.
Also, the navigation box (top) will stay but its content will change.
</p>
<t:news listOfNews="${listOfNews}"/>
<p>The current date is <%=((java.util.Date)pageContext.getAttribute("date")).getYear()+1900 %>-${date.month+1}-${date.date}</p>
</t:structure>