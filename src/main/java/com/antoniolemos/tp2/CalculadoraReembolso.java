package com.antoniolemos.tp2;

public class CalculadoraReembolso {
    private HistoricoConsultas historicoConsultas;
    public CalculadoraReembolso(HistoricoConsultas historicoConsultas) {
        this.historicoConsultas = historicoConsultas;
    }
    public double calcularReembolso(double valor, double percentual, Paciente paciente) {
        this.historicoConsultas.adicionarConsulta(new Consulta(paciente, java.time.LocalDateTime.now()));
        return valor * percentual;
    }
}
