$(document).ready(function () {
    $("#dogless-toast").show();

    $(".close-toast-btn").on("click", function () {
        $(this).closest(".toast").hide();
    })
});