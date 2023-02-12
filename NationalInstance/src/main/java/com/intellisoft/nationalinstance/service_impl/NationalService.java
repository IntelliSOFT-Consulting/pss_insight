package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.DbDataEntry;
import com.intellisoft.nationalinstance.Results;

public interface NationalService {

    /**
     * Get all the organisation units related to the country
     * @return
     */
    Results getOrganisationUnits();

    /**
     * Get all added versions from the international instance
     * @param limitNo
     * @return
     */
    Results getVersions(int limitNo);

    /**
     * Save versions to the datastore
     * @param dataEntry
     * @return
     */
    Results saveVersions(DbDataEntry dataEntry);

}
