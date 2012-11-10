<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="Content-Style-Type" content="text/css">
    <meta http-equiv="Content-Script-Type" content="text/javascript">



    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/layout.css" type="text/css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/customer.css" type="text/css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/cart.css" type="text/css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/common/css/styles.css" type="text/css" />
</head>
<body>
<table>
<tr>
<td>
<div class="formelement">
<s:text name="label.creditcard.mcvisacvv"/>
</div>
<br>
<img src="<%=request.getContextPath()%>/common/img/cvv2visa.gif">
</td>
</tr>
<tr>
<td>
<div class="formelement">
<s:text name="label.creditcard.amexcvv"/>
</div>
<br>
<img src="<%=request.getContextPath()%>/common/img/cvv2amex.gif">
</td>
</tr>
<tr>
<td align="right">
<a href="" onClick="self.parent.tb_remove();"><s:text name="button.label.close"/></a>
</td>
</tr>
</table>

</body>
</html>