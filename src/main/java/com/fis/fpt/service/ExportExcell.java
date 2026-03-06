package com.fis.fpt.service;

import com.fis.fpt.client.UaaClient;
import com.fis.fpt.converter.DateConverter;
import com.fis.fpt.domain.Form;
import com.fis.fpt.repository.FormRepository;
import com.fis.fpt.repository.VersionRepository;
import com.fis.fpt.search.*;
import com.fis.fpt.security.SecurityUtils;
import com.fis.fpt.security.UserInFoDetails;
import com.fis.fpt.service.dto.FormDto;
import com.fis.fpt.service.dto.FormShareDto;
import com.fis.fpt.service.dto.VersionDto;
import com.fis.fpt.uaadomain.UserDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExportExcell {
    @Autowired
    public static FormRepository formRepository;
    @Autowired
    public static VersionRepository versionRepository;
    @Autowired
    public static UaaClient uaaClient;
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs_Version = {"Version", "Ngày thay đổi", "Người thay đổi", "Nội dung thay đổi"};
    static String SHEET = "Form";
    static String[] HEADERs_Form = {"STT", "Form Id", "Tên biểu mẫu", "Ngày hiệu lực", "Tags", "Trạng thái", "Ngày tạo"};

    static String[] HEADERs_Form_Share = {"STT", "Form Id", "Tên biểu mẫu", "Ngày hiệu lực", "Tags", "Trạng thái", "Ngày tạo", "Người tạo"};
    static String SHEET1 = "Version";

    @Autowired
    public ExportExcell(FormRepository formRepository, VersionRepository versionRepository, UaaClient uaaClient) {
        this.formRepository = formRepository;
        this.versionRepository = versionRepository;
        this.uaaClient = uaaClient;
    }

    public static ByteArrayInputStream exportExcelForm(FormSearchOwnerExcelDto formSearchOwnerDto) {
        FormSearchOwnerExcel formSearchOwner = new FormSearchOwnerExcel(formSearchOwnerDto);
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();

        List<FormDto> forms = formRepository.findFormOwnerByFilterExcell(formSearchOwner, currentUser.getOrgIn());
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

// Tạo dòng mới và truyền giá trị cho ô đã merge
            Row mergedRow = sheet.createRow(0); // Tạo dòng mới
            Cell mergedCell = mergedRow.createCell(0);
            mergedCell.setCellValue("QUẢN LÝ BIỂU MẪU");

// Tạo CellStyle để căn giữa in đậm và tăng kích cỡ chữ
            CellStyle mergedCellStyle = createCellStyle(workbook);
            mergedCellStyle.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
            Font mergedFont = workbook.createFont();
            mergedFont.setBold(true); // In đậm
            mergedFont.setFontHeightInPoints((short) 20); // Tăng kích cỡ chữ lên 14 điểm
            mergedCellStyle.setFont(mergedFont);
            mergedCell.setCellStyle(mergedCellStyle);
            Instant currentTime = Instant.now();
            LocalDate date = currentTime.atZone(ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Row timeRow = sheet.createRow(1); // Tạo dòng mới
            Cell timeCell = timeRow.createCell(0);
            timeCell.setCellValue("Xuất file excell vào: " + date.format(formatterTime)); // Đặt giá trị thời gian


            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
            CellStyle timeCellStyle = createCellStyle(workbook);
            timeCellStyle.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
            timeCell.setCellStyle(timeCellStyle);


            Row headerRow = sheet.createRow(2);
            for (int col = 0; col < HEADERs_Form.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs_Form[col]);

                // Tạo một CellStyle để căn giữa dữ liệu trong ô
                Font font = workbook.createFont();
                font.setBold(true);

                CellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setFont(font);
                style.setAlignment(HorizontalAlignment.CENTER);

                cell.setCellStyle(style);
            }
            int[] columnWidths = {2000, 4000, 10000, 8000, 8000, 6000, 5000};
            for (int col = 0; col < columnWidths.length; col++) {
                sheet.setColumnWidth(col, columnWidths[col]);
            }

            int rowIdx = 3;
            int cnt = 1;
            for (FormDto form : forms) {
                Row row = sheet.createRow(rowIdx++);

                // Tạo một CellStyle để căn giữa dữ liệu trong ô

                CellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setAlignment(HorizontalAlignment.LEFT);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                CellStyle style1 = workbook.createCellStyle();
                style1.setBorderBottom(BorderStyle.THIN);
                style1.setBorderTop(BorderStyle.THIN);
                style1.setBorderRight(BorderStyle.THIN);
                style1.setBorderLeft(BorderStyle.THIN);
                style1.setAlignment(HorizontalAlignment.CENTER);

                String statusForm = "";
                if (form.getStatusForm().equals("releasing")) {
                    statusForm = "Đang phát hành";
                } else if (form.getStatusForm().equals("draft")) {
                    statusForm = "Bản nháp";
                } else {
                    statusForm = "Ngưng phát hành";
                }

                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date dateBegin = inputDateFormat.parse(form.getBeginTime());
                String beginTime = outputDateFormat.format(dateBegin);
                Date dateEnd = inputDateFormat.parse(form.getEndTime());
                String endTime = outputDateFormat.format(dateEnd);
                Date dateCreated = inputDateFormat.parse(form.getCreatedDate());
                String createdDate = outputDateFormat.format(dateCreated);
                StringBuilder resultTag = new StringBuilder();
                for (String tag : form.getTag()) {
                    if (resultTag.length() > 0) {
                        resultTag.append(", ");
                    }
                    resultTag.append(tag);
                }

                row.createCell(0).setCellValue(cnt++);
                row.createCell(1).setCellValue(form.getFormId());
                row.createCell(2).setCellValue(form.getFormName());
                row.createCell(3).setCellValue(beginTime + " - " + endTime);
                row.createCell(4).setCellValue(String.valueOf(resultTag));
                row.createCell(5).setCellValue(statusForm);
                row.createCell(6).setCellValue(createdDate);


                // Đặt CellStyle cho các ô dữ liệu
                row.getCell(0).setCellStyle(style1);
                row.getCell(1).setCellStyle(style);
                row.getCell(2).setCellStyle(style);
                row.getCell(3).setCellStyle(style1);
                row.getCell(4).setCellStyle(style);
                row.getCell(5).setCellStyle(style);
                row.getCell(6).setCellStyle(style1);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static ByteArrayInputStream exportExcelFormList(List<String> formIds) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

// Tạo dòng mới và truyền giá trị cho ô đã merge
            Row mergedRow = sheet.createRow(0); // Tạo dòng mới
            Cell mergedCell = mergedRow.createCell(0);
            mergedCell.setCellValue("QUẢN LÝ BIỂU MẪU");

// Tạo CellStyle để căn giữa in đậm và tăng kích cỡ chữ
            CellStyle mergedCellStyle = createCellStyle(workbook);
            mergedCellStyle.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
            Font mergedFont = workbook.createFont();
            mergedFont.setBold(true); // In đậm
            mergedFont.setFontHeightInPoints((short) 20); // Tăng kích cỡ chữ lên 14 điểm
            mergedCellStyle.setFont(mergedFont);
            mergedCell.setCellStyle(mergedCellStyle);
            Instant currentTime = Instant.now();
            LocalDate date = currentTime.atZone(ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Row timeRow = sheet.createRow(1); // Tạo dòng mới
            Cell timeCell = timeRow.createCell(0);
            timeCell.setCellValue("Xuất file excell vào: " + date.format(formatterTime)); // Đặt giá trị thời gian


            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
            CellStyle timeCellStyle = createCellStyle(workbook);
            timeCellStyle.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
            timeCell.setCellStyle(timeCellStyle);


            Row headerRow = sheet.createRow(2);

            for (int col = 0; col < HEADERs_Form.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs_Form[col]);

                Font font = workbook.createFont();
                font.setBold(true);

                CellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setFont(font);
                style.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(style);
            }
            int[] columnWidths = {2000, 4000, 10000, 8000, 8000, 6000, 5000};
            for (int col = 0; col < columnWidths.length; col++) {
                sheet.setColumnWidth(col, columnWidths[col]);
            }

            int rowIdx = 3;
            int cnt = 1;
            for (String formId : formIds) {
                Form form = formRepository.findFormByFormId(formId);
                Row row = sheet.createRow(rowIdx++);

                // Tạo một CellStyle để căn giữa dữ liệu trong ô
                CellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setAlignment(HorizontalAlignment.LEFT);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.of("Asia/Ho_Chi_Minh"));

                CellStyle style1 = workbook.createCellStyle();
                style1.setBorderBottom(BorderStyle.THIN);
                style1.setBorderTop(BorderStyle.THIN);
                style1.setBorderRight(BorderStyle.THIN);
                style1.setBorderLeft(BorderStyle.THIN);
                style1.setAlignment(HorizontalAlignment.CENTER);

                String statusForm = "";
                if (form.getStatusForm()==2) {
                    statusForm = "Đang phát hành";
                } else if (form.getStatusForm()==1) {
                    statusForm = "Bản nháp";
                } else {
                    statusForm = "Ngưng phát hành";
                }

                row.createCell(0).setCellValue(cnt++);
                row.createCell(1).setCellValue(formId);
                row.createCell(2).setCellValue(form.getFormName());
                row.createCell(3).setCellValue(formatter.format(form.getBeginTime()) + " - " + formatter.format(form.getEndTime()));
                row.createCell(4).setCellValue(String.valueOf(form.getTag()));
                row.createCell(5).setCellValue(statusForm);
                row.createCell(6).setCellValue(formatter.format(form.getCreatedDate()));


                // Đặt CellStyle cho các ô dữ liệu
                row.getCell(0).setCellStyle(style1);
                row.getCell(1).setCellStyle(style);
                row.getCell(2).setCellStyle(style);
                row.getCell(3).setCellStyle(style1);
                row.getCell(4).setCellStyle(style);
                row.getCell(5).setCellStyle(style);
                row.getCell(6).setCellStyle(style1);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream exportExcelFormShare(FormSearchShareDto formSearchShareDto) {
        UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();
        String orgIn = currentUser.getOrgIn();
        FormSearchShare formSearchShare = new FormSearchShare(formSearchShareDto);
        formSearchShare.setUsername(currentUser.getLogin().trim());
        List<FormShareDto> forms = formRepository.findFormShareExcell(formSearchShare, currentUser.getOrgIn());
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

// Tạo dòng mới và truyền giá trị cho ô đã merge
            Row mergedRow = sheet.createRow(0); // Tạo dòng mới
            Cell mergedCell = mergedRow.createCell(0);
            mergedCell.setCellValue("QUẢN LÝ BIỂU MẪU ĐƯỢC CHIA SẺ");

// Tạo CellStyle để căn giữa in đậm và tăng kích cỡ chữ
            CellStyle mergedCellStyle = createCellStyle(workbook);
            mergedCellStyle.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
            Font mergedFont = workbook.createFont();
            mergedFont.setBold(true); // In đậm
            mergedFont.setFontHeightInPoints((short) 20); // Tăng kích cỡ chữ lên 14 điểm
            mergedCellStyle.setFont(mergedFont);
            mergedCell.setCellStyle(mergedCellStyle);
            Instant currentTime = Instant.now();
            LocalDate date = currentTime.atZone(ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Row timeRow = sheet.createRow(1); // Tạo dòng mới
            Cell timeCell = timeRow.createCell(0);
            timeCell.setCellValue("Xuất file excell vào: " + date.format(formatterTime)); // Đặt giá trị thời gian


            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
            CellStyle timeCellStyle = createCellStyle(workbook);
            timeCellStyle.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
            timeCell.setCellStyle(timeCellStyle);


            Row headerRow = sheet.createRow(2);

            for (int col = 0; col < HEADERs_Form_Share.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs_Form_Share[col]);
                Font font = workbook.createFont();
                font.setBold(true);

                CellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setFont(font);
                style.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(style);
            }
            int[] columnWidths = {2000, 4000, 8000, 10000, 6000, 8000, 6000, 12000};
            for (int col = 0; col < columnWidths.length; col++) {
                sheet.setColumnWidth(col, columnWidths[col]);
            }

            int rowIdx = 3;
            int cnt = 1;
            for (FormShareDto form : forms) {
                Row row = sheet.createRow(rowIdx++);

                // Tạo một CellStyle để căn giữa dữ liệu trong ô
                CellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setAlignment(HorizontalAlignment.LEFT);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                CellStyle style1 = workbook.createCellStyle();
                style1.setBorderBottom(BorderStyle.THIN);
                style1.setBorderTop(BorderStyle.THIN);
                style1.setBorderRight(BorderStyle.THIN);
                style1.setBorderLeft(BorderStyle.THIN);
                style1.setAlignment(HorizontalAlignment.CENTER);
                StringBuilder resultTag = new StringBuilder();
                for (String tag : form.getTag()) {
                    if (resultTag.length() > 0) {
                        resultTag.append(", ");
                    }
                    resultTag.append(tag);
                }

                String statusForm = "";
                if (form.getStatusForm().equals("releasing")) {
                    statusForm = "Đang phát hành";
                } else if (form.getStatusForm().equals("draft")) {
                    statusForm = "Bản nháp";
                } else {
                    statusForm = "Ngưng phát hành";
                }

                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date dateBegin = inputDateFormat.parse(form.getBeginTime());
                String beginTime = outputDateFormat.format(dateBegin);
                Date dateEnd = inputDateFormat.parse(form.getEndTime());
                String endTime = outputDateFormat.format(dateEnd);
                Date dateCreated = inputDateFormat.parse(form.getCreatedDate());
                String createdDate = outputDateFormat.format(dateCreated);


                row.createCell(0).setCellValue(cnt++);
                row.createCell(1).setCellValue(form.getFormId());
                row.createCell(2).setCellValue(form.getFormName());
                row.createCell(3).setCellValue(beginTime + " - " + endTime);
                row.createCell(4).setCellValue(String.valueOf(resultTag));
                row.createCell(5).setCellValue(statusForm);
                row.createCell(6).setCellValue(createdDate);
                UserDTO userDTO = uaaClient.getInfo(form.getCreatedBy());
                form.setFullName(userDTO.getFirstName() + " " + userDTO.getLastName());
                RichTextString richTextString = workbook.getCreationHelper().createRichTextString(form.getFullName() + "\n" + form.getCreatedBy());
                row.createCell(7).setCellValue(richTextString);
                // Đặt CellStyle cho các ô dữ liệu
                row.getCell(0).setCellStyle(style1);
                row.getCell(1).setCellStyle(style);
                row.getCell(2).setCellStyle(style);
                row.getCell(3).setCellStyle(style1);
                row.getCell(4).setCellStyle(style);
                row.getCell(5).setCellStyle(style);
                row.getCell(6).setCellStyle(style1);
                CellStyle styleCreatedBy = style;
                styleCreatedBy.setWrapText(true);
                row.getCell(7).setCellStyle(styleCreatedBy);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static ByteArrayInputStream exportExcelFormShareList(List<String> formIds) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

// Tạo dòng mới và truyền giá trị cho ô đã merge
            Row mergedRow = sheet.createRow(0); // Tạo dòng mới
            Cell mergedCell = mergedRow.createCell(0);
            mergedCell.setCellValue("QUẢN LÝ BIỂU MẪU ĐƯỢC CHIA SẺ");

// Tạo CellStyle để căn giữa in đậm và tăng kích cỡ chữ
            CellStyle mergedCellStyle = createCellStyle(workbook);
            mergedCellStyle.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
            Font mergedFont = workbook.createFont();
            mergedFont.setBold(true); // In đậm
            mergedFont.setFontHeightInPoints((short) 20); // Tăng kích cỡ chữ lên 14 điểm
            mergedCellStyle.setFont(mergedFont);
            mergedCell.setCellStyle(mergedCellStyle);
            Instant currentTime = Instant.now();
            LocalDate date = currentTime.atZone(ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Row timeRow = sheet.createRow(1); // Tạo dòng mới
            Cell timeCell = timeRow.createCell(0);
            timeCell.setCellValue("Xuất file excell vào: " + date.format(formatterTime)); // Đặt giá trị thời gian


            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
            CellStyle timeCellStyle = createCellStyle(workbook);
            timeCellStyle.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
            timeCell.setCellStyle(timeCellStyle);


            Row headerRow = sheet.createRow(2);

            for (int col = 0; col < HEADERs_Form_Share.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs_Form_Share[col]);
                Font font = workbook.createFont();
                font.setBold(true);
                // Tạo một CellStyle để căn giữa dữ liệu trong ô
                CellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setFont(font);
                style.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(style);
            }
            int[] columnWidths = {2000, 4000, 8000, 10000, 6000, 8000, 6000, 12000};
            for (int col = 0; col < columnWidths.length; col++) {
                sheet.setColumnWidth(col, columnWidths[col]);
            }

            int rowIdx = 3;
            int cnt = 1;
            for (String formId : formIds) {
                Form form = formRepository.findFormByFormId(formId);
                Row row = sheet.createRow(rowIdx++);

                // Tạo một CellStyle để căn giữa dữ liệu trong ô
                CellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setAlignment(HorizontalAlignment.LEFT);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Ho_Chi_Minh"));

                CellStyle style1 = workbook.createCellStyle();
                style1.setBorderBottom(BorderStyle.THIN);
                style1.setBorderTop(BorderStyle.THIN);
                style1.setBorderRight(BorderStyle.THIN);
                style1.setBorderLeft(BorderStyle.THIN);
                style1.setAlignment(HorizontalAlignment.CENTER);

                String statusForm = "";
                if (form.getStatusForm()==2) {
                    statusForm = "Đang phát hành";
                } else if (form.getStatusForm()==1) {
                    statusForm = "Bản nháp";
                } else {
                    statusForm = "Ngưng phát hành";
                }

                row.createCell(0).setCellValue(cnt++);
                row.createCell(1).setCellValue(formId);
                row.createCell(2).setCellValue(form.getFormName());
                row.createCell(3).setCellValue(formatter.format(form.getBeginTime()) + " - " + formatter.format(form.getEndTime()));
                row.createCell(4).setCellValue(form.getTag().toString());
                row.createCell(5).setCellValue(statusForm);
                row.createCell(6).setCellValue(formatter.format(form.getCreatedDate()));
                row.createCell(7).setCellValue(form.getCreatedBy());
                UserDTO userDTO = uaaClient.getInfo(form.getCreatedBy());
                String fullname = userDTO.getFirstName() + " " + userDTO.getLastName();
                RichTextString richTextString = workbook.getCreationHelper().createRichTextString(fullname + "\n" + form.getCreatedBy());
                row.createCell(7).setCellValue(richTextString);


                // Đặt CellStyle cho các ô dữ liệu
                row.getCell(0).setCellStyle(style1);
                row.getCell(1).setCellStyle(style);
                row.getCell(2).setCellStyle(style);
                row.getCell(3).setCellStyle(style);
                row.getCell(4).setCellStyle(style);
                row.getCell(5).setCellStyle(style);
                row.getCell(6).setCellStyle(style1);
                CellStyle styleCreatedBy = style;
                styleCreatedBy.setWrapText(true);
                row.getCell(7).setCellStyle(styleCreatedBy);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream exportExcelVersion1(SearchVersionDto searchVersionDto) {
        SearchVersion searchVersion = new SearchVersion();
        searchVersion.formId = searchVersionDto.formId;
//        searchVersion.version = Integer.parseInt(searchVersionDto.version);
        searchVersion.sort = searchVersionDto.sort;
        if (!(searchVersionDto.start == null || searchVersionDto.start.equals(""))) {
            searchVersion.start = DateConverter.parseStringToZonedDateTime(searchVersionDto.start);
        }
        if (!(searchVersionDto.end == null || searchVersionDto.end.equals(""))) {
            searchVersion.end = DateConverter.parseStringToZonedDateTime(searchVersionDto.end);
        }
        List<VersionDto> versions = new ArrayList<>();


        if (searchVersion.sort == 0) {
            if (searchVersionDto.version == null || searchVersionDto.version.equals("")) {
                versions = versionRepository.findVersionList(searchVersion);
            } else if (searchVersionDto.version.equals("0")) {
            } else {
                List<VersionDto> versions2 = new ArrayList<>();
                versions2 = versionRepository.findVersionList(searchVersion);
                searchVersion.version = Integer.parseInt(searchVersionDto.version);
                if (searchVersion.version > versions2.size()) {
                    searchVersion.formId = "";
                    versions = versionRepository.findVersionList(searchVersion);
                } else {
                    List<VersionDto> versions1 = new ArrayList<>();
                    versions1 = versionRepository.findVersionList(searchVersion);
                    VersionDto version = versions1.get(versions1.size() - searchVersion.version);
                    versions.add(version);
                }
            }
        } else {
            if (searchVersionDto.version == null || searchVersionDto.version.equals("")) {
                versions = versionRepository.findVersionSort1(searchVersion);

            } else if (searchVersionDto.version.equals("0")) {
            } else {
                searchVersion.version = Integer.parseInt(searchVersionDto.version);
                if (searchVersion.version > versions.size()) {
                    searchVersion.formId = "";
                    versions = versionRepository.findVersionSort1(searchVersion);
                } else {
                    List<VersionDto> versions1 = new ArrayList<>();
                    versions1 = versionRepository.findVersionSort1(searchVersion);
                    VersionDto version = versions1.get(searchVersion.version - 1);
                    versions.add(version);
                }
            }
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

// Tạo dòng mới và truyền giá trị cho ô đã merge
            Row mergedRow = sheet.createRow(0); // Tạo dòng mới
            Cell mergedCell = mergedRow.createCell(0);
            mergedCell.setCellValue("QUẢN LÝ PHIÊN BẢN BIỂU MẪU");

// Tạo CellStyle để căn giữa in đậm và tăng kích cỡ chữ
            CellStyle mergedCellStyle = createCellStyle(workbook);
            mergedCellStyle.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
            Font mergedFont = workbook.createFont();
            mergedFont.setBold(true); // In đậm
            mergedFont.setFontHeightInPoints((short) 20); // Tăng kích cỡ chữ lên 14 điểm
            mergedCellStyle.setFont(mergedFont);
            mergedCell.setCellStyle(mergedCellStyle);
            Instant currentTime = Instant.now();
            LocalDate date = currentTime.atZone(ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Row timeRow = sheet.createRow(1); // Tạo dòng mới
            Cell timeCell = timeRow.createCell(0);
            timeCell.setCellValue("Xuất file excell vào: " + date.format(formatterTime)); // Đặt giá trị thời gian


            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            CellStyle timeCellStyle = createCellStyle(workbook);
            timeCellStyle.setAlignment(HorizontalAlignment.CENTER); // Căn giữa ngang
            timeCell.setCellStyle(timeCellStyle);


            Row headerRow = sheet.createRow(2);

            for (int col = 0; col < HEADERs_Version.length; col++) {
                Cell cell = headerRow.createCell(col);

                cell.setCellValue(HEADERs_Version[col]);
                Font font = workbook.createFont();
                font.setBold(true);

                // Tạo một CellStyle để căn giữa dữ liệu trong ô
                CellStyle style = workbook.createCellStyle();
                style.setAlignment(HorizontalAlignment.LEFT);
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setFont(font);
                style.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(style);
                cell.setCellStyle(style);
            }
            int[] columnWidths = {4000, 8000, 8000, 10000};
            for (int col = 0; col < columnWidths.length; col++) {
                sheet.setColumnWidth(col, columnWidths[col]);
            }

            int rowIdx = 3;
            int cnt = 1;
            for (VersionDto version : versions) {
                Row row = sheet.createRow(rowIdx++);

                // Tạo một CellStyle để căn giữa dữ liệu trong ô
                CellStyle style = workbook.createCellStyle();
                style.setBorderBottom(BorderStyle.THIN);
                style.setBorderTop(BorderStyle.THIN);
                style.setBorderRight(BorderStyle.THIN);
                style.setBorderLeft(BorderStyle.THIN);
                style.setAlignment(HorizontalAlignment.LEFT);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                CellStyle style1 = workbook.createCellStyle();
                style1.setBorderBottom(BorderStyle.THIN);
                style1.setBorderTop(BorderStyle.THIN);
                style1.setBorderRight(BorderStyle.THIN);
                style1.setBorderLeft(BorderStyle.THIN);
                style1.setAlignment(HorizontalAlignment.CENTER);

                UserInFoDetails currentUser = SecurityUtils.getInfoCurrentUserLogin();

                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                Date createdAt = inputDateFormat.parse(version.createdAt);
                String createdAtConvert = outputDateFormat.format(createdAt);


                row.createCell(0).setCellValue(version.numberVersion);
                row.createCell(1).setCellValue(createdAtConvert);
                row.createCell(2).setCellValue(currentUser.getLogin().trim());
                row.createCell(3).setCellValue(version.infoFix);


                // Đặt CellStyle cho các ô dữ liệu
                row.getCell(0).setCellStyle(style1);
                row.getCell(1).setCellStyle(style1);
                row.getCell(2).setCellStyle(style);
                row.getCell(3).setCellStyle(style);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static CellStyle createCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // Căn giữa chữ ngang trong ô
        style.setVerticalAlignment(VerticalAlignment.CENTER); // Căn giữa chữ dọc trong ô
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        return style;
    }

}
