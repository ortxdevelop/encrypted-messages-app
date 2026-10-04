function Toast({
    message,
    type = "error",
    className = ""
}) {

    if (!message) {
        return null;
    }

    return (
        <p
            className={`toast toast-${type} ${className}`}
            role="alert"
        >
            {message}
        </p>
    );
}

export default Toast;