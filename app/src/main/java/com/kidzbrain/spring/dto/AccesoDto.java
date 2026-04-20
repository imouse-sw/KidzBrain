package com.kidzbrain.spring.dto;

public class AccesoDto {
    private Integer usuarioId;
    private String accionRealizada;

    public AccesoDto(Integer usuarioId, String accionRealizada) {
        this.usuarioId = usuarioId;
        this.accionRealizada = accionRealizada;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getAccionRealizada() {
        return accionRealizada;
    }

    public void setAccionRealizada(String accionRealizada) {
        this.accionRealizada = accionRealizada;
    }
}
