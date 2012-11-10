<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.salesmanager.core.util.UrlUtil" %>

<script type='text/javascript'>

	function submitSearch() {
		document.searchFORM.submit();
	}
</script>

	
					<div class="section">
						<div class="section-header"><font class="section-header-1stword"><s:text name="catalog.search" /></font></div>
						<form id="searchFORM" name="searchFORM" action="<sm:url scheme="http" namespace="/" action="search" />" method="get">
							<div class="line-10px">
								<span class="input-box1"></span><span><input type="text" class="input-box2" name="search" value="<s:property value="search" />" height="26"></span><span class="input-box3"></span>
							</div>
							<div class="line-3px">
								<div class="button-right">
									<!--<span class="advance-search"><a href="#" class="link"><s:text name="catalog.advancedsearch" /></a></span>-->
									<br/><a href="javascript:submitSearch();"><div class="href-button"><span class="button1-box1"></span><span class="button1-box2"><s:text name="catalog.search" /></span><span class="button1-box3"></span></div></a>
								</div>
							</div>
						</form>
					</div>

