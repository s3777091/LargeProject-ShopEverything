const domain = "http://kishop.store:2323";


const localdomain = "http://103.82.25.243:2323";
const proxyUrl = "https://cors-anywhere.herokuapp.com/";
const apiUrl = `${proxyUrl}${domain}`;



var type = "Phone";
var ListData = 0;
const pageSize = 20;
let curPage = 0;

window.onload = function () {
  showphoneDevice();
};

async function getShopListDetail() {
  let seller = await fetch(
    `${domain}/api/v1/view/detail_list_product?type=${type}`,
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
  ListData = sls.total_list;

  document.querySelector(
    "#details_shop"
  ).innerHTML = `Showing 1 to 20 of ${ListData} results`;
}

function previousPage() {
  console.log(curPage);

  if (curPage > 0) {
    curPage--;
    getDataList(curPage);
  }
}

function nextPage() {
  curPage++;
  getDataList(curPage);
}

function numPages() {
  return Math.ceil(ListData / pageSize);
}

document.querySelector("#nextButton").addEventListener("click", nextPage);

document.querySelector("#prevButton").addEventListener("click", previousPage);

async function getDataList(page) {
  console.log(page + " " + numPages());
  if (page == 0) {
    prevButton.style.visibility = "hidden";
    nextButton.style.visibility = "visible";
  } else {
    prevButton.style.visibility = "visible";
  }

  if (page + 1 == numPages()) {
    nextButton.style.visibility = "hidden";
  } else {
    nextButton.style.visibility = "visible";
  }

  const seller = await fetch(
    `${domain}/api/v1/view/product?pageNo=${page}&pageSize=20&type=${type}`
  );
  const sts = await seller.json();
  const listShop = sts.results;

  var tash = "";
  for (let h in listShop) {
    const ne = listShop[h];
    tash += `<div class="col-lg-3 col-md-6 col-sm-6 col-md-6 col-sm-6 mix new-arrivals">`;
    tash += `<div class="product__item">`;
    tash += `<img src="${ne.image}" alt="Italian Trulli" class="product__item__pic set-bg">`;
    tash += `<div class="product__item__text">`;
    tash += `<h6>${ne.title}</h6>`;

    tash += `<a class="add-cart" onclick="postUser(id = '${ne.id}');">+ Add To Cart</a>`;
    tash += `<h5>${ne.price}</h5>`;
    tash += `</div>`;
    tash += `</div>`;
    tash += `</div>`;
  }
  document.querySelector("#list_results_shop").innerHTML = tash;
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

function showphoneDevice() {
  type = "Phone";
  curPage = 0;
  ListData = 0;
  getShopListDetail();
  getDataList(curPage);
}

function showLaptop() {
  type = "Laptop";
  curPage = 0;
  ListData = 0;
  getShopListDetail();
  getDataList(curPage);
}

function showManFashion() {
  type = "Man fashion";
  curPage = 0;
  ListData = 0;
  getShopListDetail();
  getDataList(curPage);
}

function showWomanFashion() {
  type = "Woman fashion";
  curPage = 0;
  ListData = 0;
  getShopListDetail();
  getDataList(curPage);
}

function showHomeAndLife() {
  type = "Home and life";
  curPage = 0;
  ListData = 0;
  getShopListDetail();
  getDataList(curPage);
}

function showHealth() {
  type = "Health";
  curPage = 0;
  ListData = 0;
  getShopListDetail();
  getDataList(curPage);
}

function showGame() {
  type = "Game";
  curPage = 0;
  ListData = 0;
  getShopListDetail();
  getDataList(curPage);
}


document
  .querySelector("#catogary_phone_device")
  .addEventListener("click", showphoneDevice);
document
  .querySelector("#catogary_laptop")
  .addEventListener("click", showLaptop);
document
  .querySelector("#catogary_man_fashion")
  .addEventListener("click", showManFashion);
document
  .querySelector("#catogary_woman_fashion")
  .addEventListener("click", showWomanFashion);
document
  .querySelector("#catogary_home_life")
  .addEventListener("click", showHomeAndLife);
document
  .querySelector("#catogary_health")
  .addEventListener("click", showHealth);
document
  .querySelector("#catogary_game")
  .addEventListener("click", showGame);


