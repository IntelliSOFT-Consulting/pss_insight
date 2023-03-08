package com.intellisoft.nationalinstance.service_impl;


import com.intellisoft.nationalinstance.db.MetadataJson;

import java.net.URISyntaxException;
import java.util.List;

public interface MetadataJsonService {

    void saveMetadataJson(List<MetadataJson> metadataJson);
    List<MetadataJson> listPaginated();
    MetadataJson getMetadataJson(String id);
    MetadataJson getMetadataJsonByCode(String code);
    MetadataJson updateMetadataJson(String id, MetadataJson metadataJson);
    void deleteMetadataJson(String id);

    void getMetadataData() throws URISyntaxException;
}
