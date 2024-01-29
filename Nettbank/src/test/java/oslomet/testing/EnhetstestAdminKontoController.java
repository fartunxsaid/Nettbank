package oslomet.testing;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKontoController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Sikkerhet.Sikkerhet;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class EnhetstestAdminKontoController {
    @InjectMocks
    // denne skal testes
    private AdminKontoController AdminKontoController;

    @Mock
    // denne skal Mock'es
    private AdminRepository repository;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;
        @Test
        public void test_HentAlleKontiOk() {
            // arrange

            List<Konto> kontoList = new ArrayList<>();

            Konto konto1 = new Konto("01010110523", "105010123456",
                    720, "Lønnskonto", "NOK", null);

            kontoList.add(konto1);

            when(sjekk.loggetInn()).thenReturn("01010110523");
            when(repository.hentAlleKonti()).thenReturn(kontoList);
            //act
            List<Konto> resultat = AdminKontoController.hentAlleKonti();
            //assert
            assertEquals(kontoList, resultat);
        }

        @Test
        public void test_HentAlleIkkeOK() {
            // arrange
            when(sjekk.loggetInn()).thenReturn(null);
            //act
            List<Konto> resultat = AdminKontoController.hentAlleKonti();
            //assert
            assertNull(resultat);
        }

        @Test
        public void test_RegOK() {
            //arrange
            Konto konto1 = new Konto("01010110523", "105010123456",
                    720, "Lønnskonto", "NOK", null);

            when(sjekk.loggetInn()).thenReturn("105010123456");

            when(repository.registrerKonto(konto1)).thenReturn("OK");

            // act
            String resultat = AdminKontoController.registrerKonto(konto1);

            // assert
            assertEquals("OK", resultat);

        }

        @Test
        public void test_RegIkkeOK() {
            //arrange
            Konto konto1 = new Konto("01010110523", "105010123456",
                    720, "Lønnskonto", "NOK", null);

            when(sjekk.loggetInn()).thenReturn(null);

            // act
            String resultat = AdminKontoController.registrerKonto(konto1);

            // assert
            assertEquals("Ikke innlogget", resultat);

        }
        @Test
        public void test_endreOK() {
            //arrange
            Konto konto1 = new Konto("01010110523", "105010123456",
                    720, "Lønnskonto", "NOK", null);

            when(sjekk.loggetInn()).thenReturn("01010110523");
            when(repository.endreKonto(any(Konto.class))).thenReturn("OK");
            //act
            String resultat = AdminKontoController.endreKonto(konto1);
            //assert
            assertEquals("OK", resultat);
        }
        @Test
        public void test_endreIkkeOK() {
            //arrange
            Konto konto1 = new Konto("01010110523", "105010123456",
                    720, "Lønnskonto", "NOK", null);

            when(sjekk.loggetInn()).thenReturn(null);
            //act
            String resultat = AdminKontoController.endreKonto(konto1);

            //assert
            assertEquals("Ikke innlogget", resultat);
        }
        @Test
        public void test_SletteOK(){
            // arrange
            Konto konto1 = new Konto("01010110523", "105010123456",
                    720, "Lønnskonto", "NOK", null);

            when(sjekk.loggetInn()).thenReturn(konto1.getPersonnummer());
            when(repository.slettKonto(konto1.getKontonummer())).thenReturn("OK");
            //act
            String resultat = AdminKontoController.slettKonto(konto1.getKontonummer());
            //assert
            assertEquals("OK", resultat);
        }
        @Test
        public void test_SletteIkkeOK(){
            // arrange
            Konto konto1 = new Konto("01010110523", "105010123456",
                    720, "Lønnskonto", "NOK", null);
            when(sjekk.loggetInn()).thenReturn(null);
            //act
            String resultat = AdminKontoController.slettKonto(konto1.getKontonummer());
            //assert
            assertEquals("Ikke innlogget",resultat);
    }
    }


