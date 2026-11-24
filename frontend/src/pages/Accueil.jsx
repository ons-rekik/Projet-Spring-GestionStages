import { Link } from 'react-router-dom'

const FILIERES = [
    'Génie Civil', 'Génie des procédés', 'Génie Informatique', 'Génie Mécanique',
    'Génie Industriel', 'Architecture', 'Licence', 'Mastère',
]

const STATS = [
    { valeur: 24, label: 'Stages', classe: 'bleu' },
    { valeur: 15, label: 'En cours', classe: 'cyan' },
    { valeur: 6, label: 'En attente', classe: 'ambre' },
    { valeur: 3, label: 'Validés', classe: 'vert' },
]

const ETAPES = [
    { titre: 'Analyse des besoins', etat: 'Validée', pct: 100, classe: 'vert' },
    { titre: 'Développement', etat: '65 %', pct: 65, classe: 'bleu' },
    { titre: 'Rapport final', etat: '20 %', pct: 20, classe: 'ambre' },
]

export default function Accueil() {
    return (
        <>
            <section className="hero">
                <div className="halo halo-1" />
                <div className="halo halo-2" />
                <div className="container">
                    <p className="pastille a1"><i />Plateforme de suivi des stages · IIT Sfax</p>
                    <h1 className="a2">
                        Le suivi de votre{' '}
                        <span className="mots">
              <span>stage</span><span>projet</span><span>parcours</span>
            </span>
                        <br />pensé pour réussir.
                    </h1>
                    <p className="sous-titre a3">
                        Étudiants, enseignants encadrants et administration collaborent sur chaque
                        stage : étapes, réunions, livrables et validation.
                    </p>
                    <div className="hero-actions a4">
                        <Link to="/connexion" className="btn btn-primary btn-lg">Se connecter →</Link>
                        <a href="#fonctionnalites" className="btn btn-outline btn-lg">Découvrir</a>
                    </div>
                </div>

                <div className="fenetre">
                    <div className="chip c1"><i style={{ background: 'var(--green)' }} />Étape validée</div>
                    <div className="chip c2"><i style={{ background: 'var(--accent)' }} />Réunion demain à 10h</div>
                    <div className="chip c3"><i style={{ background: 'var(--cyan)' }} />Livrable déposé</div>
                    <div className="chip c4"><i style={{ background: 'var(--amber)' }} />Nouvelle observation</div>

                    <div className="fenetre-cadre">
                        <div className="fenetre-barre">
                            <i style={{ background: '#f87171' }} />
                            <i style={{ background: '#fbbf24' }} />
                            <i style={{ background: '#34d399' }} />
                            <span>iit-stages · espace administration</span>
                        </div>
                        <div className="fenetre-corps">
                            <div className="mini-stats">
                                {STATS.map((s) => (
                                    <div key={s.label} className={`mini-stat ${s.classe}`}>
                                        <div className="nb"><span className="cpt" style={{ '--fin': s.valeur }} /></div>
                                        <small>{s.label}</small>
                                    </div>
                                ))}
                            </div>
                            <div className="etapes">
                                {ETAPES.map((e) => (
                                    <div key={e.titre} className={e.classe}>
                                        <div className="etape-ligne"><b>{e.titre}</b><span>{e.etat}</span></div>
                                        <div className="piste"><i style={{ '--w': `${e.pct}%` }} /></div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <div className="defile-zone" aria-hidden="true">
                <div className="defile">
                    {[...FILIERES, ...FILIERES].map((f, i) => <span key={i}>{f}</span>)}
                </div>
            </div>
        </>
    )
}