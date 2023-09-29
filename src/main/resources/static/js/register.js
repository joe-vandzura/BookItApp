let usernameInput = $("#username-input");
let usernameInvalidFeedbackDiv = $("#username-exists-div");

let emailInput = $("#email-input");
let emailInvalidFeedbackDiv = $("#email-exists-div");

let passwordInput = $("#password-input");
let passwordInvalidDiv = $("#password-invalid-div");

let registerButton = $("#register-btn");

function formIsValid() {
    if ((passwordInput.hasClass("invalid") || passwordInput.val().trim() === "") || (emailInput.hasClass("invalid") || passwordInput.val().trim() === "") || usernameInput.hasClass("invalid")) {
        registerButton.addClass("disabled");
    } else {
        console.log(passwordInput.val() === "");
        console.log(emailInput.val());
        registerButton.removeClass("disabled");
    }
}

function isValidEmail(email) {
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailRegex.test(email);
}

// validate username existence
usernameInput.on("input", async function() {
    let usernameInputValue = usernameInput.val();
    if (usernameInputValue.trim() === "") {
        usernameInput.addClass("invalid");
    } else {
        let response = await fetch("/username-check/" + usernameInputValue, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        });
        if (response.ok) {
            if (await response.json()) {
                usernameInput.addClass("invalid");
                usernameInvalidFeedbackDiv.show();
                usernameInvalidFeedbackDiv.prev().removeClass("mb-3");
            } else {
                usernameInput.removeClass("invalid");
                usernameInvalidFeedbackDiv.hide();
                usernameInvalidFeedbackDiv.prev().addClass("mb-3");
            }
        }
    }
    formIsValid();
});


// validate email input formatting and existence
emailInput.on("input", async function() {
    const email = emailInput.val();

    if (isValidEmail(email)) {
        emailInput.removeClass("invalid");
        let response = await fetch("/email-check/" + email , {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            if (await response.json()) {
                emailInput.addClass("invalid");
                emailInvalidFeedbackDiv.show();
                emailInvalidFeedbackDiv.prev().removeClass("mb-3");
            } else {
                emailInput.removeClass("invalid");
                emailInvalidFeedbackDiv.hide();
                emailInvalidFeedbackDiv.prev().addClass("mb-3");
            }
        }

    } else {
        emailInput.addClass("invalid");
    }
    formIsValid();
});


// validate password input formatting
passwordInput.on("input", function() {
    let password = $(this).val();
    let hasCapitalLetter = /[A-Z]/.test(password);
    let hasNumber = /[0-9]/.test(password);
    let hasSymbol = /[!@#$%^&*()_+{}\[\]:;<>,.?~\\-]/.test(password);

    if (password.length < 8 || !hasCapitalLetter || !hasNumber || !hasSymbol) {
        passwordInput.addClass("invalid");
        if (password.length < 8) {
            $("#password-characters-p").show();
        } else {
            $("#password-characters-p").hide();
        }
        if (!hasCapitalLetter) {
            $("#password-capital-p").show();
        } else {
            $("#password-capital-p").hide();
        }
        if (!hasNumber) {
            $("#password-number-p").show();
        } else {
            $("#password-number-p").hide();
        }
        if (!hasSymbol) {
            $("#password-symbol-p").show();
        } else {
            $("#password-symbol-p").hide();
        }
        passwordInvalidDiv.show();
        passwordInvalidDiv.prev().removeClass("mb-3");
    } else {
        passwordInput.removeClass("invalid");
        passwordInvalidDiv.hide();
        passwordInvalidDiv.prev().addClass("mb-3");
    }
    formIsValid();
});