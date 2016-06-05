<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.hose.model.Donkey"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE HTML> 
<html>
	<head>
		<style type="text/css">	
			body{margin:0;padding:0;}
			.desc_item{margin-top: 12px;}
			.choice{margin-right:15px}
			.desc{background-image:url(assets/avatars/bk.jpg); 
				background-repeat:no-repeat;
				background-size:100% 100%;
				font-size:4vw; 
				color: white;
				padding-bottom:20px;
				}
			.title{width: 4em;}
			.w2{
			     letter-spacing:2em; /*如果需要y个字两端对齐，则为(x-y)/(y-1),这里是（4-2）/(2-1)=2em */
			     margin-right:-2em; /*同上*/
			     }
			.w3{
			     letter-spacing:0.5em; /*如果需要y个字两端对齐，则为(x-y)/(y-1),这里是（4-3）/(3-1)=0.5em */
			     margin-right:-0.5em; /*同上*/
			     }
			.w4{
			     letter-spacing:0.2em; /*如果需要y个字两端对齐，则为(x-y)/(y-1),这里是（4-3）/(3-1)=0.5em */
			     margin-right:-0.2em; /*同上*/
			     }
			.img{
				max-width:100%;height:auto;margin:0;padding:0;display:block;
				}
				
			#title_img{
				max-width:15%;height:auto;vertical-align:middle;
				}
			#title{font-size:5vw;}
			
		</style>
		
		<script type="text/javascript">
			var u = navigator.userAgent, app = navigator.appVersion;
			var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
			var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
		</script>
	</head>
	<body>	
		<div class="desc">	
			<div id="title" align="center">档   <img id="title_img" src="assets/avatars/donkey.png" /> 案</div>
			<div><span class="w2 title">编号</span>:		${donkey.sn}</div>
			<div class="desc_item"><span class="title">养殖地址</span>:	科尔沁草原地区</div>
			<div class="desc_item"><span class="w2 title">品种</span>:	
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.breed eq 1}">   
							       		<img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose>
								关中驴
		 					</span>      
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.breed eq 2}">   
							       		<img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose>
								德州驴 
		 					</span>       
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.breed eq 3}">   
							            <img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose>
								华北驴
							</span>       
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.breed eq 4}">   
							            <img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose>
								其他 
							</span>
			</div>
			<div class="desc_item"><span class="w2 title">性别</span>:	
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.sex eq 1}">   
							            <img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose>
								雄性 
							</span>        
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.sex eq 2}">   
							            <img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose>
								雌性
							</span>        
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.sex eq 3}">   
							            <img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose>
								阉割
							</span>
			</div>
			<div class="desc_item"><span class="title">屠宰年龄</span>:	${donkey.agewhenkill}</div>
			<div class="desc_item"><span class="title">饲养方式</span>:	
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.feedpattern eq '圈养'}">   
							            <img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose>
								圈养
							</span>    
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.feedpattern eq '放养'}">   
							            <img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose>
								放养
							</span>
			</div>
			<div class="desc_item"><span class="title">饲草类别</span>:	${donkey.forage}</div>
			<div class="desc_item"><span class="title">健康状况</span>:	${donkey.healthstatus}</div>
			<div class="desc_item"><span class="title">屠宰单位</span>:	${donkey.killdepartment}</div>
			<div class="desc_item"><span class="title">保鲜方式</span>:	
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.freshkeepmethod eq '鲜肉'}">   
							            <img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose>
								鲜肉
							</span>	  
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.freshkeepmethod eq '冻肉'}">   
							            <img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose> 
								冻肉
							</span>
			</div>
			<div class="desc_item"><span class="w3 title">保质期</span>:	${donkey.freshkeeptime}</div>
			<div class="desc_item"><span class="w2 title">质检</span>:		
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.qualitystatus eq '合格'}">   
							            <img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose> 
								合格
							</span>	   
							<span class="choice">
								<c:choose>   
							       <c:when test="${donkey.qualitystatus eq '不合格'}">   
							            <img src="assets/avatars/check_32.png" />
							       </c:when>   							    
							       <c:otherwise>   
							            <img src="assets/avatars/uncheck_32.png" />
							       </c:otherwise>   
								</c:choose>
								不合格
							</span>
			</div>
			<div class="desc_item"><span class="w4 title">QC编号</span>:	${donkey.qc}</div>
			<div class="desc_item"><span class="w4 title">QA编号</span>:	${donkey.qa}</div>
		</div>
		<c:forEach var="imageurl" items="${imageurls}">
			<img class="img" src="${imageurl}" />
		</c:forEach>		
	</body>
</html>