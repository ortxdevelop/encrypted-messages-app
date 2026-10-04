import { useNavigate } from "react-router-dom";
import { logoutRequest } from "../services/authApi";
import { useAuth } from "../context/AuthController";
import { useEffect, useState } from "react";
import {
    createMessage,
    getMessages,
    decryptMessage
} from "../services/messageApi";
import MessageList from "../components/MessageList";
import Navbar from "../components/Navbar";
import MessageForm from "../components/MessageForm";
import DecryptedMessage from "../components/DecryptedMessage";
import Toast from "../components/Toast";
import Footer from "../components/Footer"
import LoadingSpinner from "../components/LoadingSpinner";

function MessagesPage() {

    const navigate = useNavigate();

    const {
        accessToken,
        logout,
        refreshAccessToken,
        username
    } = useAuth();

    const greetings = [
        "Good to see you",
        "Nice to have you here",
        "Welcome",
        "Hey",
        "Glad to see you",
        "Hello"
    ];

    const [greeting] = useState(() =>{
        const index = Math.floor(Math.random() * greetings.length );
        return greetings[index]
    })

    const [messages, setMessages] = useState([]);

    const [page, setPage] = useState(0);

    const [totalPages, setTotalPages] = useState(0);

    const PAGE_SIZE = 20;

    const [toast, setToast] = useState({
        message: "",
        type: "error",
        id: 0
    });

    const [loading, setLoading] = useState(true);

    const [text, setText] = useState("");
    const [creating, setCreating] = useState(false);
    const [decryptingId, setDecryptingId] = useState(null);
    const [decryptedMessage, setDecryptedMessage] = useState("");

    const showToast = (message, type = "error") => {
        setToast((prev) => ({
            message,
            type,
            id: prev.id + 1
        }));
    };

    const handleLogout = async () => {

        try {
            await logoutRequest();
        } finally {
            logout();
            navigate("/login");
        }
    };

    const loadMessages = async (pageNumber = 0) => {
        try {
            setLoading(true);

            const response = await getMessages(
                accessToken,
                refreshAccessToken,
                pageNumber,
                PAGE_SIZE
            );

            const data = await response.json();

            if (!response.ok) {
                showToast(
                    data.error || "Unable to load messages"
                );
                return;
            }

            setMessages(data.content ?? []);
            setPage(data.number ?? 0);
            setTotalPages(data.totalPages ?? 0);
        }catch{
            showToast("Unable to connect to the server");
        }finally{
            setLoading(false);
        }
    }


    const handleCreateMessage = async (event) => {

        event.preventDefault();

        if (!text.trim()) {
            showToast("Message cannot be empty");
            return;
        }

        try {
            setCreating(true);

            const response = await createMessage(
                accessToken,
                refreshAccessToken,
                text
            );

            const data = await response.json();

            if (!response.ok) {
                showToast(
                    data.error || "Unable to create message"
                );
                return;
            }

            setText("");

            await loadMessages(0);

            showToast("Message was saved in Your messages", "success");

        } catch {
            showToast("Unable to connect to the server");

        } finally {
            setCreating(false);
        }
    };

    const handleDecrypt = async (messageId) => {

        try {
            setDecryptingId(messageId);

            const response = await decryptMessage(
                accessToken,
                refreshAccessToken,
                messageId
            );

            const message = await response.json();

            if (!response.ok) {
                showToast(
                    "Unable to decrypt message"
                );
                return;
            }

            setDecryptedMessage(message.text);

        } catch {
            showToast("Unable to connect to the server");

        } finally {
            setDecryptingId(null);
        }
    };

    const usernameFormat = (username) => {
        return String(username).charAt(0).toUpperCase() + String(username).slice(1);
    }

    useEffect(() => {
        loadMessages(0);
    }, [accessToken]);

    const handlePageChange = (newPage) => {
        loadMessages(newPage);
    };

    useEffect(() => {

        if (!toast.message) {
            return;
        }

        const timer = setTimeout(() => {
            setToast((prev) => ({
                ...prev,
                message: ""
            }));
        }, 3000);

        return () => clearTimeout(timer);

    }, [toast.id]);


    return (
        <>
            <Navbar onLogout={handleLogout} />

            <main className="messages-page">

                <header className="page-header">

                    <span className="section-label">
                        Secure Storage
                    </span>

                    <h1>{greeting}, {usernameFormat(username)}</h1>

                    <p>
                        Store and decrypt your messages securely
                    </p>

                </header>

                <Toast
                    key={toast.id}
                    message={toast.message}
                    type={toast.type}
                />

                {loading ? (
                    <div className="messages-loading">
                        <LoadingSpinner />
                    </div>
                ) : (

                    <>

                <div className="top-grid">

                    <MessageForm
                        text={text}
                        setText={setText}
                        creating={creating}
                        onSubmit={handleCreateMessage}
                    />

                    <DecryptedMessage
                        message={decryptedMessage}
                        onCopy={showToast}
                        onClear={() => setDecryptedMessage("")}
                    />

                </div>

                <MessageList
                    messages={messages}
                    onDecrypt={handleDecrypt}
                    decryptingId={decryptingId}
                    page={page}
                    totalPages={totalPages}
                    onPageChange={handlePageChange}
                />

                </>
                )}

            </main>

            <Footer />
        </>
    );
}

export default MessagesPage;