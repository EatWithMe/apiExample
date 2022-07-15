package sZZZ.model;

import java.math.BigDecimal;

public class CardLimit {

    private BigDecimal sum;
    private int operationCount;


    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal amount) {
        this.sum = amount;
    }


    public long getOperationCount() {
        return operationCount;
    }

    public void setOperationCount(int operationCount) {
        this.operationCount = operationCount;
    }


}
