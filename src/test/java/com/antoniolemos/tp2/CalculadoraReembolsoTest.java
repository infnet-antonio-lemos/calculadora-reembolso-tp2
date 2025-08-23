package com.antoniolemos.tp2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CalculadoraReembolsoTest {
    public CalculadoraReembolso calculadoraReembolso;
    public HistoricoConsultas historicoConsultas;

    @Mock
    Paciente paciente;

    @Mock
    Auditoria auditoria;

    @BeforeEach
    public void setup() {
        this.historicoConsultas = new FakeHistoricoConsulta(this.auditoria);
        this.calculadoraReembolso = new CalculadoraReembolso(this.historicoConsultas);
    }


    PlanoSaudeStub planoSaudeStubBasico = new PlanoSaudeStub(0.5);
    PlanoSaudeStub planoSaudeStubPremium = new PlanoSaudeStub(0.8);

    @ParameterizedTest
    @CsvSource({
            "200.0, 0.7, 140.0",
            "150.0, 0.0, 0.0",
            "100.0, 1.0, 100.0",
    })
    public void deveCalcularReembolso(double value, double percentage, double expected) {
        double result = this.calculadoraReembolso.calcularReembolso(value, percentage, paciente);
        assertEquals(expected, result);
    }

    @Test
    public void deveCalcularReembolsoPlanoBasico() {
        double valor = 200.0;
        double expected = 100.0;
        double result = this.calculadoraReembolso.calcularReembolso(valor, planoSaudeStubBasico.percentualCobertura(), paciente);
        assertEquals(expected, result);
    }

    @Test
    public void deveCalcularReembolsoPlanoPremium() {
        double valor = 200.0;
        double expected = 160.0;
        double result = this.calculadoraReembolso.calcularReembolso(valor, planoSaudeStubPremium.percentualCobertura(), paciente);
        assertEquals(expected, result);
    }

    @Test
    public void deveRegistrarConsultaAoCalcularReembolso() {
        double valor = 0;
        double expected = 0;
        double result = this.calculadoraReembolso.calcularReembolso(valor, planoSaudeStubBasico.percentualCobertura(), paciente);
        assertEquals(expected, result);
    }

    @Test
    public void deveAuditarAoRegistrarConsulta() {
        calculadoraReembolso.calcularReembolso(1.0, 1.0, paciente);
        verify(auditoria).registrarConsulta();
    }

    @Test
    public void deveRetornarExcecaoAoTentarReembolsarConsultaNaoAutorizada() {
        assertThrows(Exception.class, () -> )
    }
}
