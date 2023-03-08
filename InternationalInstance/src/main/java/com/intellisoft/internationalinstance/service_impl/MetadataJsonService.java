package com.intellisoft.internationalinstance.service_impl;

import com.intellisoft.internationalinstance.db.MetadataJson;

import java.net.URISyntaxException;
import java.util.List;

public interface MetadataJsonService {

    void saveMetadataJson(List<MetadataJson> metadataJson);
    List<MetadataJson> listPaginated();
    MetadataJson getMetadataJson(String id);
    MetadataJson updateMetadataJson(String id, MetadataJson metadataJson);
    void deleteMetadataJson(String id);

    void getMetadataData() throws URISyntaxException;
}
