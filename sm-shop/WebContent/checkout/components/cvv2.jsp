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
<s:text name="label.creditcard.mcvisacvv2"/>
</div>
<br>
</td>
</tr>
<tr>
<td>
<div class="formelement">
<s:text name="label.creditcard.amexcvv2"/>
</div>
<br>
</td>
</table>

</body>
</html>