<%@ tag language="java" pageEncoding="UTF-8"%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  


<c:url scope="session" var="searchPlayerNameurl" value="/bat/searchPlayerName"/>  
<c:url scope="session" var="batsmanRecentFormUrl" value="/bat/recentform"/>  
<c:url scope="session" var="batsmanScoreRangeUrl" value="/bat/scoreRange"/>  


<script type="text/javascript">
	var searchPlayerNameurl = "${searchPlayerNameurl}";
	var batsmanRecentFormUrl = "${batsmanRecentFormUrl}";
	var batsmanScoreRangeUrl = "${batsmanScoreRangeUrl}";
</script>