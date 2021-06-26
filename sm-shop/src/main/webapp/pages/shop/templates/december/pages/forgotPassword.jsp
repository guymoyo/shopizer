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

<link href="<c:url value="/resources/css/assets/bootstrap-social.css" />" rel="stylesheet">
 
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<script language="javascript">
	$(document).ready(function() {
		$("#msgsuccess").hide();
		$("#msgerror").hide();
	});
	function resetPass(event) {
		var resetPasswordRequest = {};
		resetPasswordRequest.returnUrl = "shop/"
		resetPasswordRequest.username = $("#email").val();
		if (!validateEmail(resetPasswordRequest.username)) {
			return; //TODO alert msg
		}

		$.ajax({
			url: "/api/v1/customer/password/reset/request",
			type: "POST",
			data: JSON.stringify(resetPasswordRequest),
			dataType: "json",
			contentType: "application/json; charset=utf-8",
			success: function (response) {
				if (!response) {
					$("#msgerror").hide()
					$("#msgsuccess").show()
				}
			},
			error: function (error) {
				if(!error){
					$("#msgsuccess").hide()
					$("#msgerror").show();
				}
			}
		});
		$("#msgerror").hide()
		$("#msgsuccess").show()
	}
</script>


			<div class="login-area ptb-80">
				<div class="container">
					<div class="row">
						<div class=" col-lg-6 col-md-6 col-sm-6 col-xs-12">
							<div class="login-title">
								<h3><s:message code="label.customer.resetpassword" text="Reset Your Password"/></h3>
								<span id="msgsuccess" class="alert alert-success"><s:message code="label.customer.resetpasswordmsg" text="We will be sending a reset password link to your email"/>.</span>
								<span id="msgerror" class="error"><s:message code="label.customer.unknown" text="label.customer.unknown"/>.</span>
							</div>
							<div id="resetPassword" class="login-form">
								<form>									
									<div class="form-group login-page">
										<label for="email"><s:message code="label.customer.email" text="Customer email address"/> <span>*</span></label>
										<input type="email" class="form-control" placeholder="Ex: jean@gmail.com" id="email" name="email">
									</div>
									<a  id="resetPassword-btn" class="btn btn-default login-btn" onclick="resetPass()"><s:message code="button.label.reset" text="Reset"/></a>
								</form>
							</div>



						</div>
						<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
							<div class="login-title">
								<h3><s:message code="label.customer.new" text="New customer"/></h3>
								<span><s:message code="label.customer.faster" text="Creating an account has many benefits: check out faster, keep more than one address, track orders and more."/></span>
							</div>
							<a class="btn btn-default login-btn" href="<s:url value="/shop/customer/registration.html"/>"><s:message code="button.label.register" text="Register" /></a>
							<div class="login-title pt-40">
								<h3><s:message code="label.customer.registered" text="Registered customer"/></h3>
								<span><s:message code="label.customer.registered.signinemail" text="If you have an account, sign in with your email address."/></span>
							</div>
							<a class="btn btn-default login-btn" href="<s:url value="/shop/customer/customLogon.html"/>"><s:message code="button.label.signin" text="Sign In" /></a>
						</div>
					</div>
				</div>
			</div>

