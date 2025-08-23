package com.antoniolemos.tp2;

import java.time.LocalDateTime;

public class Consulta {
    public Paciente paciente;
    public LocalDateTime dataHora;

    public Consulta(Paciente paciente, LocalDateTime dataHora) {
        this.paciente = paciente;
        this.dataHora = dataHora;
    }
}
