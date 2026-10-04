export function validateUsername(username){
    const value = username.trim();

    if (!value) {
        return "Username is required";
    }

    if (value.length < 3) {
        return "Username must be at least 3 characters";
    }

    if (value.length > 20) {
        return "Username cannot exceed 20 characters";
    }

    if (!/^[a-zA-Z0-9_]+$/.test(value)) {
        return "Username can contain only letters, numbers and _";
    }

    return "";
}

export function validatePassword(password){
    if (!password) {
        return "Password is required";
    }

    if (password.length < 8) {
        return "Password must be at least 8 characters";
    }

    if (password.length > 64) {
        return "Password cannot exceed 64 characters";
    }

    if (!/[a-z]/.test(password)) {
        return "Password must contain at least one lowercase letter";
    }

    if (!/[A-Z]/.test(password)) {
        return "Password must contain at least one uppercase letter";
    }

    if (!/[0-9]/.test(password)) {
        return "Password must contain at least one number";
    }

    return "";
}