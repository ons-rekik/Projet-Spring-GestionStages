import { Link } from 'react-router-dom'
import logo from '../assets/logo-iit.png'

export default function Footer() {
    return (
        <footer className="footer">
            <div className="container">
                <div className="footer-grid">
                    <div style={{ lineHeight: 1.6 }}>
                        <span className="footer-logo"><img src={logo} alt="IIT" /></span>
                        <p style={{ margin: 0 }}>
                            Plateforme de suivi des stages de l’Institut International de Technologie.
                        </p>
                        <p>info@iit.tn<br />(+216) 70 28 26 00</p>
                    </div>
                    <div>
                        <b>Principal</b>
                        <Link to="/">Accueil</Link>
                        <Link to="/#fonctionnalites">Fonctionnalités</Link>
                        <Link to="/#faq">FAQ</Link>
                        <Link to="/contact">Contact</Link>
                    </div>
                    <div>
                        <b>L’IIT</b>
                        <span>Formations</span>
                        <span>International</span>
                        <span>AI Hub</span>
                        <span>Vie étudiante</span>
                    </div>
                    <div>
                        <b>Légal</b>
                        <span>Mentions légales</span>
                        <span>Confidentialité</span>
                        <span>Conditions d’utilisation</span>
                    </div>
                </div>
                <div className="footer-bas">
                    <span>© 2026 IIT · Route de Tunis km 10, Technopole El Ons, Sfax</span>
                    <span>Mini-projet Spring Boot · 2026 / 2027</span>
                </div>
            </div>
        </footer>
    )
}