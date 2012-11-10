<#if fieldErrors?exists><#t/>
<#assign eKeys = fieldErrors.keySet()><#t/>
<#assign eKeysSize = eKeys.size()><#t/>
<#assign doneStartUlTag=false><#t/>
<#assign doneEndUlTag=false><#t/>
<#assign haveMatchedErrorField=false><#t/>
<#if (fieldErrorFieldNames?size > 0) ><#t/>
<div class="icon-error">
	<#list fieldErrorFieldNames as fieldErrorFieldName><#t/>
		<#list eKeys as eKey><#t/>
		<#if (eKey = fieldErrorFieldName)><#t/>
			<#assign haveMatchedErrorField=true><#t/>
			<#assign eValue = fieldErrors[fieldErrorFieldName]><#t/>
			<#if (haveMatchedErrorField && (!doneStartUlTag))><#t/>
				<ul>
				<#assign doneStartUlTag=true><#t/>
			</#if><#t/>
			<#list eValue as eEachValue><#t/>
				<li>${eEachValue}</li>
			</#list><#t/>			
		</#if><#t/>
		</#list><#t/>
	</#list><#t/>
	<#if (haveMatchedErrorField && (!doneEndUlTag))><#t/>
		</ul>
		<#assign doneEndUlTag=true><#t/>
	</#if><#t/>
</div>
<#else><#t/>	
	<#if (eKeysSize > 0)><#t/>
	<div class="icon-error">
		<ul>
			<#list eKeys as eKey><#t/>
				<#assign eValue = fieldErrors[eKey]><#t/>
				<#list eValue as eEachValue><#t/>
					<li>${eEachValue}</li>
				</#list><#t/>
			</#list><#t/>
		</ul>
	</div>
	</#if><#t/>
</#if><#t/>
</#if><#t/>

