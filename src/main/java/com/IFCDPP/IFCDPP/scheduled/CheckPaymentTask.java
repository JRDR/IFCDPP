package com.ifcdpp.ifcdpp.scheduled;

import com.ifcdpp.ifcdpp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckPaymentTask {

    private final PaymentService paymentService;

    @Scheduled(fixedRate = 5 * 60000)
    public void checkPaymentStatus() {
        paymentService.checkAllPayments();
    }
}
