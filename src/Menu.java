import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    /* ----------------------------------------------------------------------------------------- */
    Scanner sc = new Scanner(System.in);
    Connect baseDonnees = new Connect();

    /* ----------------------------------------------------------------------------------------- */
    public Menu() {
    }

    /* ----------------------------------------------------------------------------------------- */
    public void execute() {
        boolean boucle = true;
        while (boucle) {
            menuPrincipal();
            String selection = sc.nextLine();
            switch (selection) {//implémenter d'autres cases pour ajouter des options au menu
                case "1":
                    sousMenu(1);
                    break;
                case "2":
                    sousMenu(2);
                    break;
                case "3":
                    sousMenu(3);
                    break;
                case "4":
                    sousMenu(4);
                    break;
                case "5":
                    sousMenu(5);
                    break;
                case "6":
                    sousMenu(6);
                    break;
                case "7":
                    sousMenu(7);
                    break;
                case "8":
                    boucle = false; // quitter la boucle
                    break;
                default:
                    System.out.println("Entrée invalide. Réessayer, s'il vous plaît.");
            }
        }
        System.out.println("Au revoir");
        sc.close();
        baseDonnees.close();
    }

    /* ----------------------------------------------------------------------------------------- */
    private static void clear() {
        // vide le contenu du terminal
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.print("\033[H\033[2J");
        }
    }

    /* ----------------------------------------------------------------------------------------- */
    private void menuPrincipal() {
        clear();
        System.out.println();
        System.out.println("\t\tMenu principal");
        System.out.println("\t-----------------------------------------------------");
        System.out.println("\t 1. Affichage de la liste de tous les étudiants de l’école");
        System.out.println("\t 2. Affichage de la liste des étudiants d’un cours");
        System.out.println("\t 3. Ajout d’un nouvel étudiant");
        System.out.println("\t 4. Suppression d’un étudiant");
        System.out.println("\t 5. Inscription d’un étudiant à un cours");
        System.out.println("\t 6. Désinscription d’un étudiant à un cours");
        System.out.println("\t 7. Entrée ou modification de la note d’un étudiant à un cours");
        System.out.println("\t 8. Sortir de l'application");
        System.out.println("\t------------------------------------------------------");
        System.out.println();
        System.out.print("\tSVP, sélectionner une option de 1 à 8");
        System.out.println();
        System.out.println();
    }

    /* ----------------------------------------------------------------------------------------- */
    private void sousMenu(int menuPrincipalChoix) {
        clear();
        switch (menuPrincipalChoix) {
            /* ----------------------------------------------------------------------------------------- */
            case 1: {
                // 1. La liste de tous les étudiants
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("liste de tous les étudiants\n");
                System.out.println("NumeroDossier\t\t\tCodePermanent\t\t\tNom de étudiants\t\tMoyennes");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
                System.out.println(baseDonnees.traiterLigne(
                        "SELECT et.NumeroDossier, et.CodePermanent, et.Nom,  ROUND(AVG(ec.note), 2) as Moyennes " +
                                "FROM etudiant as et " +
                                "INNER JOIN etudiant_cours AS ec " +
                                "ON ec.NumeroDossier = et.NumeroDossier " +
                                "WHERE ec.note IS NOT NULL " +
                                "GROUP BY et.NumeroDossier " +
                                "ORDER BY et.NumeroDossier"));

            }
            break;
            /* ----------------------------------------------------------------------------------------- */
            case 2: {
                // 2. Affichage de la liste de tous la liste des étudiants d’un cours
                System.out.println();
                ArrayList<String> Liste = new ArrayList<String>();
                Liste =
                        baseDonnees.traiterColonne(
                        "SELECT NumeroCours, Titre "
                                + "FROM cours "
                                + "ORDER BY NumeroCours");
                int i = 0;
                for (String choix : Liste) {
                    System.out.print(++i);
                    System.out.print(") ");
                    System.out.println(choix);
                }
                System.out.println("---------------------------");
                System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                System.out.println();
                int selection;
                try {
                    selection = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    selection = -1;
                }
                while (selection < 1 || selection > Liste.size()) {
                    System.out.println("Mauvaise sélection");
                    System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                    try {
                        selection = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        selection = -1;
                    }
                }
                System.out.println("liste des étudiants du cours  " + Liste.get(selection - 1));
                String NumeroCours = Liste.get(selection-1).split(" ")[0] ;
                System.out.println("---------------------------------------------------------------------------------------------------------------------------");
                System.out.println("Note\t\t\tNumero Dossier\t\tCode Permanent\t");
                System.out.println("---------------------------------------------------------------------------------------------------------------------------");
                System.out.println(baseDonnees.traiterLigne(
                        "SELECT ec.note, et.NumeroDossier, et.CodePermanent "
                                + "FROM etudiant AS et "
                                + "INNER JOIN etudiant_cours AS ec "
                                + "ON ec.NumeroDossier = et.NumeroDossier "
                                + "WHERE ec.NumeroCours = " + NumeroCours
                                + " ORDER BY et.CodePermanent")
                );
                System.out.print("Moyen du group: ");
                System.out.println(baseDonnees.traiterLigne(
                        "SELECT ROUND(AVG(ec.note), 2) "
                                + "FROM etudiant_cours AS ec"
                ));
                System.out.println();
                System.out.println("---------------------------");

            }
            break;
            /* ----------------------------------------------------------------------------------------- */
            case 3: {
                // 3. Ajout d’un nouvel étudiant
                String codePermanent = null;
                String nom = null;
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("ajout d'un étudiant\n");
                System.out.println("---------------------------");
                System.out.println();
                System.out.print("entrer le code permanent du étudiant: ");
                codePermanent = sc.nextLine();
                System.out.print("entrer le nom du étudiant: ");
                nom = sc.nextLine();
                System.out.println(baseDonnees.traiterMiseajour(
                        "INSERT INTO etudiant (CodePermanent, nom) "
                                + "VALUES ("
                                + "'" + codePermanent + "',"
                                + "'" + nom + "')"));

            }
            break;
            /* ----------------------------------------------------------------------------------------- */
            case 4: {
                // 4. Suppression d’un étudiant
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("Suppression d'un étudiant");
                System.out.println("Choisir le étudiant à supprimer:");
                System.out.println("---------------------------");
                System.out.println("");
                ArrayList<String> Liste = new ArrayList<String>();
                Liste =
                        baseDonnees.traiterColonne(
                                "SELECT NumeroDossier, CodePermanent , nom "
                                        + "FROM etudiant "
                                        + "ORDER BY nom");
                int i = 0;
                for (String choix : Liste) {
                    System.out.print(++i);
                    System.out.print(") ");
                    System.out.println(choix);
                }
                System.out.println("---------------------------");
                System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                System.out.println();
                int selection;
                try {
                    selection = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    selection = -1;
                }
                while (selection < 1 || selection > Liste.size()) {
                    System.out.println("Mauvaise sélection");
                    System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                    try {
                        selection = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        selection = -1;
                    }
                }
                System.out.println(Liste.get(selection - 1));
                String etudiant = Liste.get(selection - 1).split(" ")[0];
                System.out.println(baseDonnees.traiterMiseajour(
                        "DELETE "
                                + "FROM etudiant "
                                + "WHERE NumeroDossier='"
                                + etudiant + "' ")
                );
                System.out.println("---------------------------");
                System.out.println("L'etudiant " + Liste.get(selection - 1) + " a été effacé");
                System.out.println("---------------------------");
            }
            break;
            /* ----------------------------------------------------------------------------------------- */
            case 5: {
                //  5. Inscription d’un étudiant à un cours"
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("Ajout d'une étudiant à un cours");
                System.out.println("Choisir le étudiant:");
                System.out.println("---------------------------");
                System.out.println("");
                ArrayList<String> Liste = new ArrayList<String>();
                Liste =
                        baseDonnees.traiterColonne(
                                "SELECT NumeroDossier, CodePermanent, nom "
                                        + "FROM etudiant "
                                        + "ORDER BY nom");
                int i = 0;
                for (String choix : Liste) {
                    System.out.print(++i);
                    System.out.print(") ");
                    System.out.println(choix);
                }
                System.out.println("---------------------------");
                System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                System.out.println();
                int selection;
                try {
                    selection = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    selection = -1;
                }
                while (selection < 1 || selection > Liste.size()) {
                    System.out.println("Mauvaise sélection");
                    System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                    try {
                        selection = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        selection = -1;
                    }
                }
                System.out.println(Liste.get(selection - 1));
                String NumeroDossier = Liste.get(selection - 1).split(" ")[0];
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("Ajout d'une étudiant à un cours");
                System.out.println("Choisir le cours:");
                System.out.println("---------------------------");
                System.out.println("");
                Liste =
                        baseDonnees.traiterColonne(
                                "SELECT NumeroCours, Titre "
                                        + "FROM cours "
                                        + "ORDER BY Titre");
                i = 0;
                for (String choix : Liste) {
                    System.out.print(++i);
                    System.out.print(") ");
                    System.out.println(choix);
                }
                System.out.println("---------------------------");
                System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                System.out.println();
                try {
                    selection = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    selection = -1;
                }
                while (selection < 1 || selection > Liste.size()) {
                    System.out.println("Mauvaise sélection");
                    System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                    try {
                        selection = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        selection = -1;
                    }
                }
                System.out.println(Liste.get(selection - 1));
                String NumeroCours = Liste.get(selection - 1).split(" ")[0];
                System.out.println(baseDonnees.traiterMiseajour(
                        "INSERT INTO etudiant_cours (NumeroDossier, NumeroCours) "
                                + "VALUES ("
                                + "" + NumeroDossier + ","
                                + "" + NumeroCours + ")"));

            }
            break;
            /* ----------------------------------------------------------------------------------------- */
            case 6: {
                // 6. Désinscription d’un étudiant à un cours
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("Désinscription d’un étudiant à un cour");
                System.out.println("Choisir le étudiant:");
                System.out.println("---------------------------");
                System.out.println("");
                ArrayList<String> Liste = new ArrayList<String>();
                Liste =
                        baseDonnees.traiterColonne(
                                "SELECT NumeroDossier, CodePermanent, nom "
                                        + "FROM etudiant "
                                        + "ORDER BY nom"
                        );
                int i = 0;
                for (String choix : Liste) {
                    System.out.print(++i);
                    System.out.print(") ");
                    System.out.println(choix);
                }
                System.out.println("---------------------------");
                System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                System.out.println();
                int selection;
                try {
                    selection = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    selection = -1;
                }
                while (selection < 1 || selection > Liste.size()) {
                    System.out.println("Mauvaise sélection");
                    System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                    try {
                        selection = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        selection = -1;
                    }
                }
                System.out.println(Liste.get(selection - 1));
                String NumeroDossier = Liste.get(selection - 1).split(" ")[0];
                System.out.println(" WoW " + NumeroDossier);
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("Désinscription d’un étudiant à un cour");
                System.out.println("Choisir le cours:");
                System.out.println("---------------------------");
                System.out.println("");
                Liste =
                        baseDonnees.traiterColonne(
                                "SELECT co.NumeroCours, co.Titre "
                                + "FROM cours as co "
                                + "INNER JOIN etudiant_cours as ec "
                                + "ON co.NumeroCours = ec.NumeroCours "
                                + "INNER JOIN etudiant as et "
                                + "ON ec.NumeroDossier = " + NumeroDossier
                                + " GROUP BY co.Titre "
                                + "ORDER BY co.Titre"
                        );
                i = 0;
                for (String choix : Liste) {
                    System.out.print(++i);
                    System.out.print(") ");
                    System.out.println(choix);
                }
                System.out.println("---------------------------");
                System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                System.out.println();
                try {
                    selection = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    selection = -1;
                }
                while (selection < 1 || selection > Liste.size()) {
                    System.out.println("Mauvaise sélection");
                    System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                    try {
                        selection = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        selection = -1;
                    }
                }
                System.out.println(Liste.get(selection - 1));
                String NumeroCours = Liste.get(selection - 1).split(" ")[0];
                System.out.println(baseDonnees.traiterMiseajour(
                        "DELETE "
                                + "FROM etudiant_cours "
                                + "WHERE NumeroCours ='"
                                + NumeroCours + "' ")
                );
            }
            break;
            /* ----------------------------------------------------------------------------------------- */
            case 7: {
                // 7. Entrée ou modification de la note d’un étudiant à un cours
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("Désinscription d’un étudiant à un cour");
                System.out.println("Choisir le étudiant:");
                System.out.println("---------------------------");
                System.out.println("");
                ArrayList<String> Liste = new ArrayList<String>();
                Liste =
                        baseDonnees.traiterColonne(
                                "SELECT NumeroDossier, CodePermanent, nom "
                                        + "FROM etudiant "
                                        + "ORDER BY nom"
                        );
                int i = 0;
                for (String choix : Liste) {
                    System.out.print(++i);
                    System.out.print(") ");
                    System.out.println(choix);
                }
                System.out.println("---------------------------");
                System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                System.out.println();
                int selection;
                try {
                    selection = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    selection = -1;
                }
                while (selection < 1 || selection > Liste.size()) {
                    System.out.println("Mauvaise sélection");
                    System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                    try {
                        selection = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        selection = -1;
                    }
                }
                System.out.println(Liste.get(selection - 1));
                String NumeroDossier = Liste.get(selection - 1).split(" ")[0];
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("Désinscription d’un étudiant à un cour");
                System.out.println("Choisir le cours:");
                System.out.println("---------------------------");
                System.out.println("");
                Liste =
                        baseDonnees.traiterColonne(
                                "SELECT co.NumeroCours, co.Titre "
                                        + "FROM cours as co "
                                        + "INNER JOIN etudiant_cours as ec "
                                        + "ON co.NumeroCours = ec.NumeroCours "
                                        + "INNER JOIN etudiant as et "
                                        + "ON ec.NumeroDossier = " + NumeroDossier
                                        + " GROUP BY co.Titre "
                                        + "ORDER BY co.Titre"
                        );
                i = 0;
                for (String choix : Liste) {
                    System.out.print(++i);
                    System.out.print(") ");
                    System.out.println(choix);
                }
                System.out.println("---------------------------");
                System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                System.out.println();
                try {
                    selection = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    selection = -1;
                }
                while (selection < 1 || selection > Liste.size()) {
                    System.out.println("Mauvaise sélection");
                    System.out.print("SVP, sélectionner une option de 1 à " + String.valueOf(Liste.size()));
                    try {
                        selection = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        selection = -1;
                    }
                }
                System.out.println(Liste.get(selection - 1));
                String NumeroCours = Liste.get(selection - 1).split(" ")[0];
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("ajout d'un étudiant\n");
                System.out.println("---------------------------");
                System.out.println();
                System.out.print("entrer la note du étudiant: ");
                Double note = sc.nextDouble();
                System.out.println(baseDonnees.traiterMiseajour(
                        "UPDATE etudiant_cours "
                        + "SET note = " + note
                        + " WHERE NumeroCours = " + NumeroCours)
                );
            }
            break;
        }
        String reponse = "";
        while (!reponse.equals("1")) {
            System.out.println("Entrez 1 pour retourner au menu principal");
            System.out.println();
            reponse = sc.nextLine();
        }
    }


    public static void main(String[] args) {
        new Menu().execute();

    }
}
