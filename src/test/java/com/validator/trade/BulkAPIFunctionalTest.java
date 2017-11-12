package com.validator.trade;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.validator.trade.api.Error;
import com.validator.trade.api.TradeRequest;

/**
 * Functional Tests for TradeValidatorResource.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BulkAPIFunctionalTest {

    /**
     * The ReST template.
     */
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Functional Test1.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction1() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String tradeRequest1 = "{\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"Spot\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-15\",\"legalEntity\":\"CS Zurich\",\"trader\":\"JohannBaumfiddler\"}";
        final String tradeRequest2 = "{\"customer\":\"PLUTO2\",\"ccyPair\":\"EURUSD\",\"type\":\"Forward\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-21\",\"legalEntity\":\"CS Zurich\",\"trader\":\"JohannBaumfiddler\"}";
        final String tradeRequest3 = "{\"customer\":\"PLUT02\",\"ccyPair\":\"EURUSD\",\"type\":\"Forward\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-08\",\"legalEntity\":\"CS Zurich\",\"trader\":\"JohannBaumfiddler\"}";
        final String tradeRequest4 = "{\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"EUROPEAN\",\"direction\":\"BUY\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-19\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CSZurich\",\"trader\":\"Johann Baumfiddler\"}";
        final String tradeRequest5 = "{\"customer\":\"PLUTO3\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"AMERICAN\",\"direction\":\"SELL\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-19\",\"excerciseStartDate\":\"2016-08-10\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"}";
        final List<TradeRequest> tradeRequestsList = objectMapper.readValue(
                "["
                + tradeRequest1 + ","
                + tradeRequest2 + ","
                + tradeRequest3 + ","
                + tradeRequest4 + ","
                + tradeRequest5
                + "]",
                new TypeReference<List<TradeRequest>>() {});
        
        final ResponseEntity<String> body = this.restTemplate.postForEntity("/trade-validator/validate-bulk-trade",
                tradeRequestsList, String.class);

        final List<Error> errors = objectMapper.readValue(body.getBody(), new TypeReference<List<Error>>() {});

        assertEquals(Status.BAD_REQUEST.getStatusCode(), body.getStatusCodeValue());
        assertEquals(5L, errors.size());
        assertEquals(0L, errors.get(0).getDetails().size());
        assertEquals(objectMapper.readValue(tradeRequest1, TradeRequest.class), errors.get(0).getTradeRequest());
        assertEquals(1L, errors.get(1).getDetails().size());
        assertEquals(objectMapper.readValue(tradeRequest2, TradeRequest.class), errors.get(1).getTradeRequest());
        assertEquals(2L, errors.get(2).getDetails().size());
        assertEquals(objectMapper.readValue(tradeRequest3, TradeRequest.class), errors.get(2).getTradeRequest());
        assertEquals(4L, errors.get(3).getDetails().size());
        assertEquals(objectMapper.readValue(tradeRequest4, TradeRequest.class), errors.get(3).getTradeRequest());
        assertEquals(6L, errors.get(4).getDetails().size());
        assertEquals(objectMapper.readValue(tradeRequest5, TradeRequest.class), errors.get(4).getTradeRequest());
    }

}
