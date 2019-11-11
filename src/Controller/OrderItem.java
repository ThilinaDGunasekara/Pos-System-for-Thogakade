package Controller;

public class OrderItem {
    private String code;
    private String quantity;
    private String unitPrice;


    public OrderItem(String code, String quantity, String unitPrice) {
        this.code = code;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public OrderItem() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }
}
