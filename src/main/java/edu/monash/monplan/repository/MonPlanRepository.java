package edu.monash.monplan.repository;

import com.threewks.gaetools.objectify.repository.StringRepository;
import com.threewks.gaetools.search.gae.SearchConfig;
import edu.monash.monplan.model.DataModel;

import java.util.List;

public class MonPlanRepository<T extends DataModel> extends StringRepository<T> {

    private String codeField;
    private String nameField;

    // TODO: Make sure this works.
    public MonPlanRepository(Class<T> classType, SearchConfig searchConfig, String codeField, String nameField) {
        super(classType, searchConfig);

        this.codeField = codeField;
        this.nameField = nameField;
    }

    /**
     * Returns a list of objects idk concrete implementations of DataModel.
     * @return
     */
    public List<T> getAll() {
        return this.getAll(5000);
    }

    public List<T> getAll(int maxCount) {
        return this.list(maxCount);
    }

    public T getById(String id) {
        return this.get(id);
    }

    public List<T> getByName(String name) {
        return this.getByField(nameField, name);
    }

    public List<T> getByCode(String code) {
        return this.getByField(codeField, code);
    }

    // TODO: Remove?
    public T create(T modelInstance) {
        return this.put(modelInstance);
    }


    public T update(T modelInstance) {
        return modelInstance;
    }

}
