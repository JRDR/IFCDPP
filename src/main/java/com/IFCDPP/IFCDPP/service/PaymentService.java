package com.ifcdpp.ifcdpp.service;

import com.ifcdpp.ifcdpp.entity.PaymentEntity;
import com.ifcdpp.ifcdpp.entity.PaymentStatus;
import com.ifcdpp.ifcdpp.entity.ProductEntity;
import com.ifcdpp.ifcdpp.entity.User;
import com.ifcdpp.ifcdpp.exceptions.MessageException;
import com.ifcdpp.ifcdpp.models.Payment;
import com.ifcdpp.ifcdpp.models.PaymentStatusModel;
import com.ifcdpp.ifcdpp.models.Product;
import com.ifcdpp.ifcdpp.repo.PaymentRepository;
import com.ifcdpp.ifcdpp.repo.ProductRepository;
import com.ifcdpp.ifcdpp.repo.UserRepository;
import com.qiwi.billpayments.sdk.client.BillPaymentClient;
import com.qiwi.billpayments.sdk.client.BillPaymentClientFactory;
import com.qiwi.billpayments.sdk.model.BillStatus;
import com.qiwi.billpayments.sdk.model.MoneyAmount;
import com.qiwi.billpayments.sdk.model.in.PaymentInfo;
import com.qiwi.billpayments.sdk.model.out.BillResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:application.properties")
public class PaymentService {

    private final String publicKey;

    private final BillPaymentClient client;

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          UserRepository userRepository,
                          ProductRepository productRepository,
                          @Value("${qiwi.public}") String publicKey,
                          @Value("${qiwi.secret}") String secretKey) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.publicKey = publicKey;
        client = BillPaymentClientFactory.createDefault(secretKey);
    }

    public String createForm(String email, Long productId) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> new MessageException("Product not found"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new MessageException("User not found"));

        MoneyAmount amount = new MoneyAmount(
                product.getPrice(),
                Currency.getInstance("RUB")
        );
        UUID paymentId = UUID.randomUUID();
        String billId = paymentId.toString();
        String successUrl = "http://localhost:8080/checkPayment?productId=" + productId + "&paymentId=" + billId;

        PaymentEntity payment = new PaymentEntity(billId, product, user, PaymentStatus.PENDING);
        paymentRepository.save(payment);

        return client.createPaymentForm(new PaymentInfo(publicKey, amount, billId, successUrl));
    }

    public void cancelPayment(String email, Long productId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new MessageException("User not found"));
        PaymentEntity payment = paymentRepository.findByProduct_IdAndUser_Id(productId, user.getId())
                .orElseThrow(() -> new MessageException("Payment not found"));

        deletePayment(payment);
    }

    public void cancelAllPaymentsOnProducts(Long productId) {
        List<PaymentEntity> entities = paymentRepository.findAllByProduct_Id(productId);
        for (PaymentEntity payment : entities) {
            deletePayment(payment);
        }
    }

    private void deletePayment(PaymentEntity payment) {
        client.cancelBill(payment.getId());
        paymentRepository.delete(payment);
    }

    public void checkPayment(String paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId).orElseThrow(() -> new MessageException("Payment not found"));
        BillResponse response = client.getBillInfo(payment.getId());
        if (response.getStatus().getValue().equals(BillStatus.PAID)) {
            payment.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);
        } else if (!response.getStatus().getValue().equals(BillStatus.WAITING)) {
            paymentRepository.delete(payment);
        }
    }

    public void checkAllPayments() {
        List<PaymentEntity> payments = paymentRepository.findAll();
        for (PaymentEntity payment : payments) {
            checkPayment(payment.getId());
        }
    }

    public PaymentStatusModel checkPaymentFromDb(Long productId, Long userId) {
        Optional<PaymentEntity> payment = paymentRepository.findByProduct_IdAndUser_Id(productId, userId);
        if (!payment.isPresent()) {
            return PaymentStatusModel.CANCELLED;
        } else if (payment.get().getStatus().equals(PaymentStatus.SUCCESS)) {
            return PaymentStatusModel.SUCCESS;
        }
        return PaymentStatusModel.PENDING;
    }

    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findAllByUser_Id(userId).stream().map(this::mapPayment).collect(Collectors.toList());
    }

    private Payment mapPayment(PaymentEntity entity) {
        return Payment.builder().id(entity.getId()).product(mapProductForPayment(entity.getProduct()))
                .status(entity.getStatus() == PaymentStatus.SUCCESS ? PaymentStatusModel.SUCCESS : PaymentStatusModel.PENDING)
                .build();
    }

    private Product mapProductForPayment(ProductEntity entity) {
        return Product.builder().id(entity.getId()).title(entity.getTitle()).price(entity.getPrice()).build();
    }


}
