package hello.controller;

import hello.controller.request.MessageTaskRequest;
import hello.model.Unit;
import hello.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UnitController {


    private final UnitService unitService;

    @Autowired
    public UnitController(UnitService unitService){
        this.unitService = unitService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/unit/{unitCode}")
    Unit getUnitByUnitCode(@PathVariable String unitCode){
        return unitService.getByUnitCode(unitCode);
    }
}
