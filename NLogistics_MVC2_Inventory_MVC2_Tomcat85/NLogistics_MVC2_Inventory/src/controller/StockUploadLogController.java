package controller;

import db_config.GetConnection;
import implementors.StockUploadLogImplementor;
import model.StockUploadLog;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

@WebServlet("/StockUploadLogController")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class StockUploadLogController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private StockUploadLogImplementor op =
            new StockUploadLogImplementor();


    // =========================================================
    // GET
    // =========================================================

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
            throws ServletException, IOException {

        String action = req.getParameter("action");


        // -----------------------------------------------------
        // EDIT
        // -----------------------------------------------------

        if ("edit".equals(action)) {

            String idText = req.getParameter("id");

            if (idText == null || idText.trim().isEmpty()) {
                res.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Upload ID is required."
                );
                return;
            }

            try {

                int id = Integer.parseInt(idText);

                req.setAttribute(
                        "item",
                        op.getById(id)
                );

                req.getRequestDispatcher(
                        "/stock-upload-logs-form.jsp"
                ).forward(req, res);

            } catch (NumberFormatException e) {

                res.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid upload ID."
                );
            }

            return;
        }


        // -----------------------------------------------------
        // DELETE
        // -----------------------------------------------------

        if ("delete".equals(action)) {

            String idText = req.getParameter("id");

            if (idText == null || idText.trim().isEmpty()) {
                res.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Upload ID is required."
                );
                return;
            }

            try {

                int id = Integer.parseInt(idText);

                op.delete(id);

                res.sendRedirect(
                        "StockUploadLogController"
                );

            } catch (NumberFormatException e) {

                res.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid upload ID."
                );
            }

            return;
        }


        // -----------------------------------------------------
        // ERROR REPORT DOWNLOAD
        // -----------------------------------------------------

        if ("viewErrorReport".equals(action)) {

            downloadErrorReport(req, res);

            return;
        }


        // -----------------------------------------------------
        // DEFAULT
        // -----------------------------------------------------

        req.setAttribute(
                "items",
                op.getAll()
        );

        req.getRequestDispatcher(
                "/stock-upload-logs.jsp"
        ).forward(req, res);
    }


    // =========================================================
    // POST - CSV UPLOAD
    // =========================================================

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
            throws ServletException, IOException {

        Part csvFile = req.getPart("csvFile");


        if (csvFile == null ||
            csvFile.getSize() == 0) {

            res.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Please select a CSV file."
            );

            return;
        }


        String fileName =
                csvFile.getSubmittedFileName();


        if (fileName == null ||
            fileName.trim().isEmpty()) {

            res.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid CSV file."
            );

            return;
        }


        if (!fileName.toLowerCase().endsWith(".csv")) {

            res.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Only CSV files are allowed."
            );

            return;
        }


        /*
         * Existing database structure.
         */
        int companyId = getIntParameter(
                req,
                "companyId",
                1
        );

        int uploadedBy = getIntParameter(
                req,
                "uploadedBy",
                1
        );


        int totalRecords = 0;
        int successCount = 0;
        int failureCount = 0;


        List<String[]> errors =
                new ArrayList<String[]>();


        Connection con = null;

        PreparedStatement productPs = null;
        PreparedStatement stockPs = null;


        String errorReportPath = null;


        try {

            // -------------------------------------------------
            // DATABASE
            // -------------------------------------------------

            con = GetConnection.getConnection();

            if (con == null) {

                throw new Exception(
                        "Database connection failed."
                );
            }


            con.setAutoCommit(false);


            // -------------------------------------------------
            // PRODUCT CHECK
            // -------------------------------------------------

            String findProduct =
                    "SELECT product_id " +
                    "FROM products " +
                    "WHERE product_id=?";


            productPs =
                    con.prepareStatement(
                            findProduct
                    );


            // -------------------------------------------------
            // STOCK INSERT
            // -------------------------------------------------

            String insertStock =
                    "INSERT INTO stock(" +
                    "company_id," +
                    "product_id," +
                    "warehouse_location," +
                    "quantity_on_hand," +
                    "batch_no," +
                    "expiry_date" +
                    ") VALUES(?,?,?,?,?,?)";


            stockPs =
                    con.prepareStatement(
                            insertStock
                    );


            // -------------------------------------------------
            // READ CSV
            // -------------------------------------------------

            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(
                                    csvFile.getInputStream(),
                                    "UTF-8"
                            )
                    );


            String header = br.readLine();


            if (header == null ||
                header.trim().isEmpty()) {

                throw new Exception(
                        "CSV file is empty."
                );
            }


            // -------------------------------------------------
            // HEADER
            // -------------------------------------------------

            String[] headers =
                    header.split(",", -1);


            int productIdIndex = -1;
            int quantityIndex = -1;


            for (int i = 0;
                 i < headers.length;
                 i++) {

                String h =
                        cleanCsvValue(
                                removeBOM(headers[i])
                        );


                if ("productId".equalsIgnoreCase(h)) {
                    productIdIndex = i;
                }


                if ("quantity".equalsIgnoreCase(h)) {
                    quantityIndex = i;
                }
            }


            if (productIdIndex == -1) {

                throw new Exception(
                        "CSV column 'productId' not found."
                );
            }


            if (quantityIndex == -1) {

                throw new Exception(
                        "CSV column 'quantity' not found."
                );
            }


            // -------------------------------------------------
            // CSV ROWS
            // -------------------------------------------------

            String line;


            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }


                totalRecords++;


                String[] data =
                        line.split(",", -1);


                String productIdText = "";
                String quantityText = "";


                if (productIdIndex < data.length) {

                    productIdText =
                            cleanCsvValue(
                                    data[productIdIndex]
                            );
                }


                if (quantityIndex < data.length) {

                    quantityText =
                            cleanCsvValue(
                                    data[quantityIndex]
                            );
                }


                try {

                    // -----------------------------------------
                    // PRODUCT ID
                    // -----------------------------------------

                    if (productIdText.isEmpty()) {

                        throw new Exception(
                                "Product ID is empty."
                        );
                    }


                    int productId;


                    try {

                        productId =
                                Integer.parseInt(
                                        productIdText
                                );

                    } catch (NumberFormatException e) {

                        throw new Exception(
                                "Product ID must be a number."
                        );
                    }


                    if (productId <= 0) {

                        throw new Exception(
                                "Product ID must be greater than 0."
                        );
                    }


                    // -----------------------------------------
                    // QUANTITY
                    // -----------------------------------------

                    if (quantityText.isEmpty()) {

                        throw new Exception(
                                "Quantity is empty."
                        );
                    }


                    double quantity;


                    try {

                        quantity =
                                Double.parseDouble(
                                        quantityText
                                );

                    } catch (NumberFormatException e) {

                        throw new Exception(
                                "Quantity must be a number."
                        );
                    }


                    if (quantity < 0) {

                        throw new Exception(
                                "Quantity cannot be negative."
                        );
                    }


                    // -----------------------------------------
                    // CHECK PRODUCT
                    // -----------------------------------------

                    productPs.setInt(
                            1,
                            productId
                    );


                    ResultSet rs =
                            productPs.executeQuery();


                    boolean exists =
                            rs.next();


                    rs.close();


                    if (!exists) {

                        throw new Exception(
                                "Product ID not found: "
                                + productId
                        );
                    }


                    // -----------------------------------------
                    // INSERT STOCK
                    // -----------------------------------------

                    stockPs.setInt(
                            1,
                            companyId
                    );


                    stockPs.setInt(
                            2,
                            productId
                    );


                    /*
                     * warehouse_location
                     */
                    stockPs.setNull(
                            3,
                            java.sql.Types.VARCHAR
                    );


                    /*
                     * quantity_on_hand
                     */
                    stockPs.setDouble(
                            4,
                            quantity
                    );


                    /*
                     * batch_no
                     */
                    stockPs.setNull(
                            5,
                            java.sql.Types.VARCHAR
                    );


                    /*
                     * expiry_date
                     */
                    stockPs.setNull(
                            6,
                            java.sql.Types.DATE
                    );


                    stockPs.executeUpdate();


                    successCount++;


                } catch (Exception rowError) {

                    failureCount++;


                    String message =
                            rowError.getMessage();


                    if (message == null ||
                        message.trim().isEmpty()) {

                        message = "Unknown error";
                    }


                    errors.add(
                            new String[]{
                                    productIdText,
                                    quantityText,
                                    message
                            }
                    );
                }
            }


            br.close();


            // -------------------------------------------------
            // CLOSE
            // -------------------------------------------------

            productPs.close();
            productPs = null;


            stockPs.close();
            stockPs = null;


            // -------------------------------------------------
            // COMMIT
            // -------------------------------------------------

            con.commit();


            // -------------------------------------------------
            // ERROR REPORT
            // -------------------------------------------------

            if (!errors.isEmpty()) {

                String directoryPath =
                        getServletContext()
                                .getRealPath(
                                        "/error-reports"
                                );


                if (directoryPath == null) {

                    throw new Exception(
                            "Error report directory unavailable."
                    );
                }


                File directory =
                        new File(directoryPath);


                if (!directory.exists()) {
                    directory.mkdirs();
                }


                String baseName =
                        new File(fileName)
                                .getName();


                int dot =
                        baseName.lastIndexOf(".");


                if (dot > 0) {

                    baseName =
                            baseName.substring(
                                    0,
                                    dot
                            );
                }


                String errorFileName =
                        baseName
                        + "_error_"
                        + System.currentTimeMillis()
                        + ".csv";


                File errorFile =
                        new File(
                                directory,
                                errorFileName
                        );


                PrintWriter writer =
                        new PrintWriter(
                                new OutputStreamWriter(
                                        new FileOutputStream(
                                                errorFile
                                        ),
                                        "UTF-8"
                                )
                        );


                writer.println(
                        "productId,quantity,error"
                );


                for (String[] error : errors) {

                    writer.println(
                            csvValue(error[0])
                            + ","
                            + csvValue(error[1])
                            + ","
                            + csvValue(error[2])
                    );
                }


                writer.close();


                errorReportPath =
                        "error-reports/"
                        + errorFileName;
            }


            // -------------------------------------------------
            // SAVE LOG
            // -------------------------------------------------

            StockUploadLog log =
                    new StockUploadLog();


            log.setCompanyId(
                    companyId
            );


            log.setUploadedBy(
                    uploadedBy
            );


            log.setFileName(
                    fileName
            );


            log.setTotalRecords(
                    totalRecords
            );


            log.setSuccessCount(
                    successCount
            );


            log.setFailureCount(
                    failureCount
            );


            log.setErrorReportPath(
                    errorReportPath
            );


            if (!op.save(log)) {

                throw new Exception(
                        "Stock uploaded but upload log could not be saved."
                );
            }


            // -------------------------------------------------
            // SHOW RESULT ON STOCK PAGE
            // -------------------------------------------------

            res.sendRedirect(
                    "StockController"
                    + "?action=uploadResult"
                    + "&file="
                    + java.net.URLEncoder.encode(
                            fileName,
                            "UTF-8"
                    )
                    + "&total="
                    + totalRecords
                    + "&success="
                    + successCount
                    + "&failure="
                    + failureCount
                    + "&errorReport="
                    + java.net.URLEncoder.encode(
                            errorReportPath == null
                                    ? ""
                                    : errorReportPath,
                            "UTF-8"
                    )
            );


        } catch (Exception e) {

            e.printStackTrace();


            if (con != null) {

                try {
                    con.rollback();
                } catch (Exception ignored) {
                }
            }


            res.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "CSV upload failed: "
                    + e.getMessage()
            );


        } finally {

            try {
                if (productPs != null) {
                    productPs.close();
                }
            } catch (Exception ignored) {
            }


            try {
                if (stockPs != null) {
                    stockPs.close();
                }
            } catch (Exception ignored) {
            }


            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ignored) {
            }
        }
    }


    // =========================================================
    // ERROR REPORT DOWNLOAD
    // =========================================================

    private void downloadErrorReport(
            HttpServletRequest req,
            HttpServletResponse res)
            throws IOException {

        String path =
                req.getParameter("path");


        if (path == null ||
            path.trim().isEmpty()) {

            res.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Error report not found."
            );

            return;
        }


        if (path.contains("..") ||
            path.startsWith("/") ||
            !path.startsWith("error-reports/")) {

            res.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid error report path."
            );

            return;
        }


        String realPath =
                getServletContext()
                        .getRealPath(
                                "/" + path
                        );


        if (realPath == null) {

            res.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Error report not found."
            );

            return;
        }


        File file =
                new File(realPath);


        if (!file.exists() ||
            !file.isFile()) {

            res.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Error report not found."
            );

            return;
        }


        res.setContentType(
                "text/csv"
        );


        res.setHeader(
                "Content-Disposition",
                "attachment; filename=\""
                + file.getName()
                + "\""
        );


        try (
                FileInputStream fis =
                        new FileInputStream(file);

                OutputStream os =
                        res.getOutputStream()
        ) {

            byte[] buffer =
                    new byte[4096];

            int length;


            while ((length =
                    fis.read(buffer)) != -1) {

                os.write(
                        buffer,
                        0,
                        length
                );
            }


            os.flush();
        }
    }


    // =========================================================
    // INTEGER PARAMETER
    // =========================================================

    private int getIntParameter(
            HttpServletRequest req,
            String name,
            int defaultValue) {

        String value =
                req.getParameter(name);


        if (value == null ||
            value.trim().isEmpty()) {

            return defaultValue;
        }


        try {

            return Integer.parseInt(
                    value.trim()
            );

        } catch (NumberFormatException e) {

            return defaultValue;
        }
    }


    // =========================================================
    // CSV VALUE
    // =========================================================

    private String csvValue(String value) {

        if (value == null) {
            return "";
        }


        String v =
                value.replace(
                        "\"",
                        "\"\""
                );


        if (v.contains(",") ||
            v.contains("\"") ||
            v.contains("\n") ||
            v.contains("\r")) {

            return "\"" + v + "\"";
        }


        return v;
    }


    // =========================================================
    // REMOVE BOM
    // =========================================================

    private String removeBOM(String value) {

        if (value == null) {
            return "";
        }


        if (value.length() > 0 &&
            value.charAt(0) == '\uFEFF') {

            return value.substring(1);
        }


        return value;
    }


    // =========================================================
    // CLEAN CSV
    // =========================================================

    private String cleanCsvValue(String value) {

        if (value == null) {
            return "";
        }


        String v =
                value.trim();


        if (v.length() >= 2 &&
            v.startsWith("\"") &&
            v.endsWith("\"")) {

            v =
                    v.substring(
                            1,
                            v.length() - 1
                    );


            v =
                    v.replace(
                            "\"\"",
                            "\""
                    );
        }


        return v.trim();
    }
}