<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
level0-a-opens
<t:a value="level1">
level1-b-opens
<t:b value="level2">
level2-a-opens
<t:a value="level3">
level3-b-opens
<t:b value="level4">
level4-inner
</t:b>
level3-b-closed
</t:a>
level2-a-closed
</t:b>
level1-b-closed
</t:a>
level0-a-closed