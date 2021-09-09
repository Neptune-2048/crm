<%@ page import="com.zijing.crm.settings.domin.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>


	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		//为创建按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function (){
			//时间插件
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});
			//初始化模态窗口
			$("#create-Owner").val("");//所有者
			$("#create-name").val("");//市场活动名称
			$("#create-startDate").val("");//开始日期
			$("#create-endDate").val("");//结束日期
			$("#create-cost").val("");//成本
			 $("#create-description").val("");//描述

			/*
			* 	操作模态窗口的方式：
			* 			需要操作的模态窗口的jquery对象，调用modal方法，为方法传参数 show：打开窗口 hide：关闭窗口
			* */
			//向后台访问用户所有用户名，接收并显示到前端页面上
			$.ajax({
				url : "workbench/activity/getUserList.do",	//请求路径
				type : "get", //请求方式
				dataType : "json",	//返回值形式
				success : function (data){
					var html = "";
					$.each(data,function (i,n){
						html+="<option value='"+n.id+"'>"+n.name+"</option>";
					})
					$("#create-Owner").html(html);
					//设置默认的下拉框，从会话作用域获取当前用户的信息，并通过信息来设置默认下拉框
					var id = "${sessionScope.user.id}";
					$("#create-Owner").val(id);//设置下拉框的默认值

				}

			})
			//将模态窗口打开
			$("#createActivityModal").modal("show");
		})

		//为保存按钮绑定事件，来执行添加操作
		$("#saveBtn").click(function (){
			$.ajax({
				url: "workbench/activity/save.do",
				data: {
					//所有者是动态生成的，这里生成的标签其val()其实是用户的id，而不是下拉框显示的文本值(*****)
					"create-Owner" : $("#create-Owner").val().trim(),//所有者
					"create-name" : $("#create-name").val().trim(),//市场活动名称
					"create-startDate" : $("#create-startDate").val().trim(),//开始日期
					"create-endDate" : $("#create-endDate").val().trim(),//结束日期
					"create-cost" : $("#create-cost").val().trim(),//成本
					"create-description" : $("#create-description").val().trim(),//描述
				},
				type: "post",
				dataType: "json",
				success: function (data) {
					if (data.success){
						alert("保存成功");
						//关闭模态窗口
						// pageList(1,2);
						/*
						* pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
						*	,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						* 第一个参数代表刷新页面是保持在原来的页面（比如第二页，就保持在第二页）
						* 第二个参数代表刷新页面的记录条数的设计（比如我设计一页有5条记录，刷新之后还是保持页5条记录）
						* */
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						$("#createActivityModal").modal("hide");
					}else {
						alert("保存失败");
					}

				}

			})
		})

		//页面加载完成后触发一个方法
		//默认展开列表的第一页，每页展现两条记录
		pageList(1,2);

		//为查询按钮绑定事件，触发pageList放法
		$("#searchBtn").click(function (){
			$("#hidden-name").val($("#search-name").val().trim());
			$("#hidden-owner").val($("#search-Owner").val().trim());
			$("#hidden-startDate").val($("#search-startDate").val().trim());
			$("#hidden-endDate").val($("#search-endDate").val().trim());

			//pageList(1,2);
			pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
		})

		//为全选的复选框绑定事件，触发全选操作
		$("#qx").click(function (){
			$("input[name=xz]").prop("checked",this.checked);
		})
		//动态生成的元素，我们要以on方法的形式来触发事件
		$("#activityBody").on("click",$("input[name=xz]"),function (){
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})

		//为删除按钮绑定事件
		$("#deleteBtn").click(function (){
			//找到所有复选框里面打勾的的jquery对象
			var $xz = $("input[name=xz]:checked");
			if ($xz.length==0){
				alert("请选择需要删除的记录")
			}else {
			    if(confirm("您确定删除所选的记录吗？")){
                    var param ="";
                    for (var i=0;i < $xz.length;i++){
                        param += "id="+$($xz[i]).val();
                        if (i!=$xz.length-1){
                            param +="&";
                        }
                    }
                    $.ajax({
                        url : "workbench/activity/delete.do",
                        data : param,
                        type : "post",
                        dataType : "json",
                        success : function (data){
                            /*
                            * {"success":true/false}
                            * */
                            if (data.success){
                                //pageList(1,2);
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                                alert("删除成功")
                            }else {
                                alert("删除失败")
                            }

                        }

                    })

                }


			}

		})

		//为修改按钮绑定事件
		$("#editBtn").click(function (){
			//时间插件
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			//根据复选框向后端查询所有者以及市场活动的信息
			var $xz = $("input[name=xz]:checked");
			if ($xz.length==0){
				alert("请选择一条记录进行修改");
			}else if ($xz.length > 1){
				alert("只能选择一条记录进行修改");
			}else {
				$.ajax({
					url : "workbench/activity/getUserListAndActivity.do",
					data : {
						"id" : $($xz[0]).val().trim(),
					},
					type : "post",
					dataType : "json",
					success : function (data){
						// alert(data);
						var html = ""
						var owner;
						$.each(data.list,function (i,n){
							html +="<option value='"+n.id+"'>"+n.name+"</option>"
						})
						$("#edit-Owner").html(html);
						//alert(owner);
						$("#edit-Owner").val(data.activity.owner);
						$("#edit-ActivityName").val(data.activity.name);
						$("#edit-startDate").val(data.activity.startDate);
						$("#edit-endDate").val(data.activity.endDate);
						$("#edit-cost").val(data.activity.cost);
						$("#edit-description").val(data.activity.description);

					}

				})
				$("#editActivityModal").modal("show");
			}

		})

		//为更新按钮绑定事件
		$("#updateBtn").click(function (){
			var $xz = $("input[name=xz]:checked");
			$.ajax({
				url : "workbench/activity/update.do",
				type : "post",
				data : {
					"id" : $($xz[0]).val(),
					"owner" : $("#edit-Owner").val(),
					"name" : $("#edit-ActivityName").val(),
					"startDate" : $("#edit-startDate").val(),
					"endDate" : $("#edit-endDate").val(),
					"cost" : $("#edit-cost").val(),
					"description" : $("#edit-description").val()
				},
				dataType : "json",
				success : function (data){
						if (data.success){
							alert("更新成功");
							//刷新信息
							//pageList(1,2);
							pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
									,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							//关闭模态窗口
							$("#editActivityModal").modal("hide");
						}else {
							alert("更新失败")
						}
				}

			})
		})
	});
	/*
	* 对于所有的关系型数据库，做前端的分页相关的操作的基础组件
	* 就是pageNo：页码
	* pagesize：每页展现的记录数
	*
	* pageList方法：就是发出ajax请求到后台，从后台取得最新的市场活动
	* 		通过后台响应回来的数据，局部刷新市场活动信息列表
	*
	* 什么情况需要调用pageList方法：
	* （1）点击左侧的市场活动的时候
	* （2）添加、修改、删除需要刷新市场活动
	* （3）点击查询按钮的时候
	* （4）点击分页组件的时候
	* */

	function pageList(pageNo,pagesize){
		$("#qx").prop("checked",false);


		$("#search-name").val($("#hidden-name").val().trim());
		$("#search-Owner").val($("#hidden-owner").val().trim());
		$("#search-startDate").val($("#hidden-startDate").val().trim());
		$("#search-endDate").val($("#hidden-endDate").val().trim());


		$.ajax({
			url :"workbench/activity/pageList.do",
			data : {
				"pageNo" : pageNo,
				"pagesize" : pagesize,
				"name" : $("#search-name").val().trim(),
				"Owner" : $("#search-Owner").val().trim(),
				"startDate" : $("#search-startDate").val().trim(),
				"endDate" : $("#search-endDate").val().trim(),

			},
			type : "get",
			dataType : "json",
			success : function (data){
				/*
				* data
				* 	我们需要的，市场活动信息列表
				* 	[{市场活动1}，{市场活动2},{市场活动3}........]
				* 	{"total" : 100}
				*
				* 	我们需要从后台获取的数据
				*	{"total":100,"datalist":"[{市场活动1}，{市场活动2},{市场活动3}........]"}
				*
				* */
				var html ="";
				$.each(data.dataList,function(i,n){

					html +='<tr class="active">';
					html +='<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html +='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>'
					html +='<td>'+n.owner+'</td>';
					html +='<td>'+n.startDate+'</td>';
					html +='<td>'+n.endDate+'</td>';
					html +='</tr>'
				})
				$("#activityBody").html(html);

				//计算总页数
				var totalPages = data.total%pagesize == 0 ? data.total/pagesize : parseInt(data.total/pagesize)+1;

				//数据处理完，结合分页插件，向前端展现
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pagesize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 5, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});

			}

		})
	}
	
</script>
</head>
<body>
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-Owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<!--从后端获取所有用户信息，动态生成所有者信息，将这些信息添加在下拉框中-->
								<select class="form-control" id="create-Owner">

								</select>
							</div>
                            <label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label ">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endDate" class="col-sm-2 control-label ">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-Owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-Owner">

								</select>
							</div>
                            <label for="edit-ActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-ActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" >
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate"  >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost"  >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-Owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<!--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>-->
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>

		</div>

	</div>
</body>
</html>