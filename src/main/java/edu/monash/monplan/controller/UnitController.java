package edu.monash.monplan.controller;

import edu.monash.monplan.model.Unit;
import edu.monash.monplan.service.UnitService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/unit")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService){
        this.unitService = unitService;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    Unit getUnitByUnitCode(@PathVariable String unitCode){
        return unitService.getByUnitCode(unitCode);
    }


    @RequestMapping(path = "", method = RequestMethod.POST)
    Unit insertNewUnit(@RequestBody Unit unit){
        return unitService.addUnit(unit);
    }
}
