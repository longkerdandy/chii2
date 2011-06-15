package org.chii2.mediaserver.api.upnp;

import org.apache.commons.lang.StringUtils;

/**
 * Search Criterion
 */
public class SearchCriterion {
    // Search Type
    private SearchType searchType;

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    /**
     * Parse Search Criterion from String
     * TODO: Not Completed
     *
     * @param searchCriterionString Search Criterion String
     * @return Search Criterion
     */
    public static SearchCriterion parseSearchCriterion(String searchCriterionString) {
        SearchCriterion searchCriterion = new SearchCriterion();
        if (searchCriterionString != null) {
            String[] factors = searchCriterionString.split("(and|or)");
            for (String factor : factors) {
                factor = StringUtils.trimToEmpty(factor);
                String[] subFactors = factor.split("\\s", 3);
                if (subFactors != null && subFactors.length == 3) {
                    if ("upnp:class".equalsIgnoreCase(subFactors[0]) && ("=".equalsIgnoreCase(subFactors[1]) || "derivedfrom".equalsIgnoreCase(subFactors[1]))) {
                        if ("\"object.item.imageItem\"".equalsIgnoreCase(subFactors[2]) || "\"object.item.imageItem.photo\"".equalsIgnoreCase(subFactors[2])) {
                            searchCriterion.setSearchType(SearchCriterion.SearchType.SEARCH_IMAGE);
                        } else if ("\"object.item.videoItem\"".equalsIgnoreCase(subFactors[2])) {
                            searchCriterion.setSearchType(SearchCriterion.SearchType.SEARCH_VIDEO);
                        } else if ("\"object.container.playlistContainer\"".equalsIgnoreCase(subFactors[2])) {
                            searchCriterion.setSearchType(SearchCriterion.SearchType.SEARCH_PLAYLIST);
                        } else {
                            searchCriterion.setSearchType(SearchCriterion.SearchType.SEARCH_UNKNOWN);
                        }
                    }
                }
            }
        }
        return searchCriterion;
    }

    /**
     * Search Type
     * TODO: Not Completed
     */
    public enum SearchType {
        SEARCH_IMAGE,
        SEARCH_VIDEO,
        SEARCH_PLAYLIST,
        SEARCH_UNKNOWN
    }
}
