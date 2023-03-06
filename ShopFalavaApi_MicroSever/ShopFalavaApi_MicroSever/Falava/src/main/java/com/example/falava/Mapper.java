package com.example.falava;

import com.example.falava.Entity.User.ProductCatagory;
import com.example.falava.Entity.User.Product_items;
import com.example.falava.Entity.User.UserProductBuyingEntity;
import com.example.falava.Modal.ProductBuyingModel;
import com.example.falava.Modal.Results;

public class Mapper {
    private static String cst(String cb) {
        String ast = cb.substring(0, cb.length() - cb.indexOf("https://encrypted") - 18);
        String ast3 = ast.replace(ast.substring(ast.indexOf("https://encrypted"), 53), "");
        return ast.substring(ast.indexOf("https://encrypted"), 45).concat("=tbn:").concat(ast3);
    }

    public static Product_items ReturnProduct(Results r, ProductCatagory ns){
        Product_items us = new Product_items();
        us.setProductname(r.getTitleProduce());
        us.setPrice(Double.parseDouble(r.getPrice()));
        us.setImgURL(r.getImage());
        us.setProductlink(r.getSlug());
        us.setProductCatagory(ns);
        return us;
    }
    //Add One Price
    public static ProductBuyingModel toProductBuying(UserProductBuyingEntity ust){
        ProductBuyingModel buyingModel = new ProductBuyingModel();
        buyingModel.setProductName(ust.getProductname());
        buyingModel.setProductImage(cst(ust.getProductImage()));
        buyingModel.setPrice(ust.getPrice());
        buyingModel.setAmount(String.valueOf(ust.getAmount()));
        buyingModel.setType(ust.getType());
        buyingModel.setStatus(ust.getStatus());
        return buyingModel;
    }
}
