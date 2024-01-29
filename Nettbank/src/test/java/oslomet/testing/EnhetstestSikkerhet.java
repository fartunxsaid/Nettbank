package oslomet.testing;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpSession;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Sikkerhet.Sikkerhet;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestSikkerhet {
    @InjectMocks
    // denne skal testes
    private Sikkerhet sikkerhetsController;

    @Mock
    // denne skal Mock'es
    private BankRepository repository;

    @Mock
    // denne skal Mock'es
    private MockHttpSession session;

    @Before
    public void initSession(){
        Map<String,Object> attributes = new HashMap<String,Object>();

        doAnswer(new Answer<Object>(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                return attributes.get(key);
            }
        }).when(session).getAttribute(anyString());

        doAnswer(new Answer<Object>(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                Object value = invocation.getArguments()[1];
                attributes.put(key, value);
                return null;
            }
        }).when(session).setAttribute(anyString(), any());

    }
    @Test
    public void test_SjekkLoggetInnOK() {
        // arrange
        when(repository.sjekkLoggInn(anyString(),anyString())).thenReturn("OK");

        // act
        String resultat = sikkerhetsController.sjekkLoggInn("12345678901",
                "HeiHei");

        // assert
        assertEquals("OK", resultat);
    }
    @Test
    public void test_sjekkLoggInnIkkeOk(){

        // act
        String resultat = sikkerhetsController.sjekkLoggInn("01010110523688", "HeiHei12345");

        // assert
        assertEquals("Feil i personnummer", resultat);
    }
    @Test
    public void test_LoggInnOk(){
        //arrange
        session.setAttribute("Innlogget", "12345678901");
        //act
        String resultat= sikkerhetsController.loggetInn();
        //assert
        assertEquals("12345678901",resultat);
    }
    @Test
    public void test_LoggInnIkkeOk(){
        // arrange
        session.setAttribute("Innlogget", null);
        //act
        String resultat= sikkerhetsController.loggetInn();
        //assert
        assertNull(resultat);
    }
    @Test
    public void test_LoggUtOk() {
        // arrange
        session.setAttribute("Innlogget", "12345678901");
        // act
        sikkerhetsController.loggUt();
        String resultat = (String) session.getAttribute("Innlogget");
        // assert
        assertNull(resultat);
    }

    @Test
    public void test_LoggInnAdminOk(){
        // arrange
        session.setAttribute("Innlogget", "Admin");
        //act
        String resultat= sikkerhetsController.loggInnAdmin("Admin","Admin");
        //assert
        assertEquals("Logget inn",resultat);
    }
    @Test
    public void test_LoggInnAdminIkkeOk(){
        // arrange
        session.setAttribute("Innlogget", null);
        //act
        String resultat= sikkerhetsController.loggInnAdmin(anyString(),anyString());
        //assert
        assertEquals("Ikke logget inn",resultat);
    }

}
