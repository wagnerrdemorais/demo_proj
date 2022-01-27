function doRequest(url, type, data, callback) {
    console.log("doRequest() -> ", " url: ", url, " type: ", type, " data: ", data);
    $.ajax({
        url: url,
        type: type,
        data: data,
        headers: {"Authorization": "Bearer " + readValue(),},
        success: function (res) {
            callback(res);
        },
        error: function (e) {
            console.log(e.responseText);
            callback(e);
        }
    });
}

function createLocalstorageItem(txtJwtTokenValue) {
    sessionStorage.setItem("jwtToken", txtJwtTokenValue);
}

function readValue() {
    return sessionStorage.getItem("jwtToken");
}

function doLogin() {
    console.log("submit");
    let inputEmail = $("#input_email").val();
    let input_password = $("#input_password").val();
    $.ajax({
        url: "http://localhost:8080/auth",
        type: "post",
        data: {
            "email": inputEmail,
            "password": input_password
        },
        error: function (e) {
            console.log(e);
            console.log("Error response text: ", e.responseText);
        },
        success: function (data) {
            console.log(data);
            console.log("Success response text: ", data.responseText);
            createLocalstorageItem(data);
            window.location.href = "http://localhost:8080/query_executer"
        }
    });
}

function runQuery() {
    let url = "http://localhost:8080/query";
    let query = $("#input_query").val();
    let data = {"query": "" + query}

    let callback = function (e) {
        console.log(e);
        $("#queryResult").text(e);
    }
    doRequest(url, "post", data, callback);
}

function runQuerySaveFile() {
    let url = "http://localhost:8080/queryToFile";
    let query = $("#input_query").val();
    let data = {"query": "" + query}

    let callback = function (e) {
        console.log(e);
        $("#queryResult").text(e);
    }
    doRequest(url, "post", data, callback);
}