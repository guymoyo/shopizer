<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>		



                 
                 <div class="control-group">
                        <label class="required"><s:message code="module.payment.cinetpay.apikey" text="API key"/></label>
	                        <div class="controls">
	                        		<form:input cssClass="input-large highlight" path="integrationKeys['api_key']" />
	                        </div>
                  </div>
                  
                   <div class="control-group">
                        <label class="required"><s:message code="module.payment.cinetpay.sitekey" text="sitekey"/></label>
	                        <div class="controls">
									<form:input cssClass="input-large highlight" path="integrationKeys['sitekey']" />
	                        </div>
                  </div>

            
                  
                  