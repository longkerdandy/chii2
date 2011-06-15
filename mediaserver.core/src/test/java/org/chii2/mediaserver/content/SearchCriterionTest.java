package org.chii2.mediaserver.content;

import org.chii2.mediaserver.api.upnp.SearchCriterion;
import org.testng.annotations.Test;

/**
 * SearchCriterion Test
 */
public class SearchCriterionTest {

    @Test
    public void parseSearchCriterionTesT() {
        SearchCriterion searchCriterion = SearchCriterion.parseSearchCriterion("upnp:class derivedfrom \"object.item.imageItem\" and @refID exists false");
        assert SearchCriterion.SearchType.SEARCH_IMAGE == searchCriterion.getSearchType();
    }
}
