<%@ page contentType="text/html;charset=UTF-8" %>

<%
    boolean uploadResult =
            Boolean.TRUE.equals(
                    request.getAttribute("uploadResult")
            );

    String uploadFileName =
            (String) request.getAttribute("uploadFileName");

    String uploadTotal =
            (String) request.getAttribute("uploadTotal");

    String uploadSuccess =
            (String) request.getAttribute("uploadSuccess");

    String uploadFailure =
            (String) request.getAttribute("uploadFailure");

    String errorReportPath =
            (String) request.getAttribute("errorReportPath");

    java.util.List<model.Stock> stockItems =
            (java.util.List<model.Stock>)
                    request.getAttribute("stockItems");
%>


<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>
        Stock Upload | N Logistics
    </title>

    <link
        rel="stylesheet"
        href="css/style.css"
    >

    <style>

        .upload-box {
            margin-top: 20px;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            background: #fff;
        }


        .csv-help {
            margin-top: 8px;
            color: #666;
            font-size: 13px;
        }


        .preview-box {
            display: none;
            margin-top: 25px;
        }


        .preview-table {
            width: 100%;
            border-collapse: collapse;
            background: #fff;
        }


        .preview-table th,
        .preview-table td {

            border: 1px solid #ddd;

            padding: 9px;

            text-align: left;
        }


        .preview-table th {
            background: #f5f5f5;
        }


        .summary {
            display: flex;
            gap: 15px;
            margin-top: 15px;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }


        .summary-card {

            border: 1px solid #ddd;

            border-radius: 8px;

            padding: 14px 20px;

            background: #fafafa;

            min-width: 140px;
        }


        .summary-card strong {

            display: block;

            font-size: 22px;

            margin-top: 5px;
        }


        .success-result {

            margin-top: 20px;

            padding: 20px;

            border-radius: 10px;

            background: #eaf8ea;

            border: 1px solid #a8d8a8;
        }


        .failure-result {

            margin-top: 20px;

            padding: 20px;

            border-radius: 10px;

            background: #fff4e5;

            border: 1px solid #e5c28a;
        }


        .result-title {

            font-size: 22px;

            font-weight: bold;

            margin-bottom: 10px;
        }


        .stock-verification {

            margin-top: 30px;
        }


        .stock-table {

            width: 100%;

            border-collapse: collapse;

            background: #fff;
        }


        .stock-table th,
        .stock-table td {

            border: 1px solid #ddd;

            padding: 10px;

            text-align: left;
        }


        .stock-table th {

            background: #f5f5f5;

            font-weight: bold;
        }


        .badge-success {

            display: inline-block;

            padding: 5px 10px;

            border-radius: 20px;

            background: #dff4df;

            color: #176b17;
        }


        .badge-failure {

            display: inline-block;

            padding: 5px 10px;

            border-radius: 20px;

            background: #ffe0e0;

            color: #a00000;
        }


        .error-message {

            display: none;

            margin-top: 15px;

            padding: 12px;

            border-radius: 6px;

            background: #ffecec;

            color: #b00020;
        }


        .success-message {

            display: none;

            margin-top: 15px;

            padding: 12px;

            border-radius: 6px;

            background: #eaf8ea;

            color: #176b17;
        }


        .actions {

            margin-top: 20px;
        }


        #uploadBtn {

            display: none;
        }


        .result-actions {

            margin-top: 20px;
        }

    </style>

</head>


<body>


<!-- =========================================================
     SIDEBAR
     ========================================================= -->

<div class="sidebar">

    <div class="brand">

        N <span>Logistics</span>

    </div>


    <div class="nav">

        <a href="DashboardController">
            ▦ Dashboard
        </a>


        <a href="ProductController">
            ▣ Products
        </a>


        <a href="StockController">
            ▤ Stock
        </a>


        <a href="InventoryLedgerController">
            ↔ Inventory Ledger
        </a>


        <a href="StockUploadLogController">
            ⇧ Stock Upload Logs
        </a>


        <a href="SalesTransactionController">
            ₹ Sales Transactions
        </a>

    </div>

</div>


<!-- =========================================================
     MAIN
     ========================================================= -->

<main class="main">


    <div class="topbar">

        <div>

            <h1>
                Stock CSV Upload
            </h1>

        </div>

    </div>


<% if (!uploadResult) { %>


    <!-- =====================================================
         UPLOAD FORM
         ===================================================== -->

    <div class="panel">

        <form
            method="post"
            action="StockUploadLogController"
            enctype="multipart/form-data"
            id="csvForm"
        >


            <div class="form-grid">


                <div class="field">

                    <label>
                        Company ID
                    </label>

                    <input
                        type="number"
                        name="companyId"
                        value="1"
                        min="1"
                        required
                    >

                </div>


                <div class="field">

                    <label>
                        Uploaded By User ID
                    </label>

                    <input
                        type="number"
                        name="uploadedBy"
                        value="1"
                        min="1"
                        required
                    >

                </div>


            </div>


            <div class="upload-box">


                <div class="field">

                    <label>
                        Select Stock CSV
                    </label>


                    <input
                        type="file"
                        id="csvFile"
                        name="csvFile"
                        accept=".csv"
                        required
                    >


                    <div class="csv-help">

                        Required columns:

                        <strong>
                            productId, quantity
                        </strong>

                        <br>

                        Example:

                        <strong>
                            1,10
                        </strong>

                    </div>

                </div>


                <div
                    id="errorMessage"
                    class="error-message"
                ></div>


                <div
                    id="successMessage"
                    class="success-message"
                ></div>


                <div
                    id="previewBox"
                    class="preview-box"
                >


                    <h3>
                        CSV Preview
                    </h3>


                    <div class="summary">


                        <div class="summary-card">

                            File Name

                            <strong id="fileName">
                                -
                            </strong>

                        </div>


                        <div class="summary-card">

                            Total

                            <strong id="totalRecords">
                                0
                            </strong>

                        </div>


                        <div class="summary-card">

                            Valid

                            <strong id="validRecords">
                                0
                            </strong>

                        </div>


                        <div class="summary-card">

                            Invalid

                            <strong id="invalidRecords">
                                0
                            </strong>

                        </div>


                    </div>


                    <div style="overflow-x:auto;">


                        <table
                            class="preview-table"
                            id="previewTable"
                        >

                            <thead>

                                <tr>

                                    <th>#</th>

                                    <th>
                                        Product ID
                                    </th>

                                    <th>
                                        Quantity
                                    </th>

                                    <th>
                                        Status
                                    </th>

                                </tr>

                            </thead>


                            <tbody
                                id="previewBody"
                            ></tbody>

                        </table>

                    </div>


                    <div class="actions">


                        <button
                            type="submit"
                            class="btn btn-primary"
                            id="uploadBtn"
                        >
                            Upload Stock
                        </button>


                        <a
                            class="btn btn-secondary"
                            href="StockUploadLogController"
                        >
                            Cancel
                        </a>


                    </div>


                </div>


            </div>


        </form>

    </div>


<% } else { %>


    <!-- =====================================================
         UPLOAD RESULT
         ===================================================== -->

    <div class="panel">


        <div
            class="<%= "0".equals(uploadFailure)
                    ? "success-result"
                    : "failure-result" %>"
        >


            <div class="result-title">

                <%= "0".equals(uploadFailure)
                        ? "✓ Stock Upload Completed"
                        : "⚠ Stock Upload Completed With Errors" %>

            </div>


            <div>

                <strong>
                    File:
                </strong>

                <%= uploadFileName == null
                        ? "-"
                        : uploadFileName %>

            </div>


            <div class="summary">


                <div class="summary-card">

                    Total Records

                    <strong>
                        <%= uploadTotal == null
                                ? "0"
                                : uploadTotal %>
                    </strong>

                </div>


                <div class="summary-card">

                    Successfully Added

                    <strong>
                        <%= uploadSuccess == null
                                ? "0"
                                : uploadSuccess %>
                    </strong>

                </div>


                <div class="summary-card">

                    Failed

                    <strong>
                        <%= uploadFailure == null
                                ? "0"
                                : uploadFailure %>
                    </strong>

                </div>


            </div>


            <% if (errorReportPath != null &&
                   !errorReportPath.trim().isEmpty()) { %>


                <div style="margin-top:15px;">

                    <strong>
                        Some rows failed.
                    </strong>

                    <br><br>

                    <a
                        class="btn btn-primary"
                        href="StockUploadLogController?action=viewErrorReport&path=<%= java.net.URLEncoder.encode(errorReportPath, "UTF-8") %>"
                    >
                        Download Error Report
                    </a>

                </div>


            <% } else { %>


                <div style="margin-top:15px;">

                    <span class="badge-success">
                        All records were successfully added.
                    </span>

                </div>


            <% } %>


        </div>


        <!-- =================================================
             STOCK VERIFICATION
             ================================================= -->


        <div class="stock-verification">


            <h2>
                Stock Verification
            </h2>


            <p>
                The table below is read directly from the
                <strong>stock</strong> table and joined with
                the <strong>products</strong> table.
            </p>


            <% if (stockItems != null &&
                   !stockItems.isEmpty()) { %>


                <div style="overflow-x:auto;">


                    <table class="stock-table">


                        <thead>

                            <tr>

                                <th>
                                    Stock ID
                                </th>

                                <th>
                                    Product ID
                                </th>

                                <th>
                                    Product Name
                                </th>

                                <th>
                                    Quantity
                                </th>

                                <th>
                                    Company
                                </th>

                                <th>
                                    Warehouse
                                </th>

                                <th>
                                    Batch
                                </th>

                                <th>
                                    Expiry
                                </th>

                            </tr>

                        </thead>


                        <tbody>


                        <% for (model.Stock s : stockItems) { %>


                            <tr>


                                <td>
                                    <%= s.getStockId() %>
                                </td>


                                <td>
                                    <%= s.getProductId() %>
                                </td>


                                <td>
                                    <strong>
                                        <%= s.getProductName() %>
                                    </strong>
                                </td>


                                <td>
                                    <strong>
                                        <%= s.getQuantityOnHand() %>
                                    </strong>
                                </td>


                                <td>
                                    <%= s.getCompanyName() %>
                                </td>


                                <td>
                                    <%= s.getWarehouseLocation()
                                            == null
                                            ? "-"
                                            : s.getWarehouseLocation() %>
                                </td>


                                <td>
                                    <%= s.getBatchNo()
                                            == null
                                            ? "-"
                                            : s.getBatchNo() %>
                                </td>


                                <td>
                                    <%= s.getExpiryDate()
                                            == null
                                            ? "-"
                                            : s.getExpiryDate() %>
                                </td>


                            </tr>


                        <% } %>


                        </tbody>


                    </table>


                </div>


            <% } else { %>


                <div class="failure-result">

                    No stock records found.

                </div>


            <% } %>


        </div>


        <!-- =================================================
             BUTTONS
             ================================================= -->


        <div class="result-actions">


            <a
                class="btn btn-primary"
                href="StockController"
            >
                View Stock Page
            </a>


            <a
                class="btn btn-secondary"
                href="StockUploadLogController"
            >
                View Upload Logs
            </a>


            <a
                class="btn btn-secondary"
                href="StockUploadLogController"
            >
                Upload Another CSV
            </a>


        </div>


    </div>


<% } %>


</main>


<% if (!uploadResult) { %>

<!-- =========================================================
     JAVASCRIPT
     ========================================================= -->

<script>

document.addEventListener(
    "DOMContentLoaded",
    function () {


        const fileInput =
            document.getElementById(
                "csvFile"
            );


        const previewBox =
            document.getElementById(
                "previewBox"
            );


        const previewBody =
            document.getElementById(
                "previewBody"
            );


        const uploadBtn =
            document.getElementById(
                "uploadBtn"
            );


        const errorMessage =
            document.getElementById(
                "errorMessage"
            );


        const successMessage =
            document.getElementById(
                "successMessage"
            );


        const fileName =
            document.getElementById(
                "fileName"
            );


        const totalRecords =
            document.getElementById(
                "totalRecords"
            );


        const validRecords =
            document.getElementById(
                "validRecords"
            );


        const invalidRecords =
            document.getElementById(
                "invalidRecords"
            );


        fileInput.addEventListener(
            "change",
            function () {


                resetMessages();


                previewBody.innerHTML =
                    "";


                previewBox.style.display =
                    "none";


                uploadBtn.style.display =
                    "none";


                if (!fileInput.files ||
                    fileInput.files.length === 0) {

                    return;
                }


                const file =
                    fileInput.files[0];


                if (!file.name
                        .toLowerCase()
                        .endsWith(".csv")) {

                    showError(
                        "Please select a CSV file."
                    );

                    fileInput.value =
                        "";

                    return;
                }


                fileName.textContent =
                    file.name;


                const reader =
                    new FileReader();


                reader.onload =
                    function (event) {

                        processCSV(
                            event.target.result
                        );
                    };


                reader.onerror =
                    function () {

                        showError(
                            "Unable to read CSV file."
                        );
                    };


                reader.readAsText(
                    file,
                    "UTF-8"
                );
            }
        );


        function processCSV(text) {


            text =
                text.replace(
                    /^\uFEFF/,
                    ""
                );


            const lines =
                text.split(/\r?\n/);


            if (lines.length === 0) {

                showError(
                    "CSV file is empty."
                );

                return;
            }


            const header =
                parseCSVLine(
                    lines[0]
                );


            let productIdIndex =
                -1;


            let quantityIndex =
                -1;


            for (
                let i = 0;
                i < header.length;
                i++
            ) {


                const h =
                    header[i]
                        .trim()
                        .toLowerCase();


                if (h === "productid") {

                    productIdIndex =
                        i;
                }


                if (h === "quantity") {

                    quantityIndex =
                        i;
                }
            }


            if (productIdIndex === -1 ||
                quantityIndex === -1) {

                showError(
                    "CSV must contain: productId, quantity"
                );

                return;
            }


            let total = 0;
            let valid = 0;
            let invalid = 0;


            previewBody.innerHTML =
                "";


            const maxPreview =
                20;


            for (
                let i = 1;
                i < lines.length;
                i++
            ) {


                if (
                    lines[i].trim() === ""
                ) {
                    continue;
                }


                total++;


                const row =
                    parseCSVLine(
                        lines[i]
                    );


                let productId =
                    "";


                let quantity =
                    "";


                if (
                    productIdIndex <
                    row.length
                ) {

                    productId =
                        row[
                            productIdIndex
                        ].trim();
                }


                if (
                    quantityIndex <
                    row.length
                ) {

                    quantity =
                        row[
                            quantityIndex
                        ].trim();
                }


                let status =
                    "Valid";


                if (productId === "") {

                    status =
                        "Invalid - Product ID empty";

                    invalid++;

                } else if (
                    !/^\d+$/.test(
                        productId
                    )
                ) {

                    status =
                        "Invalid - Product ID must be number";

                    invalid++;

                } else if (
                    parseInt(
                        productId,
                        10
                    ) <= 0
                ) {

                    status =
                        "Invalid - Product ID must be > 0";

                    invalid++;

                } else if (
                    quantity === ""
                ) {

                    status =
                        "Invalid - Quantity empty";

                    invalid++;

                } else if (
                    isNaN(
                        Number(quantity)
                    )
                ) {

                    status =
                        "Invalid - Quantity must be number";

                    invalid++;

                } else if (
                    Number(quantity) < 0
                ) {

                    status =
                        "Invalid - Quantity negative";

                    invalid++;

                } else {

                    valid++;
                }


                if (
                    total <= maxPreview
                ) {


                    const tr =
                        document.createElement(
                            "tr"
                        );


                    const td1 =
                        document.createElement(
                            "td"
                        );

                    td1.textContent =
                        total;


                    const td2 =
                        document.createElement(
                            "td"
                        );

                    td2.textContent =
                        productId;


                    const td3 =
                        document.createElement(
                            "td"
                        );

                    td3.textContent =
                        quantity;


                    const td4 =
                        document.createElement(
                            "td"
                        );

                    td4.textContent =
                        status;


                    tr.appendChild(td1);
                    tr.appendChild(td2);
                    tr.appendChild(td3);
                    tr.appendChild(td4);


                    previewBody.appendChild(
                        tr
                    );
                }
            }


            totalRecords.textContent =
                total;


            validRecords.textContent =
                valid;


            invalidRecords.textContent =
                invalid;


            previewBox.style.display =
                "block";


            if (total > 0) {

                uploadBtn.style.display =
                    "inline-block";


                showSuccess(
                    "CSV verified. Click Upload Stock to add the records."
                );

            } else {

                showError(
                    "CSV contains no data records."
                );
            }
        }


        function parseCSVLine(line) {


            const result = [];

            let current = "";

            let insideQuotes =
                false;


            for (
                let i = 0;
                i < line.length;
                i++
            ) {


                const char =
                    line[i];


                if (char === '"') {


                    if (
                        insideQuotes &&
                        line[i + 1] === '"'
                    ) {

                        current += '"';

                        i++;

                    } else {

                        insideQuotes =
                            !insideQuotes;
                    }


                } else if (
                    char === "," &&
                    !insideQuotes
                ) {


                    result.push(
                        current
                    );


                    current = "";


                } else {


                    current += char;
                }
            }


            result.push(
                current
            );


            return result;
        }


        function showError(message) {

            errorMessage.textContent =
                message;


            errorMessage.style.display =
                "block";


            successMessage.style.display =
                "none";
        }


        function showSuccess(message) {

            successMessage.textContent =
                message;


            successMessage.style.display =
                "block";


            errorMessage.style.display =
                "none";
        }


        function resetMessages() {

            errorMessage.style.display =
                "none";


            successMessage.style.display =
                "none";
        }

    }
);

</script>

<% } %>


</body>

</html>