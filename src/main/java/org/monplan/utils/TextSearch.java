package org.monplan.utils;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class TextSearch {
    private static final Pattern SPACE_SEPARATOR_PATTERN = Pattern.compile("\\s+");

    /**
     * Generates all substring permutations of one or more input strings, intended to be used as a search index to facilitate partial string searching.
     * See unit test and/or monash-thesis-submission Submission.getSearchableText() and SubmissionRepository.search() for an example of how this is used and searched on
     *
     * NOTE: To allow searches to firstly match on partial or full term including spaces the first thing we do is take the whole string and all permutations down from it. Then we do words.
     *
     */
    public static String getSearchableText(String... strings) {
        Set<String> allUppercaseSubstrings = new LinkedHashSet<>();

        for (String sourceString : strings) {
            String string = StringUtils.upperCase(StringUtils.trimToNull(sourceString));
            if (string != null) {
                allUppercaseSubstrings.addAll(getAllSubstrings(string));
            }
        }
        return Joiner.on(" ").join(allUppercaseSubstrings);
    }

    private static Set<String> getAllSubstrings(String str) {
        Set<String> substrings = new LinkedHashSet<>();
        for (int length = 1; length <= str.length(); length++) {

            for (int start = 0; start + length <= str.length(); start++) {
                String subString = str.substring(start, start + length).trim();
                if (StringUtils.isNotEmpty(subString)) {
                    substrings.add(subString);
                }
            }

        }

        return substrings;
    }

    public static String getSearchableTextOld(String... strings) {
        Set<String> allUppercaseSubstrings = new LinkedHashSet<>();

        for (String sourceString : strings) {
            String string = StringUtils.upperCase(StringUtils.trimToNull(sourceString));
            if (string != null) {

                // Firstly get all substrings including spaces so we get search term match as it is typed
                allUppercaseSubstrings.addAll(getAllSubstrings(string));

                // Now do the same per word so you can search for surname only for example
                for (String word : SPACE_SEPARATOR_PATTERN.split(string)) {
                    Set<String> upperCaseSubstrings = getAllSubstrings(word.toUpperCase());
                    allUppercaseSubstrings.addAll(upperCaseSubstrings);
                }
            }
        }
        return Joiner.on(" ").join(allUppercaseSubstrings);
    }


}