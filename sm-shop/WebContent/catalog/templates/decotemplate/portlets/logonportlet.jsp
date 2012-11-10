<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ page import="com.salesmanager.core.util.UrlUtil" %>
<%@taglib prefix="s" uri="/struts-tags" %>
					<!-- THIS IS A PORTLET, NEEDS TO BE AN INCLUDE COMPONENT -->
					<div class="section">
						<div class="section-header">
							<font class="section-header-1stword"><s:text name="label.members" /></font>&nbsp;
							<s:if test="principal.remoteUser==null">
								<s:text name="label.acces" />
							</s:if>
							<s:else>
								<s:text name="label.menu.function.PROF01" />
							</s:else>
						</div>
						<div class="line-10px" style="display:block;">
							<a id="resetPassword" href="#resetpasswordText" title="<s:text name="label.logonform.resetpassword" />" class="link resetpassword"><s:text name="label.logonform.forgotpassword" /></a>
							<br>
							<a id="register" href="<sm:url scheme="https" namespace="/profile" action="displayRegistration" />" title="<s:text name="customer.register" />" class="link"><s:text name="customer.register" /></a>
						</div>


						<form id="loginFORM" name="loginFORM" action="<sm:url scheme="https" namespace="/profile" action="localLogon" />" method="post">
							
							<s:if test="principal.remoteUser==null">
								<div class="line-10px" id="logonMessage" style="display:none;"></div>
								<div class="line-10px" id="loginCustomer" style="display:none;">
							</s:if>
							<s:else>
								<div class="line-10px" id="logonMessage" style="display:none;"></div>
								<div class="line-10px" id="loginCustomer" style="display:block;">
							</s:else>
									<s:text name="label.generic.welcome" />&nbsp;<b><s:property value="#request.CUSTOMER.name" /></b><br>
									<a href="<sm:url scheme="https" namespace="/profile" action="profile" />"><s:text name="label.profile.information" /></a><br>
									<a href="<sm:url scheme="http" namespace="/profile" action="logout" />"><s:text name="button.label.logout" /></a>
								</div>
							<s:if test="principal.remoteUser==null">
							<div id="logonForm" style="display:block;">
							</s:if>
							<s:else>
							<div id="logonForm" style="display:none;">
							</s:else>
							<div class="line-10px">
								<div class="login-left"><s:text name="username" /></div>
								<div class="login-right">
									<span class="login-box1"></span><span><input id="username" type="text" class="login-box2" name="username" value="" title="<s:text name="label.customer.youremailaddress"/>"></span><span class="login-box3"></span>
								</div>
							</div>
							<div class="line-2px">
								<div class="login-left"><s:text name="password" /></div>
								<div class="login-right">
									<span class="login-box1"></span><span><input id="password" type="password" class="login-box2" name="password" value=""></span><span class="login-box3"></span>
								</div>
							</div>
							<div class="line-3px">
								<div class="button-right">
										<br/>
										<a href="javascript:document.loginFORM.submit();"><div class="href-button"><span class="button1-box1"></span><span class="button1-box2"><s:text name="button.label.submit2" /></span><span class="button1-box3"></span></div></a>
								</div>
							</div>
							</div>
						</form>
					</div>

					<div style="display: none;">
						<div id="resetpasswordText" style="width:275px;height:200px;overflow:auto;">
							<div class="section">
								<div class="section-header">
									<font class="section-header-1stword"><s:text name="label.logonform.resetpassword" /></font>
								</div>
								<form name="resetPassword" action="<sm:url scheme="https" namespace="/profile" action="resetPassword" />" method="post">
								<div class="line-10px" id="resetPasswordMessage" style="display:none;"></div>
								<div class="line-10px">
									<div class="login-left"><s:text name="username" /></div>
									<div class="login-right">
										<span class="login-box1"></span><span><input id="resetpasswordusername" type="text" class="login-box2" name="resetpasswordusername" value="" title="<s:text name="label.customer.youremailaddress"/>"></span><span class="login-box3"></span>
									</div>
								</div>
								<div class="line-3px">
									<div class="button-right">
										<a href="#" onClick="javascript:document.resetPassword.submit();">
											<div class="href-button">
												<span class="button1-box1"></span>
												<span class="button1-box2"><s:text name="button.label.reset" /></span>
												<span class="button1-box3"></span>
											</div>
										</a>
									</div>
								</div>
								</form>
							</div>
						</div>
					</div>

