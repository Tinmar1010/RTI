(function ()
{
    console.log("chris");
    miseAJourTable();
})();
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
    maTable.appendChild(nouvelleLigne);
}