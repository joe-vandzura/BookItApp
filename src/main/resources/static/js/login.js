
// render toasts on DOM load
$(document).ready(function () {
    $("#error-toast").show();
    $("#logout-toast").show();

    $(".close-toast-btn").on("click", function () {
        $(this).closest(".toast").hide();
    })
});
