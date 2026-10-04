function MessageForm({
    text,
    setText,
    creating,
    onSubmit,
    error
}) {
    return (
        <section className="message-form">

            <div className="message-form-header">
                <div>
                    <span className="section-label">
                        Encrypted Storage
                    </span>

                    <h2>New message</h2>
                </div>

                <span className="encryption-badge">
                    AES-256-GCM
                </span>
            </div>

            <form onSubmit={onSubmit}>

                <label htmlFor="message">
                    Message
                </label>

                <textarea
                    id="message"
                    name="message"
                    value={text}
                    onChange={(event) => setText(event.target.value)}
                    disabled={creating}
                    placeholder="Write your message"
                    maxLength={5000}
                />

                {error && (
                    <p
                        className="error-message"
                        role="alert"
                    >
                        {error}
                    </p>
                )}

                <div className="message-form-footer">

                    <span className="message-hint">
                        Your message will be encrypted before storage
                    </span>

                    <button
                        type="submit"
                        disabled={creating}
                    >
                        {creating
                            ? "Encrypting"
                            : "Encrypt message"
                        }
                    </button>

                </div>

            </form>

        </section>
    );
}

export default MessageForm;