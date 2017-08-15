package edu.monash.monplan.controller;

import edu.monash.monplan.model.Unit;
import edu.monash.monplan.service.UnitService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unit")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService){
        this.unitService = unitService;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    List<Unit> getAllUnits(){
        return unitService.listAllUnits();
    }

    @RequestMapping(path = "/{unitCode}", method = RequestMethod.GET)
    Unit getUnitByUnitCode(@PathVariable(value="unitCode") String unitCode){
        return unitService.getByUnitCode(unitCode);
    }

    @Async
    @RequestMapping(path = "/{unitCode}", method = RequestMethod.DELETE)
    void deleteByUnitCode(@PathVariable(value="unitCode") String unitCode){
        unitService.delete(unitCode);
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    Unit insertNewUnit(@RequestBody Unit unit){
        return unitService.addUnit(unit);
    }


}
