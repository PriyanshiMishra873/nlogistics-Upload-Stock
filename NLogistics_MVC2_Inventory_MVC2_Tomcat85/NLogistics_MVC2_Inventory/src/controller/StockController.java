package controller;

import db_config.GetConnection;
import implementors.StockImplementor;
import model.Stock;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/StockController")
public class StockController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private StockImplementor op =
            new StockImplementor();


    // =========================================================
    // GET
    // =========================================================

    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse res)
            throws ServletException, IOException {

        String action =
                req.getParameter("action");


        // -----------------------------------------------------
        // EDIT
        // -----------------------------------------------------

        if ("edit".equals(action)) {

            req.setAttribute(
                    "item",
                    op.getById(
                            Integer.parseInt(
                                    req.getParameter("id")
                            )
                    )
            );

            req.getRequestDispatcher(
                    "/stock-form.jsp"
            ).forward(req, res);

            return;
        }


        // -----------------------------------------------------
        // DELETE
        // -----------------------------------------------------

        if ("delete".equals(action)) {

            op.delete(
                    Integer.parseInt(
                            req.getParameter("id")
                    )
            );

            res.sendRedirect(
                    "StockController"
            );

            return;
        }


        // -----------------------------------------------------
        // CSV UPLOAD RESULT
        // -----------------------------------------------------

        if ("uploadResult".equals(action)) {

            showUploadResult(
                    req,
                    res
            );

            return;
        }


        // -----------------------------------------------------
        // NORMAL STOCK LIST
        // -----------------------------------------------------

        req.setAttribute(
                "items",
                op.getAll()
        );


        req.getRequestDispatcher(
                "/stock.jsp"
        ).forward(req, res);
    }


    // =========================================================
    // POST - NORMAL STOCK SAVE / UPDATE
    // =========================================================

    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse res)
            throws ServletException, IOException {

        Stock s =
                new Stock();


        String id =
                req.getParameter("stockId");


        if (id != null &&
            !id.isEmpty()) {

            s.setStockId(
                    Integer.parseInt(id)
            );
        }


        s.setCompanyId(
                Integer.parseInt(
                        req.getParameter(
                                "companyId"
                        )
                )
        );


        s.setProductId(
                Integer.parseInt(
                        req.getParameter(
                                "productId"
                        )
                )
        );


        s.setWarehouseLocation(
                req.getParameter(
                        "warehouseLocation"
                )
        );


        s.setQuantityOnHand(
                Double.parseDouble(
                        req.getParameter(
                                "quantityOnHand"
                        )
                )
        );


        s.setBatchNo(
                req.getParameter(
                        "batchNo"
                )
        );


        s.setExpiryDate(
                req.getParameter(
                        "expiryDate"
                )
        );


        if (id != null &&
            !id.isEmpty()) {

            op.update(s);

        } else {

            op.save(s);
        }


        res.sendRedirect(
                "StockController"
        );
    }


    // =========================================================
    // SHOW CSV UPLOAD RESULT
    // =========================================================

    private void showUploadResult(
            HttpServletRequest req,
            HttpServletResponse res)
            throws ServletException, IOException {

        String fileName =
                req.getParameter("file");


        String total =
                req.getParameter("total");


        String success =
                req.getParameter("success");


        String failure =
                req.getParameter("failure");


        String errorReport =
                req.getParameter("errorReport");


        /*
         * Get current stock records.
         *
         * This shows the actual Product Name
         * from the products table.
         */
        List<Stock> stockItems =
                getAllStock();


        req.setAttribute(
                "uploadResult",
                true
        );


        req.setAttribute(
                "uploadFileName",
                fileName
        );


        req.setAttribute(
                "uploadTotal",
                total
        );


        req.setAttribute(
                "uploadSuccess",
                success
        );


        req.setAttribute(
                "uploadFailure",
                failure
        );


        req.setAttribute(
                "errorReportPath",
                errorReport
        );


        req.setAttribute(
                "stockItems",
                stockItems
        );


        /*
         * Reuse the upload JSP to display
         * result + stock verification.
         */
        req.getRequestDispatcher(
                "/stock-upload-logs-form.jsp"
        ).forward(req, res);
    }


    // =========================================================
    // GET STOCK WITH PRODUCT DETAILS
    // =========================================================

    private List<Stock> getAllStock() {

        List<Stock> list =
                new ArrayList<Stock>();


        String q =
                "SELECT s.*, " +
                "c.company_name, " +
                "p.product_name " +
                "FROM stock s " +
                "JOIN companies c " +
                "ON s.company_id=c.company_id " +
                "JOIN products p " +
                "ON s.product_id=p.product_id " +
                "ORDER BY s.stock_id DESC";


        try (
                Connection c =
                        GetConnection.getConnection();

                PreparedStatement ps =
                        c.prepareStatement(q);

                ResultSet r =
                        ps.executeQuery()
        ) {

            while (r.next()) {

                Stock s =
                        new Stock();


                s.setStockId(
                        r.getInt(
                                "stock_id"
                        )
                );


                s.setCompanyId(
                        r.getInt(
                                "company_id"
                        )
                );


                s.setProductId(
                        r.getInt(
                                "product_id"
                        )
                );


                s.setCompanyName(
                        r.getString(
                                "company_name"
                        )
                );


                s.setProductName(
                        r.getString(
                                "product_name"
                        )
                );


                s.setWarehouseLocation(
                        r.getString(
                                "warehouse_location"
                        )
                );


                s.setQuantityOnHand(
                        r.getDouble(
                                "quantity_on_hand"
                        )
                );


                s.setBatchNo(
                        r.getString(
                                "batch_no"
                        )
                );


                s.setExpiryDate(
                        r.getString(
                                "expiry_date"
                        )
                );


                s.setLastUpdated(
                        r.getString(
                                "last_updated"
                        )
                );


                list.add(s);
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


        return list;
    }
}