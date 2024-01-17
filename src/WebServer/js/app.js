(function ()
{
    miseAJourTable();
})();

document.getElementById('update').addEventListener("click", function (e)
{
    var image = (document.getElementById('article').value) + ".jpg";
    const request = {id:document.getElementById('id').value,
        intitule:document.getElementById('article').value,
        prix:document.getElementById('prix').value,
        stock:document.getElementById('quantite').value,
        image:image};

    var xhr = new XMLHttpRequest();
    console.log("Gros ZIZI");

    xhr.onreadystatechange = function ()
    {
        if(this.readyState == 4 && this.status == 200)
        {
            miseAJourTable();
            alert(this.responseText);
        }
        else if (this.readyState == 4 && this.status == 400)
        {
            alert(this.responseText);
        }
    }
    xhr.open("PUT", "http://localhost:8080/api/articles", true)
    xhr.responseType =  "text";
    xhr.setRequestHeader("Content-type", "application/json");

    console.log(JSON.stringify(request));
    xhr.send(JSON.stringify(request));
});


function miseAJourTable()
{
    var xhr = new XMLHttpRequest();

    xhr.open("GET", "http://localhost:8080/api/articles", true);
    xhr.responseType = "json";
    xhr.send();

    xhr.onreadystatechange = function ()
    {
        if (xhr.readyState == 4 && xhr.status == 200)
        {
            articles = this.response;
            videTable();
            articles.forEach(function (article) {
                ajouteLigne(article.id, article.intitule, article.prix, article.stock);
            })
        }
    }
}
function videTable()
{
    var maTable = document.getElementById("maTable");
    while(maTable.rows.length > 1)
    {
        maTable.deleteRow(-1)//supprimer derniere ligne
    }
}
function ajouteLigne(id, intitule, prix, stock)
{
    var maTable = document.getElementById("maTable");
    var nouvelleLigne = document.createElement("tr");
    celluleId = document.createElement("td");
    celluleId.textContent = id;
    celluleArticle = document.createElement("td");
    celluleArticle.textContent = intitule;
    cellulePrix = document.createElement("td");
    cellulePrix.textContent = prix;
    celluleQuantite = document.createElement("td");
    celluleQuantite.textContent = stock;
    nouvelleLigne.appendChild(celluleId)
    nouvelleLigne.appendChild(celluleArticle);
    nouvelleLigne.appendChild(cellulePrix);
    nouvelleLigne.appendChild(celluleQuantite);
    nouvelleLigne.addEventListener("click", function() {
        var idArticle = document.getElementById('id');
        idArticle.value = this.children[0].innerText;
        var ArticleNom = document.getElementById('article');
        ArticleNom.value = this.children[1].innerText;
        var prixUnite = document.getElementById('prix');
        prixUnite.value = this.children[2].innerText;
        var quantite = document.getElementById('quantite');
        quantite.value = this.children[3].innerText;
    });
    maTable.appendChild(nouvelleLigne);
}