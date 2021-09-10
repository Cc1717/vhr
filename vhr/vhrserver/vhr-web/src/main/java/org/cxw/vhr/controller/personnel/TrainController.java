package org.cxw.vhr.controller.personnel;

import org.cxw.vhr.model.Employee;
import org.cxw.vhr.model.Employeetrain;
import org.cxw.vhr.model.RespBean;
import org.cxw.vhr.model.RespPageBean;
import org.cxw.vhr.service.EmployeetrainService;
import org.cxw.vhr.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/personnel/train")
public class TrainController {
    @Autowired
    EmployeetrainService employeetrainService;

    @GetMapping("/")
    public RespPageBean getTrainsByPage(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        Employee employee, Date[] timeScope) {
        return employeetrainService.getTrainsByPage(page, size, employee, timeScope);
    }

    @DeleteMapping("/")
    public RespBean deleteTrainsByIds(Integer[] ids) {
        if (employeetrainService.deleteTrainsByIds(ids)) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteTrainById(@PathVariable Integer id) {
        if (employeetrainService.deleteTrainById(id) == 1) {
            return RespBean.ok("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() {
        List<Employeetrain> allTrains = (List<Employeetrain>) employeetrainService.getTrainsByPage(null, null, null, null).getData();
        return POIUtils.trainToExcel(allTrains);
    }

    @PostMapping("/import")
    public RespBean importData(MultipartFile file) {
        List<Employeetrain> trains = POIUtils.excelToTrain(file);
        if (employeetrainService.addTrains(trains) == trains.size()) {
            return RespBean.ok("导入成功!");
        }
        return RespBean.error("导入失败!");
    }
}
