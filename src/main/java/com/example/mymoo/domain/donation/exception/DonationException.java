package com.example.mymoo.domain.donation.exception;

import com.example.mymoo.global.exception.CustomException;

public class DonationException extends CustomException {
    public DonationException(DonationExceptionDetails donationExceptionDetails){
        super(donationExceptionDetails);
    }
}
