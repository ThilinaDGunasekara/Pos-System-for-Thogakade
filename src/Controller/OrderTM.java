package Controller;

import com.jfoenix.controls.JFXButton;

public class OrderTM extends Order {
    private String code;
    private String description;
    private String quantity;
    private String unitPrice;
    private String total;
    private JFXButton delete_Id;

    public OrderTM(String code, String description, String quantity, String unitPrice, String total, JFXButton delete_Id) {
        this.code = code;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = total;
        this.delete_Id = delete_Id;
    }
    public OrderTM(String code, String description, String quantity, String unitPrice, String total) {
        this.code = code;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = total;
    }

    public OrderTM() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public JFXButton getDelete_Id() {
        return delete_Id;
    }

    public void setDelete_Id(JFXButton delete_Id) {
        this.delete_Id = delete_Id;
    }
}
