<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Product | N Logistics</title><link rel="stylesheet" href="css/style.css"></head><body>
<div class="sidebar"><div class="brand">N <span>Logistics</span></div><div class="nav">
<a href="DashboardController">▦ Dashboard</a><a href="ProductController">▣ Products</a><a href="StockController">▤ Stock</a><a href="InventoryLedgerController">↔ Inventory Ledger</a><a href="StockUploadLogController">⇧ Stock Upload Logs</a><a href="SalesTransactionController">₹ Sales Transactions</a>
</div></div><main class="main">
<% model.Product p=(model.Product)request.getAttribute("item"); boolean edit=p!=null; %>
<div class="topbar"><div><h1><%=edit?"Edit Product":"Add Product"%></h1><div class="muted">Maintain product_name, category, HSN and prices</div></div></div>
<div class="panel"><form method="post" action="ProductController"><% if(edit){ %><input type="hidden" name="productId" value="<%=p.getProductId()%>"><% } %>
<div class="form-grid">
<div class="field"><label>Product Name</label><input required name="productName" value="<%=edit?p.getProductName():""%>"></div>
<div class="field"><label>Category</label><input name="category" value="<%=edit?p.getCategory():""%>"></div>
<div class="field"><label>HSN Code</label><input name="hsnCode" value="<%=edit?p.getHsnCode():""%>"></div>
<div class="field"><label>Unit of Measure</label><input required name="unitOfMeasure" placeholder="kg, box, pallet" value="<%=edit?p.getUnitOfMeasure():""%>"></div>
<div class="field"><label>Unit Cost</label><input required type="number" step="0.01" min="0" name="unitCost" value="<%=edit?p.getUnitCost():"0"%>"></div>
<div class="field"><label>Unit Price</label><input required type="number" step="0.01" min="0" name="unitPrice" value="<%=edit?p.getUnitPrice():"0"%>"></div>
</div><div class="actions"><button class="btn btn-primary" type="submit">Save Product</button><a class="btn btn-secondary" href="ProductController">Cancel</a></div></form></div>
</main></body></html>