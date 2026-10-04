import API_URL from "./api";

export async function loginRequest(username, password){
    return fetch(`${API_URL}/api/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type" : "application/json"
        },
        credentials: "include",
        body: JSON.stringify({
            username,
            password,
        }),

    })
}

export async function registerRequest(username, password){
    return fetch(`${API_URL}/api/auth/register`, {
        method: "POST",
        headers: {
            "Content-Type" : "application/json"
        },
        body: JSON.stringify({
            username,
            password,
        }),

    })
}

export async function logoutRequest() {
    return fetch(`${API_URL}/api/auth/logout`, {
        method: "POST",
        credentials: "include",
    });
}

export async function refreshRequest() {
    return fetch(`${API_URL}/api/auth/refresh`,{
        method: "POST",
        credentials: "include",
    });
}