const QUESTIONS = [
    {
        q: 'Comment me connecter à la plateforme ?',
        r: 'Utilise l’adresse email et le mot de passe fournis par l’administration de l’IIT. Ton espace s’ouvre selon ton rôle : étudiant, enseignant ou administrateur.',
    },
    {
        q: 'Qui choisit mon enseignant encadrant ?',
        r: 'L’administration pédagogique affecte un enseignant à chaque stage. Tu vois son nom dès l’affectation, dans la page de ton stage.',
    },
    {
        q: 'Comment déclarer l’avancement de mon stage ?',
        r: 'Dans ton espace, tu passes chaque étape à « en cours » puis « terminée ». C’est ensuite ton encadrant qui la valide : une étape validée ne peut plus être modifiée.',
    },
    {
        q: 'Quels livrables puis-je déposer ?',
        r: 'Tu peux déposer un titre et un lien (dépôt GitHub, rapport en ligne, présentation) pour chaque livrable de ton stage.',
    },
    {
        q: 'Comment voir les conseils de mon encadrant ?',
        r: 'Les observations et recommandations s’affichent dans la page de ton stage, avec leur date et le nom de l’enseignant qui les a rédigées.',
    },
]

export default function Faq() {
    return (
        <section id="faq" className="section rv">
            <div className="container">
                <p className="eyebrow centre">FAQ</p>
                <h2 className="titre-section centre">
                    Réponses rapides pour <span className="accent">bien démarrer</span>
                </h2>
                <div className="faq">
                    {QUESTIONS.map((item) => (
                        <details key={item.q}>
                            <summary>{item.q}</summary>
                            <p>{item.r}</p>
                        </details>
                    ))}
                </div>
            </div>
        </section>
    )
}