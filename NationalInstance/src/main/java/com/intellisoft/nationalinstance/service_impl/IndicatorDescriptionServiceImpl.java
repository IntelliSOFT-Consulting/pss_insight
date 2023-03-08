package com.intellisoft.nationalinstance.service_impl;

import com.intellisoft.nationalinstance.db.IndicatorDescription;
import com.intellisoft.nationalinstance.db.repso.IndicatorDescriptionRepo;
import com.intellisoft.nationalinstance.util.AppConstants;
import com.intellisoft.nationalinstance.util.GenericWebclient;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Module;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IndicatorDescriptionServiceImpl implements IndicatorDescriptionService{

    private final IndicatorDescriptionRepo indicatorDescriptionRepo;


    @Override
    public IndicatorDescription addIndicatorDescription(IndicatorDescription indicatorDescription) {

        String code = indicatorDescription.getCode();
        Optional<IndicatorDescription> optionalIndicatorDescription =
                indicatorDescriptionRepo.findByCode(code);
        if (optionalIndicatorDescription.isPresent()){
            IndicatorDescription indicatorDescriptionUpdate = optionalIndicatorDescription.get();
            indicatorDescriptionUpdate.setDescription(indicatorDescription.getDescription());
            return indicatorDescriptionRepo.save(indicatorDescriptionUpdate);
        }else {
            return indicatorDescriptionRepo.save(indicatorDescription);
        }

    }

    @Override
    public IndicatorDescription getIndicatorDescriptionByCode(String code) {

        Optional<IndicatorDescription> optionalIndicatorDescription =
                indicatorDescriptionRepo.findByCode(code);
        return optionalIndicatorDescription.orElse(null);
    }

    @Async
    @Override
    public void getIndicatorDescription() throws URISyntaxException {

        var  res = GenericWebclient.getForSingleObjResponse(
                AppConstants.INDICATOR_DESCRIPTION_ENDPOINT, String.class);
        System.out.println("-------");
        System.out.println(res);

        JSONArray jsonArray = new JSONArray(res);
        System.out.println("******");
        System.out.println(jsonArray);

        jsonArray.forEach(element->{
            if (((JSONObject)element).has("Description") &&
                    ((JSONObject)element).has("Indicator_Code")){
                System.out.println("+++++++");
                String  Description = ((JSONObject)element).getString("Description");
                String  Indicator_Code = ((JSONObject)element).getString("Indicator_Code");
                System.out.println(Indicator_Code);

                IndicatorDescription indicatorDescription = new IndicatorDescription();
                indicatorDescription.setDescription(Description);
                indicatorDescription.setCode(Indicator_Code);
                addIndicatorDescription(indicatorDescription);
            }
        });

    }
}
