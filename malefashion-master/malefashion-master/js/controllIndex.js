const domain = "http://kishop.store:2323";
const localdomain = "http://103.82.25.243:2323";

window.onload = function () {
  getBestSeller();
  getKishop();
  getListBanner();
};

async function getBestSeller() {
  let seller = await fetch(
    `${domain}/api/v1/view/product?pageNo=0&pageSize=8&type=Phone`,
    {
      method: "GET",
      contentType: "application/json",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    }
  );
  const sls = await seller.json();

  for (let h in sls.results) {
    const ne = sls.results[h];
    document.querySelector("#list_seller").innerHTML += `     

      <div class="col-lg-3 col-md-6 col-sm-6 col-md-6 col-sm-6 mix new-arrivals">
    <div class="product__item">
        <img src="${ne.image}" alt="Italian Trulli" class="product__item__pic set-bg">
        <div class="product__item__text">
            <h6>${ne.title}</h6>
            
            <a class="add-cart" onclick="postUser(id = '${ne.id}');">+ Add To Cart</a>
            <h5>${ne.price}</h5>
        </div>
    </div>             
    </div>
    `;
  }
}

async function postUser(id) {

  const user = sessionStorage.getItem("email");

  if (user == null) {
    alert("Pls login to add product to carts");
  } else{
    const payload = JSON.stringify({
      email: user,
      product_id: id,
      amount: "1",
    });
    const response = await fetch(`${domain}/api/v1/payment/product`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: payload,
    });
    if (response.status >= 200 && response.status <= 299) {
      alert("success add product to cart");
    }

  }
}

async function getKishop() {
  let seller = await fetch(
    `${domain}/api/v1/view/product?pageNo=1&pageSize=6&type=Laptop`,
    {
      method: "GET",
      contentType: "application/json",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    }
  );
  const sls = await seller.json();

  for (let h in sls.results) {
    const ne = sls.results[h];
    document.querySelector("#list_ki_shop_insta").innerHTML += `   
      <img src="${ne.image}" class="instagram__pic__item set-bg">
      `;
  }
}

async function getListBanner() {
  let seller = await fetch(
    `${domain}/api/v1/view/product?pageNo=2&pageSize=4&type=Laptop`,
    {
      method: "GET",
      contentType: "application/json",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    }
  );
  const sls = await seller.json();

  for (let h in sls.results) {
    const ne = sls.results[h];
    if (h == 2) {
      document.querySelector("#list_banner").innerHTML += `   
        <div class="col-lg-7">
        <div class="banner__item banner__item--last">
            <div class="banner__item__pic">
                <img src="${ne.image}" alt="">
            </div>
            <div class="banner__item__text">
                <h2>Shoes Spring 2023</h2>
                <a href="#">Shop now</a>
            </div>
        </div>
    </div>`;
    }
  }
}
