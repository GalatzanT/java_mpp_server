    package tudor.start;

    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.ComponentScan;
    import org.springframework.context.annotation.Primary;
    import persistence.DB.dbMatchRepo;
    import persistence.MatchRepo;

    import java.io.File;
    import java.io.FileReader;
    import java.io.IOException;
    import java.util.Properties;

    @ComponentScan({"tudor.service.rest", "persistence"})
    @SpringBootApplication
    public class StartRestServices {
        public static void main(String[] args) {

            SpringApplication.run(StartRestServices.class, args);
        }

        @Bean(name="props")
        @Primary
        public Properties getBdProperties(){
            Properties props = new Properties();
            try {
                System.out.println("Searching bd.config in directory "+((new File(".")).getAbsolutePath()));
                props.load(new FileReader("bd.config"));
                System.out.println("Properties loaded: " + props);
            } catch (IOException e) {
                System.err.println("Configuration file bd.cong not found" + e);

            }
            return props;
        }
    }
