import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirestoreConfiguration {

    @PostConstruct
    void init() throws IOException {
        FileInputStream serviceAccount = null;
        serviceAccount = new FileInputStream("D:/subteno-vuo-ec8815830975.json");

        System.out.println(serviceAccount);
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("subteno-vuo.appspot.com")
                .build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

    }

}