package com.example.falava.Service;

import com.example.falava.Entity.User.UserBuyingEntity;
import com.example.falava.Entity.User.UserProductBuyingEntity;
import com.example.falava.Mapper;
import com.example.falava.Modal.ProductBuyingModel;
import com.example.falava.Repository.UserOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService implements ProductImpl {


    @Autowired
    UserOrderRepository orderRes;

    @Override
    public List<ProductBuyingModel> getListProductBuyingModel(UserBuyingEntity userBuying) {
        List<UserProductBuyingEntity> prOrderListEntity = orderRes.findByUserBuyingEntity(userBuying);
        List<ProductBuyingModel> prOrderList = new ArrayList<>();
        for (UserProductBuyingEntity ts: prOrderListEntity){
            prOrderList.add(Mapper.toProductBuying(ts));
        }
        return prOrderList;
    }
}
