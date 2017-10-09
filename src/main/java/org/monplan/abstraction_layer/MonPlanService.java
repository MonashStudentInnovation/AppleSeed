package org.monplan.abstraction_layer;

import edu.monash.monplan.controller.response.ResponseDataWithPages;
import org.monplan.exceptions.InsufficientResourcesException;
import org.monplan.exceptions.FailedOperationException;
import org.monplan.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.min;

public class MonPlanService<T extends DataModel> {

    private final MonPlanRepository<T> repository;

    public MonPlanService(MonPlanRepository<T> repository) {
        this.repository = repository;
    }

    public List<T> getByCode(String code) {
        return this.repository.getByCode(code);
    }

    public List<T> getAll() {
        return this.repository.getAll();
    }

    public List<T> getAll(int maxCount) {
        return this.repository.getAll(maxCount);
    }

    public List<T> getByName(String name) {
        return this.repository.getByName(name);
    }

    public T getById(String id) {
        return this.repository.getById(id);
    }

    private T save(final T modelInstance) {
        if (modelInstance.getId() == null) {
            modelInstance.init();
        }
        // TODO: Redo put?
        return this.repository.put(modelInstance);
    }

    public T create(T modelInstance) throws FailedOperationException {
        return this.create(modelInstance, false);
    }

    public T create(T modelInstance, boolean allowDuplicateCodes) throws FailedOperationException {
        if (modelInstance.getId() != null) {
            if (this.repository.getById(modelInstance.getId()) != null) {
                // Check if a model instance already has this id already exists in data layer.
                throw new FailedOperationException(String.format(
                        "CREATE operation failed: id %s is already in use.", modelInstance.getId()));
            }
        }

        if (!allowDuplicateCodes && modelInstance.fetchCode() != null) {
            // Check it does not already exist by code.
            if (this.repository.getByCode(modelInstance.fetchCode()).size() > 0) {
                // Check if a model instance already has this code already exists in data layer.
                throw new FailedOperationException(String.format(
                        "CREATE operation failed: code %s is already in use.", modelInstance.fetchCode()));
            }
        }
        // Save in data layer.
        T savedModel = this.save(modelInstance);

        // Check to see if saved properly.
        if (this.repository.get(savedModel.getId()) == null) {
            // Not saved properly.
            throw new FailedOperationException(String.format("CREATE operation failed: id %s could not be saved.", savedModel.getId()));
        }
        return savedModel;
    }

    public ResponseDataWithPages paginate(List<T> results, Integer itemsPerPage, Integer pageNumber) throws InsufficientResourcesException, FailedOperationException {
        // if not both provided, return error message
        if (itemsPerPage == null || pageNumber == null) {
            throw new InsufficientResourcesException("GET operation failed: both itemsPerPage and pageNumber must be specified.");
        }
        if (itemsPerPage <= 0) {
            throw new FailedOperationException("GET operation failed: itemsPerPage must be a positive integer");
        }
        if (pageNumber <= 0) {
            throw new FailedOperationException("GET operation failed: pageNumber must be a positive integer.");
        }

        // this means itemsPerPage and pageNumber was specified
        int N = results.size();
        int startIndex = (pageNumber-1)*itemsPerPage;
        // we can simply limit endIndex to N
        int endIndex = min(N, pageNumber*itemsPerPage);

        int totalPages = (N + itemsPerPage-1)/itemsPerPage;

        // ensure that we do not access outside the list
        if (startIndex >= N) {
            return new ResponseDataWithPages(new ArrayList<>(), totalPages);
        }

        List<T> pagedResults = results.subList(startIndex, endIndex);

        return new ResponseDataWithPages(pagedResults, totalPages);
    }

    public T updateById(T modelInstance) throws InsufficientResourcesException, NotFoundException, FailedOperationException {
        return updateById(modelInstance, false);
    }

    public T updateById(T modelInstance, boolean allowDuplicateCodes) throws InsufficientResourcesException, NotFoundException, FailedOperationException {
        if (modelInstance.getId() == null) {
            throw new InsufficientResourcesException("UPDATE operation failed: id not provided.");
        }

        if (this.repository.getById(modelInstance.getId()) == null) {
            throw new NotFoundException(String.format(
                    "UPDATE operation failed. id %s was not found in datastore.", modelInstance.getId()));
        }

        if (!allowDuplicateCodes && modelInstance.fetchCode() != null) {
            if (this.repository.getByCode(modelInstance.fetchCode()).size() > 0) {
                // Check if a model instance already has this code already exists in data layer.
                throw new FailedOperationException(String.format(
                        "CREATE operation failed: code %s is already in use.", modelInstance.fetchCode()));
            }
        }

        // TODO: redo put?
        return this.repository.put(modelInstance);
    }

    public void deleteById(String id) throws NotFoundException, FailedOperationException {
        T modelInstance = this.repository.get(id);
        if (modelInstance == null) {
            throw new NotFoundException(
                    String.format("DELETE operation failed. id %s not found in datastore, could not delete.", id));
        }
        // Delete from data layer.
        this.repository.deleteByKey(id);

        // Check to see if deleted properly.
        if (this.repository.getById(id) != null) {
            throw new FailedOperationException(
                    String.format("DELETE operation failed. Could not delete id %s in datastore.", id));

        }
    }

}
