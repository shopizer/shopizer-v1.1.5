<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/classes/sm.tld" prefix="sm" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.salesmanager.core.constants.LabelConstants" %>
<%@ page import="java.util.*" %>
<%@taglib prefix="s" uri="/struts-tags" %>



											<s:set name="averageRatingRound" value="product.productReviewRound" scope="request"/>
											<s:set name="averageRating" value="product.productReview" scope="request"/>
					
											<c:forEach var="i" begin="0" end="4" step="1">
												<c:choose>
													<c:when  test="${i<request.averageRatingRound}">
														<img   src="<%=request.getContextPath()%>/common/img/redStar.gif"  border="0">
													</c:when>
													<c:otherwise>

													<c:choose>
													<c:when   test="${request.averageRating>request.averageRatingRound && i == (request.averageRatingRound)}">
														<img   src="<%=request.getContextPath()%>/common/img/halfRedStar.gif"  border="0">
													</c:when>
													<c:otherwise>
														<img   src="<%=request.getContextPath()%>/common/img/whiteStar.gif"  border="0">
													</c:otherwise>
													</c:choose>
													</c:otherwise> 
												</c:choose>
											</c:forEach>&nbsp;(<s:text name="label.generic.outof" /> <s:property value="product.productReviewCount" /> <s:text name="catalog.review" />) 