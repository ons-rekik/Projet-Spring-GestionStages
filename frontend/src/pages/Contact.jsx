import { useState } from 'react'
import Faq from '../components/Faq.jsx'
import BandeauCta from '../components/BandeauCta.jsx'

const ICONE = { width: 22, height: 22, viewBox: '0 0 24 24', fill: 'none', stroke: 'currentColor', strokeWidth: 2, strokeLinecap: 'round', strokeLinejoin: 'round' }

const CARTES = [
    {
        titre: 'EMAIL',
        valeur: 'info@iit.tn',
        icone: (
            <svg {...ICONE}><rect x="3" y="5" width="18" height="14" rx="2" /><path d="m3 7 9 6 9-6" /></svg>
        ),
    },
    {
        titre: 'TÉLÉPHONE',
        valeur: '(+216) 70 28 26 00',
        icone: (
            <svg {...ICONE}><path d="M5 4h4l2 5-2.5 1.5a11 11 0 0 0 5 5L15 13l5 2v4a2 2 0 0 1-2 2A16 16 0 0 1 3 6a2 2 0 0 1 2-2z" /></svg>
        ),
    },
    {
        titre: 'ADRESSE',
        valeur: 'Technopole El Ons, Sfax',
        icone: (
            <svg {...ICONE}><path d="M12 21s-7-6.2-7-11a7 7 0 0 1 14 0c0 4.8-7 11-7 11z" /><circle cx="12" cy="10" r="2.5" /></svg>
        ),
    },
]

const VIDE = { nom: '', email: '', profil: 'Étudiant', message: '' }

function valider(v) {
    const e = {}
    if (!v.nom.trim()) e.nom = 'Le nom est obligatoire'
    if (!v.email.trim()) e.email = 'L’email est obligatoire'
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v.email)) e.email = 'Email invalide'
    if (v.message.trim().length < 10) e.message = 'Le message doit contenir au moins 10 caractères'
    return e
}

export default function Contact() {
    const [valeurs, setValeurs] = useState(VIDE)
    const [erreurs, setErreurs] = useState({})
    const [pret, setPret] = useState(false)

    const changer = (e) => {
        setValeurs({ ...valeurs, [e.target.name]: e.target.value })
        setPret(false)
    }

    const envoyer = (e) => {
        e.preventDefault()
        const err = valider(valeurs)
        setErreurs(err)
        if (Object.keys(err).length > 0) return

        const sujet = `Contact plateforme stages – ${valeurs.profil}`
        const corps = `${valeurs.message}\n\n${valeurs.nom} (${valeurs.profil})\n${valeurs.email}`
        window.location.href = `mailto:info@iit.tn?subject=${encodeURIComponent(sujet)}&body=${encodeURIComponent(corps)}`
        setPret(true)
    }

    return (
        <>
            <section className="page-hero">
                <div className="halo halo-1" />
                <div className="container">
                    <h1 className="a1">Une question ? <span className="accent">Écrivez-nous.</span></h1>
                    <p className="a2">
                        Étudiants, enseignants ou entreprises d’accueil : notre équipe vous accompagne
                        pour la prise en main de la plateforme de suivi des stages.
                    </p>
                </div>
            </section>

            <div className="cartes-contact a3">
                {CARTES.map((c) => (
                    <div key={c.titre} className="carte-contact">
                        <div className="icone">{c.icone}</div>
                        <small>{c.titre}</small>
                        <strong>{c.valeur}</strong>
                    </div>
                ))}
            </div>

            <div className="container">
                <form className="formulaire rv" onSubmit={envoyer} noValidate>
                    <div>
                        <h2>Envoyer un message</h2>
                        <p className="intro">Dites-nous comment nous pouvons vous aider.</p>
                    </div>

                    <div className={`champ ${erreurs.nom ? 'invalide' : ''}`}>
                        <label htmlFor="nom">Nom complet</label>
                        <input id="nom" name="nom" value={valeurs.nom} onChange={changer} placeholder="Prénom Nom" />
                        {erreurs.nom && <span className="msg-erreur">{erreurs.nom}</span>}
                    </div>

                    <div className={`champ ${erreurs.email ? 'invalide' : ''}`}>
                        <label htmlFor="email">Adresse email</label>
                        <input id="email" name="email" type="email" value={valeurs.email} onChange={changer} placeholder="prenom.nom@exemple.tn" />
                        {erreurs.email && <span className="msg-erreur">{erreurs.email}</span>}
                    </div>

                    <div className="champ">
                        <label htmlFor="profil">Vous êtes</label>
                        <select id="profil" name="profil" value={valeurs.profil} onChange={changer}>
                            <option>Étudiant</option>
                            <option>Enseignant</option>
                            <option>Entreprise d’accueil</option>
                            <option>Autre</option>
                        </select>
                    </div>

                    <div className={`champ ${erreurs.message ? 'invalide' : ''}`}>
                        <label htmlFor="message">Message</label>
                        <textarea id="message" name="message" rows="5" value={valeurs.message} onChange={changer} placeholder="Votre message…" />
                        {erreurs.message && <span className="msg-erreur">{erreurs.message}</span>}
                    </div>

                    {pret && (
                        <div className="alerte-ok" role="status">
                            Votre messagerie s’est ouverte avec le message prérempli. Il ne reste qu’à l’envoyer.
                        </div>
                    )}

                    <button type="submit" className="btn btn-primary">Envoyer le message</button>
                </form>
            </div>

            <Faq />
            <BandeauCta />
        </>
    )
}