function submitForm()
{
    getNumberOfItems();
    var form = document.getElementById("myForm");
    form.submit();
}

function getNumberOfItems() {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    let input = document.getElementById('inputItems');
    input.value = parseInt(cart.length);
}
