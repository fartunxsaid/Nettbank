package oslomet.testing;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKontoController;
import oslomet.testing.API.AdminKundeController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Sikkerhet.Sikkerhet;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        public void test_LagreOK() {


        }

        @Test
        public void test_LagreIkkeOK() {


        }
    }


