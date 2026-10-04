import { Link } from "react-router-dom";
import Logo from "../assets/icons/encrypted-messages-logo.svg";

function Navbar({ onLogout }) {
    return (
        <nav className="navbar">
            <div className="navbar-container">

                <Link
                    to="/messages"
                    className="navbar-brand"
                >
                    <img src={Logo} alt="Encrypted Messages logo" width={13} /> Encrypted Messages
                </Link>

            <button 
                type="button"
                className="navbar-logout"
                onClick={onLogout}
            >
                Logout
            </button>

            </div>
        </nav>
    );
}

export default Navbar;