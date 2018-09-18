<!DOCTYPE html>
<html lang="en" >

<head>
  <meta charset="UTF-8">
  <title>Login Form</title>
  
    <link rel="stylesheet" href="${request.contextPath}/public/css/normalize.min.css">
    <link rel="stylesheet" href="${request.contextPath}/public/css/login.css">

    <#--<script src="${request.contextPath}/js/prefixfree.min.js"></script>-->

</head>

<body>

  <div class="login">
	<h1>Login</h1>

    <form action="" method="post">
        <div class="error">
		  <#if RequestParameters['kickout']??>
              您已被踢出登陆
		  <#else >
			  ${error}
		  </#if>
        </div>
    	<input type="text" class="inputStyle" name="username" placeholder="账   号"  />
        <input type="password" class="inputStyle" name="password" placeholder="密   码" />
		<#if Request["jcaptchaEbabled"]>
			<input type="text" class="inputStyle1" autocomplete="off" name="jcaptchaCode" placeholder="验证码" >
			<img class="jcaptcha-btn jcaptcha-img"  title="点击更换验证码">
			<#--<a class="jcaptcha-btn" href="javascript:;">换一张</a>-->
		</#if>
        <input type="submit" class="btn btn-primary btn-block btn-large" value="登陆">
        <a href="${request.contextPath}/register" class="register">注册账号</a>
        <div class=" rememberMeStyle" >

            <input id="rememberMeId" type="checkbox" class="checkStyle" name="rememberMe" placeholder="RememberMe" />
			<label for="rememberMeId">记住密码</label>
        </div>

    </form>
</div>
  <script src="${request.contextPath}/public/js/jquery-2.1.1.min.js"></script>
  <script>
      $(function() {
          $(".jcaptcha-btn").click(function() {
              $(".jcaptcha-img").attr("src", '${request.contextPath}/static/img/jcaptcha.jpg?'+new Date().getTime());
          });
      });
  </script>


</body>

</html>
