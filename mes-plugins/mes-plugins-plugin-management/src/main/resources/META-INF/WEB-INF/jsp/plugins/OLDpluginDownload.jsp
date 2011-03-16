<%--

    ***************************************************************************
    Copyright (c) 2010 Qcadoo Limited
    Project: Qcadoo MES
    Version: 0.3.0

    This file is part of Qcadoo.

    Qcadoo is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation; either version 3 of the License,
    or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty
    of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
    ***************************************************************************

--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String ctx = request.getContextPath();
%>
<script type="text/JavaScript">

	var buttonActive = true;

	jQuery(document).ready(function(){
		window.mainController.setWindowHeader("${headerLabel}");	
	});

		function checkExtension(fileName, submitName, fileTypes) {
			if (!fileName) return;

			dots = fileName.split(".")
			//get the part AFTER the LAST period.
			fileType = dots[dots.length-1];
	      
		      if (fileTypes.indexOf(fileType) != -1) {
		    	  buttonActive = true;
		    	  $("#submit").addClass("activeButton");
		    	  //submitName.disabled = false;
		        return true;
		      } else {
		        //alert("${firstCheckExtensionMessage}" + "\n\n" + (fileTypes.join(" .")) + "\n\n" + "${lastCheckExtensionMessage}");
		        //submitName.disabled = true;
		        $("#submit").removeClass("activeButton");
		        buttonActive = false;
		        return false;
		      }
		}
</script>
<html>
    <head>
        <title>${headerLabel}</title>
    </head>
    <body>
	    	<div style="margin: 0;">
		        <form method="post" action="<%=ctx%>/${downloadAction}" enctype="multipart/form-data" id="form">
		        	
		        	<div style="margin-left: 10px; margin-top: 10px; font: 11px arial; font-weight: bold;">
		        		${chooseFileLabel}
		        	</div>
		        	<div style="margin-top: 5px; margin-bottom: 20px; margin-left: 10px;">
		            	<input type="file" name="file" size="50" onChange="checkExtension(this.value, this.form.upload, ['jar']);"/>
		            </div>
		            
					<div class="linkButton activeButton" style="width: 200px; margin-left: 10px;" id="submit">
						<a href="#" onclick="if (buttonActive) {$('#form').submit();}">
							<span>
								<div id="labelDiv">${buttonLabel}</div>
							</span>
						</a>
					</div>
					
		        </form>
	        </div>
    </body>
</html>