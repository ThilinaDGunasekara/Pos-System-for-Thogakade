package Controller;

import java.util.ArrayList;

public class Order{
    private String date;
    private String orderId;
    private String id;
    private ArrayList<OrderItem> orderItemList;

    public Order(String date, String orderId, String id, ArrayList<OrderItem> orderItemList) {
        this.date = date;
        this.orderId = orderId;
        this.id = id;
        this.orderItemList = orderItemList;

    }

    public Order() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(ArrayList<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return "Order{" +
                "date='" + date + '\'' +
                ", orderId='" + orderId + '\'' +
                ", customerId='" + id + '\'' +
                ", orderItemList=" + orderItemList +
                '}';
    }
}
