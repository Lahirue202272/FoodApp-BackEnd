package com.phegon.FoodApp.enums;

public enum PaymentGateway {
    STRIPE,
    PAYPAL,        // International payments
    PAYHERE,       // Local Sri Lankan payment gateway
    VERVE,         // Local bank card options (Visa/MasterCard/Amex)
    EZCASH         // Mobile wallet (Dialog eZ Cash)
}
