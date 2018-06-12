<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

    String thisUrl = request.getServletPath();
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <base href="<%=basePath%>">

    <title><decorator:title default="sailfish"/></title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="<%=path%>/plugins/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%=path%>/css/style.css">
    <link rel="stylesheet" href="<%=path%>/css/override-bootstrap.css">
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
        <script src="<%=path%>/plugins/html5shiv.js"></script>
        <script src="<%=path%>/plugins/respond.min.js"></script>
    <![endif]-->


    <script src="<%=path%>/plugins/jquery-1.9.1.js" type="text/javascript"></script>

    <link rel="stylesheet" href="<%=path%>/plugins/bootstrap/css/bootstrap-theme.min.css">
    <script src="<%=path%>/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="<%=path%>/plugins/jquery-form/jquery.form.js" type="text/javascript"></script>

    <script src="<%=path%>/plugins/func/utils.js" type="text/javascript"></script>
    <script src="<%=path%>/plugins/func/dialog.js" type="text/javascript"></script>
    <script src="<%=path%>/plugins/func/request.js" type="text/javascript"></script>
    <script src="<%=path%>/plugins/func/validate.js" type="text/javascript"></script>
    <script src="<%=path%>/plugins/func/alert.js" type="text/javascript"></script>
    <script src="<%=path%>/plugins/func/form.js" type="text/javascript"></script>


    <decorator:head />

    <script src="<%=path%>/js/am.js"  type="text/javascript"></script>
</head>

<body>
<div id="container" class="container">

    <c:if test="${not empty sessionScope.sessionUser}">
        <div class="row" style="margin-top: 30px;">
            <div class="col-md-12">
                <nav class="navbar navbar-default">
                    <div class="navbar-header">
                        <a class="navbar-brand" href="#">Sailfish-旗鱼</a>
                    </div>
                    <div class="collapse navbar-collapse">
                        <ul class="nav navbar-nav" id="moduleNav">
                            <li><a href="/am/index">首页</a></li>
                            <li><a href="/am/category/index">栏目</a></li>
                            <li><a href="/am/rs/index">资源与模板</a></li>
                            <li><a href="/am/ad/index">广告</a></li>
                            <li><a href="/am/article/index">文章</a></li>
                            <li><a href="/am/user/index">用户</a></li>
                        </ul>

                        <ul class="nav navbar-nav navbar-right">
                            <li><a href="#">欢迎您回来，</a></li>
                            <li class="active">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    ${sessionScope.sessionUser.userName} <b class="caret"></b>
                                    <ul class="dropdown-menu">
                                        <li><a href="/am/logout">注销</a></li>
                                    </ul>
                                </a>
                            </li>
                        </ul>
                    </div>
                </nav>
            </div>
        </div>
    </c:if>

    <decorator:body />
</div>

<footer class="footer">
    <p>Sailfish - <a href="http://zh.wikipedia.org/wiki/%E6%97%97%E9%AD%9A">旗鱼</a>
        本网站使用<a href="https://github.com/twbs/bootstrap/">Bootstrap</a>构建</p>
    <p>Powered By C.y_Chris</p>
</footer>

</body>
</html>