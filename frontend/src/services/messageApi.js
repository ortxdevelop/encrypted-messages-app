import { authenticatedFetch } from "./authenticatedFetch";

export async function getMessages(
    accessToken,
    refreshAccessToken,
    page = 0,
    size = 20
){
    const url = `/api/messages?page=${page}&size=${size}`;

    return authenticatedFetch(
        url,
        accessToken,
        refreshAccessToken,
        {
            method: "GET",
        }
    );
}

export async function createMessage(
    accessToken,
    refreshAccessToken,
    text
) {
    return authenticatedFetch(
        "/api/messages",
        accessToken,
        refreshAccessToken,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                text,
            }),
        }
    );
}

export async function decryptMessage(
    accessToken,
    refreshAccessToken,
    messageId
) {
    return authenticatedFetch(
        `/api/messages/${messageId}/decrypt`,
        accessToken,
        refreshAccessToken,
        {
            method: "POST",
        }
    );
}