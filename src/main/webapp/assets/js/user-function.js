jQuery(function($) {
				var grid_selector = "#grid-table";
				var pager_selector = "#grid-pager";
				var lastSel;
			
				jQuery(grid_selector).jqGrid({
					//direction: "rtl",
					url:"/DonkeyMgrSystem/user/page",
					editurl:"/DonkeyMgrSystem/user/save",
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
								editformbutton:true, editOptions:{recreateForm: true, closeAfterEdit: true, beforeShowForm:beforeEditCallback}
							}
						},
						{label:'序号', 	name:'id', 			index:'id', 		editable:false,		width:80, 		sorttype:'int',			search:false,		formatter:'integer', key:true},
						{label:'账号', 	name:'name', 		index:'name', 		editable:true,		width:120, 		sortable:true,			searchoptions:{sopt:['cn']},	editrules:{required:true}},
						{label:'密码', 	name:'pwd', 		index:'pwd', 		editable:true, 		width:150, 		sortable:false, 		search:false,		editoptions:{size:'20', maxlength:'30'},	editrules:{required:true}},
						{label:'用户名', 	name:'username', 	index:'username', 	editable:true,		width:120, 		sortable:true,			searchoptions:{sopt:['cn']}},
						{label:'电话', 	name:'phone',		index:'phone', 		editable:true, 		width:120, 		sortable:false, 		search:false},					
					], 
			
					viewrecords : true,
					rowNum:10,
					rowList:[10, 30, 50],
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
			
					caption: "用户列表",					
					autowidth: true,
					
					jsonReader : {
					      root:"rows",
					      page: "page",
					      total: "total",
					      records: "records",
					      id: "0"
					},
					
/*					onSelectRow: function(id) {
						if (id && id !== lastSel) {
							jQuery(grid_selector).jqGrid('restoreRow',lastSel);
							var cm = jQuery(grid_selector).jqGrid('getColProp','name');
							cm.editable = false;
							jQuery(grid_selector).jqGrid('editRow', id, true, null, null, "/user/save");
							cm.editable = true;
							lastSel = id;
						}
					}*/
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
						edit: true,
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
						closeAfterEdit: true,
						recreateForm: true,
						beforeShowForm : function(e) {
							var form = $(e[0]);
							$('#tr_name', e).hide();
							form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
							style_edit_form(form);
						}
					},
					{
						//new record form
						//closeAfterAdd: true,
						recreateForm: true,
						viewPagerButtons: false,
						beforeShowForm : function(e) {
							var form = $(e[0]);
							 $('#tr_name', e).show();
							//jQuery(grid_selector).setColProp('name', {editable:true});
							form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
							style_edit_form(form);
						},
						afterShowForm : function (formid){
							validateForm();
						},
						beforeSubmit:function(postdata, formid) { 
							return validateUserID();
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
						closeAfterSearch: true,
						recreateForm: true,
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
						recreateForm: true,
						beforeShowForm: function(e){
							var form = $(e[0]);
							form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
						}
					}
				)
				
				function validateForm(){
					$("#FrmGrid_grid-table").validate({
				        rules: {
				            name: {	
				                remote: {
				                    type: "post",
				                    url: "/DonkeyMgrSystem/user/uservalidate",
				                    data: {
				                    	name: function() {
				                    		   return $("#name").val();
				                    	}
				                    }
				                }
				            }
				        },
				        messages: {
				        	name: {
				                remote: "该账号已存在，请重新输入账号名"
				            }
				        }

				    });
				}
			
				function validateUserID(){
					var validateResult = $("#FrmGrid_grid-table").valid();
					if(validateResult == true)
						return[true,''];
					else
						return[false,'请重新输入账号名'];
				}
				
				function style_edit_form(form) {
					//enable datepicker on "sdate" field and switches for "stock" field
					form.find('input[name=dealtime]').datepicker({format:'yyyy-mm-dd' , autoclose:true})
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
				
				function beforeEditCallback(e) {
					var form = $(e[0]);
					$('#tr_name', e).hide();
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
			
				//var selr = jQuery(grid_selector).jqGrid('getGridParam','selrow');
			
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
			});