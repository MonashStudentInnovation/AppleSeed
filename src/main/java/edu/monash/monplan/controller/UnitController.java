package edu.monash.monplan.controller;

import edu.monash.monplan.controller.response.ResponseMessage;
import edu.monash.monplan.model.Unit;
import edu.monash.monplan.service.UnitService;
import org.monplan.NotFoundException;
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

    @RequestMapping(path = "", method = RequestMethod.GET)
    List<Unit> getAllUnits(){
        return unitService.listAllUnits();
    }

    @RequestMapping(path = "/{unitCode}", method = RequestMethod.GET)
    Object getUnitByUnitCode(@PathVariable(value="unitCode") String unitCode){
        Unit unit = unitService.getByUnitCode(unitCode);
        if (unit == null) {
            ResponseMessage message = new ResponseMessage();
            message.setMessage("Unit code not found");
            message.setCode(404);
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(unit, HttpStatus.OK);
    }

    @RequestMapping(path="/search/{searchItem}", method = RequestMethod.GET)
    List<Unit> getUnitByUnitName(@PathVariable(value = "searchItem") String searchItem){
        return unitService.getByName(searchItem);
    }

    @Async
    @RequestMapping(path = "/{unitId}", method = RequestMethod.DELETE)
    ResponseEntity<ResponseMessage> deleteByUnitCode(@PathVariable(value="unitId") String unitId){
        ResponseMessage message = new ResponseMessage();

        try {
            unitService.delete(unitId);
            message.setMessage("Delete operation success");
            message.setCode(200);

            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            message.setMessage(e.getLocalizedMessage());
            message.setCode(404);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    Unit insertNewUnit(@RequestBody Unit unit){
        return unitService.addUnit(unit);
    }

}
