
const searchInput = document.getElementById("search-input");
const searchBtn = document.getElementById("search-btn");
const searchResults = document.getElementById("search-results");

searchBtn.addEventListener("click", function () {
    let searchText = searchInput.value.toLowerCase();
    let textNodes = document.querySelectorAll("body *");
    let count = 0;

    searchResults.innerHTML = "";

    textNodes.forEach(function (node) {
        let nodeText = node.innerText.toLowerCase();

        if (nodeText.includes(searchText)) {
            count++;
            let nodeHtml = node.innerHTML;
            nodeHtml = nodeHtml.replace(
                new RegExp(searchText, "gi"),
                `<span class="search-highlight">${searchText}</span>`
            );
            searchResults.insertAdjacentHTML("beforeend", nodeHtml);
        }
    });

    searchResults.insertAdjacentHTML(
        "beforeend",
        `<p>${count} match(es) found</p>`
    );
});
