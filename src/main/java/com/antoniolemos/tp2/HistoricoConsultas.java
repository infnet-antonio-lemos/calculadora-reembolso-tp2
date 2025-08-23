package com.antoniolemos.tp2;

import java.util.List;

public interface HistoricoConsultas {
    void adicionarConsulta(Consulta consulta);
    List<Consulta> listarConsultas();
}
