package com.example.falava.Controller;


import com.example.falava.Entity.User.*;
import com.example.falava.Mapper;
import com.example.falava.Modal.CheckingControlModel;
import com.example.falava.Modal.ProductBuyingModel;
import com.example.falava.Repository.*;
import com.example.falava.Request.*;
import com.example.falava.Service.ProductImpl;
import com.example.falava.Thread.SearchProductThread;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductCatogoryRepository productCatogoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserBuyingRepository userBuyingRepository;

    @Autowired
    UserOrderRepository userOrderRepository;


    @Autowired
    KafkaTemplate<String, Object> controlTemplate;

    @Autowired
    ProductImpl product;

    static List<Runnable> tasksSearch = new ArrayList<>();


    private String cst(String cb) {
        String ast = cb.substring(0, cb.length() - cb.indexOf("https://encrypted") - 18);
        String ast3 = ast.replace(ast.substring(ast.indexOf("https://encrypted"), 53), "");
        return ast.substring(ast.indexOf("https://encrypted"), 45).concat("=tbn:").concat(ast3);
    }


    @PostMapping("/api/v1/create/catogory")
    public ResponseEntity<Object> createCatogory(@RequestBody AddProduct addProduct) {

        ProductCatagory productCatagory = new ProductCatagory();
        productCatagory.setProducttype(addProduct.getKeyCatogory());
        productCatagory.setProductimage(addProduct.getKeyImage());
        productCatogoryRepository.save(productCatagory);

        return new ResponseEntity<>("Success save news" + addProduct.getKeyCatogory(), HttpStatus.OK);
    }


    //Send List Of Product And Add To DataBase
    @PostMapping("/api/v1/add/product")
    @Async
    public CompletableFuture<ResponseEntity<Object>> postProductToDb(@RequestBody AddProctDB addProduct) {
        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        SearchProductThread pr_thr = new SearchProductThread(addProduct.getKeyProduct());
        ProductCatagory productCatagory = productCatogoryRepository.findByProducttype(addProduct.getKeyCatogary());

        if (Objects.equals(productCatagory.getProducttype(), addProduct.getKeyCatogary())) {
            tasksSearch.add(pr_thr);
            //Working Here
            CompletableFuture<?>[] futures = tasksSearch.stream()
                    .map(task -> CompletableFuture.runAsync(task, es))
                    .toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(futures).join();
            es.shutdown();
            if (es.isShutdown()) {
                pr_thr.getListProduct()
                        .filter(f -> !Objects.equals(f.getImage(), ""))
                        .forEach(f -> {
                            controlTemplate.send("addKiProduct", Mapper.ReturnProduct(f, productCatagory));
                        });
            }
            tasksSearch.clear();
            return CompletableFuture.completedFuture(new ResponseEntity<>("Success add Product", HttpStatus.OK));
        } else {
            return CompletableFuture.completedFuture(new ResponseEntity<>("Can't Find Category", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    //Send List Of Product And Add To DataBase
    @PostMapping("/api/v1/delete/product")
    public ResponseEntity<Object> deleteAllProduct() {
        productRepository.deleteAll();
        return new ResponseEntity<>("Success delete All Product", HttpStatus.OK);
    }

    @GetMapping("/api/v1/view/detail_list_product")
    @Async
    public CompletableFuture<ResponseEntity<Object>> getAllProduct(@RequestParam(defaultValue = "All") String type) {
        JSONObject total_pr = new JSONObject();
        ProductCatagory prc = productCatogoryRepository.findByProducttype(type);
        List<Product_items> listDetail = productRepository.findAllByProductCatagory(prc);
        total_pr.put("total_list", listDetail.stream().count());

        return CompletableFuture.completedFuture(new ResponseEntity<>(total_pr.toMap(), new HttpHeaders(), HttpStatus.OK));

    }


    @GetMapping("/api/v1/view/product")
    @Async
    public CompletableFuture<ResponseEntity<Object>> getAllProduct(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "50") Integer pageSize,
            @RequestParam(defaultValue = "All") String type) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        JSONObject total_pr = new JSONObject();
        JSONArray tsa = new JSONArray();
        ProductCatagory prc = productCatogoryRepository.findByProducttype(type);
        total_pr.put("success", true);
        total_pr.put("status", "False");
        if (!prc.getProducttype().equals(type)) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(total_pr.toMap(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR));
        } else {
            Page<Product_items> listCatagory = productRepository.findAllByProductCatagory(paging, prc);
            for (Product_items n : listCatagory) {
                JSONObject itemProduct = new JSONObject();
                itemProduct.put("id", n.getId());
                itemProduct.put("price", n.getPrice());
                itemProduct.put("title", n.getProductname());
                itemProduct.put("image", cst(n.getImgURL()));
                tsa.put(itemProduct);
                total_pr.put("results", tsa);
            }

            total_pr.put("status", "Success");

            return CompletableFuture.completedFuture(new ResponseEntity<>(total_pr.toMap(), new HttpHeaders(), HttpStatus.OK));
        }
    }

    @GetMapping("/api/v1/view/all/product")
    @Async
    public CompletableFuture<ResponseEntity<Object>> getAll(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "50") Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        JSONObject total_pr = new JSONObject();
        JSONArray tsa = new JSONArray();

        total_pr.put("success", true);
        total_pr.put("status", "success");

        Page<Product_items> list = productRepository.findAll(paging);
        for (Product_items n : list) {
            JSONObject itemProduct = new JSONObject();
            itemProduct.put("id", n.getId());
            itemProduct.put("price", n.getPrice());
            itemProduct.put("title", n.getProductname());
            itemProduct.put("image", cst(n.getImgURL()));
            tsa.put(itemProduct);
            total_pr.put("results", tsa);
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(total_pr.toMap(), new HttpHeaders(), HttpStatus.OK));
    }

    @GetMapping("/api/v1/detail/product")
    @Async
    public CompletableFuture<ResponseEntity<Object>> getProductDetail(@Param("id") long id) {
        JSONObject total_pr = new JSONObject();

        Product_items sts = productRepository.findById(id);
        if (sts == null) {
            total_pr.put("success", false);
            total_pr.put("status", "product not existing");
            JSONArray tsa = new JSONArray();
            JSONObject itemProduct = new JSONObject();
            tsa.put(itemProduct);
            total_pr.put("results", tsa);
        } else {
            total_pr.put("success", true);
            total_pr.put("status", "success");
            JSONArray tsa = new JSONArray();
            JSONObject itemProduct = new JSONObject();
            itemProduct.put("id", sts.getId());
            itemProduct.put("price", sts.getPrice());
            itemProduct.put("title", sts.getProductname());
            itemProduct.put("slug", sts.getProductlink());
            itemProduct.put("image", cst(sts.getImgURL()));
            itemProduct.put("catogary", sts.getProductCatagory().getProducttype());
            tsa.put(itemProduct);
            total_pr.put("results", tsa);
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(total_pr.toMap(), new HttpHeaders(), HttpStatus.OK));
    }

    //Add Location
    //Add type of payment
    //For another android app
    @PostMapping("/api/v1/payment/product")
    public ResponseEntity<Object> CreateOrder(@RequestBody ProductPayment productPayment) {
        Product_items items = productRepository.findById(productPayment.getProduct_id());

        UserEntity userChecking = userRepository.findByEmail(productPayment.getEmail());
        if (userChecking == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        long idChecking = userChecking.getUserBuyingEntity().getId();
        UserBuyingEntity userBuying = userBuyingRepository.findById(idChecking);

        UserProductBuyingEntity prBuying = new UserProductBuyingEntity();
        prBuying.setProductname(items.getProductname());
        prBuying.setProductImage(items.getImgURL());
        prBuying.setPrice(String.valueOf(items.getPrice()));
        prBuying.setStatus("confirm");
        prBuying.setType("Ship with fee");
        prBuying.setDate(new Timestamp(System.currentTimeMillis()));
        prBuying.setUserBuyingEntity(userBuying);
        prBuying.setAmount(productPayment.getAmount());
        controlTemplate.send("BuyingKiProduct", prBuying);
        return new ResponseEntity<>("Success create order", new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/api/v1/payment/product_by_name")
    public ResponseEntity<Object> CreateOrderByName(@RequestBody DefaultS ad) {
        Product_items items = productRepository.findByProductname(ad.getPro());

        UserEntity userChecking = userRepository.findByEmail(ad.getEmail());
        if (userChecking == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        long idChecking = userChecking.getUserBuyingEntity().getId();
        UserBuyingEntity userBuying = userBuyingRepository.findById(idChecking);

        UserProductBuyingEntity prBuying = new UserProductBuyingEntity();
        prBuying.setProductname(items.getProductname());
        prBuying.setProductImage(items.getImgURL());
        prBuying.setPrice(String.valueOf(items.getPrice()));
        prBuying.setStatus("confirm");
        prBuying.setType("Ship with fee");
        prBuying.setDate(new Timestamp(System.currentTimeMillis()));
        prBuying.setUserBuyingEntity(userBuying);
        prBuying.setAmount(1);
        controlTemplate.send("BuyingKiProduct", prBuying);

        return new ResponseEntity<>("Success create order", new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/api/v1/view/payment")
    @Async
    public CompletableFuture<ResponseEntity<Object>> ViewOrder(@RequestParam(defaultValue = "email") String email) {
        JSONObject total_pr = new JSONObject();
        total_pr.put("success", true);
        total_pr.put("status", "success");
        UserEntity userChecking = userRepository.findByEmail(email);
        if (userChecking == null) {
            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        }
        List<ProductBuyingModel> image_list = product.getListProductBuyingModel(userChecking.getUserBuyingEntity());
        total_pr.put("result", image_list);
        return CompletableFuture.completedFuture(new ResponseEntity<>(total_pr.toMap(), new HttpHeaders(), HttpStatus.OK));
    }

    //Add type of payment
    @PostMapping("/api/v1/delete/payment")
    public ResponseEntity<Object> delete_product_payment(@RequestBody DeletePayment de) {
        JSONObject total_pr = new JSONObject();
        UserEntity userChecking = userRepository.findByEmail(de.getEmail());
        if (userChecking == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserProductBuyingEntity userBuying = userOrderRepository.findByProductname(de.getProduct_name());
        controlTemplate.send("deletePayment", userBuying);
        total_pr.put("success", true);
        total_pr.put("status", "success delete order");
        return new ResponseEntity<>(total_pr.toMap(), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/api/v1/checkout")
    public ResponseEntity<?> checkOut(@RequestBody CheckOutRequest res){
        JSONObject total_pr = new JSONObject();
        total_pr.put("success", true);

        UserEntity user = userRepository.findByEmail(res.getUsermail());

        if (!Objects.equals(user.getEmail(), res.getUsermail())){
            return new ResponseEntity<>("Pls register user", new HttpHeaders(), HttpStatus.FORBIDDEN);
        } else if (!user.isStatus()){
            return new ResponseEntity<>("Pls login user or confirm user in email", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CheckingControlModel check = new CheckingControlModel();
        check.setAddress(res.getAddress());
        check.setPhone(res.getPhoneNumber());
        check.setTotal(res.getTotal());

        check.setEmail(res.getUsermail());

        controlTemplate.send("SendMailCheckingOut", check);

        UserBuyingEntity userTas = userBuyingRepository.findById(user.getUserId());
        List<UserProductBuyingEntity> userProduct = userOrderRepository.findByUserBuyingEntity(userTas);
        userProduct.forEach(e -> {
            if(Objects.equals(e.getStatus(), "confirm")){
                controlTemplate.send("CheckingOut", e);
            }
        });
        total_pr.put("status", "thanks you for buying product");
        return new ResponseEntity<>(check, new HttpHeaders(), HttpStatus.OK);
    }

}
