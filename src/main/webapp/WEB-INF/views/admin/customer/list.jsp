<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/common/taglib.jsp"%>
<c:url var ="customerURL" value="/admin/customer/list"/>
<c:url var = "customerAPI" value ="/api/customer"/>
<c:url var = "assignmentAPI" value ="/api/building/assignment-building"/>
<html>
<head>
    <title>Quản lý Khách hàng</title>
</head>
<body>
<div class="main-content">
    <form:form modelAttribute="modelSearch" action="${customerURL}" id="listForm" method="GET">
        <div class="main-content-inner">
            <div class="breadcrumbs ace-save-state" id="breadcrumbs">
                <ul class="breadcrumb">
                    <li>
                        <i class="ace-icon fa fa-home home-icon"></i>
                        <a href='<c:url value="/admin/home" />'>Trang chủ</a>
                    </li>
                    <li class="active">Danh sách khách hàng</li>
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
                        <div class="widget-box">
                            <div class="widget-header">
                                <h4 class="widget-title">Tìm kiếm</h4>
                                <div class="widget-toolbar">
                                    <a href="#" data-action="collapse">
                                        <i class="ace-icon fa fa-chevron-up"></i>
                                    </a>
                                </div>
                            </div>
                            <div class="widget-body">
                                <div class="widget-main">
                                    <div class="form-horizontal">
                                        <div class="form-group">
                                            <div class="col-xs-4">
                                                <label for="fullName">Tên khách hàng</label>
                                                <form:input path="fullName" cssClass="form-control"/>
                                            </div>
                                            <div class="col-xs-4">
                                                <label for="phone">Số điện thoại</label>
                                                <form:input path="phone" cssClass="form-control"/>
                                            </div>
                                            <div class="col-xs-4">
                                                <label for="phone">Email</label>
                                                <form:input path="email" cssClass="form-control"/>
                                            </div>
                                        </div>
                                        <security:authorize access="hasRole('MANAGER')">
                                            <div class="form-group">
                                                <div class="col-xs-4">
                                                    <label for="staffId">Nhân viên</label>
                                                    <br>
                                                    <form:select path="staffId">
                                                        <form:option value="">--- Chọn nhân viên phụ trách ---</form:option>
                                                        <form:options items="${staffMap}" />
                                                    </form:select>
                                                </div>
                                            </div>
                                        </security:authorize>

                                        <button class="btn btn-md btn-success" id="btnSearch">Tìm kiếm
                                            <i class="fa fa-arrow-circle-o-right" aria-hidden="true"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </br>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="pull-right">
                            <a class="btn btn-white btn-info btn-bold"
                               data-toggle="tooltip"
                               title="Thêm khách hàng"
                               href='<c:url value="/admin/customer/edit"/>'>
                                <span><em class="fa fa-plus-circle bigger-110 purple"></em></span>
                            </a>
                            <security:authorize access="hasRole('MANAGER')">
                                <button id="btnDeleteCustomer" type="button" class="btn btn-white btn-primary btn-bold" data-toggle="tooltip" title="Xóa toà nhà">
                                    <span><em class="fa fa-trash-o bigger-110 pink"></em></span>
                                </button>
                            </security:authorize>
                        </div>
                    </div>
                </div>
                <br/>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="table-responsive">
                        <table id="customerList" class="table table-fcv-ace table-striped table-bordered table-hover dataTable no-footer"
                                           style="margin: 3em 0 1.5em;">
                                <thead class="center select-cell">
                                  <tr>
                                    <th class="center select-cell">
                                        <fieldset class="form-group">
                                            <input type="checkbox" id="checkAll" class="check-box-element">
                                        </fieldset>
                                    </th>
                                    <th class="text-left">Tên</th>
                                    <th class="text-left">Nhân viên quản lý</th>
                                    <th class="text-left">Di động</th>
                                    <th class="text-left">Email</th>
                                    <th class="text-left">Nhu cầu</th>
                                    <th class="text-left">Người nhập</th>
                                    <th class="text-left">Ngày nhập</th>
                                    <th class="text-left">status</th>
                                    <th class="col-actions">Thao tác</th>
                                  </tr>
                                </thead>s
                                <tbody>
                                    <c:forEach var="item" items="${model.listResult}">
                                        <tr>
                                            <td>
                                            <fieldset>
                                            <input type="checkbox" name="buildingIds" value="${item.id}"
                                               id="checkbox_${item.id}" class="check-box-element" />
                                            </fieldset>
                                            </td>
                                            <td>${item.fullName}</td>
                                            <td>${item.staffName}</td>
                                            <td>${item.phone}</td>
                                            <td>${item.email}</td>
                                            <td>${item.demand}</td>
                                            <td>${item.createBy}</td>
                                            <td>${item.createDate}</td>
                                            <td>${item.status}</td>

                                            <td>
                                                <a class="btn btn-xs btn-info"
                                                   data-toggle="tooltip"
                                                   title="Sửa tòa nhà"
                                                   href='<c:url value='/admin/customer/edit/${item.id}'/>'>
                                        <span><em class="ace-icon fa fa-pencil bigger-120"></em></span>
                                                </a>
                                                <security:authorize access="hasRole('MANAGER')">
                                                    <button class="btn btn-xs btn-info" data-toggle="tooltip"
                                                            title="Giao khách hàng" id="btnCustomerAssignment" customerId="${item.id}">
                                                        <i class="fa fa-user" aria-hidden="true"></i>
                                                    </button>
                                                </security:authorize>
                                            </td>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                             </table>

                        </div>
                    </div>
                </div>
                <div class="text-center">
                     <ul id="pagination" class="pagination"></ul>
                </div>
                <form:hidden path="page" id="page"/>
            </div> <!-- /.page-content -->
        </div>
    </form:form>
</div><!-- /.main-content -->

<!-- assignmentBuilding -->
<div class="modal fade" id="assignmentCustomerModal" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Danh sách nhân viên</h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered" id="staffList">
                    <thead>
                    <tr>
                        <th>Chọn nhân viên</th>
                        <th>Tên nhân viên</th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <input type="hidden" id="customerId" name="customerId" value="">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" id="btnAssignCustomer">Giao khách hàng</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Đóng lại</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $('#btnSearch').click(function (e) {
        e.preventDefault();
        $('#listForm').submit();
    });
let totalPages = ${model.totalPages};
    let currentPage = ${model.page};
    let totalItems = ${model.totalItems};

    $(function () {
        window.pagObj = $('#pagination').twbsPagination({
            totalPages: totalPages,
            visiblePages: 10,
            startPage: currentPage,
            onPageClick: function (event, page) {
                if (currentPage !== page) {
                    $('#page').val(page);
                    $('#listForm').submit();
                    /*$('#page').val(page);
                    const formData = form.serialize();
                    let urlSearchParams = new URLSearchParams(formData);
                    const queryString = urlSearchParams.toString();
                    const action = form.attr('action')
                    form.attr('action', action + "?" + queryString);
                    form.submit();*/
                }
        },
        // Text labels
        first: 'Trang đầu',
        prev: 'Trang trước',
        next: 'Tiếp theo',
        last: 'Trang cuối',
        });
    });
    $(document).ready(function () {
        const checkboxAll = $('#checkAll');
        const buildingItemsCheckbox = $('#customerList input[type=checkbox][name="customerIds"]');

        buildingItemsCheckbox.change(function () {
            const numberOfChecked = $('#customerList input[type=checkbox][name="customerIds"]:checked').length;
            const isCheckedAll = buildingItemsCheckbox.length === numberOfChecked;
            checkboxAll.prop('checked', isCheckedAll);

            if (1 <= numberOfChecked) {
                $('#btnDeleteCustomer').attr('disabled', false);
            } else {
                $('#btnDeleteCustomer').attr('disabled', true);
            }
        });
    });

    $(document).on("click", "#customerList button#btnCustomerAssignment", function (e) {
        e.preventDefault();
        openModalAssignmentCustomer();
        let customerId = $(this).attr('customerId');
        $('#customerId').val(customerId);
        loadStaff(customerId);
    })

    function openModalAssignmentCustomer() {
        $('#assignmentCustomerModal').modal();
    }

    function loadStaff(customerId) {
        $.ajax({
            type: "GET",
            url: "${customerAPI}/"+customerId+"/staffs",
            dataType: "json",
            success: function (response) {
                console.log('success');
                let row ='';
                $.each(response, function (index,item) {
                    row += '<tr>';
                    row += '<td class = "text-center"><input type="checkbox" value=' + item.staffId + ' id ="checkbox_' + item.staffId + '" class = "check-box-element" ' + item.checked+'/></td>';
                    row += '<td class = "text-center">' +item.fullName+'</td>';
                    row += '</tr>';
                });
                $('#staffList tbody').html(row);
            },
            error: function (response) {
                alert("error, Đã xảy ra lỗi hệ thống, vui lòng thử lại sau.");
            }
        });
    }

    $('#btnAssignCustomer').click(function (e) {
        e.preventDefault();
        let data = {};
        data['customerId'] = $('#customerId').val();
        data['staffIds'] = $('#staffList').find('tbody input[type=checkbox]:checked').map(function () {
            return $(this).val();
        }).get();
        assignStaff(data);
        /*showConfirmationAlertBeforeAction(function () {

        }, "Giao", "Giao ".concat(dataLength > 1 ? "các " : "", "nhân viên đã chọn quản lý tòa nhà này?"));*/
    });
    function assignStaff(data) {
        $.ajax({
            type: "POST",
            url: "${customerAPI}/assignment/customer",
            data: JSON.stringify(data),
            dataType: "json",
            contentType: 'application/json',
            success: function () {
                alert("success, Giao tòa nhà cho nhân viên quản lý thành công!");
            },
            error: function () {
                alert("error, Đã xảy ra lỗi hệ thống, vui lòng thử lại sau.");
            }
        });
    }

    $('#btnDeleteCustomer').click(function (e) {
        e.preventDefault();
        let customerIds = $('#customerList').find(
            'tbody input[type=checkbox]:checked').map(function () {
            return $(this).val();
        }).get();
        deleteCustomer(customerIds);
    });

    function deleteCustomer(data) {
        $.ajax({
            type: "DELETE",
            url: "${customerAPI}",
            data: JSON.stringify(data),
            dataType: "json",
            contentType: 'application/json',
            success: function () {
                <%--window.location.href = "<c:url value ='/admin/building-list'/>"--%>
                alert("Xóa tòa nhà thành công")
                window.location.reload();
            },
            error: function () {
                alert('error, Đã xảy ra lỗi hệ thống, vui lòng thử lại sau.');
            }
        });
    }
    /*let totalPages ${model.totalPages};
    let currentPage = ${model.page};
    let totalPages = ${model.totalItems};
    $(function () {
    window.pagObj = $('#pagination').twbsPagination({
        totalPages: totalPages,
        visiblePages: 5,
        startPage: currentPage,
        onPageClick: function (event, page) {
            if (currentPage !== page) {
                $('#page').val(page);
                $('#listForm').submit();
                /!*$('#page').val(page);
                const formData = form.serialize();
                let urlSearchParams = new URLSearchParams(formData);
                const queryString = urlSearchParams.toString();
                const action = form.attr('action')
                form.attr('action', action + "?" + queryString);
                form.submit();*!/
            }
        },
        // Text labels
        first: 'Trang đầu',
        prev: 'Trang trước',
        next: 'Tiếp theo',
        last: 'Trang cuối',
        });
    });*/
</script>
</body>
</html>