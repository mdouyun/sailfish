<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>管理后台 - 广告</title>
    <script type="text/javascript">
        var validator;

        $(function() {
            $("#page").page({
                formId : "queryForm",
                pageSize : parseInt('${page.pageSize}'),
                totalSize : parseInt('${page.total}'),
                pageIndex : parseInt('${page.pageIndex}')
            });

            $("#adForm").formDialog({
                title : "编辑广告",
                width : 530,
                buttons : [{
                    text : "保存",
                    cls : "btn-primary",
                    click : function() {
                        if(!validator.validated()) {
                            return ;
                        }

                        $("#adForm").formDialog("close");
                        $("#adForm").submitForm({
                            progress : true,
                            onSuccess : function(msg, data) {
                                $.alert.Success(msg, function(){
                                    $.utils.refreshPage();
                                });
                            },
                            onFailure : function(msg, data) {
                                $.alert.Error(msg, function() {
                                    $("#adForm").formDialog();
                                });
                            }
                        })
                    }
                }, {
                    text : "关闭",
                    closed : true
                }]
            });
            validator = $("#adForm").validate({
                container : "#_formDialog_adForm"
            });
        });

        function addAd() {
            validator.clear();
            $("#adForm").resetForm();
            $("#adForm").attr("action", "/am/ad/create");

            $("#adForm [name=code]").removeAttr("readonly");
            $("#adForm").formDialog();
        }

        function editAd(code) {
            validator.clear();
            $("#adForm").resetForm();
            $("#adForm").attr("action", "/am/ad/edit");
            $("#adForm [name=code]").attr("readonly", "readonly");

            $.request.ajax({
                url : "/am/ad/get",
                data : {code : code},
                onSuccess : function(msg, data) {
                    $("#adForm").setFormValues(data);
                    $("#adForm").formDialog("show");
                }
            });

        }

        function deleteAd(code) {
            $.confirm("是否确认该广告脚本？", function() {

                $.request.ajax({
                    url : "/am/ad/delete",
                    data : {code : code},
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
    <li class="active">广告</li>
</ol>

<div class="row">
    <div class="col-md-9">
        <form class="form-inline" role="form" id="queryForm" action="/am/ad/index">
            <div class="well">
                <div class="form-group">
                    <label>广告编号：</label>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="code">
                </div>
                <div class="form-group pl100">
                    <label>广告状态：</label>
                </div>
                <div class="form-group">
                    <select class="form-control" name="status">
                        <option value="">---全部---</option>
                        <option value="0">显示</option>
                        <option value="1">隐藏</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-default mr50">查询</button>
                <a class="btn btn-warning" href="#" onclick="addAd();">创建广告脚本</a>
            </div>
        </form>

        <table class="table table-striped table-bordered table-hover table-condensed">
            <thead>
                <tr>
                    <th width="15%">广告编号</th>
                    <th width="30%">说明</th>
                    <th width="15%">创建时间</th>
                    <th width="15%">修改时间</th>
                    <th width="10%">是否隐藏</th>
                    <th width="15%" style="text-align: center;">操作</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="ad" items="${page.resultSet}">
                <tr>
                    <td>${ad.code}</td>
                    <td>${ad.remark}</td>
                    <td><fmt:formatDate value="${ad.createTime}" pattern="yyyy/MM/dd HH:mm" /></td>
                    <td><fmt:formatDate value="${ad.modifyTime}" pattern="yyyy/MM/dd HH:mm" /></td>
                    <td>${ad.isHidden}</td>
                    <td style="text-align: center;">
                        <a href="#" onclick="editAd('${ad.code}')"><span class="glyphicon glyphicon-edit mr30"></span></a>
                        <a href="#" onclick="deleteAd('${ad.code}')"><span class="glyphicon glyphicon-trash"></span></a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <div id="page"></div>
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

<form class="form-horizontal" role="form" action="" method="post" id="adForm">
    <div class="form-group">
        <label class="col-sm-3 control-label">广告编号</label>
        <div class="col-sm-9">
            <input type="text" validate="required" class="form-control" name="code">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">说明</label>
        <div class="col-sm-9">
            <input type="text" validate="required" class="form-control" name="remark">
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
        <label class="col-sm-3 control-label">广告脚本</label>
        <div class="col-sm-9">
            <textarea validate="required" class="form-control" name="html" rows="10"></textarea>
        </div>
    </div>
</form>


</body>
</html>