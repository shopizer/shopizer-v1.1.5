<#if (actionErrors?exists && actionErrors?size > 0)>
<div class="icon-error">
	<ul>
	<#list actionErrors as error>
		<li><span class="errorMessage">${error}</span></li>
	</#list>
	</ul>
</div>
</#if>
