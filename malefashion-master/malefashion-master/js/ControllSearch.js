const domain = "http://kishop.store:2323";
const localdomain = "http://103.82.25.243:2323";
const proxyUrl = "https://cors-anywhere.herokuapp.com/";
const apiUrl = `${proxyUrl}${domain}`;

async function getSearchValue() {
  const payload = JSON.stringify({
    searchValue: document.querySelector("#search_value").value,
  });
  let seller = await fetch(`${domain}/api/v1/search_product`, {
    method: "POST",
    contentType: "application/json",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: payload,
  });

  const sls = await seller.json();
  const listShop = sls.results;

  var tash = "";
  for (let h in listShop) {
    const ne = listShop[h];
    tash += `<div class="col-lg-3 col-md-6 col-sm-6 col-md-6 col-sm-6 mix new-arrivals">`;
    tash += `<div class="product__item">`;
    tash += `<img src="${ne.image}" alt="Italian Trulli" class="product__item__pic set-bg">`;
    tash += `<div class="product__item__text">`;
    tash += `<h6>${ne.title}</h6>`;

    tash += `<a class="add-cart" onclick="postSearchPayment(proName = '${ne.title}', proImage = '${ne.image}', proLink = '${ne.slug}', proPrice = '${ne.price}');">+ Add To Cart</a>`;
    tash += `<h5>${ne.price} đ</h5>`;
    tash += `</div>`;
    tash += `</div>`;
    tash += `</div>`;
  }
  document.querySelector("#list_search_shop").innerHTML = tash;
}

document
  .querySelector("#press_search")
  .addEventListener("click", getSearchValue);

async function postAddSearch(name, user) {
  const payload = JSON.stringify({
    email: user,
    pro: name,
  });
  const response = await fetch(`${domain}/api/v1/payment/product_by_name`, {
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

async function postSearchPayment(proName, proImage, proLink, proPrice) {
  const user = sessionStorage.getItem("email");

  if (user == null) {
    alert("Pls login to add product to carts");
  } else {
    const payload = JSON.stringify({
      productName: proName,
      productImage: proImage,
      productLink: proLink,
      price: proPrice,
    });
    let seller = await fetch(`${domain}/value/v1/add_search_product`, {
      method: "POST",
      contentType: "application/json",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: payload,
    });

    if (seller.status >= 200 && seller.status <= 299) {
      alert("success add product to cart");
    }

    if (seller.status == 403) {
      postAddSearch(proName, user);
    }

    if (seller.status === 500) {
      alert("Something went wrong");
    }
  }
}