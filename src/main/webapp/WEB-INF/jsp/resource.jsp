<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>管理后台 - 资源与模板</title>
    <script type="text/javascript">
        var editValidator;

        $(function() {

            // 新建目录
            var newValidator = $("#newDir").validate({
                placement : "top"
            });
            $("#mkdirBtn").click(function() {
                var p = "${p}";

                if(!newValidator.validated()) {
                    $("#newDir").focus();
                    return ;
                }

                var dir = $("#newDir").val();
                $.request.ajax({
                    url : "/am/rs/createFolder",
                    data : {p : p, dir : dir},
                    onSuccess : function(msg, data) {
                        $.alert.Success(msg, function(){
                            $.utils.refreshPage();
                        });
                    }
                });
            });

            // 修改目录
            $("#dirForm").formDialog({
                title : "修改目录名",
                width : 430,
                buttons : [{
                    text : "保存",
                    cls : "btn-primary",
                    click : function() {
                        var refreshPage = "/am/rs/index?p=${folder.parent}" + $("#renameDir").val();

                        if(!renameValidator.validated()) {
                            $("#renameDir").focus();
                            return ;
                        }

                        $("#dirForm").formDialog("close");
                        $("#dirForm").submitForm({
                            progress : true,
                            onSuccess : function(msg, data) {
                                $.alert.Success(msg, function(){
                                    $.utils.toUrl(refreshPage);
                                });
                            },
                            onFailure : function(msg, data) {
                                $.alert.Error(msg, function() {
                                    $("#dirForm").formDialog();
                                });
                            }
                        })
                    }
                }, {
                    text : "关闭",
                    closed : true
                }]
            });
            $("#renameDirBtn").click(function() {
                $("#renameDir").val($("#renameDir").attr("old"));
                renameValidator.clear();
                $("#dirForm").formDialog("show");
            });
            var renameValidator = $("#dirForm").validate({
                container : "#_formDialog_dirForm"
            });

            //删除当前目录
            $("#deleteDirBtn").click(function() {
                $.confirm("是否确认当前目录？", function() {
                    var p = '${p}';

                    $.request.ajax({
                        url : "/am/rs/deleteFolder",
                        data : {p : p},
                        onSuccess : function(msg, data) {
                            $.alert.Success(msg, function(){
                                $.utils.refreshPage();
                            });
                        }
                    });
                });
            });

            // 上传文件
            $("#uploadForm").formDialog({
                title : "上传文件",
                width : 430,
                buttons : [{
                    text : "上传",
                    cls : "btn-primary",
                    click : function() {
                        var file = $("#fileTxt").val();
                        if(!uploadValidator.validated()) {
                            $("#fileTxt").focus();
                            return ;
                        }

                        $("#uploadForm").formDialog("close");
                        $("#uploadForm").submitForm({
                            progress : true,
                            onSuccess : function(msg, data) {
                                $.alert.Success(msg, function(){
                                    $.utils.refreshPage();
                                });
                            },
                            onFailure : function(msg, data) {
                                $.alert.Error(msg, function() {
                                    $("#uploadForm").formDialog();
                                });
                            }
                        })
                    }
                }, {
                    text : "关闭",
                    closed : true
                }]
            });
            $("#uploadBtn").click(function() {
                $('#uploadForm').resetForm();
                uploadValidator.clear();
                $("#uploadForm").formDialog("show");
            });
            var uploadValidator = $("#uploadForm").validate({
                container : "#_formDialog_uploadForm"
            });

            // 修改文件
            $("#editForm").formDialog({
                title : "编辑文件",
                width : 800,
                height : 450,
                buttons : [{
                    text : "保存",
                    cls : "btn-primary",
                    click : function() {
                        if(!editValidator.validated()) {
                            return ;
                        }

                        $("#editForm").formDialog("close");
                        $("#editForm").submitForm({
                            progress : true,
                            onSuccess : function(msg, data) {
                                $.alert.Success(msg, function(){
                                    $.utils.refreshPage();
                                });
                            },
                            onFailure : function(msg, data) {
                                $.alert.Error(msg, function() {
                                    $("#editForm").formDialog();
                                });
                            }
                        })
                    }
                }, {
                    text : "关闭",
                    closed : true
                }]
            });
            editValidator = $("#editForm").validate({
                container : "#_formDialog_editForm"
            });
        });
        function loadFile(name) {
            var p = '${p}';
            editValidator.clear();
            $.request.ajax({
                url : "/am/rs/loadFile",
                data : {p : p, name : name},
                onSuccess : function(msg, data) {
                    $("#editForm [name=name]").val(name);
                    $("#editForm [name=newName]").val(name);

                    $("#editForm [name=content]").val(data);

                    $("#editForm").formDialog("show");
                }
            });
        }

        function deleteFile(name) {
            $.confirm("是否确认删除该文件？", function() {
                var p = '${p}';

                $.request.ajax({
                    url : "/am/rs/deleteFile",
                    data : {p : p, name : name},
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
    <li class="active">资源与模板</li>
</ol>

<div class="row">
    <div class="col-md-3">
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4 class="panel-title">
                    ${folder.name}<span class="badge pull-right">${folder.fileNumber}</span>
                </h4>
            </div>
            <div class="panel-body">
                <ul class="nav nav-pills nav-stacked">
                <c:if test="${not empty folder.parent}">
                        <li>
                            <a href="/am/rs/index?p=${folder.parent}">
                                <span class="glyphicon glyphicon-share-alt"></span>
                                &nbsp;&nbsp;&nbsp;&nbsp;返回上级目录
                            </a>
                        </li>
                </c:if>
                <c:forEach var="child" items="${folder.childFolders}">
                        <li class="active">
                            <a href="/am/rs/index?p=${child.path}">
                                <span class="glyphicon glyphicon-folder-open"></span>
                                &nbsp;&nbsp;&nbsp;&nbsp;${child.name}<span class="badge pull-right">${child.fileNumber}</span>
                            </a>
                        </li>
                </c:forEach>
                </ul>
            </div>
        </div>
    </div>
    <div class="col-md-9">

        <div class="panel panel-primary">
            <div class="panel-heading">
                <div class="panel-title">
                    <form class="form-inline" role="form">
                        <div class="form-group">
                            <span>当前目录：${p}/</span>
                        </div>
                        <div class="form-group">
                            <input type="text" validate="required" class="form-control pt5 pb5" id="newDir" placeholder="新建目录">
                        </div>
                        <div class="form-group">
                            <button type="button" id="mkdirBtn" class="btn btn-warning btn-sm">新建</button>
                        <c:if test="${not empty folder.parent and folder.path != 'CMS/FLT'}">
                            <button type="button" id="deleteDirBtn" class="btn btn-danger btn-sm">删除</button>
                        </c:if>
                        </div>
                        <div class="form-group pull-right">
                        <c:if test="${not empty folder.parent and folder.path != 'CMS/FLT'}">
                            <a href="#" id="renameDirBtn" class="btn btn-primary btn-sm">修改目录名</a>
                            <span class="pull-right">&nbsp;&nbsp;&nbsp;&nbsp;</span>
                        </c:if>
                            <a href="#" id="uploadBtn" class="btn btn-success btn-sm">上传文件</a>
                        </div>
                    </form>
                </div>
            </div>
            <div class="panel-body">
            <table class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                <tr>
                    <th width="40%">文件名</th>
                    <th width="15%">文件大小</th>
                    <th width="30%">上次修改时间</th>
                    <th width="15%" style="text-align: center;">操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="resource" items="${resources}">
                    <tr>
                    <c:if test="${resource.text}">
                        <td><a href="#" onclick="loadFile('${resource.name}')">${resource.name}</a></td>
                    </c:if>
                    <c:if test="${!resource.text}">
                        <td>${resource.name}</td>
                    </c:if>
                        <td><fmt:formatNumber pattern="#,##0.0#" value="${resource.size/1024}" />&nbsp;KB</td>
                        <td>${resource.lastModifyTime}</td>
                        <td style="text-align: center;">
                            <c:if test="${resource.text}">
                                <a href="#" onclick="loadFile('${resource.name}')"><span class="glyphicon glyphicon-edit mr30"></span></a>
                                <a href="#" onclick="deleteFile('${resource.name}')"><span class="glyphicon glyphicon-trash"></span></a>
                            </c:if>
                            <c:if test="${!resource.text}">
                                <a href="#" onclick="deleteFile('${resource.name}')"><span class="glyphicon glyphicon-trash ml50"></span></a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table></div>
        </div>
    </div>
</div>

<form role="form" action="/am/rs/renameFolder" method="post" id="dirForm">
    <div class="form-group">
        <label for="renameDir" class="control-label">目录名称</label>
        <div class="input-group">
            <span class="input-group-addon">${folder.parent}</span>
            <input type="hidden" name="p" value="${p}">
            <input type="text" validate="required" class="form-control" name="renameDir" old="${folder.name}" value="${folder.name}" id="renameDir">
        </div>
        <span class="help-block"><i class="redStar">*</i>必填，目录名称不得与现有目录名称重复</span>
    </div>
</form>

<form class="form-horizontal" role="form" action="/am/rs/uploadFile" method="post" id="uploadForm">
    <div class="form-group">
        <label class="col-sm-3 control-label">上传目录</label>
        <div class="input-group col-sm-9">
            <p class="form-control-static">${p}/</p>
            <input type="hidden" name="p" value="${p}">
        </div>
    </div>
    <div class="form-group">
        <label for="fileTxt" class="col-sm-3 control-label">选择文件</label>
        <div class="col-sm-9">
            <input type="file" validate="required" name="file" id="fileTxt"  class="form-control">
        </div>
    </div>
</form>

<form role="form" action="/am/rs/editFile" method="post" id="editForm">
    <div class="form-group">
        <label for="newName" class="control-label">文件名</label>
        <div class="input-group">
            <span class="input-group-addon">${p}/</span>
            <input type="hidden" name="p" value="${p}">
            <input type="hidden" name="name">
            <input type="text" validate="required" class="form-control" name="newName" id="newName">
        </div>
    </div>
    <div class="form-group">
        <textarea class="form-control" name="content" rows="16"></textarea>
    </div>
</form>

</body>
</html>