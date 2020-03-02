package com.encapital.io.banksmsapp.utils;

import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.Signature;
import java.security.SignatureException;

public class SigningRequest {
    private final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANZszo0X+TWQL5wAIgiMK9trvRhLX1dMwW4WeF/f00EjwmkjPzhPHw9vXBnmOzsVg+3IThJjTPo1vKn4J7aOVy4ujDKpBOC9zZikiZI9nMb8OA4nn7j6a83SUeszvkYUjS0/AcSjKOW+4Yyshy/PYvyAUGrhWNTcZd4kpDZeuK21AgMBAAECgYAbDScRzdB23XXkLHAeu/Bcgj8c7YVdsiVFCjitf8NbGHPSZ8v2AqVcgSQRcGZQkepO+VJAJ57ygg0HNHNuKI7+3WmwXIbif8xdoO7fK2HKHVzHODH+Swe5RpQs5/TaLnqyGW/aOpa87fcHmHWEqgvJKn1r3BgODMZ3xZQbdXa3kQJBAOz56Sg/fV5FB02PoVAy6KmuFv+EaBvaie3D1g6EJ9yg8yPvDZ9+qyqBUn/7ppy2ksFhTAviycC6NchlVGYdczMCQQDno3KNjRDt35c+j+vnUK4rrxpM2ldHKLm6RuJcyBviz1oOIdXHEmcFNJVYGysVra0M4UzcByFgJOQOGXKUl1t3AkEA5zkpwPXCE9tytsEwexpRXk7ZbdP56UOhVXfCQ0O7yVDaLPOx7TAhP0YUA4C+HycdpsaS9v4AMab/l1sXsNOHdQJBAM0DaJvHwrgGMMJ6dvwf+EdJjyl+BclBTkCEBEzB/4xaoQXpBGBQ9UUyKQv138gsEwFXVsNWvD9v5zhjZXoGDDkCQQCjljVOLlceCpJ3/HLdw0GTbG6l+3hxXJvcTIKp+jb/Ee/6V+GN1jKdrMRRieGW0ln7hsFVqFnRMV70KqUuFlpT";
    private final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDWbM6NF/k1kC+cACIIjCvba70YS19XTMFuFnhf39NBI8JpIz84Tx8Pb1wZ5js7FYPtyE4SY0z6Nbyp+Ce2jlcuLowyqQTgvc2YpImSPZzG/DgOJ5+4+mvN0lHrM75GFI0tPwHEoyjlvuGMrIcvz2L8gFBq4VjU3GXeJKQ2XrittQIDAQAB";
    private Signature signing;
    private Signature verifying;

    public SigningRequest() {
            this.initSigning();
            this.initVerify();
    }

    private void initSigning() {
        // Signing
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(PRIVATE_KEY));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            Signature signingSignature = Signature.getInstance("SHA256WithRSA");
            signingSignature.initSign(kf.generatePrivate(spec));
            this.signing = signingSignature;
        } catch (Exception e) {
            System.out.println("Failed to init Signature");
            e.printStackTrace();
        }
    }

    
    private void initVerify() {
        // Verifying
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(PUBLIC_KEY));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            Signature verifyingSignature = Signature.getInstance("SHA256WithRSA");
            verifyingSignature.initVerify(kf.generatePublic(spec));
            this.verifying = verifyingSignature;
        } catch (Exception e) {
            System.out.println("Failed to init Signature");
            e.printStackTrace();
        }
    }

    
    public String signRequest(String requestBody) {
        try {
            this.signing.update(requestBody.getBytes("UTF-8"));
            byte[] signatureBytes = this.signing.sign();
            return Base64.getEncoder().withoutPadding().encodeToString(signatureBytes);
        } catch (SignatureException e) {
            System.out.println("Signature not initialized");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Failed to sign request");
            e.printStackTrace();
        }
        return "";
    }

    
    public Boolean verifyRequest(String requestBody, String signature) {
        try {
            this.verifying.update(requestBody.getBytes("UTF-8"));
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            return this.verifying.verify(signatureBytes);
        } catch (Exception e) {
            System.out.println("Failed to verify request");
            e.printStackTrace();
        }
        return false;
    }

//    public static void main(String[] args) {
//        // demo init
//        SigningExample example = new SigningExample();
//        example.initSigning();
//        example.initVerify();
//
//        // demo signing
//        String body = "Nội dung cần ký";
//        String signature = example.signRequest(body);
//        System.out.println(signature);
//
//        // demo verifying
//        Boolean isVerified = example.verifyRequest(body, signature);
//        System.out.println(isVerified);
//    }
}
