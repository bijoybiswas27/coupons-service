package com.bijoy.cloud.couponservice.controller;

import com.bijoy.cloud.couponservice.model.Coupon;
import com.bijoy.cloud.couponservice.repository.CouponRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class CouponController {
    private CouponRepository couponRepository;

    public CouponController(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @GetMapping("/showCreateCoupon")
    public String showCreateCoupon() {
        return "createCoupon";
    }

    @PostMapping("/saveCoupon")
    public String save(Coupon coupon) {
        couponRepository.save(coupon);
        return "createResponse";
    }

    @GetMapping("/showGetCoupon")
    public String showGetCoupon() {
        return "getCoupon";
    }

    @PostMapping("/getCoupon")
    public ModelAndView getCoupon(@RequestParam("code") String code) {
        ModelAndView mav = new ModelAndView("couponDetails");
        Optional<Coupon> optionalCoupon = couponRepository.findByCode(code);
        if (optionalCoupon.isPresent()) {
            mav.addObject(optionalCoupon.get());
        } else {
            String message = "No Coupon: {" + code + "} found!";
            mav.addObject("message", message);
        }
        return mav;
    }


}
