package edu.monash.monplan.service;

import edu.monash.monplan.model.DataModel;
import edu.monash.monplan.repository.MonPlanRepository;
import org.monplan.InsufficientResourcesException;
import org.monplan.exceptions.FailedOperationException;
import org.monplan.exceptions.NotFoundException;

import java.util.List;

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
