<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />


<style type="text/css">
body {background-color:#ffffff; color:#000000; font-family:Verdana, Arial, Helvetica, sans-serif; text-align:center;}
a:link {color:#0066cc;}
a:hover {background-color:#eeeecc; color:#0066cc;}
a:visited {color:#0066cc;}
.holder {background-color:#f9f9f9; border:1px solid #9a9a9a; font-size:9px; text-align:left; width:550px;}
.header {font-size:10px; padding:0px; width:550px;}
.content {font-size:10px; padding:5px; width:550px;}
.footer {font-size:9px; text-align:center; width:550px;}
.extra-info {background-color:#cccc99; border:1px dashed #9a9a9a; font-size:10px; margin-top:10px; padding:5px; width:550px;}
.content-line {padding:5px; font-size:10px;}
.content-line-title {font-weight:bold; font-size:11px; padding-top:3px;}
.order-detail-area {background-color:#CCCC99; border:1px #9a9a9a; width:542px; padding:2px; font-size:10px; }
.product-details {font-size:10px;}
.product-details-num {font-size:10px; font-weight:bold;}
.order-totals-text {font-size:10px; font-weight:bold;}
.order-totals-num {font-size:10px; }
.comments {background-color:#FF6699; border:1px #9a9a9a; width:542px; padding:2px; font-size:10px; }
.address-block {background-color:#E4E8F3; border:1px solid #9a9a9a; margin-top:3px;}
.address {font-size:10px;}
.payment-detail, .payment-footer {font-size:10px;}
.extra-info {background-color:#cccc99; border:1px solid #9a9a9a; font-size:10px; margin-top:10px; padding:5px; width:550px;}
.extra-info-bold {font-weight:bold;}
.disclaimer {background-color:#f9f9f9; border:1px solid #cccccc; font-size:10px; margin-top:10px; padding:5px; width:550px;}
.disclaimer1 {color:#666666; padding:5px;}
.disclaimer1 a:link {color:#666666;}
.disclaimer1 a:visited {color:#666666;}
.disclaimer2 {color:#666666; padding:5px;}
.copyright {border-bottom:0px solid #9a9a9a; padding:5px;}

</style>
</head>

<body>
<div class="holder">

  <!-- Header Section -->
  ${LOGOPATH}


  <!-- Content Section -->
  <div class="content">
    <div class="content-line">
      ${EMAIL_STORE_NAME}<br /><br />
      ${INTRO_ORDER_NUM_TITLE} ${INTRO_ORDER_NUMBER}<br />
      ${INTRO_DATE_TITLE} ${INTRO_DATE_ORDERED}<br /><br />
      ${EMAIL_DOWNLOAD_URL_TEXT}<br />
      ${EMAIL_DOWNLOAD_URL}<br />
	</div>
  </div>

</div>


  <!-- Footer Section -->
  <div class="footer">
    <div class="copyright">${EMAIL_FOOTER_COPYRIGHT}</div>
  </div>


<div class="disclaimer">
  <div class="disclaimer1">${EMAIL_DISCLAIMER}</div>
  <div class="disclaimer2">${EMAIL_SPAM_DISCLAIMER}</div>
</div>
</body>
</html>