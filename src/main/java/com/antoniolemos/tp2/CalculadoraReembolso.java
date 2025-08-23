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
        double reembolso = valor * percentual;
        double limit = 150.0;
        if (reembolso > limit) {
            return limit;
        }
        return reembolso;
    }

    public void solicitarReembolso(Paciente paciente) throws Exception {
        if (!this.autorizadorReembolso.autorizarReembolso(paciente)) {
            throw new Exception("Reembolso não autorizado");
        }
        return;
    }
}
