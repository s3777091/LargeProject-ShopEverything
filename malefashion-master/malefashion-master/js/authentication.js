const domain = "http://kishop.store:2323";
const localdomain = "http://103.82.25.243:2323";
const proxyUrl = "https://cors-anywhere.herokuapp.com/";
const apiUrl = `${proxyUrl}${domain}`;

const login = document.getElementById("login");
const register = document.getElementById("register");


function openRegister() {
    if (login.style.display !== 'none') {
        register.style.display = 'block'
        login.style.display = 'none'
    }
}

function openLogin() {
    if (register.style.display !== 'none') {
        login.style.display = 'block'
        register.style.display = 'none'
    }
}


async function loginUser() {
    let add = document.getElementById("circle1");
    add.className += " loader";
    const emailValue = document.querySelector("#inputEmail").value;
    const payload = JSON.stringify({
        email: document.querySelector("#inputEmail").value,
        password: document.querySelector("#inputPass").value
    });
    const response = await fetch(`${domain}/api/v1/login`, {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: payload
    });
    if (response.status >= 200 && response.status <= 299) {
        sessionStorage.setItem("email", emailValue);
        location.href="index.html"
    } else if (response.status === 400) {
        alert("wrong email and password");
    } else {
        alert("Something wrong");
    }
    add.className -= " loader";
}

document.querySelector("#submit_login").addEventListener("click", loginUser);


async function registerUser() {
    let add = document.getElementById("circle2");
    add.className += " loader";
    const payload = JSON.stringify({
        fullName: document.querySelector("#FullName").value,
        email: document.querySelector("#register_email_value").value,
        password: document.querySelector("#register_password_value").value
    })
    const response_register = await fetch(`${domain}/api/v1/user/register`, {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: payload,
    })
    if (response_register.status >= 200 && response_register.status <= 299) {
        alert("Success create account pls go to check your gmail");
    } else if (response_register.status === 401) {
        alert("You need to check your email verification");
    } else if (response_register.status === 400) {
        alert("Email already have in system pls change to another email");
    } else {
        alert("Sever error");
    }
    add.className -= " loader";
}

document.querySelector("#regisAcc").addEventListener("click", registerUser);
