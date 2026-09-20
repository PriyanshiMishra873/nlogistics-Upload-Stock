package model;
public class InventoryLedger {
    private int ledgerId, productId; private String productName, transactionType, referenceType, referenceId, txnDate;
    private double quantity, unitCostAtTxn;
    public int getLedgerId(){return ledgerId;} public void setLedgerId(int v){ledgerId=v;}
    public int getProductId(){return productId;} public void setProductId(int v){productId=v;}
    public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
    public String getTransactionType(){return transactionType;} public void setTransactionType(String v){transactionType=v;}
    public double getQuantity(){return quantity;} public void setQuantity(double v){quantity=v;}
    public double getUnitCostAtTxn(){return unitCostAtTxn;} public void setUnitCostAtTxn(double v){unitCostAtTxn=v;}
    public String getReferenceType(){return referenceType;} public void setReferenceType(String v){referenceType=v;}
    public String getReferenceId(){return referenceId;} public void setReferenceId(String v){referenceId=v;}
    public String getTxnDate(){return txnDate;} public void setTxnDate(String v){txnDate=v;}
}
