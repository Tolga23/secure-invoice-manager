package com.thardal.secureinvoicemanager.report;

import com.thardal.secureinvoicemanager.base.entity.BaseAdditionalFields;
import com.thardal.secureinvoicemanager.customer.entity.Customer;
import com.thardal.secureinvoicemanager.user.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.thardal.secureinvoicemanager.base.enums.GlobalErrorMessages.UNABLE_TO_EXPORT_REPORT;

@Slf4j
public class CustomerReport {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Customer> customers;
    private static String[] HEADERS = {"ID", "Name", "Email", "Type", "Status", "Address", "Phone", "Created At"};

    public CustomerReport(List<Customer> customers) {
        this.customers = customers;
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("Customers");
        setHeaders();
    }

    /**
     * Sets the headers for the report.
     * Creates a new row for the headers, creates a cell style for the headers,
     * and then creates a cell for each header in the HEADERS array, setting the cell's value and style.
     */
    private void setHeaders() {
        Row headerRow = sheet.createRow(0);
        CellStyle style = createCellStyle(14, true);
        IntStream.range(0, HEADERS.length).forEach(index -> {
            Cell cell = headerRow.createCell(index);
            cell.setCellValue(HEADERS[index]);
            cell.setCellStyle(style);
        });
    }

    /**
     * Creates a cell style with the specified font height and bold setting.
     *
     * @param fontHeight The height of the font for the cell style.
     * @param isBold     Whether the font should be bold or not.
     * @return The created cell style.
     */
    private CellStyle createCellStyle(double fontHeight, boolean isBold) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(isBold);
        font.setFontHeight(fontHeight);
        style.setFont(font);
        return style;
    }

    /**
     * @return The report as an InputStreamResource.
     */
    public InputStreamResource export() {
        return generateReport();
    }

    /**
     * Generates the report.
     * Creates a ByteArrayOutputStream, creates a cell style for the report,
     * creates a row for each customer in the customers field and a cell for each field of the customer,
     * writes the workbook to the ByteArrayOutputStream, and then returns the ByteArrayOutputStream as an InputStreamResource.
     *
     * @return The report as an InputStreamResource.
     * @throws ApiException If an exception occurs while generating the report.
     */
    private InputStreamResource generateReport() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CellStyle style = createCellStyle(12, false);
            int rowIndex = 1;
            for (Customer customer : customers) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(customer.getId());
                row.createCell(1).setCellValue(customer.getName());
                row.createCell(2).setCellValue(customer.getEmail());
                row.createCell(3).setCellValue(customer.getType());
                row.createCell(4).setCellValue(customer.getStatus());
                row.createCell(5).setCellValue(customer.getAddress());
                row.createCell(6).setCellValue(customer.getPhone());

                Optional.ofNullable(customer.getBaseAdditionalFields())
                        .map(BaseAdditionalFields::getCreatedDate)
                        .map(createdDate -> DateFormatUtils.format(createdDate, "yyyy-MM-dd HH:mm:ss"))
                        .ifPresentOrElse(formattedDate ->
                                row.createCell(7).setCellValue(formattedDate),
                                () -> row.createCell(7).setCellValue(""));
            }
            workbook.write(out);
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException(UNABLE_TO_EXPORT_REPORT);
        }
    }


}
