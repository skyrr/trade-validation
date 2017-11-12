package com.validator.trade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.validator.trade.api.Error;
import com.validator.trade.api.TradeRequest;

/**
 * Functional Tests for TradeValidatorResource.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FunctionalTest {

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
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"Spot\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-15\",\"legalEntity\":\"CS Zurich\",\"trader\":\"JohannBaumfiddler\"}",
                TradeRequest.class);

        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);

        assertEquals(Status.ACCEPTED.getStatusCode(), body.getStatusCodeValue());
        assertNull(body.getBody());
    }

    /**
     * Functional Test2.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction2() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"Spot\",\"direction\":\"SELL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-22\",\"legalEntity\":\"CS Zurich\",\"trader\":\"JohannBaumfiddler\"}",
                TradeRequest.class);

        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);

        assertEquals(Status.ACCEPTED.getStatusCode(), body.getStatusCodeValue());
        assertNull(body.getBody());
    }

    /**
     * Functional Test3.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction3() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO2\",\"ccyPair\":\"EURUSD\",\"type\":\"Forward\",\"direction\":\"SELL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-22\",\"legalEntity\":\"CS Zurich\",\"trader\":\"JohannBaumfiddler\"}",
                TradeRequest.class);

        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);

        assertEquals(Status.ACCEPTED.getStatusCode(), body.getStatusCodeValue());
        assertNull(body.getBody());
    }

    /**
     * Functional Test4.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction4() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO2\",\"ccyPair\":\"EURUSD\",\"type\":\"Forward\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-21\",\"legalEntity\":\"CS Zurich\",\"trader\":\"JohannBaumfiddler\"}",
                TradeRequest.class);

        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);
        final Error error = body.getBody();

        assertEquals(1L, error.getDetails().size());
        final List<String> expectedErrorMessages = Arrays.asList("Value date cannot be on a weekend");
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(0).getMessage()));
    }

    /**
     * Functional Test5.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction5() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO2\",\"ccyPair\":\"EURUSD\",\"type\":\"Forward\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-08\",\"legalEntity\":\"CS Zurich\",\"trader\":\"JohannBaumfiddler\"}",
                TradeRequest.class);

        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);
        final Error error = body.getBody();

        assertEquals(1L, error.getDetails().size());
        final List<String> expectedErrorMessages = Arrays.asList("Value date cannot be before Trade date");
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(0).getMessage()));
    }

    /**
     * Functional Test6.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction6() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUT02\",\"ccyPair\":\"EURUSD\",\"type\":\"Forward\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-08\",\"legalEntity\":\"CS Zurich\",\"trader\":\"JohannBaumfiddler\"}",
                TradeRequest.class);

        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);
        final Error error = body.getBody();

        assertEquals(2L, error.getDetails().size());
        final List<String> expectedErrorMessages = Arrays.asList("Value date cannot be before Trade date", "Customer is not known.");
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(0).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(1).getMessage()));
        
    }

    /**
     * Functional Test7.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction7() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO3\",\"ccyPair\":\"EURUSD\",\"type\":\"Forward\",\"direction\":\"BUY\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"valueDate\":\"2016-08-22\",\"legalEntity\":\"CS Zurich\",\"trader\":\"JohannBaumfiddler\"}",
                TradeRequest.class);

        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);
        final Error error = body.getBody();

        assertEquals(1L, error.getDetails().size());
        final List<String> expectedErrorMessages = Arrays.asList("Customer is not known.");
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(0).getMessage()));
    }

    /**
     * Functional Test8.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction8() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"EUROPEAN\",\"direction\":\"BUY\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-19\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CSZurich\",\"trader\":\"Johann Baumfiddler\"}",
                TradeRequest.class);

        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);
        final Error error = body.getBody();

        assertEquals(4L, error.getDetails().size());
       
        final List<String> expectedErrorMessages = Arrays.asList("Value date cannot be before Trade date"
                                                                 ,"Delivery date cannot be before Expiry date"
                                                                 ,"Delivery date cannot be before Premium date",
                                                                 "Value date cannot be on a weekend");
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(0).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(1).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(2).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(3).getMessage()));

    }

    /**
     * Functional Test9.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction9() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO2\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"EUROPEAN\",\"direction\":\"SELL\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-21\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CSZurich\",\"trader\":\"Johann Baumfiddler\"}",
                TradeRequest.class);
        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);
        final Error error = body.getBody();

        assertEquals(4L, error.getDetails().size());
        final List<String> expectedErrorMessages = Arrays.asList("Value date cannot be before Trade date"
                                                                ,"Delivery date cannot be before Expiry date"
                                                                ,"Delivery date cannot be before Premium date",
                                                                "Value date cannot be on a weekend");
         assertTrue(expectedErrorMessages.contains(error.getDetails().get(0).getMessage()));
         assertTrue(expectedErrorMessages.contains(error.getDetails().get(1).getMessage()));
         assertTrue(expectedErrorMessages.contains(error.getDetails().get(2).getMessage()));
         assertTrue(expectedErrorMessages.contains(error.getDetails().get(3).getMessage()));
    }

    /**
     * Functional Test10.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction10() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"EUROPEAN\",\"direction\":\"BUY\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-25\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CSZurich\",\"trader\":\"Johann Baumfiddler\"}",
                TradeRequest.class);
        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);
        final Error error = body.getBody();

        assertEquals(4L, error.getDetails().size());
        final List<String> expectedErrorMessages = Arrays.asList("Value date cannot be before Trade date"
                                                                ,"Delivery date cannot be before Expiry date"
                                                                ,"Delivery date cannot be before Premium date",
                                                                "Value date cannot be on a weekend");
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(0).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(1).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(2).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(3).getMessage()));
    }

    /**
     * Functional Test11.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction11() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"AMERICAN\",\"direction\":\"BUY\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-19\",\"excerciseStartDate\":\"2016-08-12\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"}",
                TradeRequest.class);
        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);
        final Error error = body.getBody();

        assertEquals(4L, error.getDetails().size());
        final List<String> expectedErrorMessages = Arrays.asList("Value date cannot be before Trade date"
                                                                ,"Delivery date cannot be before Expiry date"
                                                                ,"Delivery date cannot be before Premium date",
                                                                "Value date cannot be on a weekend");
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(0).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(1).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(2).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(3).getMessage()));
    }

    /**
     * Functional Test12.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction12() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO2\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"AMERICAN\",\"direction\":\"SELL\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-21\",\"excerciseStartDate\":\"2016-08-12\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"}",
                TradeRequest.class);
        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);
        final Error error = body.getBody();

        assertEquals(4L, error.getDetails().size());
        final List<String> expectedErrorMessages = Arrays.asList("Value date cannot be before Trade date"
                                                                ,"Delivery date cannot be before Expiry date"
                                                                ,"Delivery date cannot be before Premium date",
                                                                "Value date cannot be on a weekend");
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(0).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(1).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(2).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(3).getMessage()));
    }

    /**
     * Functional Test13.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction13() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"AMERICAN\",\"direction\":\"BUY\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-25\",\"excerciseStartDate\":\"2016-08-12\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"}",
                TradeRequest.class);
        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);
        final Error error = body.getBody();

        assertEquals(4L, error.getDetails().size());
 
        final List<String> expectedErrorMessages = Arrays.asList("Value date cannot be before Trade date"
                                                                  ,"Delivery date cannot be before Expiry date"
                                                                  ,"Delivery date cannot be before Premium date"
                                                                  ,"Value date cannot be on a weekend");
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(0).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(1).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(2).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(3).getMessage()));
    }

    /**
     * Functional Test14.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction14() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue(
                "{\"customer\":\"PLUTO1\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"AMERICAN\",\"direction\":\"BUY\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-19\",\"excerciseStartDate\":\"2016-08-10\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"}",
                TradeRequest.class);
        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);
        final Error error = body.getBody();

        assertEquals(5L, error.getDetails().size());
        final List<String> expectedErrorMessages = Arrays.asList("Value date cannot be before Trade date"
                                                                  ,"Delivery date cannot be before Expiry date"
                                                                  ,"Delivery date cannot be before Premium date"
                                                                  ,"Exercise Start date cannot be before Value date",
                                                                  "Value date cannot be on a weekend");
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(0).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(1).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(2).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(3).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(4).getMessage()));
    }

    /**
     * Functional Test15.
     * 
     * @throws Exception for any failure.
     */
    @Test
    public void testFunction15() throws Exception {
        final TradeRequest tradeRequest = new ObjectMapper().readValue("{\"customer\":\"PLUTO3\",\"ccyPair\":\"EURUSD\",\"type\":\"VanillaOption\",\"style\":\"AMERICAN\",\"direction\":\"SELL\",\"strategy\":\"CALL\",\"tradeDate\":\"2016-08-11\",\"amount1\":1000000.00,\"amount2\":1120000.00,\"rate\":1.12,\"deliveryDate\":\"2016-08-22\",\"expiryDate\":\"2016-08-19\",\"excerciseStartDate\":\"2016-08-10\",\"payCcy\":\"USD\",\"premium\":0.20,\"premiumCcy\":\"USD\",\"premiumType\":\"%USD\",\"premiumDate\":\"2016-08-12\",\"legalEntity\":\"CS Zurich\",\"trader\":\"Johann Baumfiddler\"}",TradeRequest.class);
        final ResponseEntity<Error> body = this.restTemplate.postForEntity("/trade-validator/validate-trade",
                tradeRequest, Error.class);
        final Error error = body.getBody();

        assertEquals(6L, error.getDetails().size());
        final List<String> expectedErrorMessages = Arrays.asList("Value date cannot be before Trade date"
                                                                  ,"Customer is not known."
                                                                  ,"Delivery date cannot be before Expiry date"
                                                                  ,"Delivery date cannot be before Premium date"
                                                                  ,"Exercise Start date cannot be before Value date",
                                                                  "Value date cannot be on a weekend");
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(0).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(1).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(2).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(3).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(4).getMessage()));
        assertTrue(expectedErrorMessages.contains(error.getDetails().get(5).getMessage()));
    }

}
