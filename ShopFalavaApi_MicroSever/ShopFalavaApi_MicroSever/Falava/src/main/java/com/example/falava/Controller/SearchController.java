package com.example.falava.Controller;


import com.example.falava.Entity.User.ProductCatagory;
import com.example.falava.Entity.User.Product_items;
import com.example.falava.Modal.UserReceiver;
import com.example.falava.Repository.ProductCatogoryRepository;
import com.example.falava.Repository.ProductRepository;
import com.example.falava.Request.RegisterRequest;
import com.example.falava.Request.SearchAdding;
import com.example.falava.Request.SearchMessage;
import com.example.falava.Thread.SearchProductThread;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class SearchController {

    @Autowired
    KafkaTemplate<String, Object> controlTemplate;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCatogoryRepository productCatogoryRepository;

    static List<Runnable> tasksSearch = new ArrayList<>();

    private String cst(String cb) {
        String ast = cb.substring(0, cb.length() - cb.indexOf("https://encrypted") - 18);
        String ast3 = ast.replace(ast.substring(ast.indexOf("https://encrypted"), 53), "");
        return ast.substring(ast.indexOf("https://encrypted"), 45).concat("=tbn:").concat(ast3);
    }

    @Async
    @PostMapping(value = "/api/v1/search_product")
    public CompletableFuture<ResponseEntity<Object>> getProductSearch(@RequestBody SearchMessage searchMessage) {
        JSONObject total_pr = new JSONObject();
        total_pr.put("status", "success");
        JSONArray tsa = new JSONArray();

        ExecutorService es = Executors.newFixedThreadPool(10);
        SearchProductThread pr_thr = new SearchProductThread(searchMessage.getSearchValue());

        tasksSearch.add(pr_thr); //1 Task only

        CompletableFuture<?>[] futures = tasksSearch.stream()
                .map(task -> CompletableFuture.runAsync(task, es))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        es.shutdown();

        if (es.isShutdown()) {
            pr_thr.getListProduct()
                    .filter(e -> !Objects.equals(e.getImage(), ""))
                    .forEach(e -> {
                        JSONObject itemProduct = new JSONObject();
                        itemProduct.put("price", e.getPrice());
                        itemProduct.put("title", e.getTitleProduce());
                        itemProduct.put("slug", e.getSlug());
                        itemProduct.put("image", cst(e.getImage()));
                        tsa.put(itemProduct);
                        total_pr.put("results", tsa);
                    });
        }
        tasksSearch.clear();
        return CompletableFuture.completedFuture(new ResponseEntity<>(total_pr.toMap(), HttpStatus.OK));
    }

    @PostMapping("/value/v1/add_search_product")
    @Async
    public CompletableFuture<ResponseEntity<Object>> addProductSearch(@RequestBody SearchAdding search) {
        JSONObject total_pr = new JSONObject();
        total_pr.put("success", true);
        long long_rand = ThreadLocalRandom.current().nextLong();

        ProductCatagory psc = productCatogoryRepository.findByProducttype("Random");
        try {
            Product_items pr = productRepository.findByProductname(search.getProductName());
            if (pr != null) {
                return CompletableFuture.completedFuture(new ResponseEntity<>("Product search existing", HttpStatus.FORBIDDEN));
            } else {
                Product_items prchecking = productRepository.findById(long_rand);
                if (prchecking != null) {
                    addProductSearch(search);
                } else {
                    Product_items prs = new Product_items();
                    prs.setId(long_rand);

                    prs.setProductlink(search.getProductLink());
                    prs.setImgURL(search.getProductImage());
                    prs.setPrice(Double.parseDouble(search.getPrice()));
                    prs.setProductname(search.getProductName());
                    prs.setProductCatagory(psc);
                    controlTemplate.send("SendSearchPost", prs);
                    total_pr.put("status", "success add product search");
                }
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new ResponseEntity<>("Fail to add Product Search", HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return CompletableFuture.completedFuture(new ResponseEntity<>(total_pr.toMap(), HttpStatus.OK));
    }


}
