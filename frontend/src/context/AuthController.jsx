import { createContext, useContext, useState } from "react";
import { refreshRequest } from "../services/authApi";

const AuthContext = createContext(null);

export function AuthProvider({ children }){

    const [accessToken, setAccessToken] = useState(
        localStorage.getItem("accessToken")
    )

    const [username, setUsername] = useState(
        localStorage.getItem("username")
    );

    const login = (token, username)=>{
        localStorage.setItem("accessToken", token);
        localStorage.setItem("username", username);

        setAccessToken(token);
        setUsername(username);
    }

    const logout = ()=>{
        localStorage.removeItem("accessToken");
        localStorage.removeItem("username");

        setAccessToken(null);
        setUsername(null);
    }

    const refreshAccessToken = async () => {
        const response = await refreshRequest();

        if (!response.ok) {
            logout();
            return null;
        }

        const data = await response.json();

        localStorage.setItem("accessToken", data.token);
        setAccessToken(data.token);

        return data.token;
    };

    return (
        <AuthContext.Provider
        value={{
            accessToken,
            isAuthenticated: !!accessToken,
            login,
            logout,
            refreshAccessToken,
            username
        }}
        >
            {children}
        </AuthContext.Provider>
    )

}

export function useAuth() {
    return useContext(AuthContext);
}