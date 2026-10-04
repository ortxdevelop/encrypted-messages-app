import { useEffect } from "react";
import { useLocation } from "react-router-dom";

function PageTitle() {
    const location = useLocation();

    useEffect(() => {
        const titles = {
            "/login": "Login | Encrypted Messages",
            "/register": "Register | Encrypted Messages",
            "/messages": "Messages | Encrypted Messages"
        };

        document.title =
            titles[location.pathname] || "Encrypted Messages";
    }, [location.pathname]);

    return null;
}

export default PageTitle;