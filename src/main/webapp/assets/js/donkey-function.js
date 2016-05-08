jQuery(function($) {
				var selectID;
				var grid_selector = "#grid-table";
				var pager_selector = "#grid-pager";
				
				Dropzone.autoDiscover = false;
				try {
					var myDropzone = $(".dropzone").dropzone({
						//url: "/donkey/images/upload/"+selectID,
					    paramName: "image", // The name that will be used to transfer the file
					    maxFilesize: 5.5, // MB
					    maxFiles: 10,
					    acceptedFiles: ".jpg,.png,.bmp",
						addRemoveLinks: false,
						dictDefaultMessage :
						'<span class="bigger-150 bolder"><i class="icon-caret-right red"></i> 拖放文件</span>进行上传 \
						<span class="smaller-80 grey">(或者 点击)</span> <br /> \
						<i class="upload-icon icon-cloud-upload blue icon-3x"></i>'
					,
						dictResponseError: '上传时发生错误!',
						
						//change the previewTemplate to use Bootstrap progress bars
						previewTemplate: "<div class=\"dz-preview dz-file-preview\">\n  <div class=\"dz-details\">\n    <div class=\"dz-filename\"><span data-dz-name></span></div>\n    <div class=\"dz-size\" data-dz-size></div>\n    <img data-dz-thumbnail />\n  </div>\n  <div class=\"progress progress-small progress-striped active\"><div class=\"progress-bar progress-bar-success\" data-dz-uploadprogress></div></div>\n  <div class=\"dz-success-mark\"><span></span></div>\n  <div class=\"dz-error-mark\"><span></span></div>\n  <div class=\"dz-error-message\"><span data-dz-errormessage></span></div>\n</div>",
							
						init: function() {
				            this.on("success", function(file) {
				            	getImagesAndUpdate(); 
				            });
					    }
					  })
					} catch(e) {
					  alert('请使用更新的浏览器上传图片!');
				}
				
				$( "#dialog-modify-pwd" ).dialog({
				      autoOpen: false,
				      height: 270,
				      width: 350,
				      modal: true,
				      buttons: {
				        "确定": function() {
				        	if($("#oldpwd").val()==""){
								$("#tiplabel").html("请输入原密码");
								return;
							}
				        	
				        	if($("#pwd").val()==""){
								$("#tiplabel").html("请输入新密码");
								return;
							}
							
							if($("#confirm-pwd").val()==""){
								$("#tiplabel").html("请再次输入新密码");
								return;
							}
							
							if($("#pwd").val()!=$("#confirm-pwd").val()){
								$("#tiplabel").html("两次密码一不致，请重新输入");
								return;
							}
							
							$.ajax({
								type: "POST",
								url: "/DonkeyMgrSystem/user/pwd",  
								data: {"oldpwd":$('#oldpwd').val(), "pwd":$('#pwd').val()},  
								dataType: "json", 
								traditional: true,  
								success: function(result){ 		
									//var jsonResult = eval ("(" + result + ")");
									if(result.result == "success"){														
										alert("修改成功,请重新登录");	
										$( "#dialog-modify-pwd" ).dialog( "close" );
										window.location.href="/DonkeyMgrSystem/logout";
									}
									else if(result.result == "old_pwd_error"){
										alert("原密码错误，请重新输入");
									}
									else{
										alert("修改失败");
									}
								},
								error: function(result){
									alert("修改失败");
								}
							});
				        },
				        "取消": function() {
				          $( this ).dialog( "close" );
				        }
				      },
				    });
				 
				    $( "#modify-pwd" ).click(function(e) {
				    	e.preventDefault();
				        $( "#dialog-modify-pwd" ).dialog( "open" );
				     });
				    
					$( "#dialog-uploadfile" ).dialog({
					      autoOpen: false,
					      height: 600,
					      width: 800,
					      modal: true,
				    });		
					
				jQuery(grid_selector).jqGrid({
					//direction: "rtl",
					url:"/DonkeyMgrSystem/donkey/page",
					editurl:"/DonkeyMgrSystem/donkey/save",
					datatype: "json",
			        mtype: "POST",
					height: 550,
					//colNames:[' ', 'ID','Last Sales','Name', 'Stock', 'Ship via','Notes'],
					colModel:[
						{name:'编辑',index:'', width:80, fixed:true, sortable:false, resize:false,
							formatter:'actions', 
							formatoptions:{ 
								keys:true,
								
								delOptions:{recreateForm: true, beforeShowForm:beforeDeleteCallback},
								editformbutton:true, editOptions:{recreateForm: true, closeAfterEdit: true, width:500, dataheight:600, beforeShowForm:beforeEditCallback, afterShowForm:afterEditShowForm, beforeSubmit:beforeEditSubmit}
							}
						},
						{label:'序号', 		name:'id', 				index:'id', 			editable:false,		width:80, 		hidden:true,			search:false,		formatter:'integer', key:true},
						{label:'编号', 		name:'sn', 				index:'sn', 			editable:true, 		width:80, 		sorttype:'int',			formatter:'integer',editrules:{required:true},		searchoptions:{sopt:['cn']}},
						{label:'养殖户', 		name:'farmer', 			index:'farmer', 		editable:true, 		width:120, 		sortable:false,			search:false},
						{label:'养殖地址', 	name:'breedaddress', 	index:'breedaddress', 	editable:true, 		width:150, 		sortable:false, 		edittype:'textarea',		editoptions:{rows:"1",cols:"50"}, searchoptions:{sopt:['cn']}},
						{label:'供应商', 		name:'supplier', 		index:'supplier', 		editable:true, 		width:120, 		sortable:false,			search:false},
						{label:'供应地址', 	name:'supplyaddress', 	index:'supplyaddress', 	editable:true, 		width:150, 		sortable:false, 		edittype:'textarea',		editoptions:{rows:"1",cols:"50"}, searchoptions:{sopt:['cn']}},
						{label:'订单时间', 	name:'dealtime', 		index:'dealtime', 		editable:true,		width:120, 		sortable:false,			search:false,		unformat: pickDate},
						{label:'供应时间', 	name:'supplytime', 		index:'supplytime', 	editable:true,		width:120, 		sortable:false,			search:false,		unformat: pickDate},
						{label:'品种', 		name:'breed',		 	index:'breed', 			editable:true, 		width:120, 		sortable:false,			search:false,		edittype:"select",			editoptions:{value:'关中驴:关中驴;德州驴:德州驴;华北驴:华北驴;其他:其他'}},
						{label:'性别', 		name:'sex', 			index:'sex', 			editable:true, 		width:120, 		sortable:false,			search:false,		edittype:"select",			editoptions:{value:'雄性:雄性;雌性:雌性;阉割:阉割'}}, 
						//{label:'订单时年龄',	name:'agewhendeal', 	index:'agewhendeal', 	editable:true, 		width:120, 		sortable:false,			search:false},
						{label:'屠宰年龄',	name:'agewhenkill', 	index:'agewhenkill', 	editable:true, 		width:120, 		sortable:false,			search:false},
						//{label:'饲喂情况', 	name:'feedstatus', 		index:'feedstatus', 	editable:true, 		width:120, 		sortable:false,			search:false,		edittype:'textarea',		editoptions:{rows:"1",cols:"50"}},
						{label:'饲养方式', 	name:'feedpattern', 	index:'feedpattern', 	editable:true, 		width:120, 		sortable:false,			search:false,		edittype:'select', 		editoptions:{value:'圈养:圈养;放养:放养'}},
						{label:'饲草', 		name:'forage', 			index:'forage', 		editable:true, 		width:120, 		sortable:false,			search:false,		edittype:'textarea',		editoptions:{rows:"1",cols:"50"}},
						{label:'健康状况',	name:'healthstatus', 	index:'healthstatus', 	editable:true, 		width:120, 		sortable:false,			search:false,		edittype:'textarea',		editoptions:{rows:"1",cols:"50"}},
						{label:'繁育情况', 	name:'breedstatus', 	index:'breedstatus', 	editable:true, 		width:150, 		sortable:false,			search:false,		edittype:'textarea',		editoptions:{rows:"1",cols:"50"}},
						{label:'屠宰单位', 	name:'killdepartment', 	index:'killdepartment', editable:true, 		width:150, 		sortable:false,			search:false,		edittype:'textarea',		editoptions:{rows:"1",cols:"50"}},
						//{label:'屠宰地点', 	name:'killplace', 		index:'killplace', 		editable:true, 		width:150, 		sortable:false,			search:false,		edittype:'textarea',		editoptions:{rows:"1",cols:"50"}},
						{label:'屠宰时间', 	name:'killtime', 		index:'killtime', 		editable:true, 		width:150, 		sortable:false,			search:false,		unformat: pickDate},			
						//{label:'分割情况', 	name:'splitstatus', 	index:'splitstatus', 	editable:true, 		width:150, 		sortable:false,			search:false,		edittype:'textarea',		editoptions:{rows:"1",cols:"50"}},
						//{label:'加工情况', 	name:'processstatus', 	index:'processstatus', 	editable:true, 		width:150, 		sortable:false,			search:false,		edittype:'textarea',		editoptions:{rows:"1",cols:"50"}},
						{label:'保鲜方式', 	name:'freshkeepmethod', index:'freshkeepmethod',editable:true, 		width:120, 		sortable:false,			search:false,		edittype:'select', 		editoptions:{value:'鲜肉:鲜肉;冻肉:冻肉'}},
						{label:'保质期', 		name:'freshkeeptime', 	index:'freshkeeptime', 	editable:true, 		width:120, 		sortable:false,			search:false},
						{label:'质检情况', 	name:'qualitystatus', 	index:'qualitystatus', editable:true, 		width:120, 		sortable:false,			search:false,		edittype:"select",			editoptions:{value:'合格:合格;不合格:不合格'}},
						{label:'QC编号', 		name:'QC', 				index:'QC', 			editable:true, 		width:120, 		sortable:false,			search:false},
						{label:'QA编号', 		name:'QA', 				index:'QA', 			editable:true, 		width:120, 		sortable:false,			search:false},
						{label:'驴皮质量', 	name:'furquality', 		index:'furquality', 	editable:true, 		width:120, 		sortable:false,			search:false},
						//{label:'质检情况', 	name:'qualitystatyus', 	index:'qualitystatyus', editable:true, 		width:150, 		sortable:false,			search:false},
						//{label:'出厂时间', 	name:'factorytime', 	index:'factorytime', 	editable:true, 		width:150, 		sortable:false,			search:false,		unformat: pickDate},
						{label:'版本号', 		name:'version', 		index:'version', 		editable:true, 		width:150, 		sortable:false,			search:false,		hidden:true},
						{label:'照片', 		name:'images', 			index:'images', 		editable:true, 		width:150, 		sortable:false,			search:false,		formatter:imagesFormat, 	unformat:imagesUnFormat,		hidden:true},
					], 
			
					viewrecords : true,
					rowNum:100,
					rowList:[100,200,300],
					pager : pager_selector,
					altRows: true,
					//toppager: true,
					
					multiselect: true,
					//multikey: "ctrlKey",
			        multiboxonly: true,
			
					loadComplete : function() {
						var table = this;
						setTimeout(function(){
							styleCheckbox(table);
							
							updateActionIcons(table);
							updatePagerIcons(table);
							enableTooltips(table);
						}, 0);
					},
						
					caption: "毛驴列表",					
					autowidth: true,
					
					jsonReader : {
					      root:"rows",
					      page: "page",
					      total: "total",
					      records: "records",
					      id: "0"
					},
					onSelectRow: function(id){  
					      if(id){  					     
					         selectID=id;  
					      }    
					   }
				});
			
				//enable search/filter toolbar
				//jQuery(grid_selector).jqGrid('filterToolbar',{defaultSearch:true,stringResult:true})
			
				//switch element when editing inline
				function aceSwitch( cellvalue, options, cell ) {
					setTimeout(function(){
						$(cell) .find('input[type=checkbox]')
								.wrap('<label class="inline" />')
							.addClass('ace ace-switch ace-switch-5')
							.after('<span class="lbl"></span>');
					}, 0);
				}
				//enable datepicker
				function pickDate( cellvalue, options, cell ) {
					setTimeout(function(){
						$(cell) .find('input[type=text]')
								.datepicker({format:'yyyy-mm-dd' , autoclose:true}); 
					}, 0);
				}
			
			
				//navButtons
				jQuery(grid_selector).jqGrid('navGrid',pager_selector,
					{ 	//navbar options
						edit: false,
						editicon : 'icon-pencil blue',
						add: true,
						addicon : 'icon-plus-sign purple',
						del: true,
						delicon : 'icon-trash red',
						search: true,
						searchicon : 'icon-search orange',
						refresh: true,
						refreshicon : 'icon-refresh green',
						view: true,
						viewicon : 'icon-zoom-in grey',
					},
					{
						//edit record form
						width:500,
						dataheight:600,
						closeAfterEdit: true,
						recreateForm: true,
						beforeShowForm : function(e) {
							var form = $(e[0]);
							$('#tr_images', e).show();
							form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
							style_edit_form(form);
							console.log('hahaah');
						}
					},
					{
						//new record form
						width:500,
						dataheight:600,
						//closeAfterAdd: true,
						recreateForm: true,
						viewPagerButtons: false,
						beforeShowForm : function(e) {
							var form = $(e[0]);
							//addImageBtn();
							//$('#tr_images', e).show();
							form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
							style_edit_form(form);
						},
						afterShowForm : function (formid){
							validateForm(0);
						},
						beforeSubmit:function(postdata, formid) { 
							if( validateSn() ){
								postdata.version = 1;
								return[true,''];
							}else{
								return[false,'请重新输入编号'];
							}
						}
					},
					{
						//delete record form						
						recreateForm: true,
						beforeShowForm : function(e) {
							var form = $(e[0]);
							if(form.data('styled')) return false;
							
							form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
							style_delete_form(form);
							
							form.data('styled', true);
						},
						onClick : function(e) {
							alert(1);
						}
					},
					{
						//search form
						recreateForm: true,
						closeAfterSearch: true,
						afterShowSearch: function(e){
							var form = $(e[0]);
							form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
							style_search_form(form);
						},
						afterRedraw: function(){
							style_search_filters($(this));
						}
						,
						multipleSearch: false,
						/**
						multipleGroup:true,
						showQuery: true
						*/
					},
					{
						//view record form
						width:500,
						recreateForm: true,
						beforeShowForm: function(e){
							var form = $(e[0]);
							form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
						}
					}
				)
			
				function imagesFormat( cellvalue, options, rowObject ){
					return "";
				}
				
				function imagesUnFormat( cellvalue, options, cell){
					return $('img', cell).attr('src');
				}
				
				function style_edit_form(form) {
					//enable datepicker on "sdate" field and switches for "stock" field
					form.find('input[name=dealtime]').datepicker({format:'yyyy-mm-dd' , autoclose:true})
					.end().find('input[name=supplytime]').datepicker({format:'yyyy-mm-dd' , autoclose:true})
						.end().find('input[name=killtime]').datepicker({format:'yyyy-mm-dd' , autoclose:true})
						.end().find('input[name=factorytime]').datepicker({format:'yyyy-mm-dd' , autoclose:true})
						.end().find('input[name=stock]')
							  .addClass('ace ace-switch ace-switch-5').wrap('<label class="inline" />').after('<span class="lbl"></span>');
			
					//update buttons classes
					var buttons = form.next().find('.EditButton .fm-button');
					buttons.addClass('btn btn-sm').find('[class*="-icon"]').remove();//ui-icon, s-icon
					buttons.eq(0).addClass('btn-primary').prepend('<i class="icon-ok"></i>');
					buttons.eq(1).prepend('<i class="icon-remove"></i>')
					
					buttons = form.next().find('.navButton a');
					buttons.find('.ui-icon').remove();
					buttons.eq(0).append('<i class="icon-chevron-left"></i>');
					buttons.eq(1).append('<i class="icon-chevron-right"></i>');		
				}
			
				function style_delete_form(form) {
					var buttons = form.next().find('.EditButton .fm-button');
					buttons.addClass('btn btn-sm').find('[class*="-icon"]').remove();//ui-icon, s-icon
					buttons.eq(0).addClass('btn-danger').prepend('<i class="icon-trash"></i>');
					buttons.eq(1).prepend('<i class="icon-remove"></i>')
				}
				
				function style_search_filters(form) {
					form.find('.delete-rule').val('X');
					form.find('.add-rule').addClass('btn btn-xs btn-primary');
					form.find('.add-group').addClass('btn btn-xs btn-success');
					form.find('.delete-group').addClass('btn btn-xs btn-danger');
				}
				function style_search_form(form) {
					var dialog = form.closest('.ui-jqdialog');
					var buttons = dialog.find('.EditTable')
					buttons.find('.EditButton a[id*="_reset"]').addClass('btn btn-sm btn-info').find('.ui-icon').attr('class', 'icon-retweet');
					buttons.find('.EditButton a[id*="_query"]').addClass('btn btn-sm btn-inverse').find('.ui-icon').attr('class', 'icon-comment-alt');
					buttons.find('.EditButton a[id*="_search"]').addClass('btn btn-sm btn-purple').find('.ui-icon').attr('class', 'icon-search');
				}
				
				function beforeDeleteCallback(e) {
					var form = $(e[0]);
					if(form.data('styled')) return false;
					
					form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
					style_delete_form(form);
					
					form.data('styled', true);
				}
				
				function setUploadImgClickHandler(){
					$("#uploadimg").click(function(e) {
						e.preventDefault();
						$("#donkeyid").val(selectID);
				        $( "#dialog-uploadfile" ).dialog( "open" );
					});
				}
				
				function addImageBtn(){
					var htmlStr = '<div class="page-content">   \
						<div class="row">			\
							<div class="col-xs-12">		\
								<div class="row-fluid">		\
									<ul class="ace-thumbnails" id="imglist">';
						htmlStr += '</ul></div></div><br>&nbsp;<button type="button" id="uploadimg">添加照片</button></div></div>';							
						$('#tr_images').children("td.DataTD").html(htmlStr);
						
						setUploadImgClickHandler();
				}
				
				function validateForm(curId){
					$("#FrmGrid_grid-table").validate({
				        rules: {
				            sn: {	
				                remote: {
				                    type: "post",
				                    url: "/DonkeyMgrSystem/donkey/snvalidate",
				                    data: {
				                    	sn: function() {
				                    		   return $("#sn").val();
				                    	},
				                    	id:curId,
				                    }
				                }
				            }
				        },
				        messages: {
				        	sn: {
				                remote: "该编号对应的记录已存在，请重新输入编号"
				            }
				        }

				    });
				}
				
				function afterEditShowForm(e) {
					validateForm(selectID);
				}
				
				function beforeEditSubmit(postdata, formid) { 
					if( validateSn() ){
						postdata.version = Number(postdata.version)+1;
						return[true,''];
					}else{
						return[false,'请重新输入编号'];
					}
				}
				
				function getImagesAndUpdate(){
					$.ajax({
						type: "post", 
						url: "/DonkeyMgrSystem/donkey/images",
						data:{id:selectID},
						success: function(data){ 
							//var jsonObj = eval ("(" + data + ")");
							var jsonObj = data;
							var htmlStr = '';
							for(var i = 0; i < jsonObj.count; i++){
								htmlStr += '<li><a href="';
								htmlStr += jsonObj.images[i].url.replace("thumb", "image");
								htmlStr += '" data-rel="colorbox"><img alt="150x150" src="';
								htmlStr += jsonObj.images[i].url;
								htmlStr += '" /></a><div class="tools tools-bottom"><a class="delimage" img="';
								htmlStr += jsonObj.images[i].url;
								htmlStr += '"><i class="icon-remove red"></i></a></div></li>';
							}
							$('#imglist').html(htmlStr);
							
							$(".delimage").click(function(e) {
						    	e.preventDefault();
						    	var image = $(this).attr("img");
						    	var imageelement = $(this);
						    								    	
						    	$.post("/DonkeyMgrSystem/donkey/images/delimage", {img: image},
					    			   function(data){
					    					var result = eval ("(" + data + ")");
					    					if(result.result = 'success'){
					    						imageelement.parent().parent().remove();
					    					}else{
					    						alert("删除失败");
					    					}
					    					
					    			   });
							});
						}
					});
				}
				
				function beforeEditCallback(e) {
					var form = $(e[0]);
					addImageBtn();
					Dropzone.forElement(".dropzone").removeAllFiles(true);
						
					getImagesAndUpdate();
					$('#tr_images', e).show();
					form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
					style_edit_form(form);
				}
			
			
			
				//it causes some flicker when reloading or navigating grid
				//it may be possible to have some custom formatter to do this as the grid is being created to prevent this
				//or go back to default browser checkbox styles for the grid
				function styleCheckbox(table) {
				/**
					$(table).find('input:checkbox').addClass('ace')
					.wrap('<label />')
					.after('<span class="lbl align-top" />')
			
			
					$('.ui-jqgrid-labels th[id*="_cb"]:first-child')
					.find('input.cbox[type=checkbox]').addClass('ace')
					.wrap('<label />').after('<span class="lbl align-top" />');
				*/
				}
				
			
				//unlike navButtons icons, action icons in rows seem to be hard-coded
				//you can change them like this in here if you want
				function updateActionIcons(table) {
					/**
					var replacement = 
					{
						'ui-icon-pencil' : 'icon-pencil blue',
						'ui-icon-trash' : 'icon-trash red',
						'ui-icon-disk' : 'icon-ok green',
						'ui-icon-cancel' : 'icon-remove red'
					};
					$(table).find('.ui-pg-div span.ui-icon').each(function(){
						var icon = $(this);
						var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
						if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
					})
					*/
				}
				
				//replace icons with FontAwesome icons like above
				function updatePagerIcons(table) {
					var replacement = 
					{
						'ui-icon-seek-first' : 'icon-double-angle-left bigger-140',
						'ui-icon-seek-prev' : 'icon-angle-left bigger-140',
						'ui-icon-seek-next' : 'icon-angle-right bigger-140',
						'ui-icon-seek-end' : 'icon-double-angle-right bigger-140'
					};
					$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
						var icon = $(this);
						var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
						
						if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
					})
				}
			
				function enableTooltips(table) {
					$('.navtable .ui-pg-button').tooltip({container:'body'});
					$(table).find('.ui-pg-div').tooltip({container:'body'});
				}
				
				function validateSn(){
					var validateResult = $("#FrmGrid_grid-table").valid();
					if(validateResult == true)
						return[true,''];
					else
						return[false,'请重新输入编号'];
				}
			
				//var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');
			});