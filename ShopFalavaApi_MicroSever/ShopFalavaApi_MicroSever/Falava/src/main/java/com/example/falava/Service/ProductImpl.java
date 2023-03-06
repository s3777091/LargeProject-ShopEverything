package com.example.falava.Service;

import com.example.falava.Entity.User.UserBuyingEntity;
import com.example.falava.Modal.ProductBuyingModel;

import java.util.List;

public interface ProductImpl {
    List<ProductBuyingModel> getListProductBuyingModel(UserBuyingEntity userBuying);
}
