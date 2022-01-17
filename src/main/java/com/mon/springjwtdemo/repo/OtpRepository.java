package com.mon.springjwtdemo.repo;

import com.mon.springjwtdemo.model.Otp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends CrudRepository<Otp, String> {
    Optional<Otp> findOtpByUsername(String username);
}
