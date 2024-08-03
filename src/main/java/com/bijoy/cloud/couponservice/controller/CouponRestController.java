package com.bijoy.cloud.couponservice.controller;

import com.bijoy.cloud.couponservice.model.Coupon;
import com.bijoy.cloud.couponservice.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/couponapi")
public class CouponRestController {
    @Autowired
    CouponRepository couponRepository;

    @PostMapping("/coupons")
    public ResponseEntity<Object> createCoupon(@RequestBody Coupon coupon) {
        try {
            Coupon savedCoupon = couponRepository.save(coupon);
            return new ResponseEntity<>(savedCoupon, HttpStatus.CREATED);
        } catch (Exception ex) {
            System.out.println("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>("{\"message\": \"" + ex.getMessage() + "\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/coupons/{code}")
    public ResponseEntity<Object> getCoupon(@PathVariable String code) {
        Optional<Coupon> optionalCoupon = couponRepository.findByCode(code);
        if (optionalCoupon.isPresent()) {
            return ResponseEntity.ok(optionalCoupon.get());
        } else {
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Provided Coupon with code: {" + code + "} does not exist!");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
    }
}
