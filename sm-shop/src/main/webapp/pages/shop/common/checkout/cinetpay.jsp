<%
response.setCharacterEncoding("UTF-8");
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setDateHeader ("Expires", -1);
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="/WEB-INF/shopizer-tags.tld" prefix="sm" %> 
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>   


          <div class="control-group">
            <label class="control-label"><s:message code="label.payment.cinetpay" text="Cliquez et payez"/></label>
            <div class="controls">
               <jsp:include page="/pages/shop/common/checkout/selectedPayment.jsp" />
            </div>
          </div>
		 
		 <div class="control-group">
		 
		 	<!-- cinetPay Logo -->
		 	<table border="0" cellpadding="10" cellspacing="0" align="center"><tr><td align="center"></td></tr><tr><td align="center"><img src="/resources/img/payment/cinetpay.png" border="0" alt="PayPal Logo"></td></tr></table>
		 </div>