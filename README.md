# Gestion des stages et du suivi des étudiants

Mini-projet du module **Architectures logicielles évoluées : Framework Spring**
Institut International de Technologie (IIT) · Année universitaire 2026/2027 · Semestre 2

---

## Sommaire

1. [Présentation](#1-présentation)
2. [Acteurs et fonctionnalités](#2-acteurs-et-fonctionnalités)
3. [Architecture](#3-architecture)
4. [Modèle de données](#4-modèle-de-données)
5. [Technologies](#5-technologies)
6. [Structure du projet](#6-structure-du-projet)
7. [Installation et lancement](#7-installation-et-lancement)
8. [Configuration](#8-configuration)
9. [Organisation du travail et workflow Git](#9-organisation-du-travail-et-workflow-git)
10. [Équipe](#10-équipe)

---

## 1. Présentation

Cette application permet à un établissement universitaire de **gérer les stages de ses étudiants** : les étudiants stagiaires, les entreprises d'accueil, les enseignants encadrants et les différentes étapes du suivi (réunions, observations, progression, livrables).

L'objectif pédagogique est de mettre en pratique, dans un même projet :

- la conception d'une architecture logicielle en couches et sa modélisation UML ;
- le développement avec Spring Boot, Spring MVC, Spring Data JPA et Thymeleaf ;
- l'exposition de services REST consommés par une interface **CSR** (Client-Side Rendering) ;
- la mise en œuvre d'une interface **SSR** (Server-Side Rendering) avec Thymeleaf ;
- le travail collaboratif avec Git et GitHub (branches et Pull Requests) ;
- la conteneurisation et le déploiement avec Docker et Docker Compose.

L'application est conçue de manière **modulaire et évolutive**, avec une séparation claire des responsabilités entre les couches.

## 2. Acteurs et fonctionnalités

| Acteur | Fonctionnalités |
|---|---|
| **Administrateur pédagogique** | Gérer les étudiants et les enseignants · Gérer les entreprises d'accueil · Affecter un enseignant à un étudiant · Consulter les stages et leurs statistiques |
| **Enseignant encadrant** | Consulter les étudiants qui lui sont affectés · Gérer les réunions de suivi · Ajouter des observations et des recommandations · Valider les étapes de progression du stage |
| **Étudiant stagiaire** | Consulter les informations de son stage · Déclarer l'avancement de ses travaux · Déposer les informations ou liens vers ses livrables · Consulter les observations de son encadrant |

## 3. Architecture

L'application suit une **architecture en couches** au sein d'un monolithe Spring Boot. Deux interfaces coexistent et partagent la même couche métier.

```
        Navigateur                         Client CSR
  (pages rendues côté serveur)      (JavaScript, appels HTTP)
              │                                │
              ▼                                ▼
   ┌────────────────────┐          ┌────────────────────┐
   │ Contrôleurs MVC    │          │ Contrôleurs REST   │
   │ (Thymeleaf, SSR)   │          │ (JSON + DTO)       │
   └─────────┬──────────┘          └─────────┬──────────┘
             └───────────────┬───────────────┘
                             ▼
                  ┌────────────────────┐
                  │  Services (métier) │
                  └─────────┬──────────┘
                            ▼
                  ┌────────────────────┐
                  │ Repositories (JPA) │
                  └─────────┬──────────┘
                            ▼
                  ┌────────────────────┐
                  │   PostgreSQL 16    │
                  └────────────────────┘
```

| Couche | Rôle |
|---|---|
| `controller/mvc` | Gère les requêtes web et renvoie des pages Thymeleaf (SSR) |
| `controller/rest` | Expose l'API REST consommée par l'interface CSR |
| `dto` | Objets de transfert : évitent d'exposer directement les entités et leurs références circulaires |
| `service` | Logique métier : affectation, validation des étapes, statistiques |
| `repository` | Accès aux données via Spring Data JPA |
| `entity` | Modèle de données persistant |
| `exception` | Gestion globale et uniforme des erreurs |

## 4. Modèle de données

### Entités

| Entité | Attributs principaux |
|---|---|
| `Utilisateur` *(abstraite)* | id, nom, prénom, email (unique), mot de passe, rôle |
| `Etudiant` *(hérite de Utilisateur)* | matricule, filière, niveau |
| `Enseignant` *(hérite de Utilisateur)* | spécialité, département |
| `Entreprise` | id, nom, adresse, secteur, email, téléphone, tuteur |
| `Stage` | id, sujet, date de début, date de fin, statut |
| `Reunion` | id, date, lieu, compte rendu |
| `Observation` | id, contenu, recommandation, date |
| `EtapeProgression` | id, titre, description, statut, validée |
| `Livrable` | id, titre, lien, date de dépôt |

### Relations

- Un `Stage` est lié à **un** étudiant, **une** entreprise et, optionnellement, **un** enseignant encadrant.
- Un `Stage` contient plusieurs **réunions**, **observations**, **étapes de progression** et **livrables** (suppression en cascade).
- Une `Observation` peut référencer l'enseignant qui en est l'auteur.
- L'héritage `Utilisateur` → `Etudiant` / `Enseignant` utilise la stratégie `JOINED`.

### Énumérations

| Enum | Valeurs |
|---|---|
| `Role` | `ADMIN`, `ENSEIGNANT`, `ETUDIANT` |
| `StatutStage` | `EN_ATTENTE`, `EN_COURS`, `TERMINE`, `VALIDE` |
| `StatutEtape` | `A_FAIRE`, `EN_COURS`, `TERMINEE`, `VALIDEE` |

## 5. Technologies

| Domaine | Technologie |
|---|---|
| Langage | Java 17 |
| Framework | Spring Boot 4.2.0 |
| Web et SSR | Spring MVC, Thymeleaf |
| Persistance | Spring Data JPA, Hibernate |
| Validation | Jakarta Bean Validation |
| Base de données | PostgreSQL 16 |
| Build | Maven (wrapper inclus) |
| Productivité | Lombok |
| Déploiement | Docker, Docker Compose |
| Versionnement | Git, GitHub |

## 6. Structure du projet

```
Projet-Spring-GestionStages/
├── src/
│   ├── main/
│   │   ├── java/com/iit/gestion_stages/
│   │   │   ├── entity/            Entités JPA et énumérations
│   │   │   ├── repository/        Repositories Spring Data JPA
│   │   │   ├── service/           Logique métier
│   │   │   ├── dto/               Objets de transfert (API REST)
│   │   │   ├── controller/
│   │   │   │   ├── mvc/           Contrôleurs Thymeleaf (SSR)
│   │   │   │   └── rest/          Contrôleurs REST (CSR)
│   │   │   └── exception/         Gestion globale des erreurs
│   │   └── resources/
│   │       ├── templates/         Pages Thymeleaf
│   │       ├── static/            Ressources statiques
│   │       └── application.properties
│   └── test/                      Tests
├── frontend/                      Interface CSR
├── docs/                          Documentation (contrat d'API, etc.)
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## 7. Installation et lancement

### Prérequis

- **Git**
- **Docker Desktop** (pour le lancement complet)
- **JDK 17** et un IDE (IntelliJ IDEA ou VS Code), uniquement pour lancer l'application depuis l'IDE

### Option A : lancement complet avec Docker

```bash
git clone https://github.com/ons-rekik/Projet-Spring-GestionStages.git
cd Projet-Spring-GestionStages
git checkout develop
docker compose up --build
```

Le premier démarrage est plus long (téléchargement des images et des dépendances Maven). L'application est prête lorsque les logs affichent :

```
Started GestionStagesApplication
```

| Service | Adresse |
|---|---|
| Application | http://localhost:8080 |
| Base PostgreSQL | `localhost:5432` |

Arrêt des conteneurs :

```bash
docker compose down        # conserve les données
docker compose down -v     # supprime aussi les données (volume pgdata)
```

Les données sont stockées dans le volume Docker `pgdata` : elles **persistent** après un arrêt ou un redémarrage de la machine.

### Option B : développement (application dans l'IDE, base dans Docker)

1. Démarrer uniquement la base de données :
   ```bash
   docker compose up -d db
   ```
2. Lancer l'application depuis l'IDE (classe `GestionStagesApplication`) ou avec Maven :
   ```bash
   ./mvnw spring-boot:run          # Windows : mvnw.cmd spring-boot:run
   ```

### Dépannage

| Problème | Solution |
|---|---|
| `port 5432 is already allocated` | PostgreSQL est déjà installé sur la machine. Changer le mapping `5432:5432` en `5433:5432` dans `docker-compose.yml`, et utiliser `localhost:5433` dans `DB_URL`. |
| `port 8080 is already in use` | Arrêter le processus qui utilise le port, ou modifier le mapping du service `app`. |
| L'application démarre avant la base | Le service `app` attend que la base soit saine (`healthcheck`). Relancer `docker compose up --build`. |
| Les données ont disparu | `docker compose down -v` supprime le volume. Utiliser `docker compose down` pour les conserver. |

## 8. Configuration

La connexion à la base de données est réglée par **variables d'environnement**, avec des valeurs par défaut adaptées au développement local (fichier `application.properties`).

| Variable | Valeur par défaut | Rôle |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/gestion_stages` | URL JDBC de la base |
| `DB_USER` | `stages` | Utilisateur de la base |
| `DB_PASSWORD` | `stages123` | Mot de passe de la base |

Dans Docker Compose, `DB_URL` pointe vers le service `db` (`jdbc:postgresql://db:5432/gestion_stages`).

> Les identifiants par défaut sont réservés au développement. Ils doivent être remplacés pour tout autre usage.

Le schéma de la base est créé et mis à jour automatiquement par Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

## 9. Organisation du travail et workflow Git

### Répartition des rôles

L'énoncé prévoit trois étudiants. Le travail est réalisé à **deux**, et le rôle de l'étudiant 3 est partagé.

| Bloc de travail | Étudiante 1 | Étudiante 2 |
|---|:---:|:---:|
| Pages Thymeleaf, contrôleurs MVC, formulaires, navigation | ✔ | |
| Entités JPA et relations | ✔ | |
| Repositories Spring Data JPA | ✔ | |
| Tests de persistance | ✔ | |
| Contrôleurs REST et DTO | | ✔ |
| Interface CSR et appels HTTP | | ✔ |
| Services (logique métier) | | ✔ |
| Docker et Docker Compose | | ✔ |

### Branches

```
main        version stable
 └─ develop   intégration
     └─ feature/...   une branche par tâche
```

### Règles de collaboration

- Chaque tâche est développée dans une branche `feature/...` créée depuis un `develop` à jour.
- Chaque branche est intégrée dans `develop` par **Pull Request**, relue par l'autre étudiante.
- Commits petits et réguliers, avec des messages clairs : `feat:`, `fix:`, `docs:`, `chore:`.
- Un seul propriétaire par dossier pour limiter les conflits :

| Dossier | Propriétaire |
|---|---|
| `entity/`, `repository/`, `controller/mvc/`, `templates/` | Étudiante 1 |
| `service/`, `dto/`, `controller/rest/`, `exception/`, `frontend/`, Docker | Étudiante 2 |

- Le fichier `pom.xml` est partagé : toute modification est annoncée à l'autre étudiante avant fusion.

## 10. Équipe

| Nom | Rôle |
|---|---|
| Ons Yangui | Étudiante 1 : SSR, interface web, entités et persistance |
| Ons Rekik | Étudiante 2 : API REST, CSR, services et Docker |

**Encadrant :** Mohamed Zayani

---

*Projet réalisé dans le cadre du cours « Architectures logicielles évoluées : Framework Spring », IIT, 2026/2027.*
