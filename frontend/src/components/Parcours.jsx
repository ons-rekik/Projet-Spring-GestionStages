const ETAPES = [
    { titre: 'Affectation', texte: 'Le stage est créé et un encadrant est désigné.' },
    { titre: 'Avancement', texte: 'L’étudiant déclare ses étapes et dépose ses livrables.' },
    { titre: 'Validation', texte: 'L’enseignant observe, conseille et valide.' },
]

export default function Parcours() {
    return (
        <section className="section rv">
            <div className="container">
                <h2 className="titre-section centre">
                    Trois étapes, <span className="accent">zéro friction.</span>
                </h2>
                <div className="parcours">
                    <div className="ligne" />
                    {ETAPES.map((e, i) => (
                        <div key={e.titre}>
                            <span className="rond">{i + 1}</span>
                            <h3>{e.titre}</h3>
                            <p>{e.texte}</p>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    )
}