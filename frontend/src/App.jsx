import { Navigate, Route, Routes } from "react-router-dom";

import LoginPage from './pages/LoginPage';
import RegisterPage from "./pages/RegisterPage";
import ProtectedRoute from "./components/ProtectedRoute";
import PublicRoute from "./components/PublicRoute";
import MessagesPage from "./pages/MessagesPage";
import PageTitle from "./components/PageTitle";

function App(){
    return (
        <>

        <PageTitle />
        
        <Routes>
            <Route
            path="/login"
            element={
                <PublicRoute>
                    <LoginPage />
                </PublicRoute>

            }
            />

            <Route
            path="/register"
            element={
                <PublicRoute>
                    <RegisterPage />
                </PublicRoute>
            }/>

        <Route
            path="*" element={<Navigate to="/login" replace />}
        />

        <Route 
            path="/messages"
            element={
                <ProtectedRoute>
                    <MessagesPage />
                </ProtectedRoute>
            }
        />
        
        </Routes>

        </>

    )
}

export default App;