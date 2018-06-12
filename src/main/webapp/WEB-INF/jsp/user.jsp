<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>

    <title>用户管理</title>
    <script type="text/javascript">
        var validator;

        $(function() {
            $("#page").page({
                formId : "queryForm",
                pageSize : parseInt('${page.pageSize}'),
                totalSize : parseInt('${page.total}'),
                pageIndex : parseInt('${page.pageIndex}')
            });

            $("#userForm").formDialog({
                title : "编辑用户信息",
                width : 530,
                buttons : [{
                    text : "保存",
                    cls : "btn-primary",
                    click : function() {
                        if(!validator.validated()) {
                            return ;
                        }

                        $("#userForm").formDialog("close");
                        $("#userForm").submitForm({
                            progress : true,
                            onSuccess : function(msg, data) {
                                $.alert.Success(msg, function(){
                                    $.utils.refreshPage();
                                });
                            },
                            onFailure : function(msg, data) {
                                $.alert.Error(msg, function() {
                                    $("#userForm").formDialog();
                                });
                            }
                        })
                    }
                }, {
                    text : "关闭",
                    closed : true
                }]
            });
            validator = $("#userForm").validate({
                container : "#_formDialog_userForm"
            });

        });

        function addUser() {
            validator.clear();
            $("#userForm").resetForm();
            $("#userForm").attr("action", "/am/user/create");

            $("#userForm [name=userId]").removeAttr("readonly");
            $("#userForm").formDialog();
        }

        function deleteUser(userId) {
            $.confirm("是否确认删除该用户？", function() {

                $.request.ajax({
                    url : "/am/user/delete",
                    data : {userId : userId},
                    onSuccess : function(msg, data) {
                        $.alert.Success(msg, function(){
                            $.utils.refreshPage();
                        });
                    }
                });
            });
        }

        function editUser(userId) {
            validator.clear();
            $("#userForm").resetForm();
            $("#userForm").attr("action", "/am/user/edit");
            $("#userForm [name=userId]").attr("readonly", "readonly");

            $.request.ajax({
                url : "/am/user/get",
                data : {userId : userId},
                onSuccess : function(msg, data) {
                    $("#userForm").setFormValues(data);
                    $("#userForm").formDialog("show");
                }
            });
        }

    </script>
</head>
<body>

<ol class="breadcrumb">
    <li>当前位置：后台管理</li>
    <li class="active">用户</li>
</ol>

<div class="row">
    <div class="col-md-12">
        <form action="/am/user/index" id="queryForm" class="form-inline" role="form">
            <div class="well">
                <div class="form-group">
                    <label>用户ID：</label>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="userId">
                </div>
                <div class="form-group">
                    <label>用户名：</label>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="userName">
                </div>
                <button type="submit" class="btn btn-default mr50">查询</button>
                <a class="btn btn-warning" href="#" onclick="addUser();">新增用户</a>
            </div>
        </form>

        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
            <tr>
                <th width="15%">用户ID</th>
                <th width="15%">用户名</th>
                <th width="10%">是否可用</th>
                <th width="10%">默认角色</th>
                <th width="15%">修改时间</th>
                <th width="20%">上次登陆时间</th>
                <th width="15%" style="text-align: center;">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${page.resultSet}">
                <tr>
                    <td>${user.userId}</td>
                    <td>${user.userName}</td>
                    <td>${user.availableStatus}</td>
                    <td>${user.defaultRole}</td>
                    <td><fmt:formatDate value="${user.modifyTime}" pattern="yyyy/MM/dd HH:mm" /></td>
                    <td><fmt:formatDate value="${user.lastLoginTime}" pattern="yyyy/MM/dd HH:mm" /></td>
                    <td style="text-align: center;">
                        <a href="#" onclick="editUser('${user.userId}')"><span class="glyphicon glyphicon-edit mr30"></span></a>
                        <a href="#" onclick="deleteUser('${user.userId}')"><span class="glyphicon glyphicon-trash"></span></a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <div id="page"></div>
    </div>
</div>

<form class="form-horizontal" role="form" action="" method="post" id="userForm">
    <div class="form-group">
        <label class="col-sm-3 control-label">用户ID</label>
        <div class="col-sm-9">
            <input type="text" validate="required" class="form-control" name="userId">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">用户名</label>
        <div class="col-sm-9">
            <input type="text" validate="required" class="form-control" name="userName">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">密码</label>
        <div class="col-sm-9">
            <input type="password" validate="required" class="form-control" name="password">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">确认密码</label>
        <div class="col-sm-9">
            <input type="password" validate="required" class="form-control" name="cfPassword">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">默认角色</label>
        <div class="col-sm-9">
            <select class="form-control" name="defaultRole">
                <option value="ADMIN">系统管理员</option>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">是否可用</label>
        <div class="col-sm-9">
            <label class="pr30 pl30 radio-inline"><input type="radio" name="available" value="0">是</label>
            <label class="pl30 radio-inline"><input type="radio" name="available" value="1" checked>否</label>
        </div>
    </div>
</form>


</body>
</html>