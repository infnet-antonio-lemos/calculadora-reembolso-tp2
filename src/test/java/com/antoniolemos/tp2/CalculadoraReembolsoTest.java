package com.antoniolemos.tp2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CalculadoraReembolsoTest {
    public CalculadoraReembolso calculadoraReembolso;
    public HistoricoConsultas historicoConsultas;

    /**
     * Mocks para as dependências da CalculadoraReembolso.
     * Os mocks ajudam a isolar o comportamento da classe em teste.
     * São melhores usados em cenários onde a implementação real é complexa ou externa.
     */
    @Mock
    Paciente paciente;

    @Mock
    Auditoria auditoria;

    @Mock
    AutorizadorReembolso autorizadorReembolso;

    /**
     * Setup antes de cada teste para inicializar a CalculadoraReembolso com as dependências mockadas.
     */
    @BeforeEach
    public void setup() {
        this.historicoConsultas = new FakeHistoricoConsulta(this.auditoria);
        this.calculadoraReembolso = new CalculadoraReembolso(this.historicoConsultas, this.autorizadorReembolso);
    }

    /**
     * Stubs para planos de saúde com diferentes percentuais de cobertura.
     */
    PlanoSaudeStub planoSaudeStubBasico = new PlanoSaudeStub(0.5);
    PlanoSaudeStub planoSaudeStubPremium = new PlanoSaudeStub(0.8);

    /**
     * Função auxiliar para criar uma consulta com o paciente mockado e a data atual.
     */
    private Consulta criarConsulta() {
        return new Consulta(paciente, java.time.LocalDateTime.now());
    }

    /**
     * Função auxiliar para comparar valores double com uma margem de erro.
     * @param expected
     * @param actual
     */
    private void assertEqualsComMargem(double expected, double actual) {
        double delta = 0.01;
        assertEquals(expected, actual, delta);
    }

    /**
     * Teste parametrizado para calcular reembolso com diferentes valores e percentuais.
     * @param value
     * @param percentage
     * @param expected
     */
    @ParameterizedTest
    @CsvSource({
            "200.0, 0.7, 140.0",
            "150.0, 0.0, 0.0",
            "100.0, 1.0, 100.0",
    })
    public void deveCalcularReembolso(double value, double percentage, double expected) {
        double result = this.calculadoraReembolso.calcularReembolso(value, percentage, this.criarConsulta());
        assertEqualsComMargem(expected, result);
    }

    /**
     * Testes específico para planos de saúde básico.
     */
    @Test
    public void deveCalcularReembolsoPlanoBasico() {
        double valor = 200.0;
        double expected = 100.0;
        double result = this.calculadoraReembolso.calcularReembolso(valor, planoSaudeStubBasico.percentualCobertura(), this.criarConsulta());
        assertEqualsComMargem(expected, result);
    }

    /**
     * Testes específico para planos de saúde premium.
     */
    @Test
    public void deveCalcularReembolsoPlanoPremium() {
        double valor = 200.0;
        double expected = 150.0;
        double result = this.calculadoraReembolso.calcularReembolso(valor, planoSaudeStubPremium.percentualCobertura(), this.criarConsulta());
        assertEqualsComMargem(expected, result);
    }

    /**
     * Teste para verificar se a consulta é registrada ao calcular o reembolso.
     */
    @Test
    public void deveRegistrarConsultaAoCalcularReembolso() {
        double valor = 0;
        this.calculadoraReembolso.calcularReembolso(valor, planoSaudeStubBasico.percentualCobertura(), this.criarConsulta());
        List<Consulta> consultas = this.historicoConsultas.listarConsultas();
        assertEquals(1, consultas.size());
    }

    /**
     * Teste para verificar se a auditoria registra a consulta ao calcular o reembolso.
     */
    @Test
    public void deveAuditarAoRegistrarConsulta() {
        calculadoraReembolso.calcularReembolso(1.0, 1.0, this.criarConsulta());
        // o verify é uma forma de spy já embutida nos mocks do Mockito
        verify(auditoria).registrarConsulta();
    }

    /**
     * Teste para verificar se uma exceção é lançada ao solicitar reembolso sem autorização.
     */
    @Test
    public void deveLancarExcecaoAoSolicitarReembolsoSemAutorizacao() {
        when(autorizadorReembolso.autorizarReembolso(paciente)).thenReturn(false);
        assertThrows(Exception.class, () -> calculadoraReembolso.solicitarReembolso(paciente));
    }

    /**
     * Teste para verificar se o reembolso é limitado a 150.
     */
    @Test
    public void deveLimitarReembolsoPara150() {
        double valor = 400_000.0;
        double expected = 150.0;
        double result = this.calculadoraReembolso.calcularReembolso(valor, this.planoSaudeStubBasico.percentualCobertura(), this.criarConsulta());
        assertEqualsComMargem(expected, result);
    }

    /**
     * Teste completo para calcular reembolso, registrar consulta e auditar.
     */
    @Test
    public void deveCalcularReembolsoTesteCompleto() {
        double valor = 200.0;
        double expected = 100.0;
        double result = this.calculadoraReembolso.calcularReembolso(valor, planoSaudeStubBasico.percentualCobertura(), this.criarConsulta());
        List<Consulta> consultas = this.historicoConsultas.listarConsultas();
        assertEqualsComMargem(expected, result);
        assertEquals(1, consultas.size());
        verify(auditoria).registrarConsulta();
    }
}
