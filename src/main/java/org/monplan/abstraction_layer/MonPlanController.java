package org.monplan.abstraction_layer;

import edu.monash.monplan.controller.response.ResponseData;
import edu.monash.monplan.controller.response.ResponseDataWithPages;
import edu.monash.monplan.controller.response.ResponseMessage;
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

    public MonPlanService<T> getService() {
        return this.service;
    }

    public ResponseEntity create(T modelInstance) {
        try {
            return new ResponseEntity<>(this.service.create(modelInstance, allowDuplicateCodes), HttpStatus.OK);
        } catch (FailedOperationException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity getById(String id) {
        T match = service.getById(id);
        if (match == null) {
            return new ResponseEntity<>(
                    new ResponseMessage(
                            String.format("GET operation failed. id %s not found in datastore.", id)),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(match, HttpStatus.OK);
    }

    public ResponseEntity getByParams(String[] codes, String[] names) {
        return getByParams(codes, names, null, null);
    }

    public ResponseEntity getByParams(String[] codes, String[] names, Integer itemsPerPage, Integer pageNumber){
        // If no params, simply list all.
        List<T> results = new ArrayList<>();
        if (codes == null && names == null) {
            results = service.getAll();
        } else {
            // search by codes
            /*
                FIXME: Paginate at database level, and only do it if it is necessary
                Pagination should be done using limit and offset methods.
                Furthermore, we should only support fetching by codes and
                not by names. Finally, pagination is not necessary if codes
                have been specified (can set a maximum limit to prevent fetching
                too many unit codes if necessary).
             */
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
        if (itemsPerPage != null || pageNumber != null) {
            try {
                return new ResponseEntity<>(service.paginate(results, itemsPerPage, pageNumber), HttpStatus.OK);
            } catch (InsufficientResourcesException e) {
                return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.PRECONDITION_FAILED);
            } catch (FailedOperationException e) {
                return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(new ResponseData(results), HttpStatus.OK);
    }

    public ResponseEntity updateById(String id, T modelInstance) {
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

    public ResponseEntity<ResponseMessage> deleteById(String id){
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
