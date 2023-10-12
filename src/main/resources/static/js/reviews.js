$(document).ready(function () {
    $("#deleted-toast").show();

    $(".close-toast-btn").on("click", function () {
        $(this).closest(".toast").hide();
    })
});