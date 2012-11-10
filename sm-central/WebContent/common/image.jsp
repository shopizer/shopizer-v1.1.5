<%@ page import="com.salesmanager.central.web.DynamicImage"%>
<%@ page import = "java.io.*" %>


<%@ page contentType="image/jpeg"%>


<%

  String lookupkey = (String)request.getParameter("lookupkey");


  //System.out.println("Lookup key");
  //System.out.println(lookupkey);

  if(lookupkey==null) {lookupkey="STORE-IMAGE";}

  if ( request.getSession()
		  .getAttribute(lookupkey) != null ) {



	DynamicImage img = null;

	img = (DynamicImage)request.getSession()
	  .getAttribute(lookupkey) ;
	FileInputStream  in = null;
    try
    {

       // get the image from the database
       in = new FileInputStream(img.getImagePath() + img.getImageName());
       //System.out.println("image="+img.getImagePath() + img.getImageName());
       //System.out.println("inputStream.available()="+in.available());
       byte[] data = new byte[in.available()];

       in.read(data);
       // display the image
       response.setContentType("image/jpeg");
       response.setHeader ("Pragma", "no-cache");
       response.setHeader ("Cache-Control", "no-cache");
       response.setDateHeader ("Expires",0);
       OutputStream o = response.getOutputStream();
       o.write(data);
       o.flush();
       o.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw e;
    }
    finally
    {

      request.getSession()
	  .removeAttribute(lookupkey) ;
      if(in!=null) {
    	  try {
    		 in.close();
    	  } catch(Exception ignore) {
    	  }
      }
    }
  }
%>
