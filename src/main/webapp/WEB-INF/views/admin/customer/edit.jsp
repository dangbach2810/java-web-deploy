<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/common/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:url var = "customerAPI" value ="/api/customer"/>
<c:url var ="customerEditURL" value="/admin/customer/edit"/>
<html>
<head>
    <c:if test="${empty customerId}">
        <title>Thêm khách hàng</title>
    </c:if>
    <c:if test="${not empty customerId}">
        <title>Chỉnh sửa khách hàng</title>
    </c:if>
</head>
<body>
<div class="main-content">

    <div class="main-content-inner">
        <div class="breadcrumbs ace-save-state" id="breadcrumbs">
            <ul class="breadcrumb">
                <li>
                    <i class="ace-icon fa fa-home home-icon"></i>
                    <a href='<c:url value="/admin/home" />'>Home</a>
                </li>
                <c:if test="${empty customerId}">
                    <li class="active">Thêm khách hàng</li>
                </c:if>
                <c:if test="${not empty customerId}">
                    <li class="active">Chỉnh sửa khách hàng</li>
                </c:if>
            </ul><!-- /.breadcrumb -->
        </div>

        <div class="page-content">
            <div class="row">
                <c:if test="${null != messageResponse}">
                    <div class="alert alert-block alert-${alert}">
                        <button type="button" class="close" data-dismiss="alert">
                            <em class="ace-icon fa fa-times"></em>
                        </button>
                            ${messageResponse}
                    </div>
                </c:if>

                <div class="col-xs-12">
                    <form:form modelAttribute="model" id="formEdit" cssClass="form-horizontal">
                        <div class="form-group">
                            <div class="col-xs-9">
                                <form:hidden path="id" id="customerId" cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 no-padding-right" for="fullName">Tên khách hàng</label>
                            <div class="col-xs-9">
                                <form:input path="fullName" cssClass="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 no-padding-right" for="phone">Số điện thoại</label>
                            <div class="col-xs-9">
                                <form:input path="phone" cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 no-padding-right" for="email">Email</label>
                            <div class="col-xs-9">
                                <form:input path="email" cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 no-padding-right" for="companyName">Tên công ty</label>
                            <div class="col-xs-9">
                                <form:input path="companyName" cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 no-padding-right" for="demand">Nhu cầu</label>
                            <div class="col-xs-9">
                                <form:input path="demand" cssClass="form-control" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-xs-3 no-padding-right" for="note">Ghi chú</label>
                            <div class="col-xs-9">
                                <form:input path="note" cssClass="form-control" />
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label no-padding-right"></label>
                            <div class="col-xs-9">
                                <c:if test="${not empty model.id}">
                                    <input id="btnSave" type="button" class="btn btn-primary" value="Cập nhật" />
                                </c:if>
                                <c:if test="${empty model.id}">
                                    <input id="btnSave" type="button" class="btn btn-primary" value="Thêm" />
                                </c:if>
                                <input id="btnCancel" type="button" class="btn btn-warning" value="Huỷ" />
                                <img src="/img/loading.gif" style="display: none; height: 100px" id="loading_image">
                            </div>
                        </div>
                    </form:form>
                </div>
            </div><!-- /.row -->
            <c:if test="${not empty transactionMap}">
                <c:forEach var="item" items="${transactionMap}">
                    <div class="row">
                        <div class="page-header">
                            <h1>
                                ${item.value}
                                <button id="btnAddTransaction${item.key}" type="button" class="btn btn-white btn-primary btn-bold" data-toggle="tooltip" title="Thêm ghi chú">
                                    <span><em class="fa fa-plus-circle bigger-110 purple"></em></span>
                                </button>
                                <img src="/img/loading.gif" style="display: none; height: 100px" id="loading_image">
                            </h1>
                        </div><!-- /.page-header -->
                                <div class="col-xs-12">
                                    <table id="transactionList" class="table table-striped table-bordered table-hover">
                                        <thead>
                                        <tr>
                                            <th>Ngày tạo</th>
                                            <th>Ghi chú</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <C:forEach var="transaction" items="${transactions}">
                                            <c:if test="${transaction.code.equals(item.key)}">
                                                <tr>
                                                    <td>${transaction.createdDate}</td>
                                                    <td>${transaction.note}</td>
                                                </tr>
                                            </c:if>
                                        </C:forEach>
                                        <tr>
                                            <td></td>
                                            <td>
                                                <input type="text" class="transactionNote">
                                                <input type="hidden" class="transactionCode" value="${item.key}">
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>

                                </div>

                    </div><!-- /.row -->
                </c:forEach>
            </c:if>
        </div> <!-- PAGE CONTENT ENDS -->
    </div><!-- /.page-content -->
</div><!-- /.main-content -->
<script>
    $("#btnSave").click(function (e) {
        e.preventDefault();
        let title;
        let message;
        let data = {};

        const formData = $("#formEdit").serializeArray();
        $.each(formData, function (index, v) {
            data["" + v.name + ""] = v.value;
        });
        $('#loading_image').show();
        let id= $('#customerId').val();

        if ('' === id) {
            title = 'Thêm';
            message = 'Thêm khách hàng mới thành công';
        } else {
            title = 'Cập nhật';
            message = 'Cập nhật khách hàng thành công';
        }
        addCustomer(data, message);
    });

    function addCustomer(data, message){
        $.ajax({
            url: '${customerAPI}',
            type: 'POST',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                $('#loading_image').hide();
                alert(message)
                window.location.href = "${customerEditURL}/"+response.id;
            },
            error: function () {
                $('#loading_image').hide();
                alert("error, Đã xảy ra lỗi hệ thống, vui lòng thử lại sau.");
            }
        });
    }
    /*$("#btnAddTransaction").click(function (e) {
        e.preventDefault();
        let title;
        let message;
        let data = {};

        let customerId= $('#customerId').val();
        data["code"] = $('#transactionCode').val();
        data["note"] = $('#transactionNote').val();
        data["customerId"] = customerId;

        if ('' === customerId) {
            title = 'Thêm';
            message = 'Thêm giao dịch thành công';
        } else {
            title = 'Cập nhật';
            message = 'Cập nhật giao dịch thành công';
        }
        addTransaction(data, message);
    });*/
    $(document).on('click', '[id^="btnAddTransaction"]', function() {
        // Lấy giá trị của item key từ ID của button
        var itemKey = $(this).attr('id').replace('btnAddTransaction', '');
        let data = {};
        // Tìm các trường tương ứng dựa trên item key
        var transactionNote = $(this).closest('.row').find('.transactionNote').val();
        var transactionCode = $(this).closest('.row').find('.transactionCode').val();
        let customerId= $('#customerId').val();
        // Kiểm tra kết quả
        console.log("Transaction Note: " + transactionNote);
        console.log("Transaction Code: " + transactionCode);
        console.log("CustomerId: " + customerId);
        data["note"] = transactionNote;
        data["code"] = transactionCode;
        data["customerId"] = customerId;
        addTransaction(data);
    });

    function addTransaction(data){
        $.ajax({
            url: '${customerAPI}/transaction',
            type: 'POST',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json',
            success: function (response) {
                $('#loading_image').hide();
              //  alert(message)
                window.location.reload();
            },
            error: function () {
                $('#loading_image').hide();
                alert("error, Đã xảy ra lỗi hệ thống, vui lòng thử lại sau.");
            }
        });
    }

</script>
</body>
</html>
