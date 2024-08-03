package com.bijoy.cloud.couponservice.controller;

import com.bijoy.cloud.couponservice.model.Coupon;
import com.bijoy.cloud.couponservice.repository.CouponRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CouponRestControllerTest {
    @Mock
    private CouponRepository couponRepository;
    @InjectMocks
    private CouponRestController couponRestController;
    private static Coupon coupon;
    private String createCouponErrorMessage = "Failed to save coupon!";
    public static ObjectMapper objectMapper;

    @BeforeAll
    private static void setUp() {
        objectMapper = new ObjectMapper();
        coupon = new Coupon();
        coupon.setCode("aobtest01");
        coupon.setDiscount(BigDecimal.valueOf(5.00));
        coupon.setExpDate("2025-12-30");
    }

    @Test
    public void testCreateCoupon_whenExceptionThrown_thenProvideMessageField() throws JsonProcessingException {
        when(couponRepository.save(any(Coupon.class)))
                .thenThrow(new RuntimeException(createCouponErrorMessage));

        ResponseEntity<Object> createCouponResponse = couponRestController.createCoupon(coupon);
        String responseBody = (String) createCouponResponse.getBody();
        Map<String, String> responseMap = objectMapper.readValue(responseBody, Map.class);

        assertTrue(responseMap.get("message").equals(createCouponErrorMessage));
        assertTrue(createCouponResponse.getStatusCode().is5xxServerError());
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

}
