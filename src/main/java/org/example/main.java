package org.example;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class main {
    public static void main(String[] args) {
        // Paramètres de connexion IMAP pour Outlook
        String serveurIMAP = "imap.gmail.com";
        String utilisateur = "kenza.bouzerna1@gmail.com";
        String motDePasse = "dwlb oxdc ampo fhzl";

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");

        try {
            // Créer une session de messagerie
            Session session = Session.getInstance(props, null);

            // Se connecter au serveur IMAP
            Store store = session.getStore();
            store.connect(serveurIMAP, utilisateur, motDePasse);

            // Ouvrir la boîte aux lettres
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Récupérer les messages
            Message[] messages = inbox.getMessages();

            // Afficher les détails de chaque message
            for (Message message : messages) {
                System.out.println("Sujet : " + message.getSubject());
                System.out.println("Expéditeur : " + InternetAddress.toString(message.getFrom()));
                System.out.println("Date : " + message.getSentDate());
                System.out.println("--------------------------------------------------");
            }

            // Fermer la boîte aux lettres
            inbox.close(false);

            // Fermer la connexion
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}