package com.example.falava.Modal;

import com.example.falava.Entity.User.UserBuyingEntity;
import com.example.falava.Entity.User.UserProductBuyingEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderKafka {
    UserBuyingEntity userBuying;
    UserProductBuyingEntity userProductBuyingEntity;
}
