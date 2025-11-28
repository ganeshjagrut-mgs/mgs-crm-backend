package com.mgs.service.mapper;

import com.mgs.domain.Product;
import com.mgs.domain.Quotation;
import com.mgs.domain.QuotationItem;
import com.mgs.domain.Tenant;
import com.mgs.service.dto.ProductDTO;
import com.mgs.service.dto.QuotationDTO;
import com.mgs.service.dto.QuotationItemDTO;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuotationItem} and its DTO {@link QuotationItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuotationItemMapper extends EntityMapper<QuotationItemDTO, QuotationItem> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "quotation", source = "quotation", qualifiedByName = "quotationId")
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    QuotationItemDTO toDto(QuotationItem s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("quotationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuotationDTO toDtoQuotationId(Quotation quotation);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
