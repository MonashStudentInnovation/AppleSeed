package edu.monash.monplan.controller;

import edu.monash.monplan.model.Unit;
import edu.monash.monplan.service.MonPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/units")
public class UnitController extends MonPlanController<Unit> {

    @Autowired
    public UnitController(MonPlanService<Unit> service) {
        super(service);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createUnit(@RequestBody Unit unit) {
        return this.create(unit);
    }

    @RequestMapping(path = "/{unitId}", method = RequestMethod.GET)
    public ResponseEntity getUnitById(@PathVariable(value="unitId") String unitId) {
        return this.getById(unitId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getUnitsByParams(@RequestParam(value="unitCode", required=false) String[] unitCodes,
                                           @RequestParam(value="unitName", required=false) String[] unitNames) {
        return this.getByParams(unitCodes, unitNames);
    }

    @RequestMapping(path = "/{unitId}", method = RequestMethod.PUT)
    public ResponseEntity updateUnitById(@PathVariable(value="unitId") String unitId,
                                         @RequestBody Unit unit) {
        return this.updateById(unitId, unit);
    }

    @RequestMapping(path = "/{unitId}", method = RequestMethod.DELETE)
    public ResponseEntity updateUnitById(@PathVariable(value="unitId") String unitId) {
        return this.deleteById(unitId);
    }
}
