package KTPM.Backend.dto;

import java.math.BigDecimal;

import KTPM.Backend.entity.ServiceType;
import KTPM.Backend.entity.ServiceType.Type;
import lombok.Data;

@Data
public class ServiceTypeResponse {
    private Integer serviceTypeId;
    private String serviceName;
    private Type serviceType;
    private BigDecimal unitPrice;

    public static ServiceTypeResponse fromEntity(ServiceType entity) {
        ServiceTypeResponse response = new ServiceTypeResponse();
        response.setServiceTypeId(entity.getServiceTypeId());
        response.setServiceName(entity.getServiceName());
        response.setServiceType(entity.getServiceType());
        response.setUnitPrice(entity.getUnitPrice());
        return response;
    }
} 