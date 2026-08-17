package com.projectlove.lovable_clone.repository;

import com.projectlove.lovable_clone.entity.Plan;
import com.stripe.model.checkout.Session;
import com.stripe.model.tax.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long> {
    Optional<Plan> findByStripePriceId(String id);

}
