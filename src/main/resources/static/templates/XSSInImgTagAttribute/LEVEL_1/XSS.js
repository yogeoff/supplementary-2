function addingEventListenerToLoadImageButton() {
    document.getElementById("loadImage").addEventListener("click", function () {
        let url = getUrlForVulnerabilityLevel();
        doGetAjaxCall(
            appendResponseCallback,
            url +
            "?src=/VulnerableApp/images/" +
            document.getElementById("images").value,
            false
        );
    });
}

addingEventListenerToLoadImageButton();

function appendResponseCallback(data) {
    document.getElementById("image").innerHTML = data;
}
