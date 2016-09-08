<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
	<head>
		<meta charset="utf-8" />
		<title>登录 </title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />

		<link rel="stylesheet" href="assets/css/bootstrap.min.css"  />
		<link rel="stylesheet" href="assets/css/font-awesome.min.css" />
		<link rel="stylesheet" href="assets/css/ace.min.css" />
		<link rel="stylesheet" href="assets/css/ace-rtl.min.css" />
		
		<script src="assets/js/jquery-2.0.3.min.js"></script>
		<script src="assets/js/jquery.mobile.custom.min.js"></script>
		<script src="assets/js/jquery.cookie.js"></script>
		
		<style type="text/css">	
			body{margin:0;padding:0;
				background-image:url(assets/avatars/bk.jpg); 
				background-repeat:no-repeat;
				background-size:100% 100%;
				}
		</style>
	</head>

	<body class="login-layout">
		<div class="main-container">
			<div class="main-content">
				<div class="row">
					<div class="col-sm-10 col-sm-offset-1">
						<div class="login-container">
							<div class="center">
								<!--  <h1>
									<i class="icon-leaf green"></i>
									<span class="red">Ace</span>
									<span class="white">Application</span>
								</h1>-->
								<div class="space"></div>
								<div class="space"></div>
								<div class="space"></div>
								<div class="space"></div>
								<div class="space"></div>
								<h4 class="blue">&copy; 内蒙古阿鲁科尔沁旗太极天驴有限公司</h4>
							</div>

							<div class="space-6"></div>

							<div class="position-relative">
								<div id="login-box" class="login-box visible widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header blue lighter bigger">
												<i class="icon-coffee green"></i>
												请输入您的登录信息
											</h4>

											<div class="space-6"></div>

											<form>
												<fieldset>
													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="text" id="username" class="form-control" placeholder="用户名" />
															<i class="icon-user"></i>
														</span>
													</label>

													<label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input type="password" id="pwd" class="form-control" placeholder="密码" />
															<i class="icon-lock"></i>
														</span>
													</label>
													<label id="tiplabel" class="block clearfix red"></label>

													<div class="space"></div>

													<div class="clearfix">
														<label class="inline">
															<input type="checkbox" class="ace" id="remembermecheckbox"/>
															<span class="lbl"> 记住我</span>
														</label>

														<button type="button" id="loginbtn" class="width-35 pull-right btn btn-sm btn-primary">
															<i class="icon-key"></i>
															登录
														</button>
													</div>

													<div class="space-4"></div>
												</fieldset>
											</form>
										</div><!-- /widget-main -->
									</div><!-- /widget-body -->
								</div><!-- /login-box -->
							</div><!-- /position-relative -->
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div>
		</div><!-- /.main-container -->

		<script type="text/javascript">
			function show_box(id) {
			 jQuery('.widget-box.visible').removeClass('visible');
			 jQuery('#'+id).addClass('visible');
			}
		</script>
		
		<script>
			$(function(){
				
				$("#loginbtn").click(function(){
					if($("#username").val()==""){
						$("#tiplabel").html("请输入用户名");
						return;
					}
					
					if($("#pwd").val()==""){
						$("#tiplabel").html("请输入密码");
						return;
					}
					
					$.ajax({
						type: "POST",
						url: "/DonkeyMgrSystem/login-rest",  
						data: {"name":$('#username').val(),
							   "pwd":$('#pwd').val()},  
						dataType: "json", 
						traditional: true,  
						success: function(result){ 		
							//var jsonResult = eval ("(" + result + ")");
							if(result.result == "success"){														
								if( $('#remembermecheckbox').val() == "on" ){
									$.cookie('user_name', $('#username').val(), {expires:30});
									$.cookie('user_pwd', $('#pwd').val(), {expires:30});								
								}
								window.location.href="/DonkeyMgrSystem/index";
							}
							else{
								$("#tiplabel").html("用户名或密码错误");
							}
						},
						error: function(result){
							$("#tiplabel").html("发生内部错误");
						}
					});
				});
				
				function init(){
					var username = $.cookie('user_name');
					var userpwd = $.cookie('user_pwd');
					if(username != null && userpwd != null){
						$('#username').val(username);
						$('#pwd').val(userpwd);
						$('#remembermecheckbox').attr("checked","checked");
					}
				};
				init();
			});
		</script>
</body>
</html>
