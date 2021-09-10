package org.cxw.vhr.utils;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.cxw.vhr.model.*;
import org.cxw.vhr.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class POIUtils {
    //private static Logger logger = LoggerFactory.getLogger(POIUtils.class);
    private static EmployeeService employeeService;

    @Autowired
    private EmployeeService empService;

    @PostConstruct
    public void init() {
        employeeService = empService;
    }

    public static ResponseEntity<byte[]> employeeToExcel(List<Employee> allEmployees) {
        //1.创建excel文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2.创建文档摘要
        workbook.createInformationProperties();
        //3.获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("员工信息");
        //文档管理员
        docInfo.setManager("cxw");
        //公司信息
        docInfo.setCompany("个人");
        //4.获取并配置文档摘要信息
        SummaryInformation sumInfo = workbook.getSummaryInformation();
        //文档标题
        sumInfo.setTitle("员工信息表");
        //文档作者
        sumInfo.setAuthor("cxw");
        //文档备注
        sumInfo.setComments("本文档由 cxw 提供");
        //5.创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //创建日期的样式
        HSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        //设置列的宽度
        HSSFSheet sheet = workbook.createSheet("员工信息表");
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 12 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 5 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 16 * 256);
        sheet.setColumnWidth(10, 12 * 256);
        sheet.setColumnWidth(11, 25 * 256);
        sheet.setColumnWidth(12, 15 * 256);
        sheet.setColumnWidth(13, 20 * 256);
        sheet.setColumnWidth(14, 16 * 256);
        sheet.setColumnWidth(15, 14 * 256);
        sheet.setColumnWidth(16, 14 * 256);
        sheet.setColumnWidth(17, 12 * 256);
        sheet.setColumnWidth(18, 8 * 256);
        sheet.setColumnWidth(19, 20 * 256);
        sheet.setColumnWidth(20, 20 * 256);
        sheet.setColumnWidth(21, 15 * 256);
        sheet.setColumnWidth(22, 15 * 256);
        sheet.setColumnWidth(23, 15 * 256);
        sheet.setColumnWidth(24, 15 * 256);
        sheet.setColumnWidth(25, 14 * 256);

        //6.创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("编号");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellValue("姓名");
        c1.setCellStyle(headerStyle);
        HSSFCell c2 = r0.createCell(2);
        c2.setCellValue("工号");
        c2.setCellStyle(headerStyle);
        HSSFCell c3 = r0.createCell(3);
        c3.setCellValue("在职状态");
        c3.setCellStyle(headerStyle);
        HSSFCell c4 = r0.createCell(4);
        c4.setCellValue("性别");
        c4.setCellStyle(headerStyle);
        HSSFCell c5 = r0.createCell(5);
        c5.setCellValue("出生日期");
        c5.setCellStyle(headerStyle);
        HSSFCell c6 = r0.createCell(6);
        c6.setCellValue("身份证号码");
        c6.setCellStyle(headerStyle);
        HSSFCell c7 = r0.createCell(7);
        c7.setCellValue("婚姻状况");
        c7.setCellStyle(headerStyle);
        HSSFCell c8 = r0.createCell(8);
        c8.setCellValue("民族");
        c8.setCellStyle(headerStyle);
        HSSFCell c9 = r0.createCell(9);
        c9.setCellValue("籍贯");
        c9.setCellStyle(headerStyle);
        HSSFCell c10 = r0.createCell(10);
        c10.setCellValue("政治面貌");
        c10.setCellStyle(headerStyle);
        HSSFCell c11 = r0.createCell(11);
        c11.setCellValue("电子邮箱");
        c11.setCellStyle(headerStyle);
        HSSFCell c12 = r0.createCell(12);
        c12.setCellValue("电话号码");
        c12.setCellStyle(headerStyle);
        HSSFCell c13 = r0.createCell(13);
        c13.setCellValue("联系地址");
        c13.setCellStyle(headerStyle);
        HSSFCell c14 = r0.createCell(14);
        c14.setCellValue("所属部门");
        c14.setCellStyle(headerStyle);
        HSSFCell c15 = r0.createCell(15);
        c15.setCellValue("职位");
        c15.setCellStyle(headerStyle);
        HSSFCell c16 = r0.createCell(16);
        c16.setCellValue("职称");
        c16.setCellStyle(headerStyle);
        HSSFCell c17 = r0.createCell(17);
        c17.setCellValue("聘用形式");
        c17.setCellStyle(headerStyle);
        HSSFCell c18 = r0.createCell(18);
        c18.setCellValue("最高学历");
        c18.setCellStyle(headerStyle);
        HSSFCell c19 = r0.createCell(19);
        c19.setCellValue("毕业院校");
        c19.setCellStyle(headerStyle);
        HSSFCell c20 = r0.createCell(20);
        c20.setCellValue("专业名称");
        c20.setCellStyle(headerStyle);
        HSSFCell c21 = r0.createCell(21);
        c21.setCellValue("入职日期");
        c21.setCellStyle(headerStyle);
        HSSFCell c22 = r0.createCell(22);
        c22.setCellValue("转正日期");
        c22.setCellStyle(headerStyle);
        HSSFCell c23 = r0.createCell(23);
        c23.setCellValue("合同起始日期");
        c23.setCellStyle(headerStyle);
        HSSFCell c24 = r0.createCell(24);
        c24.setCellValue("合同终止日期");
        c24.setCellStyle(headerStyle);
        HSSFCell c25 = r0.createCell(25);
        c25.setCellValue("合同期限(年)");
        c25.setCellStyle(headerStyle);

        for (int i = 0; i < allEmployees.size(); i++) {
            Employee employee = allEmployees.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(employee.getId());
            if (employee.getName() != null) {
                row.createCell(1).setCellValue(employee.getName());
            }
            if (employee.getWorkID() != null) {
                row.createCell(2).setCellValue(employee.getWorkID());
            }
            if (employee.getWorkState() != null) {
                row.createCell(3).setCellValue(employee.getWorkState());
            }
            if (employee.getGender() != null) {
                row.createCell(4).setCellValue(employee.getGender());
            }
            if (employee.getBirthday() != null) {
                HSSFCell cell5 = row.createCell(5);
                cell5.setCellValue(employee.getBirthday());
                cell5.setCellStyle(dateStyle);
            }
            if (employee.getIdCard() != null) {
                row.createCell(6).setCellValue(employee.getIdCard());
            }
            if (employee.getWedlock() != null) {
                row.createCell(7).setCellValue(employee.getWedlock());
            }
            if (employee.getNationId() != null) {
                row.createCell(8).setCellValue(employee.getNation().getName());
            }
            if (employee.getNativePlace() != null) {
                row.createCell(9).setCellValue(employee.getNativePlace());
            }
            if (employee.getPoliticId() != null) {
                row.createCell(10).setCellValue(employee.getPoliticsstatus().getName());
            }
            if (employee.getEmail() != null) {
                row.createCell(11).setCellValue(employee.getEmail());
            }
            if (employee.getPhone() != null) {
                row.createCell(12).setCellValue(employee.getPhone());
            }
            if (employee.getAddress() != null) {
                row.createCell(13).setCellValue(employee.getAddress());
            }
            if (employee.getDepartmentId() != null) {
                row.createCell(14).setCellValue(employee.getDepartment().getName());
            }
            if (employee.getPosId() != null) {
                row.createCell(15).setCellValue(employee.getPosition().getName());
            }
            if (employee.getJobLevelId() != null) {
                row.createCell(16).setCellValue(employee.getJobLevel().getName());
            }
            if (employee.getEngageForm() != null) {
                row.createCell(17).setCellValue(employee.getEngageForm());
            }
            if (employee.getTiptopDegree() != null) {
                row.createCell(18).setCellValue(employee.getTiptopDegree());
            }
            if (employee.getSchool() != null) {
                row.createCell(19).setCellValue(employee.getSchool());
            }
            if (employee.getSpecialty() != null) {
                row.createCell(20).setCellValue(employee.getSpecialty());
            }
            if (employee.getBeginDate() != null) {
                HSSFCell cell21 = row.createCell(21);
                cell21.setCellValue(employee.getBeginDate());
                cell21.setCellStyle(dateStyle);
            }
            if (employee.getConversionTime() != null) {
                HSSFCell cell22 = row.createCell(22);
                cell22.setCellValue(employee.getConversionTime());
                cell22.setCellStyle(dateStyle);
            }
            if (employee.getBeginContract() != null) {
                HSSFCell cell23 = row.createCell(23);
                cell23.setCellValue(employee.getBeginContract());
                cell23.setCellStyle(dateStyle);
            }
            if (employee.getEndContract() != null) {
                HSSFCell cell24 = row.createCell(24);
                cell24.setCellValue(employee.getEndContract());
                cell24.setCellStyle(dateStyle);
            }
            if (employee.getContractTerm() != null) {
                row.createCell(25).setCellValue(employee.getContractTerm());
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            workbook.write(byteArrayOutputStream);
            headers.setContentDispositionFormData("attachment", new String("员工表导出.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
    }

    public static ResponseEntity<byte[]> trainToExcel(List<Employeetrain> allTrains) {
        //1.创建excel文档
        HSSFWorkbook workbook = new HSSFWorkbook();
        //2.创建文档摘要
        workbook.createInformationProperties();
        //3.获取并配置文档信息
        DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
        //文档类别
        docInfo.setCategory("员工培训");
        //文档管理员
        docInfo.setManager("cxw");
        //公司信息
        docInfo.setCompany("个人");
        //4.获取并配置文档摘要信息
        SummaryInformation sumInfo = workbook.getSummaryInformation();
        //文档标题
        sumInfo.setTitle("员工培训记录");
        //文档作者
        sumInfo.setAuthor("cxw");
        //文档备注
        sumInfo.setComments("本文档由 cxw 提供");
        //5.创建样式
        //创建标题行的样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //创建日期的样式
        HSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        //设置列的宽度
        HSSFSheet sheet = workbook.createSheet("员工培训记录");
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 28 * 256);
        sheet.setColumnWidth(2, 28 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);

        //6.创建标题行
        HSSFRow r0 = sheet.createRow(0);
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("编号");
        c0.setCellStyle(headerStyle);
        HSSFCell c1 = r0.createCell(1);
        c1.setCellValue("培训内容");
        c1.setCellStyle(headerStyle);
        HSSFCell c2 = r0.createCell(2);
        c2.setCellValue("培训备注");
        c2.setCellStyle(headerStyle);
        HSSFCell c3 = r0.createCell(3);
        c3.setCellValue("培训时间");
        c3.setCellStyle(headerStyle);
        HSSFCell c4 = r0.createCell(4);
        c4.setCellValue("员工姓名");
        c4.setCellStyle(headerStyle);
        HSSFCell c5 = r0.createCell(5);
        c5.setCellValue("员工工号");
        c5.setCellStyle(headerStyle);
        HSSFCell c6 = r0.createCell(6);
        c6.setCellValue("所属部门");
        c6.setCellStyle(headerStyle);

        for (int i = 0; i < allTrains.size(); i++) {
            Employeetrain employeetrain = allTrains.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            if (employeetrain.getId() != null) {
                row.createCell(0).setCellValue(employeetrain.getId());
            }
            if (employeetrain.getTrainContent() != null) {
                row.createCell(1).setCellValue(employeetrain.getTrainContent());
            }
            if (employeetrain.getRemark() != null) {
                row.createCell(2).setCellValue(employeetrain.getRemark());
            }
            if (employeetrain.getTrainDate() != null) {
                HSSFCell cell3 = row.createCell(3);
                cell3.setCellValue(employeetrain.getTrainDate());
                cell3.setCellStyle(dateStyle);
            }
            if (employeetrain.getEmployee() != null) {
                if (employeetrain.getEmployee().getName() != null) {
                    row.createCell(4).setCellValue(employeetrain.getEmployee().getName());
                }
                if (employeetrain.getEmployee().getWorkID() != null) {
                    row.createCell(5).setCellValue(employeetrain.getEmployee().getWorkID());
                }
                if (employeetrain.getEmployee().getDepartment() != null && employeetrain.getEmployee().getDepartment().getName() != null) {
                    row.createCell(6).setCellValue(employeetrain.getEmployee().getDepartment().getName());
                }
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HttpHeaders headers = new HttpHeaders();
        try {
            workbook.write(byteArrayOutputStream);
            headers.setContentDispositionFormData("attachment", new String("员工培训记录导出.xls".getBytes("UTF-8"), "ISO-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.CREATED);
    }

    /**
     * excel解析成员工集合
     *
     * @param file
     * @param allNations
     * @param allPoliticsstatus
     * @param allDepartments
     * @param allPositions
     * @param allJobLevels
     * @return
     */
    public static List<Employee> excelToEmployee(MultipartFile file, List<Nation> allNations, List<Politicsstatus> allPoliticsstatus, List<Department> allDepartments, List<Position> allPositions, List<JobLevel> allJobLevels) {
        List<Employee> employees = new ArrayList<>();
        Employee employee = null;
        try {
            //1.创建一个workbook对象
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            //2.遍历表单
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                //3.遍历表单的行
                for (int r = 0; r < sheet.getPhysicalNumberOfRows(); r++) {
                    if (r == 0) {
                        continue;
                    }
                    HSSFRow row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }
                    //遍历行的单元格
                    employee = new Employee();
                    for (int c = 0; c < row.getPhysicalNumberOfCells(); c++) {
                        HSSFCell cell = row.getCell(c);
                        if (cell == null) {
                            continue;
                        }
                        switch (cell.getCellTypeEnum()) {
                            case STRING:
                                String s = cell.getStringCellValue();
                                switch (c) {
                                    case 1:
                                        employee.setName(s);
                                        break;
                                    case 2:
                                        employee.setWorkID(s);
                                        break;
                                    case 3:
                                        employee.setWorkState(s);
                                        break;
                                    case 4:
                                        employee.setGender(s);
                                        break;
                                    case 6:
                                        employee.setIdCard(s);
                                        break;
                                    case 7:
                                        employee.setWedlock(s);
                                        break;
                                    case 8:
                                        int nationIndex = allNations.indexOf(new Nation(s));
                                        employee.setNationId(allNations.get(nationIndex).getId());
                                        break;
                                    case 9:
                                        employee.setNativePlace(s);
                                        break;
                                    case 10:
                                        int politicIndex = allPoliticsstatus.indexOf(new Politicsstatus(s));
                                        employee.setPoliticId(allPoliticsstatus.get(politicIndex).getId());
                                        break;
                                    case 11:
                                        employee.setEmail(s);
                                        break;
                                    case 12:
                                        employee.setPhone(s);
                                        break;
                                    case 13:
                                        employee.setAddress(s);
                                        break;
                                    case 14:
                                        int departmentIndex = allDepartments.indexOf(new Department(s));
                                        employee.setDepartmentId(allDepartments.get(departmentIndex).getId());
                                        break;
                                    case 15:
                                        int positionIndex = allPositions.indexOf(new Position(s));
                                        employee.setPosId(allPositions.get(positionIndex).getId());
                                        break;
                                    case 16:
                                        int jobLevelIndex = allJobLevels.indexOf(new JobLevel(s));
                                        employee.setJobLevelId(allJobLevels.get(jobLevelIndex).getId());
                                        break;
                                    case 17:
                                        employee.setEngageForm(s);
                                        break;
                                    case 18:
                                        employee.setTiptopDegree(s);
                                        break;
                                    case 19:
                                        employee.setSchool(s);
                                        break;
                                    case 20:
                                        employee.setSpecialty(s);
                                        break;
                                }
                                break;
                            default:
                                switch (c) {
                                    case 5:
                                        employee.setBirthday(cell.getDateCellValue());
                                        break;
                                    case 21:
                                        employee.setBeginDate(cell.getDateCellValue());
                                        break;
                                    case 22:
                                        employee.setConversionTime(cell.getDateCellValue());
                                        break;
                                    case 23:
                                        employee.setBeginContract(cell.getDateCellValue());
                                        break;
                                    case 24:
                                        employee.setEndContract(cell.getDateCellValue());
                                        break;
                                    case 25:
                                        employee.setContractTerm(cell.getNumericCellValue());
                                        break;
                                }
                                break;
                        }
                    }
                    employees.add(employee);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public static List<Employeetrain> excelToTrain(MultipartFile file) {
        List<Employeetrain> trains = new ArrayList<>();
        Employeetrain train = null;
        try {
            //1.创建一个workbook对象
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            //2.遍历表单
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                //3.遍历表单的行
                for (int r = 0; r < sheet.getPhysicalNumberOfRows(); r++) {
                    if (r == 0) {
                        continue;
                    }
                    HSSFRow row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }
                    //遍历行的单元格
                    train = new Employeetrain();
                    for (int c = 0; c < row.getPhysicalNumberOfCells(); c++) {
                        HSSFCell cell = row.getCell(c);
                        if (cell == null) {
                            continue;
                        }
                        switch (cell.getCellTypeEnum()) {
                            case STRING:
                                String s = cell.getStringCellValue();
                                switch (c) {
                                    case 1:
                                        train.setTrainContent(s);
                                        break;
                                    case 2:
                                        train.setRemark(s);
                                        break;
                                    case 5:
                                        Employee employee = employeeService.getEmployeeByWorkID(s);
                                        if (employee != null) {
                                            train.setEid(employee.getId());
                                        }
                                        break;
                                }
                                break;
                            default:
                                switch (c) {
                                    case 3:
                                        train.setTrainDate(cell.getDateCellValue());
                                        break;
                                }
                                break;
                        }
                    }
                    trains.add(train);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trains;
    }
}
