package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.BankController;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Models.Transaksjon;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestBankController {

    @InjectMocks
    // denne skal testes
    private BankController bankController;

    @Mock
    // denne skal Mock'es
    private BankRepository repository;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;

    @Test
    public void hentKundeInfo_loggetInn() {

        // arrange
        Kunde enKunde = new Kunde("01010110523", "01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKundeInfo(anyString())).thenReturn(enKunde);

        // act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertEquals(enKunde, resultat);

    }

    @Test
    public void hentKundeInfo_IkkeloggetInn() {

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertNull(resultat);
    }

    @Test
    public void hentKonti_LoggetInn()  {
        // arrange
        List<Konto> konti = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        konti.add(konto1);
        konti.add(konto2);

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKonti(anyString())).thenReturn(konti);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertEquals(konti, resultat);
    }

    @Test
    public void hentKonti_IkkeLoggetInn()  {
        // arrange

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertNull(resultat);
    }

    @Test
    public void utforBetaling_OK() {
        // arrange
        List<Transaksjon> betalinger = new ArrayList<>();
        Transaksjon enTransaksjon = new Transaksjon(2, "20102012345", 400.4, "2015-03-20", "Skagen", "1", "105010123456");

        betalinger.add(enTransaksjon);

        Konto konto = new Konto("05068924604", "41925811793",
                13495.41, "Brukskonto", "NOK", betalinger);

        konto.setTransaksjoner(betalinger);

        when(sjekk.loggetInn()).thenReturn(konto.getPersonnummer());

        when(repository.utforBetaling(enTransaksjon.getTxID())).thenReturn("OK");
        when(repository.hentBetalinger(anyString())).thenReturn(betalinger);

        // act

        List<Transaksjon> resultat = bankController.utforBetaling(enTransaksjon.getTxID());

        // assert
        assertEquals(betalinger, resultat);
    }
    @Test
    public void utforBetaling_ikkeOK() {

        // arrange
        List<Transaksjon> betaling = new ArrayList<>();
        Transaksjon enBetaling = new Transaksjon(2, "20102012345", 400.4, "2015-03-20", "Skagen",
                "1", "105010123456");

        Konto konto = new Konto("05068924604", "41925811793",
                13495.41, "Brukskonto", "NOK", betaling);

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Transaksjon> resultat = bankController.utforBetaling(enBetaling.getTxID());

        // assert
        assertNull((resultat));
    }
    @Test
    public void  endre_OK() {
        // arrange
        Kunde kunde = new Kunde("01010110523", "Lene", "Jensen",
                "Askerveien 22", "3270", "Oslo", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn(kunde.getPersonnummer());

        when(repository.endreKundeInfo(any(Kunde.class))).thenReturn(kunde.getPersonnummer());

        // act
        String resultat = bankController.endre(kunde);

        // assert
        assertEquals(kunde.getPersonnummer(), resultat);
    }
    @Test
    public void  endre_ikkeOK() {
        // arrange
        Kunde kunde = new Kunde("01010110523", "Lene", "Jensen",
                "Askerveien 22", "3270", "Oslo", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        String resultat = bankController.endre(kunde);

        // assert
        assertNull(resultat);
    }
    @Test
    public void hentBetalinger_OK() {

        // arrange
        List<Transaksjon> transaksjoner = new ArrayList<>();
        Transaksjon transaksjon = new Transaksjon(2, "123456789101", 23.5,
                "2012-03-11", "send", "1", "23456789101");
        transaksjoner.add(transaksjon);

        Konto konto1 = new Konto("115111133557", "02020211533",
                800, "Lønnskonto", "NOK", transaksjoner);

        when(sjekk.loggetInn()).thenReturn("115111133557");
        when(repository.hentBetalinger(konto1.getPersonnummer())).thenReturn(transaksjoner);

        // act
        List<Transaksjon> resultat = bankController.hentBetalinger();

        // assert
        assertEquals(transaksjoner, resultat);
    }
    @Test
    public void hentBetalinger_ikkeOK() {

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Transaksjon> resultat = bankController.hentBetalinger();

        // assert
        assertNull(resultat);
    }
    @Test
    public void registrerbetaling_OK() {
        // arrange
        List<Transaksjon> konto1transaksjoner = new ArrayList<>();
        Transaksjon enTransaksjon = new Transaksjon(2, "20102012345", 400.4, "2015-03-20", "Skagen", "1",
                "105010123456");

        konto1transaksjoner.add(enTransaksjon);

        Konto konto1 = new Konto("05068924604", "41925811793",
                13495.41, "Brukskonto", "NOK", konto1transaksjoner);

        when(sjekk.loggetInn()).thenReturn(konto1.getPersonnummer());
        when(repository.registrerBetaling(enTransaksjon)).thenReturn("OK");

        // act
        String resultat = bankController.registrerBetaling(enTransaksjon);

        // assert
        assertEquals("OK", resultat);
    }

    @Test
    public void registrerbetaling_ikkeOK() {

        // arrange
        Transaksjon enTransaksjon = new Transaksjon(2, "20102012345", 400.4, "2015-03-20", "Skagen", "1",
                "105010123456");

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        String resultat = bankController.registrerBetaling(enTransaksjon);

        // assert
        assertNull(resultat);
    }
    @Test
    public void hentSaldi_loggetInn() {

        // arrange
        List<Konto> saldi = new ArrayList<>();
        Konto konto3 = new Konto("115111133557", "02020211533",
                800, "Lønnskonto", "NOK", null);
        saldi.add(konto3);

        when(sjekk.loggetInn()).thenReturn("115111133557");

        when(repository.hentSaldi(anyString())).thenReturn(saldi);

        // act
        List<Konto> resultat = bankController.hentSaldi();

        // assert
        assertEquals(saldi, resultat);
    }
    @Test
    public void hentSaldi_IkkeLoggetInn() {
        // arrange

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentSaldi();

        // assert
        assertNull(resultat);
    }

    @Test
    public void test_initDB(){

        when(repository.initDB(any())).thenReturn("OK");

        String resultat = bankController.initDB();

        assertEquals("OK", resultat);
    }
}

