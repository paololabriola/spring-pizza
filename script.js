const addBtn = document.getElementById('add-pizza');
addBtn.addEventListener('click', addPizza);

let c = 0;

function addPizza() {
  axios
    .get('http://localhost:8080/api/pizza')
    .then(function (response) {

        createPizza(response.data[c])
        c++;

    })
    .catch(function (error) {

      console.log(error);

    });
}

function createPizza(data) {

  const card = document.createElement('div');
  card.classList.add("card","col-2","mx-3","shadow-lg");

  const img = document.createElement('img', 'card-img-top');
  img.src = 'data:image/jpeg;base64,' + data.immagini[0].content;

  const title = document.createElement('h3');
  title.innerHTML = data.nome;
  title.className = "card-title";

  const description = document.createElement('p');
  description.innerHTML = data.descrizione;
  description.className= "card-text";
  
  const cardBody = document.createElement('div');
  cardBody.className = "card-body";
  
  card.appendChild(img);
  card.appendChild(cardBody);
  cardBody.appendChild(title);
  cardBody.appendChild(description);

  document.getElementById('gallery-pizza').appendChild(card);
} 