let usernameInput = $("#username-input");
let usernameInvalidFeedbackDiv = $("#username-exists-div");

let emailInput = $("#email-input");
let emailInvalidFeedbackDiv = $("#email-exists-div");

let passwordInput = $("#password-input");
let passwordInvalidDiv = $("#password-invalid-div");

let registerButton = $("#register-btn");

function formIsValid() {
    if ((passwordInput.hasClass("is-invalid") || passwordInput.val().trim() === "") || (emailInput.hasClass("is-invalid") || passwordInput.val().trim() === "") || usernameInput.hasClass("is-invalid")) {
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
        usernameInput.addClass("is-invalid");
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
                usernameInput.addClass("is-invalid");
                usernameInvalidFeedbackDiv.show();
            } else {
                usernameInput.removeClass("is-invalid");
                usernameInvalidFeedbackDiv.hide();
            }
        }
    }
    formIsValid();
});


// validate email input formatting and existence
emailInput.on("input", async function() {
    const email = emailInput.val();

    if (isValidEmail(email)) {
        emailInput.removeClass("is-invalid");
        let response = await fetch("/email-check/" + email , {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            if (await response.json()) {
                emailInput.addClass("is-invalid");
                emailInvalidFeedbackDiv.show();
            } else {
                emailInput.removeClass("is-invalid");
                emailInvalidFeedbackDiv.hide();
            }
        }

    } else {
        emailInput.addClass("is-invalid");
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
        passwordInput.addClass("is-invalid");
        if (password.length < 8) {
            $("#password-characters-span").show();
        } else {
            $("#password-characters-span").hide();
        }
        if (!hasCapitalLetter) {
            $("#password-capital-span").show();
        } else {
            $("#password-capital-span").hide();
        }
        if (!hasNumber) {
            $("#password-number-span").show();
        } else {
            $("#password-number-span").hide();
        }
        if (!hasSymbol) {
            $("#password-symbol-span").show();
        } else {
            $("#password-symbol-span").hide();
        }
        passwordInvalidDiv.show();
    } else {
        passwordInput.removeClass("is-invalid");
        passwordInvalidDiv.hide();
    }
    formIsValid();
});