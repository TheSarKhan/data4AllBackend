package org.example.dataprotal.repository.user;

import org.example.dataprotal.model.user.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long>, JpaSpecificationExecutor<PaymentHistory> {

    List<PaymentHistory> findPaymentHistoryByUserId(Long userId);

    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM PaymentHistory p
            WHERE p.paymentStatus = 'SUCCESS'
            """)
    BigDecimal totalEarnings();

}
