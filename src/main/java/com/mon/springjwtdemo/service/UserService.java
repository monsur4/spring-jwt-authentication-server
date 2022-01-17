package com.mon.springjwtdemo.service;

import com.mon.springjwtdemo.model.Otp;
import com.mon.springjwtdemo.model.User;
import com.mon.springjwtdemo.repo.OtpRepository;
import com.mon.springjwtdemo.repo.UserRepository;
import com.mon.springjwtdemo.util.GenerateCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    public void addUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void auth(User user){
        Optional<User> o = userRepository.findUserByUsername(user.getUsername());
        if(o.isPresent()){
            User u = o.get();
            if(passwordEncoder.matches(user.getPassword(), u.getPassword())){
                renewOtp(u);
            }else{
                throw new BadCredentialsException("Bad Credentials.");
            }
        }else{
            throw new BadCredentialsException("Bad Credentials.");
        }
    }

    private void renewOtp(User u){
        String code = GenerateCodeUtil.generateCode();
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(u.getUsername());
        Otp otp;
        if(userOtp.isPresent()){
            otp = userOtp.get();
            otp.setCode(code);
        }else{
            otp = new Otp(u.getUsername(), code);
        }
        otpRepository.save(otp);
    }

    public boolean check(Otp otpToValidate){
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(otpToValidate.getUsername());
        if(userOtp.isPresent()){
            Otp otp = userOtp.get();
            if(otpToValidate.getCode().equals(otp.getCode())){
                return true;
            }
        }
        return false;
    }
}
