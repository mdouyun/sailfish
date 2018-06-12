<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <title>管理后台 - 编辑文章</title>

    <link rel="stylesheet" href="<%=path%>/plugins/kindeditor/themes/default/default.css">
    <script src="<%=path%>/plugins/kindeditor/kindeditor.js" type="text/javascript"></script>
    <script src="<%=path%>/plugins/kindeditor/lang/zh_CN.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(function() {
            $("select[name=category]").val("${article.category}");
            if("${article.starLevel}" != "") {
                $("input[name=starLevel]").attr("checked", false);
                $(":radio[value=${article.starLevel}]").attr("checked", true);
            }

            var editor;
            KindEditor.ready(function(K) {
                editor = K.create('textarea[name="content"]', {
                    uploadJson : "/am/ke/upload",
                    fileManagerJson : "/am/ke/list",
                    allowFileManager : true
                });
            });

            var validator = $("#articleForm").validate();

            $("#publishBtn").click(function() {
                $("#opType").val(1);
                submitForm("发布");
            });

            $("#saveBtn").click(function() {
                $("#opType").val(0);
                submitForm("保存");
            });

            function submitForm(tip) {
                if(!validator.validated()) {
                    return ;
                }

                editor.sync();
                $("#articleForm").submitForm({
                    progress : true,
                    onSuccess : function(msg, data) {
                        $.alert.Success("文章" + tip + "成功。");
                    },
                    onFailure : function(msg, data) {
                        $.alert.Error("文章" + tip + "失败。");
                    }
                })
            }

        });
    </script>


</head>

<body>

<ol class="breadcrumb">
    <li>当前位置：后台管理</li>
    <li><a href="/am/article/index">文章</a></li>
    <li class="active">编辑文章</li>
</ol>

<div class="row">
    <div class="col-md-12">
        <form role="form"  class="form-horizontal" action="/am/article/saveOrModify" method="post" id="articleForm">
            <div class="form-group">
                <label class="col-sm-1 control-label">所属栏目</label>
                <div class="col-sm-3">
                    <input type="hidden" name="id" value="${id}">
                    <select class="form-control" name="category" validate="required">
                        <option value="">--------请选择--------</option>
                        <c:forEach items="${categories}" var="ct">
                            <option value="${ct.enName}">${ct.cnName}</option>
                        </c:forEach>
                    </select>
                </div>
                <label class="col-sm-1 control-label">文章星级</label>
                <div class="col-sm-7">
                    <label class="radio-inline">
                        <input type="radio" name="starLevel" value="5">★★★★★
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="starLevel" value="4">★★★★☆
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="starLevel" value="3">★★★☆☆
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="starLevel" value="2">★★☆☆☆
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="starLevel" value="1" checked>★☆☆☆☆
                    </label>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1 control-label">文章标题</label>
                <div class="col-sm-8">
                    <input type="text" validate="required" value="${article.title}"
                           class="form-control" name="title" placeholder="请输入文章标题">
                </div>
                <div class="input-group col-sm-3"><a href="/am/article/index">返回文章列表</a></div>
            </div>
            <div class="form-group">
                <div class="col-sm-12">
                    <textarea class="form-control" name="content" rows="25"><c:out value="${article.content}"/></textarea>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-12" style="text-align: center;">
                    <button type="button" id="saveBtn" class="btn btn-default ml200">保存文章</button>
                    <button type="button" id="publishBtn" class="btn btn-primary">立即发表</button>
                    <input type="hidden" name="opType" id="opType">
                </div>
            </div>
        </form>
    </div>
</div>

</body>
</html>