package org.monplan.abstraction_layer;

import com.threewks.gaetools.logger.Logger;
import com.threewks.gaetools.objectify.repository.StringRepository;
import com.threewks.gaetools.search.Is;
import com.threewks.gaetools.search.Result;
import com.threewks.gaetools.search.Search;
import com.threewks.gaetools.search.gae.SearchConfig;
import edu.monash.monplan.model.Course;
import org.monplan.abstraction_layer.DataModel;

import java.util.ArrayList;
import java.util.List;

public class MonPlanRepository<T extends DataModel> extends StringRepository<T> {

    private String codeField;
    private String nameField;

    private static final int SEARCH_LIMIT = 1000;

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
        return this.search(name);
    }

    public List<T> getByCode(String code) {
        // TODO: Rewrite getByField to return .first() instead of .list()
        return this.getByField(codeField, code);
    }

    // TODO: Remove?
    public T create(T modelInstance) {
        return this.put(modelInstance);
    }

    public List<T> search(String name) {
        Search<T, String> search = createSearch(name);
        Result<T, String> result = search.run();
        if (result.getReturnedRecordCount() == SEARCH_LIMIT) {
            Logger.error("Search returned maximum records - dashboard will be missing items. Filters: " + name);
        }
        return new ArrayList<>(result.getResults());
    }

    private Search<T, String> createSearch(String name) {
        Search<T, String> search = search();
        search = search.limit(SEARCH_LIMIT);
        search = search.field("searchableText", Is.EqualTo, name.toUpperCase());

        return search;
    }
}
