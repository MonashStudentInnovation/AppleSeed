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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    List<Unit> getUnits(@RequestParam(value="unitCode", required=false) String[] unitCodes,
                        @RequestParam(value="unitName", required=false) String[] unitNames){
        // If no query params, simply list all, otherwise list all by unitName.
        if (unitCodes == null && unitNames == null) {
            return unitService.listAllUnits();
        }

        // initialize the results to a set, because we only want unique units
        Set<String> seenUnitCodes = new HashSet<>();
        List<Unit> results = new ArrayList<>();
        if (unitCodes != null) {
            // for each given unitCode, find the matches for that
            for (String unitCode: unitCodes) {
                Unit unit = unitService.getUnitsByUnitCode(unitCode);
                // only add to results if unit is not null and if we have not seen this unit code
                if (unit != null && !seenUnitCodes.contains(unit.getUnitCode())) {
                    results.add(unit);
                    seenUnitCodes.add(unit.getUnitCode());
                }
            }
        }
        if (unitNames != null) {
            // for each given unitName, find the matches for that
            for (String unitName : unitNames) {
                List<Unit> matches = unitService.getUnitsByUnitName(unitName);
                for (Unit unit: matches) {
                    // only add to results if we have not seen this unit code
                    if (!seenUnitCodes.contains(unit.getUnitCode())) {
                        results.add(unit);
                        seenUnitCodes.add(unit.getUnitCode());
                    }
                }
            }
        }
        return results;
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