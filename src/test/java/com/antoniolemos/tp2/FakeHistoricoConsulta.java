package com.antoniolemos.tp2;

import java.util.ArrayList;
import java.util.List;

public class FakeHistoricoConsulta implements HistoricoConsultas {
    private Auditoria auditoria;
    private final List<Consulta> consultas = new ArrayList<>();

    public FakeHistoricoConsulta(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    @Override
    public void adicionarConsulta(Consulta consulta) {

        this.consultas.add(consulta);
        this.auditoria.registrarConsulta();

    }

    @Override
    public List<Consulta> listarConsultas() {
        return this.consultas;
    }
}
