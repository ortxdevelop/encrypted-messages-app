import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { registerRequest } from "../services/authApi";
import LoadingSpinner from "../components/LoadingSpinner";
import { validateUsername, validatePassword } from "../utils/validation";

function RegisterPage(){
    const navigate = useNavigate();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleSubmit = async(event) => {
        event.preventDefault();

        setError("");

        const usernameError = validateUsername(username);

        if (usernameError) {
            setError(usernameError);
            return;
        }

        const passwordError = validatePassword(password);

        if (passwordError) {
            setError(passwordError);
            return;
        }

        try{
            setLoading(true);

            const response = await registerRequest(
                username.trim(),
                password
            );

            const data = await response.json();

            if (!response.ok) {
                setError(data.error || "Something went wrong");
                return;
            }

            navigate("/login");

        } catch {
            setError("Unable to connect to the server");

        } finally {
            setLoading(false);
        }
    };

    return (
        <main className="auth-page">

            <div className="auth-container">

                <section className="auth-card">

                    <h1>Create an account</h1>

                    <form
                        className="auth-form"
                        onSubmit={handleSubmit}
                    >

                        <div className="auth-field">

                            <label htmlFor="username">
                                Username
                            </label>

                            <input
                                id="username"
                                name="username"
                                type="text"
                                autoComplete="username"
                                value={username}
                                onChange={(event) =>
                                    setUsername(event.target.value)
                                }
                                maxLength={20}
                                disabled={loading}
                            />

                        </div>

                        <div className="auth-field">

                            <label htmlFor="password">
                                Password
                            </label>

                            <input
                                id="password"
                                name="password"
                                type="password"
                                autoComplete="new-password"
                                value={password}
                                onChange={(event) =>
                                    setPassword(event.target.value)
                                }
                                disabled={loading}
                            />

                        </div>

                        {error && (
                        <p className="auth-error">
                            {error}
                        </p>
                        )}

                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? (
                            <LoadingSpinner />
                        ) : (
                            "Register"
                        )}
                    </button>

                    </form>


                    <p className="auth-switch">
                        Already have an account?{" "}
                        <Link to="/login">
                            Login
                        </Link>
                    </p>

                </section>

            </div>

        </main>
    );
}

export default RegisterPage;