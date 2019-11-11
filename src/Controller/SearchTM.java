package Controller;

public class SearchTM {
    private String orderId;
    private String date;
    private String customerId;
    private String customerName;
    private double total;

    public SearchTM(String orderId, String date, String customerId, String customerName, double total) {
        this.orderId = orderId;
        this.date = date;
        this.customerId = customerId;
        this.customerName = customerName;
        this.total = total;
    }

    public SearchTM() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
