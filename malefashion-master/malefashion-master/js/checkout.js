const domain = "http://kishop.store:2323";
const localdomain = "http://103.82.25.243:2323";
const proxyUrl = "https://cors-anywhere.herokuapp.com/";
const apiUrl = `${proxyUrl}${domain}`;

var totalController = 0;
window.onload = function () {
  sessionStorage.getItem("lastname");

  const user = sessionStorage.getItem("email");

  if (user == null) {
    alert("Pls login to continue carts");
  } else {
    getShopCart(user);
  }
};

async function getShopCart(email) {
  let seller = await fetch(`${domain}/api/v1/view/payment?email=${email}`, {
    method: "GET",
    contentType: "application/json",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
  });

  const sls = await seller.json();
  const TableData = sls.result;
  var total = 0;

  var tash = "";
  for (let h in TableData) {
    const ne = TableData[h];

    tash += `<li>${h}. ${ne.productName} <span>${ne.price}</span></li>`;
    total += parseInt(ne.price) * parseInt(ne.amount);
  }

  document.querySelector("#list_check_out").innerHTML = tash;

  document.querySelector("#total_price").innerHTML = total;
}

async function getShopCart(email) {
  let seller = await fetch(`${domain}/api/v1/view/payment?email=${email}`, {
    method: "GET",
    contentType: "application/json",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
  });

  const sls = await seller.json();
  const TableData = sls.result;
  var total = 0;

  var tash = "";
  for (let h in TableData) {
    const ne = TableData[h];

    tash += `<li>${h}. ${ne.productName} <span>${ne.price}</span></li>`;
    totalController += parseInt(ne.price) * parseInt(ne.amount);
  }

  document.querySelector("#list_check_out").innerHTML = tash;

  document.querySelector("#total_price").innerHTML = totalController;
}

async function checkingOut() {
  const user = sessionStorage.getItem("email");
  if (user == null) {
    alert("Pls login to add product to carts");
  } else {
    const payload = JSON.stringify({
      phoneNumber: document.querySelector("#user_phone").value,
      usermail: user,
      address: document.querySelector("#user_address").value,
      total: totalController,
    });
    const response = await fetch(`${domain}/api/v1/checkout`, {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: payload,
    });
    if (response.status >= 200 && response.status <= 299) {
      alert("Thanks for buy product");
      
    }  
    if (response.status === 500){
      alert("pls confirm your email register");
    } 
    if (response.status === 403) {
      alert("pls register user");
    } 
  }
}

document.querySelector("#checkingout").addEventListener("click", checkingOut);
