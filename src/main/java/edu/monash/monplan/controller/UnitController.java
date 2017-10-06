package edu.monash.monplan.controller;

import edu.monash.monplan.model.Unit;
import org.monplan.abstraction_layer.MonPlanService;
import org.monplan.abstraction_layer.MonPlanController;
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

    // @RequestBody will serialize the body of the POST request into a unit object.
    // Fields in the POST body that don't exist in unit will not be taken and fields in unit may be null.
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
                                           @RequestParam(value="unitName", required=false) String[] unitNames,
                                           @RequestParam(value="itemsPerPage", required=false) Integer itemsPerPage,
                                           @RequestParam(value="pageNumber", required=false) Integer pageNumber) {
        return this.getByParams(unitCodes, unitNames, itemsPerPage, pageNumber);
    }

    @RequestMapping(path = "/{unitId}", method = RequestMethod.PUT)
    public ResponseEntity updateUnitById(@PathVariable(value="unitId") String unitId,
                                         @RequestBody Unit unit) {
        return this.updateById(unitId, unit);
    }

    @RequestMapping(path = "/{unitId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUnitById(@PathVariable(value="unitId") String unitId) {
        return this.deleteById(unitId);
    }
}
