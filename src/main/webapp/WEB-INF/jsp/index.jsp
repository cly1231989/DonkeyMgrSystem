<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html>
	<head>
		<meta charset="utf-8" />
		<title>毛驴信息管理系统</title>

		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link rel="stylesheet" href="assets/css/bootstrap.min.css" />
		<link rel="stylesheet" href="assets/css/font-awesome.min.css" />
		<link rel="stylesheet" href="assets/css/jquery-ui-1.10.3.full.min.css" />
		<link rel="stylesheet" href="assets/css/datepicker.css" />
		<link rel="stylesheet" href="assets/css/ui.jqgrid.css" />
		<link rel="stylesheet" href="assets/css/dropzone.css" />
		<script src="assets/js/ace-extra.min.js"></script>
		<script src='assets/js/jquery-2.0.3.min.js'></script>;
		<script src="assets/js/jquery-ui-1.10.3.full.min.js"></script>
		<script src="assets/js/dropzone.min.js"></script>
		<script src="assets/js/jquery.validate.js"></script>
		<link rel="stylesheet" href="assets/css/ace.min.css" />
		<link rel="stylesheet" href="assets/css/ace-rtl.min.css" />
		<link rel="stylesheet" href="assets/css/ace-skins.min.css" />
	</head>

	<body>
		<div class="navbar navbar-default" id="navbar">
			<script type="text/javascript">
				try{ace.settings.check('navbar' , 'fixed')}catch(e){}
			</script>

			<div class="navbar-container" id="navbar-container">
				<div class="navbar-header pull-left">
					<a href="#" class="navbar-brand">
						<small>
							<i class="icon-leaf"></i>
							内蒙古阿鲁科尔沁旗太极天驴有限公司毛驴信息管理系统
						</small>
					</a><!-- /.brand -->
				</div><!-- /.navbar-header -->

				<div class="navbar-header pull-right" role="navigation">
					<ul class="nav ace-nav">
						<li class="light-blue">
							<a data-toggle="dropdown" href="#" class="dropdown-toggle">
								<img class="nav-user-photo" src="assets/avatars/user.jpg" alt="Jason's Photo" />
								<span class="user-info">
									<small>欢迎,</small>
									${username}
								</span>

								<i class="icon-caret-down"></i>
							</a>

							<ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
							 
								<li>
									<a id="modify-pwd" href="#">
										<i class="icon-cog"></i>
										修改密码
									</a>
								</li>
								<li class="divider"></li>
								<li>
									<a href="/DonkeyMgrSystem/logout">
										<i id="logoutbtn" class="icon-off"></i>
										退出系统
									</a>
								</li>
							</ul>
						</li>
					</ul><!-- /.ace-nav -->
				</div><!-- /.navbar-header -->
			</div><!-- /.container -->
		</div>

		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>

			<div class="main-container-inner">
				<a class="menu-toggler" id="menu-toggler" href="#">
					<span class="menu-text"></span>
				</a>

				<div class="sidebar" id="sidebar">
					<script type="text/javascript">
						try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
					</script>
					<ul class="nav nav-list">
						<li class="active">
							<a href="index.html">
								<i class="icon-dashboard"></i>
								<span class="menu-text">毛驴信息管理</span>
							</a>
						</li>
					</ul><!-- /.nav-list -->

					<div class="sidebar-collapse" id="sidebar-collapse">
						<i class="icon-double-angle-left" data-icon1="icon-double-angle-left" data-icon2="icon-double-angle-right"></i>
					</div>

					<script type="text/javascript">
						try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
					</script>
				</div>

				<div class="main-content">
					<div class="breadcrumbs" id="breadcrumbs">
						<script type="text/javascript">
							try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
						</script>
					</div>
					
					<div id="dialog-modify-pwd" title="修改密码" dispaly=none>
					  <form>
						  <fieldset>
						  <div class="space-4"></div>
						  	<label class="block clearfix">
								<span class="block input-icon input-icon-right">
									<input type="password" id="oldpwd" name="oldpwd" class="form-control" placeholder="请输入原密码" />
									<i class="icon-lock"></i>
								</span>
							</label>
							<div class="space"></div>
							<label class="block clearfix">
								<span class="block input-icon input-icon-right">
									<input type="password" id="pwd" name="pwd" class="form-control" placeholder="请输入新密码" />
									<i class="icon-lock"></i>
								</span>
							</label>
							<div class="space"></div>
							<label class="block clearfix">
								<span class="block input-icon input-icon-right">
									<input type="password" id="confirm-pwd" name="confirm-pwd" class="form-control" placeholder="再次输入新密码" />
									<i class="icon-lock"></i>
								</span>
							</label>
							<label id="tiplabel" class="block clearfix red"></label>
						  </fieldset>
					  </form>
					</div>	
					
					<div class="main-container-inner" id="dialog-uploadfile" title="上传图片" dispaly=none>				
						<div class="row">
							<div class="col-xs-12">
								<!-- PAGE CONTENT BEGINS -->								
								<div id="dropzone">
									<form action="/DonkeyMgrSystem/donkey/images/upload" class="dropzone" method="post">
										<input type="text" id="donkeyid" name="donkeyid" hidden/>
										<div class="fallback">
											<input name="file" type="file" multiple="" />
										</div>
									</form>
								</div><!-- PAGE CONTENT ENDS -->
							</div><!-- /.col -->
						</div>
					</div>

					<div class="page-content">
						<div class="row">
							<div class="col-xs-12">
								<!-- PAGE CONTENT BEGINS -->
								<table id="grid-table"></table>
								<div id="grid-pager"></div>

								<script type="text/javascript">
									var $path_base = "/";//this will be used in gritter alerts containing images
								</script>
								<!-- PAGE CONTENT ENDS -->
							</div><!-- /.col -->
						</div><!-- /.row -->
					</div><!-- /.page-content -->
				</div><!-- /.main-content -->
			</div><!-- /.main-container-inner -->

			<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
				<i class="icon-double-angle-up icon-only bigger-110"></i>
			</a>
		</div><!-- /.main-container -->
		<script type="text/javascript">
			if("ontouchend" in document) document.write("<script src='assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
		</script>
		<script src="assets/js/bootstrap.min.js"></script>
		<script src="assets/js/typeahead-bs2.min.js"></script>
		<script src="assets/js/date-time/bootstrap-datepicker.min.js"></script>
		<script src="assets/js/jqGrid/jquery.jqGrid.min.js"></script>
		<script src="assets/js/jqGrid/i18n/grid.locale-cn.js"></script>
		<script src="assets/js/ace-elements.min.js"></script>
		<script src="assets/js/ace.min.js"></script>
		<script src="assets/js/donkey-function.js"></script>
</body>
</html>
