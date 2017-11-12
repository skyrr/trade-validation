package com.validator.trade;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.validator.trade.api.Detail;
import com.validator.trade.api.Error;
import com.validator.trade.api.TradeRequest;
import com.validator.trade.dto.TradeRequestDTO;
import com.validator.trade.enums.ProductStyle;
import com.validator.trade.enums.ProductType;
import com.validator.trade.validators.groups.AmericanStyleChecks;
import com.validator.trade.validators.groups.VanillaOptionalTypeChecks;

/**
 * The resource to validate the {@link TradeRequest}.
 */
@Component
@Path("/trade-validator")
public class TradeValidatorResource {

    /**
     * Validates a single {@link TradeRequest}.
     * 
     * @param tradeRequest the {@link TradeRequest}
     * @return the respose with {@link Error} or {@link Status#ACCEPTED}
     */
    @Path("/validate-trade")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateTrade(final TradeRequest tradeRequest) {
        final TradeRequestDTO tradeRequestDTO = populateDTO(tradeRequest);
        final Set<ConstraintViolation<TradeRequestDTO>> violations = validateRequest(tradeRequestDTO);

        if (violations.isEmpty()) {
            return Response.status(Status.ACCEPTED).build();
        }

        final Error error = new Error();
        final List<Detail> errorDetails = new ArrayList<>();
        error.setDetails(errorDetails);
        error.setTradeRequest(tradeRequest);
        for (final ConstraintViolation<TradeRequestDTO> constraintViolation : violations) {
            final Detail errorDetail = new Detail();
            errorDetail.setField(constraintViolation.getPropertyPath().toString());
            errorDetail.setMessage(constraintViolation.getMessage());
            errorDetails.add(errorDetail);
        }

        return Response.status(Status.BAD_REQUEST).entity(error).build();
    }

    /**
     * Validates a list of {@link TradeRequest}s.
     * 
     * @param tradeRequest the list of {@link TradeRequest}s
     * @return the response with {@link Error} or {@link Status#ACCEPTED}
     */
    @Path("/validate-bulk-trade")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateBulkTrade(final List<TradeRequest> tradeRequests) {
        final List<Error> errors = new ArrayList<>();
        boolean isValid = true;
        for (final TradeRequest tradeRequest : tradeRequests) {
            final TradeRequestDTO tradeRequestDTO = populateDTO(tradeRequest);
            final Set<ConstraintViolation<TradeRequestDTO>> violations = validateRequest(tradeRequestDTO);
            final Error error = new Error();
            final List<Detail> errorDetails = new ArrayList<>();
            error.setTradeRequest(tradeRequest);
            error.setDetails(errorDetails);
            if (!violations.isEmpty()) {
                for (final ConstraintViolation<TradeRequestDTO> constraintViolation : violations) {
                    final Detail errorDetail = new Detail();
                    errorDetail.setField(constraintViolation.getPropertyPath().toString());
                    errorDetail.setMessage(constraintViolation.getMessage());
                    errorDetails.add(errorDetail);
                }
                isValid = false;
            }
            errors.add(error);
        }
        if (isValid) {
            return Response.status(Status.ACCEPTED).entity(errors).build();
        }

        return Response.status(Status.BAD_REQUEST).entity(errors).build();
    }

    /**
     * Validates the {@link TradeRequestDTO} for all defined groups.
     * 
     * @param tradeRequestDTO the {@link TradeRequestDTO}
     * @return the {@link Set} of {@link ConstraintViolation} errors
     */
    private Set<ConstraintViolation<TradeRequestDTO>> validateRequest(final TradeRequestDTO tradeRequestDTO) {
        final Set<ConstraintViolation<TradeRequestDTO>> constraintViolations = new LinkedHashSet<>();
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();

        constraintViolations.addAll(validator.validate(tradeRequestDTO));

        switch (ProductType.getByProductTypeName(tradeRequestDTO.getType())) {
        case OPTIONAL:
            constraintViolations.addAll(validator.validate(tradeRequestDTO, VanillaOptionalTypeChecks.class));
            switch (ProductStyle.getByProductStyleName(tradeRequestDTO.getStyle())) {
            case AMERICAN:
                constraintViolations.addAll(validator.validate(tradeRequestDTO, AmericanStyleChecks.class));
                break;
            default:
                break;
            }
            break;
        case SPOT:
        case FORWARD:
            break;
        default:
            break;
        }

        return constraintViolations;
    }

    /**
     * Populates and returns {@link TradeRequestDTO} from {@link TradeRequest}.
     * 
     * @param tradeRequest the {@link TradeRequest} to be converted
     * @return the {@link TradeRequestDTO}
     */
    private TradeRequestDTO populateDTO(final TradeRequest tradeRequest) {
        return TradeRequestDTO.builder()
                .customer(tradeRequest.getCustomer())
                .ccyPair(tradeRequest.getCcyPair())
                .type(tradeRequest.getType())
                .style(tradeRequest.getStyle())
                .direction(tradeRequest.getDirection())
                .strategy(tradeRequest.getStrategy())
                .tradeDate(toLocalDate(tradeRequest.getTradeDate()))
                .amount1(tradeRequest.getAmount1())
                .amount2(tradeRequest.getAmount2())
                .rate(tradeRequest.getRate())
                .valueDate(toLocalDate(tradeRequest.getValueDate()))
                .expiryDate(toLocalDate(tradeRequest.getExpiryDate()))
                .excerciseStartDate(toLocalDate(tradeRequest.getExcerciseStartDate()))
                .payCcy(tradeRequest.getPayCcy())
                .premium(tradeRequest.getPremium())
                .premiumCcy(tradeRequest.getPremiumCcy())
                .premiumType(tradeRequest.getPremiumType())
                .premiumDate(toLocalDate(tradeRequest.getPremiumDate()))
                .legalEntity(tradeRequest.getLegalEntity())
                .trader(tradeRequest.getTrader())
                .build();
    }

    /**
     * Converts a String date into {@link LocalDate}. Null if the parameter is Null.
     * 
     * @param stringDate the string date to be parsed
     * @return the parsed {@link LocalDate}, Null otherwise
     */
    private LocalDate toLocalDate(final String stringDate) {
        if (StringUtils.isBlank(stringDate)) {
            return null;
        }

        return LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE);
    }
}
