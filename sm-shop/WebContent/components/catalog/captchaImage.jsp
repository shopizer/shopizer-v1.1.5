<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import = "java.io.*" %>


<%@ page contentType="image/jpeg"%>


<%

    try
    {

       byte[] data = (byte[])request.getSession().getAttribute("CAPTCHAIMAGE");

	 if(data!=null) {


       	// display the image
       	response.setContentType("image/jpeg");
       	response.setHeader ("Pragma", "no-cache");
       	response.setHeader ("Cache-Control", "no-cache");
       	response.setDateHeader ("Expires",0);
       	OutputStream o = response.getOutputStream();

		if(o!=null && data!=null) {
       	o.write(data);
       	o.flush();
       	o.close();
		}

	}
    }
    catch (Exception e)
    {
      e.printStackTrace();
      //throw e;
    }
    finally
    {
		request.getSession().removeAttribute("CAPTCHAIMAGE");
    }

%>



