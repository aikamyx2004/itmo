window.notify = function (message) {
    $.notify(message, {
        position: "right bottom",
        className: "success"
    });
}
window.ajax = function (data, successFunction) {
    $.ajax({
        type: "POST",
        url: "",
        dataType: "json",
        data: data,
        success: function (response) {
            successFunction(response);
            if (response.hasOwnProperty("redirect")) {
                location.href = response["redirect"];
            }
            return false;
        }
    });
}