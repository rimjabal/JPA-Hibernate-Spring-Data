Rapport de TP : JPA, Hibernate et Spring Data JPA

1. Introduction

Ce projet porte sur la mise en œuvre de la couche de persistance dans une application Java basée sur le framework Spring. L'objectif est d'utiliser JPA (Java Persistence API) avec Hibernate comme fournisseur d'implémentation, et de simplifier l'accès aux données grâce à Spring Data JPA.

<img width="1171" height="287" alt="Capture d&#39;écran 2026-04-10 215055" src="https://github.com/user-attachments/assets/2540dc15-3869-46d4-b116-ee7227b07d22" />
<img width="860" height="300" alt="Capture d&#39;écran 2026-04-10 222115" src="https://github.com/user-attachments/assets/49691fd2-2db6-4a43-8fae-3c3359bd08c4" />

2. Objectifs du TP

Comprendre le concept de l'ORM (Object-Relational Mapping).

Configurer une source de données (H2 Database ou MySQL).

Créer des entités JPA avec des annotations (@Entity, @Id, @GeneratedValue, etc.).

Utiliser les interfaces JpaRepository pour les opérations CRUD.

Explorer les méthodes de recherche personnalisées (Query Methods).

3. Architecture et Technologies

Le projet utilise :

Spring Boot : Pour une configuration rapide et un serveur embarqué.

Spring Data JPA : Pour faire abstraction de la complexité de l'API JPA.

Hibernate : Le moteur ORM qui assure le mapping entre les objets Java et les tables SQL.

H2/MySQL : Base de données pour le stockage.

4. Implémentation

4.1. Définition des Entités

Les classes métier (ex: Patient) sont annotées pour devenir des entités persistantes.

@Entity : Marque la classe comme une table de base de données.

@Id : Définit la clé primaire.

@Temporal : Gère les formats de date.

4.2. Couche DAO (Spring Data)

Au lieu de créer des implémentations manuelles avec EntityManager, nous utilisons des interfaces qui héritent de JpaRepository<T, ID>.

public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Spring génère automatiquement l'implémentation
    List<Patient> findByNameContains(String mc);
}


4.3. Configuration de la Persistence

Dans le fichier application.properties, on définit la stratégie de génération de la base de données :

spring.jpa.hibernate.ddl-auto=update : Pour mettre à jour le schéma automatiquement.

spring.jpa.show-sql=true : Pour afficher les requêtes SQL générées dans la console.

5. Fonctionnalités Implémentées

Insertion : Ajout de nouveaux enregistrements via .save().

Consultation : Récupération par ID ou liste complète.

Mise à jour : Modification d'objets existants.

Suppression : Retrait d'enregistrements via .deleteById().

Pagination et Tri : Utilisation de Pageable pour gérer de grands volumes de données.

6. Conclusion

L'utilisation de Spring Data JPA réduit considérablement le code "boilerplate" (code répétitif). L'intégration de Hibernate permet de manipuler des objets Java tout en laissant le framework gérer la complexité des transactions et des requêtes SQL, ce qui améliore la productivité et la robustesse de l'application.
