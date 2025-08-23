package com.antoniolemos.tp2;

public class PlanoSaudeStub implements PlanoSaude {
    private double percentual;

    public PlanoSaudeStub(double percentual) {
        this.percentual = percentual;
    }

    @Override
    public double percentualCobertura() {
        return percentual;
    }
}
