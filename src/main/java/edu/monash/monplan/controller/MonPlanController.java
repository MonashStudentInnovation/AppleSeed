package edu.monash.monplan.controller;

import edu.monash.monplan.controller.response.ResponseData;
import edu.monash.monplan.controller.response.ResponseMessage;
import edu.monash.monplan.model.DataModel;
import edu.monash.monplan.service.MonPlanService;
import org.monplan.exceptions.InsufficientResourcesException;
import org.monplan.exceptions.FailedOperationException;
import org.monplan.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.min;

public class MonPlanController<T extends DataModel> {

    private final MonPlanService<T> service;
    private boolean allowDuplicateCodes = false;

    public MonPlanController(MonPlanService<T> service) {
        this.service = service;
    }

    public MonPlanController(MonPlanService<T> service, boolean allowDuplicateCodes) {
        this.service = service;
        this.allowDuplicateCodes = allowDuplicateCodes;
    }

    ResponseEntity create(T modelInstance) {
        try {
            return new ResponseEntity<>(this.service.create(modelInstance, allowDuplicateCodes), HttpStatus.OK);
        } catch (FailedOperationException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    ResponseEntity getById(String id) {
        T match = service.getById(id);
        if (match == null) {
            return new ResponseEntity<>(
                    new ResponseMessage(
                            String.format("GET operation failed. id %s not found in datastore.", id)),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(match, HttpStatus.OK);
    }

    ResponseEntity getByParams(String[] codes, String[] names) {
        return getByParams(codes, names, null, null);
    }

    ResponseEntity getByParams(String[] codes, String[] names, Integer itemsPerPage, Integer pageNumber){
        // If no params, simply list all.
        List<T> results = new ArrayList<>();
        if (codes == null && names == null) {
            results = service.getAll();
        } else {
            // search by codes
            Set<String> seenIds = new HashSet<>();
            if (codes != null) {
                // for each given code, find the matches for that
                for (String code: codes) {
                    List<T> matches = service.getByCode(code);
                    // add matches if we have not seen them
                    for (T match : matches) {
                        if (!seenIds.contains(match.getId())) {
                            results.add(match);
                            seenIds.add(match.getId());
                        }
                    }
                }
            }
            if (names != null) {
                // for each given code, find the matches for that
                for (String name : names) {
                    List<T> matches = service.getByName(name);
                    // add matches if we have not seen them
                    for (T match : matches) {
                        if (!seenIds.contains(match.getId())) {
                            results.add(match);
                            seenIds.add(match.getId());
                        }
                    }
                }
            }
        }

        // check if itemsPerPage and pageNumber was specified
        if (itemsPerPage != null && pageNumber != null && pageNumber >= 1) {
            // this means itemsPerPage and pageNumber was specified

            int N = results.size();
            int startIndex = (pageNumber-1)*itemsPerPage;
            // we can simply limit endIndex to N
            int endIndex = min(N, pageNumber*itemsPerPage);

            // ensure that we do not access outside the list
            if (startIndex >= N) {
                return new ResponseEntity<>(new ResponseData(new ArrayList<>()), HttpStatus.OK);
            }

            List<T> pagedResults = results.subList(startIndex, endIndex);

            return new ResponseEntity<>(new ResponseData(pagedResults), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ResponseData(results), HttpStatus.OK);
    }

    ResponseEntity updateById(String id, T modelInstance) {
        try {
            modelInstance.setId(id);
            service.updateById(modelInstance, allowDuplicateCodes);

            // check if updateById was successful
            T instanceWithId = service.getById(id);

            // TODO: compare with input

            return new ResponseEntity<>(instanceWithId, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (InsufficientResourcesException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.PRECONDITION_FAILED);
        } catch (FailedOperationException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    ResponseEntity<ResponseMessage> deleteById(String id){
        try {
            service.deleteById(id);
            return new ResponseEntity<>(
                    new ResponseMessage(
                            String.format("DELETE operation success: id %s has been deleted", id)),
                    HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (FailedOperationException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
