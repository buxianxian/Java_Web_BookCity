package com.atguigu.pojo;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

//购物车对象
public class Cart {
    //private Integer totalCount;
    //private BigDecimal totalPrice;
    //key是商品编号，value是商品信息
    private Map<Integer, CartItem> items = new LinkedHashMap<>();

    //添加商品项
    public void addItem(CartItem cartItem) {
        //先查看购物车中是否已添加过此商品，如果已添加，则数量累加，总金额更新。如果没有添加过，直接放到集合中即可
        CartItem item = items.get(cartItem.getId());

        if (item == null) {
            //之前没添加过此商品
            items.put(cartItem.getId(), cartItem);
        } else {
            //已经添加过的情况
            item.setCount(item.getCount() + 1); //数量累加
            item.setTotalPrice(item.getPrice().multiply(new BigDecimal(item.getCount())));
        }

    }

    //删除商品项
    public void deleteItem(Integer id) {
        items.remove(id);
    }

    //清空购物车
    public void clear() {
        items.clear();
    }

    //修改商品数量
    public void updateCount(Integer id, Integer count) {
        //先查看购物车中是否有此商品。如果有，修改商品数量，更新总金额
        CartItem cartItem = items.get(id);

        if (cartItem != null) {
            cartItem.setCount(count);
            cartItem.setTotalPrice(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount())));
        }
    }

    //setTotalCount方法也不能使用，理由同下
    public Integer getTotalCount() {
        //每次调用这个方法都会有一个新的totalCount，所以把它定义为实例变量没有意义，直接定义为局部变量即可
        Integer totalCount = 0;
        for(Map.Entry<Integer, CartItem> entry: items.entrySet()) {
            totalCount += entry.getValue().getCount();
        }
        return totalCount;
    }

    //setTotalPrice方法是不能用的，因为总金额是根据购物车中的商品决定的，我们不能随意赋值
    public BigDecimal getTotalPrice() {
        //每次调用这个方法都会有一个新的totalPrice，所以把它定义为实例变量没有意义，直接定义为局部变量即可
        BigDecimal totalPrice = new BigDecimal(0);
        for(Map.Entry<Integer, CartItem> entry: items.entrySet()) {
            totalPrice = totalPrice.add(entry.getValue().getTotalPrice());
        }
        return totalPrice;
    }

    public Map<Integer, CartItem> getItems() {
        return items;
    }

    public void setItems(Map<Integer, CartItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "totalCount=" + getTotalCount() +
                ", totalPrice=" + getTotalPrice() +
                ", items=" + items +
                '}';
    }
}
