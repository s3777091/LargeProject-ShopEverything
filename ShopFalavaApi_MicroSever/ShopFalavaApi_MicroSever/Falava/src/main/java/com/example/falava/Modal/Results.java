package com.example.falava.Modal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Results {
    public Results(String price, String titleProduce, String slug, String image) {
        this.price = price;
        this.TitleProduce = titleProduce;
        this.Slug = slug;
        this.image = image;
    }

    private String TitleProduce;
    private String Slug;
    private String price;
    private String image;
}
