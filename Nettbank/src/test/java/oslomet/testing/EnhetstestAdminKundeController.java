package oslomet.testing;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKundeController;
import oslomet.testing.API.BankController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class EnhetstestAdminKundeController {
    @InjectMocks
    // denne skal testes
    private AdminKundeController AdminKundeController;

    @Mock
    // denne skal Mock'es
    private AdminRepository repository;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;

    @Test
    public void test_HentAlleOk(){
        // arrange
        List<Kunde> kundeList = new ArrayList<>();

        Kunde kunde1= new Kunde("01010110523","Lene","Jensen","Askerveien 22","3270","22224444","HeiHei");
        Kunde kunde2=new Kunde("12345678901","Per","Hansen","Osloveien 82","1234","12345678","HeiHei");

        kundeList.add(kunde1);
        kundeList.add(kunde2);

        when(repository.hentAlleKunder()).thenReturn(kundeList);
        when(sjekk.loggetInn()).thenReturn("01010110523");

        // act
        List<Kunde> resultat = AdminKundeController.hentAlle();
        //assert
        assertEquals(kundeList, resultat);
    }
    @Test
    public void test_HentAlleIkkeOK(){
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);
        //act
        List<Kunde> resultat = AdminKundeController.hentAlle();
        //assert
        assertNull(resultat);
    }
    @Test
    public void test_LagreOK(){
        // arrange
        Kunde kunde1= new Kunde("01010110523","Lene","Jensen","Askerveien 22","3270","22224444","HeiHei");
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.registrerKunde(kunde1)).thenReturn("OK");
        //act
        String resultat = AdminKundeController.lagreKunde(kunde1);
        //assert
        assertEquals("OK", resultat);
    }
    @Test
    public void test_LagreIkkeOK(){
        // arrange
        Kunde kunde1= new Kunde("01010110523","Lene","Jensen","Askerveien 22","3270","22224444","HeiHei");
        when(sjekk.loggetInn()).thenReturn(null);
        //act
        String resultat = AdminKundeController.lagreKunde(kunde1);
        //assert
        assertEquals("Ikke logget inn",resultat);
    }
    @Test
    public void test_EndreOK(){
        // arrange
        Kunde enkunde= new Kunde("01010110523","Lene","Jensen","Askerveien 22","3270","22224444","HeiHei");
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.endreKundeInfo(enkunde)).thenReturn("OK");        //act
        String resultat = AdminKundeController.endre(enkunde);
        //assert
        assertEquals("OK", resultat);
    }
    @Test
    public void test_EndreIkkeOK(){
        // arrange
        Kunde enkunde= new Kunde("01010110523","Lene","Jensen","Askerveien 22","3270","22224444","HeiHei");
        when(sjekk.loggetInn()).thenReturn(null);
        //act
        String resultat = AdminKundeController.endre(enkunde);
        //assert
        assertEquals("Ikke logget inn",resultat);
    }
    @Test
    public void test_SletteOK(){
        // arrange
        when(sjekk.loggetInn()).thenReturn("01010110523");
        when(repository.slettKunde(anyString())).thenReturn("OK");
        //act
        String resultat = AdminKundeController.slett("01010110523");
        //assert
        assertEquals("OK", resultat);
    }
    @Test
    public void test_SletteIkkeOK(){
        // arrange
        when(sjekk.loggetInn()).thenReturn(null);
        //act
        String resultat = AdminKundeController.slett("01010110523");
        //assert
        assertEquals("Ikke logget inn",resultat);
    }

}

