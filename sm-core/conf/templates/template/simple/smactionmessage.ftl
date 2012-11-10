<#if (actionMessages?exists && actionMessages?size > 0)>
<div class="icon-ok">
	<ul>
		<#list actionMessages as message>
			<li><span class="actionMessage">${message}</span></li>
		</#list>
	</ul>
</div>
</#if>