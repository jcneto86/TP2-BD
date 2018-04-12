import java.util.ArrayList;
import java.util.Scanner;

public class Menu {

    Scanner sc = new Scanner(System.in);
    Connect baseDonnees = new Connect();

    public Menu() {
    }

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


    private void menuPrincipal() {
        clear();
        System.out.println();
        System.out.println("\t\tMenu principal");
        System.out.println("\t-----------------------------------------------------");
        System.out.println("\t 1. Affichage de la liste de tous les patients");
        System.out.println("\t 2. Affichage de la liste de tous les patients allergiques à un médicament");
        System.out.println("\t 3. Ajout d’un nouveau patient");
        System.out.println("\t 4. Suppression d’un patient");
        System.out.println("\t 5. (à faire) Entrée d'un médicament");
        System.out.println("\t 6. (à faire) Suppression d'un médicament");
        System.out.println("\t 7. (à faire) Ajout d'une allergie à un patient");
        System.out.println("\t 8. Sortir de l'application");
        System.out.println("\t------------------------------------------------------");
        System.out.println();
        System.out.print("\tSVP, sélectionner une option de 1 à 8");
        System.out.println();
        System.out.println();
    }

    private void sousMenu(int menuPrincipalChoix) {
        clear();
        switch (menuPrincipalChoix) {
            case 1: {
                // 1. La liste de tous les patients
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("liste de tous les patients\n");
                System.out.println("Nom\t\t\tPrénom\t\t\tNuméro de Patient\tSexe\t\tAge\t\tQt Allergies");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
                System.out.println(baseDonnees.traiterLigne(
                        "SELECT p.nom,p.prenom,p.nopatient,p.sexe,p.age,COUNT(a.nomedicament) " +
                                "FROM patient AS p " +
                                "LEFT JOIN allergie AS a " +
                                "ON p.nopatient=a.nopatient " +
                                "GROUP BY p.nopatient " +
                                "ORDER BY p.nopatient;"));

            }
            break;

            case 2: {
                // 2. Affichage de la liste de tous les patients allergiques à un médicament
                System.out.println();
                ArrayList<String> Liste = new ArrayList<String>();
                Liste = baseDonnees.traiterColonne(
                        "SELECT description "
                                + "FROM medicament "
                                + "ORDER BY description");
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
                System.out.println("Patients allergiques au médicament " + Liste.get(selection - 1));
                System.out.println("---------------------------------------------------------------------------------------------------------------------------");
                System.out.println("Nom\t\t\tPrénom\t2\t\tNuméro de Patient\tSexe\t\t\tAge");
                System.out.println("---------------------------------------------------------------------------------------------------------------------------");
                System.out.println(baseDonnees.traiterLigne(
                        "SELECT p.nom,p.prenom,p.nopatient,p.sexe,p.age "
                                + "FROM patient AS p "
                                + "INNER JOIN allergie AS a "
                                + "ON p.nopatient=a.nopatient "
                                + "INNER JOIN medicament AS m "
                                + "ON a.nomedicament = m.nomedicament "
                                + "WHERE m.description='"
                                + Liste.get(selection - 1) + "' "
                                + "ORDER BY p.nopatient")
                );
                System.out.print("Age moyen: ");
                System.out.println(baseDonnees.traiterLigne(
                        "SELECT FORMAT(AVG(p.age),0) "
                                + "FROM patient AS p "
                                + "INNER JOIN allergie AS a "
                                + "ON p.nopatient=a.nopatient "
                                + "INNER JOIN medicament AS m "
                                + "ON a.nomedicament = m.nomedicament "
                                + "WHERE m.description='"
                                + Liste.get(selection - 1) + "' "
                                + "ORDER BY p.nopatient"
                ));


                System.out.println();
                System.out.println("---------------------------");

            }
            break;

            case 3: {
                // 3. Ajout d'un nouveau patient
                String nom = null;
                String prenom = null;
                String sexe = null;
                Integer age = null;
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("ajout d'un patient\n");
                System.out.println("---------------------------");
                System.out.println();
                System.out.print("entrer le nom du patient: ");
                nom = sc.nextLine();
                System.out.print("entrer le prénom du patient: ");
                prenom = sc.nextLine();
                System.out.print("entrer le sexe du patient : ");
                sexe = sc.nextLine();
                System.out.print("entrer l'âge du patient : ");
                try {
                    age = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    age = -1;
                }

                System.out.println(baseDonnees.traiterMiseajour(
                        "INSERT INTO patient (nom, prenom, sexe, age) "
                                + "VALUES ("
                                + "'" + nom + "',"
                                + "'" + prenom + "',"
                                + "'" + sexe + "',"
                                + "'" + age + "')"));

            }
            break;

            case 4: {
                // 4. Suppression d'un patient
                System.out.println();
                System.out.println("---------------------------");
                System.out.println("Suppression d'un patient");
                System.out.println("Choisir le patient à supprimer:");
                System.out.println("---------------------------");
                System.out.println("");
                ArrayList<String> Liste = new ArrayList<String>();
                Liste =
                        baseDonnees.traiterColonne(
                                "SELECT nopatient, nom , prenom "
                                        + "FROM patient "
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
                String patient = Liste.get(selection - 1).split(" ")[0];
                System.out.println(baseDonnees.traiterMiseajour(
                        "DELETE "
                                + "FROM patient "
                                + "WHERE nopatient='"
                                + patient + "' ")
                );
                System.out.println("---------------------------");
                System.out.println("Le patient " + Liste.get(selection - 1) + " a été effacé");
                System.out.println("---------------------------");

            }
            break;


            case 5: {
                //  5. (à faire) Entrée d'un médicament"


            }
            break;


            case 6: {
                // 6. (à faire) Suppression d'un médicament


            }
            break;


            case 7: {

                // 7. (à faire) Ajout d'une allergie à un patient

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
