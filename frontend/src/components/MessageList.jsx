function MessageList({
    messages,
    onDecrypt,
    decryptingId,
    page,
    totalPages,
    onPageChange
}) {
    if (messages.length === 0) {
        return (
            <section className="message-list">
                <div className="message-list-empty">
                    <h2>No messages yet</h2>

                    <p>
                        Your encrypted messages will appear here
                    </p>
                </div>
            </section>
        );
    }

    return (

    <section className="message-list">

        <div className="message-list-header">

            <div>
                <span className="section-label">
                    Secure Storage
                </span>

                <h2>Your messages</h2>
            </div>

            <span className="message-count">
                {messages.length}
            </span>
        </div>

         <ul className="message-list-items">
            {messages.map((message) => (
                <li
                    className="message-item"
                    key={message.id}
               >
                    <div className="message-item-content">
                        <span className="message-item-label">
                            Encrypted
                        </span>

                        <p className="encrypted-text">
                            {message.text}
                        </p>
                    </div>

                    <button
                        type="button"
                        className="decrypt-button"
                        onClick={() => onDecrypt(message.id)}
                        disabled={decryptingId === message.id}
                    >
                        {decryptingId === message.id
                            ? "Decrypting..."
                            : "Decrypt"
                          }
                    </button>
                </li>
            ))}
        </ul>

        <div className="message-pagination">

            <button
                type="button"
                onClick={() => onPageChange(page - 1)}
                disabled={page === 0}
            >
                Previous
            </button>

            <span>
                Page {page + 1} of {totalPages}
            </span>

            <button
                type="button"
                onClick={() => onPageChange(page + 1)}
                disabled={page + 1 >= totalPages}
            >
                Next
            </button>   

        </div>



    </section>

    );
}

export default MessageList;