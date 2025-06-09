package KTPM.Backend.dto;

import java.math.BigDecimal;

import KTPM.Backend.entity.ServiceType.Type;
import lombok.Data;

@Data
public class ServiceTypeRequest {
    private String serviceName;
    private Type serviceType;
    private BigDecimal unitPrice;
} 