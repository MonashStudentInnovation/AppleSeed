package com.monplan.utils;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.monplan.utils.TextSearch;


public class TextSearchTest {

    /**
     * Test that the inner for loop in the getSearchableText method is not needed and can be removed.
     */
    @Test
    public void searchesGetSameResults() {
        String testCourseName = "Introduction to the study of twelth century law and the effects of clean water\nidk why there is a new line :)";

        String searchResultUpdated = TextSearch.getSearchableText(testCourseName);
        String searchResultOld = TextSearch.getSearchableTextOld(testCourseName);
        assertEquals(searchResultUpdated, searchResultOld);
    }


}
