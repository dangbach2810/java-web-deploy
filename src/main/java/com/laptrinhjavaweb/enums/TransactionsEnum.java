package com.laptrinhjavaweb.enums;

public enum TransactionsEnum {

    QUA_TRINH_CSKH("Quá trình CSKH"),
    DAN_DI_XEM("Dẫn đi xem");

    private final String transactionValue;

    TransactionsEnum(String transactionValue) {
        this.transactionValue = transactionValue;
    }

    public String getTransactionValue() {
        return transactionValue;
    }
}
