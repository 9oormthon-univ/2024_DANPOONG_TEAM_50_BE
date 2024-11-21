package com.example.mymoo.domain.donationusage.exception;

import com.example.mymoo.global.exception.CustomException;

public class DonationUsageException extends CustomException {
    public DonationUsageException(DonationUsageExceptionDetails donationUsageExceptionDetails){
        super(donationUsageExceptionDetails);
    }
}