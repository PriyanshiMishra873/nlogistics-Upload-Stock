<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><title>Inventory Ledger | N Logistics</title><link rel="stylesheet" href="css/style.css"></head><body>
<div class="sidebar"><div class="brand">N <span>Logistics</span></div><div class="nav">
<a href="DashboardController">▦ Dashboard</a><a href="ProductController">▣ Products</a><a href="StockController">▤ Stock</a><a href="InventoryLedgerController">↔ Inventory Ledger</a><a href="StockUploadLogController">⇧ Stock Upload Logs</a><a href="SalesTransactionController">₹ Sales Transactions</a>
</div></div><main class="main">
<% model.InventoryLedger x=(model.InventoryLedger)request.getAttribute("item"); boolean edit=x!=null; %>
<div class="topbar"><div><h1><%=edit?"Edit Ledger Entry":"Add Ledger Entry"%></h1></div></div>
<div class="panel"><form method="post" action="InventoryLedgerController"><%if(edit){%><input type="hidden" name="ledgerId" value="<%=x.getLedgerId()%>"><%}%><div class="form-grid">
<div class="field"><label>Product ID</label><input required type="number" name="productId" value="<%=edit?x.getProductId():"1"%>"></div>
<div class="field"><label>Transaction Type</label><select name="transactionType"><option>IN</option><option>OUT</option><option>ADJUSTMENT</option></select></div>
<div class="field"><label>Quantity</label><input required type="number" step="0.01" name="quantity" value="<%=edit?x.getQuantity():"0"%>"></div>
<div class="field"><label>Unit Cost at Transaction</label><input required type="number" step="0.01" name="unitCostAtTxn" value="<%=edit?x.getUnitCostAtTxn():"0"%>"></div>
<div class="field"><label>Reference Type</label><input name="referenceType" placeholder="Upload / Sale / Damage / Return" value="<%=edit?x.getReferenceType():""%>"></div>
<div class="field"><label>Reference ID</label><input name="referenceId" value="<%=edit?x.getReferenceId():""%>"></div>
<div class="field"><label>Transaction Date</label><input required type="date" name="txnDate" value="<%=edit?x.getTxnDate():""%>"></div>
</div><div class="actions"><button class="btn btn-primary">Save Entry</button><a class="btn btn-secondary" href="InventoryLedgerController">Cancel</a></div></form></div>
</main></body></html>