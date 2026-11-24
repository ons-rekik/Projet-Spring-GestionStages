import { Link } from 'react-router-dom'

export default function BandeauCta() {
    return (
        <section className="section rv">
            <div className="container">
                <div className="cta">
                    <div className="halo" />
                    <h2>Prêt à mettre ton suivi de stage en <span>pilote automatique ?</span></h2>
                    <p>Fini les échanges dispersés : tout le suivi est centralisé pour toute l’équipe pédagogique.</p>
                    <Link to="/connexion" className="btn btn-blanc btn-lg">Se connecter →</Link>
                </div>
            </div>
        </section>
    )
}