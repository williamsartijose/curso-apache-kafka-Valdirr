package com.wsj.paymentservice.service;


import com.wsj.paymentservice.model.Payment;

public interface PaymentService {

    void sendPayment(Payment payment);
}