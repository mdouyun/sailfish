<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>管理后台 - 栏目</title>
    <script type="text/javascript">
        var validator;

        $(function() {
            $("#categoryForm").formDialog({
                title : "编辑栏目",
                width : 530,
                buttons : [{
                    text : "保存",
                    cls : "btn-primary",
                    click : function() {
                        if(!validator.validated()) {
                            return ;
                        }

                        $("#categoryForm").formDialog("close");
                        $("#categoryForm").submitForm({
                            progress : true,
                            onSuccess : function(msg, data) {
                                $.alert.Success(msg, function(){
                                    $.utils.refreshPage();
                                });
                            },
                            onFailure : function(msg, data) {
                                $.alert.Error(msg, function() {
                                    $("#categoryForm").formDialog();
                                });
                            }
                        })
                    }
                }, {
                    text : "关闭",
                    closed : true
                }]
            });
            validator = $("#categoryForm").validate({
                container : "#_formDialog_categoryForm"
            });
        });

        function addCategory() {
            validator.clear();
            $("#categoryForm").resetForm();
            $("#categoryForm [name=parent]").val("${parent}");
            $("#categoryForm").attr("action", "/am/category/create");

            $("#categoryForm [name=enName]").removeAttr("readonly");
            $("#categoryForm").formDialog();
        }

        function editCategory(enName) {
            validator.clear();
            $("#categoryForm").resetForm();
            $("#categoryForm").attr("action", "/am/category/edit");
            $("#categoryForm [name=enName]").attr("readonly", "readonly");

            $.request.ajax({
                url : "/am/category/get",
                data : {enName : enName},
                onSuccess : function(msg, data) {
                    $("#categoryForm").setFormValues(data);
                    $("#categoryForm").formDialog("show");
                }
            });

        }

        function deleteCategory(enName) {
            $.confirm("是否确认删除该栏目及其下属子栏目？", function() {

                $.request.ajax({
                    url : "/am/category/delete",
                    data : {enName : enName},
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
    <li class="active">栏目</li>
</ol>

<div class="row">
    <div class="col-md-9">
        <div class="panel panel-primary">
            <div class="panel-heading">
                <div class="panel-title">
                    上级栏目：<a href="/am/category/index?parent=${grandparent}">${parent}</a>
                    <button type="button" onclick="addCategory();" id="addCategoryBtn" class="btn btn-warning btn-sm pull-right">创建栏目</button>
                </div>
            </div>
            <div class="panel-body">
                <table class="table table-striped table-bordered table-hover table-condensed">
                    <thead>
                    <tr>
                        <th width="15%">栏目代码</th>
                        <th width="15%">栏目名</th>
                        <th width="20%">访问路径</th>
                        <th width="15%">父栏目</th>
                        <th width="10%">是否隐藏</th>
                        <th width="10%">排序号</th>
                        <th width="15%" style="text-align: center;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="category" items="${list}">
                        <tr>
                            <td><a href="/am/category/index?parent=${category.enName}">${category.enName}</a></td>
                            <td>${category.cnName}</td>
                            <td>${category.path}</td>
                            <td>${category.parent}</td>
                            <td>${category.isHidden}</td>
                            <td>${category.sort}</td>
                            <td style="text-align: center;">
                                <a href="#" onclick="editCategory('${category.enName}')"><span class="glyphicon glyphicon-edit mr30"></span></a>
                                <a href="#" onclick="deleteCategory('${category.enName}')"><span class="glyphicon glyphicon-trash"></span></a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="panel panel-info">
            <div style="margin: 5px;">
                <p>说明：</p>
                <p>1、xxxxx</p>
            </div>
        </div>
    </div>
</div>

<form class="form-horizontal" role="form" action="" method="post" id="categoryForm">
    <div class="form-group">
        <label class="col-sm-3 control-label">栏目代码</label>
        <div class="col-sm-9">
            <input type="text" validate="required" class="form-control" name="enName">
            <input type="hidden" name="parent" value="${parent}">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">栏目名称</label>
        <div class="col-sm-9">
            <input type="text" validate="required" class="form-control" name="cnName">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">访问路径</label>
        <div class="col-sm-9">
            <input type="text" validate="required" class="form-control" name="path">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">是否隐藏</label>
        <div class="col-sm-9">
            <label class="pr30 pl30 radio-inline"><input type="radio" name="hidden" value="0">是</label>
            <label class="pl30 radio-inline"><input type="radio" name="hidden" value="1" checked>否</label>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">排序号</label>
        <div class="col-sm-9">
            <input type="text" validate="required number" class="form-control" name="sort">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">栏目模板</label>
        <div class="col-sm-9">
            <select validate="required" class="form-control" name="indexTemplate">
                <option value="">---------------请选择---------------</option>
            <c:forEach items="${templates}" var="tp">
                <option value="${tp}">${tp}</option>
            </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">文章模板</label>
        <div class="col-sm-9">
            <select validate="required" class="form-control" name="articleTemplate">
                <option value="">---------------请选择---------------</option>
                <c:forEach items="${templates}" var="tp">
                    <option value="${tp}">${tp}</option>
                </c:forEach>
            </select>
        </div>
    </div>
</form>


</body>
</html>