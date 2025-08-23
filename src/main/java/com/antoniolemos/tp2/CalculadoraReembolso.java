package com.antoniolemos.tp2;

public class CalculadoraReembolso {
    private HistoricoConsultas historicoConsultas;
    private AutorizadorReembolso autorizadorReembolso;
    public CalculadoraReembolso(HistoricoConsultas historicoConsultas, AutorizadorReembolso autorizadorReembolso) {
        this.historicoConsultas = historicoConsultas;
        this.autorizadorReembolso = autorizadorReembolso;
    }
    public double calcularReembolso(double valor, double percentual, Consulta consulta) {
        this.historicoConsultas.adicionarConsulta(consulta);
        return valor * percentual;
    }

    public void solicitarReembolso(Paciente paciente) throws Exception {
        if (!this.autorizadorReembolso.autorizarReembolso(paciente)) {
            throw new Exception("Reembolso não autorizado");
        }
        return;
    }
}
