<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Sales Transaction | N Logistics</title><link rel="stylesheet" href="css/style.css"></head><body>
<div class="sidebar"><div class="brand">N <span>Logistics</span></div><div class="nav">
<a href="DashboardController">📊 Dashboard</a><a href="ProductController">📦 Products</a><a href="StockController">📋 Stock</a><a href="InventoryLedgerController">📑 Inventory Ledger</a><a href="StockUploadLogController">📥 Stock Upload Logs</a><a href="SalesTransactionController">💰 Sales Transactions</a>
</div></div><main class="main">
<% model.SalesTransaction x=(model.SalesTransaction)request.getAttribute("item"); boolean edit=x!=null; %>
<div class="topbar"><div><h1><%=edit?"Edit Sale":"Add Sale"%></h1><div class="muted">sale_amount = quantity_sold × sale_price_snapshot</div></div></div>
<div class="panel"><form method="post" action="SalesTransactionController"><%if(edit){%><input type="hidden" name="transactionId" value="<%=x.getTransactionId()%>"><%}%><div class="form-grid">
<div class="field"><label>Product ID</label><input required type="number" name="productId" value="<%=edit?x.getProductId():"1"%>"></div>
<div class="field"><label>Customer ID</label><input required type="number" name="customerId" value="<%=edit?x.getCustomerId():"1"%>"></div>
<div class="field"><label>Shipment ID (optional)</label><input type="number" name="shipmentId" value="<%=edit&&x.getShipmentId()!=null?x.getShipmentId():""%>"></div>
<div class="field"><label>Quantity Sold</label><input required type="number" step="0.01" min="0" name="quantitySold" value="<%=edit?x.getQuantitySold():"1"%>"></div>
<div class="field"><label>Sale Price Snapshot</label><input required type="number" step="0.01" min="0" name="salePriceSnapshot" value="<%=edit?x.getSalePriceSnapshot():"0"%>"></div>
<div class="field"><label>Sale Date</label><input required type="date" name="saleDate" value="<%=edit?x.getSaleDate():""%>"></div>
</div><div class="actions"><button class="btn btn-primary">Save Sale</button><a class="btn btn-secondary" href="SalesTransactionController">Cancel</a></div></form></div>
</main></body></html>