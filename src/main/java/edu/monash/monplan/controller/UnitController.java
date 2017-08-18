package edu.monash.monplan.controller;

import edu.monash.monplan.controller.response.ResponseMessage;
import edu.monash.monplan.model.Unit;
import edu.monash.monplan.service.UnitService;
import org.monplan.exceptions.FailedOperationException;
import org.monplan.InsufficientResourcesException;
import org.monplan.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/units")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService){
        this.unitService = unitService;
    }

    // HTTP CREATE.

    @RequestMapping(path = "", method = RequestMethod.POST)
    ResponseEntity createUnit(@RequestBody Unit unit){
        try {
            return new ResponseEntity<>(unitService.createUnit(unit), HttpStatus.OK);
        } catch (FailedOperationException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // HTTP GET.

    @RequestMapping(path = "", method = RequestMethod.GET)
    List<Unit> getUnits(@RequestParam(value="unitName", required=false) String unitName){
        // If no query params, simply list all, otherwise list all by unitName.
        if (unitName == null) {
            return unitService.listAllUnits();
        } else {
            return unitService.getUnitsByUnitName(unitName);
        }
    }

    @RequestMapping(path = "/{unitCode}", method = RequestMethod.GET)
    ResponseEntity getUnitByUnitCode(@PathVariable(value="unitCode") String unitCode){
        Unit unit = unitService.getUnitsByUnitCode(unitCode);
        if (unit == null) {
            return new ResponseEntity<>(new ResponseMessage(
                    String.format("Unit code %s not found", unitCode)),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(unit, HttpStatus.OK);
    }

    // HTTP UPDATE.

    @RequestMapping(path = "/{unitId}", method = RequestMethod.PUT)
    ResponseEntity updateUnitByUnitId(@PathVariable(value="unitId") String unitId, @RequestBody Unit unit){
        try {
            unit.setId(unitId);
            Unit updatedUnit = unitService.updateUnitByUnitId(unit);
            return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
        } catch (InsufficientResourcesException e) {
            // This should never happen, but it might.
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.PRECONDITION_FAILED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    // HTTP DELETE.

    @Async
    @RequestMapping(path = "/{unitId}", method = RequestMethod.DELETE)
    ResponseEntity<ResponseMessage> deleteByUnitId(@PathVariable(value="unitId") String unitId){
        try {
            unitService.deleteUnit(unitId);
            return new ResponseEntity<>(new ResponseMessage("Delete operation success"), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (FailedOperationException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}