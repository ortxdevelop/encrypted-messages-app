import API_URL from "./api";

export async function authenticatedFetch(
    endpoint,
    accessToken,
    refreshAccessToken,
    options = {}
){
    let response = await fetch(`${API_URL}${endpoint}`, {
        ...options,
        headers: {
            ...options.headers,
            Authorization: `Bearer ${accessToken}`,
        }
    });
    

    if (response.status !== 401) {
        return response;
    }

    const newAccessToken = await refreshAccessToken();

    if(!newAccessToken){
        return response;
    }

    response = await fetch(`${API_URL}${endpoint}`, {
        ...options,
        headers: {
            ...options.headers,
            Authorization: `Bearer ${newAccessToken}`,
        }
    });

    return response;
}