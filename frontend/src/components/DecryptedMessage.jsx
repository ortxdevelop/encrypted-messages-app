import { useState } from "react";

function DecryptedMessage({ message, onCopy, onClear }) {

    const [copied, setCopied] = useState(false);

    const handleCopy = async () =>{
        if (!message){
            return
        }

        try{
            if (navigator.clipboard && navigator.clipboard.writeText) {
                await navigator.clipboard.writeText(message);
            } else {
                const textArea = document.createElement("textarea");
                textArea.value = message;
                textArea.style.position = "fixed";
                textArea.style.left = "-999999px";
                textArea.style.top = "-999999px";
                document.body.appendChild(textArea);
                textArea.focus();
                textArea.select();

                try {
                    document.execCommand('copy');
                } finally {
                    document.body.removeChild(textArea);
                }
            }

            setCopied(true);

            setTimeout(()=>{
                setCopied(false);
            }, 1500);

            onCopy?.("Message copied", "success");
        }catch{
            onCopy?.("Unable to copy message")
        }
    }

    return (
        <section className="decrypted-message">
            <div className="decrypted-message-header">
                <div>
                    <span className="section-label">
                        Decrypted Content
                    </span>

                    <h2>Decrypted message</h2>
                </div>

                <span className="decrypted-badge">
                    Private
                </span>
            </div>

            <div className="decrypted-content">
                {message ? (
                    <p key={message}>
                        {message}
                    </p>
                ) : (
                    <span className="decrypted-placeholder">
                        Decrypted message will appear here
                    </span>
                )}
            </div>

            <div className="decrypted-footer">

                <button
                    type="button"
                    className="copy-button"
                    onClick={onClear}
                    disabled={!message}
                >
                    Clear
                </button>

                <button
                    type="button"
                    className="copy-button"
                    onClick={handleCopy}
                    disabled={!message}
                >
                    {copied ? "Copied" : "Copy"}
                </button>

            </div>
        </section>
    );
}

export default DecryptedMessage;