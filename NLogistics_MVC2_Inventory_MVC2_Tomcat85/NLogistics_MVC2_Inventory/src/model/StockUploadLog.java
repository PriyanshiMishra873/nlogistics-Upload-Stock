package model;
public class StockUploadLog {
    private int uploadId, companyId, uploadedBy; private String companyName, uploadedByName, fileName, errorReportPath, uploadedAt;
    private int totalRecords, successCount, failureCount;
    public int getUploadId(){return uploadId;} public void setUploadId(int v){uploadId=v;}
    public int getCompanyId(){return companyId;} public void setCompanyId(int v){companyId=v;}
    public int getUploadedBy(){return uploadedBy;} public void setUploadedBy(int v){uploadedBy=v;}
    public String getCompanyName(){return companyName;} public void setCompanyName(String v){companyName=v;}
    public String getUploadedByName(){return uploadedByName;} public void setUploadedByName(String v){uploadedByName=v;}
    public String getFileName(){return fileName;} public void setFileName(String v){fileName=v;}
    public int getTotalRecords(){return totalRecords;} public void setTotalRecords(int v){totalRecords=v;}
    public int getSuccessCount(){return successCount;} public void setSuccessCount(int v){successCount=v;}
    public int getFailureCount(){return failureCount;} public void setFailureCount(int v){failureCount=v;}
    public String getErrorReportPath(){return errorReportPath;} public void setErrorReportPath(String v){errorReportPath=v;}
    public String getUploadedAt(){return uploadedAt;} public void setUploadedAt(String v){uploadedAt=v;}
}
