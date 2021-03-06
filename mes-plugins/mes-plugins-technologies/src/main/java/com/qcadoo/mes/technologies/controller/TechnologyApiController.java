package com.qcadoo.mes.technologies.controller;

import com.qcadoo.localization.api.TranslationService;
import com.qcadoo.mes.productionLines.constants.ProductionLineFields;
import com.qcadoo.mes.productionLines.controller.dataProvider.ProductionLineDto;
import com.qcadoo.mes.technologies.constants.OperationFields;
import com.qcadoo.mes.technologies.constants.TechnologiesConstants;
import com.qcadoo.mes.technologies.controller.dataProvider.DataProviderForTechnology;
import com.qcadoo.mes.technologies.controller.dataProvider.MaterialDto;
import com.qcadoo.mes.technologies.controller.dataProvider.OperationMaterialDto;
import com.qcadoo.mes.technologies.controller.dataProvider.OperationRequest;
import com.qcadoo.mes.technologies.controller.dataProvider.OperationResponse;
import com.qcadoo.mes.technologies.controller.dataProvider.OperationsResponse;
import com.qcadoo.mes.technologies.controller.dataProvider.TechnologiesGridResponse;
import com.qcadoo.mes.technologies.controller.dataProvider.TechnologiesResponse;
import com.qcadoo.model.api.DataDefinitionService;
import com.qcadoo.model.api.Entity;
import com.qcadoo.model.api.validators.ErrorMessage;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public final class TechnologyApiController {

    @Autowired
    private DataProviderForTechnology dataProvider;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private DataDefinitionService dataDefinitionService;

    @ResponseBody
    @RequestMapping(value = "/operations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OperationsResponse getOperations() {
        return dataProvider.getOperations();
    }

    @ResponseBody
    @RequestMapping(value = "/technologies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public TechnologiesResponse getTechnologies(@RequestParam("query") String query, @RequestParam("productId") Long productId, @RequestParam(value = "master", required = false, defaultValue = "false") Boolean master) {
        return dataProvider.getTechnologies(query, productId, master);
    }

    @ResponseBody
    @RequestMapping(value = "/technology/{technologyId}/materials", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MaterialDto> getTechnologyMaterials(@PathVariable Long technologyId) {
        return dataProvider.getTechnologyMaterials(technologyId);
    }

    @ResponseBody
    @RequestMapping(value = "/technology/{technologyId}/operationMaterials", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OperationMaterialDto> getTechnologyOperationMaterials(@PathVariable Long technologyId) {
        return dataProvider.getTechnologyOperationMaterials(technologyId);
    }

    @ResponseBody
    @RequestMapping(value = "/technology/{technologyId}/productionLine", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductionLineDto getTechnologyProductionLine(@PathVariable Long technologyId) {
        return dataProvider.getTechnologyProductionLine(technologyId);
    }

    @ResponseBody
    @RequestMapping(value = "/technologiesByPage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public TechnologiesGridResponse getProducts(@RequestParam(value = "limit") int limit, @RequestParam(value = "offset") int offset,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "productId") Long productId) {
        return dataProvider.getTechnologiesResponse(limit, offset, sort, order, search, productId);
    }

    @ResponseBody
    @RequestMapping(value = "/operation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public OperationResponse saveOperation(@RequestBody OperationRequest operation) {

        Entity operationEntity = dataDefinitionService.get(TechnologiesConstants.PLUGIN_IDENTIFIER, TechnologiesConstants.MODEL_OPERATION).create();
        operationEntity.setField(OperationFields.NUMBER, operation.getNumber());
        operationEntity.setField(OperationFields.NAME, operation.getName());
        operationEntity.setField(OperationFields.QUANTITY_OF_WORKSTATIONS, 1);
        operationEntity.setField(OperationFields.ASSIGNED_TO_OPERATION, "01workstations");

        operationEntity = operationEntity.getDataDefinition().save(operationEntity);
        if(operationEntity.isValid()) {
            OperationResponse operationResponse = new OperationResponse(OperationResponse.StatusCode.OK);
            operationResponse.setId(operationEntity.getId());
            operationResponse.setNumber(operation.getNumber());
            return operationResponse;
        } else {
            //
            ErrorMessage numberError = operationEntity.getError(ProductionLineFields.NUMBER);
            if(Objects.nonNull(numberError) && numberError.getMessage().equals("qcadooView.validate.field.error.duplicated")) {
                OperationResponse response = new OperationResponse(OperationResponse.StatusCode.ERROR);
                response.setMessage(translationService.translate("basic.dashboard.orderDefinitionWizard.error.validationError.productionLineDuplicated",
                        LocaleContextHolder.getLocale()));
                return response;
            }

        }
        OperationResponse response = new OperationResponse(OperationResponse.StatusCode.ERROR);
        response.setMessage(translationService.translate("basic.dashboard.orderDefinitionWizard.error.validationError.productionLineErrors",
                LocaleContextHolder.getLocale()));
        return response;
    }

}
