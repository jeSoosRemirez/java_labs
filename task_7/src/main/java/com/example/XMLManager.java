package task_7.src.main.java.com.example;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLManager {
    public static void main(String[] args) {
        try {
            Football football = new Football();
            Team team1 = new Team("Team1");
            team1.addPlayer(new Player("Player1"));
            team1.addPlayer(new Player("Player2"));

            Team team2 = new Team("Team2");
            team2.addPlayer(new Player("Player3"));
            team2.addPlayer(new Player("Player4"));

            football.addTeam(team1);
            football.addTeam(team2);

            // Запис у XML файл
            marshalToXML(football, "football.xml");

            // Читання з XML файлу
            Football readFootball = unmarshalFromXML("football.xml");
            System.out.println(readFootball);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static void marshalToXML(Football football, String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Football.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(football, new File(filePath));
    }

    private static Football unmarshalFromXML(String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Football.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Football) unmarshaller.unmarshal(new File(filePath));
    }
}
