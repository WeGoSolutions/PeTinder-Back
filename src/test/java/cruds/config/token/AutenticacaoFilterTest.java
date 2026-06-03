package cruds.config.token;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class AutenticacaoFilterTest {

    private UserDetailsService autenticacaoService;
    private GerenciadorTokenJwt jwtTokenManager;
    private AutenticacaoFilter filter;

    @BeforeEach
    void setUp() {
        autenticacaoService = mock(UserDetailsService.class);
        jwtTokenManager = mock(GerenciadorTokenJwt.class);
        filter = new AutenticacaoFilter(autenticacaoService, jwtTokenManager);
    }

    @Test
    @DisplayName("Deve ignorar token quando usuário não existir mais")
    void deveIgnorarTokenQuandoUsuarioNaoExistirMais() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer token-valido-mas-desatualizado");
        when(jwtTokenManager.getUsernameFromToken("token-valido-mas-desatualizado")).thenReturn("desconhecido@email.com");
        when(autenticacaoService.loadUserByUsername("desconhecido@email.com"))
                .thenThrow(new UsernameNotFoundException("Usuário não encontrado"));

        assertDoesNotThrow(() -> filter.doFilter(request, response, filterChain));

        verify(autenticacaoService).loadUserByUsername("desconhecido@email.com");
        verify(jwtTokenManager, never()).validateToken(anyString(), any(UserDetails.class));
        verify(filterChain).doFilter(request, response);
    }
}
