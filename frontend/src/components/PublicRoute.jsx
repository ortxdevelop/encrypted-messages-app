import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthController";

function PublicRoute({ children }){
    const { isAuthenticated} = useAuth();

    if (isAuthenticated){
        return <Navigate to="/messages" replace />
    }

    return children
}

export default PublicRoute;