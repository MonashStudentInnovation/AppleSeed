package org.monplan.abstraction_layer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.threewks.gaetools.search.SearchIndex;
import org.monplan.utils.TextSearch;

import java.util.UUID;

public abstract class DataModel {

    /***
     * IMPORTANT: Declare the following fields in all subclasses
     * public static String nameField = "";
     * public static String codeField = "";
    ***/

    public abstract void setId(String id);

    public abstract String getId();


    public abstract String fetchCode();

    public abstract String fetchName();

    /**
     * Prevents accidentally re-initialising an object that is retrieved from the data layer.
     */
    public void init() {
        // Protects us from accidentally re-initialising an object that's retrieved from db
        this.setId(UUID.randomUUID().toString());
    }

    @JsonIgnore
    @SearchIndex
    public String getSearchableText() {
        return TextSearch.getSearchableText("searchableText");
    }

}
