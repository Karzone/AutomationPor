package Utilities;

import com.sun.mail.imap.IMAPFolder;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by kalaiyak on 05/01/2018.
 * Copyrights : KCOM
 */
public class ReadEmail {
    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol","imaps");

            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect("smtp.gmail.com","kcomportaltest@gmail.com","EverythingIsOrange");

            IMAPFolder folder = (IMAPFolder) store.getFolder("inbox");
            folder.open(Folder.READ_ONLY);

            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen,false);
            Message message[] = folder.search(unseenFlagTerm);


            int j = message.length-1;
            for (int i=j;i>=0;i--) {
                System.out.println("Message " + (i + 1));
                System.out.println("From : " + message[i].getFrom());
                System.out.println("Subject : " + message[i].getSubject());
                try {
                    System.out.println("Body: " + message[i].getContent());
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }

            folder.close(false);
            store.close();
        }
        catch (MessagingException e) {
            System.out.println("Error: " + e);
        }
    }
}
