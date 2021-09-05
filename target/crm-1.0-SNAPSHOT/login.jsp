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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(function (){
    	if (window.top!=window){
			window.top=window;
		}

        //页面加载完毕，将用户文本框的内容清空
        $("#loginAct").val("");

        //页面加载完毕后，让用户的文本框自动获得焦点
        $("#loginAct").focus();

        //为登录按钮绑定时间，执行登录操作
        $("#submitBtn").click(function (){
            login();
            //alert("进行登录操作");
        })

        //为当前登录窗口绑定键盘事件
        //event：这个参数可以取得我们敲的是哪一个键
        $(window).keydown(function (event){
            //alert(event.keyCode);
            if ("13"==event.keyCode){
                login();
                //alert("进行登录操作");
            }
        })
    })

    //登录方法
    function login(){
        //alert("登录操作");

        //验证账户密码不能为空
        var loginAct = $("#loginAct").val().trim();
        var loginPwd = $("#loginPwd").val().trim();
        //当账户和密码框获得焦点的时候将提示信息清空
        $("#loginAct,#loginPwd").focus(function (){
            $("#msg").html("");
        })


        if (loginAct=="" || loginPwd==""){
            $("#msg").html("账号密码不能为空");
        }else {
            //去后台验证登录相关操作，这里使用ajax局部刷新

            $.ajax({
                url : "settings/user/login.do",           //请求地址
                data : {            //请求参数
                    "loginAct" : loginAct,
                    "loginPwd" : loginPwd

                },
                type : "post",      //请求方式
                dataType : "json",      //放回数据格式
                success : function (data){      //数据的操作

                    /*
                    *
                    *      成功返回 data
                    *      {"success" : true}
                    *
                    *      失败返回 data
                    *       {"success" : false , "msg" : "错误信息"}
                    *
                    * */

                    //如果登录成功,跳转页面
                    if (data.success){
                        window.location.href = "workbench/index.jsp";
                    }else {
                    //登录失败，展现失败信息
                        $("#msg").html(data.msg);

                    }

                }

            })

        }

    }
</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM </div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>