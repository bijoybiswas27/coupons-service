package com.bijoy.cloud.couponservice;

import com.bijoy.cloud.couponservice.model.Coupon;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponserviceApplicationTests {
    private MockMvc mvc;
    private static JdbcTemplate jdbcTemplate;
    private String createCouponRequestBody = "{\"code\": \"120OFF\", \"discount\": 120.0, \"expDate\": \"2027-01-20\"}";
    private static String createdCoupon = null;
    private static final String COUPON_REMOVE_QUERY = "DELETE FROM coupon WHERE id = ?";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    CouponserviceApplicationTests(@Autowired MockMvc mvc, @Autowired JdbcTemplate jdbcTemplateInstance) {
        this.mvc = mvc;
        jdbcTemplate = jdbcTemplateInstance;
    }

    @Test
    void testGetCoupon_whenNoAuth_thenProvideSuccessResponse() throws Exception {
        String couponCode = "50SALE";

        mvc.perform(get("/couponapi/coupons/" + couponCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discount").exists())
                .andExpect(jsonPath("$.code").value(couponCode))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    @WithMockUser()
    void testCreateCoupon_withRoleUSER_forbidden() throws Exception {
        mvc.perform(post("/couponapi/coupons").content(createCouponRequestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCreateCoupon_withRoleADMIN_success() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/couponapi/coupons").content(createCouponRequestBody)
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.discount").value(BigDecimal.valueOf(120.0)))
                .andReturn();
        createdCoupon = mvcResult.getResponse().getContentAsString();
    }

    @AfterAll
    static void dbCleanUp() throws JsonProcessingException {
        if (createdCoupon != null) {
            Coupon coupon = MAPPER.readValue(createdCoupon, Coupon.class);
            jdbcTemplate.update(COUPON_REMOVE_QUERY,
                    coupon.getId());
        }
    }
}
