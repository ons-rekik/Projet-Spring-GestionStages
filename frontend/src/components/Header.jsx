import { Link, NavLink } from 'react-router-dom'
import logo from '../assets/logo-iit.png'

export default function Header() {
    return (
        <header className="header">
            <div className="header-in">
                <Link to="/" className="logo" aria-label="IIT, accueil">
                    <img src={logo} alt="Institut International de Technologie" />
                </Link>
                <nav className="nav" aria-label="Navigation principale">
                    <NavLink to="/" end>Accueil</NavLink>
                    <Link to="/#fonctionnalites">Fonctionnalités</Link>
                    <Link to="/#faq">FAQ</Link>
                    <NavLink to="/contact">Contact</NavLink>
                </nav>
                <Link to="/connexion" className="btn btn-primary">Connexion</Link>
            </div>
        </header>
    )
}