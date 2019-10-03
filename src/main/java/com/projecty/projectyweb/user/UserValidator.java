package com.projecty.projectyweb.user;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }
    
    // validation for user input user name, password and password validation
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.empty.user.username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.user.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordRepeat", "passwordRepeat.empty.user.passwordRepeat");

        User user = (User) target;

        if (!user.getPassword().equals(user.getPasswordRepeat())) {
            errors.rejectValue("passwordRepeat", "passwordRepeat.diff.user.passwordRepeat");
        }

        if (user.getPassword().length() < 8) {
            errors.rejectValue("password", "password.short.user.password");
        }

        if (user.getPassword().length() > 30) {
            errors.rejectValue("password", "password.long.user.password");
        }

        if (user.getEmail() != null) {
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(user.getEmail());
            if (!matcher.matches()) {
                errors.rejectValue("email", "email.invalid.user.email");
            }
        }
    }
}
