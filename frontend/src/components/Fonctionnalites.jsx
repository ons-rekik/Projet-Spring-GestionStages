export default function Fonctionnalites() {
    return (
        <section id="fonctionnalites" className="section rv">
            <div className="container">
                <p className="eyebrow">FONCTIONNALITÉS</p>
                <h2 className="titre-section" style={{ maxWidth: 640 }}>
                    Tout ce qu’il faut pour <span className="accent">piloter un stage.</span>
                </h2>
                <div className="bento">
                    <div className="tuile large bleue">
                        <h3>Affectation d’encadrant</h3>
                        <p>L’administration associe chaque étudiant à un enseignant, en quelques clics.</p>
                        <div className="avatars">
                            <span className="avatar" style={{ background: '#fff', color: 'var(--accent)' }}>SB</span>
                            <span className="avatar" style={{ background: 'var(--cyan)', color: 'var(--ink)' }}>AT</span>
                            <span className="avatar" style={{ background: '#fbbf24', color: 'var(--ink)' }}>MJ</span>
                        </div>
                    </div>

                    <div className="tuile">
                        <h3>Avancement par étapes</h3>
                        <p>Chaque étape est déclarée puis validée.</p>
                        <div className="piste-grosse"><i /></div>
                    </div>

                    <div className="tuile">
                        <h3>Réunions de suivi</h3>
                        <p>Planifiées, avec compte rendu.</p>
                        <div className="rendez-vous">
                            <div className="date-badge">12<small>MARS</small></div>
                            <div>Point d’avancement<br /><span className="detail">10h00 · IIT Sfax</span></div>
                        </div>
                    </div>

                    <div className="tuile">
                        <h3>Livrables</h3>
                        <p>Liens et rapports centralisés.</p>
                        <div className="fichier">Rapport_v1.pdf <b>✓</b></div>
                    </div>

                    <div className="tuile large">
                        <h3>Observations et recommandations</h3>
                        <p>L’encadrant guide, l’étudiant lit et progresse.</p>
                        <div className="bulle">Très bon travail. Pense à ajouter des tests.</div>
                    </div>

                    <div className="tuile">
                        <h3>Statistiques</h3>
                        <p>Vue d’ensemble en direct.</p>
                        <div className="colonnes">
                            <i style={{ height: 30, background: 'var(--cyan)' }} />
                            <i style={{ height: 56, background: 'var(--accent)' }} />
                            <i style={{ height: 42, background: 'var(--cyan)' }} />
                            <i style={{ height: 70, background: 'var(--accent)' }} />
                        </div>
                    </div>
                </div>
            </div>
        </section>
    )
}