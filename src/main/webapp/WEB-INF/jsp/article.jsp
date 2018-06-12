<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <title>管理后台 - 文章</title>

    <link rel="stylesheet" href="<%=path%>/plugins/jquery-ui/css/flick/jquery-ui-1.10.3.custom.min.css">
    <script src="<%=path%>/plugins/jquery-ui/js/jquery.ui.datepicker-zh-CN.js" type="text/javascript"></script>
    <script src="<%=path%>/plugins/jquery-ui/js/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script>

    <script type="text/javascript">
        $(function() {
            $("#page").page({
                formId : "queryForm",
                pageSize : parseInt('${page.pageSize}'),
                totalSize : parseInt('${page.total}'),
                pageIndex : parseInt('${page.pageIndex}')
            });

            $.datepicker.setDefaults( $.datepicker.regional[ "zh-CN" ] );
            $(".datePicker").datepicker({
                showOtherMonths: true,
                selectOtherMonths: true,
                dateFormat : "yy-mm-dd"
            });
        });

        function deleteArticle(id) {
            $.confirm("是否确认删除该文章？", function() {

                $.request.ajax({
                    url : "/am/article/delete",
                    data : {ids : id},
                    onSuccess : function(msg, data) {
                        $.alert.Success(msg, function(){
                            $.utils.refreshPage();
                        });
                    }
                });
            });
        }

    </script>
</head>

<body>

<ol class="breadcrumb">
    <li>当前位置：后台管理</li>
    <li class="active">文章</li>
</ol>

<div class="row">
    <div class="col-md-12">
        <form class="form-inline" role="form" id="queryForm" action="/am/article/index">
            <div class="well">
                <p>
                    <div class="form-group">
                        <label>文章标题：</label>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control" name="title">
                    </div>

                    <div class="form-group pl100">
                        <label>文章状态：</label>
                    </div>
                    <div class="form-group">
                        <select class="form-control" name="status">
                            <option value="">---全部---</option>
                            <option value="0">未发布</option>
                            <option value="1">已发布</option>
                            <option value="2">已删除</option>
                        </select>
                    </div>
                    <div class="form-group pl100">
                        <label>修改时间：</label>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control w100 datePicker" name="startTime" readonly>
                    </div>
                    <div class="form-group">
                        <input type="text" class="form-control w100 datePicker" name="endTime" readonly>
                    </div>
                </p>
                <p>
                    <div class="form-group">
                        <label>所属栏目：</label>
                    </div>
                    <div class="form-group pr100">
                        <select class="form-control" name="category">
                            <option value="">--------请选择--------</option>
                            <c:forEach items="${categories}" var="ct">
                                <option value="${ct.enName}">${ct.cnName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-default mr50">查询</button>
                    <a class="btn btn-warning" href="/am/article/index/one">写新文章</a>
                </p>
            </div>
        </form>
    </div>
</div>


<div class="row">
    <div class="col-md-12">
        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
            <tr>
                <th width="20%">文章标题</th>
                <th width="10%">所属栏目</th>
                <th width="10%">状态</th>
                <th width="10%">创建人</th>
                <th width="15%">创建时间</th>
                <th width="15%">修改时间</th>
                <th width="5%">星级</th>
                <th width="15%" style="text-align: center;">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="article" items="${page.resultSet}">
                <tr>
                    <td>${article.title}</td>
                    <td>${article.category}</td>
                    <td>${article.articleStatus}</td>
                    <td>${article.creater}</td>
                    <td><fmt:formatDate value="${article.createTime}" pattern="yyyy/MM/dd HH:mm" /></td>
                    <td><fmt:formatDate value="${article.modifyTime}" pattern="yyyy/MM/dd HH:mm" /></td>
                    <td class="center"><span class="badge">${article.starLevel}</span></td>
                    <td style="text-align: center;">
                        <a href="/am/article/index/${article.id}" ><span class="glyphicon glyphicon-edit mr30"></span></a>
                        <a href="#" onclick="deleteArticle('${article.id}')"><span class="glyphicon glyphicon-trash"></span></a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <div id="page"></div>
    </div>
</div>

<c:import url="common/conditionWriteBack.jsp">
    <c:param name="conditionWriteBackFormId">queryForm</c:param>
</c:import>

</body>
</html>