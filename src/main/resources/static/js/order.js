function addToCart(id)
{
    let productImg = document.getElementById("img" + id);
    let imgSrc = productImg.getAttribute("src");
    let imgSrcString = String(imgSrc);
    let productName = document.getElementById("title" + id).innerText;
    let productPrice = document.getElementById("info" + id).innerText;
    productPrice = parseInt(productPrice.replaceAll(' ', '').slice(0, -4));
    let item = {
        id: id,
        img: imgSrcString,
        name: productName,
        price: productPrice,
    };
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    let existingItem = cart.find(cartItem => cartItem.name === item.name);

        cart.push(item);
    localStorage.setItem('cart', JSON.stringify(cart));
}

var oformitButton = document.getElementById("Oformit_but");

var cartModal = document.getElementById("cartModal");
var closeButton = document.getElementsByClassName("close")[0];

function fillCartItems()
{
    let cartItemsElement = document.getElementById("cart-items");
    cartItemsElement.innerHTML = "";
    let summ = 0;
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    if (cart.length == 0){
        oformitButton.addEventListener("click", function(event)
        {
            event.preventDefault();
            cartModal.style.display = "block";
        });

        closeButton.addEventListener("click", function()
        {
            cartModal.style.display = "none";
        });

        window.addEventListener("click", function(event)
        {
            if (event.target == cartModal) {
                cartModal.style.display = "none";
            }
        });
    } else {
        oformitButton.addEventListener("click", function(event) {
            removeAll();
        });

    }
    for (let ind = 0; ind < cart.length; ind++) {
        let cartItemElement = document.createElement('div');
        cartItemElement.classList.add('oneGood');
        cartItemElement.innerHTML = `
            <div class="card">
                <img src="${cart[ind].img}" alt="${cart[ind].name}">
                <p>${cart[ind].name}</p>
            </div>
            <p>${cart[ind].price} ₽</p>
            <div>
                <button class="remove-btn" onclick="removeItem(${ind})"></button>
            </div>`;
        let cartIndex = document.getElementById('index' + ind);
        cartIndex.value = parseInt(cart[ind].id);
        let cartName = document.getElementById('name' + ind);
        cartName.value = cart[ind].name;
        let cartCoursPrice = document.getElementById('coursPrice' + ind);
        cartCoursPrice.value = parseInt(cart[ind].price);
        summ += cart[ind].price;
        cartItemsElement.appendChild(cartItemElement);
    }
    let quantitySumm = document.getElementById(`text2`);
    quantitySumm.value = parseInt(quantitySumm.value | 0) + summ;
}

function removeItem(index)
{
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    cart.splice(index, 1);
    localStorage.setItem('cart', JSON.stringify(cart));
    submitForm();
    fillCartItems();
}

function removeAll()
{
    localStorage.removeItem('cart');
    window.location.href = 'person';
}