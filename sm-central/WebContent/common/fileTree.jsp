<%@ page import="java.io.File,java.io.FilenameFilter,java.util.Arrays"%>
<%@ page import="com.salesmanager.central.profile.ProfileConstants" %>
<%@ page import="com.salesmanager.core.util.*" %>
<%@ page import="com.salesmanager.core.entity.merchant.MerchantStore" %>
<%@ page import="com.salesmanager.core.service.ServiceFactory" %>
<%@ page import="com.salesmanager.core.service.merchant.MerchantService" %>
<%

    	String dir = request.getParameter("dir");
	String dirType = request.getParameter("fileType");
	String serverUrl = request.getParameter("serverUrl");
	boolean isMediaBin = (request.getParameter("isMediaBin")!= null)?Boolean.parseBoolean(request.getParameter("isMediaBin")):false;

      if (dir == null) {
    	  return;
      }

	dir = dir.replaceAll("%3A",":/");
	
	if (dir.charAt(dir.length()-1) == '\\') {
    	dir = dir.substring(0, dir.length()-1) + "/";
	} else if (dir.charAt(dir.length()-1) != '/') {
	    dir += "/";
	}


    Integer merchantid = (Integer)request.getSession().getAttribute(ProfileConstants.merchant);


	//System.out.println("************************************************");
	//System.out.println("Before files");
	//System.out.println(isMediaBin);
	//System.out.println(dir);
	//System.out.println("************************************************");

if (new File(dir).exists()) {
		
		//get product images and same block for getting media files
		String[] files = new File(dir).list(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
				return name.charAt(0) != '.';
		    }
		});
		Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
		out.print("<ul class=\"jqueryFileTree\" style=\"display: none;\">");
		// All dirs
		if(isMediaBin){
			for (String file : files) {
				String line = "<li class=\"directory collapsed\"><a href=\"#\"  rel=\"" + dir + file + "/" + merchantid + "/\">" + file + "</a></li>";
				if (new File(dir, file).isDirectory()) {
					out.print(line);
					//System.out.println("--------------------------------------");
					//System.out.println(serverUrl + file);
					//System.out.println("--------------------------------------");
				}
			}
		}else{
			for (String file : files) {
				String line = "<li class=\"directory collapsed\"><a href=\"#\"  rel=\"" + dir + file + "/" + merchantid + "/\">"+ file + "</a></li>";
				if (new File(dir, file).isDirectory()) {
					out.print(line);
				}
			}
		}

		// All files
		for (String file : files) {
		    if (!new File(dir, file).isDirectory()) {
				int dotIndex = file.lastIndexOf('.');
				String ext = dotIndex > 0 ? file.substring(dotIndex + 1) : "";
				String line = "<li class=\"file ext_" + ext + "\"><a href=\"#\" onclick=\"javascript:displayFile('" + dir + file + "')\" class=\"screenshot\" rel=\"" + serverUrl + file + "\">" + file + "</a></li>";
				if(isMediaBin) {
					line = "<li class=\"file ext_" + ext + "\"><a href=\"#\" onclick=\"javascript:displayFile('" + dir + file + "')\" class=\"screenshot\" rel=\"" + serverUrl + file + "\">" + file + "</a><a href=\"#\" onclick=\"javascript:deleteFile('" + dir + file + "');\"><img src=\"" + request.getContextPath() + "/common/img/icon-red.png\"></a></li>";
				}
				out.print(line);
				//System.out.println("**************************************");
				//System.out.println(line);
				//System.out.println(isMediaBin);
				//System.out.println("**************************************");
				
		    	}
		}
		out.print("</ul>");
    }%>

