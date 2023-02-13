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
     * Get all data elements related to the version
     */
    Results getVersionDataElements(String version);

    /**
     * Save versions to the datastore
     * We're assuming this backend was deployed on the same server as the DHIS2 National Instance
     * @param dataEntry
     * @return
     */
    Results saveVersions(DbDataEntry dataEntry);

}
