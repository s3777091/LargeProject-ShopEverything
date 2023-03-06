const domain = "http://kishop.store:2323";
const localdomain = "http://103.82.25.243:2323";
const proxyUrl = "https://cors-anywhere.herokuapp.com/";
const apiUrl = `${proxyUrl}${domain}`;

window.onload = function () {
  sessionStorage.getItem("lastname");

  const user = sessionStorage.getItem("email");

  if (user == null) {
    alert("Pls login to continue carts");
  } else{
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
    tash += '<tr>';
    tash += '<td class="product__cart__item">';
    tash += '<div class="product__cart__item__pic">';
    tash += `<img src="${ne.productImage}" alt="">`
    tash += '</div>';
    tash += '<div class="product__cart__item__text">';
    tash += `<h6>${ne.productName}</h6>`;
    tash += `<h5>${ne.price}</h5>`
    tash += '</div>';
    tash += '</td>';
    tash += '<td class="quantity__item">';
    tash += '<div class="quantity">';
    tash += '<div class="pro-qty-2">';
    tash += `<input type="text" value="${ne.amount}">`
    tash += '</div>' + '</div>' + '</td>';
    tash += `<td class="cart__price">${ne.price}</td>`
    tash += '<td class="cart__close"><i class="fa fa-close"></i></td>' + '</tr>';
    total += parseInt(ne.price) * parseInt(ne.amount);
  }

  document.querySelector("#tash_table").innerHTML = tash;

  document.querySelector("#total_price").innerHTML = total;

}


